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
 * The CertificateException encapsulates an error that occurred while a Certificate is being used. If multiple errors are found within a Certificate the more significant error should be reported in the exception.
 * Since: MIDP 2.0
 */
public class CertificateException extends java.io.IOException{
    /**
     * Indicates a certificate has unrecognized critical extensions. The value is 1.
     * See Also:Constant Field Values
     */
    public static final byte BAD_EXTENSIONS=1;

    /**
     * Indicates a certificate in a chain was not issued by the next authority in the chain. The value is 11.
     * See Also:Constant Field Values
     */
    public static final byte BROKEN_CHAIN=11;

    /**
     * Indicates the server certificate chain exceeds the length allowed by an issuer's policy. The value is 2.
     * See Also:Constant Field Values
     */
    public static final byte CERTIFICATE_CHAIN_TOO_LONG=2;

    /**
     * Indicates a certificate is expired. The value is 3.
     * See Also:Constant Field Values
     */
    public static final byte EXPIRED=3;

    /**
     * Indicates a certificate public key has been used in way deemed inappropriate by the issuer. The value is 10.
     * See Also:Constant Field Values
     */
    public static final byte INAPPROPRIATE_KEY_USAGE=10;

    /**
     * Indicates a certificate object does not contain a signature. The value is 5.
     * See Also:Constant Field Values
     */
    public static final byte MISSING_SIGNATURE=5;

    /**
     * Indicates a certificate is not yet valid. The value is 6.
     * See Also:Constant Field Values
     */
    public static final byte NOT_YET_VALID=6;

    /**
     * Indicates the root CA's public key is expired. The value is 12.
     * See Also:Constant Field Values
     */
    public static final byte ROOT_CA_EXPIRED=12;

    /**
     * Indicates a certificate does not contain the correct site name. The value is 7.
     * See Also:Constant Field Values
     */
    public static final byte SITENAME_MISMATCH=7;

    /**
     * Indicates an intermediate certificate in the chain does not have the authority to be a intermediate CA. The value is 4.
     * See Also:Constant Field Values
     */
    public static final byte UNAUTHORIZED_INTERMEDIATE_CA=4;

    /**
     * Indicates a certificate was issued by an unrecognized entity. The value is 8.
     * See Also:Constant Field Values
     */
    public static final byte UNRECOGNIZED_ISSUER=8;

    /**
     * Indicates that type of the public key in a certificate is not supported by the device. The value is 13.
     * See Also:Constant Field Values
     */
    public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE=13;

    /**
     * Indicates a certificate was signed using an unsupported algorithm. The value is 9.
     * See Also:Constant Field Values
     */
    public static final byte UNSUPPORTED_SIGALG=9;

    /**
     * Indicates a certificate failed verification. The value is 14.
     * See Also:Constant Field Values
     */
    public static final byte VERIFICATION_FAILED=14;

    /**
     * Create a new exception with a Certificate and specific error reason. The descriptive message for the new exception will be automatically provided, based on the reason.
     * certificate - the certificate that caused the exceptionstatus - the reason for the exception; the status MUST be between BAD_EXTENSIONS and VERIFICATION_FAILED inclusive.
     */
    public CertificateException(Certificate certificate, byte status){
         //TODO codavaj!!
    }

    /**
     * Create a new exception with a message, Certificate, and specific error reason.
     * message - a descriptive messagecertificate - the certificate that caused the exceptionstatus - the reason for the exception; the status MUST be between BAD_EXTENSIONS and VERIFICATION_FAILED inclusive.
     */
    public CertificateException(java.lang.String message, Certificate certificate, byte status){
         //TODO codavaj!!
    }

    /**
     * Get the Certificate that caused the exception.
     */
    public Certificate getCertificate(){
        return null; //TODO codavaj!!
    }

    /**
     * Get the reason code.
     */
    public byte getReason(){
        return 0; //TODO codavaj!!
    }

}
