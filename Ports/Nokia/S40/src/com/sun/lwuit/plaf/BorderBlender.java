/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.plaf;

import com.nokia.lwuit.ImageUtils;
import com.nokia.mid.ui.DirectUtils;
import com.sun.lwuit.Display;
import com.sun.lwuit.util.Resources;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


/**
 *
 * @author tkor
 */
public class BorderBlender {
    
    private static BorderBlender mSelf;
    
    private BorderBlender() {
        
    }
    
    public static BorderBlender getInstance() {
        if(mSelf == null) {
            mSelf = new BorderBlender();
        }
        return mSelf;
    }
    /**
     * Blends Button's borderimages with given color. Used to apply platform colors
     * to buttons.
     * @param b
     * @param color 
     */
    public void applyColorToButton(Border b, int color) {
        
        Resources nokiaresource = Display.getInstance().getNokiaResource();
        com.sun.lwuit.Image[] maskButton = new com.sun.lwuit.Image[9];
        
        maskButton[0] = nokiaresource.getImage("mask_button_FT_02.png");//top
        maskButton[1] = nokiaresource.getImage("mask_button_FT_08.png");//bottom
        maskButton[2] = nokiaresource.getImage("mask_button_FT_04.png");//left
        maskButton[3] = nokiaresource.getImage("mask_button_FT_06.png");//right
        maskButton[4] = nokiaresource.getImage("mask_button_FT_01.png");//topleft
        maskButton[5] = nokiaresource.getImage("mask_button_FT_03.png");//topright
        maskButton[6] = nokiaresource.getImage("mask_button_FT_07.png");//bottomleft
        maskButton[7] = nokiaresource.getImage("mask_button_FT_09.png");//bottomRight
        maskButton[8] = nokiaresource.getImage("mask_button_FT_05.png");//background
        
        int l = b.images.length;
        Image img = null;
        Image temp = null;
        for(int i = 0; i < l; i++) {
            img = DirectUtils.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());            
            
            temp = DirectUtils.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            temp.getGraphics().drawRGB(maskButton[i].getRGB(), 0, maskButton[i].getWidth(), 0, 0, maskButton[i].getWidth(), maskButton[i].getHeight(), true);
            img = ImageUtils.drawMaskedImage(img, temp);
            img.getGraphics().drawRGB(b.images[i].getRGB(), 0, b.images[i].getWidth(), 0, 0, b.images[i].getWidth(), b.images[i].getHeight(), true);
            b.images[i] = com.sun.lwuit.Image.createImage(img);
        }
    }
    public void applyColorToBorder(Border b, int color, com.sun.lwuit.Image[] mask) {
        com.sun.lwuit.Image[] maskButton = mask;
        
        int l = b.images.length;
        Image img = null;
        Image temp = null;
        for(int i = 0; i < l; i++) {
            img = DirectUtils.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());            
            
            temp = DirectUtils.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            temp.getGraphics().drawRGB(maskButton[i].getRGB(), 0, maskButton[i].getWidth(), 0, 0, maskButton[i].getWidth(), maskButton[i].getHeight(), true);
            img = ImageUtils.drawMaskedImage(img, temp);
            img.getGraphics().drawRGB(b.images[i].getRGB(), 0, b.images[i].getWidth(), 0, 0, b.images[i].getWidth(), b.images[i].getHeight(), true);
            b.images[i] = com.sun.lwuit.Image.createImage(img);
        }
    }
    
    public void applyColorToListItem(Border listRendererFocusBorder, final int color) {
        int l = listRendererFocusBorder.images.length;
        Border b = listRendererFocusBorder;
        Image img = null;
        for(int i = 0; i < l; i++) {
            img = DirectUtils.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawRGB(b.images[i].getRGB(), 0, b.images[i].getWidth(), 0, 0, b.images[i].getWidth(), b.images[i].getHeight(), true);
            b.images[i] = com.sun.lwuit.Image.createImage(img);
        }
    }
    
}
