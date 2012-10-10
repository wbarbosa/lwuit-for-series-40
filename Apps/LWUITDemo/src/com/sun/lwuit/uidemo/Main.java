/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import javax.microedition.midlet.*;

/**
 * Main MIDlet for the LWUIT demo
 *
 * @author Shai Almog
 */
public class Main
    extends MIDlet {

    private UIDemoMain main = new UIDemoMain();

    public void startApp() {
        Display.init(this);
        
        // Show splash screen
        try {
            Resources res = Resources.open("/images.res");
            new SplashScreen(res.getImage("splash.png")).show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Start initializing main view in the background
        long start = System.currentTimeMillis();
        main.startApp();
        long difference = System.currentTimeMillis() - start;
        
        try {
            // Show splash view for at least a second
            Thread.sleep(Math.max(0, 1000 - difference));
        }
        catch (InterruptedException e) {
        }
        
        UIDemoMain.getMainForm().show();
    }

    public void pauseApp() {
        main.pauseApp();
    }

    public void destroyApp(boolean unconditional) {
        main.destroyApp(unconditional);
    }
}
