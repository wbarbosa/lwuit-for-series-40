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

import javax.microedition.midlet.MIDlet;

/**
 * Display represents the manager of the display and input devices of the system. It includes methods for retrieving properties of the device and for requesting that objects be displayed on the device. Other methods that deal with device attributes are primarily used with Canvas objects and are thus defined there instead of here.
 * There is exactly one instance of Display per MIDlet and the application can get a reference to that instance by calling the getDisplay() method. The application may call the getDisplay() method at any time during course of its execution. The Display object returned by all calls to getDisplay() will remain the same during this time.
 * A typical application will perform the following actions in response to calls to its MIDlet methods: startApp - the application is moving from the paused state to the active state. Initialization of objects needed while the application is active should be done. The application may call setCurrent() for the first screen if that has not already been done. Note that startApp() can be called several times if pauseApp() has been called in between. This means that one-time initialization should not take place here but instead should occur within the MIDlet's constructor. pauseApp - the application may pause its threads. Also, if it is desirable to start with another screen when the application is re-activated, the new screen should be set with setCurrent(). destroyApp - the application should free resources, terminate threads, etc. The behavior of method calls on user interface objects after destroyApp() has returned is undefined.
 * The user interface objects that are shown on the display device are contained within a Displayable object. At any time the application may have at most one Displayable object that it intends to be shown on the display device and through which user interaction occurs. This Displayable is referred to as the current Displayable.
 * The Display class has a setCurrent() method for setting the current Displayable and a getCurrent() method for retrieving the current Displayable. The application has control over its current Displayable and may call setCurrent() at any time. Typically, the application will change the current Displayable in response to some user action. This is not always the case, however. Another thread may change the current Displayable in response to some other stimulus. The current Displayable will also be changed when the timer for an Alert elapses.
 * The application's current Displayable may not physically be drawn on the screen, nor will user events (such as keystrokes) that occur necessarily be directed to the current Displayable. This may occur because of the presence of other MIDlet applications running simultaneously on the same device.
 * An application is said to be in the foreground if its current Displayable is actually visible on the display device and if user input device events will be delivered to it. If the application is not in the foreground, it lacks access to both the display and input devices, and it is said to be in the background. The policy for allocation of these devices to different MIDlet applications is outside the scope of this specification and is under the control of an external agent referred to as the application management software.
 * As mentioned above, the application still has a notion of its current Displayable even if it is in the background. The current Displayable is significant, even for background applications, because the current Displayable is always the one that will be shown the next time the application is brought into the foreground. The application can determine whether a Displayable is actually visible on the display by calling isShown(). In the case of Canvas, the showNotify() and hideNotify() methods are called when the Canvas is made visible and is hidden, respectively.
 * Each MIDlet application has its own current Displayable. This means that the getCurrent() method returns the MIDlet's current Displayable, regardless of the MIDlet's foreground/background state. For example, suppose a MIDlet running in the foreground has current Displayable F, and a MIDlet running in the background has current Displayable B. When the foreground MIDlet calls getCurrent(), it will return F, and when the background MIDlet calls getCurrent(), it will return B. Furthermore, if either MIDlet changes its current Displayable by calling setCurrent(), this will not affect the any other MIDlet's current Displayable.
 * It is possible for getCurrent() to return null. This may occur at startup time, before the MIDlet application has called setCurrent() on its first screen. The getCurrent() method will never return a reference to a Displayable object that was not passed in a prior call to setCurrent() call by this MIDlet.
 * Typically, the current screen of the foreground MIDlet will be visible on the display. However, under certain circumstances, the system may create a screen that temporarily obscures the application's current screen. These screens are referred to as system screens. This may occur if the system needs to show a menu of commands or if the system requires the user to edit text on a separate screen instead of within a text field inside a Form. Even though the system screen obscures the application's screen, the notion of the current screen does not change. In particular, while a system screen is visible, a call to getCurrent() will return the application's current screen, not the system screen. The value returned by isShown() is false while the current Displayable is obscured by a system screen.
 * If system screen obscures a canvas, its hideNotify() method is called. When the system screen is removed, restoring the canvas, its showNotify() method and then its paint() method are called. If the system screen was used by the user to issue a command, the commandAction() method is called after showNotify() is called.
 * This class contains methods to retrieve the prevailing foreground and background colors of the high-level user interface. These methods are useful for creating CustomItem objects that match the user interface of other items and for creating user interfaces within Canvas that match the user interface of the rest of the system. Implementations are not restricted to using foreground and background colors in their user interfaces (for example, they might use highlight and shadow colors for a beveling effect) but the colors returned are those that match reasonably well with the implementation's color scheme. An application implementing a custom item should use the background color to clear its region and then paint text and geometric graphics (lines, arcs, rectangles) in the foreground color.
 * Since: MIDP 1.0
 */
