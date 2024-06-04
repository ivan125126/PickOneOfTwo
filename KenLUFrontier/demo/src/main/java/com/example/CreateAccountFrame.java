package com.example;

import javax.swing.JFrame;

public class CreateAccountFrame  extends Frame
{
    CreateAccountPanel CAPanel = new CreateAccountPanel(this);

    public CreateAccountFrame()
    {
        setTitle("Create Account");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(CAPanel);

        setVisible(true);
    }
}
