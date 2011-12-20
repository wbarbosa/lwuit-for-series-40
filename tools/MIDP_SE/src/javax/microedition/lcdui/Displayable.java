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
 * An object that has the capability of being placed on the display. A Displayable object may have a title, a ticker, zero or more commands and a listener associated with it. The contents displayed and their interaction with the user are defined by subclasses.
 * The title string may contain line breaks. The display of the title string must break accordingly. For example, if only a single line is available for a title and the string contains a line break then only the characters up to the line break are displayed.
 * Unless otherwise specified by a subclass, the default state of newly created Displayable objects is as follows:
 * Since: MIDP 1.0
 */
public abstract class Displayable{
    /**
     * Adds a command to the Displayable. The implementation may choose, for example, to add the command to any of the available soft buttons or place it in a menu. If the added command is already in the screen (tested by comparing the object references), the method has no effect. If the Displayable is actually visible on the display, and this call affects the set of visible commands, the implementation should update the display as soon as it is feasible to do so.
     */
    public void addCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Gets the height in pixels of the displayable area available to the application. The value returned is appropriate for the particular Displayable subclass. This value may depend on how the device uses the display and may be affected by the presence of a title, a ticker, or commands. This method returns the proper result at all times, even if the Displayable object has not yet been shown.
     */
    public int getHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the ticker used by this Displayable.
     */
    public Ticker getTicker(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the title of the Displayable. Returns null if there is no title.
     */
    public java.lang.String getTitle(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the width in pixels of the displayable area available to the application. The value returned is appropriate for the particular Displayable subclass. This value may depend on how the device uses the display and may be affected by the presence of a title, a ticker, or commands. This method returns the proper result at all times, even if the Displayable object has not yet been shown.
     */
    public int getWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Checks if the Displayable is actually visible on the display. In order for a Displayable to be visible, all of the following must be true: the Display's MIDlet must be running in the foreground, the Displayable must be the Display's current screen, and the Displayable must not be obscured by a
     * .
     */
    public boolean isShown(){
        return false; //TODO codavaj!!
    }

    /**
     * Removes a command from the Displayable. If the command is not in the Displayable (tested by comparing the object references), the method has no effect. If the Displayable is actually visible on the display, and this call affects the set of visible commands, the implementation should update the display as soon as it is feasible to do so. If cmd is null, this method does nothing.
     */
    public void removeCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Sets a listener for
     * to this Displayable, replacing any previous CommandListener. A null reference is allowed and has the effect of removing any existing listener.
     */
    public void setCommandListener(CommandListener l){
        return; //TODO codavaj!!
    }

    /**
     * Sets a ticker for use with this Displayable, replacing any previous ticker. If null, removes the ticker object from this Displayable. The same ticker may be shared by several Displayable objects within an application. This is done by calling setTicker() with the same Ticker object on several different Displayable objects. If the Displayable is actually visible on the display, the implementation should update the display as soon as it is feasible to do so.
     * The existence of a ticker may affect the size of the area available for Displayable's contents. Addition, removal, or the setting of the ticker at runtime may dynamically change the size of the content area. This is most important to be aware of when using the Canvas class. If the available area does change, the application will be notified via a call to sizeChanged().
     */
    public void setTicker(Ticker ticker){
        return; //TODO codavaj!!
    }

    /**
     * Sets the title of the Displayable. If null is given, removes the title.
     * If the Displayable is actually visible on the display, the implementation should update the display as soon as it is feasible to do so.
     * The existence of a title may affect the size of the area available for Displayable content. Addition, removal, or the setting of the title text at runtime may dynamically change the size of the content area. This is most important to be aware of when using the Canvas class. If the available area does change, the application will be notified via a call to sizeChanged().
     */
    public void setTitle(java.lang.String s){
        return; //TODO codavaj!!
    }

    /**
     * The implementation calls this method when the available area of the Displayable has been changed. The
     * available area
     * is the area of the display that may be occupied by the application's contents, such as Items in a Form or graphics within a Canvas. It does not include space occupied by a title, a ticker, command labels, scroll bars, system status area, etc. A size change can occur as a result of the addition, removal, or changed contents of any of these display features.
     * This method is called at least once before the Displayable is shown for the first time. If the size of a Displayable changes while it is visible, sizeChanged will be called. If the size of a Displayable changes while it is not visible, calls to sizeChanged may be deferred. If the size had changed while the Displayable was not visible, sizeChanged will be called at least once at the time the Displayable becomes visible once again.
     * The default implementation of this method in Displayable and its subclasses defined in this specification must be empty. This method is intended solely for being overridden by the application. This method is defined on Displayable even though applications are prohibited from creating direct subclasses of Displayable. It is defined here so that applications can override it in subclasses of Canvas and Form. This is useful for Canvas subclasses to tailor their graphics and for Forms to modify Item sizes and layout directives in order to fit their contents within the the available display area.
     */
    protected void sizeChanged(int w, int h){
        return; //TODO codavaj!!
    }

}
