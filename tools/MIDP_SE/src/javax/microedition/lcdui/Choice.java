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
 * Choice defines an API for a user interface components implementing selection from predefined number of choices. Such UI components are List and ChoiceGroup. The contents of the Choice are represented with strings and images.
 * Each element of a Choice is composed of a text string part, an Image part, and a font attribute that are all treated as a unit. The font attribute applies to the text part and can be controlled by the application. The application may provide null for the image if the element is not to have an image part. The implementation must display the image at the beginning of the text string. If the Choice also has a selection indicator (such as a radio button or a checkbox) placed at the beginning of the text string, the element's image should be placed between the selection indicator and the beginning of the text string.
 * When a new element is inserted or appended, the implementation provides a default font for the font attribute. This default font is the same font that is used if the application calls setFont(i, null). All ChoiceGroup instances must have the same default font, and all List instances must have the same default font. However, the default font used for Choice objects may differ from the font returned by Font.getDefaultFont.
 * The Image part of a Choice element may be mutable or immutable. If the Image is mutable, the effect is as if snapshot of its contents is taken at the time the Choice is constructed with this Image or when the Choice element is created or modified with the append, insert, or set methods. The snapshot is used whenever the contents of the Choice element are to be displayed. Even if the application subsequently draws into the Image, the snapshot is not modified until the next call to one of the above methods. The snapshot is not updated when the Choice becomes visible on the display. (This is because the application does not have control over exactly when Displayables and Items appear and disappear from the display.)
 * The following code illustrates a technique to refresh the image part of element k of a Choice ch:
 * If the application provides an image, the implementation may choose to truncate it if it exceeds the capacity of the device to display it. Images within any particular Choice object should all be of the same size, because the implementation is allowed to allocate the same amount of space for every element. The application can query the implementation's image size recommendation by calling Display.getBestImageWidth(int) and Display.getBestImageHeight(int).
 * If an element is very long or contains a line break, the implementation may display only a portion of it. If this occurs, the implementation should provide the user with a means to see as much as possible of the element. If this is done by wrapping an element to multiple lines, the second and subsequent lines should show a clear indication to the user that they are part of the same element and are not a new element.
 * The application can express a preference for the policy used by the implementation for display of long elements including those that contain line break characters. The characters after the first line break may only be visible if the policy permits it. The setFitPolicy(int) and getFitPolicy() methods control this preference. The valid settings are TEXT_WRAP_DEFAULT, TEXT_WRAP_ON, and TEXT_WRAP_OFF. Unless specified otherwise by Choice implementation classes, the initial value of the element fit policy is TEXT_WRAP_DEFAULT.
 * After a Choice object has been created, elements may be inserted, appended, and deleted, and each element's string part and image part may be get and set. Elements within a Choice object are referred to by their indexes, which are consecutive integers in the range from zero to size()-1, with zero referring to the first element and size()-1 to the last element.
 * There are four types of Choices: implicit-choice (valid only for List), exclusive-choice, multiple-choice, and pop-up (valid only for ChoiceGroup).
 * The exclusive-choice presents a series of elements and interacts with the user. That is, when the user selects an element, that element is shown to be selected using a distinct visual representation. If there are elements present in the Choice, one element must be selected at any given time. If at any time a situation would result where there are elements in the exclusive-choice but none is selected, the implementation will choose an element and select it. This situation can arise when an element is added to an empty Choice, when the selected element is deleted from the Choice, or when a Choice is created and populated with elements by a constructor. In these cases, the choice of which element is selected is left to the implementation. Applications for which the selected element is significant should set the selection explicitly. There is no way for the user to unselect an element within an exclusive Choice.
 * The popup choice is similar to the exclusive choice. The selection behavior of a popup choice is identical to that of an exclusive choice. However, a popup choice differs from an exclusive choice in presentation and interaction. In an exclusive choice, all elements should be displayed in-line. In a popup choice, the selected element should always be displayed, and the other elements should remain hidden until the user performs a specific action to show them. For example, an exclusive choice could be implemented as a series of radio buttons with one always selected. A popup choice could be implemented as a popup menu, with the selected element being displayed in the menu button.
 * The implicit choice is an exclusive choice where the focused or highlighted element is implicitly selected when a command is initiated. As with the exclusive choice, if there are elements present in the Choice, one element is always selected.
 * A multiple-choice presents a series of elements and allows the user to select any number of elements in any combination. As with exclusive-choice, the multiple-choice interacts with the user in object-operation mode. The visual appearance of a multiple-choice will likely have a visual representation distinct from the exclusive-choice that shows the selected state of each element as well as indicating to the user that multiple elements may be selected.
 * The selected state of an element is a property of the element. This state stays with that element if other elements are inserted or deleted, causing elements to be shifted around. For example, suppose element n is selected, and a new element is inserted at index zero. The selected element would now have index n+1. A similar rule applies to deletion. Assuming n is greater than zero, deleting element zero would leave element n-1 selected. Setting the contents of an element leaves its selected state unchanged. When a new element is inserted or appended, it is always unselected (except in the special case of adding an element to an empty Exclusive, Popup, or Implicit Choice as mentioned above).
 * The selected state of a Choice object can be controlled by the application with the setSelectedFlags and setSelectedIndex methods. This state is available to the application through the getSelectedFlags and getSelectedIndex methods. The selected state reported by these methods is generally identical to what has been set by the application, with the following exceptions. Adding or removing elements may change the selection. When the Choice is present on the display, the implementation's user interface policy and direct user interaction with the object may also affect the selection. For example, the implementation might update the selection to the current highlight location as the user is moving the highlight, or it might set the selection from the highlight only when the user is about to invoke a command. As another example, the implementation might move the highlight (and thus the selection) of an implicit List to the first element each time the List becomes current. When a Choice object is present on the display, applications should query its selected state only within a CommandListener or a ItemStateListener callback. Querying the state at other times might result in a value different from what has been set by the application (because the user or the implementation's UI policy might have changed it) and it might not reflect the user's intent (because the user might still in the process of making a selection).
 * Note: Methods have been added to the Choice interface in version 2.0. Adding methods to interfaces is normally an incompatible change. However, Choice does not appear as a type in any field, method parameter, or method return value, and so it is not useful for an application to create a class that implements the Choice interface. Future versions of this specification may make additional changes to the Choice interface. In order to remain compatible with future versions of this specification, applications should avoid creating classes that implement the Choice interface.
 * Since: MIDP 1.0
 */
