/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader.gui;

import java.awt.Dimension;
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
        out = new PrintStream(new JTextAreaOutputStream(text));
        this.setTitle(title);
    }

    private void initComponents() {
        this.setSize(new Dimension(500, 700));
        text = new JTextArea();
        text.setEditable(false);
        this.add(new JScrollPane(text));
        this.setVisible(true);
    }

}
