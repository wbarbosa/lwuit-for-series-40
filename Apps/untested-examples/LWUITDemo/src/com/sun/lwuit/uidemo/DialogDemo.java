/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.M3G;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.animations.Transition;
import com.sun.lwuit.animations.Transition3D;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.plaf.UIManager;

/**
 * Demonstrates the dialog box functionality 
 *
 * @author Shai Almog
 */
public class DialogDemo extends Demo {
    private static final String[] ALARM_TEXT = {"Alarm", "Confirmation", "Error", "Info", "Warning"};
    private static final int[] ALARM_VALUES = {
        Dialog.TYPE_ALARM, Dialog.TYPE_CONFIRMATION, 
        Dialog.TYPE_ERROR, Dialog.TYPE_INFO, Dialog.TYPE_WARNING
    };
    private static final String[] TINT_DESCRIPTION = {"Black", "White", "Red", "Green", "Blue"};
    private static final int[] TINT_COLOR = {0x8f000000, 0x8fffffff, 0x8fff0000, 0x8f00ff00, 0x8f0000ff};
    private static final String[] TRANSITION_TEXT = {"Default", "Slide Up", "Slide Down", "Fade", "Static Rotation", "Rotation", "Fly In", "Cube", "Swing In", "None"};
    private static final String[] TRANSITION_TEXT_NO3D = {"Default", "Slide Up", "Slide Down", "Fade"};
    
    public String getName() {
        return "Dialogs";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("dialogHelp", "Help description");
    }

    
    protected void executeDemo(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
       
        Button b = new Button("Show Dialog");
        final Button popup = new Button("Show Popup");
        final ComboBox sound = new ComboBox(ALARM_TEXT);
        final ComboBox tint = new ComboBox(TINT_DESCRIPTION);
        final ComboBox transition = new ComboBox(TRANSITION_TEXT);
        if(!M3G.isM3GSupported()) {
            transition.setModel(new DefaultListModel(TRANSITION_TEXT_NO3D));
        }
        final TextArea title = new TextField("Title");   
        final TextArea body = new TextField("This is the body of the alert....", 20);
        body.setSingleLineTextArea(false);
        body.setMaxSize(255);
        body.setRows(3);
        final TextField timeoutText = new TextField("0");
        timeoutText.setConstraint(TextArea.NUMERIC);
        timeoutText.setInputModeOrder(new String[] {"123"});

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().getCurrent().setTintColor(TINT_COLOR[tint.getSelectedIndex()]);
                Command okCommand = new Command("OK");
                int timeout = Integer.parseInt(timeoutText.getText());
                Transition transitionSelection = null;
                switch(transition.getSelectedIndex()) {
                case 0:
                    if(evt.getSource() == popup) {
                        break;
                    }
                    Dialog.show(title.getText(), body.getText(), okCommand,
                        new Command[] {okCommand}, ALARM_VALUES[sound.getSelectedIndex()], null,
                        timeout);
                    return;
                case 1:
                    transitionSelection = CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000);
                    break;
                case 2:
                    transitionSelection = CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 1000);
                    break;
                case 3:
                    transitionSelection = CommonTransitions.createFade(400);
                    break;
                case 4:
                    transitionSelection = Transition3D.createStaticRotation(500, true);
                    break;
                case 5:
                    transitionSelection = Transition3D.createRotation(500, true);
                    break;
                case 6:
                    transitionSelection = Transition3D.createFlyIn(500);
                    break;
                case 7:
                    transitionSelection = Transition3D.createCube(500, true);
                    break;
                case 8:
                    transitionSelection = Transition3D.createSwingIn(1000);
                    break;
                }
                if(evt.getComponent() == popup) {
                    Dialog d = new Dialog(title.getText());
                    d.setLayout(new BorderLayout());
                    TextArea dialogBody = new TextArea(body.getText(), 3, 20);
                    dialogBody.setUIID("Label");
                    d.addComponent(BorderLayout.CENTER, dialogBody);
                    d.addCommand(okCommand);
                    if(timeout > 0) {
                        d.setTimeout(timeout);
                    }
                    d.setTransitionInAnimator(transitionSelection);
                    d.showPopupDialog(popup);
                } else {
                    Dialog.show(title.getText(), body.getText(), okCommand,
                        new Command[] {okCommand}, ALARM_VALUES[sound.getSelectedIndex()], null,
                        timeout, transitionSelection);
                }
            }
        };
        b.addActionListener(al);
        popup.addActionListener(al);

        Container largest = createPair("Transition", transition, 30);
        int largestW = largest.getComponentAt(0).getPreferredW();
        f.addComponent(createPair("Title", title, largestW));
        f.addComponent(createPair("Body", body, largestW));
        f.addComponent(createPair("Timeout", timeoutText, largestW));
        f.addComponent(createPair("Sound", sound, largestW));
        f.addComponent(createPair("Tint Color", tint, largestW));
        f.addComponent(largest);
        Label l = new Label("The button shows the appropriate dialog");
        l.setUIID("TitleLabel");
        f.addComponent(l);

        ComponentGroup buttonContainer = new ComponentGroup();
        buttonContainer.setElementUIID("ButtonGroup");
        buttonContainer.addComponent(b);
        buttonContainer.addComponent(popup);
        f.addComponent(buttonContainer);

    }
}
