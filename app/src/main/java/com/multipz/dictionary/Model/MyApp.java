package com.multipz.dictionary.Model;

import android.content.Context;

import com.multipz.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Admin on 24-07-2017.
 */

public class MyApp {
    int id;
    String title, desc, url;
    int img;

    public MyApp(String title,String desc,String url,int img) {
        this.title=title;
        this.desc=desc;
        this.url=url;
        this.img=img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage()
    {
        return img;
    }

    public void setImage(int image)
    {
        this.img = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
