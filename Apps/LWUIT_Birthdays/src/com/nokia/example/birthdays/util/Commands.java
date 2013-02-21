/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.util;

import com.sun.lwuit.Image;
import java.io.IOException;

public class Commands {
    
    public static final String ADD_COMMAND_IMAGE = "add_icon.png";
    
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
}
