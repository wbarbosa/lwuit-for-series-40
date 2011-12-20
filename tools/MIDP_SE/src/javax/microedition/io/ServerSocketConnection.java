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
 * This interface defines the server socket stream connection.
 * A server socket is accessed using a generic connection string with the host omitted. For example, socket://:79 defines an inbound server socket on port 79. The host can be discovered using the getLocalAddress method.
 * The acceptAndOpen() method returns a SocketConnection instance. In addition to the normal StreamConnection behavior, the SocketConnection supports accessing the IP end point addresses of the live connection and access to socket options that control the buffering and timing delays associated with specific application usage of the connection.
 * Access to server socket connections may be restricted by the security policy of the device. Connector.open MUST check access for the initial server socket connection and acceptAndOpen MUST check before returning each new SocketConnection.
 * A server socket can be used to dynamically select an available port by omitting both the host and the port parameters in the connection URL string. For example, socket:// defines an inbound server socket on a port which is allocated by the system. To discover the assigned port number use the getLocalPort method.
 * The URI must conform to the BNF syntax specified below. If the URI does not conform to this syntax, an IllegalArgumentException is thrown.
 * The following examples show how a ServerSocketConnection would be used to access a sample loopback program.
 * Since: MIDP 2.0
 */
public interface ServerSocketConnection extends StreamConnectionNotifier{
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

}
