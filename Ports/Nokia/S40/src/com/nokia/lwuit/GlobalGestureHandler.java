/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.sun.lwuit.Display;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.impl.s40.S40GestureImplementation;

/**
 *
 * @author tkor
 */
public abstract class GlobalGestureHandler {
    
    public static void setGlobalGestureHandler(GlobalGestureHandler h) {
        LWUITImplementation impl = Display.getInstance().getImplementation();
        if(impl instanceof S40GestureImplementation) {
            ((S40GestureImplementation)impl).setGlobalGestureHandler(h);
        }
    }
    
    public abstract void gestureAction(GestureEvent e);
}
