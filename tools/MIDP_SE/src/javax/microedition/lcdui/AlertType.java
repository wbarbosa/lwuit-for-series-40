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
 * The AlertType provides an indication of the nature of alerts. Alerts are used by an application to present various kinds of information to the user. An AlertType may be used to directly signal the user without changing the current Displayable. The playSound method can be used to spontaneously generate a sound to alert the user. For example, a game using a Canvas can use playSound to indicate success or progress. The predefined types are INFO, WARNING, ERROR, ALARM, and CONFIRMATION.
 * Since: MIDP 1.0 See Also:Alert
 */
public class AlertType{
    /**
     * An ALARM AlertType is a hint to alert the user to an event for which the user has previously requested to be notified. For example, the message might say,
     * Staff meeting in five minutes.
     */
    public static final AlertType ALARM=null;

    /**
     * A CONFIRMATION AlertType is a hint to confirm user actions. For example,
     * Saved!
     * might be shown to indicate that a Save operation has completed.
     */
    public static final AlertType CONFIRMATION=null;

    /**
     * An ERROR AlertType is a hint to alert the user to an erroneous operation. For example, an error alert might show the message,
     * There is not enough room to install the application.
     */
    public static final AlertType ERROR=null;

    /**
     * An INFO AlertType typically provides non-threatening information to the user. For example, a simple splash screen might be an INFO AlertType.
     */
    public static final AlertType INFO=null;

    /**
     * A WARNING AlertType is a hint to warn the user of a potentially dangerous operation. For example, the warning message may contain the message,
     * Warning: this operation will erase your data.
     */
    public static final AlertType WARNING=null;

    /**
     * Protected constructor for subclasses.
     */
    protected AlertType(){
         //TODO codavaj!!
    }

    /**
     * Alert the user by playing the sound for this AlertType. The AlertType instance is used as a hint by the device to generate an appropriate sound. Instances other than those predefined above may be ignored. The actual sound made by the device, if any, is determined by the device. The device may ignore the request, use the same sound for several AlertTypes or use any other means suitable to alert the user.
     */
    public boolean playSound(Display display){
        return false; //TODO codavaj!!
    }

}
