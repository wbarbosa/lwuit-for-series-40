/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

/**
 * wrapper around the nokia Orientation class
 * @author tkor
 */
public abstract class OrientationProvider {
    public static int ORIENTATION_LANDSCAPE = 2;
    public static int ORIENTATION_LANDSCAPE_180 = 4;
    public static int ORIENTATION_PORTRAIT = 1;
    public static int ORIENTATION_PORTRAIT_180 = 3;
    
    public abstract void addOrientationListener(OrientationListener listener);
    public abstract int getAppOrientation();
    public abstract int getDisplayOrientation();
    public abstract void removeOrientationListener(OrientationListener listener);
    public abstract void setAppOrientation(int newAppOrientation);
    
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
