package com.example;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.google.gson.Gson;

public class LoginPanel extends JPanel
{
    JLabel UserLabel = new JLabel("User:");
    JTextField UserText = new JTextField(20);
    JLabel PasswordLabel = new JLabel("Password:");
    JPasswordField PasswordText = new JPasswordField(20);
    JButton LoginButton = new JButton("Login");
    JLabel CreateText = new JLabel("Create new account");
    String Status;
    String Username;
    LoginFrame loginframe;

    public LoginPanel(LoginFrame Loginframe)
    {
        loginframe = Loginframe;
        setLayout(null);
        
        UserLabel.setForeground(Color.WHITE);
        UserLabel.setBounds(65, 40, 80, 25);
        add(UserLabel);

        UserText.setBounds(155,40,165,25);
        add(UserText);
        
        PasswordLabel.setForeground(Color.WHITE);
        PasswordLabel.setBounds(65,80,80,25);
        add(PasswordLabel);

        PasswordText.setBounds(155,80,165,25);
        add(PasswordText);

        LoginButton.setBounds(155, 130, 80, 25);
        add(LoginButton);

        LoginButton.addActionListener(new ActionListener() 
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
                else if (username.equals("Admin") && password.equals("0410")) 
                {
                    Status = "Login Successful";
                    CheckUserExist(Status);
                }
                else
                {
                    Account account = new Account(username , password);
                    Gson gson = new Gson();
                    String Package = gson.toJson(account);
                    Client client = new Client();
                    client.login();
                    client.Access(Package);
                    Status = client.getStatus();
                    CheckUserExist(Status);
                } 
            }
        });

        CreateText.setForeground(Color.WHITE);
        CreateText.setBounds(135, 170, 120, 15);
        add(CreateText);

        CreateText.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                CreateAccountFrame CAFrame = new CreateAccountFrame();
                CAFrame.KenLuakaYBen();
                loginframe.CloseFrame();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                CreateText.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                CreateText.setForeground(Color.WHITE);
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

    public void CheckUserExist(String Status)
    {
        if(Status.equals("Login Successful"))
            {
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Login Success." , "" , JOptionPane.PLAIN_MESSAGE);
                MainFrame main = new MainFrame(UserText.getText());
                main.KenLuakaYBen();
                loginframe.CloseFrame();
            }
            else if(Status.equals("Password not correct"))
            {
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Password Not Correct." , "" , JOptionPane.ERROR_MESSAGE);
            }
            else if(Status.equals("Account not exist"))
            {
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Account Not Exist." , "" , JOptionPane.ERROR_MESSAGE); 
            }
            else
            {
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Error." , "" , JOptionPane.ERROR_MESSAGE); 
                System.out.println("Fxxk");
            }
    }
}
