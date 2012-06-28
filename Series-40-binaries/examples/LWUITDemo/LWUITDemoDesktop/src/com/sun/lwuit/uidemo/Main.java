/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

/**
 * Java SE version of the LWUIT demo, easy for fast running debugging
 *
 * @author Shai Almog
 */
public class Main {
    public Main() {
        // for a full screen application use this code
       // Display.init(null);

        // for a windowed application use this code
        Frame f = new Frame("LWUIT Demo");
        f.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Display.getInstance().exitApplication();
            }
        });
        f.setLayout(new java.awt.BorderLayout());
        f.setSize(480, 800);
        Display.init(f);
        f.validate();
        f.setLocationByPlatform(true);
        f.setVisible(true);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIDemoMain().startApp();
            }
        });
    }

    public static void main(String[] argv) {
        new Main();
    }
}
