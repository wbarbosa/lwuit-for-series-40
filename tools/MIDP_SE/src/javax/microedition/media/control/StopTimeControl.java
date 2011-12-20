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
 * StopTimeControl allows one to specify a preset stop time for a Player.
 */
public interface StopTimeControl extends javax.microedition.media.Control{
    /**
     * Returned by getStopTime if no stop-time is set.
     * Value Long.MAX_VALUE is assigned to RESET.
     * See Also:Constant Field Values
     */
    static final long RESET=9223372036854775807l;

    /**
     * Gets the last value successfully set by setStopTime. Returns the constant RESET if no stop time is set. This is the default.
     */
    abstract long getStopTime();

    /**
     * Sets the
     * at which you want the Player to stop. The Player will stop when its
     * reaches the stop-time. A STOPPED_AT_TIME event will be delivered through the PlayerListener.
     * The Player is guaranteed to stop within one second past the preset stop-time (i.e. stop-time <= current-media-time <= stop-time + 1 sec.); unless the current media time is already passed the preset stop time when the stop time is set. If the current media time is already past the stop time set, the Player will stop immediately. A STOPPED_AT_TIME event will be delivered. After the Player stops due to the stop-time set, the previously set stop-time will be cleared automatically. Alternatively, the stop time can be explicitly removed by setting it to: RESET.
     * You can always call setStopTime on a stopped Player. To avoid a potential race condition, it is illegal to call setStopTime on a started Player if a media stop-time has already been set.
     */
    abstract void setStopTime(long stopTime);

}
