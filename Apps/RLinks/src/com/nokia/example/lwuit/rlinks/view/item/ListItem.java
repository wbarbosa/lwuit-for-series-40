/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.mid.ui.DirectUtils;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.util.EventDispatcher;

/**
 * A clickable container for creating lists with variable height. Optimized for smooth scrolling.
 */
public class ListItem
        extends Item
        implements ActionListener {

    protected boolean repaint = true;
    private final Button leadComponent = new Button();
    private final EventDispatcher dispatcher = new EventDispatcher();
    private Image buffer = null;

    public ListItem() {
        setUIID("ListItem");
        setLeadComponent(leadComponent);
        leadComponent.setUIID("LeadComponent");
        addComponent(leadComponent);

        addActionListener(this);
        leadComponent.addActionListener(new ActionListener() {

            /**
             * Create new ActionEvent with ListItem as source instead of lead component.
             */
            public void actionPerformed(ActionEvent evt) {
                ActionEvent event;
                Command cmd = evt.getCommand();
                if (cmd != null) {
                    event = new ActionEvent(cmd, ListItem.this, evt.getX(), evt.getY());
                }
                else {
                    event = new ActionEvent(ListItem.this, evt.getX(), evt.getY());
                }
                dispatcher.fireActionEvent(event);
            }
        });
    }

    /**
     * Adds a listener to the button which will cause an event to dispatch on click
     *
     * @param l implementation of the action listener interface
     */
    public void addActionListener(ActionListener l) {
        dispatcher.addListener(l);
    }

    /**
     * Removes the given action listener from the button
     *
     * @param l implementation of the action listener interface
     */
    public void removeActionListener(ActionListener l) {
        dispatcher.removeListener(l);
    }

    /**
     * Handle ActionEvents. Can be safely overridden in derived classes.
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
    }

    /**
     * Capture fire clicked event, so that lead component can be notified.
     * Cannot call leadComponents fireClicked directly, but this is what
     * Button would basically do. This would happen automatically in an
     * ideal world.
     */
    protected void fireClicked() {
        leadComponent.pressed();
        leadComponent.released();
    }

    /**
     * Release hard reference to the buffer image
     */
    protected void deinitialize() {
        super.deinitialize();
        buffer = null;
        repaint = true;
    }

    /**
     * Paint component using image buffer
     * @param g
     */
    public void paint(Graphics g) {
        if (repaint) {
            updateBuffer();
            repaint = false;
        }
        if (buffer != null) {
            g.drawImage(buffer, getX(), getY());
        }
        else {
            super.paint(g);
        }
    }

    /**
     * Update component's image buffer
     */
    public void updateBuffer() {
        int width = getWidth();
        int height = getHeight();
        if (width < 1 || height < 1) {
            return;
        }
        try {
            Image img = Image.createImage(DirectUtils.createImage(width, height, 0x00000000));
            Graphics bg = img.getGraphics();
            int x = bg.getTranslateX();
            int y = bg.getTranslateY();
            bg.translate(-getX(), -getY());
            super.paint(bg);
            bg.translate(x, y);
            buffer = img;
        }
        catch (OutOfMemoryError oome) {
            // Not enough memory for image buffering
        }
    }

    /**
     * Set width and update image buffer if width changed
     * @param width
     */
    public void setWidth(int width) {
        if (width != getWidth()) {
            super.setWidth(width);
            updateBuffer();
        }
    }

    /**
     * Set height and update image buffer if height changed
     * @param width
     */
    public void setHeight(int height) {
        if (height != getHeight()) {
            super.setHeight(height);
            updateBuffer();
        }
    }

    /**
     * Obey repaint call
     */
    public void repaint() {
        repaint = true;
        super.repaint();
    }
}
