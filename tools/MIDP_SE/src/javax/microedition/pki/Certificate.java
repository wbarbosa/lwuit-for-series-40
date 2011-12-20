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

package javax.microedition.pki;
/**
 * Interface common to certificates. The features abstracted of Certificates include subject, issuer, type, version, serial number, signing algorithm, dates of valid use, and serial number.
 * Printable Representation for Binary Values
 * A non-string values in a certificate are represented as strings with each byte as two hex digits (capital letters for A-F) separated by ":" (Unicode U+003A).
 * For example: 0C:56:FA:80
 * Printable Representation for X.509 Distinguished Names
 * For a X.509 certificate the value returned is the printable verision of the distingished name (DN) from the certificate.
 * An X.509 distinguished name of is set of attributes, each attribute is a sequence of an object ID and a value. For string comparison purposes, the following rules define a strict printable representation.
 * There is no added white space around separators. The attributes are in the same order as in the certificate; attributes are not reordered. If an object ID is in the table below, the label from the table will be substituted for the object ID, else the ID is formatted as a string using the binary printable representation above. Each object ID or label and value within an attribute will be separated by a "=" (Unicode U+003D), even if the value is empty. If value is not a string, then it is formatted as a string using the binary printable representation above. Attributes will be separated by a ";" (Unicode U+003B)
 * Example of a printable distinguished name:
 * Since: MIDP 2.0
 */
public interface Certificate{
    /**
     * Gets the name of this certificate's issuer.
     */
    public abstract java.lang.String getIssuer();

    /**
     * Gets the time after which this Certificate may not be used from the validity period.
     */
    public abstract long getNotAfter();

    /**
     * Gets the time before which this Certificate may not be used from the validity period.
     */
    public abstract long getNotBefore();

    /**
     * Gets the printable form of the serial number of this Certificate. If the serial number within the certificate is binary it should be formatted as a string using the binary printable representation in class description. For example, 0C:56:FA:80.
     */
    public abstract java.lang.String getSerialNumber();

    /**
     * Gets the name of the algorithm used to sign the Certificate. The algorithm names returned should be the labels defined in RFC2459 Section 7.2.
     */
    public abstract java.lang.String getSigAlgName();

    /**
     * Gets the name of this certificate's subject.
     */
    public abstract java.lang.String getSubject();

    /**
     * Get the type of the Certificate. For X.509 Certificates the value returned is "X.509".
     */
    public abstract java.lang.String getType();

    /**
     * Gets the version number of this Certificate. The format of the version number depends on the specific type and specification. For a X.509 certificate per RFC 2459 it would be "2".
     */
    public abstract java.lang.String getVersion();

}
