/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author zacke
 */
public class Downloader implements Runnable {
    
    private URL url;
    private File target;
    /**
     * 
    */
    public Downloader(URL url, File target){
        this.url = url;
        this.target = target;
    }

    @Override
    public void run(){
        try (InputStream in = url.openStream()){
            Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException e){
        }
    }
    
    
}
