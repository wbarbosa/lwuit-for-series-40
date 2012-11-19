/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.test.util.LWUITTest;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BoxLayout;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.microemu.device.Device;
import org.microemu.device.DeviceFactory;
import static org.mockito.Mockito.*;
/**
 *
 * @author tkor
 */
public class TextFieldNonTouchTest extends LWUITTest{
    
    public TextFieldNonTouchTest() {
    }
    
    @BeforeClass
    public static void changeDeviceTypeToNonTouch() {
        Device device = spy(DeviceFactory.getDevice());
        when(device.hasPointerEvents()).thenReturn(false);
        DeviceFactory.setDevice(device);
    }
    
    @Test
    public void testTextFieldReturnsBackToRSKAfterEditWithoutTextEditor() throws InterruptedException {
        final Form f = new Form();
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Command back = new Command("back");
        f.setBackCommand(back);
        
        TextField field = new TextField();
        f.addComponent(field);
        f.show();
        waitEdt();
        f.setFocused(field);
        MenuBar menubar = f.getMenuBar();
        Button [] softButtons = menubar.getSoftButtons();
        softButtons[0].released();
        waitEdt();
        softButtons[0].released();
        waitEdt();
        Command [] cmds = menubar.getSoftCommands();
        assertEquals(back, cmds[2]);
        
        
    }
}