public class Display{
    /**
     * Image type for Alert image.
     * The value of ALERT is 3.
     * Since: MIDP 2.0 See Also:getBestImageWidth(int imageType), getBestImageHeight(int imageType), Constant Field Values
     */
    public static final int ALERT=3;

    /**
     * Image type for ChoiceGroup element image.
     * The value of CHOICE_GROUP_ELEMENT is 2.
     * Since: MIDP 2.0 See Also:getBestImageWidth(int imageType), getBestImageHeight(int imageType), Constant Field Values
     */
    public static final int CHOICE_GROUP_ELEMENT=2;

    /**
     * A color specifier for use with getColor. COLOR_BACKGROUND specifies the background color of the screen. The background color will always contrast with the foreground color.
     * COLOR_BACKGROUND has the value 0.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_BACKGROUND=0;

    /**
     * A color specifier for use with getColor. COLOR_BORDER identifies the color for boxes and borders when the object is to be drawn in a non-highlighted state. The border color is intended to be used with the background color and will contrast with it. The application should draw its borders using the stroke style returned by getBorderStyle().
     * COLOR_BORDER has the value 4.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_BORDER=4;

    /**
     * A color specifier for use with getColor. COLOR_FOREGROUND specifies the foreground color, for text characters and simple graphics on the screen. Static text or user-editable text should be drawn with the foreground color. The foreground color will always constrast with background color.
     * COLOR_FOREGROUND has the value 1.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_FOREGROUND=1;

    /**
     * A color specifier for use with getColor. COLOR_HIGHLIGHTED_BACKGROUND identifies the color for the focus, or focus highlight, when it is drawn as a filled in rectangle. The highlighted background will always constrast with the highlighted foreground.
     * COLOR_HIGHLIGHTED_BACKGROUND has the value 2.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_HIGHLIGHTED_BACKGROUND=2;

    /**
     * A color specifier for use with getColor. COLOR_HIGHLIGHTED_BORDER identifies the color for boxes and borders when the object is to be drawn in a highlighted state. The highlighted border color is intended to be used with the background color (not the highlighted background color) and will contrast with it. The application should draw its borders using the stroke style returned by getBorderStyle().
     * COLOR_HIGHLIGHTED_BORDER has the value 5.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_HIGHLIGHTED_BORDER=5;

    /**
     * A color specifier for use with getColor. COLOR_HIGHLIGHTED_FOREGROUND identifies the color for text characters and simple graphics when they are highlighted. Highlighted foreground is the color to be used to draw the highlighted text and graphics against the highlighted background. The highlighted foreground will always constrast with the highlighted background.
     * COLOR_HIGHLIGHTED_FOREGROUND has the value 3.
     * Since: MIDP 2.0 See Also:getColor(int), Constant Field Values
     */
    public static final int COLOR_HIGHLIGHTED_FOREGROUND=3;

    /**
     * Image type for List element image.
     * The value of LIST_ELEMENT is 1.
     * Since: MIDP 2.0 See Also:getBestImageWidth(int imageType), getBestImageHeight(int imageType), Constant Field Values
     */
    public static final int LIST_ELEMENT=1;

