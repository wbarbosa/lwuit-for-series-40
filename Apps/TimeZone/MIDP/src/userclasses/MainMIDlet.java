/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package userclasses;

import com.sun.lwuit.Display;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.impl.s40.S40Implementation;
import javax.microedition.midlet.*;

/**
 * The main MIDlet for the demo
 *
 * @author Shai Almog
 */
public class MainMIDlet extends MIDlet implements Runnable {
    public void startApp() {
        Display.init(this);
        LWUITImplementation impl = Display.getInstance().getImplementation();
        if(impl instanceof S40Implementation) {
            ((S40Implementation)impl).setHideMenu(true);
        }
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
