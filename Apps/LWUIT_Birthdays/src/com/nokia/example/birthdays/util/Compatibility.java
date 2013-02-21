/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.util;

import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.IconCommand;
import com.nokia.mid.ui.LCDUIUtil;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This is a helper class to provide functionalities based on platform
 * capabilities. It does this mainly by trying to implement the more advances
 * feature and catching the resulting error on less advanced platforms. On these
 * platforms a corresponding, more traditional, feature is used.
 *
 * Some functionality may not be implementable with try-catch and in these cases
 * one has to use preprocessing to select the code that is valid for the
 * platform.
 */
public final class Compatibility {

    private static Boolean nonTouch = null;

    /**
     * This method returns either a Command object or a IconCommand object. The
     * result is cast as a Command in either case to avoid crashing the VM on
     * the device.
     *
     * @param String label
     * @param int type
     * @param int priority
     * @param int icon
     * @return Command
     */
    public static Command getCommand(String label, int type, int priority,
        int icon) {

        try {
            Class.forName("com.nokia.mid.ui.IconCommand"); //Try to produce the exception
            Object c = new IconCommand(label, type, priority, icon);
            return (Command) c;
        }
        catch (ClassNotFoundException e) {
            Command c = new Command(label, type, priority);
            return c;
        }

    }

    /**
     * This method returns either a Command object or a IconCommand object. The
     * result is cast as a Command in either case to avoid crashing the VM on
     * the device. This method uses custom images for the IconCommand's icon.
     *
     * @param Image selected
     * @param Image deselected
     * @param String label
     * @param int type
     * @param int priority
     * @return Command
     */
    public static Command getCommand(Image selected, Image unselected,
        String label, int type, int priority) {

        try {
            Class.forName("com.nokia.mid.ui.IconCommand"); //Try to produce the exception
            Object c = new IconCommand(label, selected, unselected, type,
                priority);
            return (Command) c;
        }
        catch (ClassNotFoundException e) {
            Command c = new Command(label, type, priority);
            return c;
        }
    }

    /**
     * This method returns true if IconCommands are supported on the device.
     *
     * @return true if IconCommands are supported
     */
    public static boolean supportsIconCommands() {
        try {
            Class.forName("com.nokia.mid.ui.IconCommand"); //Try to produce the exception
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * This method return a Font object with a custom size if that is supported,
     * otherwise it returns the default font.
     *
     * @param face
     * @param style
     * @param size
     * @return Font
     */
    public static Font getFont(int face, int style, int size) {

        // Check for custom size fonts
        if (System.getProperty("com.nokia.mid.ui.customfontsize") != null) {
            return DirectUtils.getFont(face, style, size);
        }
        return Font.getDefaultFont();
    }

    /**
     * Wrapper method for the setObjectTrait method of the LCDUIUtil class. If
     * the class is not present on the current platform the method returns
     * false, but does not crash.
     *
     * @param target
     * @param trait
     * @param value
     * @return boolean
     */
    public static boolean setObjectTrait(Object target, String trait,
        Object value) {

        try {
            Class.forName("com.nokia.mid.ui.LCDUIUtil"); //Try to produce the exception

            return LCDUIUtil.setObjectTrait(target, trait, value);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Wrapper method for the getObjectTrait method of the LCDUIUtil class. If
     * the class is not present on the current platform the method returns null,
     * but does not crash.
     *
     * @param target
     * @param trait
     * @param value
     * @return Object or null
     */
    public static Object getObjectTrait(Object target, String trait) {
        try {
            Class.forName("com.nokia.mid.ui.LCDUIUtil"); // Try to produce the exception

            return LCDUIUtil.getObjectTrait(target, trait);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean isCategoryBarAvailable() {
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");

            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean hasGestureSupport() {
        try {
            Class.forName("com.nokia.mid.ui.gestures.GestureInteractiveZone");
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean hasTextEditorSupport() {
        try {
            Class.forName("com.nokia.mid.ui.TextEditor");
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFullTouch() {
        return supportsIconCommands() && isCategoryBarAvailable();
    }

    public static String toLowerCaseIfFT(String text) {
        return isFullTouch() ? text.toLowerCase() : text;
    }

    public static String[] toLowerCaseIfFT(String[] texts) {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = toLowerCaseIfFT(texts[i]);
        }
        return texts;
    }

    public static boolean isNonTouch() {
        if (nonTouch == null) {
            Canvas canvas = new Canvas() {
                protected void paint(Graphics g) {
                }
            };
            nonTouch = !canvas.hasPointerEvents() ? Boolean.TRUE : Boolean.FALSE;
        }
        return nonTouch.booleanValue();
    }
}
