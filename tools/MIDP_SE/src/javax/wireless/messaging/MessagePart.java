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

package javax.wireless.messaging;

import java.io.*;

/**
 * Instances of the <code>MessagePart</code> class can be added to a 
 * <code>MultipartMessage</code>. Each <code>MessagePart</code> consists
 * of the content element, MIME type and content-id. The Content can be of
 * any type. Additionally, it's possible to specify the content location and 
 * the encoding scheme.
 * @since WMA 2.0
 */
public class MessagePart {
 
    // TODO: allow maximum part size to be externally set
    /** Maximum size for message part. */
    static int MAX_PART_SIZE_BYTES = 30720; // 30K
    /**
     * Constructs a message.
     * @param contents byte array containing the contents for the
     *      <code>MessagePart</code>.
     * @param offset start position
     * @param length the number of bytes to be included in the 
     *      <code>MessagePart</code>.
     * @param mimeType the MIME Content-Type for the <code>MessagePart</code>
     *      [RFC 2046]
     * @param contentId the content-id header field value for the
     *      <code>MessagePart</code> [RFC 2045]. The content-id is unique 
     *      over all <code>MessagePart</code>s of a 
     *      <code>MultipartMessage</code> and must always be set for each
     *      message part.
     * @param contentLocation the content location which specifies the
     *      file name of the file that is attached. If the content location is
     *      set to <code>null</code> no content location will be set for this
     *      <code>MessagePart</code>.
     * @param enc the encoding scheme for the <code>MessagePart</code>.
     *      if <code>enc</code> is set to <code>null</code> no encoding will
     *      be used for this <code>MessagePart</code>.
     * @throws java.lang.IllegalArgumentException if mimeType or contentId is
     *      <code>null</code>. This exception will be thrown if
     *      <code>contentID</code> or <code>contentLocation</code> contains
     *      other characters than specified in US-ASCII format. This exception
     *      will be thrown if either <code>length</code> is less than 0 or
     *      <code>offset + length</code> exceeds the <code>length</code> of the
     *      <code>content</code> or if <code>offset</code> is less than 0 or if
     *      the specified encoding scheme is unknown.
     * @throws SizeExceededException if the <code>contents</code> is larger than
     *      the available memory or supported size for the message part
     */ 
    void construct(byte[] contents, int offset, int length, 
        java.lang.String mimeType, java.lang.String contentId,
        java.lang.String contentLocation, java.lang.String enc) throws
        SizeExceededException {

        if (length > MAX_PART_SIZE_BYTES) {
            throw new SizeExceededException(
                "InputStream data exceeds " +
                "MessagePart size limit");            
        }

        if (mimeType == null) {
            throw new IllegalArgumentException("mimeType must be specified");
        }
        checkContentID(contentId);
        checkContentLocation(contentLocation);
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0");
        }
        if (contents != null && offset + length > contents.length) {
            throw new IllegalArgumentException(
                "offset + length exceeds contents length");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be >= 0");
        }
        checkEncodingScheme(enc);
    
        if (contents != null) {
            this.content = new byte[length]; 
            System.arraycopy(contents, offset, this.content, 0, length);
        }
        
