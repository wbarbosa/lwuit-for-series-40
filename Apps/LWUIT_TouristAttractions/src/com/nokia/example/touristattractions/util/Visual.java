/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.util;

import com.nokia.mid.ui.DirectUtils;
import com.sun.lwuit.Font;

/**
 * Visual properties.
 */
public class Visual {

    // Colors
    public static final int BACKGROUND_COLOR = 0xffffff;
    public static final int DARK_BACKGROUND_COLOR = 0x101010;
    public static final int SPLASH_BACKGROUND_COLOR = 0x107EDA;
    public static final int HEADER_FONT_COLOR = BACKGROUND_COLOR;
    public static final int SPLASH_TEXT_COLOR = BACKGROUND_COLOR;
    public static final int LIST_PRIMARY_COLOR = 0x0;
    public static final int LIST_SECONDARY_COLOR = 0x888888;
    public static final int LIST_SCROLLBAR_COLOR = 0x333333;
    public static final int LOGO_TEXT_COLOR = 0xffffff;
    public static final int LIST_FOCUS_COLOR = 0xaaaaaa;
    public static final int MAP_MARKER_COLOR = 0x333333;
    public static final int MAP_CIRCLE_COLOR = 0x88CD0000;
    
    // Fonts
    public static final Font SMALL_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public static final Font SMALL_BOLD_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    public static final Font MEDIUM_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    public static final Font MEDIUM_BOLD_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    public static final Font LARGE_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
    public static final Font LARGE_BOLD_FONT = Font.createSystemFont(
        Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
    public static final javax.microedition.lcdui.Font MAP_MARKER_FONT;

    static {
        if ("true".equals(System.getProperty("com.nokia.mid.ui.customfontsize"))) {
            MAP_MARKER_FONT = DirectUtils.getFont(
                javax.microedition.lcdui.Font.FACE_SYSTEM,
                javax.microedition.lcdui.Font.STYLE_BOLD,
                11);
        }
        else {
            MAP_MARKER_FONT = javax.microedition.lcdui.Font.getFont(
                javax.microedition.lcdui.Font.FACE_SYSTEM,
                javax.microedition.lcdui.Font.STYLE_BOLD,
                javax.microedition.lcdui.Font.SIZE_SMALL);
        }                
    }
    
    public static final int SOFTKEY_MARGIN = 4;
}
