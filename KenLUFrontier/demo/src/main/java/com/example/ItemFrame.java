package com.example;

import javax.swing.JFrame;

public class ItemFrame extends Frame
{
    public String Username;
    public int ID;

    public ItemFrame(String UserName , int id)
    {
        Username = UserName;
        ID = id;
        setTitle("ItemName");
        setSize(700, 900);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ItemPanel itemPanel = new ItemPanel(this);

        add(itemPanel);

        setVisible(true);
    }
}
