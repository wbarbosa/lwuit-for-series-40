/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.spinner.Spinner;

/**
 * Demonstrates layout, replace and grow animations in some detail.
 *
 * @author Shai Almog
 */
public class AnimationDemo extends Demo {
    static final int LAYOUT_ANIMATION_SPEED = 1000;
    public String getName() {
        return "Animations";
    }

    protected void executeDemo(final Container f) {
        final Button replaceSlideV = new Button("Replace Slide Vertical");
        final Button replaceSlideH = new Button("Replace Slide Horizontal");
        final Button replaceFade = new Button("Replace Fade");
        final Button grow = new Button("Grow");
        final Button shrink = new Button("Shrink");
        final Button interlace = new Button("Interlace");
        final Button slideFromAbove = new Button("Slide From Above");
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        final ComponentGroup grp = new ComponentGroup();
        grp.addComponent(replaceSlideH);
        grp.addComponent(replaceSlideV);
        grp.addComponent(replaceFade);
        grp.addComponent(grow);
        grp.addComponent(shrink);
        grp.addComponent(interlace);
        grp.addComponent(slideFromAbove);
        f.addComponent(grp);

        f.addComponent(new Label("Animation Speed In Milliseconds"));
        final Spinner animationSpeed = Spinner.create(200, 6000, 500, 100);
        f.addComponent(animationSpeed);

        ActionListener l = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int speed = ((Integer)animationSpeed.getValue()).intValue();
                if(replaceSlideV == evt.getSource()) {
                    Label destination = new Label("Place Anything Here....");
                    if(replaceSlideV.getUIID().startsWith(grp.getElementUIID())) {
                        destination.setUIID(replaceSlideV.getUIID());
                    }
                    grp.replaceAndWait(replaceSlideV, destination,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, speed), LAYOUT_ANIMATION_SPEED);
                    grp.replaceAndWait(destination, replaceSlideV,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, speed), LAYOUT_ANIMATION_SPEED);
                    return;
                }
                if(replaceSlideH == evt.getSource()) {
                    TextField destination = new TextField("Place Anything Here....");
                    if(replaceSlideH.getUIID().startsWith(grp.getElementUIID())) {
                        destination.setUIID(replaceSlideH.getUIID());
                    }
                    grp.replaceAndWait(replaceSlideH, destination,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, speed), LAYOUT_ANIMATION_SPEED);
                    grp.replaceAndWait(destination, replaceSlideH,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, speed), LAYOUT_ANIMATION_SPEED);
                    return;
                }
                if(replaceFade == evt.getSource()) {
                    CheckBox destination = new CheckBox("Place Anything Here....");
                    if(replaceFade.getUIID().startsWith(grp.getElementUIID())) {
                        destination.setUIID(replaceFade.getUIID());
                    }
                    grp.replaceAndWait(replaceFade, destination, CommonTransitions.createFade(speed), LAYOUT_ANIMATION_SPEED);
                    grp.replaceAndWait(destination, replaceFade, CommonTransitions.createFade(speed), LAYOUT_ANIMATION_SPEED);
                    return;
                }
                if(grow == evt.getSource()) {
                    Dimension dim = grow.getPreferredSize();
                    dim.setWidth(dim.getWidth() * 2);
                    dim.setHeight(dim.getHeight() * 2);
                    grow.setPreferredSize(dim);
                    grp.setShouldCalcPreferredSize(true);
                    grp.animateLayoutAndWait(speed);
                    grow.setPreferredSize(null);
                    grp.setShouldCalcPreferredSize(true);
                    grp.animateLayout(speed);
                    return;
                }
                if(shrink == evt.getSource()) {
                    Dimension dim = shrink.getPreferredSize();
                    dim.setWidth(dim.getWidth() / 2);
                    dim.setHeight(dim.getHeight() / 2);
                    shrink.setPreferredSize(dim);
                    grp.setShouldCalcPreferredSize(true);
                    grp.animateLayoutAndWait(speed);
                    shrink.setPreferredSize(null);
                    grp.setShouldCalcPreferredSize(true);
                    grp.animateLayout(speed);
                    return;
                }
                if(interlace == evt.getSource()) {
                    for(int iter = 0 ; iter < grp.getComponentCount() ; iter++) {
                        Component c = grp.getComponentAt(iter);
                        if(iter % 2 == 0) {
                            c.setX(-c.getWidth());
                        } else {
                            c.setX(c.getWidth());
                        }
                    }
                    grp.setShouldCalcPreferredSize(true);
                    grp.animateLayout(speed);
                    return;
                }
                if(slideFromAbove == evt.getSource()) {
                    for(int iter = 0 ; iter < f.getComponentCount() ; iter++) {
                        Component c = f.getComponentAt(iter);
                        c.setY(-c.getHeight());
                    }
                    f.setShouldCalcPreferredSize(true);
                    f.animateLayout(speed);
                    return;
                }
            }
        };
        replaceSlideV.addActionListener(l);
        replaceSlideH.addActionListener(l);
        replaceFade.addActionListener(l);
        grow.addActionListener(l);
        shrink.addActionListener(l);
        interlace.addActionListener(l);
        slideFromAbove.addActionListener(l);
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("animationHelp", "Help description");
    }
    
    
}