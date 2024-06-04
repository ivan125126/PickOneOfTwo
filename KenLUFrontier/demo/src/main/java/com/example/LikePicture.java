package com.example;

import java.awt.*;
import javax.swing.*;

public class LikePicture extends JLabel
{
    public boolean Checked = false;
    public boolean IsLike = true;

    String L = "C:/Users/afatf/Desktop/JAVA/PM2.0/demo/src/images/";

    public void LikePicture_Like(int w , int h)
    {
        ImageIcon icon = new ImageIcon(L + "WhiteLike.png");
        Image image = icon.getImage();
        Image ResizedImage = image.getScaledInstance(w , h , Image.SCALE_SMOOTH);
        ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
        setIcon(ResizedIcon);
        IsLike = true;
    }

    public void LikePicture_DisLike(int w , int h)
    {
        ImageIcon icon = new ImageIcon(L + "WhiteDisLike.png");
        Image image = icon.getImage();
        Image ResizedImage = image.getScaledInstance(w , h , Image.SCALE_SMOOTH);
        ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
        setIcon(ResizedIcon);
        IsLike = false;
    }

    public void Change(String PNG , int w , int h)
    {
        ImageIcon icon = new ImageIcon(L + PNG);
        Image image = icon.getImage();
        Image ResizedImage = image.getScaledInstance(w , h , Image.SCALE_SMOOTH);
        ImageIcon ResizedIcon = new ImageIcon(ResizedImage);
        setIcon(ResizedIcon);
    }
}
