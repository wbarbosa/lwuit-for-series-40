/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

/**
 *
 * @author tkor
 */
public abstract class DirectUtilsProvider {
    
    public abstract javax.microedition.lcdui.Image createImage(byte[] imageData, int imageOffset, int imageLength);
    
    public abstract javax.microedition.lcdui.Image createImage(int width, int height, int color);
    
    public abstract javax.microedition.lcdui.Font getFont(int identifier); 
    
    public abstract javax.microedition.lcdui.Font getFont(int face, int style, int height);
    
    public static DirectUtilsProvider getDirectUtils() {
        DirectUtilsProvider ret = null;
        try {
            Class.forName("com.nokia.mid.ui.DirectUtils");
            Class c = Class.forName("com.nokia.lwuit.DirectUtilsProviderImpl");
            ret = (DirectUtilsProvider) c.newInstance();
        }catch(Exception ncdfe) {
            System.out.println("directutils class not available");
        }
        return ret;
    }
}
