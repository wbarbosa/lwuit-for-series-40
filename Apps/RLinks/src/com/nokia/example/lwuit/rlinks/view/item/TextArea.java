/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.lwuit.TextEditorProvider;

/**
 * Wraps the LWUIT TextArea to enable text changed actions
 */
public class TextArea
        extends com.sun.lwuit.TextArea {

    private TextChangedListener listener;

    /**
     * Interface used to signal of text changes.
     */
    public interface TextChangedListener {

        public void textChanged(com.sun.lwuit.TextArea textArea);
    }

    /**
     * Constructor
     * @param rows Number of rows
     * @param columns Number of columns
     * @param constraints Input constraints
     * @param listener Listener for text changes
     */
    public TextArea(int rows, int constraints, TextChangedListener listener) {
        super("", rows, 2, constraints);
        this.listener = listener;
    }

    /**
     * Constructor
     * @param rows Number of rows
     * @param columns Number of columns
     * @param listener Listener for text changes
     */
    public TextArea(int rows, TextChangedListener listener) {
        this(rows, TextArea.ANY, listener);
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
