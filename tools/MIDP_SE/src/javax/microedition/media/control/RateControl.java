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
 * RateControl controls the playback rate of a Player.
 * The rate defines the relationship between the Player'smedia time and its TimeBase. Rates are specified in milli- percentage.
 * For example, a rate of 200'000 indicates that media time will pass twice as fast as the TimeBase time once the Player starts. Similarly, a negative rate indicates that the Player runs in the opposite direction of its TimeBase, i.e. playing in reverse.
 * All Player must support the default rate 100'000. Players that support only the default rate must not implement this interface. Players that support other rates besides 100'000, should implement this interface and specify the appropriate minimum and maximum playback rates.
 * For audio, specific implementations may change the playback pitch when changing the playback rate. This may be viewed as an undesirable side-effect. See PitchControl for changing pitch without changing playback rate.
 * See Also:Player, TempoControl, PitchControl
 */
public interface RateControl extends javax.microedition.media.Control{
    /**
     * Gets the maximum playback rate supported by the Player.
     */
    abstract int getMaxRate();

    /**
     * Gets the minimum playback rate supported by the Player.
     */
    abstract int getMinRate();

    /**
     * Gets the current playback rate.
     */
    abstract int getRate();

    /**
     * Sets the playback rate. The specified rate is 1000 times the percentage of the actual rate. For example, to play back at twice the speed, specify a rate of 200'000.
     * The setRate method returns the actual rate set by the Player. Player should set their rate as close to the requested value as possible, but are not required to set the rate to the exact value of any argument other than 100'000. A Player is only guaranteed to set its rate exactly to 100'000. If the given rate is less than getMinRate or greater than getMaxRate, the rate will be adjusted to the minimum or maximum supported rate respectively.
     * If the Player is already started, setRate will immediately take effect.
     */
    abstract int setRate(int millirate);

}
