/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.orientation.Orientation;
import java.util.Hashtable;

/**
 *
 * @author tkor
 */
class OrientationProviderImpl extends OrientationProvider{

    private static final Hashtable listeners = new Hashtable();
    public OrientationProviderImpl() {
        
    }
    
    public void addOrientationListener(final OrientationListener listener) {
        com.nokia.mid.ui.orientation.OrientationListener ol = new com.nokia.mid.ui.orientation.OrientationListener() {

            public void displayOrientationChanged(int i) {
                listener.displayOrientationChanged(i);
            }
        };
        Orientation.addOrientationListener(ol);
        synchronized(listeners) {
            listeners.put(listener, ol);
        }
        
    }

    public int getAppOrientation() {
        return Orientation.getAppOrientation();
    }

    public int getDisplayOrientation() {
        return Orientation.getDisplayOrientation();
    }

    public void removeOrientationListener(OrientationListener listener) {
        com.nokia.mid.ui.orientation.OrientationListener ol = (com.nokia.mid.ui.orientation.OrientationListener)listeners.get(listener);
        if(ol != null) {
            Orientation.removeOrientationListener(ol);
        }
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }

    public void setAppOrientation(int newAppOrientation) {
        Orientation.setAppOrientation(newAppOrientation);
    }
    
}
