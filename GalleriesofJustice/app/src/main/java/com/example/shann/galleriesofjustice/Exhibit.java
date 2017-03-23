package com.example.shann.galleriesofjustice;

import java.io.Serializable;

/**
 * Created by N0499010 Shannon Hibbett on 16/03/2017.
 */

public class Exhibit  implements Serializable {

    private String key;

    private String image;
    private String title;
    private String desc;

    public Exhibit() {

    }

    public Exhibit(String image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
