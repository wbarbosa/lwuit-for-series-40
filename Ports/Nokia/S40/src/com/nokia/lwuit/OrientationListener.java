
package com.nokia.lwuit;

/**
 * Clone of the nokia OrientationListener.
 * @author tkor
 */
public interface OrientationListener {
    /**
     * This method is called when orientation changes if the implementing class
     * is registered to listen orientationchanges.
     * @param newDisplayOrientation constant value of display's new orientation. See OrientationProvider class.
     */
    public void displayOrientationChanged(int newDisplayOrientation); 
}
