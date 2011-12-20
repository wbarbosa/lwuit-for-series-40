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

package javax.microedition.media.control;
/**
 * VolumeControl is an interface for manipulating the audio volume of a Player.
 * See Also:Control, Player, PlayerListener
 */
public interface VolumeControl extends javax.microedition.media.Control{
    /**
     * Get the current volume level set. getLevel may return -1 if and only if the Player is in the
     * state (the audio device has not been initialized) and setLevel has not yet been called.
     */
    abstract int getLevel();

    /**
     * Get the mute state of the signal associated with this VolumeControl.
     */
    abstract boolean isMuted();

    /**
     * Set the volume using a linear point scale with values between 0 and 100. 0 is silence; 100 is the loudest useful level that this VolumeControl supports. If the given level is less than 0 or greater than 100, the level will be set to 0 or 100 respectively. When setLevel results in a change in the volume level, a VOLUME_CHANGED event will be delivered through the PlayerListener.
     */
    abstract int setLevel(int level);

    /**
     * Mute or unmute the Player associated with this VolumeControl. Calling setMute(true) on the Player that is already muted is ignored, as is calling setMute(false) on the Player that is not currently muted. Setting mute on or off doesn't change the volume level returned by getLevel. When setMute results in a change in the muted state, a VOLUME_CHANGED event will be delivered through the PlayerListener.
     */
    abstract void setMute(boolean mute);

}
