/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.plaf;

import com.nokia.lwuit.ImageUtils;

/**
 *
 * @author tkor
 */
public class BorderBlender {
    /**
     * Blends border's images with given color. Mainly used for applying platform theme colors to component borders
     * @param b
     * @param color 
     */
    public static void blendBorderWithColor(Border b, int color) {
        int l = b.images.length;
        for(int i = 0; i < l; i++) {
            
            b.images[i] = com.sun.lwuit.Image.createImage(
                        ImageUtils.applyAlphaBlending(
                            b.images[i].getRGB(), 
                            color, 
                            100),
                        b.images[i].getWidth(), 
                        b.images[i].getHeight());
        }
    }
}
