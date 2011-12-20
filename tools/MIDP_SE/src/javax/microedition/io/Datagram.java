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

package javax.microedition.io;
/**
 * This class defines an abstract interface for datagram packets. The implementations of this interface hold data to be sent or received from a DatagramConnection object.
 * Since this is an interface class, the internal structure of the datagram packets is not defined here. However, it is assumed that each implementation of this interface will provide the following fields / state variables (the actual implementation and the names of these fields may vary): buffer: the internal buffer in which data is stored offset: the read/write offset for the internal buffer length: the length of the data in datagram packet address: the destination or source address read/write pointer: a pointer that is added to the offset to point to the current data location during a read or write operation
 * Reading and Writing
 * The Datagram interface extends interfaces DataInput and DataOutput in order to provide a simple way to read and write binary data in and out of the datagram buffer instead of using getData and setData methods. Writing automatically increments length and reading will continue while the read/write pointer is less than length. Before any writing is done reset must be called. If setData() is to be used when reading or writing, any value for the offset parameter other than 0 is not supported.
 * For example to write to datagram: datagram = connection.newDatagram(max); // Reset prepares the datagram for writing new message. datagram.reset(); // writeUTF automatically increases the datagram length. datagram.writeUTF("hello world"); connection.send(datagram); For example to read from a datagram (single use only): datagram = connection.newDatagram(max); connection.receive(datagram); message = datagram.readUTF(); Reusing Datagrams
 * It should be noted the length above is returned from getLength and can have different meanings at different times. When sending length is the number of bytes to send. Before receiving length is the maximum number of bytes to receive. After receiving length is the number of bytes that were received. So when reusing a datagram to receive after sending or receiving, length must be set back to the maximum using setLength. datagram = connection.newDatagram(max); while (notDone) { // The last receive in the loop changed the length // so put it back to the maximum length. datagram.setLength(max); connection.receive(datagram); data = datagram.getData(); bytesReceived = datagram.getLength(); // process datagram ... } When reading instead of using getData the reset method must be used. datagram = connection.newDatagram(max); while (notDone) { // The last read in the loop changed the read pointer // so reset the pointer. datagram.reset(); datagram.setLength(max); connection.receive(datagram); message = datagram.readUTF(message); // process message ... } For example to reread a datagram: connection.receive(datagram); message = datagram.readUTF(message); len = datagram.getLength(); datagram.reset(); datagram.setLength(len); copy = datagram.readUTF(message);
 * Since: CLDC 1.0
 */
public interface Datagram extends java.io.DataInput, java.io.DataOutput{
    /**
     * Get the address of the datagram.
     */
    public abstract java.lang.String getAddress();

    /**
     * Get the contents of the data buffer.
     * Depending on the implementation, this operation may return the internal buffer or a copy of it. However, the user must not assume that the contents of the internal data buffer can be manipulated by modifying the data returned by this operation. Rather, the setData operation should be used for changing the contents of the internal buffer.
     */
    public abstract byte[] getData();

    /**
     * Get the length of the datagram.
     */
    public abstract int getLength();

    /**
     * Get the offset.
     */
    public abstract int getOffset();

    /**
     * Zero the read/write pointer as well as the offset and length state variables.
     */
    public abstract void reset();

    /**
     * Set datagram address, copying the address from another datagram.
     */
    public abstract void setAddress(Datagram reference);

    /**
     * Set datagram address.
     * The actual addressing scheme is implementation-dependent. Please read the general comments on datagram addressing in DatagramConnection.java.
     * Note that if the address of a datagram is not specified, then it defaults to that of the connection.
     */
    public abstract void setAddress(java.lang.String addr) throws java.io.IOException;

    /**
     * Set the buffer, offset and length state variables. Depending on the implementation, this operation may copy the buffer or just set the state variable buffer to the value of the buffer argument. However, the user must not assume that the contents of the internal data buffer can be manipulated by modifying the buffer passed on to this operation.
     */
    public abstract void setData(byte[] buffer, int offset, int len);

    /**
     * Set the length state variable.
     */
    public abstract void setLength(int len);

}
