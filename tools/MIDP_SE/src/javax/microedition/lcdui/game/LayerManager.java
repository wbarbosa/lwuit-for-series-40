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
 * The LayerManager manages a series of Layers. The LayerManager simplifies the process of rendering the Layers that have been added to it by automatically rendering the correct regions of each Layer in the appropriate order.
 * The LayerManager maintains an ordered list to which Layers can be appended, inserted and removed. A Layer's index correlates to its z-order; the layer at index 0 is closest to the user while a the Layer with the highest index is furthest away from the user. The indices are always contiguous; that is, if a Layer is removed, the indices of subsequent Layers will be adjusted to maintain continuity.
 * The LayerManager class provides several features that control how the game's Layers are rendered on the screen.
 * The view window controls the size of the visible region and its position relative to the LayerManager's coordinate system. Changing the position of the view window enables effects such as scrolling or panning the user's view. For example, to scroll to the right, simply move the view window's location to the right. The size of the view window controls how large the user's view will be, and is usually fixed at a size that is appropriate for the device's screen.
 * In this example, the view window is set to 85 x 85 pixels and is located at (52, 11) in the LayerManager's coordinate system. The Layers appear at their respective positions relative to the LayerManager's origin.
 * The paint(Graphics, int, int) method includes an (x,y) location that controls where the view window is rendered relative to the screen. Changing these parameters does not change the contents of the view window, it simply changes the location where the view window is drawn. Note that this location is relative to the origin of the Graphics object, and thus it is subject to the translation attributes of the Graphics object.
 * For example, if a game uses the top of the screen to display the current score, the view window may be rendered at (17, 17) to provide enough space for the score.
 * Since: MIDP 2.0
 */
public class LayerManager{
    /**
     * Creates a new LayerManager.
     */
    public LayerManager(){
         //TODO codavaj!!
    }

    /**
     * Appends a Layer to this LayerManager. The Layer is appended to the list of existing Layers such that it has the highest index (i.e. it is furthest away from the user). The Layer is first removed from this LayerManager if it has already been added.
     */
    public void append(Layer l){
        return; //TODO codavaj!!
    }

    /**
     * Gets the Layer with the specified index.
     */
    public Layer getLayerAt(int index){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the number of Layers in this LayerManager.
     */
    public int getSize(){
        return 0; //TODO codavaj!!
    }

    /**
     * Inserts a new Layer in this LayerManager at the specified index. The Layer is first removed from this LayerManager if it has already been added.
     */
    public void insert(Layer l, int index){
        return; //TODO codavaj!!
    }

    /**
     * Renders the LayerManager's current view window at the specified location.
     * The LayerManager renders each of its layers in order of descending index, thereby implementing the correct z-order. Layers that are completely outside of the view window are not rendered.
     * The coordinates passed to this method determine where the LayerManager's view window will be rendered relative to the origin of the Graphics object. For example, a game may use the top of the screen to display the current score, so to render the game's layers below that area, the view window might be rendered at (0, 20). The location is relative to the Graphics object's origin, so translating the Graphics object will change where the view window is rendered on the screen.
     * The clip region of the Graphics object is intersected with a region having the same dimensions as the view window and located at (x,y). The LayerManager then translates the graphics object such that the point (x,y) corresponds to the location of the viewWindow in the coordinate system of the LayerManager. The Layers are then rendered in the appropriate order. The translation and clip region of the Graphics object are restored to their prior values before this method returns.
     * Rendering is subject to the clip region and translation of the Graphics object. Thus, only part of the specified view window may be rendered if the clip region is not large enough.
     * For performance reasons, this method may ignore Layers that are invisible or that would be rendered entirely outside of the Graphics object's clip region. The attributes of the Graphics object are not restored to a known state between calls to the Layers' paint methods. The clip region may extend beyond the bounds of a Layer; it is the responsibility of the Layer to ensure that rendering operations are performed within its bounds.
     */
    public void paint(Graphics g, int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Removes the specified Layer from this LayerManager. This method does nothing if the specified Layer is not added to the this LayerManager.
     */
    public void remove(Layer l){
        return; //TODO codavaj!!
    }

    /**
     * Sets the view window on the LayerManager.
     * The view window specifies the region that the LayerManager draws when its paint(javax.microedition.lcdui.Graphics, int, int) method is called. It allows the developer to control the size of the visible region, as well as the location of the view window relative to the LayerManager's coordinate system.
     * The view window stays in effect until it is modified by another call to this method. By default, the view window is located at (0,0) in the LayerManager's coordinate system and its width and height are both set to Integer.MAX_VALUE.
     */
    public void setViewWindow(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

}
