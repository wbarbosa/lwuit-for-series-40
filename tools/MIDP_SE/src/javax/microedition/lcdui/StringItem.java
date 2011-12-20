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
 * An item that can contain a string. A StringItem is display-only; the user cannot edit the contents. Both the label and the textual content of a StringItem may be modified by the application. The visual representation of the label may differ from that of the textual contents.
 */
public class StringItem extends Item{
    /**
     * Creates a new StringItem object. Calling this constructor is equivalent to calling
     * label - the Item labeltext - the text contents
     */
    public StringItem(java.lang.String label, java.lang.String text){
         //TODO codavaj!!
    }

    /**
     * Creates a new StringItem object with the given label, textual content, and appearance mode. Either label or text may be present or null.
     * The appearanceMode parameter (see Appearance Modes) is a hint to the platform of the application's intended use for this StringItem. To provide hyperlink- or button-like behavior, the application should associate a default Command with this StringItem and add an ItemCommandListener to this StringItem.
     * Here is an example showing the use of a StringItem as a button:
     * label - the StringItem's label, or null if no labeltext - the StringItem's text contents, or null if the contents are initially emptyappearanceMode - the appearance mode of the StringItem, one of
     * ,
     * , or
     * - if appearanceMode invalid
     * MIDP 2.0
     */
    public StringItem(java.lang.String label, java.lang.String text, int appearanceMode){
         //TODO codavaj!!
    }

    /**
     * Returns the appearance mode of the StringItem. See
     * .
     */
    public int getAppearanceMode(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the application's preferred font for rendering this StringItem. The value returned is the font that had been set by the application, even if that value had been disregarded by the implementation. If no font had been set by the application, or if the application explicitly set the font to null, the value is the default font chosen by the implementation.
     */
    public Font getFont(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the text contents of the StringItem, or null if the StringItem is empty.
     */
    public java.lang.String getText(){
        return null; //TODO codavaj!!
    }

    /**
     * Sets the application's preferred font for rendering this StringItem. The font is a hint, and the implementation may disregard the application's preferred font.
     * The font parameter must be a valid Font object or null. If the font parameter is null, the implementation must use its default font to render the StringItem.
     */
    public void setFont(Font font){
        return; //TODO codavaj!!
    }

    /**
     * Sets the preferred width and height for this Item. Values for width and height less than -1 are illegal. If the width is between zero and the minimum width, inclusive, the minimum width is used instead. If the height is between zero and the minimum height, inclusive, the minimum height is used instead.
     * Supplying a width or height value greater than the minimum width or height locks that dimension to the supplied value. The implementation may silently enforce a maximum dimension for an Item based on factors such as the screen size. Supplying a value of -1 for the width or height unlocks that dimension. See Item Sizes for a complete discussion.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setPreferredSize(int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Sets the text contents of the StringItem. If text is null, the StringItem is set to be empty.
     */
    public void setText(java.lang.String text){
        return; //TODO codavaj!!
    }

}
