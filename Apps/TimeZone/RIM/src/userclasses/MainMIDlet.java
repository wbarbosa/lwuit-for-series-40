/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package userclasses;

import com.sun.lwuit.Display;
import net.rim.device.api.ui.UiApplication;

/**
 * The main RIM application for the demo
 * @author Shai Almog
 */
public class MainMIDlet extends UiApplication implements Runnable {
    public MainMIDlet() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/timeline.res");
    }
    
    public static void main(String[] arg) {
        new MainMIDlet();
    }
}
