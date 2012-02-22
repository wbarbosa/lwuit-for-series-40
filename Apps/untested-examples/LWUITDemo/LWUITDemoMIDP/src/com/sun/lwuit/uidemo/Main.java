/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;
import javax.microedition.midlet.*;

/**
 * Main MIDlet for the LWUIT demo
 *
 * @author Shai Almog
 */
public class Main extends MIDlet {
    private UIDemoMain main = new UIDemoMain();
    public void startApp() {
            Display.init(this);
            main.startApp();
    }

    public void pauseApp() {
        main.pauseApp();
    }

    public void destroyApp(boolean unconditional) {
        main.destroyApp(unconditional);
    }
}
