/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.TextEditorProvider;
import com.nokia.lwuit.test.util.LWUITTest;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.TextArea;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author tkor
 */
public class MenuBarTest extends LWUITTest{

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
    public void testSettingClearOverridesBack() throws InterruptedException {
        final Form f = new Form();
        final Command clear = new Command("clear");
        Command back = new Command("back");
        f.show();
        waitEdt();
        f.setBackCommand(back);
        f.setClearCommand(clear);
        waitEdt();
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
        waitEdt();
        assertTrue(f.isVisible());
        assertTrue(area.isTextEditorActive());
        
        
        MenuBar m = f.getMenuBar();
        assertNotNull(m.getClearCommand());
        Command[] softs = m.getSoftCommands();
        assertEquals("Clear", softs[2].getCommandName());
        
        
    }
    
    @Test
    public void testWhenClearRemovedRecoverPreviousBack() throws InterruptedException {
        final Form f = new Form();
        f.show();
        final Command back = new Command("back");
        Command clear = new Command("clear");
        f.setBackCommand(back);
        waitEdt();
        f.setClearCommand(clear);
        waitEdt();
        f.removeCommand(clear);
        waitEdt();
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
        waitEdt();
        area.pointerReleased(5, 5);
        area.setFocus(true);
        waitEdt();
        assertTrue(area.isTextEditorActive());
        waitEdt();
        waitEdt();
        waitEdt();
        Command [] softs = f.getMenuBar().getSoftCommands();
        assertEquals("Clear", softs[2].getCommandName());
        f.getMenuBar().getSoftButtons()[2].released();
        waitEdt();
        softs = f.getMenuBar().getSoftCommands();
        assertEquals(back, softs[2]);
        
    }
    @Test
    public void testWhenClearShownBackIsNotInMenu() throws InterruptedException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Form f = new Form();
        f.show();
        waitEdt();
        Command back = new Command("Back");
        Command singlecmd = new Command("item 1");
        f.addCommand(singlecmd);
        f.setBackCommand(back);
        waitEdt();
        Command clear = new Command("Clear");
        f.setClearCommand(clear);
        waitEdt();
        MenuBar menu = f.getMenuBar();
        Class clazz = menu.getClass();
        Method createMenu = clazz.getDeclaredMethod("createCommandComponent", Vector.class);
        createMenu.setAccessible(true);
        Method getCommands = clazz.getDeclaredMethod("getCommands");
        getCommands.setAccessible(true);
        Component c = (Component)createMenu.invoke(menu, getCommands.invoke(menu));
        assertNotNull(c);
        boolean foundback = false;
        if(c instanceof Container) {
            Container cont = (Container) c;
            int l = cont.getComponentCount();
            for(int i = 0; i < l; i++) {
                Button b = (Button)cont.getComponentAt(i);
                if(b.getCommand() == back) {
                    foundback = true;
                    break;
                }
            }
        }
        assertFalse(foundback);
    }
    
    @Test
    public void testClearIsNotShownInMenu() throws InterruptedException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
         final Form f = new Form();
        f.show();
        waitEdt();
        Command back = new Command("Back");
        f.setBackCommand(back);
        Command singleCmd = new Command("item 1");
        f.addCommand(singleCmd);
        waitEdt();
        Command clear = new Command("Clear");
        f.setClearCommand(clear);
        waitEdt();
        MenuBar menu = f.getMenuBar();
        Class clazz = menu.getClass();
        Method createMenu = clazz.getDeclaredMethod("createCommandComponent", Vector.class);
        createMenu.setAccessible(true);
        Method getCommands = clazz.getDeclaredMethod("getCommands");
        getCommands.setAccessible(true);
        Component c = (Component)createMenu.invoke(menu, getCommands.invoke(menu));
        assertNotNull(c);
        boolean foundclear = false;
        if(c instanceof Container) {
            Container cont = (Container) c;
            int l = cont.getComponentCount();
            for(int i = 0; i < l; i++) {
                Button b = (Button)cont.getComponentAt(i);
                if(b.getCommand() == clear) {
                    foundclear = true;
                    break;
                }
            }
        }
        assertFalse(foundclear);
    }
    
    @Test
    public void testOptionsIsNotShownIfClearOverridesBack() throws InterruptedException {
        Form f = new Form();
        Command back = new Command("back");
        Command clear = new Command("clear");
        f.setBackCommand(back);
        f.setClearCommand(clear);
        f.show();
        waitEdt();
        MenuBar menubar = f.getMenuBar();
        Command [] cmds = menubar.getSoftCommands();
        assertNull(cmds[1]);
        
        
    }
}