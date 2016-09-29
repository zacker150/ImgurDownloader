/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader;

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
 *
 * @author zacke
 */
public class AlbumDownloader {
    public static final Pattern IMGUR_REGEX = 
            Pattern.compile("(?<protocol>https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/(a|gallery)/(?<imgurID>[a-zA-Z0-9]+)(#[0-9]+)?");
    public static final Pattern IMG_REGEX = Pattern.compile("id=\"(?<link>.+?)\"");
    
    private Path target;
    private String albumID;
    
    public AlbumDownloader(String url,Path target){
        getAlbumID(url);
        this.target = target;
    }
    
    private void getAlbumID(String url){
        Matcher match = IMGUR_REGEX.matcher(url);
        if(match.matches()){
            this.albumID = match.group("imgurID");
        }
        throw new IllegalArgumentException("That is not an imgur link");
    }
    
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
                    System.out.println(next);
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
    
    public void downloadFiles(){
        ArrayList<String> images = getImages();
        ExecutorService pool = Executors.newWorkStealingPool();
        for(String img: images){
            try{
                FileDownloader f = new FileDownloader(new URL(img),target.toFile());
                pool.execute(f);
            } catch(MalformedURLException e){
            }
        }
    }
}
