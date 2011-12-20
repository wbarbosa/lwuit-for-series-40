/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.lwuit.animations;

import com.sun.lwuit.Graphics;

/**
 * Transitions utilizing the M3G API for 3D effects, this transition requires
 * M3G (JSR 184) support on the device in order to work properly. Currently
 * none of these transitions work with dialogs or any component type that 
 * is not a form
 *
 * @author Shai Almog
 */
public final class Transition3D extends Transition {
    
    private Transition3D(int transitionType) {
    }
    
    /**
     * @inheritDoc
     */
    public void cleanup() {
    }
    
    /**
     * Allows performance/memory sensitive devices to define a maximum size for the 
     * texture thus increasing performance on the expense of quality.
     * @param size
     */
    public static void setMaxTextureDimension(int size) {
    }
    
    /**
     * Creates a rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createRotation(int duration, boolean rotateRight) {
        return new Transition3D(0);
    }

    /**
     * Creates a rotation transition from the current form to the next dialog, in this rotation only
     * the dialog will rotate and the form will remain static
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createStaticRotation(int duration, boolean rotateRight) {
        return new Transition3D(0);
    }

    /**
     * Creates a rotation transition from the top to the bottom giving a feeling of Swinging into place
     * 
     * @param duration duration in milliseconds of the transition
     * @return new transtion object
     */
    public static Transition3D createSwingIn(int duration) {
        return new Transition3D(0);
    }

    /**
     * Creates a rotation transition from the top to the bottom giving a feeling of Swinging into place
     * 
     * @param duration duration in milliseconds of the transition
     * @param topDown indicates rotating downwards or upwards
     * @return new transtion object
     */
    public static Transition3D createSwingIn(int duration, boolean topDown) {
        return new Transition3D(0);
    }

    /**
     * Creates a cube rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createCube(int duration, boolean rotateRight) {
        return new Transition3D(0);
    }

    /**
     * Creates a cube rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateDown indicates rotating towards the upper side when true
     * @return newly created transition object
     */
    public static Transition3D createVerticalCube(int duration, boolean rotateDown) {
        return new Transition3D(0);
    }

    /**
     * Creates a fly in transition object.
     * 
     * @param duration duration in milliseconds of the transition
     * @return newly created transition object
     */
    public static Transition3D createFlyIn(int duration) {
        return new Transition3D(0);
    }
    
    /**
     * @inheritDoc
     */
    public Transition copy() {
        return new Transition3D(0);
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        return false;
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
    }

    /**
     * @inheritDoc
     */
    public void initTransition() {
    }
    
    /**
     * High quality mode renders the transition using smoother graphics but can
     * take a whole lot more memory per texture and bitmap resulting in a likely
     * out of memory error on high resolution/low memory devices with complex UI
     * elements.
     * 
     * @return whether this is high quality rendering mode
     */
    public boolean isHighQualityMode() {
        return false;
    }

    /**
     * High quality mode renders the transition using smoother graphics but can
     * take a whole lot more memory per texture and bitmap resulting in a likely
     * out of memory error on high resolution/low memory devices with complex UI
     * elements.
     * 
     * @param highQualityMode indicates whether this is the high quality mode
     */
    public void setHighQualityMode(boolean highQualityMode) {
    }
}
