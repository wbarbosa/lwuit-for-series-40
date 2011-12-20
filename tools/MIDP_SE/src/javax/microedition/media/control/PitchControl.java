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
 * PitchControl raises or lowers the playback pitch of audio without changing the playback speed.
 * PitchControl can be implemented in Players for MIDI media or sampled audio. It is not possible to set audible output to an absolute pitch value. This control raises or lowers pitch relative to the original.
 * The pitch change is specified in number of milli- semitones to raise the pitch. As an example, specifying a pitch of 12'000 results in playback one octave higher. For MIDI that means that all MIDI notes are raised by 12 (semitones). For sampled audio playback, it means doubling the frequency of perceived sounds (i.e. a 440Hz sound will become a 880Hz sound.). Negative values are used to lower the pitch.
 * All Players by default support 0, or no pitch change. A Player which supports only 0 pitch change must not implement PitchControl.
 * PitchControl does not influence playback volume in any way.
 * See Also:Player, RateControl, TempoControl
 */
public interface PitchControl extends javax.microedition.media.Control{
    /**
     * Gets the maximum playback pitch raise supported by the Player.
     */
    abstract int getMaxPitch();

    /**
     * Gets the minimum playback pitch raise supported by the Player.
     */
    abstract int getMinPitch();

    /**
     * Gets the current playback pitch raise.
     */
    abstract int getPitch();

    /**
     * Sets the relative pitch raise. The pitch change is specified in
     * milli- semitones
     * , i.e. 1000 times the number of semitones to raise the pitch. Negative values lower the pitch by the number of milli-semitones.
     * The setPitch() method returns the actual pitch change set by the Player. Players should set their pitch raise as close to the requested value as possible, but are not required to set it to the exact value of any argument other than 0. A Player is only guaranteed to set its pitch change exactly to 0. If the given pitch raise is less than the value returned by getMinPitch or greater than the value returned by getMaxPitch, it will be adjusted to the minimum or maximum supported pitch raise respectively.
     */
    abstract int setPitch(int millisemitones);

}
