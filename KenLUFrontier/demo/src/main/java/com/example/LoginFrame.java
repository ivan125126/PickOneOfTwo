package com.example;

import javax.swing.*;
import java.util.ResourceBundle;

public class LoginFrame extends Frame  
{
    LoginPanel LoginPanel = new LoginPanel(this);
    ResourceBundle bundle;

    public LoginFrame()
    {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(LoginPanel);

        setVisible(true);
    }
}