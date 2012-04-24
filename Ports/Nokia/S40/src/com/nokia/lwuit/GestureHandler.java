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

/**
 *
 * @author tkor
 */
public abstract class GestureHandler implements GestureListener{
    
    private Form form;
    
    public GestureHandler(Form f) {
        form = f;
        S40Implementation impl = (S40Implementation)Display.getInstance().getImplementation();
        impl.addGestureHandler(this);
    }

    public Form getForm() {
        return form;
    }

    public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
        gestureEvent(gestureEvent);
    }
    
    
    
    public abstract void gestureEvent(GestureEvent e);
}
