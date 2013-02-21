/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.view.item.TextArea.TextChangedListener;
import com.nokia.lwuit.TextEditorProvider;

/**
 * Wraps the LWUIT TextField to enable text changed actions
 */
public class TextField
        extends com.sun.lwuit.TextField {

    private TextChangedListener listener;

    /**
     * Constructor
     * @param columns Number of columns
     * @param constraints Input constraints
     * @param listener Listener for text changes
     */
    public TextField(int constraints, TextChangedListener listener) {
        super(2);
        setConstraint(constraints);
        this.listener = listener;
    }

    /**
     * Constructor
     * @param columns Number of columns
     * @param listener Listener for text changes
     */
    public TextField(TextChangedListener listener) {
        this(TextField.ANY, listener);
    }

    /**
     * Capture input actions
     * @param textEditor
     * @param actions
     */
    public void inputAction(TextEditorProvider textEditor, int actions) {
        super.inputAction(textEditor, actions);

        if (isInitialized() && listener != null) {
            listener.textChanged(this);
        }
    }

    /**
     * Handle text setting
     * @param t
     */
    public void setText(String t) {
        super.setText(t);

        // Notify listener, if Text Editor is not sending events.
        if (isInitialized() && listener != null && !isTextEditorActive()) {
            listener.textChanged(this);
        }
    }
}
