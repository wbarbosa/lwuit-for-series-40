/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.models;

import com.nokia.maps.common.GeoCoordinate;

/**
 * A class representing the application user as an object with geocoordinates.
 */
public class Self {

    static private GeoCoordinate currentPosition = null;
    static private int accuracy = 10;  // accuracy of the position in meters

    /**
     * Sets the current position of user.
     * 
     * @param currentPosition Position of user as GeoCoordinate object
     * @param accuracy Accuracy of the position
     */
    public static final void setCurrentPosition(GeoCoordinate currentPosition,
        int accuracy) {
        
        Self.currentPosition = currentPosition;
        Self.accuracy = accuracy;
        
        if (Self.currentPosition == null) {
            Self.currentPosition = new GeoCoordinate(15.0, 15.0, 0);
        }
    }

    public static final GeoCoordinate getCurrentPosition() {
        return currentPosition;
    }

    public static final int getAccuracy() {
        return accuracy;
    }

    /**
     * Returns a string with the distance from the current position
     * to the given attraction.
     *
     * @param attraction
     * @return
     */
    public static String distanceToAttraction(Attraction attraction) {
        if (currentPosition == null) {
            return null;
        }
        
        double distance = currentPosition.distanceTo(attraction.getLocation());
        
        if (distance < 1000.0) {
            return ((int) (distance)) + " m";
        }
        else {
            String d = String.valueOf(distance % 1000).substring(0, 1);
            return (int) (distance / 1000) + "." + d + " km";
        }
    }
}
