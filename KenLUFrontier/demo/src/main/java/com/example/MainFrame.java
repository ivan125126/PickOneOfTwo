package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class MainFrame extends Frame 
{
    JMenuBar mb = new JMenuBar();
    JMenu Menu = new JMenu("Menu");
    JMenuItem MenuItemVote = new JMenuItem("Vote");
    JMenuItem MenuItemRank = new JMenuItem("Rank");
    JMenuItem MenuItemFollow = new JMenuItem("Followed");
    JMenuItem MenuItemNT = new JMenuItem("Notification");
    JMenuItem MenuItemLogout = new JMenuItem("Logout");

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    VotePanel votePanel = new VotePanel(this);
    RankPanel rankPanel = new RankPanel(this);
    FollowPanel followPanel = new FollowPanel(this);
    NTPlane ntPlane = new NTPlane(this);
    String Username = "";

    public MainFrame(String username)
    {
        Username = username;
        setTitle("Main");
        setSize(600, 530);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Menu.add(MenuItemVote);
        MenuItemVote.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "Vote");
                System.out.println("Panel Vote");
            }
        });

        Menu.add(MenuItemRank);
        MenuItemRank.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "Rank");
                System.out.println("Panel Rank");
            }
        });

        Menu.add(MenuItemFollow);
        MenuItemFollow.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "Followed");
                System.out.println("Panel Followed");
            }
        });

        Menu.add(MenuItemNT);
        MenuItemNT.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "Notification");
                System.out.println("Panel NT");
            }
        });

        Menu.add(MenuItemLogout);
        MenuItemLogout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame jFrame = new JFrame();
                int response = JOptionPane.showConfirmDialog(jFrame, 
                        "Are you sure to log out?", 
                        "Confirm Logout", 
                        JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.OK_OPTION) 
                {
                    JOptionPane.showMessageDialog(jFrame, "Logged out successfully.");
                    LoginFrame LFrame = new LoginFrame();
                    LFrame.KenLuakaYBen();
                    dispose();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(jFrame, "Logout cancelled.");
                }
                System.out.println("Log Out");
            }
        });

        mb.add(Menu);
        setJMenuBar(mb); 

        mainPanel.add(votePanel , "Vote");
        mainPanel.add(rankPanel , "Rank");
        mainPanel.add(followPanel , "Followed");
        mainPanel.add(ntPlane , "Notification");
        
        add(mainPanel);

        setVisible(true);
    }
}
