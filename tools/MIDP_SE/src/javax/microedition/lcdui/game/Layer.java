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

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

/**
 * A Layer is an abstract class representing a visual element of a game. Each Layer has position (in terms of the upper-left corner of its visual bounds), width, height, and can be made visible or invisible. Layer subclasses must implement a paint(Graphics) method so that they can be rendered.
 * The Layer's (x,y) position is always interpreted relative to the coordinate system of the Graphics object that is passed to the Layer's paint() method. This coordinate system is referred to as the painter's coordinate system. The initial location of a Layer is (0,0).
 * Since: MIDP 2.0
 */
public abstract class Layer{
    /**
     * Gets the current height of this layer, in pixels.
     */
    public final int getHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current width of this layer, in pixels.
     */
    public final int getWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the horizontal position of this Layer's upper-left corner in the painter's coordinate system.
     */
    public final int getX(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the vertical position of this Layer's upper-left corner in the painter's coordinate system.
     */
    public final int getY(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the visibility of this Layer.
     */
    public final boolean isVisible(){
        return false; //TODO codavaj!!
    }

    /**
     * Moves this Layer by the specified horizontal and vertical distances. The Layer's coordinates are subject to wrapping if the passed parameters will cause them to exceed beyond Integer.MAX_VALUE or Integer.MIN_VALUE.
     */
    public void move(int dx, int dy){
        return; //TODO codavaj!!
    }

    /**
     * Paints this Layer if it is visible. The upper-left corner of the Layer is rendered at it's current (x,y) position relative to the origin of the provided Graphics object. Applications may make use of Graphics clipping and translation to control where the Layer is rendered and to limit the region that is rendered.
     * Implementations of this method are responsible for checking if this Layer is visible; this method does nothing if the Layer is not visible.
     * The attributes of the Graphics object (clip region, translation, drawing color, etc.) are not modified as a result of calling this method.
     */
    public abstract void paint(Graphics g);

    /**
     * Sets this Layer's position such that its upper-left corner is located at (x,y) in the painter's coordinate system. A Layer is located at (0,0) by default.
     */
    public void setPosition(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Sets the visibility of this Layer. A visible Layer is rendered when its
     * method is called; an invisible Layer is not rendered.
     */
    public void setVisible(boolean visible){
        return; //TODO codavaj!!
    }

}
