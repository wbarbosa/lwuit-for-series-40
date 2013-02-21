/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.gesturesdemo;

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;

public class GesturesDemo extends MIDlet {

    public void startApp() {
        Display.init(this);

        ImageGrid imageGrid = new ImageGrid();
        try {
            Image[] images = new Image[6];
            images[0] = Image.createImage("/image1.jpg");
            images[1] = Image.createImage("/image2.jpg");
            images[2] = Image.createImage("/image3.jpg");
            images[3] = Image.createImage("/image4.jpg");
            images[4] = Image.createImage("/image5.jpg");
            images[5] = Image.createImage("/image6.jpg");
            for (int i = 0; i < images.length; i++) {
                ImageCell imageCell = new ImageCell(images[i]);
                imageGrid.addComponent(imageCell);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        imageGrid.show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
