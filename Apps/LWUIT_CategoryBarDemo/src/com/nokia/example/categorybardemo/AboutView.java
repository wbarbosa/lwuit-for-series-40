/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.categorybardemo;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * View for showing application information.
 */
public class AboutView
    extends View {

    /**
     * Constructor
     * 
     * @param name
     * @param vendor
     * @param version 
     */
    public AboutView(String name, String vendor, String version) {
        super("about", "/categorybar_information.png");

        setTitle("about");
        BorderLayout layout = new BorderLayout();
        layout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(layout);
        
        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(BorderLayout.CENTER, container);
        container.addComponent(createLabel(name));
        container.addComponent(createLabel("with LWUIT"));
        container.addComponent(createLabel("by " + vendor));
        container.addComponent(createLabel("Version " + version));
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyle().setAlignment(Component.CENTER);
        return label;
    }
}
