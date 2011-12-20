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
 * RecordControl controls the recording of media from a Player. RecordControl records what's currently being played by the Player.
 * See Also:Player
 */
public interface RecordControl extends javax.microedition.media.Control{
    /**
     * Complete the current recording.
     * If the recording is in progress, commit will implicitly call stopRecord.
     * To record again after commit has been called, setRecordLocation or setRecordStream must be called.
     */
    abstract void commit() throws java.io.IOException;

    /**
     * Return the content type of the recorded media. The content type is given in the
     * .
     */
    abstract java.lang.String getContentType();

    /**
     * Erase the current recording.
     * If the recording is in progress, reset will implicitly call stopRecord.
     * Calling reset after commit will have no effect on the current recording.
     * If the Player that is associated with this RecordControl is closed, reset will be called implicitly.
     */
    abstract void reset() throws java.io.IOException;

    /**
     * Set the output location where the data will be recorded.
     * Whenever possible, the recording format is the same as the format of the input media. In some cases, the recording format may be different from the input format if the input format is not a recordable format, e.g. streaming media data. An application can query the recorded format by calling the getContentType method.
     */
    abstract void setRecordLocation(java.lang.String locator) throws java.io.IOException, javax.microedition.media.MediaException;

    /**
     * Set the record size limit. This limits the size of the recorded media to the number of bytes specified.
     * When recording is in progress, commit will be called implicitly in the following cases: Record size limit is reached If the requested size is less than the already recorded size No more space is available.
     * Once a record size limit has been set, it will remain so for future recordings until it is changed by another setRecordSizeLimit call.
     * To remove the record size limit, set it to Integer.MAX_VALUE. By default, the record size limit is not set.
     * Only positive values can be set. Zero or negative values are invalid and an IllegalArgumentException will be thrown.
     */
    abstract int setRecordSizeLimit(int size) throws javax.microedition.media.MediaException;

    /**
     * Set the output stream where the data will be recorded.
     * Whenever possible, the recording format is the same as the format of the input media. In some cases, the recording format may be different from the input format if the input format is not a recordable format, e.g. streaming media data. An application can query the recorded format by calling the getContentType method.
     */
    abstract void setRecordStream(java.io.OutputStream stream);

    /**
     * Start recording the media.
     * If the Player is already started, startRecord will immediately start the recording. If the Player is not already started, startRecord will not record any media. It will put the recording in a "standby" mode. As soon as the Player is started, the recording will start right away.
     * If startRecord is called when the recording has already started, it will be ignored.
     * When startRecord returns, the recording has started and a RECORD_STARTED event will be delivered through the PlayerListener.
     * If an error occurs while recording is in progress, RECORD_ERROR event will be delivered via the PlayerListener.
     */
    abstract void startRecord();

    /**
     * Stop recording the media. stopRecord will not automatically stop the Player. It only stops the recording.
     * Stopping the Player does not imply a stopRecord. Rather, the recording will be put into a "standby" mode. Once the Player is re-started, the recording will resume automatically.
     * After stopRecord, startRecord can be called to resume the recording.
     * If stopRecord is called when the recording has already stopped, it will be ignored.
     * When stopRecord returns, the recording has stopped and a RECORD_STOPPED event will be delivered through the PlayerListener.
     */
    abstract void stopRecord();

}
