/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */


package userclasses;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.Storage;
import net.rim.device.api.ui.UiApplication;

/**
 * Blackberry version of tipster
 * @author Shai Almog
 */
public class MainMIDlet extends UiApplication implements Runnable {
    public MainMIDlet() {
        Display.init(this);
        Storage.init("Tipster");
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/tipster.res");
    }
    
    public static void main(String[] arg) {
        new MainMIDlet();
    }
}
