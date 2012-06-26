/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.impl.s40;

import com.nokia.lwuit.OrientationListener;
import com.nokia.lwuit.OrientationProvider;

/**
 *
 * @author tkor
 */
public class S40FullTouchImplementation extends S40GestureImplementation{
    
    public void init(Object m) {
        super.init(m);
        
        OrientationListener ol = new OrientationListener() {

            public void displayOrientationChanged(int i) {
                String orientation = getProperty("Nokia-MIDlet-App-Orientation", "");
                if (orientation.equals("manual")) {
                    com.nokia.mid.ui.orientation.Orientation.setAppOrientation(i);
                }
            }
        };
        OrientationProvider p = OrientationProvider.getOrientationProvider();
        if(p != null) {
            p.addOrientationListener(ol);
        }
    }
}
