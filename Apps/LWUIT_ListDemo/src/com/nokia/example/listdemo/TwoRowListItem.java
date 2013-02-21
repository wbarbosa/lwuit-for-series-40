package com.nokia.example.listdemo;

import com.sun.lwuit.Image;
import com.sun.lwuit.list.DefaultListModel;

public class TwoRowListItem extends DefaultListModel {

    private String title;
    private String text;
    private Image icon;

    public TwoRowListItem(String title, String text, Image icon) {
        this.title = title;
        this.text = text;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }
}
