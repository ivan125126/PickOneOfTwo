package com.example;

import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import java.awt.event.*;

import java.awt.*;

public class NTPlane  extends JPanel
{
    MainFrame mainFrame;
    JLabel NTArea = new JLabel();
    JTextArea MessageLabel = new JTextArea(); 
    String Message = "";

    public NTPlane(MainFrame MFrame)
    {
        mainFrame = MFrame;
        setLayout(null);
        
        NTArea.setBounds(550 , 10 , 30 , 30);
        try 
        {
            ImageIcon icon = new ImageIcon("C:/Users/afatf/Desktop/JAVA/PM2.0/demo/src/images/Update.png");
            Image image = icon.getImage();
            Image ResizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
            NTArea.setIcon(ResizedIcon);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        NTArea.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Client client = new Client();
                client.GetMessage();
                client.Access("");
                Message += client.Status + "\n";
                MessageLabel.setText(Message);
            }
        });
        add(NTArea);

        MessageLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        MessageLabel.setBackground(mainFrame.getContentPane().getBackground());
        MessageLabel.setLineWrap(false); // 设置自动换行
        MessageLabel.setWrapStyleWord(true); // 设置在单词边界换行
        MessageLabel.setEditable(false); // 设置为不可编辑
        Caret noCaret = new DefaultCaret() 
        {
            @Override
            public void paint(Graphics g) 
            {
                // 不执行任何绘制操作，使光标不可见
            }
        };
        MessageLabel.setCaret(noCaret);
        JScrollPane MesScr = new JScrollPane(MessageLabel);
        MesScr.setBounds(50 , 50 , 480 , 450);
        MesScr.setBorder(null);
        add(MesScr);
    }
}
