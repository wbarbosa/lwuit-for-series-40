/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package userclasses;

import com.sun.lwuit.Display;
import javax.microedition.midlet.*;

/**
 * The main MIDlet for the demo
 *
 * @author Shai Almog
 */
public class MainMIDlet extends MIDlet implements Runnable {
    public void startApp() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/timeline.res");
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
