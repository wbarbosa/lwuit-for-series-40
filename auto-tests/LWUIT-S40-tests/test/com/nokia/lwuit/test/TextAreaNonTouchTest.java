/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.TextEditorProvider;
import com.nokia.lwuit.test.util.LWUITTest;
import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.TextArea;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.microemu.device.Device;
import org.microemu.device.DeviceFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/**
 *
 * @author tkor
 */
public class TextAreaNonTouchTest extends LWUITTest{
    
    
    public TextAreaNonTouchTest() {
    }
    
    @BeforeClass
    public static void changeDeviceTypeToNonTouch() {
        Device device = spy(DeviceFactory.getDevice());
        when(device.hasPointerEvents()).thenReturn(false);
        DeviceFactory.setDevice(device);
    }
    
    @Test
    public void testIfNonTouchAndDialogOpenedCloseEditMode() throws InterruptedException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        assertEquals(Display.NON_TOUCH_DEVICE, Display.getInstance().getDeviceType());
        assertEquals(Display.getInstance().isTouchScreenDevice(), false);
        final Form f = new Form();
        
        waitEdt();
        Command back = new Command("back");
        f.setBackCommand(back);
        waitEdt();
        TextArea textarea = new TextArea("text");
        final TextEditorProvider editor = createMockTextEditor();
        when(editor.isVisible()).thenReturn(false);
        when(editor.getContent()).thenReturn("text");
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(editor.isVisible()).thenReturn(Boolean.TRUE);
                return null;
            }
        }).when(editor).setVisible(true);
        
        Field texteditor = TextArea.class.getDeclaredField("textEditor");
        texteditor.setAccessible(true);
        texteditor.set(textarea, editor);
        
        f.addComponent(textarea);
        f.show();
        waitEdt();
        f.setFocused(textarea);
        waitEdt();
        f.removeComponent(textarea);
        waitEdt();
        MenuBar menubar = f.getMenuBar();
        Command [] softcmds = menubar.getSoftCommands();
        assertEquals(back, softcmds[2]);
        
    }
    @Test
    public void testReinitializingTextAreaResetsEditToMSKMode() throws InterruptedException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Form f = new Form();
        
        waitEdt();
        Command back = new Command("back");
        f.setBackCommand(back);
        waitEdt();
        TextArea textarea = new TextArea("text");
        final TextEditorProvider editor = createMockTextEditor();
        when(editor.isVisible()).thenReturn(false);
        when(editor.getContent()).thenReturn("text");
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(editor.isVisible()).thenReturn(Boolean.TRUE);
                return null;
            }
        }).when(editor).setVisible(true);
        
        Field texteditor = TextArea.class.getDeclaredField("textEditor");
        texteditor.setAccessible(true);
        texteditor.set(textarea, editor);
        
        f.addComponent(textarea);
        f.show();
        waitEdt();
        f.setFocused(textarea);
        waitEdt();
        Method onClick = TextArea.class.getDeclaredMethod("onClick");
        onClick.setAccessible(true);
        onClick.invoke(textarea);
        waitEdt();
        
        
        Method deinitialize = TextArea.class.getDeclaredMethod("deinitialize");
        deinitialize.setAccessible(true);
        deinitialize.invoke(textarea);
        waitEdt();
        Method initComponentImpl = TextArea.class.getDeclaredMethod("initComponentImpl");
        initComponentImpl.setAccessible(true);
        initComponentImpl.invoke(textarea);
        waitEdt();
        MenuBar menubar = f.getMenuBar();
        Command[] softcmds = menubar.getSoftCommands();
        assertEquals("Edit", softcmds[0].getCommandName());
        
    }
}
