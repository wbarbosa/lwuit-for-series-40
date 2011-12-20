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
 * Implements a ticker-tape, a piece of text that runs continuously across the display. The direction and speed of scrolling are determined by the implementation. While animating, the ticker string scrolls continuously. That is, when the string finishes scrolling off the display, the ticker starts over at the beginning of the string.
 * There is no API provided for starting and stopping the ticker. The application model is that the ticker is always scrolling continuously. However, the implementation is allowed to pause the scrolling for power consumption purposes, for example, if the user doesn't interact with the device for a certain period of time. The implementation should resume scrolling the ticker when the user interacts with the device again.
 * The text of the ticker may contain line breaks. The complete text MUST be displayed in the ticker; line break characters should not be displayed but may be used as separators.
 * The same ticker may be shared by several Displayable objects (screens). This can be accomplished by calling setTicker() on each of them. Typical usage is for an application to place the same ticker on all of its screens. When the application switches between two screens that have the same ticker, a desirable effect is for the ticker to be displayed at the same location on the display and to continue scrolling its contents at the same position. This gives the illusion of the ticker being attached to the display instead of to each screen.
 * An alternative usage model is for the application to use different tickers on different sets of screens or even a different one on each screen. The ticker is an attribute of the Displayable class so that applications may implement this model without having to update the ticker to be displayed as the user switches among screens.
 * Since: MIDP 1.0
 */
public class Ticker{
    /**
     * Constructs a new Ticker object, given its initial contents string.
     * str - string to be set for the Ticker
     * - if str is null
     */
    public Ticker(java.lang.String str){
         //TODO codavaj!!
    }

    /**
     * Gets the string currently being scrolled by the ticker.
     */
    public java.lang.String getString(){
        return null; //TODO codavaj!!
    }

    /**
     * Sets the string to be displayed by this ticker. If this ticker is active and is on the display, it immediately begins showing the new string.
     */
    public void setString(java.lang.String str){
        return; //TODO codavaj!!
    }

}
