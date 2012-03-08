/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Calendar;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.Tabs;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;

/**
 * Demot of the TabbedPane available in the UI
 * 
 * @author Tamir Shabat
 */
public class TabsDemo extends Demo {

    Tabs tp = null;

     public void cleanup() {
         tp = null;
     }

    public String getName() {
        return "Tabs";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("tabHelp", "Help description");
    }

    protected void executeDemo(Container f) {
        f.setLayout(new BorderLayout());
        f.setScrollable(false);
        tp = new Tabs();

        tp.addTab("Tab 1", new Label("Welcome to TabbedPane demo!"));

        Container radioButtonsPanel = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        RadioButton topRB = new RadioButton("Top");
        RadioButton LeftRB = new RadioButton("Left");
        RadioButton BottomRB = new RadioButton("Bottom");
        RadioButton RightRB = new RadioButton("Right");
        topRB.setName("TopRB");
        LeftRB.setName("LeftRB");
        BottomRB.setName("BottomRB");
        RightRB.setName("RightRB");

        RadioListener myListener = new RadioListener();
        topRB.addActionListener(myListener);
        LeftRB.addActionListener(myListener);
        BottomRB.addActionListener(myListener);
        RightRB.addActionListener(myListener);

        ButtonGroup group1 = new ButtonGroup();
        group1.add(topRB);
        group1.add(LeftRB);
        group1.add(BottomRB);
        group1.add(RightRB);

        TextArea lbl = new TextArea("Please choose a tab placement direction");
        lbl.setEditable(false);
        lbl.setFocusable(false);
        lbl.setUIID("TitleLabel");
        radioButtonsPanel.addComponent(lbl);
        ComponentGroup radioGroup = new ComponentGroup();
        radioButtonsPanel.addComponent(radioGroup);
        radioGroup.addComponent(topRB);
        radioGroup.addComponent(LeftRB);
        radioGroup.addComponent(BottomRB);
        radioGroup.addComponent(RightRB);

        tp.addTab("Tab 2", radioButtonsPanel);
        Calendar c = new Calendar();
        c.setScrollableY(true);
        tp.addTab("Tab 3", c);
        
        f.addComponent("Center", tp);
    }

    /** Listens to the radio buttons. */
    class RadioListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            /*
            String title = ((RadioButton) e.getSource()).getText();
            if ("Top".equals(title)) {
                tp.setTabPlacement(Tabs.TOP);
            } else if ("Left".equals(title)) {
                tp.setTabPlacement(Tabs.LEFT);
            } else if ("Bottom".equals(title)) {
                tp.setTabPlacement(Tabs.BOTTOM);
            } else {//right
                tp.setTabPlacement(Tabs.RIGHT);
            }
             *
             */
            String title = ((RadioButton) e.getSource()).getName();
            if ("TopRB".equals(title)) {
                tp.setTabPlacement(Tabs.TOP);
            } else if ("LeftRB".equals(title)) {
                tp.setTabPlacement(Tabs.LEFT);
            } else if ("BottomRB".equals(title)) {
                tp.setTabPlacement(Tabs.BOTTOM);
            } else {//right
                tp.setTabPlacement(Tabs.RIGHT);
            }
        }
    }
}
