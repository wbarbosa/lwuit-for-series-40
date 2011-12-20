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
 * TempoControl controls the tempo, in musical terms, of a song.
 * TempoControl is typically implemented in Players for MIDI media, i.e. playback of a Standard MIDI File (SMF).
 * TempoControl is basic functionality for a MIDI playback application. This is in contrast to MIDIControl, which targets advanced applications. Moreover, TempoControl needs a sequence - e.g. a MIDI file - to operate. MIDIControl does not require a sequence.
 * Musical tempo is usually specified in beats per minute. To provide a means to access tempos with fractional beats per minute, the methods to set and get the tempo work on milli-beat per minute. A simple division by 1000 is sufficient to get the actual beats per minute.
 * As a MIDI file can contain any number of tempo changes during playback, the absolute tempo is a state of the sequencer. During playback of a MIDI file, setting the tempo in response to a user interaction will not always yield the desired result: the user's tempo can be overridden by the playing MIDI file to another tempo just moments later. In order to overcome this problem, a relative tempo rate is used (in Java Sound terms: tempo factor). This rate is applied to all tempo settings. The tempo rate is specified in milli-percent, i.e. a value of 100'000 means playback at original tempo. The tempo rate is set with the setRate() method of the super class, RateControl.
 * The concept of tempo rate allows one to play back a MIDI sequence at a different tempo without losing the relative tempo changes in it.
 * The setTempo() and getTempo() methods do not affect or reflect the playback rate. This means that changing the rate will not result in a change of the value returned by getTempo(). Similarly, setting the tempo with setTempo() does not change the rate, i.e. the return value of getRate() is not changed. The effective playback tempo is always the product of tempo and rate: effectiveBeatsPerMinute = getTempo() getRate() / 1000 / 100000
 * See Also:Player, RateControl, PitchControl, MIDIControl
 */
public interface TempoControl extends javax.microedition.media.control.RateControl{
    /**
     * Gets the current playback tempo. This represents the current state of the sequencer: A sequencer may not be initialized before the Player is prefetched. An uninitialized sequencer in this case returns a default tempo of 120 beats per minute. After prefetching has finished, the tempo is set to the start tempo of the MIDI sequence (if any). During playback, the return value is the current tempo and varies with tempo events in the MIDI file A stopped sequence retains the last tempo it had before it was stopped. A call to setTempo() changes current tempo until a tempo event in the MIDI file is encountered.
     */
    abstract int getTempo();

    /**
     * Sets the current playback tempo. Tempo is a volatile state of the sequencer. As MIDI sequences may contain META tempo events, tempo may change during playback of the sequence. Setting the tempo with setTempo() does not prevent the tempo from being changed subsequently by tempo events in the MIDI sequence. Example: during playback of a sequence, the user changes the tempo. But just moments later, the MIDI sequence changes the tempo to another value, so effectively the user interaction is ignored. To overcome this, and to allow consistent user interaction, use
     * inherited from RateControl.
     * The setTempo() method returns the actual tempo set by the Player's implementation. It sets the tempo as close to the requested value as possible, but is not required to set it to the exact value. Specifically, implementations may have a lower or upper limit, which will be used as tempo if the requested tempo is out of limits. 0 or negative tempo does not exist and will always result in the lower tempo limit of the implementation. Implementations are guaranteed to support 10'000 to 300'000 milli-beats per minute.
     * Setting tempo to a stopped sequence will force the sequence to start with that tempo, even if the sequence has a tempo event at the start position. Any subsequent tempo events in the sequence will be considered, though. Rewinding back to a position with a tempo event will result in a tempo change caused by the tempo event, too. Example: a sequence with initial tempo of 120bpm has not been started yet. The user sets the tempo to 140bpm and starts playback. When the playback position is then reset to the beginning, the tempo will be set to 120bpm due to the tempo event at the beginning of the sequence.
     * Playback rate (see setRate()) and tempo are independent factors of the effective tempo. Modifying tempo with setTempo() does not affect the playback rate and vice versa. The effective tempo is the product of tempo and rate.
     */
    abstract int setTempo(int millitempo);

}
