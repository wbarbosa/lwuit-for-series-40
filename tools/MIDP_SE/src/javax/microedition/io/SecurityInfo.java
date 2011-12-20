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
 * This interface defines methods to access information about a secure network connection. Protocols that implement secure connections may use this interface to report the security parameters of the connection.
 * It provides the certificate, protocol, version, and cipher suite, etc. in use.
 * Since: MIDP 2.0 See Also:CertificateException, SecureConnection, HttpsConnection
 */
public interface SecurityInfo{
    /**
     * Returns the name of the cipher suite in use for the connection. The name returned is from the CipherSuite column of the CipherSuite definitions table in Appendix C of RFC 2246. If the cipher suite is not in Appendix C, the name returned is non-null and its contents are not specified. For non-TLS implementions the cipher suite name should be selected according to the actual key exchange, cipher, and hash combination used to establish the connection, so that regardless of whether the secure connection uses SSL V3 or TLS 1.0 or WTLS or WAP TLS Profile and Tunneling, equivalent cipher suites have the same name.
     */
    public abstract java.lang.String getCipherSuite();

    /**
     * Returns the secure protocol name.
     */
    public abstract java.lang.String getProtocolName();

    /**
     * Returns the protocol version. If appropriate, it should contain the major and minor versions for the protocol separated with a "." (Unicode U+002E). For SSL V3 it MUST return "3.0" For TLS 1.0 it MUST return "3.1" For WTLS (WAP-199) it MUST return "1" For WAP TLS Profile and Tunneling Specification it MUST return "3.1"
     */
    public abstract java.lang.String getProtocolVersion();

    /**
     * Returns the Certificate used to establish the secure connection with the server.
     */
    //public abstract pki.Certificate getServerCertificate();

}
