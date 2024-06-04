package com.example;

public class UpdateThumbs 
{
    String user;
    int objectId;
    int newRate;

    public UpdateThumbs(String UserName , int ID , int Like)
    {
        user = UserName;
        objectId = ID;
        newRate = Like;
    }
}
