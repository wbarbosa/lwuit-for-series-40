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
 * This interface defines the capabilities that a datagram connection must have.
 * Reminder: Since the CLDC Specification does not define any actual network protocol implementations, the syntax for datagram addressing is not defined in the CLDC Specification. Rather, syntax definition takes place at the level of J2ME profiles such as MIDP.
 * In the sample implementation that is provided as part of the CLDC reference implementation, the following addressing scheme is used:
 * The parameter string describing the target of a connection in the CLDC reference implementation takes the following form: {protocol}://[{host}]:[{port}] A datagram connection can be opened in a "client" mode or "server" mode. If the "//{host}" part is missing then the connection is opened as a "server" (by "server", we mean that a client application initiates communication). When the "//{host}" part is specified, the connection is opened as a "client".
 * Examples:
 * A datagram connection for accepting datagrams datagram://:1234
 * A datagram connection for sending to a server: datagram://123.456.789.12:1234
 * Note that the port number in "server mode" (unspecified host name) is that of the receiving port. The port number in "client mode" (host name specified) is that of the target port. The reply-to port in both cases is never unspecified. In "server mode", the same port number is used for both receiving and sending. In "client mode", the reply-to port is always dynamically allocated.
 * Also note that the allocation of datagram objects is done in a more abstract way than in Java 2 Standard Edition (J2SE). Instead of providing a concrete DatagramPacket class, an abstract Datagram interface is provided. This is to allow a single platform to support several different datagram interfaces simultaneously. Datagram objects must be allocated by calling the newDatagram methods of the DatagramConnection object. The resulting object is defined using another interface type called javax.microedition.io.Datagram.
 * Since: CLDC 1.0
 */
public interface DatagramConnection extends Connection{
    /**
     * Get the maximum length a datagram can be. Maximum length determines the maximum size of the datagram that can be created using the newDatagram method, and the maximum size of the datagram that can be sent or received.
     */
    public abstract int getMaximumLength() throws java.io.IOException;

    /**
     * Get the nominal length of a datagram. Nominal length refers to the size of the datagram that is stored into the data buffer. Nominal length may be equal or less than the maximum length of the datagram.
     */
    public abstract int getNominalLength() throws java.io.IOException;

    /**
     * Create a new datagram object.
     */
    public abstract Datagram newDatagram(byte[] buf, int size) throws java.io.IOException;

    /**
     * Make a new datagram object.
     */
    public abstract Datagram newDatagram(byte[] buf, int size, java.lang.String addr) throws java.io.IOException;

    /**
     * Create a new datagram object.
     */
    public abstract Datagram newDatagram(int size) throws java.io.IOException;

    /**
     * Create a new datagram object.
     */
    public abstract Datagram newDatagram(int size, java.lang.String addr) throws java.io.IOException;

    /**
     * Receive a datagram. When this method returns, the internal buffer in the Datagram object is filled with the data received, starting at the location determined by the offset state variable, and the data is ready to be read using the methods of the DataInput interface.
     * This method blocks until a datagram is received. The internal length state variable in the Datagram object contains the length of the received datagram. If the received data is longer than the length of the internal buffer minus offset, data is truncated.
     * This method does not change the internal read/write state variable of the Datagram object. Use method Datagram.reset to change the pointer before reading if necessary.
     */
    public abstract void receive(Datagram dgram) throws java.io.IOException;

    /**
     * Send a datagram. The Datagram object includes the information indicating the data to be sent, its length, and the address of the receiver. The method sends length bytes starting at the current offset of the Datagram object, where length and offset are internal state variables of the Datagram object.
     */
    public abstract void send(Datagram dgram) throws java.io.IOException;

}
