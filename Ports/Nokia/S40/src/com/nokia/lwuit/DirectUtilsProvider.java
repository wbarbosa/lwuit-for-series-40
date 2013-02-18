
package com.nokia.lwuit;

/**
 * Wrapper class for Nokia DirectUtils class. Here's a copy and paste of the javadoc:
 * This class is a placeholder of utility methods. 
 * It contains methods for converting standard lcdui classes to Nokia UI classes and vice versa, 
 * and method for creating Image out of ARGB pixel array, 
 * creating empty transparent or specific background colored images, 
 * creating mutable image from encoded image byte array.
 * @author tkor
 */
public abstract class DirectUtilsProvider {
    
    /**
     * Creates a mutable image which is decoded from the data stored in the specified byte array at the specified offset and length. The data must be in a self-identifying image file format supported by the implementation, such as PNG.
     * Note that the semantics of this method are exactly the same as Image.createImage(byte[],int,int) except that the returned image is mutable.
     * @param imageData the array of image data in a supported image format
     * @param imageOffset  the offset of the start of the data in the array
     * @param imageLength the length of the data in the array
     * @return the created mutable image
     */
    public abstract javax.microedition.lcdui.Image createImage(byte[] imageData, int imageOffset, int imageLength);
    
    /**
     * The method will return a newly created mutable Image with specified dimension 
     * with all pixels of an image of defined ARGB color. The color can contain alpha '
     * channel transparency information.
     * @param width the width of the new image, in pixels
     * @param height the height of the new image, in pixels
     * @param color the color of the image.
     * @return the created image
     * @throws java.lang.IllegalArgumentException - if either width or height is zero or less
     */
    public abstract javax.microedition.lcdui.Image createImage(int width, int height, int color);
    
    /**
     * The method returns new instance of javax.microedition.lcdui.Font. Legacy function, use of this function is deprecated.
     * @param identifier  Legacy identifier, use of this is depracted.
     * @return new instance of javax.microedition.lcdui.Font
     */
    public abstract javax.microedition.lcdui.Font getFont(int identifier); 
    
    /**
     * The method returns new instance of javax.microedition.lcdui.Font with custom font height. System provides a font that matches the requested attributes as closely as possible.
     * Font created in this way can be used only for Graphics instance (Canvas, CustomItem, Image). This font is not supported for high-level UI components (ChoiceGroup, StringItem and List). If font with custom height is set to some high-level component, it's replaced by default font.
     * Actual font height could be affected by system limitations, there may be a maximum height defined by the system.
     * @param face one of Font.FACE_SYSTEM, Font.FACE_MONOSPACE, or Font.FACE_PROPORTIONAL
     * @param style Font.STYLE_PLAIN, DirectUtils.STYLE_LIGHT, or a combination of Font.STYLE_BOLD, Font.STYLE_ITALIC, and Font.STYLE_UNDERLINED
     * @param height font height in pixels
     * @return new instance of javax.microedition.lcdui.Font
     */
    public abstract javax.microedition.lcdui.Font getFont(int face, int style, int height);
    
    /**
     * Get an instance of the DirectUtilsProvider
     * @return new instance of the provider or null, if API not supported by the device.
     */
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
