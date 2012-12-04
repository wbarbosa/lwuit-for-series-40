/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;
import java.applet.Applet;

/**
 * Embeddable applet useful for demos
 *
 * @author Shai Almog
 */
public class LWUITApplet extends Applet {
    private UIDemoMain u;
    
    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init() {
        setLayout(new java.awt.BorderLayout());
        if(u == null) {
            u = new UIDemoMain();
            Display.init(this);
        }
        u.startApp();
    }

    public void destroy() {
        Display.deinitialize();
    }

    public void stop() {
        u.pauseApp();
    }
}
