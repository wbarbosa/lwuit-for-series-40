/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.impl.s40;

import com.nokia.lwuit.FormGestureHandler;
import com.nokia.lwuit.GlobalGestureHandler;
import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import java.util.Vector;

/**
 *
 * @author tkor
 */
public class S40GestureImplementation extends S40Implementation{
    
    private FormGestureHandler currentFormGestureHandler;
    private GlobalGestureHandler globalGestureHandler;
        
    private Vector gestureListeners = new Vector();
    private GestureListenerImpl internalListener;
    
    public void init(Object m) {
        super.init(m);
        
        internalListener = new GestureListenerImpl();
        //register for gestures
        GestureInteractiveZone giz = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_ALL);
        GestureRegistrationManager.register(canvas, giz);
        GestureRegistrationManager.setListener(canvas, internalListener);
    }
        
    public void addGestureHandler(FormGestureHandler l) {
        gestureListeners.addElement(l);
    }
    
    public synchronized void setCurrentGestureListener(com.sun.lwuit.Form f) {
        int l = gestureListeners.size();
        FormGestureHandler h = null;
        FormGestureHandler c = null;
        for(int i = 0; i < l; i++) {
            c = (FormGestureHandler) gestureListeners.elementAt(i);
            if(c.getForm() == f) {
                h = c;
                break;
            }
        }
        currentFormGestureHandler = h;
    }
    
    public synchronized void removeGestureHandler(FormGestureHandler l) {
        gestureListeners.removeElement(l);
    }

    public synchronized void setGlobalGestureHandler(GlobalGestureHandler l) {
        globalGestureHandler = l;
    }
    
    private class GestureListenerImpl implements GestureListener {

        public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
            if(currentFormGestureHandler != null) {
            currentFormGestureHandler.gestureEvent(gestureEvent);
        }
        if(globalGestureHandler != null) {
            globalGestureHandler.gestureAction(gestureEvent);
        }
        }
        
    }
}
