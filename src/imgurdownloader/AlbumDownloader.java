
package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A driver class which does the work of downloading the Imgur Album. Files will
 * be named [number].[fileextension] when downloaded. 
 * @author Victor Zeng
 */
public class AlbumDownloader {
    //A regex to check the link is a valid album
    public static final Pattern IMGUR_REGEX = 
            Pattern.compile("(?<protocol>https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/(a|gallery)/(?<imgurID>[a-zA-Z0-9]+)(#[0-9]+)?");
    //A regex to identify images in the HTML
    public static final Pattern IMG_REGEX = Pattern.compile("id=\"(?<link>.+?)\"");
    public static final Pattern FILE_REGEX = Pattern.compile("http://i\\.imgur\\.com/(?<imgurID>[a-zA-Z0-9]+)(?<extension>\\.[a-zA-Z0-9]+)");
    
    private Path target;
    private String albumID;
    private PrintStream out;
    
    /**
     * Constructs a new AlbumDownloader
     * @param url The url of the album to downlaod from
     * @param target The directory to place the downloaded images in.
     */
    public AlbumDownloader(String url,Path target,PrintStream out){
        getAlbumID(url);
        this.target = target;
        this.out = out;
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
                Matcher m = IMG_REGEX.matcher(next);
                if(next.contains("post-image-container") && m.find()){
                    if(next.contains("itemtype=\"http://schema.org/ImageObject\"")){
                        String pic = "http://i.imgur.com/" + m.group("link") + ".jpg";
                        imgLinks.add(pic);
                    }
                    else if(next.contains("itemtype=\"http://schema.org/VideoObject\"")){
                        String pic = "http://i.imgur.com/" + m.group("link") + ".mp4";
                        imgLinks.add(pic);
                    }
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
        out.println("Downloading album " + albumID);
        System.out.println("Downloading album " + albumID);
        ArrayList<String> images = getImages();
        String s = target.toString();
        ExecutorService pool = Executors.newWorkStealingPool();
        int x = 1;
        for(String img: images){
            try{
                Matcher m = FILE_REGEX.matcher(img);
                if(m.matches()){
                    String filename = x + m.group("extension");
                    String path = s + "\\" + filename;
                    x++;
                    File file = new File(path);
                    FileDownloader f = new FileDownloader(new URL(img),file,out);
                    pool.execute(f);
                }
            } catch(Exception e){
            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            out.println("Finished Downloading all files");
        } catch (InterruptedException ex) {
            Logger.getLogger(AlbumDownloader.class.getName()).log(Level.SEVERE, null, ex);
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
