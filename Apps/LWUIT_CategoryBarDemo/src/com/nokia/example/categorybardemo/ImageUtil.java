/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.categorybardemo;

import com.sun.lwuit.Image;
import java.io.IOException;

/**
 * Contains helper a method for loading images.
 */
public class ImageUtil {

    /**
     * Loads the image in the resources directory with the given path.
     *
     * @param path Path of the image.
     * @return Image found with the given path
     */
    public static Image loadImage(String path) {
        try {
            // load image
            return Image.createImage(path);
        }
        catch (IOException ioe) {
        }
        return null;
    }
}
