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
 * A superclass for components that can be added to a Form. All Item objects have a label field, which is a string that is attached to the item. The label is typically displayed near the component when it is displayed within a screen. The label should be positioned on the same horizontal row as the item or directly above the item. The implementation should attempt to distinguish label strings from other textual content, possibly by displaying the label in a different font, aligning it to a different margin, or appending a colon to it if it is placed on the same line as other string content. If the screen is scrolling, the implementation should try to keep the label visible at the same time as the Item.
 * In some cases, when the user attempts to interact with an Item, the system will switch to a system-generated screen where the actual interaction takes place. If this occurs, the label will generally be carried along and displayed within this new screen in order to provide the user with some context for the operation. For this reason it is recommended that applications supply a label to all interactive Item objects. However, this is not required, and a null value for a label is legal and specifies the absence of a label.
 * An Item's layout within its container is influenced through layout directives:
 * The LAYOUT_DEFAULT directive indicates that the container's default layout policy is to be used for this item. LAYOUT_DEFAULT has the value zero and has no effect when combined with other layout directives. It is useful within programs in order to document the programmer's intent.
 * The LAYOUT_LEFT, LAYOUT_RIGHT, and LAYOUT_CENTER directives indicate horizontal alignment and are mutually exclusive. Similarly, the LAYOUT_TOP, LAYOUT_BOTTOM, and LAYOUT_VCENTER directives indicate vertical alignment and are mutually exclusive.
 * A horizontal alignment directive, a vertical alignment directive, and any combination of other layout directives may be combined using the bit-wise OR operator (|) to compose a layout directive value. Such a value is used as the parameter to the setLayout(int) method and is the return value from the getLayout() method.
 * Some directives have no defined behavior in some contexts. A layout directive is ignored if its behavior is not defined for the particular context within which the Item resides.
 * A complete specification of the layout of Items within a Form is given here.
 * Items have two explicit size concepts: the minimum size and the preferred size. Both the minimum and the preferred sizes refer to the total area of the Item, which includes space for the Item's contents, the Item's label, as well as other space that is significant to the layout policy. These sizes do not include space that is not significant for layout purposes. For example, if the addition of a label to an Item would cause other Items to move in order to make room, then the space occupied by this label is significant to layout and is counted as part of the Item's minimum and preferred sizes. However, if an implementation were to place the label in a margin area reserved exclusively for labels, this would not affect the layout of neighboring Items. In this case, the space occupied by the label would not be considered part of the minimum and preferred sizes.
 * The minimum size is the smallest size at which the Item can function and display its contents, though perhaps not optimally. The minimum size may be recomputed whenever the Item's contents changes.
 * The preferred size is generally a size based on the Item's contents and is the smallest size at which no information is clipped and text wrapping (if any) is kept to a tolerable minimum. The preferred size may be recomputed whenever the Item's contents changes. The application can lock the preferred width or preferred height (or both) by supplying specific values for parameters to the setPreferredSize method. The manner in which an Item fits its contents within an application-specified preferred size is implementation-specific. However, it is recommended that textual content be word-wrapped to fit the preferred size set by the application. The application can unlock either or both dimensions by supplying the value -1 for parameters to the setPreferredSize method.
 * When an Item is created, both the preferred width and height are unlocked. In this state, the implementation computes the preferred width and height based on the Item's contents, possibly including other relevant factors such as the Item's graphic design and the screen dimensions. After having locked either the preferred width or height, the application can restore the initial, unlocked state by calling setPreferredSize(-1,-1).
 * The application can lock one dimension of the preferred size and leave the other unlocked. This causes the system to compute an appropriate value for the unlocked dimension based on arranging the contents to fit the locked dimension. If the contents changes, the size on the unlocked dimension is recomputed to reflect the new contents, but the size on the locked dimension remains unchanged. For example, if the application called setPreferredSize(50,-1), the preferred width would be locked at 50 pixels and the preferred height would be computed based on the Item's contents. Similarly, if the application called setPreferredSize(-1,60), the preferred height would be locked at 60 pixels and the preferred width would be computed based on the Item's contents. This feature is particularly useful for Items with textual content that can be line wrapped.
 * The application can also lock both the preferred width and height to specific values. The Item's contents are truncated or padded as necessary to honor this request. For Items containing text, the text should be wrapped to the specified width, and any truncation should occur at the end of the text.
 * Items also have an implicit maximum size provided by the implementation. The maximum width is typically based on the width of the screen space available to a Form. Since Forms can scroll vertically, the maximum height should typically not be based on the height of the available screen space.
 * If the application attempts to lock a preferred size dimension to a value smaller than the minimum or larger than the maximum, the implementation may disregard the requested value and instead use either the minimum or maximum as appropriate. If this occurs, the actual values used must be visible to the application via the values returned from the getPreferredWidth and getPreferredHeight methods.
 * A Command is said to be present on an Item if the Command has been added to this Item with a prior call to addCommand(javax.microedition.lcdui.Command) or setDefaultCommand(javax.microedition.lcdui.Command) and if the Command has not been removed with a subsequent call to removeCommand(javax.microedition.lcdui.Command). Commands present on an item should have a command type of ITEM. However, it is not an error for a command whose type is other than ITEM to be added to an item. For purposes of presentation and placement within its user interface, the implementation is allowed to treat a command's items as if they were of type ITEM.
 * Items may have a default Command. This state is controlled by the setDefaultCommand(javax.microedition.lcdui.Command) method. The default Command is eligible to be bound to a special platform-dependent user gesture. The implementation chooses which gesture is the most appropriate to initiate the default command on that particular Item. For example, on a device that has a dedicated selection key, pressing this key might invoke the item's default command. Or, on a stylus-based device, tapping on the Item might invoke its default command. Even if it can be invoked through a special gesture, the default command should also be invokable in the same fashion as other item commands.
 * It is possible that on some devices there is no special gesture suitable for invoking the default command on an item. In this case the default command must be accessible to the user in the same fashion as other item commands. The implementation may use the state of a command being the default in deciding where to place the command in its user interface.
 * It is possible for an Item not to have a default command. In this case, the implementation may bind its special user gesture (if any) for another purpose, such as for displaying a menu of commands. The default state of an Item is not to have a default command. An Item may be set to have no default Command by removing it from the Item or by passing null to the setDefaultCommand() method.
 * The same command may occur on more than one Item and also on more than one Displayable. If this situation occurs, the user must be provided with distinct gestures to invoke that command on each Item or Displayable on which it occurs, while those Items or Displayables are visible on the display. When the user invokes the command, the listener (CommandListener or ItemCommandListener as appropriate) of just the object on which the command was invoked will be called.
 * Adding commands to an Item may affect its appearance, the way it is laid out, and the traversal behavior. For example, the presence of commands on an Item may cause row breaks to occur, or it may cause additional graphical elements (such as a menu icon) to appear. In particular, if a StringItem whose appearance mode is PLAIN (see below) is given one or more Commands, the implementation is allowed to treat it as if it had a different appearance mode.
 * The StringItem and ImageItem classes have an appearance mode attribute that can be set in their constructors. This attribute can have one of the values PLAIN, HYPERLINK, or BUTTON. An appearance mode of PLAIN is typically used for non-interactive display of textual or graphical material. The appearance mode values do not have any side effects on the interactivity of the item. In order to be interactive, the item must have one or more Commands (preferably with a default command assigned), and it must have a CommandListener that receives notification of Command invocations. The appearance mode values also do not have any effect on the semantics of Command invocation on the item. For example, setting the appearance mode of a StringItem to be HYPERLINK requests that the implementation display the string contents as if they were a hyperlink in a browser. It is the application's responsibility to attach a Command and a listener to the StringItem that provide behaviors that the user would expect from invoking an operation on a hyperlink, such as loading the referent of the link or adding the link to the user's set of bookmarks.
 * Setting the appearance mode of an Item to be other than PLAIN may affect its minimum, preferred, and maximum sizes, as well as the way it is laid out. For example, a StringItem with an appearance mode of BUTTON should not be wrapped across rows. (However, a StringItem with an appearance mode of HYPERLINK should be wrapped the same way as if its appearance mode is PLAIN.)
 * A StringItem or ImageItem in BUTTON mode can be used to create a button-based user interface. This can easily lead to applications that are inconvenient to use. For example, in a traversal-based system, users must navigate to a button before they can invoke any commands on it. If buttons are spread across a long Form, users may be required to perform a considerable amount of navigation in order to discover all the available commands. Furthermore, invoking a command from a button at the other end of the Form can be quite cumbersome. Traversal-based systems often provide a means of invoking commands from anywhere (such as from a menu), without the need to traverse to a particular item. Instead of adding a command to a button and placing that button into a Form, it would often be more appropriate and convenient for users if that command were added directly to the Form. Buttons should be used only in cases where direct user interaction with the item's string or image contents is essential to the user's understanding of the commands that can be invoked from that item.
 * Unless otherwise specified by a subclass, the default state of newly created Items is as follows:
 * Since: MIDP 1.0
 */
