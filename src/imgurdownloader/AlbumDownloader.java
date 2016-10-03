/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A driver class which does the work of downloading the Imgur Album. Files will
 * be named [number].[filename] when downloaded. 
 * @author zacke
 */
public class AlbumDownloader {
    //A regex to check the link is a valid album
    public static final Pattern IMGUR_REGEX = 
            Pattern.compile("(?<protocol>https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/(a|gallery)/(?<imgurID>[a-zA-Z0-9]+)(#[0-9]+)?");
    //A regex to identify images in the HTML
    public static final Pattern IMG_REGEX = Pattern.compile("id=\"(?<link>.+?)\"");
    
    private Path target;
    private String albumID;
    
    /**
     * Constructs a new AlbumDownloader
     * @param url The url of the album to downlaod from
     * @param target The directory to place the downloaded images in.
     */
    public AlbumDownloader(String url,Path target){
        getAlbumID(url);
        this.target = target;
    }
    
    /**
     * Gets the albumID from the full link
     * @param url The full link to the Imgur album
     */
    private void getAlbumID(String url){
        Matcher match = IMGUR_REGEX.matcher(url);
        if(match.matches()){
            this.albumID = match.group("imgurID");
        }
        else{
            throw new IllegalArgumentException("That is not an imgur link: " + url);
        }
    }
    /**
     * Retrieves an ArrayList containing all the files in the album
     * @return 
     */
    private ArrayList<String> getImages(){
        ArrayList<String> imgLinks = new ArrayList<>();
        String link = "http://imgur.com/a/" + albumID + "/layout/blog";
         URL webpage;
        try {
            webpage = new URL(link);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("albumID must be an imgur album ID");
        }
        try (InputStream stream = webpage.openStream()){
            Scanner input = new Scanner(stream);
            while(input.hasNextLine()){
                String next = input.nextLine();
                if(next.contains("class=\"post-image-container")){
                    Matcher m = IMG_REGEX.matcher(next);
                    m.find();
                    //System.out.println(next);
                    String pic = "http://i.imgur.com/" + m.group("link") + ".jpg";
                    imgLinks.add(pic);
                }
            }
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(AlbumDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
                
        }
        return imgLinks;
    }
    /**
     * Starts downloading the album
     */
    public void downloadFiles(){
        ArrayList<String> images = getImages();
        String s = target.toString();
        ExecutorService pool = Executors.newWorkStealingPool();
        int x = 1;
        for(String img: images){
            try{
                String filename = x + ".jpg";
                x++;
                String path = s + "\\" + filename;
                System.out.println(path);
                File file = new File(path);
                FileDownloader f = new FileDownloader(new URL(img),file);
                pool.execute(f);
            } catch(MalformedURLException e){
            }
        }
    }
    /**
     * Checks if a String is formatted as a link to a proper imgur album
     * @param s The string to check
     * @return true if the s is in the format of a link to an imgur album. false
     * otherwise
     */
    public static boolean isValidAlbumLink(String s){
        Matcher m = IMGUR_REGEX.matcher(s);
        return m.matches();
    }
}
