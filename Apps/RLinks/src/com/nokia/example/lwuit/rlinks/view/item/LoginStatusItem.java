/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.SessionManager;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;

/**
 * A custom view item for displaying login status.
 */
public class LoginStatusItem
        extends ListItem {

    private static final Font bold = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    private final SessionManager session = SessionManager.getInstance();
    private final Label status = new Label();
    private final Label username = new Label();

    public LoginStatusItem() {
        getStyle().setPadding(RIGHT, 10);
        setLayout(new FlowLayout(Component.RIGHT));
        addComponent(status);
        addComponent(username);
        Style style = username.getStyle();
        style.setFont(bold);
        style.setPadding(Component.LEFT, 0);
        style.setMargin(Component.LEFT, 0);
        username.setSelectedStyle(style);
        username.setPressedStyle(style);
        updateStatus();
    }

    public final void updateStatus() {
        String loginStatus = "";
        if (session.isLoggedIn()) {
            loginStatus = "Logged in as ";
            username.setText(session.getUsername());
            username.setVisible(true);
        }
        else {
            loginStatus = "Not logged in";
            username.setText("");
            username.setVisible(false);
        }
        status.setText(loginStatus);
        repaint();
    }
}