    /**
     * Causes the Runnable object r to have its run() method called later, serialized with the event stream, soon after completion of the repaint cycle. As noted in the
     * section of the package summary, the methods that deliver event notifications to the application are all called serially. The call to r.run() will be serialized along with the event calls into the application. The run() method will be called exactly once for each call to callSerially(). Calls to run() will occur in the order in which they were requested by calls to callSerially().
     * If the current Displayable is a Canvas that has a repaint pending at the time of a call to callSerially(), the paint() method of the Canvas will be called and will return, and a buffer switch will occur (if double buffering is in effect), before the run() method of the Runnable is called. If the current Displayable contains one or more CustomItems that have repaints pending at the time of a call to callSerially(), the paint() methods of the CustomItems will be called and will return before the run() method of the Runnable is called. Calls to the run() method will occur in a timely fashion, but they are not guaranteed to occur immediately after the repaint cycle finishes, or even before the next event is delivered.
     * The callSerially() method may be called from any thread. The call to the run() method will occur independently of the call to callSerially(). In particular, callSerially() will never block waiting for r.run() to return.
     * As with other callbacks, the call to r.run() must return quickly. If it is necessary to perform a long-running operation, it may be initiated from within the run() method. The operation itself should be performed within another thread, allowing run() to return.
     * The callSerially() facility may be used by applications to run an animation that is properly synchronized with the repaint cycle. A typical application will set up a frame to be displayed and then call repaint(). The application must then wait until the frame is actually displayed, after which the setup for the next frame may occur. The call to run() notifies the application that the previous frame has finished painting. The example below shows callSerially() being used for this purpose.
     */
    public void callSerially(java.lang.Runnable r){
        return; //TODO codavaj!!
    }

    /**
     * Requests a flashing effect for the device's backlight. The flashing effect is intended to be used to attract the user's attention or as a special effect for games. Examples of flashing are cycling the backlight on and off or from dim to bright repeatedly. The return value indicates if the flashing of the backlight can be controlled by the application.
     * The flashing effect occurs for the requested duration, or it is switched off if the requested duration is zero. This method returns immediately; that is, it must not block the caller while the flashing effect is running.
     * Calls to this method are honored only if the Display is in the foreground. This method MUST perform no action and return false if the Display is in the background.
     * The device MAY limit or override the duration. For devices that do not include a controllable backlight, calls to this method return false.
     */
    public boolean flashBacklight(int duration){
        return false; //TODO codavaj!!
    }

