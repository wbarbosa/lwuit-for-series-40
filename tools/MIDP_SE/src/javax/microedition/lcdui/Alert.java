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
 * An alert is a screen that shows data to the user and waits for a certain period of time before proceeding to the next Displayable. An alert can contain a text string and an image. The intended use of Alert is to inform the user about errors and other exceptional conditions.
 * The application can set the alert time to be infinity with setTimeout(Alert.FOREVER) in which case the Alert is considered to be modal and the implementation provide a feature that allows the user to dismiss the alert, whereupon the next Displayable is displayed as if the timeout had expired immediately.
 * If an application specifies an alert to be of a timed variety and gives it too much content such that it must scroll, then it automatically becomes a modal alert.
 * An alert may have an AlertType associated with it to provide an indication of the nature of the alert. The implementation may use this type to play an appropriate sound when the Alert is presented to the user. See AlertType.playSound().
 * An alert may contain an optional Image. The Image may be mutable or immutable. If the Image is mutable, the effect is as if a snapshot of its contents is taken at the time the Alert is constructed with this Image and when setImage is called with an Image. This snapshot is used whenever the contents of the Alert are to be displayed. Even if the application subsequently draws into the Image, the snapshot is not modified until the next call to setImage. The snapshot is not updated when the Alert becomes current or becomes visible on the display. (This is because the application does not have control over exactly when Displayables appear and disappear from the display.)
 * An alert may contain an optional Gauge object that is used as an activity or progress indicator. By default, an Alert has no activity indicator; one may be set with the setIndicator(javax.microedition.lcdui.Gauge) method. The Gauge object used for the activity indicator must conform to all of the following restrictions:
 * It is an error for the application to attempt to use a Gauge object that violates any of these restrictions. In addition, when the Gauge object is being used as the indicator within an Alert, the application is prevented from modifying any of these pieces of the Gauge's state.
 * Like the other Displayable classes, an Alert can accept Commands, which can be delivered to a CommandListener set by the application. The Alert class adds some special behavior for Commands and listeners.
 * When it is created, an Alert implicitly has the special Command DISMISS_COMMAND present on it. If the application adds any other Commands to the Alert, DISMISS_COMMAND is implicitly removed. If the application removes all other Commands, DISMISS_COMMAND is implicitly restored. Attempts to add or remove DISMISS_COMMAND explicitly are ignored. Thus, there is always at least one Command present on an Alert.
 * If there are two or more Commands present on the Alert, it is automatically turned into a modal Alert, and the timeout value is always FOREVER. The Alert remains on the display until a Command is invoked. If the Alert has one Command (whether it is DISMISS_COMMAND or it is one provided by the application), the Alert may have the timed behavior as described above. When a timeout occurs, the effect is the same as if the user had invoked the Command explicitly.
 * When it is created, an Alert implicitly has a CommandListener called the default listener associated with it. This listener may be replaced by an application-provided listener through use of the setCommandListener(javax.microedition.lcdui.CommandListener) method. If the application removes its listener by passing null to the setCommandListener method, the default listener is implicitly restored.
 * The Display.setCurrent(Alert, Displayable) method and the Display.setCurrent(Displayable) method (when called with an Alert) define special behavior for automatically advancing to another Displayable after the Alert is dismissed. This special behavior occurs only when the default listener is present on the Alert at the time it is dismissed or when a command is invoked. If the user invokes a Command and the default listener is present, the default listener ignores the Command and implements the automatic-advance behavior.
 * If the application has set its own CommandListener, the automatic-advance behavior is disabled. The listener code is responsible for advancing to another Displayable. When the application has provided a listener, Commands are invoked normally by passing them to the listener's commandAction method. The Command passed will be one of the Commands present on the Alert: either DISMISS_COMMAND or one of the application-provided Commands.
 * The application can restore the default listener by passing null to the setCommandListener method.
 * Note: An application may set a Ticker with Displayable.setTicker on an Alert, however it may not be displayed due to implementation restrictions.
 * Since: MIDP 1.0 See Also:AlertType
 */
public class Alert extends Screen{
    /**
     * A Command delivered to a listener to indicate that the Alert has been dismissed. This Command is implicitly present an on Alert whenever there are no other Commands present. The field values of DISMISS_COMMAND are as follows: label =
     * (an empty string) type = Command.OK priority = 0
     * The label value visible to the application must be as specified above. However, the implementation may display DISMISS_COMMAND to the user using an implementation-specific label.
     * Attempting to add or remove DISMISS_COMMAND from an Alert has no effect. However, DISMISS_COMMAND is treated as an ordinary Command if it is used with other Displayable types.
     * Since: MIDP 2.0
     */
    public static final Command DISMISS_COMMAND=null;