public interface Choice{
    /**
     * EXCLUSIVE is a choice having exactly one element selected at time. All elements of an EXCLUSIVE type Choice should be displayed in-line. That is, the user should not need to perform any extra action to traverse among and select from the elements.
     * Value 1 is assigned to EXCLUSIVE.
     * See Also:Constant Field Values
     */
    public static final int EXCLUSIVE=1;

    /**
     * IMPLICIT is a choice in which the currently focused element is selected when a
     * is initiated.
     * The IMPLICIT type is not valid for ChoiceGroup objects.
     * Value 3 is assigned to IMPLICIT.
     * See Also:Constant Field Values
     */
    public static final int IMPLICIT=3;

    /**
     * MULTIPLE is a choice that can have arbitrary number of elements selected at a time.
     * Value 2 is assigned to MULTIPLE.
     * See Also:Constant Field Values
     */
    public static final int MULTIPLE=2;

    /**
     * POPUP is a choice having exactly one element selected at a time. The selected element is always shown. The other elements should be hidden until the user performs a particular action to show them. When the user performs this action, all elements become accessible. For example, an implementation could use a popup menu to display the elements of a ChoiceGroup of type POPUP.
     * The POPUP type is not valid for List objects.
     * Value 4 is assigned to POPUP.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int POPUP=4;

    /**
     * Constant for indicating that the application has no preference as to wrapping or truncation of text element contents and that the implementation should use its default behavior.
     * Field has the value 0.
     * Since: MIDP 2.0 See Also:getFitPolicy(), setFitPolicy(int), Constant Field Values
     */
    public static final int TEXT_WRAP_DEFAULT=0;

    /**
     * Constant for hinting that text element contents should be limited to a single line. Line ending is forced, for example by cropping, if there is too much text to fit to the line. The implementation should provide some means to present the full element contents. This may be done, for example, by using a special pop-up window or by scrolling the text of the focused element.
     * Implementations should indicate that cropping has occurred, for example, by placing an ellipsis at the point where the text contents have been cropped.
     * Field has the value 2.
     * Since: MIDP 2.0 See Also:getFitPolicy(), setFitPolicy(int), Constant Field Values
     */
    public static final int TEXT_WRAP_OFF=2;

    /**
     * Constant for hinting that text element contents should be wrapped to to multiple lines if necessary to fit available content space. The Implementation may limit the maximum number of lines that it will actually present.
     * Field has the value 1.
     * Since: MIDP 2.0 See Also:getFitPolicy(), setFitPolicy(int), Constant Field Values
     */
    public static final int TEXT_WRAP_ON=1;

    /**
     * Appends an element to the Choice. The added element will be the last element of the Choice. The size of the Choice grows by one.
     */
    public abstract int append(java.lang.String stringPart, Image imagePart);

    /**
     * Deletes the element referenced by elementNum. The size of the Choice shrinks by one. It is legal to delete all elements from a Choice. The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract void delete(int elementNum);

    /**
     * Deletes all elements from this Choice, leaving it with zero elements. This method does nothing if the Choice is already empty.
     */
    public abstract void deleteAll();

