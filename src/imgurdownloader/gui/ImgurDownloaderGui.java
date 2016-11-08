package imgurdownloader.gui;

import imgurdownloader.AlbumDownloader;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

/**
 * The main graphical interface for the ImgurDownloader
 * @author Victor Zeng
 */
public class ImgurDownloaderGui extends JFrame{
    
    private JTextField imgurLocationField;
    private JTextField targetField;
    private JButton selectTargetButton;
    private JButton goButton;
    private Path target;
    
    public ImgurDownloaderGui(){
        initComponents();
    }
    
    private void initComponents(){
        this.setSize(600, 200);
        this.setResizable(false);        
        this.setTitle("Imgur Album Downloader");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel imgurLabel = new JLabel("Imgur Album URL:", JLabel.LEFT);  
        imgurLocationField = new JTextField(); 
        JLabel targetLabel = new JLabel("Download location:",JLabel.LEFT);
        targetField = new JTextField();
        targetField.setEditable(false);
        selectTargetButton = new JButton("Select Folder");
        selectTargetButton.addActionListener(new FileChooseButtonListener());
        goButton = new JButton("Start Download");
        Font f = new Font("Times New Roman",Font.PLAIN,72);
        goButton.setFont(f);
        goButton.addActionListener(new StartDownloadButtonListener());
        
        //Creates the layout
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);        
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

        this.setVisible(true);
    }
    
    /**
     * Upon receiving an ActionEvent, prompts the user with a JFileChooser to 
     * choose the directory to download to.
     * Should be added to selectTargetButton
     */
    private class FileChooseButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose the target");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                File f = chooser.getSelectedFile();
                target = f.toPath();
                targetField.setText(target.toString());                
            }
        }
    }
    
    /**
     * Upon receiving an ActionEvent, checks to see if the provided text is a 
     * valid Imgur.com album link. If it is, it creates an AlbumDownloader to 
     * download the album. Otherwise, notifies the user that the album link is 
     * not valid.
     * 
     * Should be added to goButton
     */
    public class StartDownloadButtonListener implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
            TextFrame f = new TextFrame();
            String url = imgurLocationField.getText();
            if(AlbumDownloader.isValidAlbumLink(url) && !targetField.getText().isEmpty()){
                AlbumDownloader downloader = new AlbumDownloader(url,target,f.out);
                Thread t = new Thread(downloader);
                t.start();
            }
            else if(targetField.getText().isEmpty()){
                JOptionPane.showMessageDialog(rootPane, "Please select a target directory to download to.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "That is not a valid imgur album.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }  
    
    public static void main(String[] args){
        ImgurDownloaderGui gui = new ImgurDownloaderGui();
    }
    
}
