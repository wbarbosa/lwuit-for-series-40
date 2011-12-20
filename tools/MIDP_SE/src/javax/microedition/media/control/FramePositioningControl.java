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
 * The FramePositioningControl is the interface to control precise positioning to a video frame for Players.
 * Frame numbers for a bounded movie must be non-negative and should generally begin with 0, corresponding to media time 0. Each video frame of a movie must have a unique frame number that is one bigger than the previous frame.
 * There is a direct mapping between the frame number and the media time of a video frame; although not all Players can compute that relationship. For Players that can compute that relationship, the mapFrameToTime and mapTimeToFrame methods can be used.
 * When a Player is seeked or skipped to a new video frame, the media time of the Player will be changed to the media time of the corresponding video frame.
 * As much as possible, the methods in this interface should provide frame-level accuracy with a plus-or-minus-one-frame margin of error to accommodate for round-off errors. However, if the content has inaccurate frame positioning information, implementations may not be able to provide the necessary frame-level accuracy. For instance, some media content may contain wrong time-stamps or have missing frames. In any case, the results of each operation should represent the best effort. For the seek and skip methods, the returned value should indicate the actual new location or the number of frames skipped.
 */
public interface FramePositioningControl extends javax.microedition.media.Control{
    /**
     * Converts the given frame number to the corresponding media time. The method only performs the calculations. It does not position the media to the given frame.
     */
    abstract long mapFrameToTime(int frameNumber);

    /**
     * Converts the given media time to the corresponding frame number. The method only performs the calculations. It does not position the media to the given media time.
     * The frame returned is the nearest frame that has a media time less than or equal to the given media time.
     * mapTimeToFrame(0) must not fail and must return the frame number of the first frame.
     */
    abstract int mapTimeToFrame(long mediaTime);

    /**
     * Seek to a given video frame. The media time of the Player will be updated to reflect the new position set.
     * This method can be called on a stopped or started Player. If the Player is in the Started state, this method may cause the Player to change states. If that happens, the appropriate transition events will be posted by the Player when its state changes.
     * If the given frame number is less than the first or larger than the last frame number in the media, seek will jump to either the first or the last frame respectively.
     */
    abstract int seek(int frameNumber);

    /**
     * Skip a given number of frames from the current position. The media time of the Player will be updated to reflect the new position set.
     * This method can be called on a stopped or started Player. If the Player is in the Started state, the current position is changing. Hence, the frame actually skipped to will not be exact.
     * If the Player is in the Started state, this method may cause the Player to change states. If that happens, the appropriate transition events will be posted.
     * If the given framesToSkip will cause the position to extend beyond the first or last frame, skip will jump to the first or last frame respectively.
     */
    abstract int skip(int framesToSkip);

}
