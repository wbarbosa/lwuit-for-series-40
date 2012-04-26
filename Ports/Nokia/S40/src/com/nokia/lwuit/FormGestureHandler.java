/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nokia.lwuit;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.impl.s40.S40GestureImplementation;
import java.lang.ref.WeakReference;

/**
 *
 * @author tkor
 */
public abstract class FormGestureHandler{
    
    private WeakReference form;
    
    public FormGestureHandler(Form f) {
        form = new WeakReference(f);
        LWUITImplementation impl = Display.getInstance().getImplementation();
        if(impl instanceof S40GestureImplementation) {
            ((S40GestureImplementation) impl).addGestureHandler(this);
        }
    }

    public Form getForm() {
        Object o = form.get();
        return (o != null) ? (Form)o : null;
    }
    
    public abstract void gestureEvent(GestureEvent e);
}
