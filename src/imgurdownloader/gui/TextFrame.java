/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author zacke
 */
public class TextFrame extends JFrame {

    private JTextArea text;

    /**
     * The PrintStream associated with this TextFrame
     */
    public final PrintStream out;

    /**
     * Constructs a new TextFrame with title "Download Log"
     */
    public TextFrame() {
        this("Download Log");
    }

    public TextFrame(String title) {
        initComponents();
        out = new PrintStream(new JTextAreaOutputStream());
        this.setTitle(title);
    }

    private void initComponents() {
        this.setSize(new Dimension(500, 700));
        text = new JTextArea();
        text.setEditable(false);
        this.add(new JScrollPane(text));
        this.setVisible(true);
    }

    /**
     * Wraps a JTextArea into an OutputStream
     *
     */
    private class JTextAreaOutputStream extends OutputStream {

        private StringBuilder buffer;

        public JTextAreaOutputStream() {
            buffer = new StringBuilder();
        }

        @Override
        public void flush() {
            text.append(buffer.toString());
            buffer.setLength(0);
        }

        @Override
        public void write(int b) throws IOException {
            if (b == '\r') {
                return;
            }
            buffer.append((char) b);
            if (b == '\n') {
                flush();
            }
        }

    }

}
