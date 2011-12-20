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
 * The TextBox class is a Screen that allows the user to enter and edit text.
 * A TextBox has a maximum size, which is the maximum number of characters that can be stored in the object at any time (its capacity). This limit is enforced when the TextBox instance is constructed, when the user is editing text within the TextBox, as well as when the application program calls methods on the TextBox that modify its contents. The maximum size is the maximum stored capacity and is unrelated to the number of characters that may be displayed at any given time. The number of characters displayed and their arrangement into rows and columns are determined by the device.
 * The implementation may place a boundary on the maximum size, and the maximum size actually assigned may be smaller than the application had requested. The value actually assigned will be reflected in the value returned by getMaxSize(). A defensively-written application should compare this value to the maximum size requested and be prepared to handle cases where they differ.
 * The text contained within a TextBox may be more than can be displayed at one time. If this is the case, the implementation will let the user scroll to view and edit any part of the text. This scrolling occurs transparently to the application.
 * If the constraints are set to TextField.ANY The text may contain line breaks. The display of the text must break accordingly and the user must be able to enter line break characters.
 * TextBox has the concept of input constraints that is identical to TextField. The constraints parameters of methods within the TextBox class use constants defined in the TextField class. See the description of input constraints in the TextField class for the definition of these constants. TextBox also has the same notions as TextField of the actual contents and the displayed contents, described in the same section.
 * TextBox also has the concept of input modes that is identical to TextField. See the description of input modes in the TextField class for more details.
 * Since: MIDP 1.0
 */
public class TextBox extends Screen{
    /**
     * Creates a new TextBox object with the given title string, initial contents, maximum size in characters, and constraints. If the text parameter is null, the TextBox is created empty. The maxSize parameter must be greater than zero. An IllegalArgumentException is thrown if the length of the initial contents string exceeds maxSize. However, the implementation may assign a maximum size smaller than the application had requested. If this occurs, and if the length of the contents exceeds the newly assigned maximum size, the contents are truncated from the end in order to fit, and no exception is thrown.
     * title - the title text to be shown with the displaytext - the initial contents of the text editing area, null may be used to indicate no initial contentmaxSize - the maximum capacity in characters. The implementation may limit boundary maximum capacity and the actually assigned capacity may me smaller than requested. A defensive application will test the actually given capacity with
     * .constraints - see
     * - if maxSize is zero or less
     * - if the constraints parameter is invalid
     * - if text is illegal for the specified constraints
     * - if the length of the string exceeds the requested maximum capacity
     */
    public TextBox(java.lang.String title, java.lang.String text, int maxSize, int constraints){
         //TODO codavaj!!
    }

    /**
     * Deletes characters from the TextBox.
     * The offset and length parameters must specify a valid range of characters within the contents of the TextBox. The offset parameter must be within the range [0..(size())], inclusive. The length parameter must be a non-negative integer such that (offset + length) = size().
     */
    public void delete(int offset, int length){
        return; //TODO codavaj!!
    }

    /**
     * Gets the current input position. For some UIs this may block and ask the user for the intended caret position, and on other UIs this may simply return the current caret position.
     */
    public int getCaretPosition(){
        return 0; //TODO codavaj!!
    }

    /**
     * Copies the contents of the TextBox into a character array starting at index zero. Array elements beyond the characters copied are left unchanged.
     */
    public int getChars(char[] data){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current input constraints of the TextBox.
     */
    public int getConstraints(){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the maximum size (number of characters) that can be stored in this TextBox.
     */
    public int getMaxSize(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the contents of the TextBox as a string value.
     */
    public java.lang.String getString(){
        return null; //TODO codavaj!!
    }

    /**
     * Inserts a subrange of an array of characters into the contents of the TextBox. The offset and length parameters indicate the subrange of the data array to be used for insertion. Behavior is otherwise identical to
     * .
     * The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = data.length.
     */
    public void insert(char[] data, int offset, int length, int position){
        return; //TODO codavaj!!
    }

    /**
     * Inserts a string into the contents of the TextBox. The string is inserted just prior to the character indicated by the position parameter, where zero specifies the first character of the contents of the TextBox. If position is less than or equal to zero, the insertion occurs at the beginning of the contents, thus effecting a prepend operation. If position is greater than or equal to the current size of the contents, the insertion occurs immediately after the end of the contents, thus effecting an append operation. For example, text.insert(s, text.size()) always appends the string s to the current contents.
     * The current size of the contents is increased by the number of inserted characters. The resulting string must fit within the current maximum capacity.
     * If the application needs to simulate typing of characters it can determining the location of the current insertion point (caret) using the with getCaretPosition() method. For example, text.insert(s, text.getCaretPosition()) inserts the string s at the current caret position.
     */
    public void insert(java.lang.String src, int position){
        return; //TODO codavaj!!
    }

    /**
     * Sets the contents of the TextBox from a character array, replacing the previous contents. Characters are copied from the region of the data array starting at array index offset and running for length characters. If the data array is null, the TextBox is set to be empty and the other parameters are ignored.
     * The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = data.length.
     */
    public void setChars(char[] data, int offset, int length){
        return; //TODO codavaj!!
    }

    /**
     * Sets the input constraints of the TextBox. If the current contents of the TextBox do not match the new constraints, the contents are set to empty.
     */
    public void setConstraints(int constraints){
        return; //TODO codavaj!!
    }

    /**
     * Sets a hint to the implementation as to the input mode that should be used when the user initiates editing of this TextBox. The characterSubset parameter names a subset of Unicode characters that is used by the implementation to choose an initial input mode. If null is passed, the implementation should choose a default input mode.
     * See Input Modes for a full explanation of input modes.
     */
    public void setInitialInputMode(java.lang.String characterSubset){
        return; //TODO codavaj!!
    }

    /**
     * Sets the maximum size (number of characters) that can be contained in this TextBox. If the current contents of the TextBox are larger than maxSize, the contents are truncated to fit.
     */
    public int setMaxSize(int maxSize){
        return 0; //TODO codavaj!!
    }

    /**
     * Sets the contents of the TextBox as a string value, replacing the previous contents.
     */
    public void setString(java.lang.String text){
        return; //TODO codavaj!!
    }

    /**
     * Sets a ticker for use with this Displayable, replacing any previous ticker. If null, removes the ticker object from this Displayable. The same ticker may be shared by several Displayable objects within an application. This is done by calling setTicker() with the same Ticker object on several different Displayable objects. If the Displayable is actually visible on the display, the implementation should update the display as soon as it is feasible to do so.
     * The existence of a ticker may affect the size of the area available for Displayable's contents. If the application adds, removes, or sets the ticker text at runtime, this can dynamically change the size of the content area. This is most important to be aware of when using the Canvas class.
     */
    public void setTicker(Ticker ticker){
        return; //TODO codavaj!!
    }

    /**
     * Sets the title of the Displayable. If null is given, removes the title.
     * If the Displayable is actually visible on the display, the implementation should update the display as soon as it is feasible to do so.
     * The existence of a title may affect the size of the area available for Displayable content. If the application adds, removes, or sets the title text at runtime, this can dynamically change the size of the content area. This is most important to be aware of when using the Canvas class.
     */
    public void setTitle(java.lang.String s){
        return; //TODO codavaj!!
    }

    /**
     * Gets the number of characters that are currently stored in this TextBox.
     */
    public int size(){
        return 0; //TODO codavaj!!
    }

}
