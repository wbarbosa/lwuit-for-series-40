/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

/**
 *
 * @author tkor
 */
class DirectUtilsProviderImpl extends DirectUtilsProvider{

    public Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        return com.nokia.mid.ui.DirectUtils.createImage(imageData, imageOffset, imageLength);
    }

    public Image createImage(int width, int height, int color) {
        return com.nokia.mid.ui.DirectUtils.createImage(width, height, color);
    }

    public Font getFont(int identifier) {
        return com.nokia.mid.ui.DirectUtils.getFont(identifier);
    }

    public Font getFont(int face, int style, int height) {
        return com.nokia.mid.ui.DirectUtils.getFont(face, style, height);
    }
    
}
