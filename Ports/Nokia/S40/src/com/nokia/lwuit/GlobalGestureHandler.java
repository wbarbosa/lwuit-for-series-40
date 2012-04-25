/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.sun.lwuit.Display;
import com.sun.lwuit.impl.s40.S40Implementation;

/**
 *
 * @author tkor
 */
public abstract class GlobalGestureHandler {
    
    public static void setGlobalGestureHandler(GlobalGestureHandler h) {
        S40Implementation impl = (S40Implementation) Display.getInstance().getImplementation();
        impl.setGlobalGestureHandler(h);
    }
    
    public abstract void gestureAction(GestureEvent e);
}
