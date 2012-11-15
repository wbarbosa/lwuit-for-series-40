/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test.util;

import com.nokia.lwuit.TextEditorProvider;
import com.sun.lwuit.Display;
import javax.microedition.lcdui.Font;
import org.junit.After;
import org.junit.Before;
import org.microemu.MIDletBridge;
import org.microemu.MIDletContext;
import static org.mockito.Mockito.*;

/**
 *
 * @author tkor
 */
public class LWUITTest extends BaseTest{
    
    static DummyMidlet m;
        
            
    @Before
    public void initMidletAndLWUIT() {
        if(m == null) {
        m = new DummyMidlet();
        m.startApplication();
        }
    }
    
    
    @After
    public void killLWUIT() throws InterruptedException {
        /*if(m != null) {
        m.stopApplication();
        }*/
        MIDletContext miDletContext = MIDletBridge.getMIDletContext(m);
        MIDletBridge.destroyMIDletContext(miDletContext);
    }
    
    public static TextEditorProvider createMockTextEditor() {
        TextEditorProvider teprovider = mock(TextEditorProvider.class);
        when(teprovider.isVisible()).thenReturn(true);
        when(teprovider.getFont()).thenReturn(Font.getDefaultFont());
        when(teprovider.getLineMarginHeight()).thenReturn(2);
        return teprovider;
    }
    
    protected void waitEdt() throws InterruptedException {
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
}
