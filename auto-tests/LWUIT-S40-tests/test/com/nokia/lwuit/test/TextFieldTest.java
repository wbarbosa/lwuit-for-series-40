/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test;

import com.nokia.lwuit.TextEditorProvider;
import com.nokia.lwuit.test.util.LWUITTest;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import java.lang.reflect.Field;
import javax.microedition.lcdui.Font;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author tkor
 */
public class TextFieldTest extends LWUITTest{
    
    
    public TextFieldTest() {
    }
    
    @Test
    public void testDataChangeListenerIsCalledWhenInputActionIsCalled() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        final Form f = new Form();
        f.show();
        TextField field = new TextField();
        f.addComponent(field);
        TextEditorProvider tep = mock(TextEditorProvider.class);
        when(tep.getFont()).thenReturn(Font.getDefaultFont());
        when(tep.getLineMarginHeight()).thenReturn(2);
        when(tep.isVisible()).thenReturn(true);
        when(tep.getContent()).thenReturn("test");
        Field texteditor = TextArea.class.getDeclaredField("textEditor");
        texteditor.setAccessible(true);
        texteditor.set(field, tep);
        
        DataChangedListener mocklistener = mock(DataChangedListener.class);
        
        field.addDataChangeListener(mocklistener);
        
        field.inputAction(tep, TextEditorProvider.TextEditorListener.ACTION_CONTENT_CHANGE);
        waitEdt();
        verify(mocklistener).dataChanged(DataChangedListener.CHANGED, -1);
    }
}
