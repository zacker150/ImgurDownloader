package imgurdownloader.gui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * Wraps a JTextArea into an OutputStream
 * @author Victor Zeng
 */
public class JTextAreaOutputStream extends OutputStream{
    
    private JTextArea text;
    private StringBuilder buffer;
    
    public JTextAreaOutputStream(JTextArea area){
        text = area;
        buffer = new StringBuilder();
    }
    
    @Override
    public void flush(){
        text.append(buffer.toString());
        buffer.setLength(0);        
    }

    @Override
    public void write(int b) throws IOException {
        if(b == '\r'){
            return;
        }
        buffer.append((char) b);
        if(b == '\n'){
            flush();
        }
    }
    
}