    /**
     * Returns the best image height for a given image type. The image type must be one of
     * ,
     * , or
     * .
     */
    public int getBestImageHeight(int imageType){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the best image width for a given image type. The image type must be one of
     * ,
     * , or
     * .
     */
    public int getBestImageWidth(int imageType){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the stroke style used for border drawing depending on the state of the component (highlighted/non-highlighted). For example, on a monochrome system, the border around a non-highlighted item might be drawn with a DOTTED stroke style while the border around a highlighted item might be drawn with a SOLID stroke style.
     */
    public int getBorderStyle(boolean highlighted){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns one of the colors from the high level user interface color scheme, in the form 0x00RRGGBB based on the colorSpecifier passed in.
     */
    public int getColor(int colorSpecifier){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current Displayable object for this MIDlet. The Displayable object returned may not actually be visible on the display if the MIDlet is running in the background, or if the Displayable is obscured by a system screen. The
     * method may be called to determine whether the Displayable is actually visible on the display.
     * The value returned by getCurrent() may be null. This occurs after the application has been initialized but before the first call to setCurrent().
     */
    public Displayable getCurrent(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the Display object that is unique to this MIDlet.
     */
    public static Display getDisplay(MIDlet m){
        return null; //TODO codavaj!!
    }

    /**
     * Gets information about color support of the device.
     */
    public boolean isColor(){
        return false; //TODO codavaj!!
    }

    /**
     * Gets the number of alpha transparency levels supported by this implementation. The minimum legal return value is 2, which indicates support for full transparency and full opacity and no blending. Return values greater than 2 indicate that alpha blending is supported. For further information, see
     * .
     */
    public int numAlphaLevels(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the number of colors (if isColor() is true) or graylevels (if isColor() is false) that can be represented on the device.
     * Note that the number of colors for a black and white display is 2.
     */
    public int numColors(){
        return 0; //TODO codavaj!!
    }

    /**
     * Requests that this Alert be made current, and that nextDisplayable be made current after the Alert is dismissed. This call returns immediately regardless of the Alert's timeout value or whether it is a modal alert. The nextDisplayable must not be an Alert, and it must not be null.
     * The automatic advance to nextDisplayable occurs only when the Alert's default listener is present on the Alert when it is dismissed. See Alert Commands and Listeners for details.
     * In other respects, this method behaves identically to setCurrent(Displayable).
     */
    public void setCurrent(Alert alert, Displayable nextDisplayable){
        return; //TODO codavaj!!
    }

    /**
     * Requests that a different Displayable object be made visible on the display. The change will typically not take effect immediately. It may be delayed so that it occurs between event delivery method calls, although it is not guaranteed to occur before the next event delivery method is called. The setCurrent() method returns immediately, without waiting for the change to take place. Because of this delay, a call to getCurrent() shortly after a call to setCurrent() is unlikely to return the value passed to setCurrent().
     * Calls to setCurrent() are not queued. A delayed request made by a setCurrent() call may be superseded by a subsequent call to setCurrent(). For example, if screen S1 is current, then
     * may eventually result in S3 being made current, bypassing S2 entirely.
     * When a MIDlet application is first started, there is no current Displayable object. It is the responsibility of the application to ensure that a Displayable is visible and can interact with the user at all times. Therefore, the application should always call setCurrent() as part of its initialization.
     * The application may pass null as the argument to setCurrent(). This does not have the effect of setting the current Displayable to null; instead, the current Displayable remains unchanged. However, the application management software may interpret this call as a request from the application that it is requesting to be placed into the background. Similarly, if the application is in the background, passing a non-null reference to setCurrent() may be interpreted by the application management software as a request that the application is requesting to be brought to the foreground. The request should be considered to be made even if the current Displayable is passed to the setCurrent(). For example, the code
     * generally will have no effect other than requesting that the application be brought to the foreground. These are only requests, and there is no requirement that the application management software comply with these requests in a timely fashion if at all.
     * If the Displayable passed to setCurrent() is an Alert, the previously current Displayable, if any, is restored after the Alert has been dismissed. If there is a current Displayable, the effect is as if setCurrent(Alert, getCurrent()) had been called. Note that this will result in an exception being thrown if the current Displayable is already an alert. If there is no current Displayable (which may occur at startup time) the implementation's previous state will be restored after the Alert has been dismissed. The automatic restoration of the previous Displayable or the previous state occurs only when the Alert's default listener is present on the Alert when it is dismissed. See Alert Commands and Listeners for details.
     * To specify the Displayable to be shown after an Alert is dismissed, the application should use the setCurrent(Alert, Displayable) method. If the application calls setCurrent() while an Alert is current, the Alert is removed from the display and any timer it may have set is cancelled.
     * If the application calls setCurrent() while a system screen is active, the effect may be delayed until after the system screen is dismissed. The implementation may choose to interpret setCurrent() in such a situation as a request to cancel the effect of the system screen, regardless of whether setCurrent() has been delayed.
     */
    public void setCurrent(Displayable nextDisplayable){
        return; //TODO codavaj!!
    }

    /**
     * Requests that the Displayable that contains this Item be made current, scrolls the Displayable so that this Item is visible, and possibly assigns the focus to this Item. The containing Displayable is first made current as if
     * had been called. When the containing Displayable becomes current, or if it is already current, it is scrolled if necessary so that the requested Item is made visible. Then, if the implementation supports the notion of input focus, and if the Item accepts the input focus, the input focus is assigned to the Item.
     * This method always returns immediately, without waiting for the switching of the Displayable, the scrolling, and the assignment of input focus to take place.
     * It is an error for the Item not to be contained within a container. It is also an error if the Item is contained within an Alert.
     */
    public void setCurrentItem(Item item){
        return; //TODO codavaj!!
    }

    /**
     * Requests operation of the device's vibrator. The vibrator is intended to be used to attract the user's attention or as a special effect for games. The return value indicates if the vibrator can be controlled by the application.
     * This method switches on the vibrator for the requested duration, or switches it off if the requested duration is zero. If this method is called while the vibrator is still activated from a previous call, the request is interpreted as setting a new duration. It is not interpreted as adding additional time to the original request. This method returns immediately; that is, it must not block the caller while the vibrator is running.
     * Calls to this method are honored only if the Display is in the foreground. This method MUST perform no action and return false if the Display is in the background.
     * The device MAY limit or override the duration. For devices that do not include a controllable vibrator, calls to this method return false.
     */
    public boolean vibrate(int duration){
        return false; //TODO codavaj!!
    }

}
