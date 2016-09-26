/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgurdownloader.gui;

import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

/**
 *
 * @author zacke
 */
public class ImgurDownloaderGui extends JFrame{
    
    private JTextField imgurLocationField;
    private JTextField targetField;
    private JButton selectTargetButton;
    private JButton goButton;
    
    public ImgurDownloaderGui(){
        initComponents();
    }
    
    private void initComponents(){
        this.setSize(600, 200);
        this.setResizable(false);        
        this.setTitle("Imgur Album Downloader");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);     
        
        JLabel imgurLabel = new JLabel("Imgur Album URL:", JLabel.LEFT);  
        imgurLocationField = new JTextField(); 
        JLabel targetLabel = new JLabel("Download location:",JLabel.LEFT);
        targetField = new JTextField();
        selectTargetButton = new JButton("Select Folder");
        goButton = new JButton("Download Now");
        Font f = new Font("Times New Roman",Font.PLAIN,72);
        goButton.setFont(f);
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup pGroup = layout.createSequentialGroup();
        
        pGroup.addGroup(layout.createParallelGroup()
                .addComponent(imgurLabel)
                .addComponent(targetLabel));
        pGroup.addGroup(layout.createParallelGroup()
                .addComponent(imgurLocationField)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(targetField)
                        .addComponent(selectTargetButton)));
        hGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addGroup(pGroup)
                .addComponent(goButton));
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(imgurLabel)
                .addComponent(imgurLocationField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(targetLabel)
                .addComponent(targetField)
                .addComponent(selectTargetButton));
        vGroup.addComponent(goButton);
        
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
       
        targetField = new JTextField();
        targetField.setBounds(new Rectangle(500,75));

        this.setVisible(true);
    }
    /*
    For Testing
    */
    public static void main(String[] args){
        ImgurDownloaderGui gui = new ImgurDownloaderGui();
    }
    
}
