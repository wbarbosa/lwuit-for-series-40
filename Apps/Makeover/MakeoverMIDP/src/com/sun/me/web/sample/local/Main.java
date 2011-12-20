/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.me.web.sample.local;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import javax.microedition.midlet.*;

/**
 * MIDP version of the makeover demo
 *
 * @author Shai Almog
 */
public class Main extends MIDlet {
    private LocalApp app = new LocalApp();

    public void startApp() {
        Display.init(this);
        NetworkManager.getInstance().start();
        Storage.init("Makeover");
        app.startApp();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
