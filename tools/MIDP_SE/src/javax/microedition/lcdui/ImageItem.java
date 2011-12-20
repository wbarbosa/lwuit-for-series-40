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
 * An item that can contain an image.
 * Each ImageItem object contains a reference to an Image object. This Image may be mutable or immutable. If the Image is mutable, the effect is as if snapshot of its contents is taken at the time the ImageItem is constructed with this Image and when setImage is called with an Image. The snapshot is used whenever the contents of the ImageItem are to be displayed. Even if the application subsequently draws into the Image, the snapshot is not modified until the next call to setImage. The snapshot is not updated when the container of the ImageItem becomes current or becomes visible on the display. (This is because the application does not have control over exactly when Displayables and Items appear and disappear from the display.)
 * The value null may be specified for the image contents of an ImageItem. If this occurs (and if the label is also null) the ImageItem will occupy no space on the screen.
 * ImageItem contains layout directives that were originally defined in MIDP 1.0. These layout directives have been moved to the Item class and now apply to all items. The declarations are left in ImageItem for source compatibility purposes.
 * The altText parameter specifies a string to be displayed in place of the image if the image exceeds the capacity of the display. The altText parameter may be null.
 * Since: MIDP 1.0
 */
public class ImageItem extends Item{
    /**
     * See
     * .
     * Value 3 is assigned to LAYOUT_CENTER.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_CENTER=3;

    /**
     * See
     * .
     * Value 0 is assigned to LAYOUT_DEFAULT.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_DEFAULT=0;

    /**
     * See
     * .
     * Value 1 is assigned to LAYOUT_LEFT.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_LEFT=1;

    /**
     * See
     * .
     * Value 0x200 is assigned to LAYOUT_NEWLINE_AFTER.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_NEWLINE_AFTER=512;

    /**
     * See
     * .
     * Value 0x100 is assigned to LAYOUT_NEWLINE_BEFORE.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_NEWLINE_BEFORE=256;

    /**
     * See
     * .
     * Value 2 is assigned to LAYOUT_RIGHT.
     * See Also:Constant Field Values
     */
    public static final int LAYOUT_RIGHT=2;

    /**
     * Creates a new ImageItem with the given label, image, layout directive, and alternate text string. Calling this constructor is equivalent to calling
     * label - the label stringimg - the image, can be mutable or immutablelayout - a combination of layout directivesaltText - the text that may be used in place of the image
     * - if the layout value is not a legal combination of directives
     */
    public ImageItem(java.lang.String label, Image img, int layout, java.lang.String altText){
         //TODO codavaj!!
    }

    /**
     * Creates a new ImageItem object with the given label, image, layout directive, alternate text string, and appearance mode. Either label or alternative text may be present or null.
     * The appearanceMode parameter (see Appearance Modes) is a hint to the platform of the application's intended use for this ImageItem. To provide hyperlink- or button-like behavior, the application should associate a default Command with this ImageItem and add an ItemCommandListener to this ImageItem.
     * Here is an example showing the use of an ImageItem as a button:
     * ImageItem imgItem = new ImageItem("Default: ", img, Item.LAYOUT_CENTER, null, Item.BUTTON); imgItem.setDefaultCommand( new Command("Set", Command.ITEM, 1); // icl is ItemCommandListener imgItem.setItemCommandListener(icl);
     * label - the label stringimage - the image, can be mutable or immutablelayout - a combination of layout directivesaltText - the text that may be used in place of the imageappearanceMode - the appearance mode of the ImageItem, one of
     * ,
     * , or
     * - if the layout value is not a legal combination of directives
     * - if appearanceMode invalid
     * MIDP 2.0
     */
    public ImageItem(java.lang.String label, Image image, int layout, java.lang.String altText, int appearanceMode){
         //TODO codavaj!!
    }

    /**
     * Gets the text string to be used if the image exceeds the device's capacity to display it.
     */
    public java.lang.String getAltText(){
        return null; //TODO codavaj!!
    }

    /**
     * Returns the appearance mode of the ImageItem. See
     * .
     */
    public int getAppearanceMode(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the image contained within the ImageItem, or null if there is no contained image.
     */
    public Image getImage(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the layout directives used for placing the image.
     */
    public int getLayout(){
        return 0; //TODO codavaj!!
    }

    /**
     * Sets the alternate text of the ImageItem, or null if no alternate text is provided.
     */
    public void setAltText(java.lang.String text){
        return; //TODO codavaj!!
    }

    /**
     * Sets the Image object contained within the ImageItem. The image may be mutable or immutable. If img is null, the ImageItem is set to be empty. If img is mutable, the effect is as if a snapshot is taken of img's contents immediately prior to the call to setImage. This snapshot is used whenever the contents of the ImageItem are to be displayed. If img is already the Image of this ImageItem, the effect is as if a new snapshot of img's contents is taken. Thus, after painting into a mutable image contained by an ImageItem, the application can call
     * to refresh the ImageItem's snapshot of its Image.
     * If the ImageItem is visible on the display when the snapshot is updated through a call to setImage, the display is updated with the new snapshot as soon as it is feasible for the implementation to so do.
     */
    public void setImage(Image img){
        return; //TODO codavaj!!
    }

    /**
     * Sets the layout directives.
     */
    public void setLayout(int layout){
        return; //TODO codavaj!!
    }

}
