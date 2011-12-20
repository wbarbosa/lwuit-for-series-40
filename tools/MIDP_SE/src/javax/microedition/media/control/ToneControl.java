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
 * ToneControl is the interface to enable playback of a user-defined monotonic tone sequence.
 * A tone sequence is specified as a list of tone-duration pairs and user-defined sequence blocks. The list is packaged as an array of bytes. The setSequence method is used to input the sequence to the ToneControl. In addition, the tone sequence format specified below can also be used as a file format to define tone sequences. A file containing a tone sequence as specified must use ".jts" as the file extension. "audio/x-tone-seq" designates the MIME type for this format.
 * The syntax of a tone sequence is described in Augmented BNF notations:
 * Following table shows the valid range of the parameters:
 * The duration of each tone is measured in units of 1/resolution notes and tempo is specified in beats/minute, where 1 beat = 1/4 note. Because the range of positive values of byte is only 1 - 127, the tempo is formed by multiplying the tempo modifier by 4. Very slow tempos are excluded so range of tempo modifiers is 5 - 127 providing an effective range of 20 - 508 bpm.
 * To compute the effective duration in milliseconds for a tone, the following formula can be used: duration * 60 * 1000 * 4 / (resolution * tempo) The following table lists some common durations in musical notes:
 */
public interface ToneControl extends javax.microedition.media.Control{
    /**
     * Defines an ending point for a block.
     * Value -6 is assigned to BLOCK_END.
     * See Also:Constant Field Values
     */
    static final byte BLOCK_END=-6;

    /**
     * Defines a starting point for a block.
     * Value -5 is assigned to BLOCK_START.
     * See Also:Constant Field Values
     */
    static final byte BLOCK_START=-5;

    /**
     * Middle C.
     * Value 60 is assigned to C4.
     * See Also:Constant Field Values
     */
    static final byte C4=60;

    /**
     * Play a defined block.
     * Value -7 is assigned to PLAY_BLOCK.
     * See Also:Constant Field Values
     */
    static final byte PLAY_BLOCK=-7;

    /**
     * The REPEAT event tag.
     * Value -9 is assigned to REPEAT.
     * See Also:Constant Field Values
     */
    static final byte REPEAT=-9;

    /**
     * The RESOLUTION event tag.
     * Value -4 is assigned to RESOLUTION.
     * See Also:Constant Field Values
     */
    static final byte RESOLUTION=-4;

    /**
     * The SET_VOLUME event tag.
     * Value -8 is assigned to SET_VOLUME.
     * See Also:Constant Field Values
     */
    static final byte SET_VOLUME=-8;

    /**
     * Silence.
     * Value -1 is assigned to SILENCE.
     * See Also:Constant Field Values
     */
    static final byte SILENCE=-1;

    /**
     * The TEMPO event tag.
     * Value -3 is assigned to TEMPO.
     * See Also:Constant Field Values
     */
    static final byte TEMPO=-3;

    /**
     * The VERSION attribute tag.
     * Value -2 is assigned to VERSION.
     * See Also:Constant Field Values
     */
    static final byte VERSION=-2;

    /**
     * Sets the tone sequence.
     */
    abstract void setSequence(byte[] sequence);

}
