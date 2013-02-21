/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.location;

import com.nokia.example.touristattractions.util.Compatibility;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

/**
 * Abstract location finder.
 */
public abstract class LocationFinder {

    protected int interval = 60000;
    protected Listener listener;

    private static final String[] S40_GPS_DEVICES = {"2710", "6350", "6750",
        "3710", "6700", "6260"};
    
    private LocationProvider lp;
    private long lastRequestTime = -1;
    private static boolean supportsFindingLocation = true;
    private final Object updateLock = new Object();
    private UpdateThread updateThread;

    /**
     * Initialize the location finder.
     */
    protected final void init(Listener listener)
        throws InitializationException, SecurityException {
        this.listener = listener;
        
        LocationProvider lp = null;
        try {
            lp = getLocationProvider();
        }
        catch (LocationException se) {
            throw new InitializationException(se.getMessage());
        }
        
        if (lp == null) {
            throw new InitializationException("Provider not found");
        }
        this.lp = lp;
    }

    /**
     * Start finding location.
     */
    public synchronized final void start() {
        if (supportsFindingLocation && updateThread == null) {
            updateThread = new UpdateThread();
            updateThread.start();
        }
    }

    /**
     * Stop finding location.
     */
    public synchronized final void quit() {
        if (updateThread != null) {
            updateThread.cancel();
            updateThread = null;
        }
    }
    
    /**
     * Interface for notifying when the location has changed.
     */
    public interface Listener {
        void newLocation(double lat, double lon, int accuracy);
    }

    /**
     * Creates a location finder based on the capabilities the device.
     * @param listener
     * @return 
     */
    public static LocationFinder getFinder(Listener listener) {
        if (listener == null) {
            throw new NullPointerException("listener not defined");
        }

        LocationFinder finder = null;        
        try {
            // this will throw an exception if JSR-179 is missing
            Class.forName("javax.microedition.location.Location");

            if (finder == null && supportsGPS()) {
                Class c =
                    Class.forName(
                    "com.nokia.example.touristattractions.location.GpsLocationFinder");
                finder = (LocationFinder) (c.newInstance());
                try {
                    finder.init(listener);
                }
                catch (InitializationException e) {
                    finder = null;
                }
            }
            
            if (finder == null && supportsCellId()) {
                Class c =
                    Class.forName(
                    "com.nokia.example.touristattractions.location.CellIdLocationFinder");
                finder = (LocationFinder) (c.newInstance());
                try {
                    finder.init(listener);
                }
                catch (InitializationException e) {
                    finder = null;
                }
            }
        }
        catch (Exception e) {
            finder = null;
        }
        return finder;
    }

    private static boolean supportsGPS() {
        String platform = System.getProperty("microedition.platform");
        
        if (platform != null) {
            for (int i = 0; i < S40_GPS_DEVICES.length; i++) {
                if (platform.indexOf(S40_GPS_DEVICES[i]) > -1) {
                    return true;
                }
            }
        }
        return Compatibility.IS_S60_DEVICE;
    }

    private static boolean supportsCellId() {
        try {
            Class.forName("com.nokia.mid.location.LocationUtil");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    protected static class InitializationException
        extends Exception {

        public InitializationException() {}
        
        public InitializationException(String s) {
            super(s);
        }
    }
    
    private void requestLocation() {
        synchronized (updateLock) {
            
            if (System.currentTimeMillis() >= lastRequestTime + interval) {
                lastRequestTime = System.currentTimeMillis();
                
                try {
                    QualifiedCoordinates coordinates =
                        lp.getLocation(-1).getQualifiedCoordinates();
                    if (coordinates != null) {
                        double lat = coordinates.getLatitude();
                        double lon = coordinates.getLongitude();
                        int accuracy = (int) coordinates.getHorizontalAccuracy();
                        accuracy = Math.max(accuracy, 10);
                        listener.newLocation(lat, lon, accuracy);
                    }
                }
                catch (LocationException e) {
                    if (shouldQuit(e)) {
                        supportsFindingLocation = false;
                        quit();
                    }
                }
                catch (InterruptedException e) {
                }
                catch (SecurityException e) {
                    supportsFindingLocation = false;
                    quit();
                }
            }
        }
    }
    
    private class UpdateThread
        extends Thread {

        private static final long MIN_DELAY = 5000;
        private volatile boolean running = true;

        public void run() {            
            while (running) {
                requestLocation();
                try {
                    sleep(Math.max(interval + lastRequestTime
                        - System.currentTimeMillis(), MIN_DELAY));
                }
                catch (InterruptedException e) {
                }
            }
        }

        public void cancel() {
            running = false;
        }
    }
    
    /**
     * Get a location provider. 
     * @return location provider
     * @throws LocationException
     * @throws SecurityException
     */
    protected abstract LocationProvider getLocationProvider()
        throws LocationException, SecurityException;

    /**
     * @param e
     * @return true if should quit because location provider doesn't work 
     */
    protected abstract boolean shouldQuit(LocationException e);
}
