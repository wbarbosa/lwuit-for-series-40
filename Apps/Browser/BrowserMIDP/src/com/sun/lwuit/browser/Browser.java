/*
 *  Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.browser;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import javax.microedition.midlet.*;

/**
 * MIDP port of the browser app
 *
 * @author Shai Almog
 */
public class Browser extends MIDlet {
    private BrowserApp app;
    public Browser() {
        Display.init(this);
        NetworkManager.getInstance().start();
        Storage.init("LWUITBrowser");
        app = new BrowserApp();
    }

    public void startApp() {
        app.startApp();
    }

    public void pauseApp() {
        app.pauseApp();
    }

    public void destroyApp(boolean unconditional) {
        app.destroyApp(unconditional);
    }
}
