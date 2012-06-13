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
     * Apply color to border. The algorithm works in a way that first the mask is used
     * color the background and after that the border is drawn over the colored mask.
     * In order to get a good result the border has to be semitransparent.
     * @param b
     * @param color
     * @param mask 
     */
    public void applyColorToBorder(Border b, int color, com.sun.lwuit.Image[] mask) {
        if(mask == null || mask.length == 0) {
            System.out.println("border mask null or length zero");
            return;
        }
            
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
