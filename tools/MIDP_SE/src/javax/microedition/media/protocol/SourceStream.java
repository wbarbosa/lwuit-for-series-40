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

package javax.microedition.media.protocol;
/**
 * Abstracts a single stream of media data. It is used in conjunction with DataSource to provide the input interface to a Player
 * SourceStream may provide type-specific controls. For that reason, it implements the Controllable interface to provide additional controls.
 * See Also:DataSource
 */
public interface SourceStream extends javax.microedition.media.Controllable{
    /**
     * The value returned by getSeekType indicating that this SourceStream is not seekable.
     * Value 0 is assigned to NOT_SEEKABLE.
     * See Also:Constant Field Values
     */
    static final int NOT_SEEKABLE=0;

    /**
     * The value returned by getSeekType indicating that this SourceStream can be seeked anywhere within the media.
     * Value 2 is assigned to RANDOM_ACCESSIBLE.
     * See Also:Constant Field Values
     */
    static final int RANDOM_ACCESSIBLE=2;

    /**
     * The value returned by getSeekType indicating that this SourceStream can be seeked only to the beginning of the media stream.
     * Value 1 is assigned to SEEKABLE_TO_START.
     * See Also:Constant Field Values
     */
    static final int SEEKABLE_TO_START=1;

    /**
     * Get the content type for this stream.
     */
    abstract javax.microedition.media.protocol.ContentDescriptor getContentDescriptor();

    /**
     * Get the size in bytes of the content on this stream.
     */
    abstract long getContentLength();

    /**
     * Find out if the stream is seekable. The return value can be one of these three: NOT_SEEKABLE, SEEKABLE_TO_START and RANDOM_ACCESSIBLE. If the return value is SEEKABLE_TO_START, it means that the stream can only be repositioned to the beginning of the stream. If the return value is RANDOM_ACCESSIBLE, the stream can be seeked anywhere within the stream.
     */
    abstract int getSeekType();

    /**
     * Get the size of a "logical" chunk of media data from the source. This method can be used to determine the minimum size of the buffer to use in conjunction with the read method to read data from the source.
     */
    abstract int getTransferSize();

    /**
     * Reads up to len bytes of data from the input stream into an array of bytes. An attempt is made to read as many as len bytes, but a smaller number may be read. The number of bytes actually read is returned as an integer.
     * This method blocks until input data is available, end of file is detected, or an exception is thrown.
     * If b is null, a NullPointerException is thrown.
     * If off is negative, or len is negative, or off+len is greater than the length of the array b, then an IndexOutOfBoundsException is thrown.
     * If len is zero, then no bytes are read and 0 is returned; otherwise, there is an attempt to read at least one byte. If no byte is available because the stream is at end of file, the value -1 is returned; otherwise, at least one byte is read and stored into b.
     * The first byte read is stored into element b[off], the next one into b[off+1], and so on. The number of bytes read is, at most, equal to len. Let k be the number of bytes actually read; these bytes will be stored in elements b[off] through b[off+k-1], leaving elements b[off+k] through b[off+len-1] unaffected.
     * If the first byte cannot be read for any reason other than end of file, then an IOException is thrown. In particular, an IOException is thrown if the input stream has been closed.
     */
    abstract int read(byte[] b, int off, int len) throws java.io.IOException;

    /**
     * Seek to the specified point in the stream. The seek method may, for a variety of reasons, fail to seek to the specified position. For example, it may be asked to seek to a position beyond the size of the stream; or the stream may only be seekable to the beginning (getSeekType returns SEEKABLE_TO_START). The return value indicates whether the seeking is successful. If it is successful, the value returned will be the same as the given position. Otherwise, the return value will indicate what the new position is.
     * If the given position is negative, seek will treat that as 0 and attempt to seek to 0.
     * An IOException will be thrown if an I/O error occurs, e.g. when the stream comes from a remote connection and the connection is broken.
     */
    abstract long seek(long where) throws java.io.IOException;

    /**
     * Obtain the current position in the stream.
     */
    abstract long tell();

}
