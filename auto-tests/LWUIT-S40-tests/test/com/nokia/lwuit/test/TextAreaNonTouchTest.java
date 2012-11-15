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
        //textarea.setFocus(true);
        waitEdt();
        Command[] cmds_before = f.getMenuBar().getSoftCommands();
        assertEquals("Edit", cmds_before[0].getCommandName());
        assertEquals("back", cmds_before[2].getCommandName());
        Class clazz = textarea.getClass();
        Method onClick = clazz.getDeclaredMethod("onClick");
        onClick.setAccessible(true);
        onClick.invoke(textarea);
        
        waitEdt();
        waitEdt();
        assertTrue(textarea.isTextEditorActive());
        Command[] cmds_after_click = f.getMenuBar().getSoftCommands();
        assertEquals("Clear", cmds_after_click[2].getCommandName());
        Dialog.show("title", "text", Dialog.TYPE_INFO, null, "ok", "cancel", 100);
        waitEdt();
        //make sure the dialog is not shown anymore
        Thread.sleep(100);
        waitEdt();
        MenuBar menubar = f.getMenuBar();
        Command [] cmds = menubar.getSoftCommands();
        assertEquals(back, cmds[2]);
        assertEquals("Edit", cmds[0].getCommandName());
    }
}
