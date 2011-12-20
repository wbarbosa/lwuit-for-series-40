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
 * The Command class is a construct that encapsulates the semantic information of an action. The behavior that the command activates is not encapsulated in this object. This means that command contains only information about command not the actual action that happens when command is activated. The action is defined in a CommandListener associated with the Displayable. Command objects are presented in the user interface and the way they are presented may depend on the semantic information contained within the command.
 * Commands may be implemented in any user interface construct that has semantics for activating a single action. This, for example, can be a soft button, item in a menu, or some other direct user interface construct. For example, a speech interface may present these commands as voice tags.
 * The mapping to concrete user interface constructs may also depend on the total number of the commands. For example, if an application asks for more abstract commands than can be mapped onto the available physical buttons on a device, then the device may use an alternate human interface such as a menu. For example, the abstract commands that cannot be mapped onto physical buttons are placed in a menu and the label Menu is mapped onto one of the programmable buttons.
 * A command contains four pieces of information: a short label, an optional long label, a type, and a priority. One of the labels is used for the visual representation of the command, whereas the type and the priority indicate the semantics of the command.
 * Each command includes one or two label strings. The label strings are what the application requests to be shown to the user to represent this command. For example, one of these strings may appear next to a soft button on the device or as an element in a menu. For command types other than SCREEN, the labels provided may be overridden by a system-specific label that is more appropriate for this command on this device. The contents of the label strings are otherwise not interpreted by the implementation.
 * All commands have a short label. The long label is optional. If the long label is not present on a command, the short label is always used.
 * The short label string should be as short as possible so that it consumes a minimum of screen real estate. The long label can be longer and more descriptive, but it should be no longer than a few words. For example, a command's short label might be Play, and its long label might be Play Sound Clip.
 * The implementation chooses one of the labels to be presented in the user interface based on the context and the amount of space available. For example, the implementation might use the short label if the command appears on a soft button, and it might use the long label if the command appears on a menu, but only if there is room on the menu for the long label. The implementation may use the short labels of some commands and the long labels of other commands, and it is allowed to switch between using the short and long label at will. The application cannot determine which label is being used at any given time.
 * The application uses the command type to specify the intent of this command. For example, if the application specifies that the command is of type BACK, and if the device has a standard of placing the back operation on a certain soft-button, the implementation can follow the style of the device by using the semantic information as a guide. The defined types are BACK, CANCEL, EXIT, HELP, ITEM, OK, SCREEN, and STOP.
 * The application uses the priority value to describe the importance of this command relative to other commands on the same screen. Priority values are integers, where a lower number indicates greater importance. The actual values are chosen by the application. A priority value of one might indicate the most important command, priority values of two, three, four, and so on indicate commands of lesser importance.
 * Typically, the implementation first chooses the placement of a command based on the type of command and then places similar commands based on a priority order. This could mean that the command with the highest priority is placed so that user can trigger it directly and that commands with lower priority are placed on a menu. It is not an error for there to be commands on the same screen with the same priorities and types. If this occurs, the implementation will choose the order in which they are presented.
 * For example, if the application has the following set of commands:
 * An implementation with two soft buttons may map the BACK command to the right soft button and create an Options menu on the left soft button to contain the other commands. When user presses the left soft button, a menu with the two remaining Commands appears: If the application had three soft buttons, all commands can be mapped to soft buttons:
 * The application is always responsible for providing the means for the user to progress through different screens. An application may set up a screen that has no commands. This is allowed by the API but is generally not useful; if this occurs the user would have no means to move to another screen. Such program would simply considered to be in error. A typical device should provide a means for the user to direct the application manager to kill the erroneous application.
 * Since: MIDP 1.0
 */
public class Command{
    /**
     * A navigation command that returns the user to the logically previous screen. The jump to the previous screen is not done automatically by the implementation but by the
     * provided by the application. Note that the application defines the actual action since the strictly previous screen may not be logically correct.
     * Value 2 is assigned to BACK.
     * See Also:CANCEL, STOP, Constant Field Values
     */
    public static final int BACK=2;

