/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.me.web.sample.local;

import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import com.sun.lwuit.Display;
import net.rim.device.api.ui.UiApplication;

/**
 * RIM version of the makeover demo
 *
 * @author Shai Almog
 */
public class Main extends UiApplication implements Runnable {
    private LocalApp app = new LocalApp();

    public Main() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public static void main(String[] argv) {
        new Main();
    }

    public void run() {
        NetworkManager.getInstance().start();
        Storage.init("Makeover");
        app.startApp();
    }
}
