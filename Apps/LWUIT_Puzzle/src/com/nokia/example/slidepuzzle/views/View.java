/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.views;

import com.nokia.example.slidepuzzle.main.PuzzleMidlet;
import com.sun.lwuit.Form;

/**
 * An abstract implementation of a form-based view.
 */
public abstract class View
    extends Form {

    protected PuzzleMidlet midlet;

    public View(PuzzleMidlet midlet) {
        super();
        this.midlet = midlet;

        hideDefaultTitle();
        setScrollable(false);
    }

    public PuzzleMidlet getMidlet() {
        return midlet;
    }

    private void hideDefaultTitle() {
        getTitleArea().getStyle().setPadding(0, 0, 0, 0);
        getTitleArea().setVisible(false);
        getTitleComponent().setVisible(false);
    }
}
