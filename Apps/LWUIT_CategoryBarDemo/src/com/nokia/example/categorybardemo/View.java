/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.categorybardemo;

import com.sun.lwuit.Form;

/**
 * Base class for all views used in this application.
 * Provides methods for constructing category bar commands.
 */
public class View
    extends Form {

    private String label;
    private String iconPath;

    public View(String label, String iconPath) {
        this.label = label;
        this.iconPath = iconPath;
    }

    /**
     * @return path to an icon for a category bar command.
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * @return label for a category bar command.
     */
    public String getLabel() {
        return label;
    }
}
