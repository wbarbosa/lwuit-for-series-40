/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.TextEditorProvider;
import com.nokia.lwuit.test.util.BaseTest;
import com.nokia.lwuit.test.util.DummyMidlet;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.TextArea;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
    
    @Test
    public void testClearIsShownWhenTextAreaFocused() throws InterruptedException, NoSuchFieldException, IllegalAccessException{
        final Form f = new Form();

        TextArea area = new TextArea();
        area.setText("test");
        TextEditorProvider teprovider = mock(TextEditorProvider.class);
        when(teprovider.isVisible()).thenReturn(true);
        when(teprovider.getFont()).thenReturn(Font.getDefaultFont());
        when(teprovider.getLineMarginHeight()).thenReturn(2);
        
        Field texteditor = TextArea.class.getDeclaredField("textEditor");
        texteditor.setAccessible(true);
        texteditor.set(area, teprovider);
        
        f.addComponent(area);
        f.show();
        area.pointerPressed(5, 5);
        area.pointerReleased(5, 5);
        area.setFocus(true);
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized(this) {
                    notifyAll();
                }
            }
        };
        Display.getInstance().callSerially(runnable);
        synchronized(runnable) {
            runnable.wait(2000);
        }
        assertTrue(f.isVisible());
        assertTrue(area.isTextEditorActive());
        
        
        MenuBar m = f.getMenuBar();
        assertNotNull(m.getClearCommand());
        Command[] softs = m.getSoftCommands();
        assertEquals("Clear", softs[2].getCommandName());
        
        
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
    @Test
    public void testClearWillDisappearWhenLastCharDeleted() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        final Form f = new Form();
        f.show();
        final TextArea area = new TextArea("t");
        Command back = new Command("back");
        f.setBackCommand(back);
        
        final TextEditorProvider teprovider = mock(TextEditorProvider.class);
        when(teprovider.isVisible()).thenReturn(true);
        when(teprovider.getFont()).thenReturn(Font.getDefaultFont());
        when(teprovider.getLineMarginHeight()).thenReturn(2);
        when(teprovider.getCaretPosition()).thenReturn(1);
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(teprovider.getContent()).thenReturn("");
                area.inputAction(teprovider, TextEditorProvider.TextEditorListener.ACTION_CONTENT_CHANGE | TextEditorProvider.TextEditorListener.ACTION_CARET_MOVE);
                return null;
            }
        }).when(teprovider).delete(anyInt(), anyInt());
        
        Field texteditor = TextArea.class.getDeclaredField("textEditor");
        texteditor.setAccessible(true);
        texteditor.set(area, teprovider);
        
        f.addComponent(area);
        area.pointerPressed(5, 5);
        area.pointerReleased(5, 5);
        area.setFocus(true);
        waitEdt();
        int h = Display.getInstance().getDisplayHeight();
        int w = Display.getInstance().getDisplayWidth();
        assertTrue(area.isTextEditorActive());
        Command [] softs = f.getMenuBar().getSoftCommands();
        assertEquals("Clear", softs[2].getCommandName());
        f.getMenuBar().getSoftButtons()[2].released();
        waitEdt();
        softs = f.getMenuBar().getSoftCommands();
        assertEquals(back, softs[2]);
        
    }
    
    private void waitEdt() throws InterruptedException {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized(this) {
                    notifyAll();
                }
            }
        };
        Display.getInstance().callSerially(runnable);
        synchronized(runnable) {
            runnable.wait(2000);
        }
    }
    
    
    @AfterClass
    public static void killLWUIT() {
        if(m != null) {
            m.stopApplication();
        }
    }
    
}