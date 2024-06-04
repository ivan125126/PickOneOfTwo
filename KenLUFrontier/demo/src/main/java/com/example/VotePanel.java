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

public class VotePanel extends JPanel
{
    JLabel Type = new JLabel("Vote Type :");
    String[] Options = {"All" , "Animate" , "Movie" , "Liked" , "Else"};
    JComboBox<String> TypeCombobox = new JComboBox<>(Options);
    JButton SubmitButton = new JButton("Submit");
    LikePicture Like1 = new LikePicture();
    LikePicture DisLike1 = new LikePicture();
    LikePicture Like2 = new LikePicture();
    LikePicture DisLike2 = new LikePicture();
    MainFrame mainFrame;
    String selectedType;
    String U1 = "https://all.web.img.acsta.net/img/91/41/91412fbc0ed5d0bf8962e9d9523da195.jpg";
    String U2 = "https://lumiere-a.akamaihd.net/v1/images/p_luca_21670_3c13c611.jpeg";
    JLabel Picture1 = new JLabel();
    JLabel Picture2 = new JLabel();
    List<ItemData> ItemDateList;
    List<Integer> LikeList;

    public VotePanel(MainFrame MFrame)
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
                GetPicture();
            }
        });

        Like1.setBounds(65 , 410 , 20 , 30);
        Like1.LikePicture_Like(20 , 30);
        Like1.setVisible(false);
        Like1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(Like1.Checked) 
                {
                    Like1.Change("WhiteLike.png" , 20 , 30);
                    Like1.Checked = false;
                    UpdateLike(ItemDateList.get(0), 0);
                }
                else
                {
                    Like1.Change("BlackLike.png" , 20 , 30);
                    DisLike1.Change("WhiteDisLike.png" , 20 , 30);
                    Like1.Checked = true;
                    DisLike1.Checked = false;
                    UpdateLike(ItemDateList.get(0), 1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!Like1.Checked) Like1.Change("BlueLike.png" , 20 , 30);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!Like1.Checked) Like1.Change("WhiteLike.png" , 20 , 30);
            }
        });
        add(Like1);
        
        DisLike1.setBounds(100 , 410 , 20 , 30);
        DisLike1.LikePicture_DisLike(20 , 30);
        DisLike1.setVisible(false);
        DisLike1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(DisLike1.Checked)
                {
                    DisLike1.Change("WhiteDisLike.png" , 20 , 30);
                    DisLike1.Checked = false;
                    UpdateLike(ItemDateList.get(0), 0);
                } 
                else
                {
                    Like1.Change("WhiteLike.png" , 20 , 30);
                    DisLike1.Change("BlackDisLike.png" , 20 , 30);
                    Like1.Checked = false;
                    DisLike1.Checked = true;
                    UpdateLike(ItemDateList.get(0), -1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!DisLike1.Checked) DisLike1.Change("RedLike.png" , 20 , 30);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!DisLike1.Checked) DisLike1.Change("WhiteDisLike.png" , 20 , 30);
            }
        });
        add(DisLike1);

        Like2.setBounds(320 , 410 , 20 , 30);
        Like2.LikePicture_Like(20 , 30);
        Like2.setVisible(false);
        Like2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(Like2.Checked) 
                {
                    Like2.Change("WhiteLike.png" , 20 , 30);
                    UpdateLike(ItemDateList.get(1), 0);
                }
                else
                {
                    Like2.Change("BlackLike.png" , 20 , 30);
                    DisLike2.Change("WhiteDisLike.png" , 20 , 30);
                    Like2.Checked = true;
                    DisLike2.Checked = false;
                    UpdateLike(ItemDateList.get(1), 1);
                }  
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!Like2.Checked) Like2.Change("BlueLike.png" , 20 , 30);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!Like2.Checked) Like2.Change("WhiteLike.png" , 20 , 30);
            }
        });
        add(Like2);
        
        DisLike2.setBounds(355 , 410 , 20 , 30);
        DisLike2.LikePicture_DisLike(20 , 30);
        DisLike2.setVisible(false);
        DisLike2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(DisLike2.Checked) 
                {
                    DisLike2.Change("WhiteDisLike.png" , 20 , 30);
                    UpdateLike(ItemDateList.get(1), 0);
                }
                else
                {
                    Like2.Change("WhiteLike.png" , 20 , 30);
                    DisLike2.Change("BlackDisLike.png" , 20 , 30);
                    Like2.Checked = false;
                    DisLike2.Checked = true;
                    UpdateLike(ItemDateList.get(1), -1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!DisLike2.Checked) DisLike2.Change("RedLike.png" , 20 , 30);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!DisLike2.Checked) DisLike2.Change("WhiteDisLike.png" , 20 , 30);
            }
        });
        add(DisLike2);

        Picture1.setBounds(65 , 100 , 200 , 300);
        add(Picture1);
        Picture1.setVisible(false);

        Picture1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(Picture1.isVisible())
                {
                    ItemData WinItemData = ItemDateList.get(0);
                    ItemData LoseItemData = ItemDateList.get(1);
                    VoteResult Result = new VoteResult(mainFrame.Username, WinItemData.id , LoseItemData.id , selectedType);
                    Gson gson = new Gson();
                    String Package = gson.toJson(Result);
                    Client client = new Client();
                    client.Vote();
                    client.Access(Package);
                    System.out.println(Package);
                    GetPicture();
                }
            }
        });

        Picture2.setBounds(320 , 100 , 200 , 300);
        add(Picture2);
        Picture2.setVisible(false);

        Picture2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(Picture2.isVisible())
                {
                    ItemData WinItemData = ItemDateList.get(1);
                    ItemData LoseItemData = ItemDateList.get(0);
                    VoteResult Result = new VoteResult(mainFrame.Username, WinItemData.id , LoseItemData.id , selectedType);
                    Gson gson = new Gson();
                    String Package = gson.toJson(Result);
                    Client client = new Client();
                    client.Vote();
                    client.Access(Package);
                    System.out.println(Package);
                    GetPicture();
                }
            }
        });
    }

    public void GetPicture()
    {
        selectedType = TypeCombobox.getItemAt(TypeCombobox.getSelectedIndex());
        SearchType searchType = new SearchType(selectedType);
        Gson gson = new Gson();
        String Package = gson.toJson(searchType);
        Client client = new Client();
        client.Vote_Submit(selectedType);
        client.Access("");
        System.out.println(client.Status);

        Type userListType = new TypeToken<List<ItemData>>() {}.getType();
        ItemDateList = gson.fromJson(client.Status, userListType);

        String [] uri = new String[2];
        int [] id = new int[2];
        int i = 0;
        for (ItemData user : ItemDateList) 
        {
            System.out.println("ID: " + user.imageURL);
            uri[i] = user.imageURL;
            id[i] = user.id;
            i++;
        }

        int [] like = new int[2];
        ItemLike itemlike1 = new ItemLike(mainFrame.Username , id[0]);
        Package = gson.toJson(itemlike1);
        client.GetLike();
        client.Access(Package);
        System.out.println(Package);
        like[0] = Integer.parseInt(client.Status);

        ItemLike itemlike2 = new ItemLike(mainFrame.Username , id[1]);
        Package = gson.toJson(itemlike2);
        client.GetLike();
        client.Access(Package);
        System.out.println(id[1]);
        like[1] = Integer.parseInt(client.Status);
        
        ShowPicture(uri , like);
    }

    public void ShowPicture(String [] Uri , int [] Like)
    {
        try 
        {
            URI uri = new URI(Uri[0]);
            URL url = uri.toURL();
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            Image ResizedImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
            Picture1.setIcon(ResizedIcon);
            Picture1.setVisible(true);
            Like1.setVisible(true);
            DisLike1.setVisible(true);
            if(Like[0] == 0) 
            {
                Like1.Change("WhiteLike.png" , 20 , 30);
                DisLike1.Change("WhiteDisLike.png" , 20 , 30);
                Like1.Checked = false;
                DisLike1.Checked = false;
            }
            else if(Like[0] == 1)
            {
                Like1.Change("BlackLike.png" , 20 , 30);
                DisLike1.Change("WhiteDisLike.png" , 20 , 30);
                Like1.Checked = true;
                DisLike1.Checked = false;
            }
            else if(Like[0] == -1) 
            {
                Like1.Change("WhiteLike.png" , 20 , 30);
                DisLike1.Change("BlackDisLike.png" , 20 , 30);
                Like1.Checked = false;
                DisLike1.Checked = true;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        try 
        {
            URI uri = new URI(Uri[1]);
            URL url = uri.toURL();
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            Image ResizedImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
            Picture2.setIcon(ResizedIcon);
            Picture2.setVisible(true);
            Like2.setVisible(true);
            DisLike2.setVisible(true);
            if(Like[1] == 0) 
            {
                Like2.Change("WhiteLike.png" , 20 , 30);
                DisLike2.Change("WhiteDisLike.png" , 20 , 30);
                Like2.Checked = false;
                DisLike2.Checked = false;
            }
            else if(Like[1] == 1)
            {
                Like2.Change("BlackLike.png" , 20 , 30);
                DisLike2.Change("WhiteDisLike.png" , 20 , 30);
                Like2.Checked = true;
                DisLike2.Checked = false;
            }
            else if(Like[1] == -1) 
            {
                Like2.Change("WhiteLike.png" , 20 , 30);
                DisLike2.Change("BlackDisLike.png" , 20 , 30);
                Like2.Checked = false;
                DisLike2.Checked = true;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void UpdateLike(ItemData item , int NewRate)
    {
        UpdateThumbs NewLike = new UpdateThumbs(mainFrame.Username , item.id, NewRate);
        Gson gson = new Gson();
        String Package = gson.toJson(NewLike);
        Client client = new Client();
        client.updateThumbs();
        client.Access(Package);
        System.out.println(Package);
    }
}