/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import java.io.IOException;

public class SplashView
        extends Form {

    private Image splash = null;

    public SplashView() {
        super("");
        try {
            splash = Image.createImage("/splash.png");
        }
        catch (IOException ioe) {
            System.out.println("Cannot load splash image.");
        }
        Display.getInstance().setForceFullScreen(true);
    }

    public void paint(Graphics g) {
        g.setColor(0xdfeafb);
        g.fillRect(0, 0, getWidth(), getHeight());
        int x = (getWidth() - splash.getWidth()) / 2;
        int y = (getHeight() - splash.getHeight()) / 2;
        g.drawImage(splash, x, y);
    }
}
