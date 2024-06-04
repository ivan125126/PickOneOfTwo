package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Client 
{
    public StringBuilder response;
    public String Status = "";
    String IP = "10.6.78.227";
    String UrI = "http://" + IP + ":8080";
    public URI uri;

    public String getStatus()
    {
        return Status;
    }

    public void login()
    {
        try
        {
            uri = new URI(UrI + "/login");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void create()
    {
        try
        {
            uri = new URI(UrI + "/createAccount");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void Vote_Submit(String type)
    {
        try
        {
            uri = new URI(UrI + "/objectSet/" + type);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void Vote()
    {
        try
        {
            uri = new URI(UrI + "/record");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetLike()
    {
        try
        {
            uri = new URI(UrI + "/userThumbs");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void updateThumbs()
    {
        try
        {
            uri = new URI(UrI + "/updateThumbs");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetRank(String Type , int Number)
    {
        try
        {
            uri = new URI(UrI + "/getRank/" + Type + "/" + Number);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetDetail(int id)
    {
        try
        {
            uri = new URI(UrI + "/object/" + id);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetGruop(int id)
    {
        try
        {
            uri = new URI(UrI + "/objectGroup/" + id);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetTag(int id)
    {
        try
        {
            uri = new URI(UrI + "/objectTag/" + id);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetWinRate(int id , String GroupName)
    {
        try
        {
            uri = new URI(UrI + "/winRate/" + id + "/" + GroupName.replace(" ", ""));
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetMessage()
    {
        try
        {           
            uri = new URI(UrI + "/notification");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void Follow(String name , int ID)
    {
        try
        {           
            uri = new URI(UrI + "/follow/" + name + "/" + ID);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void UnFollow(String name , int ID)
    {
        try
        {           
            uri = new URI(UrI + "/unfollow/" + name + "/" + ID);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetFollowing(String name)
    {
        try
        {           
            uri = new URI(UrI + "/following/" + name);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void GetFollowed(String name , int ID)
    {
        try
        {           
            uri = new URI(UrI + "/isFollowing/" + name + "/" + ID);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    

    public void Access(String jsonInputString)
    {
        try 
        {
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            
            try (OutputStream os = conn.getOutputStream()) 
            {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) 
            {
                response.append(responseLine.trim());
            }
            Status = response.toString();
            System.out.println("Response: " + response.toString());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
