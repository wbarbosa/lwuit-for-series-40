/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.test.util.BaseTest;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkor
 */
public class MenuBarTest extends BaseTest{
    
    
    @Test
    public void testDefaultCommandIsSet() {
        DummyMidlet m = new DummyMidlet();
        m.startApplication();
        Form f = new Form();
        Command def = new Command("def");
        f.setDefaultCommand(def);
        assertEquals(def, f.getDefaultCommand());
        
    }
    
    private static class DummyMidlet extends MIDlet {
        @Override
            protected void startApp() throws MIDletStateChangeException {
                Display.init(this);
            }

            @Override
            protected void pauseApp() {

            }

            @Override
            protected void destroyApp(boolean bln) throws MIDletStateChangeException {

            }
            public void startApplication() {
            try {
                startApp();
            } catch (MIDletStateChangeException ex) {
                System.out.println("midletstatechangeexception");
            }
            }
    }
}
