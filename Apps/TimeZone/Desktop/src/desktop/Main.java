/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package desktop;

import com.sun.lwuit.Display;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import userclasses.StateMachine;

/**
 * Main application version of the demo
 *
 * @author Shai Almog
 */
public class Main implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // for a full screen application use this code
        // Display.init(null);

        // for a windowed application use this code
        Frame f = new Frame("Friends t-Zone by LWUIT");
        f.setIconImage(new ImageIcon(Main.class.getResource("/icon.png")).getImage());
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Display.getInstance().exitApplication();
            }
        });
        f.setLayout(new java.awt.BorderLayout());
        Display.init(f);
        f.setSize(320, 480);
        f.validate();
        f.setLocationByPlatform(true);
        f.setVisible(true);

        Display.getInstance().callSerially(new Main());
    }

    public void run() {
        new StateMachine("/timeline.res");
    }
}