public abstract class Item{
    /**
     * An appearance mode value indicating that the Item is to appear as a button.
     * Value 2 is assigned to BUTTON.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int BUTTON=2;

    /**
     * An appearance mode value indicating that the Item is to appear as a hyperlink.
     * Value 1 is assigned to HYPERLINK.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int HYPERLINK=1;

    /**
     * A layout directive indicating that new MIDP 2.0 layout rules are in effect for this Item. If this bit is clear, indicates that MIDP 1.0 layout behavior applies to this Item.
     * Value 0x4000 is assigned to LAYOUT_2.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_2=16384;

    /**
     * A layout directive indicating that this Item should have a bottom-aligned layout.
     * Value 0x20 is assigned to LAYOUT_BOTTOM.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_BOTTOM=32;

    /**
     * A layout directive indicating that this Item should have a horizontally centered layout.
     * Value 3 is assigned to LAYOUT_CENTER.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_CENTER=3;

    /**
     * A layout directive indicating that this Item should follow the default layout policy of its container.
     * Value 0 is assigned to LAYOUT_DEFAULT.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_DEFAULT=0;

    /**
     * A layout directive indicating that this Item's width may be increased to fill available space.
     * Value 0x800 is assigned to LAYOUT_EXPAND.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_EXPAND=2048;

    /**
     * A layout directive indicating that this Item should have a left-aligned layout.
     * Value 1 is assigned to LAYOUT_LEFT.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_LEFT=1;

    /**
     * A layout directive indicating that this Item should the last on its line or row, and that the next Item (if any) in the container should be placed on a new line or row.
     * Value 0x200 is assigned to LAYOUT_NEWLINE_AFTER.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_NEWLINE_AFTER=512;

    /**
     * A layout directive indicating that this Item should be placed at the beginning of a new line or row.
     * Value 0x100 is assigned to LAYOUT_NEWLINE_BEFORE.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_NEWLINE_BEFORE=256;

    /**
     * A layout directive indicating that this Item should have a right-aligned layout.
     * Value 2 is assigned to LAYOUT_RIGHT.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_RIGHT=2;

    /**
     * A layout directive indicating that this Item's width may be reduced to its minimum width.
     * Value 0x400 is assigned to LAYOUT_SHRINK
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_SHRINK=1024;

    /**
     * A layout directive indicating that this Item should have a top-aligned layout.
     * Value 0x10 is assigned to LAYOUT_TOP.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_TOP=16;

    /**
     * A layout directive indicating that this Item should have a vertically centered layout.
     * Value 0x30 is assigned to LAYOUT_VCENTER.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_VCENTER=48;

    /**
     * A layout directive indicating that this Item's height may be increased to fill available space.
     * Value 0x2000 is assigned to LAYOUT_VEXPAND.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_VEXPAND=8192;

    /**
     * A layout directive indicating that this Item's height may be reduced to its minimum height.
     * Value 0x1000 is assigned to LAYOUT_VSHRINK.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int LAYOUT_VSHRINK=4096;

    /**
     * An appearance mode value indicating that the Item is to have a normal appearance.
     * Value 0 is assigned to PLAIN.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int PLAIN=0;

    /**
     * Adds a context sensitive Command to the item. The semantic type of Command should be ITEM. The implementation will present the command only when the item is active, for example, highlighted.
     * If the added command is already in the item (tested by comparing the object references), the method has no effect. If the item is actually visible on the display, and this call affects the set of visible commands, the implementation should update the display as soon as it is feasible to do so.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void addCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Gets the label of this Item object.
     */
    public java.lang.String getLabel(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the layout directives used for placing the item.
     */
    public int getLayout(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the minimum height for this Item. This is a height at which the item can function and display its contents, though perhaps not optimally. See
     * for a complete discussion.
     */
    public int getMinimumHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the minimum width for this Item. This is a width at which the item can function and display its contents, though perhaps not optimally. See
     * for a complete discussion.
     */
    public int getMinimumWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the preferred height of this Item. If the application has locked the height to a specific value, this method returns that value. Otherwise, the return value is computed based on the Item's contents, possibly with respect to the Item's preferred width if it is locked. See
     * for a complete discussion.
     */
    public int getPreferredHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the preferred width of this Item. If the application has locked the width to a specific value, this method returns that value. Otherwise, the return value is computed based on the Item's contents, possibly with respect to the Item's preferred height if it is locked. See
     * for a complete discussion.
     */
    public int getPreferredWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Causes this Item's containing Form to notify the Item's
     * . The application calls this method to inform the listener on the Item that the Item's state has been changed in response to an action. Even though this method simply causes a call to another part of the application, this mechanism is useful for decoupling the implementation of an Item (in particular, the implementation of a CustomItem, though this also applies to subclasses of other items) from the consumer of the item.
     * If an edit was performed by invoking a separate screen, and the editor now wishes to return to the form which contained the selected Item, the preferred method is Display.setCurrent(Item) instead of Display.setCurrent(Displayable), because it allows the Form to restore focus to the Item that initially invoked the editor.
     * In order to make sure that the documented behavior of ItemStateListener is maintained, it is up to the caller (application) to guarantee that this function is not called unless:
     * the Item's value has actually been changed, and the change was the result of a user action (an
     * edit
     * ) and NOT as a result of state change via calls to Item's APIs
     * The call to ItemStateListener.itemStateChanged may be delayed in order to be serialized with the event stream. The notifyStateChanged method does not block awaiting the completion of the itemStateChanged method.
     */
    public void notifyStateChanged(){
        return; //TODO codavaj!!
    }

    /**
     * Removes the context sensitive command from item. If the command is not in the Item (tested by comparing the object references), the method has no effect. If the Item is actually visible on the display, and this call affects the set of visible commands, the implementation should update the display as soon as it is feasible to do so. If the command to be removed happens to be the default command, the command is removed and the default command on this Item is set to null. The following code: // Command c is the default command on Item item item.removeCommand(c); is equivalent to the following code: // Command c is the default command on Item item item.setDefaultCommand(null); item.removeCommand(c);
     */
    public void removeCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Sets default Command for this Item. If the Item previously had a default Command, that Command is no longer the default, but it remains present on the Item.
     * If not null, the Command object passed becomes the default Command for this Item. If the Command object passed is not currently present on this Item, it is added as if addCommand(javax.microedition.lcdui.Command) had been called before it is made the default Command.
     * If null is passed, the Item is set to have no default Command. The previous default Command, if any, remains present on the Item.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setDefaultCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Sets a listener for Commands to this Item, replacing any previous ItemCommandListener. A null reference is allowed and has the effect of removing any existing listener.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setItemCommandListener(ItemCommandListener l){
        return; //TODO codavaj!!
    }

    /**
     * Sets the label of the Item. If label is null, specifies that this item has no label.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setLabel(java.lang.String label){
        return; //TODO codavaj!!
    }

    /**
     * Sets the layout directives for this item.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setLayout(int layout){
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

}
