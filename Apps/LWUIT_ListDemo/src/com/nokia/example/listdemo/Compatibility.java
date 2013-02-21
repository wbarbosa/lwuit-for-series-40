/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.listdemo;

/**
 * This is a helper class to provide functionalities based on platform
 * capabilities. It does this mainly by trying to implement the more advances
 * feature and catching the resulting error on less advanced platforms. On these
 * platforms a corresponding, more traditional, feature is used.
 */
public final class Compatibility {

    /**
     * This method returns true if IconCommands are supported on the device.
     *
     * @return true if IconCommands are supported
     */
    public static boolean supportsIconCommands() {
        try {
            Class.forName("com.nokia.mid.ui.IconCommand"); //Try to produce the exception
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * This method returns true if CategoryBar is supported on the device.
     *
     * @return true if CategoryBar is supported
     */
    public static boolean supportsCategoryBar() {
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * This method returns true if the device is a full touch device.
     *
     * @return true if full touch is supported
     */
    public static boolean isFullTouch() {
        return supportsIconCommands() && supportsCategoryBar();
    }

    /**
     * This method converts a String to lower case if the application is running
     * in full touch device.
     *
     * @return String converted to lower case
     */    
    public static String toLowerCaseIfFT(String text) {
        return isFullTouch() ? text.toLowerCase() : text;
    }

    /**
     * This method converts an array of Strings to lower case if the application
     * is running in full touch device.
     *
     * @return String converted to lower case
     */    
    public static String[] toLowerCaseIfFT(String[] texts) {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = toLowerCaseIfFT(texts[i]);
        }
        return texts;
    }

    /**
     * This method converts a String to upper case if the application is running
     * in full touch device.
     *
     * @return String converted to upper case
     */  
    public static String toUpperCaseIfFT(String text) {
        return isFullTouch() ? text.toUpperCase() : text;
    }
}
