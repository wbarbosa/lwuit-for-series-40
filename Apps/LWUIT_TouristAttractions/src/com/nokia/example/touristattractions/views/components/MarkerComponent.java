/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views.components;

import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;

/**
 * Component showing a (map) marker image with an id.
 */
public class MarkerComponent
    extends Component {

    private static Image markerImage;
    private String id;

    public MarkerComponent() {
        // Load base image only once
        if (markerImage == null) {
            markerImage = Util.loadImage("location.png");
        }
        getStyle().setBgTransparency(0x00);
    }

    public void paint(Graphics g) {
        int baseX = getX();
        int baseY = getY();

        g.drawImage(markerImage, baseX, baseY);
        g.setColor(Visual.LIST_PRIMARY_COLOR);
        
        if (id != null) {
            g.setFont(Visual.SMALL_FONT);
            
            // Because font sizes differ in devices, the coordinates vary depending on resolution.
            int x = baseX + (markerImage.getWidth() - Visual.SMALL_FONT.stringWidth(id)) / 2;
            int offsetY = (Compatibility.SCREEN_SIZE == Compatibility.RES_240x320
                || Compatibility.SCREEN_SIZE == Compatibility.RES_320x240 ? 1
                : (Compatibility.IS_FULLTOUCH ? 3 : -1));
            int y = baseY + offsetY;
            g.drawString(id, x, y);
        }
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(markerImage.getWidth(), markerImage.getHeight());
    }

    public void setId(String id) {
        this.id = id;
        repaint();
    }

    public static void drawMarker(Graphics g, String id, int x, int y) {
        g.drawImage(markerImage, x - markerImage.getWidth() / 2,
            y - markerImage.getHeight());
        if (id != null) {
            g.setFont(Visual.SMALL_FONT);
            g.drawString(id, x - Visual.SMALL_FONT.stringWidth(id) / 2,
                y - markerImage.getHeight() + 3);
        }
    }
}