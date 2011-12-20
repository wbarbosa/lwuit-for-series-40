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

package javax.microedition.lcdui;
/**
 * A blank, non-interactive item that has a settable minimum size. The minimum width is useful for allocating flexible amounts of space between Items within the same row of a Form. The minimum height is useful for enforcing a particular minimum height of a row. The application can set the minimum width or height to any non-negative value. The implementation may enforce implementation-defined maximum values for the minimum width and height.
 * The unlocked preferred width of a Spacer is the same as its current minimum width. Its unlocked preferred height is the same as its current minimum height.
 * Since a Spacer's primary purpose is to position other items, it is restricted to be non-interactive, and the application is not allowed to add Commands to a Spacer. Since the presence of a label on an Item may affect layout in device-specific ways, the label of a Spacer is restricted to always be null, and the application is not allowed to change it.
 * Since: MIDP 2.0
 */
public class Spacer extends Item{
    /**
     * Creates a new Spacer with the given minimum size. The Spacer's label is null. The minimum size must be zero or greater. If minWidth is greater than the implementation-defined maximum width, the maximum width will be used instead. If minHeight is greater than the implementation-defined maximum height, the maximum height will be used instead.
     * minWidth - the minimum width in pixelsminHeight - the minimum height in pixels
     * - if either minWidth or minHeight is less than zero
     */
    public Spacer(int minWidth, int minHeight){
         //TODO codavaj!!
    }

    /**
     * Spacers are restricted from having Commands, so this method will always throw IllegalStateException whenever it is called.
     */
    public void addCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Spacers are restricted from having Commands, so this method will always throw IllegalStateException whenever it is called.
     */
    public void setDefaultCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Spacers are restricted to having null labels, so this method will always throw IllegalStateException whenever it is called.
     */
    public void setLabel(java.lang.String label){
        return; //TODO codavaj!!
    }

    /**
     * Sets the minimum size for this spacer. The Form will not be allowed to make the item smaller than this size. The minimum size must be zero or greater. If minWidth is greater than the implementation-defined maximum width, the maximum width will be used instead. If minHeight is greater than the implementation-defined maximum height, the maximum height will be used instead.
     */
    public void setMinimumSize(int minWidth, int minHeight){
        return; //TODO codavaj!!
    }

}
