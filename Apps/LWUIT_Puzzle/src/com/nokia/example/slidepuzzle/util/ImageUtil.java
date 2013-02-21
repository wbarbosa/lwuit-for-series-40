/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.util;

import com.nokia.mid.ui.DirectUtils;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import java.io.IOException;

public class ImageUtil {

    private static Image digitsImg; // holds the images of digits 0-9

    /**
     * Load the image in the resources directory with the given file name.
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
     * Return a number (0-999) as an image.
     *
     * @param num the number wanted as an image
     * @return Image with the given number drawn in it.
     */
    public static Image getNumberAsImage(int num) {
        if (num < 0) {
            return null;
        }

        if (num > 999) {
            return getNumberAsImage(num % 1000);
        }

        if (digitsImg == null) {
            digitsImg = loadImage("numbers.png");
        }
        
        if (num < 10) {
            return getDigitAsImage(num);
        }

        if (num < 100) {
            javax.microedition.lcdui.Image nativeImg = DirectUtils.createImage(2
                * Compatibility.DIGIT_WIDTH, Compatibility.DIGIT_HEIGHT, 0x00000000);
            Image numImg = Image.createImage(nativeImg);
            Graphics g = numImg.getGraphics();
            g.drawImage(getDigitAsImage(num / 10), 0, 0);
            g.drawImage(getDigitAsImage(num % 10), Compatibility.DIGIT_WIDTH, 0);
            return numImg;
        }

        if (num < 1000) {
            javax.microedition.lcdui.Image nativeImg = DirectUtils.createImage(3
                * Compatibility.DIGIT_WIDTH, Compatibility.DIGIT_HEIGHT, 0x00000000);
            Image numImg = Image.createImage(nativeImg);
            Graphics g = numImg.getGraphics();
            g.drawImage(getDigitAsImage(num / 100), 0, 0);
            g.drawImage(getDigitAsImage(num % 100 / 10), Compatibility.DIGIT_WIDTH, 0);
            g.drawImage(getDigitAsImage(num % 10), 2 * Compatibility.DIGIT_WIDTH, 0);
            return numImg;
        }
        
        return null;
    }

    /**
     * Return a version number as an image.
     *
     * @param version The version number in form "x.x"
     * @return An image representing the given version number
     */
    public static Image getVersionNumberAsImage(String version) {
        if (digitsImg == null) {
            digitsImg = loadImage("numbers.png");
        }

        int first = Character.digit(version.charAt(0), 10);
        int second = Character.digit(version.charAt(2), 10);

        javax.microedition.lcdui.Image nativeImg = DirectUtils.createImage((int) (2.5f
            * Compatibility.DIGIT_WIDTH), Compatibility.DIGIT_HEIGHT, 0x00000000);
        Image numImg = Image.createImage(nativeImg);
        Graphics g = numImg.getGraphics();
        g.drawImage(getDigitAsImage(first), 0, 0);
        g.drawImage(getPointAsImage(), (int) (0.5f * Compatibility.DIGIT_WIDTH), 0);
        g.drawImage(getDigitAsImage(second), (int) (1.3f * Compatibility.DIGIT_WIDTH), 0);
        
        return numImg;
    }

    private static Image getDigitAsImage(int num) {
        return digitsImg.subImage(num * Compatibility.DIGIT_WIDTH, 0, Compatibility.DIGIT_WIDTH,
            Compatibility.DIGIT_HEIGHT, true);
    }

    private static Image getPointAsImage() {
        return digitsImg.subImage(10 * Compatibility.DIGIT_WIDTH, 0, Compatibility.DIGIT_WIDTH,
            Compatibility.DIGIT_HEIGHT, true);
    }
}
