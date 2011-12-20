/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package com.sun.lwuit.resource.util;

import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * Wraps a LWUIT component in a Swing component for preview purposes, this is
 * effectively the "live preview" API
 *
 * @author Shai Almog
 */
public class LWUITComponentWrapper extends JLabel {
    private com.sun.lwuit.Component lwuitCmp;
    public LWUITComponentWrapper() {
        this(new Button("Preview"));
    }
    public LWUITComponentWrapper(com.sun.lwuit.Component lwuitCmp) {
        this(lwuitCmp, false);
    }
    public LWUITComponentWrapper(com.sun.lwuit.Component lwuitCmp, boolean forceShow) {
        this.lwuitCmp = lwuitCmp;
        if(lwuitCmp.getParent() == null) {
            if(!(lwuitCmp instanceof Form)) {
                Form dummy = new Form("");
                dummy.setLayout(new com.sun.lwuit.layouts.BorderLayout());
                dummy.addComponent(com.sun.lwuit.layouts.BorderLayout.CENTER, lwuitCmp);
                dummy.setWidth(1000);
                dummy.setHeight(1000);
                dummy.layoutContainer();
                if(forceShow || com.sun.lwuit.Display.getInstance().getCurrent() == null) {
                    dummy.show();
                }
            } else {
                ((Form)lwuitCmp).layoutContainer();
                if(com.sun.lwuit.Display.getInstance().getCurrent() == null) {
                    if(lwuitCmp instanceof com.sun.lwuit.Dialog) {
                        ((com.sun.lwuit.Dialog)lwuitCmp).showModeless();
                    } else {
                        ((Form)lwuitCmp).show();
                    }
                }
            }
        }
    }
    
    @Override
    public void setText(String s) {
        if(lwuitCmp != null && lwuitCmp instanceof Label) {
            ((Label)lwuitCmp).setText(s);
        }
    }
    
    @Override
    public String getText() {
        if(lwuitCmp != null && lwuitCmp instanceof Label) {
            return ((Label)lwuitCmp).getText();
        }
        return "";
    }
    
    public Component getLWUITComponent() {
        return lwuitCmp;
    }

    public void setLWUITComponent(com.sun.lwuit.Component l) {
        lwuitCmp = l;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(lwuitCmp.getPreferredW(), lwuitCmp.getPreferredH());
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    private com.sun.lwuit.Image buffer;

    @Override
    public void paint(Graphics g) {
        if(isEnabled()) {
            try {
                if(buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
                    buffer = com.sun.lwuit.Image.createImage(getWidth(), getHeight(), 0);
                }
                com.sun.lwuit.Display.getInstance().callSeriallyAndWait(new Runnable() {
                    public void run() {
                        lwuitCmp.setX(0);
                        lwuitCmp.setY(0);
                        lwuitCmp.setWidth(getWidth());
                        lwuitCmp.setHeight(getHeight());
                        com.sun.lwuit.Form parentForm = lwuitCmp.getComponentForm();
                        if(parentForm.getWidth() == 0 || parentForm != lwuitCmp) {
                            parentForm.setWidth(getWidth());
                            parentForm.setHeight(getHeight());
                            parentForm.revalidate();
                        }
                        if(lwuitCmp instanceof com.sun.lwuit.Container) {
                            ((com.sun.lwuit.Container)lwuitCmp).revalidate();
                        }
                        com.sun.lwuit.Graphics gl = buffer.getGraphics();
                        gl.setColor(0xcccccc);
                        gl.fillRect(0, 0, getWidth(), getHeight());
                        gl.setClip(0, 0, buffer.getWidth(), buffer.getHeight());
                        lwuitCmp.setVisible(true);
                        lwuitCmp.paintComponent(gl);
                    }
                }, 300);
                g.drawImage((java.awt.Image)buffer.getImage(), 0, 0, this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            super.paintChildren(g);
        }
    }
}