    /**
     * A command that is a standard negative answer to a dialog implemented by current screen. Nothing is cancelled automatically by the implementation; cancellation is implemented by the
     * provided by the application.
     * With this command type, the application hints to the implementation that the user wants to dismiss the current screen without taking any action on anything that has been entered into the current screen, and usually that the user wants to return to the prior screen. In many cases CANCEL is interchangeable with BACK, but BACK is mainly used for navigation as in a browser-oriented applications.
     * Value 3 is assigned to CANCEL.
     * See Also:BACK, STOP, Constant Field Values
     */
    public static final int CANCEL=3;

    /**
     * A command used for exiting from the application. When the user invokes this command, the implementation does not exit automatically. The application's
     * will be called, and it should exit the application if it is appropriate to do so.
     * Value 7 is assigned to EXIT.
     * See Also:Constant Field Values
     */
    public static final int EXIT=7;

    /**
     * This command specifies a request for on-line help. No help information is shown automatically by the implementation. The
     * provided by the application is responsible for showing the help information.
     * Value 5 is assigned to HELP.
     * See Also:Constant Field Values
     */
    public static final int HELP=5;

    /**
     * With this command type the application can hint to the implementation that the command is specific to the items of the Screen or the elements of a Choice. Normally this means that command relates to the focused item or element. For example, an implementation of List can use this information for creating context sensitive menus.
     * Value 8 is assigned to ITEM.
     * See Also:Constant Field Values
     */
    public static final int ITEM=8;

    /**
     * A command that is a standard positive answer to a dialog implemented by current screen. Nothing is done automatically by the implementation; any action taken is implemented by the
     * provided by the application.
     * With this command type the application hints to the implementation that the user will use this command to ask the application to confirm the data that has been entered in the current screen and to proceed to the next logical screen.
     * CANCEL is often used together with OK.
     * Value 4 is assigned to OK.
     * See Also:CANCEL, Constant Field Values
     */
    public static final int OK=4;

    /**
     * Specifies an application-defined command that pertains to the current screen. Examples could be
     * Load
     * and
     * Save
     * . A SCREEN command generally applies to the entire screen's contents or to navigation among screens. This is in constrast to the ITEM type, which applies to the currently activated or focused item or element contained within this screen.
     * Value 1 is assigned to SCREEN.
     * See Also:Constant Field Values
     */
    public static final int SCREEN=1;

    /**
     * A command that will stop some currently running process, operation, etc. Nothing is stopped automatically by the implementation. The cessation must be performed by the
     * provided by the application.
     * With this command type the application hints to the implementation that the user will use this command to stop any currently running process visible to the user on the current screen. Examples of running processes might include downloading or sending of data. Use of the STOP command does not necessarily imply a switch to another screen.
     * Value 6 is assigned to STOP.
     * See Also:BACK, CANCEL, Constant Field Values
     */
    public static final int STOP=6;

    /**
     * Creates a new command object with the given short
     * ,
     * , and
     * . The newly created command has no long label. This constructor is identical to Command(label, null, commandType, priority).
     * label - the command's short labelcommandType - the command's typepriority - the command's priority value
     * - if label is null
     * - if the commandType is an invalid type
     */
    public Command(java.lang.String label, int commandType, int priority){
         //TODO codavaj!!
    }

    /**
     * Creates a new command object with the given
     * ,
     * , and
     * .
     * The short label is required and must not be null. The long label is optional and may be null if the command is to have no long label.
     * shortLabel - the command's short labellongLabel - the command's long label, or null if nonecommandType - the command's typepriority - the command's priority value
     * - if shortLabel is null
     * - if the commandType is an invalid type
     * MIDP 2.0
     */
    public Command(java.lang.String shortLabel, java.lang.String longLabel, int commandType, int priority){
         //TODO codavaj!!
    }

    /**
     * Gets the type of the command.
     */
    public int getCommandType(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the short label of the command.
     */
    public java.lang.String getLabel(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the long label of the command.
     */
    public java.lang.String getLongLabel(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the priority of the command.
     */
    public int getPriority(){
        return 0; //TODO codavaj!!
    }

}
