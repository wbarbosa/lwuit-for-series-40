/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test.util;

import com.sun.lwuit.Display;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author tkor
 */
public class DummyMidlet extends MIDlet{

        @Override
            protected void startApp() throws MIDletStateChangeException {
                Display.init(this);
            }

            @Override
            protected void pauseApp() {

            }

            @Override
            protected void destroyApp(boolean bln) throws MIDletStateChangeException {
                Display.deinitialize();
            }
            public void startApplication() {
            try {
                startApp();
            } catch (MIDletStateChangeException ex) {
                System.out.println("midletstatechangeexception");
            }
            }
            public void stopApplication() {
                this.notifyDestroyed();
            try {
                destroyApp(true);
            } catch (MIDletStateChangeException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            }
    
}