        this.mimeType = mimeType;
        this.contentID = contentId;
        this.contentLocation = contentLocation;
        this.encoding = enc;
    }
 
    /**
     * Constructs a <code>MessagePart</code> object from a subset of the byte 
     * array. This constructor is only useful if the data size is small
     * (roughly less than 10K). For larger content the <code>InputStream</code>
     * based constructor should be used.
     * @param contents byte array containing the contents for the
     *      <code>MessagePart</code>.
     * @param offset start position
     * @param length the number of bytes to be included in the 
     *      <code>MessagePart</code>.
     * @param mimeType the MIME Content-Type for the <code>MessagePart</code>
     *      [RFC 2046]
     * @param contentId the content-id header field value for the
     *      <code>MessagePart</code> [RFC 2045]. The content-id is unique 
     *      over all <code>MessagePart</code>s of a 
     *      <code>MultipartMessage</code> and must always be set for each
     *      message part.
     * @param contentLocation the content location which specifies the
     *      file name of the file that is attached. If the content location is
     *      set to <code>null</code> no content location will be set for this
     *      <code>MessagePart</code>.
     * @param enc the encoding scheme for the <code>MessagePart</code>.
     *      if <code>enc</code> is set to <code>null</code> no encoding will
     *      be used for this <code>MessagePart</code>.
     * @throws java.lang.IllegalArgumentException if mimeType or contentId is
     *      <code>null</code>. This exception will be thrown if
     *      <code>contentID</code> or <code>contentLocation</code> contains
     *      other characters than specified in US-ASCII format. This exception
     *      will be thrown if either <code>length</code> is less than 0 or
     *      <code>offset + length</code> exceeds the <code>length</code> of the
     *      <code>content</code> or if <code>offset</code> is less than 0 or if
     *      the specified encoding scheme is unknown.
     * @throws SizeExceededException if the <code>contents</code> is larger than
     *      the available memory or supported size for the message part
     */
    public MessagePart(byte[] contents, int offset, int length, 
        java.lang.String mimeType, java.lang.String contentId,
        java.lang.String contentLocation, java.lang.String enc) throws
        SizeExceededException {
        construct(contents, offset, length, mimeType, contentId,
            contentLocation, enc);
    }
       
    /**
     * Construct a <code>MessagePart</code> object from a byte array. This
     * constructor is only useful if the data size is small (roughly 10K).
     * For larger content the <code>InputStream</code> based constructor
     * should be used.
     * @param contents byte array containing the contents for the
     *      <code>MessagePart</code>. The contents of the array will be
     *      copied into the <code>MessagePart</code>.
     * @param mimeType the MIME Content-Type for the <code>MessagePart</code>
     *      [RFC 2046]
     * @param contentId the content-id header field value for the
     *      <code>MessagePart</code> [RFC 2045]. The content-id is unique 
     *      over all <code>MessagePart</code>s of a 
     *      <code>MultipartMessage</code> and must always be set for each
     *      message part.
     * @param contentLocation the content location which specifies the
     *      file name of the file that is attached. If the content location is
     *      set to <code>null</code> no content location will be set for this
     *      <code>MessagePart</code>.
     * @param enc the encoding scheme for the <code>MessagePart</code>.
     *      if <code>enc</code> is set to <code>null</code> no encoding will
     *      be used for this <code>MessagePart</code>.
     * @throws java.lang.IllegalArgumentException if mimeType or contentId is
     *      <code>null</code>. This exception will be thrown if
     *      <code>contentID</code> or <code>contentLocation</code> contains
     *      other characters than specified in US-ASCII format or if
     *      the specified encoding scheme is unknown.
     * @throws SizeExceededException if the <code>contents</code> is larger than
     *      the available memory or supported size for the message part
     */       
    public MessagePart(byte[] contents, java.lang.String mimeType, 
        java.lang.String contentId, java.lang.String contentLocation,
        java.lang.String enc) throws SizeExceededException {
        construct(contents, 0, (contents == null ? 0 : contents.length),
            mimeType, contentId, contentLocation, enc);
    }
    /** Buffer size 2048. */    
    static final int BUFFER_SIZE = 2048;
    
    /**
     * Constructs a <code>MessagePart</code> object from an 
     * <code>InputStream</code>. The contents of the <code>MessagePart</code>
     * are loaded from the <code>InputStream</code> during the constructor
     * call until the end of the stream is reached.
     * @param is <code>InputStream</code> from which the contents of the 
     *      <code>MessagePart</code> are read.
     * @param mimeType the MIME Content-Type for the <code>MessagePart</code>
     *      [RFC 2046]
     * @param contentId the content-id header field value for the
     *      <code>MessagePart</code> [RFC 2045]. The content-id is unique 
     *      over all <code>MessagePart</code>s of a 
     *      <code>MultipartMessage</code> and must always be set for each
     *      message part.
     * @param contentLocation the content location which specifies the
     *      file name of the file that is attached. If the content location is
     *      set to <code>null</code> no content location will be set for this
     *      <code>MessagePart</code>.
     * @param enc the encoding scheme for the <code>MessagePart</code>.
     *      if <code>enc</code> is set to <code>null</code> no encoding will
     *      be used for this <code>MessagePart</code>.
     * @throws java.io.IOException if reading the <code>InputStream</code>
     *      causes an exception other than <code>EOFException</code>.
     * @throws java.lang.IllegalArgumentException if mimeType or contentId is
     *      <code>null</code>. This exception will be thrown if
     *      <code>contentID</code> or <code>contentLocation</code> contains
     *      other characters than specified in US-ASCII format or if
     *      the specified encoding scheme is unknown.
     * @throws SizeExceededException of the content from the 
     *      <code>InputStream</code> is larger than the available memory or
     *      supported size for the message part.
     */
    public MessagePart(java.io.InputStream is, java.lang.String mimeType, 
        java.lang.String contentId, java.lang.String contentLocation,
        java.lang.String enc) throws IOException, SizeExceededException {
        byte[] bytes = {};
        if (is != null) {
            ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes = 0;
            while ((readBytes = is.read(buffer)) != -1) {
                accumulator.write(buffer, 0, readBytes);
            }
            bytes = accumulator.toByteArray();
        }
        construct(bytes, 0, bytes.length, mimeType, contentId, 
            contentLocation, enc);
    }
    
    /**
     * Returns the content of the <code>MessagePart</code> as an array of
     * bytes. If it's not possible to create an arary which can contain all 
     * data, this method must throw an <code>OutOfMemoryError</code>.
     * @return <code>MessagePart</code> data as byte array
     */
    public byte[] getContent() {
        if (content == null) {
            return null;
        }
        byte[] copyOfContent = new byte[content.length];
        System.arraycopy(content, 0, copyOfContent, 0, content.length);
        return copyOfContent;
    }
    
    /**
     * Returns an <code>InputStream</code> for reading the contents of the
     * <code>MessagePart</code>. Returns an empty stream if no content is
     * available.
     * @return an <code>InputStream</code> that can be used for reading the
     *  contents of this <code>MessagePart</code>.
     */
    public java.io.InputStream getContentAsStream() {
        if (content == null) {
            return new ByteArrayInputStream(new byte[0]);
        } else {
            return new ByteArrayInputStream(content);
        }
    }
    
    /**
     * Returns the content-id value of the <code>MessagePart</code>.
     * @return the value of the content-id as a String, or <code>null</code>
     *      if the content-id is not set (possible if the message was sent
     *      from a not JSR 205 compliant client).
     */
    public java.lang.String getContentID() {
        return contentID;
    }
    
    /**
     * Returns content location of the <code>MessagePart</code>.
     * @return content location
     */
    public java.lang.String getContentLocation() {
        return contentLocation;
    }
    
    /**
     * Returns the encoding of the content, e.g. "US-ASCII", "UTF-8",
     * "UTF-16", ... as a <code>String</code>.
     * @return the encoding of the <code>MessagePart</code> content or
     *      </code>null</code> if the encoding scheme of the 
     *      <code>MessagePart</code> cannot be determined.
     */
    public java.lang.String getEncoding() {
        return encoding;
    }
    
    /**
     * Returns the content size of this <code>MessagePart</code>.
     * @return Content size (in bytes) of this <code>MessagePart</code> or 0 if
     *      the <code>MessagePart</code> is empty.
     */
    public int getLength() {
        return content == null ? 0 : content.length; 
    }
    
    /**
     * Returns the mime type of the <code>MessagePart</code>.
     * @return MIME type of the <code>MessagePart</code>.
     */
    public java.lang.String getMIMEType() {
        return mimeType;
    }
    
    /** Content byte array. */
    byte[] content;
    /** MIME Content ID. */
    String contentID;
    /** Content location. */
    String contentLocation;
    /** Content encoding. */
    String encoding;
    /** MIME type. */
    String mimeType;

    /**
     * Verifies the content identifier.
     * @param contentId content id to be checked
     * @exception IllegalArgumentException if content id is not valid
     */    
    static void checkContentID(String contentId) 
        throws IllegalArgumentException {
        if (contentId == null) {
            throw new IllegalArgumentException("contentId must be specified");
        }
        if (contentId.length() > 100) { // MMS Conformance limit
            throw new IllegalArgumentException(
                "contentId exceeds 100 char limit");
        }
        if (containsNonUSASCII(contentId)) {
            throw new IllegalArgumentException(
                "contentId must not contain non-US-ASCII characters");
        }
    }
 
    /**
     * Verifies the content location.
     * @param contentLoc content location to be checked.
     * @exception IllegalArgumentException if content location is not valid.
     */    
    static void checkContentLocation(String contentLoc)
        throws IllegalArgumentException {
        if (contentLoc != null) {
            if (containsNonUSASCII(contentLoc)) {
                throw new IllegalArgumentException(
                    "contentLocation must not contain non-US-ASCII characters");
            }
            if (contentLoc.length() > 100) { // MMS Conformance limit
                throw new IllegalArgumentException(
                    "contentLocation exceeds 100 char limit");
            }
        }
    }
    
    /**
     * Verifies the content encoding.
     * @param encoding The content encoding to be checked.
     * @exception IllegalArgumentException if content encoding is not valid.
     */    
    static void checkEncodingScheme(String encoding)
        throws IllegalArgumentException {
        // TODO: check for a valid encoding scheme        
    }

    /** Lowest valid ASCII character. */
    static final char US_ASCII_LOWEST_VALID_CHAR = 32;

    /** Mask for ASCII character checks. */
    static final char US_ASCII_VALID_BIT_MASK = 0x7F;
    
    /**
     * Checks if a string contains non-ASCII characters.
     * @param str Text to be checked.
     * @return <code>true</code> if non-ASCII characters are found.
     */    
    static boolean containsNonUSASCII(String str) {
        int numChars = str.length();
        for (int i = 0; i < numChars; ++i) {
            char thisChar = str.charAt(i);
            if (thisChar < US_ASCII_LOWEST_VALID_CHAR ||
                thisChar != (thisChar & US_ASCII_VALID_BIT_MASK))
                return true;
        }
        return false;
    }
}
