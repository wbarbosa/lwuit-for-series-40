/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.impl.s40;

import com.nokia.lwuit.GestureHandler;
import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.sun.lwuit.Display;
import java.util.Vector;

/**
 *
 * @author tkor
 */
public class S40GestureImplementation extends S40Implementation{
    
    private GestureHandler currentFormGestureHandler;
    private GestureHandler globalGestureHandler;
        
    private Vector gestureListeners = new Vector();
    private GestureListenerImpl internalListener;
    private boolean registered = false;
    
    /**
     * Default constructor.
     */
    public S40GestureImplementation() {
        
    }
    
    public void init(Object m) {
        super.init(m);
    }
    
    private void registerGestureCanvas() {
        internalListener = new GestureListenerImpl();
        
        int gestures = GestureInteractiveZone.GESTURE_FLICK
                | GestureInteractiveZone.GESTURE_LONG_PRESS
                | GestureInteractiveZone.GESTURE_TAP
                | GestureInteractiveZone.GESTURE_DRAG
                | GestureInteractiveZone.GESTURE_DROP;
        if(Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
            gestures = GestureInteractiveZone.GESTURE_ALL;
        }
        //register for gestures
        GestureInteractiveZone giz = new GestureInteractiveZone(gestures);
        GestureRegistrationManager.register(canvas, giz);
        GestureRegistrationManager.setListener(canvas, internalListener);
    }
        
    /**
     * Add gesturehandler. This causes the handler to receive gesture-events when the
     * Form that this handler is attached to is shown.
     * @param l the handler to register to receive events
     */
    public void addGestureHandler(GestureHandler l) {
        if (!registered) {
            registerGestureCanvas();
            registered = true;
        }
        gestureListeners.addElement(l);
    }
    
    public synchronized void setCurrentGestureListener(com.sun.lwuit.Form f) {
        int l = gestureListeners.size();
        GestureHandler h = null;
        GestureHandler c = null;
        for(int i = 0; i < l; i++) {
            c = (GestureHandler) gestureListeners.elementAt(i);
            if(c.getForm() == f) {
                h = c;
                break;
            }
        }
        currentFormGestureHandler = h;
    }
    
    /**
     * Remove a gesturehandler. After this the handler will no longer receive Gesture
     * events.
     * @param l 
     */
    public synchronized void removeGestureHandler(GestureHandler l) {
        gestureListeners.removeElement(l);
    }

    /**
     * Set a gesturehandler that will get every gesture event that occur in the app.
     * To remove a global handler, set this to null.
     * @param l the handler to register as global handler
     */
    public synchronized void setGlobalGestureHandler(GestureHandler l) {
        globalGestureHandler = l;
    }
    
    private class GestureListenerImpl implements GestureListener {

        public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
            if(currentFormGestureHandler != null) {
            currentFormGestureHandler.gestureAction(gestureEvent);
        }
        if(globalGestureHandler != null) {
            globalGestureHandler.gestureAction(gestureEvent);
        }
        }
        
    }

    public boolean areGesturesSupported() {
        return true;
    }
    
}