    /**
     * FOREVER indicates that an Alert is kept visible until the user dismisses it. It is used as a value for the parameter to
     * to indicate that the alert is modal. Instead of waiting for a specified period of time, a modal Alert will wait for the user to take some explicit action, such as pressing a button, before proceeding to the next Displayable.
     * Value -2 is assigned to FOREVER.
     * See Also:Constant Field Values
     */
    public static final int FOREVER=-2;

    /**
     * Constructs a new, empty Alert object with the given title. If null is passed, the Alert will have no title. Calling this constructor is equivalent to calling Alert(title, null, null, null)
     * title - the title string, or null
     */
    public Alert(java.lang.String title){
         //TODO codavaj!!
    }

    /**
     * Constructs a new Alert object with the given title, content string and image, and alert type. The layout of the contents is implementation dependent. The timeout value of this new alert is the same value that is returned by getDefaultTimeout(). The Image provided may either be mutable or immutable. The handling and behavior of specific AlertTypes is described in
     * . null is allowed as the value of the alertType parameter and indicates that the Alert is not to have a specific alert type. DISMISS_COMMAND is the only Command present on the new Alert. The CommandListener associated with the new Alert is the default listener. Its behavior is described in more detail in the section
     * .
     * title - the title string, or null if there is no titlealertText - the string contents, or null if there is no stringalertImage - the image contents, or null if there is no imagealertType - the type of the Alert, or null if the Alert has no specific type
     */
    public Alert(java.lang.String title, java.lang.String alertText, Image alertImage, AlertType alertType){
         //TODO codavaj!!
    }

    /**
     * Similar to
     * , however when the application first adds a command to an Alert,
     * is implicitly removed. Calling this method with DISMISS_COMMAND as the parameter has no effect.
     */
    public void addCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Gets the default time for showing an Alert. This is either a positive value, which indicates a time in milliseconds, or the special value
     * , which indicates that Alerts are modal by default. The value returned will vary across implementations and is presumably tailored to be suitable for each.
     */
    public int getDefaultTimeout(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the Image used in the Alert.
     */
    public Image getImage(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the activity indicator for this Alert.
     */
    public Gauge getIndicator(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the text string used in the Alert.
     */
    public java.lang.String getString(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the time this Alert will be shown. This is either a positive value, which indicates a time in milliseconds, or the special value FOREVER, which indicates that this Alert is modal. This value is not necessarily the same value that might have been set by the application in a call to
     * . In particular, if the Alert is made modal because its contents is large enough to scroll, the value returned by getTimeout will be FOREVER.
     */
    public int getTimeout(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the type of the Alert.
     */
    public AlertType getType(){
        return null; //TODO codavaj!!
    }

    /**
     * Similar to
     * , however when the application removes the last command from an Alert,
     * is implicitly added. Calling this method with DISMISS_COMMAND as the parameter has no effect.
     */
    public void removeCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * The same as
     * but with the following additional semantics. If the listener parameter is null, the default listener is restored. See
     * for the definition of the behavior of the default listener.
     */
    public void setCommandListener(CommandListener l){
        return; //TODO codavaj!!
    }

    /**
     * Sets the Image used in the Alert. The Image may be mutable or immutable. If img is null, specifies that this Alert has no image. If img is mutable, the effect is as if a snapshot is taken of img's contents immediately prior to the call to setImage. This snapshot is used whenever the contents of the Alert are to be displayed. If img is already the Image of this Alert, the effect is as if a new snapshot of img's contents is taken. Thus, after painting into a mutable image contained by an Alert, the application can call
     * to refresh the Alert's snapshot of its Image.
     * If the Alert is visible on the display when its contents are updated through a call to setImage, the display will be updated with the new snapshot as soon as it is feasible for the implementation to do so.
     */
    public void setImage(Image img){
        return; //TODO codavaj!!
    }

    /**
     * Sets an activity indicator on this Alert. The activity indicator is a
     * object. It must be in a restricted state in order for it to be used as the activity indicator for an Alert. The restrictions are listed
     * . If the Gauge object violates any of these restrictions, IllegalArgumentException is thrown.
     * If indicator is null, this removes any activity indicator present on this Alert.
     */
    public void setIndicator(Gauge indicator){
        return; //TODO codavaj!!
    }

    /**
     * Sets the text string used in the Alert.
     * If the Alert is visible on the display when its contents are updated through a call to setString, the display will be updated with the new contents as soon as it is feasible for the implementation to do so.
     */
    public void setString(java.lang.String str){
        return; //TODO codavaj!!
    }

    /**
     * Set the time for which the Alert is to be shown. This must either be a positive time value in milliseconds, or the special value FOREVER.
     */
    public void setTimeout(int time){
        return; //TODO codavaj!!
    }

    /**
     * Sets the type of the Alert. The handling and behavior of specific AlertTypes is described in
     * .
     */
    public void setType(AlertType type){
        return; //TODO codavaj!!
    }

}
