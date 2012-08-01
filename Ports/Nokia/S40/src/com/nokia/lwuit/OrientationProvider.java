/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

/**
 * wrapper around the nokia Orientation class. The methods in this class are 
 * the same as in com.nokia.mid.ui.orientation.Orientation.
 * @author tkor
 */
public abstract class OrientationProvider {
    /**
     * Constant for display and UI orientation mode (Landscape, right side up).
     */
    public static int ORIENTATION_LANDSCAPE = 2;
    /**
     * Constant for display orientation mode (Landscape, left side up).
     */
    public static int ORIENTATION_LANDSCAPE_180 = 4;
    /**
     * Constant for display and UI orientation mode (Portrait, top side up).
     */
    public static int ORIENTATION_PORTRAIT = 1;
    /**
     * Constant for display orientation mode (Portrait, bottom side up).
     */
    public static int ORIENTATION_PORTRAIT_180 = 3;
    
    /**
     * Registers an orientation listener
     * @param listener the listener to register
     */
    public abstract void addOrientationListener(OrientationListener listener);
    
    /**
     * Gets application's current UI orientation.
     * @return constant value of application's UI orientation.
     */
    public abstract int getAppOrientation();
    
    /**
     * Gets the current display orientation.
     * @return constant value of display orientation.
     */
    public abstract int getDisplayOrientation();
    /**
     * Removes and orientation listener.
     * @param listener the listener to remove.
     */
    public abstract void removeOrientationListener(OrientationListener listener);
    /**
     * Sets the application's UI orientation.
     * @param newAppOrientation value of application's new UI orientation. The value
     * must be one of the following: {@link ORIENTATION_PORTRAIT} or {@link ORIENTATION_LANDSCAPE}
     */
    public abstract void setAppOrientation(int newAppOrientation);
    
    /**
     * Get and instance of this class. This factorymethod creates and instance if the
     * supported api is available. Otherwise returns null.
     * @return an instance of OrientationProvider or null if Nokia API is not available.
     */
    public static OrientationProvider getOrientationProvider() {
        OrientationProvider p = null;
        try {
            Class.forName("com.nokia.mid.ui.orientation.Orientation");
            Class c = Class.forName("com.nokia.lwuit.OrientationProviderImpl");
            p = (OrientationProvider)c.newInstance();
        }catch(Exception cnfe) {
            System.out.println("[LWUIT]orientation api not supported.");
        }
        return p;
    }
}
