/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Vector;

/**
 * Demonstrates the usage and comparison between system fonts and custom bitmap
 * fonts
 *
 * @author Shai Almog
 */
public class FontDemo extends Demo {
     public void cleanup() {
     }

    public String getName() {
        return "Fonts";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("fontHelp", "Help description");
    }

    protected void executeDemo(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(createFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM), "System Font"));
        f.addComponent(createFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD | Font.STYLE_ITALIC, Font.SIZE_LARGE), "Bold Italic Large System Font"));
        try {
            Resources res = Resources.open("/resources.res");
            f.addComponent(createFont(res.getFont("Dialog"), "Dialog 12 Anti-Aliased Bitmap Font"));
            f.addComponent(createFont(res.getFont("DialogInput"), "DialogInput 12 Anti-Aliased Bitmap Font"));
            f.addComponent(createFont(res.getFont("SansSerif"), "SansSerif 20 Anti-Aliased Bitmap Font"));
            f.addComponent(createFont(res.getFont("Monospaced"), "Monospaced 10 Anti-Aliased Bitmap Font"));
            Label l = createFont(res.getFont("Dialog"), "Dialog 12 Bitmap Font in Red");
            l.getStyle().setFgColor(0xff0000);
            f.addComponent(l);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ComponentGroup buttons = new ComponentGroup();
        buttons.setElementUIID("ToggleButton");
        buttons.setHorizontal(true);
        final RadioButton plain = new RadioButton("Plain");
        final RadioButton underline = new RadioButton("Underline");
        final RadioButton strikeout = new RadioButton("Strikethru");
        final RadioButton threeD = new RadioButton("3D Raised");
        final RadioButton threeDLow = new RadioButton("3D Lowered");

        ActionListener listener = new ActionListener() {
            boolean restore;
            private void setComponentColorTo(int color) {
                for(int iter = 0 ; iter < f.getComponentCount() ; iter++) {
                    Component c = f.getComponentAt(iter);
                    if(c instanceof Label && c.getUnselectedStyle().getFgColor() != 0xff0000) {
                        c.getUnselectedStyle().setFgColor(color);
                    }
                }
            }

            public void actionPerformed(ActionEvent evt) {
                if(restore) {
                    // restore the font color if the 3D effect changed it
                    setComponentColorTo(new Label().getStyle().getFgColor());
                    restore = false;
                }
                if(evt.getSource() == plain) {
                    updateStyle(0);
                    return;
                }
                if(evt.getSource() == underline) {
                    updateStyle(Style.TEXT_DECORATION_UNDERLINE);
                    return;
                }
                if(evt.getSource() == strikeout) {
                    updateStyle(Style.TEXT_DECORATION_STRIKETHRU);
                    return;
                }
                if(evt.getSource() == threeD) {
                    // 3D looks awful with black
                    setComponentColorTo(0xffffff);
                    restore = true;
                    updateStyle(Style.TEXT_DECORATION_3D);
                    return;
                }
                if(evt.getSource() == threeDLow) {
                    // 3D lowered looks awful with white
                    setComponentColorTo(0);
                    restore = true;
                    updateStyle(Style.TEXT_DECORATION_3D_LOWERED);
                    return;
                }
            }
            private void updateStyle(int s) {
                for(int iter = 0 ; iter < f.getComponentCount() ; iter++) {
                    Component c = f.getComponentAt(iter);
                    if(c instanceof Label) {
                        c.getUnselectedStyle().setTextDecoration(s);
                    }
                }
                f.repaint();
            }
        };

        ButtonGroup bg = new ButtonGroup();
        initRb(bg, buttons, listener, plain);
        initRb(bg, buttons, listener, underline);
        initRb(bg, buttons, listener, strikeout);

        ComponentGroup buttons2;
        Container centerFlow = new Container(new FlowLayout(Component.CENTER));
        f.addComponent(centerFlow);
        centerFlow.addComponent(buttons);
        if(buttons.getPreferredW() >= Display.getInstance().getDisplayWidth() / 2) {
            buttons2 = new ComponentGroup();
            buttons2.setElementUIID("ToggleButton");
            buttons2.setHorizontal(true);
            centerFlow.addComponent(buttons2);
        } else {
            buttons2 = buttons;
        }

        initRb(bg, buttons2, listener, threeD);
        initRb(bg, buttons2, listener, threeDLow);
    }

    private void initRb(ButtonGroup bg, Container buttons, ActionListener listener, RadioButton rb) {
        bg.add(rb);
        rb.setToggle(true);
        buttons.addComponent(rb);
        rb.addActionListener(listener);
    }

    private Label createFont(Font f, String label) {
        Label fontLabel = new Label(label);
        fontLabel.getUnselectedStyle().setFont(f);
        fontLabel.getSelectedStyle().setFont(f);
        fontLabel.getUnselectedStyle().setBgTransparency(0);
        fontLabel.getSelectedStyle().setBgTransparency(0);
        
        fontLabel.setFocusable(true);
        return fontLabel;
    }
}
