package com.example.votebe;

public class MyTag
{
    public Integer tagId;
    public String tag;
    public String getTag(){return this.tag;}
    @Override
    public String toString() {
        return tagId + ". " + tag;  // Return the tag name when toString() is called
    }
}
