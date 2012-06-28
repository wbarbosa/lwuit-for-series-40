/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package userclasses;

import com.sun.lwuit.Display;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.impl.s40.S40Implementation;
import com.sun.lwuit.io.Storage;
import javax.microedition.midlet.*;

/**
 * MIDP version of tipster
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
        Storage.init("Tipster");
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/tipster.res");
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
