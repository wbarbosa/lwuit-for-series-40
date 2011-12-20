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
 * This interface defines the socket stream connection.
 * A socket is accessed using a generic connection string with an explicit host and port number. The host may be specified as a fully qualified host name or IPv4 number. e.g. socket://host.com:79 defines a target socket on the host.com system at port 79.
 * Note that RFC1900 recommends the use of names rather than IP numbers for best results in the event of IP number reassignment.
 * Every StreamConnection provides a Connection object as well as an InputStream and OutputStream to handle the I/O associated with the connection. Each of these interfaces has its own close() method. For systems that support duplex communication over the socket connection, closing of the input or output stream SHOULD shutdown just that side of the connection. e.g. closing the InputStream will permit the OutputStream to continue sending data.
 * Once the input or output stream has been closed, it can only be reopened with a call to Connector.open(). The application will receive an IOException if an attempt is made to reopen the stream.
 * The URI must conform to the BNF syntax specified below. If the URI does not conform to this syntax, an IllegalArgumentException is thrown.
 * The following examples show how a SocketConnection would be used to access a sample loopback program.
 * Since: MIDP 2.0
 */
public interface SocketConnection extends StreamConnection{
    /**
     * Socket option for the small buffer writing delay (0). Set to zero to disable Nagle algorithm for small buffer operations. Set to a non-zero value to enable.
     * See Also:Constant Field Values
     */
    public static final byte DELAY=0;

    /**
     * Socket option for the keep alive feature (2). Setting KEEPALIVE to zero will disable the feature. Setting KEEPALIVE to a non-zero value will enable the feature.
     * See Also:Constant Field Values
     */
    public static final byte KEEPALIVE=2;

    /**
     * Socket option for the linger time to wait in seconds before closing a connection with pending data output (1). Setting the linger time to zero disables the linger wait interval.
     * See Also:Constant Field Values
     */
    public static final byte LINGER=1;

    /**
     * Socket option for the size of the receiving buffer (3).
     * See Also:Constant Field Values
     */
    public static final byte RCVBUF=3;

    /**
     * Socket option for the size of the sending buffer (4).
     * See Also:Constant Field Values
     */
    public static final byte SNDBUF=4;

    /**
     * Gets the remote address to which the socket is bound. The address can be either the remote host name or the IP address(if available).
     */
    public abstract java.lang.String getAddress() throws java.io.IOException;

    /**
     * Gets the local address to which the socket is bound.
     * The host address(IP number) that can be used to connect to this end of the socket connection from an external system. Since IP addresses may be dynamically assigned, a remote application will need to be robust in the face of IP number reasssignment.
     * The local hostname (if available) can be accessed from System.getProperty("microedition.hostname")
     */
    public abstract java.lang.String getLocalAddress() throws java.io.IOException;

    /**
     * Returns the local port to which this socket is bound.
     */
    public abstract int getLocalPort() throws java.io.IOException;

    /**
     * Returns the remote port to which this socket is bound.
     */
    public abstract int getPort() throws java.io.IOException;

    /**
     * Get a socket option for the connection.
     */
    public abstract int getSocketOption(byte option) throws java.lang.IllegalArgumentException, java.io.IOException;

    /**
     * Set a socket option for the connection.
     * Options inform the low level networking code about intended usage patterns that the application will use in dealing with the socket connection.
     * Calling setSocketOption to assign buffer sizes is a hint to the platform of the sizes to set the underlying network I/O buffers. Calling getSocketOption can be used to see what sizes the system is using. The system MAY adjust the buffer sizes to account for better throughput available from Maximum Transmission Unit (MTU) and Maximum Segment Size (MSS) data available from current network information.
     */
    public abstract void setSocketOption(byte option, int value) throws java.lang.IllegalArgumentException, java.io.IOException;

}
