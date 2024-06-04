package com.example;

public class ItemDetail 
{
    int id;
    String name;
    String description;
    String type;
    String [] tag;
    String imageURL;
    int thumbs;
    int thumbsUp;
    int thumbsDown;

    public ItemDetail(int ID , String Name , String Des , String group , String [] Tag , String http , int garbage , int Like , int DisLike)
    {
        id = ID;
        name = Name;
        description = Des;
        type = group;
        tag = Tag;
        imageURL = http;
        thumbs = garbage;
        thumbsUp = Like;
        thumbsDown = DisLike;
    }
}
