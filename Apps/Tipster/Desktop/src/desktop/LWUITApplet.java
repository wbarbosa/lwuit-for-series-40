/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */


package desktop;

import com.sun.lwuit.Display;
import java.applet.Applet;
import userclasses.StateMachine;

/**
 * Applet version of tipster
 *
 * @author Shai Almog
 */
public class LWUITApplet extends Applet implements Runnable {

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init() {
        setLayout(new java.awt.BorderLayout());
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/tipster.res");
    }
}
