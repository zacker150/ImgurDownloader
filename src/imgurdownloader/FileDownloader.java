package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Downloader is a Runnable that will download a single file from the 
 * Internet
 * @author Victor Zeng
 */
public class FileDownloader implements Runnable {
    
    private URL url;
    private File target;
    private PrintStream out;
    
    /**
     * Constructs a new Downloader
     * @param url A URL pointing to the file to be downloaded
     * @param target The file to store the downloaded object
     * @param out the PrintStream to record logs to
     */
    public FileDownloader(URL url, File target,PrintStream out){
        this.url = url;
        this.target = target;
        this.out = out;
    }

    @Override
    public void run(){        
        try (InputStream in = url.openStream()){
            out.println("Starting download: " + url);
            Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            out.println("Downlaoded " + url + " to " + target);
        }
        catch(IOException e){
        }
    }  
    
}
