/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nokia.lwuit;

import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Image;

/**
 * A helper class for manipulating images.
 * @author tkor
 */
public class ImageUtils {
    /**
     * Apply alphablending to source-image and return the blended image.
     * @param source the image that will be blended
     * @param color the color that is used for blending
     * @param coeff the amount of transparency (0-255) for the color
     * @return blended image
     */
    public static Image applyAlphaBlending(final Image source, final int color, final int coeff) {
        // Reserve an array for the pixel data of each image
        int [] sourceData = new int[source.getHeight()*source.getWidth()];

        // Retrieve the individual pixels of each image (source, mask)
        source.getRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        //apply blending
        sourceData = applyAlphaBlending(sourceData, color, coeff);
        
        Image ret = DirectUtils.createImage(source.getWidth(), source.getHeight(), 0xFF000000);
        ret.getGraphics().drawRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight(), true);

        return ret;
    }
    /**
     * Apply alphablending to rgb data.
     * @param rgb
     * @param color the color which is blended to the rgb data
     * @param coeff the amount of transparency (0-255) for the color
     * @return rgb data that has alphablending applied
     */
    public static int[] applyAlphaBlending(final int [] rgb, final int color, final int coeff) {
        
        int [] sourceData = rgb;
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
        return sourceData;
    }
    
    /**
     * Create a new image by using a mask to a source image.
     * @param source
     * @param mask
     * @return new image that has been masked from the source.
     */
    public static Image drawMaskedImage(Image source, Image mask) {
        // Reserve an array for the pixel data of each image
        int[] sourceData = new int[source.getHeight() * source.getWidth()];
        int[] maskData = new int[mask.getHeight() * mask.getWidth()];
        
        source.getRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(),
                source.getHeight());
        mask.getRGB(maskData, 0, mask.getWidth(), 0, 0, mask.getWidth(),
                mask.getHeight());
        // Merge the alpha channel of the mask with the color channels of the source
        for (int i = 0; i < sourceData.length; i++) {
            sourceData[i] = (maskData[i] & 0xFF000000)
                    | (sourceData[i] & 0x00FFFFFF);
        }
        
        Image ret = DirectUtils.createImage(source.getWidth(), source.getHeight(), 0x000000);
        ret.getGraphics().drawRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight(), true);
        return ret;
    }
}
