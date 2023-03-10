package com.yunwltn98.memoapp2.model;

import java.io.Serializable;

public class Memo implements Serializable {
    public int id;
    public String title;
    public String content;

    public Memo(){
    }

    public Memo(String title, String content){
        this.title = title;
        this.content = content;
    }

    public Memo(int id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
