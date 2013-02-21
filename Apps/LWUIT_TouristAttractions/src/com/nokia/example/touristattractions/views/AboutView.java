/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.util.Visual;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;

public class AboutView
    extends View {
    
    private Label titleLabel;
    private Label nokiaLabel;
    private Label versionLabel;

    public AboutView(TouristMidlet midlet) {
        super(midlet);
        
        setTitle("About");        
        initializeAndStyleComponents();
        createLayout();
        addCommands();        
    }
    
    private void initializeAndStyleComponents() {
        Style style = getStyle();
        style.setBgColor(Visual.BACKGROUND_COLOR);
        
        /* Title label */
        titleLabel = new Label("Tourist Attractions with LWUIT");
        style = titleLabel.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        style.setFont(Visual.SMALL_BOLD_FONT);
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        titleLabel.setPreferredW(this.getWidth());
        
        /* "by Nokia" label */
        nokiaLabel = new Label("by Nokia");
        nokiaLabel.setPreferredW(this.getWidth());
        style.setAlignment(CENTER);
        style = nokiaLabel.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        style.setFont(Visual.SMALL_FONT);
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        style.setAlignment(CENTER);
        
        /* Version label */
        versionLabel = new Label("Version 1.0");
        versionLabel.setPreferredW(this.getWidth());
        style = versionLabel.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        style.setFont(Visual.SMALL_FONT);
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        style.setAlignment(CENTER);        
    }
    
    private void createLayout() {
        Container aboutContainer = new Container();
        FlowLayout flowLayout = new FlowLayout(CENTER);
        aboutContainer.setLayout(flowLayout);
        
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(borderLayout);
        aboutContainer.addComponent(titleLabel);
        aboutContainer.addComponent(nokiaLabel);
        aboutContainer.addComponent(versionLabel);
        addComponent(BorderLayout.CENTER, aboutContainer);        
    }
    
    private void addCommands() {
        /* Back button */
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                AboutView.this.midlet.showAttractionView();
            }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);        
    }
}
