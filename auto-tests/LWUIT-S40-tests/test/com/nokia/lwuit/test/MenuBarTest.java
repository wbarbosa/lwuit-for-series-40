/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.test.util.BaseTest;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author tkor
 */
public class MenuBarTest extends BaseTest{
    
    static DummyMidlet m;
    
    @BeforeClass
    public static  void initMidletAndLWUIT() {
        if(m == null) {
        m = new DummyMidlet();
        m.startApplication();
        }
    }
    
    @Test
    public void testDefaultCommandIsSet() {
        
        Form f = new Form();
        Command def = new Command("def");
        f.setDefaultCommand(def);
        assertEquals(def, f.getDefaultCommand());
        
    }
    @Test
    public void testClearIsShownWhenTextAreaFocused() {
        final Form f = new Form();

        TextArea area = new TextArea();
        f.addComponent(area);
        System.out.println("before show");
        f.show();
        System.out.println("after show");
        assertNotNull(f.getClearCommand());
       
        
    }
    @AfterClass
    public static void killLWUIT() {
        if(m != null) {
            m.stopApplication();
        }
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
                Logger.getLogger(MenuBarTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }
}
