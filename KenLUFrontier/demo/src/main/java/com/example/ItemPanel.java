package com.example;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import java.awt.event.*;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class ItemPanel  extends JPanel
{
    JLabel Picture = new JLabel();
    JLabel ItemName = new JLabel();
    JTextArea Description = new JTextArea();
    JLabel SearchType = new JLabel("Search Type :");
    List<String> Taglist;
    String [] Tags;
    JTextArea Tag = new JTextArea();
    List<String> Grouplist;
    String [] Groups;
    JComboBox<String> GroupCombobox;
    JButton SubmitButton = new JButton("Submit");
    JLabel VoteNum = new JLabel();
    JLabel WinRate = new JLabel();
    JLabel LikeVote = new JLabel();
    JLabel DisLikeVote = new JLabel();
    LikePicture Like = new LikePicture();
    LikePicture DisLike = new LikePicture();
    JButton BackButton = new JButton("Back");
    JCheckBox FollowCheckBox = new JCheckBox("Follow");
    boolean Followed;

    ItemFrame itemFrame;
    ItemDetail itemDetail;
    VoteWinRate vwr;

    public ItemPanel(ItemFrame IFrame)
    {
        itemFrame = IFrame;
        setLayout(null);

        System.out.println(itemFrame.ID);
        Gson gson = new Gson();
        Client client = new Client();
        client.GetDetail(itemFrame.ID);
        client.Access("");
        itemDetail = gson.fromJson(client.Status , ItemDetail.class);

        client.GetGruop(itemFrame.ID);
        client.Access("");
        Type GroupList = new TypeToken<List<String>>() {}.getType();
        Grouplist = gson.fromJson(client.Status, GroupList);
        Groups = Grouplist.toArray(new String[0]);
        GroupCombobox = new JComboBox<>(Groups);

        client.GetTag(itemFrame.ID);
        client.Access("");
        Type tagList = new TypeToken<List<String>>() {}.getType();
        Taglist = gson.fromJson(client.Status, tagList);
        Tags = Taglist.toArray(new String[0]);

        client.GetWinRate(itemFrame.ID , GroupCombobox.getItemAt(GroupCombobox.getSelectedIndex()));
        client.Access("");
        vwr = gson.fromJson(client.Status , VoteWinRate.class); 
        
        client.GetFollowed(itemFrame.Username, itemFrame.ID);
        client.Access("");
        Followed = gson.fromJson(client.Status , boolean.class);

        Picture.setBounds(40 , 90 , 200 , 300);
        try 
        {
            URI uri = new URI(itemDetail.imageURL);
            URL url = uri.toURL();
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            Image ResizedImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
            Picture.setIcon(ResizedIcon);
            Picture.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        add(Picture);

        ItemName.setBounds(40 , 30 , 600 , 40);
        ItemName.setText(itemDetail.name);
        ItemName.setFont(new Font("Arial", Font.PLAIN, 36));
        add(ItemName);

        Description.setText(itemDetail.description);
        Description.setFont(new Font("Arial", Font.PLAIN, 20));
        Description.setBackground(itemFrame.getContentPane().getBackground());
        Description.setLineWrap(true); // 设置自动换行
        Description.setWrapStyleWord(true); // 设置在单词边界换行
        Description.setEditable(false); // 设置为不可编辑

        // 自定义一个不可见的 Caret
        Caret noCaret = new DefaultCaret() 
        {
            @Override
            public void paint(Graphics g) 
            {
                // 不执行任何绘制操作，使光标不可见
            }
        };
        Description.setCaret(noCaret);
        JScrollPane DesScr = new JScrollPane(Description);
        DesScr.setBounds(260 , 100 , 420 , 280);
        DesScr.setBorder(null);
        add(DesScr);

        String AllTags = Tags[0];
        for(int i = 1 ; i < Tags.length ; i++)
        {
            AllTags += "\n       " + Tags[i];
        }

        Tag.setText("Tags:\n\n       " + AllTags);
        Tag.setFont(new Font("Arial", Font.PLAIN, 24));
        Tag.setBackground(itemFrame.getContentPane().getBackground());
        Tag.setLineWrap(false); // 设置自动换行
        Tag.setWrapStyleWord(true); // 设置在单词边界换行
        Tag.setEditable(false); // 设置为不可编辑
        Tag.setCaret(noCaret);
        JScrollPane TagScr = new JScrollPane(Tag);
        TagScr.setBounds(50 , 420 , 200 , 300);
        TagScr.setBorder(null);
        add(TagScr);

        SearchType.setBounds(310 , 415 , 150 , 30);
        SearchType.setFont(new Font("Arial", Font.PLAIN, 18));
        add(SearchType);

        GroupCombobox.setBounds(450 , 420 , 180 , 20);
        add(GroupCombobox);
        GroupCombobox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String selectedType =  GroupCombobox.getItemAt(GroupCombobox.getSelectedIndex());
                Client client = new Client();
                Gson gson = new Gson();
                String Package = gson.toJson(selectedType);
                client.GetWinRate(itemFrame.ID , GroupCombobox.getItemAt(GroupCombobox.getSelectedIndex()));
                client.Access("");
                VoteWinRate vwr = gson.fromJson(client.Status , VoteWinRate.class);
                System.out.println(Package);
                WinRate.setText("Win% : " + Double.toString(vwr.winRate) + "%");
                VoteNum.setText("Vote : " + Integer.toString(vwr.gamesPlayed));
            }
        });

        VoteNum.setBounds(310 , 490 , 150 , 30);
        VoteNum.setText("Vote : " + Integer.toString(vwr.gamesPlayed)); //6
        VoteNum.setFont(new Font("Arial", Font.PLAIN, 24));
        add(VoteNum);

        WinRate.setBounds(310 , 560 , 200 , 30);
        WinRate.setText("Win% : " + Double.toString(vwr.winRate) + "%"); //8
        WinRate.setFont(new Font("Arial", Font.PLAIN, 24));
        add(WinRate);
        
        LikeVote.setBounds(310 , 630 , 150 , 30);
        LikeVote.setText("Like : " + Integer.toString(itemDetail.thumbsUp)); //6
        LikeVote.setFont(new Font("Arial", Font.PLAIN, 24));
        add(LikeVote);

        DisLikeVote.setBounds(310 , 700 , 200 , 30);
        DisLikeVote.setText("Dislike : " + Integer.toString(itemDetail.thumbsDown)); //8
        DisLikeVote.setFont(new Font("Arial", Font.PLAIN, 24));
        add(DisLikeVote);

        ItemLike itemlike = new ItemLike(itemFrame.Username , itemFrame.ID);
        String Package = gson.toJson(itemlike);
        client.GetLike();
        client.Access(Package);
        System.out.println(Package);
        int like = Integer.parseInt(client.Status);
        System.out.println(like);

        Like.setBounds(90 , 780 , 30 , 50);
        if(like == 0) 
        {
            Like.Change("WhiteLike.png" , 30 , 50);
            DisLike.Change("WhiteDisLike.png" , 30 , 50);
            Like.Checked = false;
            DisLike.Checked = false;
        }
        else if(like == 1)
        {
            Like.Change("BlackLike.png" , 30 , 50);
            DisLike.Change("WhiteDisLike.png" , 30 , 50);
            Like.Checked = true;
            DisLike.Checked = false;
        }
        else if(like == -1) 
        {
            Like.Change("WhiteLike.png" , 30 , 50);
            DisLike.Change("BlackDisLike.png" , 30 , 50);
            Like.Checked = false;
            DisLike.Checked = true;
        }

        Like.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(Like.Checked) 
                {
                    Like.Change("WhiteLike.png" , 30 , 50);
                    Like.Checked = false;
                    UpdateLike(itemDetail , 0);
                }
                else
                {
                    Like.Change("BlackLike.png" , 30 , 50);
                    DisLike.Change("WhiteDisLike.png" , 30 , 50);
                    Like.Checked = true;
                    DisLike.Checked = false;
                    UpdateLike(itemDetail , 1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!Like.Checked) Like.Change("BlueLike.png" , 30 , 50);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!Like.Checked) Like.Change("WhiteLike.png" , 30 , 50);
            }
        });
        add(Like);

        DisLike.setBounds(170 , 780 , 30 , 50);
        DisLike.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(DisLike.Checked)
                {
                    DisLike.Change("WhiteDisLike.png" , 30 , 50);
                    DisLike.Checked = false;
                    UpdateLike(itemDetail , 0);
                } 
                else
                {
                    Like.Change("WhiteLike.png" , 30 , 50);
                    DisLike.Change("BlackDisLike.png" , 30 , 50);
                    Like.Checked = false;
                    DisLike.Checked = true;
                    UpdateLike(itemDetail , -1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(!DisLike.Checked) DisLike.Change("RedLike.png" , 30 , 50);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(!DisLike.Checked) DisLike.Change("WhiteDisLike.png" , 30 , 50);
            }
        });
        add(DisLike);

        BackButton.setBounds(500 , 790 , 100 , 30);
        BackButton.setFont(new Font("Arial", Font.PLAIN, 18));
        BackButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                itemFrame.dispose();
            }
        });
        add(BackButton);

        FollowCheckBox.setBounds(400 , 790 , 100 , 30);
        if(Followed) FollowCheckBox.setSelected(true);
        else FollowCheckBox.setSelected(false);
        FollowCheckBox.addItemListener(new ItemListener() 
        {
            public void itemStateChanged(ItemEvent e) 
            {
                Client client = new Client();
                if (FollowCheckBox.isSelected()) 
                {
                    client.Follow(itemFrame.Username, itemDetail.id);
                } 
                else 
                {
                    client.UnFollow(itemFrame.Username , itemDetail.id);
                }
                client.Access("");
                System.out.println(client.Status);
            }
        });
        add(FollowCheckBox);
    }

    public void UpdateLike(ItemDetail item , int NewRate)
    {
        Client client = new Client();
        UpdateThumbs NewLike = new UpdateThumbs(itemFrame.Username , itemFrame.ID , NewRate);
        Gson gson = new Gson();
        String Package = gson.toJson(NewLike);
        client.updateThumbs();
        client.Access(Package);
        System.out.println(Package);
        GetLikeNum();
    }

    public void GetLikeNum()
    {
        Gson gson = new Gson();
        Client client = new Client();
        client.GetDetail(itemFrame.ID);
        client.Access("{/\"id\"/:/\"" + itemFrame.ID + "\"/}");
        itemDetail = gson.fromJson(client.Status , ItemDetail.class);
        LikeVote.setText("Like : " + Integer.toString(itemDetail.thumbsUp));
        DisLikeVote.setText("DisLike : " + Integer.toString(itemDetail.thumbsDown));
    }
}
