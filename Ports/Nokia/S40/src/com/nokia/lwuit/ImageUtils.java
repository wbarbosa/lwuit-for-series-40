/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nokia.lwuit;

import javax.microedition.lcdui.Image;

/**
 *
 * @author tkor
 */
public class ImageUtils {
    /**
     * Apply colorblending to source-image and return the blended image.
     * @param source the image that will be blended
     * @param color the color that is used for blending
     * @param coeff the amount of transparency (0-255) for the color
     * @return blended image
     */
    public static Image applyColorBlending(final Image source, final int color, final int coeff) {
        // Reserve an array for the pixel data of each image
        int [] sourceData = new int[source.getHeight()*source.getWidth()];

        // Retrieve the individual pixels of each image (source, mask)
        source.getRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());


        // Define the needed pixel values
        int alpha1, alpha2;
        int red1, red2;
        int green1, green2;
        int blue1, blue2;
        int resultA,resultR,resultG,resultB;

        alpha2 = (color & 0xFF000000) >>> 24;
        red2 = (color & 0x00FF0000) >> 16;
        green2 = (color & 0x0000FF00) >> 8;
        blue2 = (color & 0x000000FF);
        // Go through all the pixels in the top and bottom images
        for (int i=0;i<sourceData.length;i++) {

           // Get individual channel values for each pixel (top,bottom)
           alpha1 = (sourceData[i] & 0xFF000000) >>> 24;
           
           red1 = (sourceData[i] & 0x00FF0000) >> 16;
           
           green1 = (sourceData[i] & 0x0000FF00) >> 8;
           
           blue1 = (sourceData[i] & 0x000000FF);
           
           int c = 255 - coeff;
           // Apply the image blending formula
           resultA = ( alpha1 * coeff + alpha2 * c ) / 255;
           resultR = ( red1 * coeff + red2 * c ) / 255;
           resultG = ( green1 * coeff + green2 * c ) / 255;
           resultB = ( blue1 * coeff + blue2 * c ) / 255;

           // Create the final pixel value
           sourceData[i] = resultA << 24 | resultR << 16 | resultG << 8 | resultB ;
        }
        Image ret = Image.createRGBImage(sourceData, source.getWidth(), source.getHeight(), true);
        
        return ret;
    }
}
