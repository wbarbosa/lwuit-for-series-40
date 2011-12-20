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
 * This interface defines the necessary methods and constants to establish a secure network connection. The URI format with scheme https when passed to Connector.open will return a HttpsConnection. RFC 2818 defines the scheme.
 * A secure connection MUST be implemented by one or more of the following specifications: HTTP over TLS as documented in RFC 2818 and TLS Protocol Version 1.0 as specified in RFC 2246. SSL V3 as specified in The SSL Protocol Version 3.0 WTLS as specified in WAP Forum Specifications June 2000 (WAP 1.2.1) conformance release - Wireless Transport Layer Security document WAP-199. WAP(TM) TLS Profile and Tunneling Specification as specified in WAP-219-TLS-20010411-a
 * HTTPS is the secure version of HTTP (IETF RFC2616), a request-response protocol in which the parameters of the request must be set before the request is sent.
 * In addition to the normal IOExceptions that may occur during invocation of the various methods that cause a transition to the Connected state, CertificateException (a subtype of IOException) may be thrown to indicate various failures related to establishing the secure link. The secure link is necessary in the Connected state so the headers can be sent securely. The secure link may be established as early as the invocation of Connector.open() and related methods for opening input and output streams and failure related to certificate exceptions may be reported.
 * Open a HTTPS connection, set its parameters, then read the HTTP response.
 * Since: MIDP 2.0 See Also:CertificateException
 */
public interface HttpsConnection extends HttpConnection{
    /**
     * Returns the network port number of the URL for this HttpsConnection.
     */
    public abstract int getPort();

    /**
     * Return the security information associated with this successfully opened connection. If the connection is still in Setup state then the connection is initiated to establish the secure connection to the server. The method returns when the connection is established and the Certificate supplied by the server has been validated. The SecurityInfo is only returned if the connection has been successfully made to the server.
     */
    public abstract SecurityInfo getSecurityInfo() throws java.io.IOException;

}
