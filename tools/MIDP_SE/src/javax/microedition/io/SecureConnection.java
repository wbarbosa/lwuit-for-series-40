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
 * This interface defines the secure socket stream connection. A secure connection is established using Connector.open with the scheme "ssl" and the secure connection is established before open returns. If the secure connection cannot be established due to errors related to certificates a CertificateException is thrown.
 * A secure socket is accessed using a generic connection string with an explicit host and port number. The host may be specified as a fully qualified host name or IPv4 number. e.g. ssl://host.com:79 defines a target socket on the host.com system at port 79.
 * Note that RFC1900 recommends the use of names rather than IP numbers for best results in the event of IP number reassignment.
 * A secure connection MUST be implemented by one or more of the following specifications: TLS Protocol Version 1.0 as specified in RFC 2246. SSL V3 as specified in The SSL Protocol Version 3.0 WAP(TM) TLS Profile and Tunneling Specification as specified in WAP-219-TLS-20010411-a
 * The URI must conform to the BNF syntax specified below. If the URI does not conform to this syntax, an IllegalArgumentException is thrown.
 * The following examples show how a SecureConnection would be used to access a sample loopback program.
 * Since: MIDP 2.0
 */
public interface SecureConnection extends SocketConnection{
    /**
     * Return the security information associated with this connection when it was opened.
     */
    public abstract SecurityInfo getSecurityInfo() throws java.io.IOException;

}
