/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.gesturesdemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;

public class ImageCell extends Button {

    private Image image;

    public ImageCell(Image image) {
        this.image = image;
        getStyle().setMargin(0, 0, 0, 0);
        getStyle().setPadding(0, 0, 0, 0);
        getPressedStyle().setMargin(0, 0, 0, 0);
        getPressedStyle().setPadding(0, 0, 0, 0);
        setFocusable(false);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void paint(Graphics g) {

        // Scale image to fit the component while preserving the aspect ratio by cropping
        if (image.getWidth() > image.getHeight()) {
            double scale = (double) getHeight() / image.getHeight();
            int w = (int) (image.getWidth() * scale);
            int h = getHeight();
            int xOffset = (int) (w - getWidth()) / 2;
            g.drawImage(image, getX() - xOffset, getY(), w, h);
        } else {
            double scale = (double) getWidth() / image.getWidth();
            int w = getWidth();
            int h = (int) (image.getHeight() * scale);
            int yOffset = (int) (h - getHeight()) / 2;
            g.drawImage(image, getX(), getY() - yOffset, w, h);
        }

    }
}
