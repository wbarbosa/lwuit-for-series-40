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
 * The Canvas class is a base class for writing applications that need to handle low-level events and to issue graphics calls for drawing to the display. Game applications will likely make heavy use of the Canvas class. From an application development perspective, the Canvas class is interchangeable with standard Screen classes, so an application may mix and match Canvas with high-level screens as needed. For example, a List screen may be used to select the track for a racing game, and a Canvas subclass would implement the actual game.
 * The Canvas provides the developer with methods to handle game actions, key events, and pointer events (if supported by the device). Methods are also provided to identify the device's capabilities and mapping of keys to game actions. The key events are reported with respect to key codes, which are directly bound to concrete keys on the device, use of which may hinder portability. Portable applications should use game actions instead of key codes.
 * Like other subclasses of Displayable, the Canvas class allows the application to register a listener for commands. Unlike other Displayables, however, the Canvas class requires applications to subclass it in order to use it. The paint() method is declared abstract, and so the application must provide an implementation in its subclass. Other event-reporting methods are not declared abstract, and their default implementations are empty (that is, they do nothing). This allows the application to override only the methods that report events in which the application has interest.
 * This is in contrast to the Screen classes, which allow the application to define listeners and to register them with instances of the Screen classes. This style is not used for the Canvas class, because several new listener interfaces would need to be created, one for each kind of event that might be delivered. An alternative would be to have fewer listener interfaces, but this would require listeners to filter out events in which they had no interest.
 * Applications receive keystroke events in which the individual keys are named within a space of key codes. Every key for which events are reported to MIDP applications is assigned a key code. The key code values are unique for each hardware key unless two keys are obvious synonyms for each other. MIDP defines the following key codes: KEY_NUM0, KEY_NUM1, KEY_NUM2, KEY_NUM3, KEY_NUM4, KEY_NUM5, KEY_NUM6, KEY_NUM7, KEY_NUM8, KEY_NUM9, KEY_STAR, and KEY_POUND. (These key codes correspond to keys on a ITU-T standard telephone keypad.) Other keys may be present on the keyboard, and they will generally have key codes distinct from those list above. In order to guarantee portability, applications should use only the standard key codes.
 * The standard key codes' values are equal to the Unicode encoding for the character that represents the key. If the device includes any other keys that have an obvious correspondence to a Unicode character, their key code values should equal the Unicode encoding for that character. For keys that have no corresponding Unicode character, the implementation must use negative values. Zero is defined to be an invalid key code. It is thus possible for an application to convert a keyCode into a Unicode character using the following code:
 * This technique is useful only in certain limited cases. In particular, it is not sufficient for full textual input, because it does not handle upper and lower case, keyboard shift states, and characters that require more than one keystroke to enter. For textual input, applications should always use TextBox or TextField objects.
 * It is sometimes useful to find the name of a key in order to display a message about this key. In this case the application may use the getKeyName() method to find a key's name.
 * Portable applications that need arrow key events and gaming-related events should use game actions in preference to key codes and key names. MIDP defines the following game actions: UP, DOWN, LEFT, RIGHT, FIRE, GAME_A, GAME_B, GAME_C, and GAME_D.
 * Each key code may be mapped to at most one game action. However, a game action may be associated with more than one key code. The application can translate a key code into a game action using the getGameAction(int keyCode) method, and it can translate a game action into a key code using the getKeyCode(int gameAction) method. There may be multiple keycodes associated with a particular game action, but getKeyCode returns only one of them. Supposing that g is a valid game action and k is a valid key code for a key associated with a game action, consider the following expressions:
 * Expression (1) is always true. However, expression (2) might be true but is not necessarily true.
 * The implementation is not allowed to change the mapping of game actions and key codes during execution of the application.
 * Portable applications that are interested in using game actions should translate every key event into a game action by calling the getGameAction() method and then testing the result. For example, on some devices the game actions UP, DOWN, LEFT and RIGHT may be mapped to 4-way navigation arrow keys. In this case, getKeyCode(UP) would return a device-dependent code for the up-arrow key. On other devices, a possible mapping would be on the number keys 2, 4, 6 and 8. In this case, getKeyCode(UP) would return KEY_NUM2. In both cases, the getGameAction() method would return the LEFT game action when the user presses the key that is a natural left on her device.
 * It is also possible for the user to issue commands when a canvas is current. Commands are mapped to keys and menus in a device-specific fashion. For some devices the keys used for commands may overlap with the keys that will deliver key code events to the canvas. If this is the case, the device will provide a means transparent to the application that enables the user to select a mode that determines whether these keys will deliver commands or key code events to the application. When the Canvas is in normal mode (see below), the set of key code events available to a canvas will not change depending upon the number of commands present or the presence of a command listener. When the Canvas is in full-screen mode, if there is no command listener present, the device may choose to deliver key code events for keys that would otherwise be reserved for delivery of commands. Game developers should be aware that access to commands will vary greatly across devices, and that requiring the user to issue commands during game play may have a great impact on the ease with which the game can be played.
 * The Canvas object defines several methods that are called by the implementation. These methods are primarily for the purpose of delivering events to the application, and so they are referred to as event delivery methods. The set of methods is:
 * These methods are all called serially. That is, the implementation will never call an event delivery method before a prior call to any of the event delivery methods has returned. The serviceRepaints() method is an exception to this rule, as it blocks until paint() is called and returns. This will occur even if the application is in the midst of one of the event delivery methods when it calls serviceRepaints().
 * The Display.callSerially() method can be used to serialize some application-defined work with the event stream. For further information, see the Event Handling and Concurrency sections of the package summary.
 * The key-related, pointer-related, and paint() methods will only be called while the Canvas is actually visible on the output device. These methods will therefore only be called on this Canvas object only after a call to showNotify() and before a call to hideNotify(). After hideNotify() has been called, none of the key, pointer, and paint methods will be called until after a subsequent call to showNotify() has returned. A call to a run() method resulting from callSerially() may occur irrespective of calls to showNotify() and hideNotify().
 * The showNotify() method is called prior to the Canvas actually being made visible on the display, and the hideNotify() method is called after the Canvas has been removed from the display. The visibility state of a Canvas (or any other Displayable object) may be queried through the use of the Displayable.isShown() method. The change in visibility state of a Canvas may be caused by the application management software moving MIDlets between foreground and background states, or by the system obscuring the Canvas with system screens. Thus, the calls to showNotify() and hideNotify() are not under the control of the MIDlet and may occur fairly frequently. Application developers are encouraged to perform expensive setup and teardown tasks outside the showNotify() and hideNotify() methods in order to make them as lightweight as possible.
 * A Canvas can be in normal mode or in full-screen mode. In normal mode, space on the display may be occupied by command labels, a title, and a ticker. By setting a Canvas into full-screen mode, the application is requesting that the Canvas occupy as much of the display space as is possible. In full-screen mode, the title and ticker are not displayed even if they are present on the Canvas, and Commands may be presented using some alternative means (such as through a pop-up menu). Note that the implementation may still consume a portion of the display for things like status indicators, even if the displayed Canvas is in full-screen mode. In full-screen mode, although the title is not displayed, its text may still be used for other purposes, such as for the title of a pop-up menu of Commands.
 * Canvas objects are in normal mode by default. The normal vs. full-screen mode setting is controlled through the use of the setFullScreenMode(boolean) method.
 * Calling setFullScreenMode(boolean) may result in sizeChanged() being called. The default implementation of this method does nothing. The application can override this method to handle changes in size of available drawing area.
 * Note: As mentioned in the Specification Requirements section of the overview, implementations must provide the user with an indication of network usage. If the indicator is rendered on screen, it must be visible when network activity occurs, even when the Canvas is in full-screen mode.
 * Since: MIDP 1.0
 */
