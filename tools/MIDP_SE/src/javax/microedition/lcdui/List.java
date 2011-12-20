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
 * A Screen containing list of choices. Most of its behavior is common with class ChoiceGroup, and their common API. The different List types in particular, are defined in interface Choice. When a List is present on the display, the user can interact with it by selecting elements and possibly by traversing and scrolling among them. Traversing and scrolling operations do not cause application-visible events. The system notifies the application only when a Command is invoked by notifying its CommandListener. The List class also supports a select command that may be invoked specially depending upon the capabilities of the device.
 * The notion of a select operation on a List element is central to the user's interaction with the List. On devices that have a dedicated hardware select or go key, the select operation is implemented with that key. Devices that do not have a dedicated key must provide another means to do the select operation, for example, using a soft key. The behavior of the select operation within the different types of lists is described in the following sections.
 * List objects may be created with Choice types of Choice.EXCLUSIVE, Choice.MULTIPLE, and Choice.IMPLICIT. The Choice type Choice.POPUP is not allowed on List objects.
 * The select operation is not associated with a Command object, so the application has no means of setting a label for it or being notified when the operation is performed. In Lists of type EXCLUSIVE, the select operation selects the target element and deselects the previously selected element. In Lists of type MULTIPLE, the select operation toggles the selected state of the target element, leaving the selected state of other elements unchanged. Devices that implement the select operation using a soft key will need to provide a label for it. The label should be something similar to Select for Lists of type EXCLUSIVE, and it should be something similar to Mark or Unmark for Lists of type MULTIPLE.
 * The select operation is associated with a Command object referred to as the select command. When the user performs the select operation, the system will invoke the select command by notifying the List's CommandListener. The default select command is the system-provided command SELECT_COMMAND. The select command may be modified by the application through use of the setSelectCommand method. Devices that implement the select operation using a soft key will use the label from the select command. If the select command is SELECT_COMMAND, the device may choose to provide its own label instead of using the label attribute of SELECT_COMMAND. Applications should generally provide their own select command to replace SELECT_COMMAND. This allows applications to provide a meaningful label, instead of relying on the one provided by the system for SELECT_COMMAND. The implementation must not invoke the select command if there are no elements in the List, because if the List is empty the selection does not exist. In this case the implementation should remove or disable the select command if it would appear explicitly on a soft button or in a menu. Other commands can be invoked normally when the List is empty.
 * IMPLICIT Lists can be used to construct menus by providing operations as List elements. The application provides a Command that is used to select a List element and then defines this Command to be used as the select command. The application must also register a CommandListener that is called when the user selects or activates the Command:
 * The listener can query the List to determine which element is selected and then perform the corresponding action. Note that setting a command as the select command adds it to the List as a side effect.
 * The select command should be considered as a default operation that takes place when a select key is pressed. For example, a List displaying email headers might have three operations: read, reply, and delete. Read is considered to be the default operation.
 * On a device with a dedicated select key, pressing this key will invoke readCommand. On a device without a select key, the user is still able to invoke the read command, since it is also provided as an ordinary Command.
 * It should be noted that this kind of default operation must be used carefully, and the usability of the resulting user interface must always kept in mind. The default operation should always be the most intuitive operation on a particular List.
 * Since: MIDP 1.0
 */
public class List extends Screen implements Choice{
    /**
     * The default select command for IMPLICIT Lists. Applications using an IMPLICIT List should set their own select command using
     * .
     * The field values of SELECT_COMMAND are: - label = (an empty string) - type = SCREEN - priority = 0
     * (It would be more appropriate if the type were ITEM, but the type of SCREEN is retained for historical purposes.)
     * The application should not use these values for recognizing the SELECT_COMMAND. Instead, object identities of the Command and Displayable (List) should be used.
     * SELECT_COMMAND is treated as an ordinary Command if it is used with other Displayable types.
     */
    public static final Command SELECT_COMMAND=null;

    /**
     * Creates a new, empty List, specifying its title and the type of the list.
     * title - the screen's title (see
     * )listType - one of IMPLICIT, EXCLUSIVE, or MULTIPLE
     * - if listType is not one of IMPLICIT, EXCLUSIVE, or MULTIPLE
     */
    public List(java.lang.String title, int listType){
         //TODO codavaj!!
    }

    /**
     * Creates a new List, specifying its title, the type of the List, and an array of Strings and Images to be used as its initial contents.
     * The stringElements array must be non-null and every array element must also be non-null. The length of the stringElements array determines the number of elements in the List. The imageElements array may be null to indicate that the List elements have no images. If the imageElements array is non-null, it must be the same length as the stringElements array. Individual elements of the imageElements array may be null in order to indicate the absence of an image for the corresponding List element. Non-null elements of the imageElements array may refer to mutable or immutable images.
     * title - the screen's title (see
     * )listType - one of IMPLICIT, EXCLUSIVE, or MULTIPLEstringElements - set of strings specifying the string parts of the List elementsimageElements - set of images specifying the image parts of the List elements
     * - if stringElements is null
     * - if the stringElements array contains any null elements
     * - if the imageElements array is non-null and has a different length from the stringElements array
     * - if listType is not one of IMPLICIT, EXCLUSIVE, or MULTIPLE
     * ,
     * ,
     */
    public List(java.lang.String title, int listType, java.lang.String[] stringElements, Image[] imageElements){
         //TODO codavaj!!
    }

    /**
     * Appends an element to the List.
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
     * Deletes all elements from this List.
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
     * Queries the state of a List and returns the state of all elements in the boolean array selectedArray_return.
     */
    public int getSelectedFlags(boolean[] selectedArray_return){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the index number of an element in the List that is selected.
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
     * Inserts an element into the List just prior to the element specified.
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
     * The same as
     * but with the following additional semantics.
     * If the command to be removed happens to be the select command, the List is set to have no select command, and the command is removed from the List.
     * The following code:
     * is equivalent to the following code:
     */
    public void removeCommand(Command cmd){
        return; //TODO codavaj!!
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
     * Sets the Command to be used for an IMPLICIT List selection action. By default, an implicit selection of a List will result in the predefined List.SELECT_COMMAND being used. This behavior may be overridden by calling the List.setSelectCommand() method with an appropriate parameter value. If a null reference is passed, this indicates that no
     * select
     * action is appropriate for the contents of this List.
     * If a reference to a command object is passed, and it is not the special command List.SELECT_COMMAND, and it is not currently present on this List object, the command object is added to this List as if addCommand(command) had been called prior to the command being made the select command. This indicates that this command is to be invoked when the user performs the select on an element of this List.
     * The select command should have a command type of ITEM to indicate that it operates on the currently selected object. It is not an error if the command is of some other type. (List.SELECT_COMMAND has a type of SCREEN for historical purposes.) For purposes of presentation and placement within its user interface, the implementation is allowed to treat the select command as if it were of type ITEM.
     * If the select command is later removed from the List with removeCommand(), the List is set to have no select command as if List.setSelectCommand(null) had been called.
     * The default behavior can be reestablished explicitly by calling setSelectCommand() with an argument of List.SELECT_COMMAND.
     * This method has no effect if the type of the List is not IMPLICIT.
     */
    public void setSelectCommand(Command command){
        return; //TODO codavaj!!
    }

    /**
     * Sets the selected state of all elements of the List.
     */
    public void setSelectedFlags(boolean[] selectedArray){
        return; //TODO codavaj!!
    }

    /**
     * Sets the selected state of an element.
     */
    public void setSelectedIndex(int elementNum, boolean selected){
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
     * Gets the number of elements in the List.
     */
    public int size(){
        return 0; //TODO codavaj!!
    }

}
