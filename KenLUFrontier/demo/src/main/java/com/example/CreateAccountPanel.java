package com.example;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.Gson;

public class CreateAccountPanel extends JPanel
{
    JLabel UserLabel = new JLabel("User:");
    JTextField UserText = new JTextField(20);
    JLabel PasswordLabel = new JLabel("Password:");
    JPasswordField PasswordText = new JPasswordField(20);
    JButton CreateButton = new JButton("Create");
    CreateAccountFrame CAFrame;
    String Status;

    public CreateAccountPanel(CreateAccountFrame CAF)
    {
        CAFrame = CAF;
        setLayout(null);
        
        UserLabel.setForeground(Color.WHITE);
        UserLabel.setBounds(65, 40, 80, 25);
        this.add(UserLabel);

        UserText.setBounds(155,40,165,25);
        this.add(UserText);
        
        PasswordLabel.setForeground(Color.WHITE);
        PasswordLabel.setBounds(65,80,80,25);
        this.add(PasswordLabel);

        PasswordText.setBounds(155,80,165,25);
        add(PasswordText);

        CreateButton.setBounds(155, 130, 80, 25);
        this.add(CreateButton);

        CreateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String username = UserText.getText();
                String password = new String(PasswordText.getPassword());
                if(username.length() == 0)
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Invalid Username." , "" , JOptionPane.ERROR_MESSAGE); 
                    System.out.println("Invalid Username");
                }
                else if(password.length() == 0)
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Invalid Password." , "" , JOptionPane.ERROR_MESSAGE); 
                    System.out.println("Invalid Password");
                }
                else
                {
                    Account account = new Account(username , password);
                    Gson gson = new Gson();
                    String Package = gson.toJson(account);
                    Client client = new Client();
                    client.create();
                    client.Access(Package);
                    Status = client.getStatus();
                    CheckCreate(Status);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 400, 300);
    }

    public void CheckCreate(String Status)
    {
        if(Status.equals("Account created"))
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "Account Create." , "" , JOptionPane.PLAIN_MESSAGE);
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.KenLuakaYBen();
            CAFrame.CloseFrame();
        }
        else if(Status.equals("Account already exist"))
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "Account Already Exist." , "" , JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "Error." , "" , JOptionPane.ERROR_MESSAGE); 
            System.out.println("Fxxk");
        }
    }
}