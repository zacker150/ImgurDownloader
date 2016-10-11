/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader.gui;

import java.awt.Dimension;
import java.io.OutputStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author zacke
 */
public class TextFrame extends JFrame {

    private JTextAreaOutputStream textStream;
    private JTextArea text;

    /**
     * Constructs a new TextFrame with title "Download Log"
     */
    public TextFrame() {
        this("Download Log");
    }

    public TextFrame(String title) {
        initComponents();
        textStream = new JTextAreaOutputStream(text);
        this.setTitle(title);
    }

    private void initComponents() {
        this.setSize(new Dimension(300, 500));
        text = new JTextArea();
        text.setEditable(false);
        this.add(new JScrollPane(text));
        this.setVisible(true);
    }

    /**
     * Gets the OutputStream corresponding to the JTextArea in this frame
     *
     * @return
     */
    public OutputStream getStream() {
        return textStream;
    }
}
