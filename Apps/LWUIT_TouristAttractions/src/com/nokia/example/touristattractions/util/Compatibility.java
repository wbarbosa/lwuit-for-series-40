/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.util;

import com.sun.lwuit.Display;

/**
 * Helper class to write feature-based code
 */
public class Compatibility {

    public static boolean TOUCH_SUPPORTED;
    public static boolean IAP_SUPPORTED;
    public static boolean IS_S60_DEVICE;
    public static boolean IS_FULLTOUCH;
    public static int SCREEN_SIZE;
    public static int RES_320x480 = 1;
    public static int RES_240x400 = 2;
    public static int RES_240x320 = 3;
    public static int RES_320x240 = 4;
    public static int RES_400x240 = 5;
    public static int WIDTH;
    public static int HEIGHT;

    /**
     * Initialize compatibility related constants.
     */
    static {
        TOUCH_SUPPORTED = checkTouchSupport();
        IAP_SUPPORTED = checkIAPSupport();
        IS_S60_DEVICE = checkIfS60Device();
        SCREEN_SIZE = checkScreenType();
        IS_FULLTOUCH = checkIfFulltouch();
    }

    /**
     * Checks if touch input is supported.
     *
     * @return true if touch input is supported, false otherwise
     */
    private static boolean checkTouchSupport() {
        return Display.getInstance().isTouchScreenDevice();
    }

    /**
     * Checks if In-App Purchase operations are supported.
     *
     * @return true if In-App Purchase operations are supported, false otherwise
     */
    private static boolean checkIAPSupport() {
        try {
            Class.forName("com.nokia.mid.payment.IAPClientPaymentListener");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the device is an S60 device.
     *
     * @return true if S60, false otherwise
     */
    private static boolean checkIfS60Device() {
        String platform = System.getProperty("microedition.platform");
        if (platform == null) {
            platform = "";
        }
        
        if (platform.indexOf("sw_platform=S60") > 0) {
            return true;
        }
        
        if (platform.indexOf("/S60_") > 0) {
            return true;
        }
        
        try {
            Class.forName("com.symbian.gcf.NativeInputStream");
            return true;
        }
        catch (ClassNotFoundException e) {
        }

        return false;
    }

    private static boolean checkIfFulltouch() {
        return SCREEN_SIZE == RES_240x400 || SCREEN_SIZE == RES_400x240;
    }

    /**
     * Checks the size of the screen
     *
     * @return size of the screen
     */
    private static int checkScreenType() {
        int w = Display.getInstance().getDisplayWidth();
        int h = Display.getInstance().getDisplayHeight();

        WIDTH = w;
        HEIGHT = h;

        if (w == 320 && h == 480) {
            return RES_320x480;
        }

        if (w == 240 && h == 400) {
            return RES_240x400;
        }

        if (w == 240 && h == 320) {
            return RES_240x320;
        }

        if (w == 320 && h == 240) {
            return RES_320x240;
        }

        if (w == 400 && h == 240) {
            return RES_400x240;
        }

        Display.getInstance().exitApplication();
        return -1;
    }
}
