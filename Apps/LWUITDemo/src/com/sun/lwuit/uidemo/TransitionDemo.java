/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.M3G;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.animations.Transition;
import com.sun.lwuit.animations.Transition3D;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.spinner.Spinner;
import com.sun.lwuit.plaf.UIManager;

/**
 * Transitons between screens
 *
 * @author Shai Almog
 */
public class TransitionDemo extends Demo {
    /**
     * The selected radio button index 
     */
    private static int selectedIndex = 0;

    public String getName() {
        return "Transitions";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("transitionHelp", "Help description");
    }

    private RadioButton createRB(String label, ButtonGroup g, Container f) {
        RadioButton b = new RadioButton(label);
//        Style s = b.getStyle();
//        s.setMargin(0, 0, 0, 0);
//        s.setBgTransparency(70);
        g.add(b);
        f.addComponent(b);
        return b;
    }
    
    protected void executeDemo(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Label title = new Label("Please select a transition type");
        title.setUIID("TitleLabel");
        f.addComponent(title);

        final ButtonGroup radioButtonGroup = new ButtonGroup();
        ComponentGroup radios = new ComponentGroup();
        createRB("Slide Horizontal", radioButtonGroup, radios);
        createRB("Slide Vertical", radioButtonGroup, radios);
        createRB("Fade", radioButtonGroup, radios);
        if(M3G.isM3GSupported()) {
            createRB("Rotate", radioButtonGroup, radios);
            createRB("Fly In", radioButtonGroup, radios);
            createRB("Cube", radioButtonGroup, radios);
            createRB("Static Rotation", radioButtonGroup, radios);
            createRB("Swing Top", radioButtonGroup, radios);
            createRB("Swing Bottom", radioButtonGroup, radios);
        }
        f.addComponent(radios);

        radioButtonGroup.setSelected(selectedIndex);

        final Spinner speed = Spinner.create(0, 50000, 500, 100);
        Label speedLabel = new Label("Speed");
        speedLabel.setUIID("TitleLabel");
        f.addComponent(speedLabel);
        f.addComponent(speed);

        final Form destination = new Form("Destination");
        destination.addComponent(new Label("This is the transition destination..."));
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent evt) {
                f.getComponentForm().show();
            }
        };
        destination.addCommand(backCommand);
        destination.setBackCommand(backCommand);
        
        final Button updateButton = new Button("Preview Transition");
        final Button applyButton = new Button("Apply Transition");
        final Button applyMenu = new Button("Apply To Menu");
        
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int runSpeed = ((Integer)speed.getValue()).intValue();
                Transition in, out;
                switch (radioButtonGroup.getSelectedIndex()) {
                    case 0: {
                        out = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, runSpeed);
                        in = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, runSpeed);
                        break;
                    }
                    case 1: {
                        out = CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, runSpeed);
                        in = CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, runSpeed);
                        break;
                    }
                    case 2: {
                        out = CommonTransitions.createFade(runSpeed);
                        in = CommonTransitions.createFade(runSpeed);
                        break;
                    }
                    case 3:  {
                        out = Transition3D.createRotation(runSpeed, true);
                        in = Transition3D.createRotation(runSpeed, false);
                        break;
                    }
                    case 4:  {
                        out = Transition3D.createFlyIn(runSpeed);
                        in = Transition3D.createFlyIn(runSpeed);
                        break;
                    }
                    case 5:  {
                        out = Transition3D.createCube(runSpeed, true);
                        in = Transition3D.createCube(runSpeed, false);
                        break;
                    }
                    case 6:  {
                        out = Transition3D.createStaticRotation(runSpeed, true);
                        in = Transition3D.createStaticRotation(runSpeed, false);
                        break;
                    }
                    case 7:  {
                        out = Transition3D.createSwingIn(runSpeed);
                        in = out;
                        break;
                    }
                    default:  {
                        out = Transition3D.createSwingIn(runSpeed, false);
                        in = out;
                        break;
                    }
                }
                selectedIndex = radioButtonGroup.getSelectedIndex();
                f.getComponentForm().setTransitionOutAnimator(CommonTransitions.createEmpty());
                if(updateButton == ev.getSource()) {
                    destination.setTransitionOutAnimator(out);
                    destination.setTransitionInAnimator(in);
                    destination.show();
                } else {
                    if(applyButton == ev.getSource()) {
                        UIDemoMain.setTransition(in, out);
                    } else {
                        if(applyMenu == ev.getSource()) {
                            UIDemoMain.setMenuTransition(in, out);
                        }
                    }
                }
            }
        };
        updateButton.addActionListener(listener);
        applyButton.addActionListener(listener);
        applyMenu.addActionListener(listener);

        ComponentGroup buttonPanel = new ComponentGroup();
        buttonPanel.setElementUIID("ButtonGroup");
        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(applyButton);
        buttonPanel.addComponent(applyMenu);
        f.addComponent(buttonPanel);
    }    
}