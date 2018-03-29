package com.multipz.dictionary.Model;

/**
 * Created by Admin on 20-07-2017.
 */

public class MyActivityModel {

    private String def_title,is_word_status,html_string;

    public MyActivityModel(String def_title,String html_string,String is_word_status){

        this.def_title = def_title;
        this.html_string = html_string;
        this.is_word_status = is_word_status;
    }

    public String getDef_title() {
        return def_title;
    }

    public void setDef_title(String def_title) {
        this.def_title = def_title;
    }

    public String getIs_word_status() {
        return is_word_status;
    }

    public void setIs_word_status(String is_word_status) {
        this.is_word_status = is_word_status;
    }

    public String getHtml_string() {
        return html_string;
    }

    public void setHtml_string(String html_string) {
        this.html_string = html_string;
    }
}
