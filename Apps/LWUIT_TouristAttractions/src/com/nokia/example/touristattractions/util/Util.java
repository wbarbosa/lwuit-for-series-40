/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.util;

import com.nokia.example.touristattractions.network.ImageOperation;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.plaf.Style;
import java.io.IOException;
import java.util.Vector;

public class Util {

    /**
     * Loads the image in the resources directory with the given file name.
     *
     * @param filename File name of the image.
     * @return Image found with the given file name
     */
    public static Image loadImage(String filename) {
        String path = "/";

        try {
            // Load image
            return Image.createImage(path + filename);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * Loads the image from url.
     * 
     * @param listener Listener containing callback functions
     * @param url Url where the image is located
     * @return Image loaded from the given url
     */
    public static Image loadImage(final ImageOperation.Listener listener,
        final String url) {
        new Thread() {
            public void run() {
                try {
                    // Load image
                    Image image = Image.createImage(url);
                    listener.imageReceived(url, image);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
        return null;
    }

    /**
     * Splits given text to lines.
     * 
     * @param text Text to be split
     * @param font Font of the given text
     * @param maxWidth Maximum width of text lines in pixels
     * 
     * @return List of lines (Strings)
     */
    public static Vector splitToLines(String text, Font font, int maxWidth) {
        Vector lines = new Vector();
        return splitToLines(lines, text, font, maxWidth);
    }

    private static Vector splitToLines(Vector lines, String text, Font font,
        int maxWidth) {
        int len = 0, lastSpace = 0;
        boolean newLine = false;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                lastSpace = i;
            }
            
            if (c == '\n') {
                newLine = true;
                lastSpace = i;
            }
            else {
                len = font.stringWidth(text.substring(0, i));
            }
            
            if (len > maxWidth || newLine) {
                lines.addElement(text.substring(0, lastSpace + 1).trim());
                return splitToLines(lines, text.substring(lastSpace + 1), font,
                    maxWidth);
            }
        }
        lines.addElement(text.trim());
        return lines;
    }

    
    /**
     * Shows an alert with given title and text for 2 seconds.
     * 
     * @param title Title to be shown
     * @param msg Alert message to be shown
     */
    public static void showAlert(String title, String msg) {
        Dialog validDialog = new Dialog(title);
        validDialog.setScrollable(false);
        validDialog.setTimeout(2000);

        Font font = Visual.MEDIUM_FONT;
        Vector lines = Util.splitToLines(msg, font, validDialog.getWidth() - 90);
        
        for (int i = 0; i < lines.size(); i++) {
            Label lineLabel = new Label((String) lines.elementAt(i));
            Style style = lineLabel.getStyle();
            style.setFont(font);
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
            
            validDialog.addComponent(lineLabel);
        }
        validDialog.show(20, 20, 20, 20, false, true);
    }

    /**
     * Creates a map marker.
     * 
     * @param id text which is shown in the marker
     * @param imagePath
     * @return map marker image
     * @throws IOException 
     */
    public static javax.microedition.lcdui.Image loadMapMarker(final String id, final String imagePath)
        throws IOException {
        javax.microedition.lcdui.Image background =
            (javax.microedition.lcdui.Image) loadImage(imagePath).getImage();
        
        int w = background.getWidth();
        int h = background.getHeight();
        javax.microedition.lcdui.Image image =
            javax.microedition.lcdui.Image.createImage(w, h);
        javax.microedition.lcdui.Graphics g = image.getGraphics();
        
        g.setColor(0xffffff);
        g.fillRect(0, 0, w, h);
        g.setColor(Visual.MAP_MARKER_COLOR);
        g.setFont(Visual.MAP_MARKER_FONT);
        
        g.drawString(id, w / 2, h / 2 + g.getFont().getHeight() / 2 - (isS60Phone() ? 3 : 5),
            javax.microedition.lcdui.Graphics.BOTTOM | javax.microedition.lcdui.Graphics.HCENTER);
        image = multiplyImages(background, image);
        
        return image;
    }

    private static javax.microedition.lcdui.Image multiplyImages(
        javax.microedition.lcdui.Image img1,
        javax.microedition.lcdui.Image img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight()
            != img2.getHeight()) {
            throw new IllegalArgumentException(
                "Sizes of the images must be same");
        }
        
        int[] rawImg1 = new int[img1.getHeight() * img1.getWidth()];
        img1.getRGB(rawImg1, 0, img1.getWidth(), 0, 0,
            img1.getWidth(), img1.getHeight());
        
        int[] rawImg2 = new int[img2.getHeight() * img2.getWidth()];
        img2.getRGB(rawImg2, 0, img2.getWidth(), 0, 0, img2.getWidth(), img2.
            getHeight());

        int mrgb, mr, mg, mb, a, r, g, b;
        for (int i = 0, l = rawImg1.length; i < l; i++) {
            mrgb = rawImg2[i] & 0xffffff;
            
            if (mrgb < 0xffffff) {
                mr = mrgb >>> 16;
                mg = (mrgb & 0xff00) >>> 8;
                mb = mrgb & 0xff;
                a = rawImg1[i] & 0xff000000;
                r = (((rawImg1[i] & 0xff0000) * mr) / 0xff) & 0xff0000;
                g = (((rawImg1[i] & 0xff00) * mg) / 0xff) & 0xff00;
                b = (((rawImg1[i] & 0xff) * mb) / 0xff) & 0xff;
                rawImg1[i] = a | r | g | b;
            }
        }

        return javax.microedition.lcdui.Image.createRGBImage(rawImg1,
            img1.getWidth(),
            img1.getHeight(),
            true);
    }

    public static boolean isS60Phone() {
        String platform = System.getProperty("microedition.platform");
        
        if (platform == null) {
            platform = "";
        }
        
        if (platform.indexOf("sw_platform=S60") > 0) {
            return true;
        }
        
        if (platform.indexOf("/S60_") > 0) {
            return true;
        }
        
        try {
            Class.forName("com.symbian.gcf.NativeInputStream");
            return true;
        }
        catch (ClassNotFoundException e) {
        }

        return false;
    }
}
