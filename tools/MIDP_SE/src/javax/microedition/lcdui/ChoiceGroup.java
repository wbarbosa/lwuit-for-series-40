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
 * A ChoiceGroup is a group of selectable elements intended to be placed within a Form. The group may be created with a mode that requires a single choice to be made or that allows multiple choices. The implementation is responsible for providing the graphical representation of these modes and must provide visually different graphics for different modes. For example, it might use radio buttons for the single choice mode and check boxes for the multiple choice mode.
 * Note: most of the essential methods have been specified in the Choice interface.
 * Since: MIDP 1.0
 */
public class ChoiceGroup extends Item implements Choice{
    /**
     * Creates a new, empty ChoiceGroup, specifying its title and its type. The type must be one of EXCLUSIVE, MULTIPLE, or POPUP. The IMPLICIT choice type is not allowed within a ChoiceGroup.
     * label - the item's label (see
     * )choiceType - EXCLUSIVE, MULTIPLE, or POPUP
     * - if choiceType is not one of EXCLUSIVE, MULTIPLE, or POPUP
     * ,
     * ,
     * ,
     */
    public ChoiceGroup(java.lang.String label, int choiceType){
         //TODO codavaj!!
    }

    /**
     * Creates a new ChoiceGroup, specifying its title, the type of the ChoiceGroup, and an array of Strings and Images to be used as its initial contents.
     * The type must be one of EXCLUSIVE, MULTIPLE, or POPUP. The IMPLICIT type is not allowed for ChoiceGroup.
     * The stringElements array must be non-null and every array element must also be non-null. The length of the stringElements array determines the number of elements in the ChoiceGroup. The imageElements array may be null to indicate that the ChoiceGroup elements have no images. If the imageElements array is non-null, it must be the same length as the stringElements array. Individual elements of the imageElements array may be null in order to indicate the absence of an image for the corresponding ChoiceGroup element. Non-null elements of the imageElements array may refer to mutable or immutable images.
     * label - the item's label (see
     * )choiceType - EXCLUSIVE, MULTIPLE, or POPUPstringElements - set of strings specifying the string parts of the ChoiceGroup elementsimageElements - set of images specifying the image parts of the ChoiceGroup elements
     * - if stringElements is null
     * - if the stringElements array contains any null elements
     * - if the imageElements array is non-null and has a different length from the stringElements array
     * - if choiceType is not one of EXCLUSIVE, MULTIPLE, or POPUP
     * ,
     * ,
     * ,
     */
    public ChoiceGroup(java.lang.String label, int choiceType, java.lang.String[] stringElements, Image[] imageElements){
         //TODO codavaj!!
    }

    /**
     * Appends an element to the ChoiceGroup.
     */
    public int append(java.lang.String stringPart, Image imagePart){
        return 0; //TODO codavaj!!
    }

    /**
     * Deletes the element referenced by elementNum.
     */
    public void delete(int elementNum){
        return; //TODO codavaj!!
    }

    /**
     * Deletes all elements from this ChoiceGroup.
     */
    public void deleteAll(){
        return; //TODO codavaj!!
    }

    /**
     * Gets the application's preferred policy for fitting Choice element contents to the available screen space. The value returned is the policy that had been set by the application, even if that value had been disregarded by the implementation.
     */
    public int getFitPolicy(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the application's preferred font for rendering the specified element of this Choice. The value returned is the font that had been set by the application, even if that value had been disregarded by the implementation. If no font had been set by the application, or if the application explicitly set the font to null, the value is the default font chosen by the implementation.
     * The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public Font getFont(int elementNum){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the Image part of the element referenced by elementNum.
     */
    public Image getImage(int elementNum){
        return null; //TODO codavaj!!
    }

    /**
     * Queries the state of a ChoiceGroup and returns the state of all elements in the boolean array selectedArray_return. Note: this is a result parameter. It must be at least as long as the size of the ChoiceGroup as returned by size(). If the array is longer, the extra elements are set to false.
     * For ChoiceGroup objects of type MULTIPLE, any number of elements may be selected and set to true in the result array. For ChoiceGroup objects of type EXCLUSIVE and POPUP exactly one element will be selected, unless there are zero elements in the ChoiceGroup.
     */
    public int getSelectedFlags(boolean[] selectedArray_return){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the index number of an element in the ChoiceGroup that is selected. For ChoiceGroup objects of type EXCLUSIVE and POPUP there is at most one element selected, so this method is useful for determining the user's choice. Returns -1 if there are no elements in the ChoiceGroup.
     * For ChoiceGroup objects of type MULTIPLE, this always returns -1 because no single value can in general represent the state of such a ChoiceGroup. To get the complete state of a MULTIPLE Choice, see getSelectedFlags.
     */
    public int getSelectedIndex(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the String part of the element referenced by elementNum.
     */
    public java.lang.String getString(int elementNum){
        return null; //TODO codavaj!!
    }

    /**
     * Inserts an element into the ChoiceGroup just prior to the element specified.
     */
    public void insert(int elementNum, java.lang.String stringPart, Image imagePart){
        return; //TODO codavaj!!
    }

    /**
     * Gets a boolean value indicating whether this element is selected.
     */
    public boolean isSelected(int elementNum){
        return false; //TODO codavaj!!
    }

    /**
     * Sets the String and Image parts of the element referenced by elementNum, replacing the previous contents of the element.
     */
    public void set(int elementNum, java.lang.String stringPart, Image imagePart){
        return; //TODO codavaj!!
    }

    /**
     * Sets the application's preferred policy for fitting Choice element contents to the available screen space. The set policy applies for all elements of the Choice object. Valid values are
     * ,
     * , and
     * . Fit policy is a hint, and the implementation may disregard the application's preferred policy.
     */
    public void setFitPolicy(int fitPolicy){
        return; //TODO codavaj!!
    }

    /**
     * Sets the application's preferred font for rendering the specified element of this Choice. An element's font is a hint, and the implementation may disregard the application's preferred font.
     * The elementNum parameter must be within the range [0..size()-1], inclusive.
     * The font parameter must be a valid Font object or null. If the font parameter is null, the implementation must use its default font to render the element.
     */
    public void setFont(int elementNum, Font font){
        return; //TODO codavaj!!
    }

    /**
     * Attempts to set the selected state of every element in the ChoiceGroup. The array must be at least as long as the size of the ChoiceGroup. If the array is longer, the additional values are ignored.
     * For ChoiceGroup objects of type MULTIPLE, this sets the selected state of every element in the Choice. An arbitrary number of elements may be selected.
     * For ChoiceGroup objects of type EXCLUSIVE and POPUP, exactly one array element must have the value true. If no element is true, the first element in the Choice will be selected. If two or more elements are true, the implementation will choose the first true element and select it.
     */
    public void setSelectedFlags(boolean[] selectedArray){
        return; //TODO codavaj!!
    }

    /**
     * For ChoiceGroup objects of type MULTIPLE, this simply sets an individual element's selected state.
     * For ChoiceGroup objects of type EXCLUSIVE and POPUP, this can be used only to select an element. That is, the selected parameter must be true . When an element is selected, the previously selected element is deselected. If selected is false , this call is ignored.
     * For both list types, the elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public void setSelectedIndex(int elementNum, boolean selected){
        return; //TODO codavaj!!
    }

    /**
     * Returns the number of elements in the ChoiceGroup.
     */
    public int size(){
        return 0; //TODO codavaj!!
    }

}
