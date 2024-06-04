package com.example.votebe;

public class MyGroupObject {
    public int objId;
    public int gId;
    public int winGames;
    public int games;
    public int rankInGroup;
    public double calWinRate(){
        return (((double) winGames)/(double) games) * 100;
    }
}
