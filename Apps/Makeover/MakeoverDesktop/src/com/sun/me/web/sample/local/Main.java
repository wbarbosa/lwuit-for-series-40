/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.me.web.sample.local;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

/**
 * Desktop version of the makeover demo
 *
 * @author Shai Almog
 */
public class Main {
    private LocalApp app = new LocalApp();

    public Main() {
        // for a full screen application use this code
       // Display.init(null);

        // for a windowed application use this code
        Frame f = new Frame("LWUIT Makeover");
        f.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Display.getInstance().exitApplication();
            }
        });
        f.setLayout(new java.awt.BorderLayout());
        f.setSize(320, 480);
        Display.init(f);
        f.validate();
        f.setLocationByPlatform(true);
        f.setVisible(true);
        NetworkManager.getInstance().start();
        Storage.init("Makeover");
        app.startApp();
    }

    public static void main(String[] argv) {
        new Main();
    }
}
