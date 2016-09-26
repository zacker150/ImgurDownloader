package imgurdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Downloader is a Runnable object that will download a single file from the 
 * Internet
 * @author Victor Zeng
 */
public class FileDownloader implements Runnable {
    
    private URL url;
    private File target;
    
    /**
     * Constructs a new Downloader
     * @param url A URL pointing to the object to be downloaded
     * @param target The file to store the downloaded object
     */
    public FileDownloader(URL url, File target){
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
