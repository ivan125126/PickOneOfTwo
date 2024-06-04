package com.example;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class FollowPanel extends JPanel
{
    MainFrame mainFrame;
    String selectedType;
    JLabel LUPicture = new JLabel();
    JLabel RUPicture = new JLabel();
    JLabel LDPicture = new JLabel();
    JLabel RDPicture = new JLabel();
    int Page = 1;
    int ItemNum;
    List<GetRank> FollowedList;
    JLabel PreviousPage = new JLabel();
    JLabel NextPage = new JLabel();
    JLabel NTArea = new JLabel();

    public FollowPanel(MainFrame MFrame)
    {
        mainFrame = MFrame;
        setLayout(null);

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
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , FollowedList.get((Page - 1) * 4).IDNumber);
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
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , FollowedList.get(Page * 4 - 3).IDNumber);
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
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , FollowedList.get(Page * 4 - 2).IDNumber);
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
                    ItemFrame itemFrame = new ItemFrame(mainFrame.Username , FollowedList.get(Page * 4 - 1).IDNumber);
                    itemFrame.KenLuakaYBen();
                }
            }
        });

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

        NTArea.setBounds(280 , 40 , 30 , 30);
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
                GetPicture();
            }
        });
        add(NTArea);
    }

    public void GetPicture()
    {
        Gson gson = new Gson();
        Client client = new Client();
        client.GetFollowing(mainFrame.Username);
        client.Access("");
        Type FollowList = new TypeToken<List<GetRank>>() {}.getType();
        FollowedList = gson.fromJson(client.Status, FollowList);
        ItemNum = FollowedList.size();
        System.out.println(FollowedList);

        if(ItemNum >= Page * 4 - 3) ShowPicture(FollowedList.get((Page - 1) * 4).httpString , LUPicture);

        if(ItemNum >= Page * 4 - 2) ShowPicture(FollowedList.get(Page * 4 - 3).httpString, RUPicture);
        else RUPicture.setVisible(false);

        if(ItemNum >= Page * 4 - 1) ShowPicture(FollowedList.get(Page * 4 - 2).httpString, LDPicture);
        else LDPicture.setVisible(false);

        if(ItemNum >= Page * 4) ShowPicture(FollowedList.get(Page * 4 - 1).httpString, RDPicture);
        else RDPicture.setVisible(false);

        if(ItemNum >= Page * 4 + 1) NextPage.setVisible(true);
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
