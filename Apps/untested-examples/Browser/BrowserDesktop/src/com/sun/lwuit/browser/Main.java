/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.browser;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

/**
 * Java SE version of the Browser demo
 *
 * @author Shai Almog
 */
public class Main {
    public Main() {
        // for a full screen application use this code
       // Display.init(null);

        // for a windowed application use this code
        Frame f = new Frame("LWUIT Browser Demo");
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
        Storage.init("LWUITBrowser");

        //Form helloWorld = new Form("Hi World");
        //helloWorld.show();
        new BrowserApp().startApp();
    }

    public static void main(String[] argv) {
        new Main();
    }
}
