/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.plaf;

import com.nokia.lwuit.DirectUtilsProvider;
import com.nokia.lwuit.ImageUtils;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


/**
 * For internal use only. This class is used to apply graphical effects to a border
 * @author tkor
 */
public class PlatformColorBlender {
    
    private static PlatformColorBlender mSelf;
    
    private DirectUtilsProvider du;
    /**
     * Default constructor
     */
    private PlatformColorBlender() {
        du = DirectUtilsProvider.getDirectUtils();
    }
    
    /**
     * Get instnace of the BorderBlender class.
     * @return singleton instance of the BorderBlender class.
     */
    public static PlatformColorBlender getInstance() {
        if(mSelf == null) {
            mSelf = new PlatformColorBlender();
        }
        return mSelf;
    }
    
    /**
     * Apply color to border. The algorithm works in a way that first the mask
     * is used color the background and after that the border is drawn over the
     * colored mask. In order to get a good result the border has to be
     * semitransparent.
     * @param b
     * @param color
     * @param mask 
     */
    public void applyColorToBorder(Border b, int color, com.sun.lwuit.Image[] mask) {
        if(du == null) {
            return;
        }
        
        if(mask == null || mask.length == 0) {
            System.out.println("border mask null or length zero");
            return;
        }
        
        int l = b.images.length;
        Image gfxLayer, maskLayer;
        
        for(int i = 0; i < l; i++) {
            com.sun.lwuit.Image maskTile = mask[i];
            com.sun.lwuit.Image sourceTile = b.images[i];
            
            // create images representing layers
            maskLayer = du.createImage(
                    sourceTile.getWidth(), sourceTile.getHeight(), 0x00000000);                  
            gfxLayer = du.createImage(
                    sourceTile.getWidth(), sourceTile.getHeight(), 0x00000000);
            
            // fill mask layer image with mask data
            maskLayer.getGraphics().drawRGB(
                    maskTile.getRGB(),          // rgb data
                    0,                          // offset
                    maskTile.getWidth(),        // scanlength
                    0,                          // x
                    0,                          // y
                    maskTile.getWidth(),        // width
                    maskTile.getHeight(),       // height
                    true);                      // do process alpha
            
            // fill graphics layer image with background color
            Graphics g = gfxLayer.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, gfxLayer.getWidth(), gfxLayer.getHeight());
                   
            // apply mask to graphics layer
            gfxLayer = ImageUtils.drawMaskedImage(gfxLayer, maskLayer);
            
            // draw component image to graphics layer
            gfxLayer.getGraphics().drawRGB(
                    sourceTile.getRGB(),        // rgb data
                    0,                          // offset
                    sourceTile.getWidth(),      // scanlength
                    0,                          // x
                    0,                          // y
                    sourceTile.getWidth(),      // width
                    sourceTile.getHeight(),     // height
                    true);                      // do process alpha
                     
            b.images[i] = com.sun.lwuit.Image.createImage(gfxLayer);  
        }
    }
    /**
     * Apply color to border.
     * @param b the border to be colored
     * @param color the color
     */
    public void applyColorToBorder(Border b, int color) {
        if(du == null) {
            return;
        }
        if(b == null) {
            return;
        }
        int l = b.images.length;
        Image img = null;
        Image temp = null;
        for(int i = 0; i < l; i++) {
            img = du.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());            
            
            temp = du.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            temp.getGraphics().drawRGB(b.images[i].getRGB(), 0, b.images[i].getWidth(), 0, 0, b.images[i].getWidth(), b.images[i].getHeight(), true);
            img = ImageUtils.drawMaskedImage(img, temp);
            b.images[i] = com.sun.lwuit.Image.createImage(img);
        }
    }    
    
    /**
     * Apply color to a border used in list.
     * @param listRendererFocusBorder the border the listitem is using
     * @param color the color
     */
    public void applyColorToListItem(Border listRendererFocusBorder, final int color) {
        if(du == null) {
            return;
        }
        int l = listRendererFocusBorder.images.length;
        Border b = listRendererFocusBorder;
        Image img = null;
        for(int i = 0; i < l; i++) {
            img = du.createImage(b.images[i].getWidth(), b.images[i].getHeight(), 0x00000000);
            Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawRGB(b.images[i].getRGB(), 0, b.images[i].getWidth(), 0, 0, b.images[i].getWidth(), b.images[i].getHeight(), true);
            b.images[i] = com.sun.lwuit.Image.createImage(img);
        }
    }

    /**
     * Fill background of image in theme with given color
     * @param themeProps Collection of theme props
     * @param key Key pointing to an image used in the theme
     * @param color Fill color
     */
    public void applyBackgroundColorToThemeProp(Hashtable themeProps, String key, final int color) {
        if(du == null) {
            return;
        }
        com.sun.lwuit.Image bg = null;
        
        try {
             bg = (com.sun.lwuit.Image) themeProps.get(key);
        } catch(ClassCastException cce) {
            throw new IllegalArgumentException(key + " is not LWUIT Image.");
        }

        int width = bg.getWidth();
        int height = bg.getHeight();
        Image img = null;
        img = du.createImage(width, height, 0x00000000);
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawRGB(bg.getRGB(), 0, width, 0, 0, width, height, true);
        bg = com.sun.lwuit.Image.createImage(img);
        themeProps.put(key, bg);
    }
}