public abstract class Canvas extends Displayable{
    /**
     * Constant for the DOWN game action.
     * Constant value 6 is set to DOWN.
     * See Also:Constant Field Values
     */
    public static final int DOWN=6;

    /**
     * Constant for the FIRE game action.
     * Constant value 8 is set to FIRE.
     * See Also:Constant Field Values
     */
    public static final int FIRE=8;

    /**
     * Constant for the general purpose
     * A
     * game action.
     * Constant value 9 is set to GAME_A.
     * See Also:Constant Field Values
     */
    public static final int GAME_A=9;

    /**
     * Constant for the general purpose
     * B
     * game action.
     * Constant value 10 is set to GAME_B.
     * See Also:Constant Field Values
     */
    public static final int GAME_B=10;

    /**
     * Constant for the general purpose
     * C
     * game action.
     * Constant value 11 is set to GAME_C.
     * See Also:Constant Field Values
     */
    public static final int GAME_C=11;

    /**
     * Constant for the general purpose
     * D
     * game action.
     * Constant value 12 is set to GAME_D.
     * See Also:Constant Field Values
     */
    public static final int GAME_D=12;

    /**
     * keyCode for ITU-T key 0.
     * Constant value 48 is set to KEY_NUM0.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM0=48;

    /**
     * keyCode for ITU-T key 1.
     * Constant value 49 is set to KEY_NUM1.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM1=49;

    /**
     * keyCode for ITU-T key 2.
     * Constant value 50 is set to KEY_NUM2.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM2=50;

    /**
     * keyCode for ITU-T key 3.
     * Constant value 51 is set to KEY_NUM3.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM3=51;

    /**
     * keyCode for ITU-T key 4.
     * Constant value 52 is set to KEY_NUM4.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM4=52;

    /**
     * keyCode for ITU-T key 5.
     * Constant value 53 is set to KEY_NUM5.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM5=53;

    /**
     * keyCode for ITU-T key 6.
     * Constant value 54 is set to KEY_NUM6.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM6=54;

    /**
     * keyCode for ITU-T key 7.
     * Constant value 55 is set to KEY_NUM7.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM7=55;

    /**
     * keyCode for ITU-T key 8.
     * Constant value 56 is set to KEY_NUM8.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM8=56;

    /**
     * keyCode for ITU-T key 9.
     * Constant value 57 is set to KEY_NUM09.
     * See Also:Constant Field Values
     */
    public static final int KEY_NUM9=57;

