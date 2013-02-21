/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.components;

import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;

/**
 * A simple button implementation with button image and click handling.
 */
public class Button
    extends Component {

    // Button states
    public static final int UNPRESSED = 1;
    public static final int PRESSED = 2;
    public static final int SELECTED = 3;
    public static final int DISABLED = 4;
    
    protected int state = UNPRESSED;
    protected Image imgUnpressed, imgPressed, imgSelected, imgDisabled;
    
    // Refs to buttons that are next and previous
    // when moving between buttons on non-touch
    private Button previous, next;

    public Button(String imgUnpressedUrl, String imgPressedUrl,
        String imgSelectedUrl, String imgDisabledUrl) {
        super();

        imgUnpressed = imgUnpressedUrl != null ? ImageUtil.loadImage(imgUnpressedUrl) : null;
        imgPressed = imgPressedUrl != null ? ImageUtil.loadImage(imgPressedUrl) : null;
        imgSelected = imgSelectedUrl != null ? ImageUtil.loadImage(imgSelectedUrl) : null;
        imgDisabled = imgDisabledUrl != null ? ImageUtil.loadImage(imgDisabledUrl) : null;

        setFocusable(false);
        Style style = getStyle();
        style.setBgTransparency(0x00);
        style.setMargin(0, 0, 0, 0);
        style.setPadding(0, 0, 0, 0);
    }

    public void paint(Graphics g) {
        int baseX = this.getX();
        int baseY = this.getY();

        // Draw the correct image depending on state
        Image drawnImg = null;
        if (state == UNPRESSED) {
            drawnImg = imgUnpressed;
        }
        else if (state == PRESSED) {
            drawnImg = imgPressed;
        }
        else if (state == SELECTED) {
            drawnImg = imgSelected;
        }
        else if (state == DISABLED) {
            drawnImg = imgDisabled;
        }
        g.drawImage(drawnImg, baseX, baseY);
    }

    public void pointerPressed(int x, int y) {
        if (encloses(x, y) && state != DISABLED) {
            onPress();
        }
    }

    public void pointerDragged(int x, int y) {
        if (!encloses(x, y) && state != DISABLED) {
            onDrag();
        }
    }

    public void pointerReleased(int x, int y) {
        if (encloses(x, y) && state != DISABLED) {
            onRelease();
        }
    }

    public void onPress() {
        if (state != DISABLED) {
            state = PRESSED;
            repaint();
        }
    }

    public void onDrag() {
        if (state != DISABLED) {
            state = UNPRESSED;
            repaint();
        }
    }

    public void onRelease() {
        if (state != DISABLED) {
            if (Compatibility.TOUCH_SUPPORTED || imgSelected == null) {
                state = UNPRESSED;
            }
            else {
                state = SELECTED;
            }
            repaint();
            onClick();
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            state = SELECTED;
        }
        else {
            state = UNPRESSED;
        }
        repaint();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            state = UNPRESSED;
        }
        else {
            state = DISABLED;
        }
        repaint();
    }

    public void setNext(Button next) {
        this.next = next;
        next.setPrevious(this);
    }

    public Button getNext() {
        return next;
    }

    public Button getPrevious() {
        return previous;
    }

    /**
     * Invoked when the button is clicked.
     */
    public void onClick() {
    }

    private void setPrevious(Button previous) {
        this.previous = previous;
    }

    private boolean encloses(int x, int y) {
        return x > getAbsoluteX() && x < getAbsoluteX() + getWidth() &&
               y > getAbsoluteY() && y < getAbsoluteY() + getHeight();
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(imgUnpressed.getWidth(), imgUnpressed.getHeight());
    }
}
