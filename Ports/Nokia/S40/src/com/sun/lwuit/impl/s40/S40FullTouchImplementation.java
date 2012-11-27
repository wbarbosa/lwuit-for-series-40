/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.impl.s40;

import com.nokia.lwuit.OrientationListener;
import com.nokia.lwuit.OrientationProvider;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;

/**
 *
 * @author tkor
 */
public class S40FullTouchImplementation extends S40GestureImplementation implements com.nokia.mid.ui.KeyboardVisibilityListener{
    
    /**
     * Default constructor.
     */
    public S40FullTouchImplementation() {
        
    }
    
    public void init(Object m) {
        super.init(m);

        if (getProperty("Nokia-MIDlet-App-Orientation", "").equals("manual")) {   
            com.nokia.mid.ui.orientation.Orientation.setAppOrientation(
                    com.nokia.mid.ui.orientation.Orientation.getAppOrientation());
        }
        
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
        
        com.nokia.mid.ui.VirtualKeyboard.hideOpenKeypadCommand(true);
        com.nokia.mid.ui.VirtualKeyboard.setVisibilityListener(this);
    }

    /**
     * Override of com.nokia.mid.ui.KeyboardVisibilityListener
     * @param i which can be VirtualKeyboard.SYSTEM_KEYBOARD or VirtualKeyboard.CUSTOM_KEYBOARD
     */
    public void showNotify(int i) {
        //do nothing
    }

    /**
     * Override of com.nokia.mid.ui.KeyboardVisibilityListener
     * @param i which can be VirtualKeyboard.SYSTEM_KEYBOARD or VirtualKeyboard.CUSTOM_KEYBOARD
     */
    public void hideNotify(int i) {
        //issue extra repaint so that the screensize is updated properly 
        this.getCurrentForm().repaint();
    }
    
}
