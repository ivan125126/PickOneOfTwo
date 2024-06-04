package com.example;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URL;
import com.google.gson.Gson;
public class RankPanel extends JPanel
{
    JLabel Type = new JLabel("Rank Type :");
    String[] Options = {"All" , "Animate" , "Movie" , "Liked" , "Else"};
    JComboBox<String> TypeCombobox = new JComboBox<>(Options);
    JButton SubmitButton = new JButton("Submit");
    MainFrame mainFrame;
    String selectedType;
    JLabel LUPicture = new JLabel();
    JLabel RUPicture = new JLabel();
    JLabel LDPicture = new JLabel();
    JLabel RDPicture = new JLabel();
    JLabel LURank = new JLabel();
    JLabel RURank = new JLabel();
    JLabel LDRank = new JLabel();
    JLabel RDRank = new JLabel();
    int Page = 1;
    GetRank Rank;
    int [] id = new int[5];
    
    JLabel PreviousPage = new JLabel();
    JLabel NextPage = new JLabel();

    String U1 = "https://all.web.img.acsta.net/img/91/41/91412fbc0ed5d0bf8962e9d9523da195.jpg";

    public RankPanel(MainFrame MFrame)
    {
        mainFrame = MFrame;
        setLayout(null);

        Type.setBounds(120 , 40 , 80 , 20);
        add(Type);

        TypeCombobox.setBounds(220 , 40 , 100 , 20);
        add(TypeCombobox);

        SubmitButton.setBounds(370 , 40 , 80 , 20);
        add(SubmitButton);

        SubmitButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Page = 1;
                GetPicture();
            }
        });

        LUPicture.setBounds(125 , 100 , 100 , 150);
        add(LUPicture);
        LUPicture.setVisible(false);

        LUPicture.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(LUPicture.isVisible())
                {
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , id[4]);
                    itemFrame.KenLuakaYBen();
                }
            }
        });

        RUPicture.setBounds(365 , 100 , 100 , 150);
        add(RUPicture);
        RUPicture.setVisible(false);

        RUPicture.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(RUPicture.isVisible())
                {
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , id[3]);
                    itemFrame.KenLuakaYBen();
                }
            }
        });

        LDPicture.setBounds(125 , 280 , 100 , 150);
        add(LDPicture);
        LDPicture.setVisible(false);

        LDPicture.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(LDPicture.isVisible())
                {
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , id[2]);
                    itemFrame.KenLuakaYBen();
                }
            }
        });

        RDPicture.setBounds(365 , 280 , 100 , 150);
        add(RDPicture);
        RDPicture.setVisible(false);

        RDPicture.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(RDPicture.isVisible())
                {
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , id[1]);
                    itemFrame.KenLuakaYBen();
                }
            }
        });

        LURank.setBounds(100 , 165 , 100 , 150);
        add(LURank);
        LURank.setFont(new Font("Arial", Font.PLAIN, 24));
        LURank.setText("1");
        LURank.setVisible(false);

        RURank.setBounds(330 , 165 , 100 , 150);
        add(RURank);
        RURank.setFont(new Font("Arial", Font.PLAIN, 24));
        RURank.setText("2");
        RURank.setVisible(false);

        LDRank.setBounds(100 , 345 , 100 , 150);
        add(LDRank);
        LDRank.setFont(new Font("Arial", Font.PLAIN, 24));
        LDRank.setText("3");
        LDRank.setVisible(false);

        RDRank.setBounds(330 , 345 , 100 , 150);
        add(RDRank);
        RDRank.setFont(new Font("Arial", Font.PLAIN, 24));
        RDRank.setText("4");
        RDRank.setVisible(false);

        PreviousPage.setBounds(30 , 240 , 50 , 50);
        add(PreviousPage);
        SetArrow("C:/Users/afatf/Desktop/JAVA/PM2.0/demo/src/images/Previous.png" , PreviousPage);
        PreviousPage.setVisible(false);
        PreviousPage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(PreviousPage.isVisible())
                {
                    PreviousPage.setVisible(true);
                    Page--;
                    GetPicture();
                }
            }
        });

        NextPage.setBounds(510 , 240 , 50 , 50);
        add(NextPage);
        SetArrow("C:/Users/afatf/Desktop/JAVA/PM2.0/demo/src/images/Next.png" , NextPage);
        NextPage.setVisible(false);
        NextPage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(NextPage.isVisible())
                {
                    PreviousPage.setVisible(true);
                    Page++;
                    GetPicture();
                }
            }
        });

        /* String title = "Congratulations sir";
        String message = "You've successfully created your first Tray Notification";
        
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndWait(); */
    }

    public void GetPicture()
    {
        selectedType =  TypeCombobox.getItemAt(TypeCombobox.getSelectedIndex());
        Gson gson = new Gson();
        Client client = new Client();
        String [] uri = new String[5];
        for(int i = 0 ; i <= 4 ; i++)
        {
            client.GetRank(selectedType , Page * 4 - i + 1);
            client.Access("");
            System.out.println(client.Status + i);

            if(client.Status.length() != 0)
            {
                Rank = gson.fromJson(client.Status, GetRank.class);
                uri[i] = Rank.httpString;
                id[i] = Rank.IDNumber;
            }
            else uri[i] = "";
        }
        
        if(uri[4].length() > 0) 
        {
            ShowPicture(uri[4], LUPicture);
            LURank.setText(Integer.toString(Page * 4 - 3));
            LURank.setVisible(true);
        }

        if(uri[3].length() > 0) 
        {
            ShowPicture(uri[3], RUPicture);
            RURank.setText(Integer.toString(Page * 4 - 2));
            RURank.setVisible(true);
        }
        else
        {
            RUPicture.setVisible(false);
            RURank.setVisible(false);
        }

        if(uri[2].length() > 0) 
        {
            ShowPicture(uri[2], LDPicture);
            LDRank.setText(Integer.toString(Page * 4 - 1));
            LDRank.setVisible(true);
        }
        else
        {
            LDPicture.setVisible(false);
            LDRank.setVisible(false);
        }

        if(uri[1].length() > 0) 
        {
            ShowPicture(uri[1], RDPicture);
            RDRank.setText(Integer.toString(Page * 4));
            RDRank.setVisible(true);
        }
        else
        {
            RDPicture.setVisible(false);
            RDRank.setVisible(false);
        }

        if(uri[0].length() > 0) NextPage.setVisible(true);
        else NextPage.setVisible(false);

        if(Page == 1) PreviousPage.setVisible(false);
        else PreviousPage.setVisible(true);
    }

    public void ShowPicture(String Uri , JLabel Picture)
    {
        try 
        {
            URI uri = new URI(Uri);
            URL url = uri.toURL();
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            Image ResizedImage = image.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
            Picture.setIcon(ResizedIcon);
            Picture.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void SetArrow(String Uri , JLabel Picture)
    {
        ImageIcon icon = new ImageIcon(Uri);
        Image image = icon.getImage();
        Image ResizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
        Picture.setIcon(ResizedIcon);
    }
}
