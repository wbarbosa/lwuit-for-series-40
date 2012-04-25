/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nokia.lwuit;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.impl.s40.S40Implementation;
import java.lang.ref.WeakReference;

/**
 *
 * @author tkor
 */
public abstract class FormGestureHandler{
    
    private WeakReference form;
    
    public FormGestureHandler(Form f) {
        form = new WeakReference(f);
        S40Implementation impl = (S40Implementation)Display.getInstance().getImplementation();
        impl.addGestureHandler(this);
    }

    public Form getForm() {
        Object o = form.get();
        return (o != null) ? (Form)o : null;
    }
    
    public abstract void gestureEvent(GestureEvent e);
}