    /**
     * keyCode for ITU-T key
     * pound
     * (#).
     * Constant value 35 is set to KEY_POUND.
     * See Also:Constant Field Values
     */
    public static final int KEY_POUND=35;

    /**
     * keyCode for ITU-T key
     * star
     * (*).
     * Constant value 42 is set to KEY_STAR.
     * See Also:Constant Field Values
     */
    public static final int KEY_STAR=42;

    /**
     * Constant for the LEFT game action.
     * Constant value 2 is set to LEFT.
     * See Also:Constant Field Values
     */
    public static final int LEFT=2;

    /**
     * Constant for the RIGHT game action.
     * Constant value 5 is set to RIGHT.
     * See Also:Constant Field Values
     */
    public static final int RIGHT=5;

    /**
     * Constant for the UP game action.
     * Constant value 1 is set to UP.
     * See Also:Constant Field Values
     */
    public static final int UP=1;

    /**
     * Constructs a new Canvas object.
     */
    protected Canvas(){
         //TODO codavaj!!
    }

    /**
     * Gets the game action associated with the given key code of the device. Returns zero if no game action is associated with this key code. See
     * for further discussion of game actions.
     * The mapping between key codes and game actions will not change during the execution of the application.
     */
    public int getGameAction(int keyCode){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets a key code that corresponds to the specified game action on the device. The implementation is required to provide a mapping for every game action, so this method will always return a valid key code for every game action. See
     * for further discussion of game actions. There may be multiple keys associated with the same game action; however, this method will return only one of them. Applications should translate the key code of every key event into a game action using
     * and then interpret the resulting game action, instead of generating a table of key codes at using this method during initialization.
     * The mapping between key codes and game actions will not change during the execution of the application.
     */
    public int getKeyCode(int gameAction){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets an informative key string for a key. The string returned will resemble the text physically printed on the key. This string is suitable for displaying to the user. For example, on a device with function keys F1 through F4, calling this method on the keyCode for the F1 key will return the string
     * F1
     * . A typical use for this string will be to compose help text such as
     * Press F1 to proceed.
     * This method will return a non-empty string for every valid key code.
     * There is no direct mapping from game actions to key names. To get the string name for a game action GAME_A, the application must call
     */
    public java.lang.String getKeyName(int keyCode){
        return null; //TODO codavaj!!
    }

    /**
     * Checks if the platform supports pointer press and release events.
     */
    public boolean hasPointerEvents(){
        return false; //TODO codavaj!!
    }

    /**
     * Checks if the platform supports pointer motion events (pointer dragged). Applications may use this method to determine if the platform is capable of supporting motion events.
     */
    public boolean hasPointerMotionEvents(){
        return false; //TODO codavaj!!
    }

    /**
     * Checks if the platform can generate repeat events when key is kept down.
     */
    public boolean hasRepeatEvents(){
        return false; //TODO codavaj!!
    }

    /**
     * The implementation calls hideNotify() shortly after the Canvas has been removed from the display. Canvas subclasses may override this method in order to pause animations, revoke timers, etc. The default implementation of this method in class Canvas is empty.
     */
    protected void hideNotify(){
        return; //TODO codavaj!!
    }

    /**
     * Checks if the Canvas is double buffered by the implementation.
     */
    public boolean isDoubleBuffered(){
        return false; //TODO codavaj!!
    }

    /**
     * Called when a key is pressed.
     * The getGameAction() method can be called to determine what game action, if any, is mapped to the key. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void keyPressed(int keyCode){
        return; //TODO codavaj!!
    }

    /**
     * Called when a key is released.
     * The getGameAction() method can be called to determine what game action, if any, is mapped to the key. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void keyReleased(int keyCode){
        return; //TODO codavaj!!
    }

    /**
     * Called when a key is repeated (held down).
     * The getGameAction() method can be called to determine what game action, if any, is mapped to the key. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void keyRepeated(int keyCode){
        return; //TODO codavaj!!
    }

    /**
     * Renders the Canvas. The application must implement this method in order to paint any graphics.
     * The Graphics object's clip region defines the area of the screen that is considered to be invalid. A correctly-written paint() routine must paint every pixel within this region. This is necessary because the implementation is not required to clear the region prior to calling paint() on it. Thus, failing to paint every pixel may result in a portion of the previous screen image remaining visible.
     * Applications must not assume that they know the underlying source of the paint() call and use this assumption to paint only a subset of the pixels within the clip region. The reason is that this particular paint() call may have resulted from multiple repaint() requests, some of which may have been generated from outside the application. An application that paints only what it thinks is necessary to be painted may display incorrectly if the screen contents had been invalidated by, for example, an incoming telephone call.
     * Operations on this graphics object after the paint() call returns are undefined. Thus, the application must not cache this Graphics object for later use or use by another thread. It must only be used within the scope of this method.
     * The implementation may postpone visible effects of graphics operations until the end of the paint method.
     * The contents of the Canvas are never saved if it is hidden and then is made visible again. Thus, shortly after showNotify() is called, paint() will always be called with a Graphics object whose clip region specifies the entire displayable area of the Canvas. Applications must not rely on any contents being preserved from a previous occasion when the Canvas was current. This call to paint() will not necessarily occur before any other key or pointer methods are called on the Canvas. Applications whose repaint recomputation is expensive may create an offscreen Image, paint into it, and then draw this image on the Canvas when paint() is called.
     * The application code must never call paint(); it is called only by the implementation.
     * The Graphics object passed to the paint() method has the following properties:
     * the destination is the actual display, or if double buffering is in effect, a back buffer for the display; the clip region includes at least one pixel within this Canvas; the current color is black; the font is the same as the font returned by
     * ; the stroke style is
     * ; the origin of the coordinate system is located at the upper-left corner of the Canvas; and the Canvas is visible, that is, a call to isShown() will return true.
     */
    protected abstract void paint(Graphics g);

    /**
     * Called when the pointer is dragged.
     * The hasPointerMotionEvents() method may be called to determine if the device supports pointer events. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void pointerDragged(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Called when the pointer is pressed.
     * The hasPointerEvents() method may be called to determine if the device supports pointer events. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void pointerPressed(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Called when the pointer is released.
     * The hasPointerEvents() method may be called to determine if the device supports pointer events. Class Canvas has an empty implementation of this method, and the subclass has to redefine it if it wants to listen this method.
     */
    protected void pointerReleased(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Requests a repaint for the entire Canvas. The effect is identical to
     * repaint(0, 0, getWidth(), getHeight());
     */
    public final void repaint(){
        return; //TODO codavaj!!
    }

    /**
     * Requests a repaint for the specified region of the Canvas. Calling this method may result in subsequent call to paint(), where the passed Graphics object's clip region will include at least the specified region.
     * If the canvas is not visible, or if width and height are zero or less, or if the rectangle does not specify a visible region of the display, this call has no effect.
     * The call to paint() occurs asynchronously of the call to repaint(). That is, repaint() will not block waiting for paint() to finish. The paint() method will either be called after the caller of repaint() returns to the implementation (if the caller is a callback) or on another thread entirely.
     * To synchronize with its paint() routine, applications can use either Display.callSerially() or serviceRepaints(), or they can code explicit synchronization into their paint() routine.
     * The origin of the coordinate system is above and to the left of the pixel in the upper left corner of the displayable area of the Canvas. The X-coordinate is positive right and the Y-coordinate is positive downwards.
     */
    public final void repaint(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Forces any pending repaint requests to be serviced immediately. This method blocks until the pending requests have been serviced. If there are no pending repaints, or if this canvas is not visible on the display, this call does nothing and returns immediately.
     * Warning: This method blocks until the call to the application's paint() method returns. The application has no control over which thread calls paint(); it may vary from implementation to implementation. If the caller of serviceRepaints() holds a lock that the paint() method acquires, this may result in deadlock. Therefore, callers of serviceRepaints() must not hold any locks that might be acquired within the paint() method. The Display.callSerially() method provides a facility where an application can be called back after painting has completed, avoiding the danger of deadlock.
     */
    public final void serviceRepaints(){
        return; //TODO codavaj!!
    }

    /**
     * Controls whether the Canvas is in full-screen mode or in normal mode.
     */
    public void setFullScreenMode(boolean mode){
        return; //TODO codavaj!!
    }

    /**
     * The implementation calls showNotify() immediately prior to this Canvas being made visible on the display. Canvas subclasses may override this method to perform tasks before being shown, such as setting up animations, starting timers, etc. The default implementation of this method in class Canvas is empty.
     */
    protected void showNotify(){
        return; //TODO codavaj!!
    }

    /**
     * Called when the drawable area of the Canvas has been changed. This method has augmented semantics compared to
     * .
     * In addition to the causes listed in Displayable.sizeChanged, a size change can occur on a Canvas because of a change between normal and full-screen modes.
     * If the size of a Canvas changes while it is actually visible on the display, it may trigger an automatic repaint request. If this occurs, the call to sizeChanged will occur prior to the call to paint. If the Canvas has become smaller, the implementation may choose not to trigger a repaint request if the remaining contents of the Canvas have been preserved. Similarly, if the Canvas has become larger, the implementation may choose to trigger a repaint only for the new region. In both cases, the preserved contents must remain stationary with respect to the origin of the Canvas. If the size change is significant to the contents of the Canvas, the application must explicitly issue a repaint request for the changed areas. Note that the application's repaint request should not cause multiple repaints, since it can be coalesced with repaint requests that are already pending.
     * If the size of a Canvas changes while it is not visible, the implementation may choose to delay calls to sizeChanged until immediately prior to the call to showNotify. In that case, there will be only one call to sizeChanged, regardless of the number of size changes.
     * An application that is sensitive to size changes can update instance variables in its implementation of sizeChanged. These updated values will be available to the code in the showNotify, hideNotify, and paint methods.
     */
    protected void sizeChanged(int w, int h){
        return; //TODO codavaj!!
    }

}
