/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.test.util.LWUITTest;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import org.junit.BeforeClass;
import org.junit.Test;
import org.microemu.device.Device;
import org.microemu.device.DeviceFactory;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
/**
 *
 * @author tkor
 */
public class MenuBarNonTouchTest extends LWUITTest{
    
    public MenuBarNonTouchTest() {
    }
    @BeforeClass
    public static void changeDeviceTypeToNonTouch() {
        Device device = spy(DeviceFactory.getDevice());
        when(device.hasPointerEvents()).thenReturn(false);
        DeviceFactory.setDevice(device);
    }
    @Test
    public void testIfSelectCmdDefaultNotInMSK() throws InterruptedException {
        final Form f = new Form();
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.show();
        Button b = new Button("test");
        f.addComponent(b);
        waitEdt();
        Command defaultcmds = new Command("def");
        f.setDefaultCommand(defaultcmds);
        waitEdt();
        f.setFocused(b);
        MenuBar menubar = f.getMenuBar();
        Command [] softCmds = menubar.getSoftCommands();
        assertEquals("Select", softCmds[0].getCommandName());
    }
    @Test
    public void testIfSelectThenDefaultInMenu() throws InterruptedException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Form f = new Form();
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.show();
        Button b = new Button("test");
        f.addComponent(b);
        waitEdt();
        Command defaultcmd = new Command("def");
        f.setDefaultCommand(defaultcmd);
        waitEdt();
        f.setFocused(b);
        MenuBar menubar = f.getMenuBar();
        Command [] softCmds = menubar.getSoftCommands();
        assertEquals("Options", softCmds[1].getCommandName());
        Method createCommandComponent = MenuBar.class.getDeclaredMethod("createCommandComponent", Vector.class);
        createCommandComponent.setAccessible(true);
        Method commandsmethod = MenuBar.class.getDeclaredMethod("getCommands");
        commandsmethod.setAccessible(true);
        Vector cmds = (Vector) commandsmethod.invoke(menubar);
        Component cmp = (Component) createCommandComponent.invoke(menubar, cmds);
        
        List l = (List) cmp;
        assertEquals(1, l.getModel().getSize());
        ListModel model = l.getModel();
        Command cmd = (Command) model.getItemAt(0);
        assertEquals(defaultcmd, cmd);

    }
    @Test
    public void testDefaultIsInMSKandNoOptionsIsShown() throws InterruptedException {
        final Form f = new Form();
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.show();
        waitEdt();
        Command def = new Command("def");
        f.setDefaultCommand(def);
        waitEdt();
        MenuBar menubar = f.getMenuBar();
        Command [] softcmd = menubar.getSoftCommands();
        assertEquals(def, softcmd[0]);
        assertNull(softcmd[1]);
        assertNull(softcmd[2]);
    }
}
