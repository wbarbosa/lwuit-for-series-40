/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.test.util.BaseTest;
import com.nokia.lwuit.test.util.DummyMidlet;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.TextArea;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;

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
    public void testDeviceType() {
        
        int type = Display.getInstance().getDeviceType();
        assertEquals(Display.TOUCH_AND_TYPE_DEVICE, type);
    }
    @Test
    public void testCommandBehavior() {
        int b = Display.getInstance().getCommandBehavior();
        assertNotSame(10, b);
    }
    @Test
    public void testSettingClearOverridesBack() {
        final Form f = new Form();
        final Command clear = new Command("clear");
        Command back = new Command("back");
        f.show();
        f.setBackCommand(back);
        f.setClearCommand(clear);
        MenuBar m = f.getMenuBar();

        Command[] softkeys = m.getSoftCommands();
        assertEquals(clear, softkeys[2]);
       
        
    }
    @Ignore("no texteditor mock yet")
    @Test
    public void testClearIsShownWhenTextAreaFocused() {
        final Form f = new Form();

        TextArea area = new TextArea();
        f.addComponent(area);
        f.show();
        area.requestFocus();
        
        assertNotNull(f.getClearCommand());
       
        
    }
    @Test
    public void testWhenClearRemovedRecoverPreviousBack() {
        final Form f = new Form();
        f.show();
        final Command back = new Command("back");
        Command clear = new Command("clear");
        f.setBackCommand(back);
        f.setClearCommand(clear);
        f.removeCommand(clear);

        Command[] softs = f.getMenuBar().getSoftCommands();
        
        assertEquals(back, softs[2]);

    }
    @AfterClass
    public static void killLWUIT() {
        if(m != null) {
            m.stopApplication();
        }
    }
    
}
