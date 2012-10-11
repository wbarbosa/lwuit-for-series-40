/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.templates.list;

import com.sun.lwuit.Image;

/**
 * Data object for holding the text + image. Used with NokiaListCellRenderer
 * @author tkor
 */
public class BasicListItem {
    
    private Image image;
    private String text;
    
    public BasicListItem() {
        
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
}
