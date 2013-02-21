/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.util;

import com.sun.lwuit.Display;

public class Compatibility {

    public static boolean TOUCH_SUPPORTED;
    public static boolean CAMERA_SUPPORTED;
    public static int SCREEN_SIZE;
    public static int RES_UNKNOWN = 0;
    public static int RES_240x400 = 1;
    public static int RES_240x320 = 2;
    public static int RES_320x240 = 3;
    public static int DIGIT_WIDTH; // width of digit image
    public static int DIGIT_HEIGHT; // height of digit image
    public static int PUZZLE_SIZE; // width/height of the puzzle

    /**
     * Initialize compatibility related constants
     */
    static {
        TOUCH_SUPPORTED = checkTouchSupport();
        CAMERA_SUPPORTED = checkCameraSupport();
        SCREEN_SIZE = checkScreenType();
    }

    /**
     * Check if touch input is supported
     *
     * @return true if touch input is supported, false otherwise
     */
    private static boolean checkTouchSupport() {
        return Display.getInstance().isTouchScreenDevice();
    }

    /**
     * Check if camera is supported and the battery level is high enough to
     * prevent camera from being in stand-by mode.
     *
     * @return true if camera is supported, false otherwise
     */
    private static boolean checkCameraSupport() {
        String batteryStr = System.getProperty("com.nokia.mid.batterylevel");

        int batteryPercentage = -1;
        if (batteryStr != null) {
            batteryPercentage = Integer.parseInt(batteryStr.substring(0, batteryStr.length() - 1));
            return batteryPercentage > 20;
        } else {
            return true;
        }
    }

    /**
     * Check the size of the screen
     *
     * @return size of the screen
     */
    private static int checkScreenType() {
        int w = Display.getInstance().getDisplayWidth();
        int h = Display.getInstance().getDisplayHeight();

        if (w == 240 && h == 400) {
            PUZZLE_SIZE = 219;
            DIGIT_WIDTH = 13;
            DIGIT_HEIGHT = 12;
            return RES_240x400;
        }

        if (w == 240 && h == 320) {
            PUZZLE_SIZE = 219;
            DIGIT_WIDTH = 13;
            DIGIT_HEIGHT = 12;
            return RES_240x320;
        }

        if (w == 320 && h == 240) {
            PUZZLE_SIZE = 219;
            DIGIT_WIDTH = 13;
            DIGIT_HEIGHT = 12;
            return RES_320x240;
        }

        PUZZLE_SIZE = 219;
        DIGIT_WIDTH = 13;
        DIGIT_HEIGHT = 12;
        
        return RES_UNKNOWN;
    }
}
