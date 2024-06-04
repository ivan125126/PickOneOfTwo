package com.example;

public class VoteResult 
{
    String UserName;
    int winnerId;
    int loserId;
    String groupName;

    public VoteResult(String userName , int WinnerId , int LoserId , String Group)
    {
        UserName = userName;
        winnerId = WinnerId;
        loserId = LoserId;
        groupName = Group;
    }
}