    /**
     * Gets the application's preferred policy for fitting Choice element contents to the available screen space. The value returned is the policy that had been set by the application, even if that value had been disregarded by the implementation.
     */
    public abstract int getFitPolicy();

    /**
     * Gets the application's preferred font for rendering the specified element of this Choice. The value returned is the font that had been set by the application, even if that value had been disregarded by the implementation. If no font had been set by the application, or if the application explicitly set the font to null, the value is the default font chosen by the implementation.
     * The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract Font getFont(int elementNum);

    /**
     * Gets the Image part of the element referenced by elementNum. The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract Image getImage(int elementNum);

    /**
     * Queries the state of a Choice and returns the state of all elements in the boolean array selectedArray_return. Note: this is a result parameter. It must be at least as long as the size of the Choice as returned by size(). If the array is longer, the extra elements are set to false.
     * This call is valid for all types of Choices. For MULTIPLE, any number of elements may be selected and set to true in the result array. For EXCLUSIVE, POPUP, and IMPLICIT exactly one element will be selected (unless there are zero elements in the Choice).
     */
    public abstract int getSelectedFlags(boolean[] selectedArray_return);

    /**
     * Returns the index number of an element in the Choice that is selected. For Choice types EXCLUSIVE, POPUP, and IMPLICIT there is at most one element selected, so this method is useful for determining the user's choice. Returns -1 if the Choice has no elements (and therefore has no selected elements).
     * For MULTIPLE, this always returns -1 because no single value can in general represent the state of such a Choice. To get the complete state of a MULTIPLE Choice, see getSelectedFlags.
     */
    public abstract int getSelectedIndex();

    /**
     * Gets the String part of the element referenced by elementNum. The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract java.lang.String getString(int elementNum);

    /**
     * Inserts an element into the Choice just prior to the element specified. The size of the Choice grows by one. The elementNum parameter must be within the range [0..size()], inclusive. The index of the last element is size()-1, and so there is actually no element whose index is size(). If this value is used for elementNum, the new element is inserted immediately after the last element. In this case, the effect is identical to
     * .
     */
    public abstract void insert(int elementNum, java.lang.String stringPart, Image imagePart);

    /**
     * Gets a boolean value indicating whether this element is selected. The elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract boolean isSelected(int elementNum);

    /**
     * Sets the String and Image parts of the element referenced by elementNum, replacing the previous contents of the element. The elementNum parameter must be within the range [0..size()-1], inclusive. The font attribute of the element is left unchanged.
     */
    public abstract void set(int elementNum, java.lang.String stringPart, Image imagePart);

    /**
     * Sets the application's preferred policy for fitting Choice element contents to the available screen space. The set policy applies for all elements of the Choice object. Valid values are
     * ,
     * , and
     * . Fit policy is a hint, and the implementation may disregard the application's preferred policy.
     */
    public abstract void setFitPolicy(int fitPolicy);

    /**
     * Sets the application's preferred font for rendering the specified element of this Choice. An element's font is a hint, and the implementation may disregard the application's preferred font.
     * The elementNum parameter must be within the range [0..size()-1], inclusive.
     * The font parameter must be a valid Font object or null. If the font parameter is null, the implementation must use its default font to render the element.
     */
    public abstract void setFont(int elementNum, Font font);

    /**
     * Attempts to set the selected state of every element in the Choice. The array must be at least as long as the size of the Choice. If the array is longer, the additional values are ignored.
     * For Choice objects of type MULTIPLE, this sets the selected state of every element in the Choice. An arbitrary number of elements may be selected.
     * For Choice objects of type EXCLUSIVE, POPUP, and IMPLICIT, exactly one array element must have the value true. If no element is true, the first element in the Choice will be selected. If two or more elements are true, the implementation will choose the first true element and select it.
     */
    public abstract void setSelectedFlags(boolean[] selectedArray);

    /**
     * For MULTIPLE, this simply sets an individual element's selected state.
     * For EXCLUSIVE and POPUP, this can be used only to select any element, that is, the selected parameter must be true . When an element is selected, the previously selected element is deselected. If selected is false , this call is ignored. If element was already selected, the call has no effect.
     * For IMPLICIT, this can be used only to select any element, that is, the selected parameter must be true . When an element is selected, the previously selected element is deselected. If selected is false , this call is ignored. If element was already selected, the call has no effect.
     * The call to setSelectedIndex does not cause implicit activation of any Command.
     * For all list types, the elementNum parameter must be within the range [0..size()-1], inclusive.
     */
    public abstract void setSelectedIndex(int elementNum, boolean selected);

    /**
     * Gets the number of elements present.
     */
    public abstract int size();

}
