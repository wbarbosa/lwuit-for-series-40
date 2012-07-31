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
        com.nokia.mid.ui.VirtualKeyboard.hideOpenKeypadCommand(true);
        com.nokia.mid.ui.VirtualKeyboard.setVisibilityListener(this);
    }

    public void setCurrentForm(Form f) {
        
        super.setCurrentForm(f);
    }

    public void showNotify(int i) {
        //do nothing
    }

    public void hideNotify(int i) {
        //issue extra repaint so that the screensize is updated properly
        this.getCurrentForm().repaint();
    }
    
}
