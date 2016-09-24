

package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author zacke
 */
public class ImgurDownloader {
    
    public static final Pattern IMGUR_REGEX = 
            Pattern.compile("(?<protocol>https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/(a|gallery)/(?<imgurID>[a-zA-Z0-9]+)(#[0-9]+)?");
    public static final Pattern IMG_REGEX = Pattern.compile("id=\"(?<link>.+?)\"");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {
         String target = args[1];
         String albumID = getAlbumID(args[0]);
         ArrayList<String> imgs = getImages(albumID);
         File f = new File(target);
         f.mkdir();
         for(int x = 0;x<imgs.size();x++){
             String img = imgs.get(x);
             System.out.println(img);
             String[] arr = img.split("\\.");
             String filetype = arr[arr.length-1];
             System.out.println(filetype);
             String filePath = target + "\\" + x + "." + filetype;
             System.out.println(filePath);
             copyFile(img,filePath);
         }
         
         
    }
    
    
    public static String getAlbumID(String s){
        Matcher match = IMGUR_REGEX.matcher(s);
        if(match.matches()){
            return match.group("imgurID");
        }
        throw new IllegalArgumentException("That is not an imgur link");
    }
    
    public static ArrayList<String> getImages(String albumID){
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
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(ImgurDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
                
        }
        return imgLinks;
    }
    
    public static void copyFile(String location, String target) throws MalformedURLException{
        URL url = new URL(location);
        File f = new File(target);
        try (InputStream in = url.openStream()){
            Files.copy(in, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
        }
    }

}
