package com.example;

public class ItemData 
{
    public int id;
    public String name;
    public String description;
    public String type;
    public String [] tag;
    public String imageURL;
    public int Like;

    public ItemData(int ID , String Name , String Description , String Type , String [] Tag , String ImageURL , int like)
    {
        id = ID;
        name = Name;
        description = Description;
        type = Type;
        tag = Tag;
        imageURL = ImageURL;
        Like = like;
    }
}
