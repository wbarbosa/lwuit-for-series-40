/*
 *  Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.browser;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import net.rim.device.api.ui.UiApplication;

/**
 * RIM port of the browser app
 *
 * @author Shai Almog
 */
public class Browser extends UiApplication {
    public Browser() {
        Display.init(this);
        NetworkManager.getInstance().start();
        Storage.init("LWUITBrowser");
        new BrowserApp().startApp();
    }

    public static void main(String[] argv) {
        new Browser();
    }
}
