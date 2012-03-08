/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import java.io.IOException;
import com.sun.lwuit.plaf.UIManager;

/**
 * Set of the button types available in the UI
 * 
 * @author Shai Almog
 */
public class ButtonsDemo extends Demo {

    public String getName() {
        return "Buttons";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("buttonHelp", "Help description");
    }

    protected void executeDemo(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        ComponentGroup buttonGroup = new ComponentGroup();
        Label buttonLabel = new Label("Buttons");
        buttonLabel.setUIID("TitleLabel");
        f.addComponent(buttonLabel);
        final Button left = new Button("Left Alignment");
        final Button right = new Button("Right Alignment");
        final Button center = new Button("Center Alignment");
        buttonGroup.addComponent(left);
        buttonGroup.addComponent(right);
        buttonGroup.addComponent(center);
        f.addComponent(buttonGroup);
        
        left.getUnselectedStyle().setAlignment(Label.LEFT);
        left.getSelectedStyle().setAlignment(Label.LEFT);
        left.getPressedStyle().setAlignment(Label.LEFT);
        right.getUnselectedStyle().setAlignment(Label.RIGHT);
        right.getSelectedStyle().setAlignment(Label.RIGHT);
        right.getPressedStyle().setAlignment(Label.RIGHT);
        center.getUnselectedStyle().setAlignment(Label.CENTER);
        center.getSelectedStyle().setAlignment(Label.CENTER);
        center.getPressedStyle().setAlignment(Label.CENTER);


        ComponentGroup  checkboxGroup = new ComponentGroup();
        Label cbLabel = new Label("CheckBox");
        cbLabel.setUIID("TitleLabel");
        f.addComponent(cbLabel);
        CheckBox firstCB = new CheckBox("First CheckBox");
        checkboxGroup.addComponent(firstCB);
        CheckBox secondCB = new CheckBox("Second CheckBox");
        checkboxGroup.addComponent(secondCB);
        CheckBox disabledCB = new CheckBox("Disabled CheckBox");
        checkboxGroup.addComponent(disabledCB);
        disabledCB.setEnabled(false);
        f.addComponent(checkboxGroup);

        Label bordersLabel = new Label("Borders");
        bordersLabel.setUIID("TitleLabel");
        f.addComponent(bordersLabel);

        ComponentGroup radioGroup = new ComponentGroup();
        final RadioButton defaultBorder = new RadioButton("Default");
        radioGroup.addComponent(defaultBorder);
        
        final RadioButton etchedBorder = new RadioButton("Etched Raised Theme");
        radioGroup.addComponent(etchedBorder);

        final RadioButton etchedColors = new RadioButton("Etched Raised Colors");
        radioGroup.addComponent(etchedColors);

        final RadioButton etchedLowBorder = new RadioButton("Etched Lowered Theme");
        radioGroup.addComponent(etchedLowBorder);

        final RadioButton etchedLowColors = new RadioButton("Etched Lowered Colors");
        radioGroup.addComponent(etchedLowColors);

        final RadioButton bevelBorder = new RadioButton("Bevel Raised Theme");
        radioGroup.addComponent(bevelBorder);

        final RadioButton bevelColors = new RadioButton("Bevel Raised Colors");
        radioGroup.addComponent(bevelColors);

        final RadioButton bevelLowBorder = new RadioButton("Bevel Lowered Theme");
        radioGroup.addComponent(bevelLowBorder);

        final RadioButton bevelLowColors = new RadioButton("Bevel Lowered Colors");
        radioGroup.addComponent(bevelLowColors);

        final RadioButton roundBorder = new RadioButton("Round Theme");
        radioGroup.addComponent(roundBorder);

        final RadioButton roundColors = new RadioButton("Round Colors");
        radioGroup.addComponent(roundColors);

        RadioButton disabledRadioButton = new RadioButton("Disabled Radio Button");
        disabledRadioButton.setEnabled(false);
        radioGroup.addComponent(disabledRadioButton);

        f.addComponent(radioGroup);
        
        ActionListener listener = new ActionListener() {
            private Border lastBorder;
            private void setBorder(Border b) {
                lastBorder = b;
                left.getStyle().setBorder(b);
                right.getStyle().setBorder(b);
                center.getStyle().setBorder(b);
                f.repaint();
            }
            
            public void actionPerformed(ActionEvent evt) {
                Object source = evt.getSource();
                
                if(source == defaultBorder) {
                    setBorder(Border.getDefaultBorder());
                    return;
                }
                
                if(source == etchedBorder) {
                    setBorder(Border.createEtchedRaised());
                    return;
                }

                if(source == etchedColors) {
                    setBorder(Border.createEtchedRaised(0x020202, 0xBBBBBB));
                    return;
                }

                if(source == etchedLowBorder) {
                    setBorder(Border.createEtchedLowered());
                    return;
                }

                if(source == etchedLowColors) {
                    setBorder(Border.createEtchedLowered(0x020202, 0xBBBBBB));
                    return;
                }

                if(source == bevelBorder) {
                    setBorder(Border.createBevelRaised());
                    return;
                }

                if(source == bevelColors) {
                    setBorder(Border.createBevelRaised(0xdddddd, 0xAAAAAA, 0x111111,0x020202));
                    return;
                }

                if(source == bevelLowBorder) {
                    setBorder(Border.createBevelLowered());
                    return;
                }

                if(source == bevelLowColors) {
                    setBorder(Border.createBevelLowered(0xdddddd, 0xAAAAAA, 0x111111,0x020202));
                    return;
                }

                if(source == roundBorder) {
                    setBorder(Border.createRoundBorder(8, 8));
                    return;
                }

                if(source == roundColors) {
                    setBorder(Border.createRoundBorder(8, 8, 0xcccccc));
                    return;
                }
            }
        };

        ButtonGroup group = new ButtonGroup();
        group.add(defaultBorder);
        defaultBorder.addActionListener(listener);
        group.add(etchedBorder);
        etchedBorder.addActionListener(listener);
        group.add(etchedColors);
        etchedColors.addActionListener(listener);
        group.add(etchedLowBorder);
        etchedLowBorder.addActionListener(listener);
        group.add(etchedLowColors);
        etchedLowColors.addActionListener(listener);
        group.add(bevelBorder);
        bevelBorder.addActionListener(listener);
        bevelColors.addActionListener(listener);
        group.add(bevelColors);
        bevelLowBorder.addActionListener(listener);
        group.add(bevelLowBorder);
        bevelLowColors.addActionListener(listener);
        group.add(bevelLowColors);
        roundBorder.addActionListener(listener);
        group.add(roundBorder);
        roundColors.addActionListener(listener);
        group.add(roundColors);
        //setAsDefault.addActionListener(listener);
    }
}
