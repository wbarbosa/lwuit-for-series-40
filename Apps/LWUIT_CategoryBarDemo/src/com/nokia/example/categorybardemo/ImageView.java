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
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.IOException;

/**
 * A simple view which shows an image.
 */
public class ImageView
    extends View {

    /**
     * Constructor
     */
    public ImageView() {
        super("image", "/categorybar_image.png");

        setTitle("image");
        BorderLayout layout = new BorderLayout();
        layout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(layout);
        
        try {
            Label image = new Label(Image.createImage("/image.jpg"));
            addComponent(BorderLayout.CENTER, image);
        }
        catch (IOException e) {
            // should not occur
        }
    }
}
