/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;
import net.rim.device.api.ui.UiApplication;

/**
 * Main RIM application for the LWUIT demo
 *
 * @author Shai Almog
 */
public class Main extends UiApplication implements Runnable {
    public Main() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public static void main(String[] argv) {
        new Main();
    }

    public void run() {
            new UIDemoMain().startApp();
    }
}
