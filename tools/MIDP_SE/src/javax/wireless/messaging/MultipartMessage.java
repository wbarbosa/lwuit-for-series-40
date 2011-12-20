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

/**
 * An interface representing a multipart message. This is a subinterface of
 * <code>Message</code> which contains methods to add and get 
 * <code>MessagePart</code>s. The interface also allows to specify the subject
 * of the message. The basic methods for manipulating the address portion of
 * the message are inherited from <code>Message</code>. Additionally, this
 * interface defines methods for adding and removing addresses to/from the 
 * "to", "cc" or "bcc" fields. Furthermore it offers methods to get and set
 * special header fields of the message. The contents of the MultipartMessage
 * are assembled during the invocation of the MessageConnection.send() method.
 * The contents of each MessagePart are copied before the send message returns. 
 * Changes to the MessagePart contents after send must not appear in the
 * transmitted message.
 * @since WMA 2.0
 */
public interface MultipartMessage extends Message {
    
    /**
     * Adds an address to the multipart message.
     * @param type the adress type ("to", "cc" or "bcc") as a 
     *      <code>String</code>. Each message can have none or multiple "to",
     *      "cc" and "bcc" addresses. Eash address is added separately. The
     *      method is not case sensitive. The implementation of 
     *      <code>MessageConnection.send()</code> makes sure that the "from"
     *      address is set correctly.
     * @param address the address as a <code>String</code>
     * @return <code>true</code> if it was possible to add the address, else
     *      <code>false</code>
     * @throws java.lang.IllegalArgumentException if type is none of "to", "cc",
     *      or "bcc" or if <code>address</code> is not valid.
     * @see #setAddress(String)
     */
    boolean addAddress(java.lang.String type, java.lang.String address);
    
    /**
     * Attaches a <code>MessagePart</code> to the multipart message.
     * @param part <code>MessagePart</code> to add
     * @throws java.lang.IllegalArgumentException if the Content-ID of the
     *      <code>MessagePart</code> conflicts with a Content-ID of a
     *      <code>MessagePart</code> already contained in this 
     *      <code>MultiPartMessage</code>. The Content-IDs must be unique
     *      within a MultipartMessage.
     * @throws NullPointerException if the parameter is <code>null</code>
     * @throws SizeExceededException if it's not possible to attach the 
     *      <code>MessagePart</code>.
     */
    void addMessagePart(MessagePart part) throws SizeExceededException;
    
    /**
     * Returns the "from" address associated with this message, e.g. address of
     * the sender. If the message is a newly created message, e.g. not a 
     * received one, then the first "to" address is returned.
     * Returns <code>null</code> if the "from" or "to" address for the
     * message, dependent on the case, are not set.
     * Note: This design allows sending responses to a received message easily 
     * by reusing the same <code>Message</code> object and just replacing the
     * payload. The address field can normally be kept untouched (unless the
     * used messaging protocol requires some special handling of the address).
     * @return the "from" or "to" address of this message, or <code>null</code>
     *      if the address that is expected as a result of this method is not
     *      set
     * @see #setAddress(String)
     */
    java.lang.String getAddress();
    
    /**
     * Gets the addresses of the multipart message of the specified type (e.g.
     * "to", "cc", "bcc" or "from") as <code>String</code>. The method is not
     * case sensitive.
     * @param type the type of address list to return
     * @return the addresses as a <code>String</code> array or <code>null</code>
     *      if this value is not present.
     */
    java.lang.String[] getAddresses(java.lang.String type);
    
    /**
     * Gets the content of the specific header field of the multipart message.
     * <p>
     * See Appendix D for known header fields.
     *
     * @see #setHeader
     *
     * @param headerField the name of the header field as a <code>String</code>
     *
     * @return the content of the specified header field as a 
     *      <code>String</code> or <code>null</code> of the specified header
     *      field is not present.
     *
     * @throws SecurityException if the access to specified header field is 
     *      restricted
     * @throws IllegalArgumentException if <code>headerField</code> is unknown
     */      
    java.lang.String getHeader(java.lang.String headerField);
    
    /**
     * This method returns a <code>MessagePart</code> from the message that
     * matches the content-id passed as a parameter.
     * @param contentID the content-id for the <code>MessagePart</code> to be
     *      returned
     * @return <code>MessagePart</code> that matches the provided content-id or
     *      <code>null</code> if there is no <code>MessagePart</code> in this
     *      message with the provided content-id
     * @throws NullPointerException if the parameter is <code>null</code>
     */
    javax.wireless.messaging.MessagePart getMessagePart(
        java.lang.String contentID);
    
    /**
     * Returns an array of all <code>MessagePart</code>s of this message.
     * @return array of <code>MessagePart</code>s, or <code>null</code> if no
     *      <code>MessagePart</code>s are available
     */
    javax.wireless.messaging.MessagePart[] getMessageParts();
    
    /**
     * Returns the <code>contentId</code> of the start <code>MessagePart</code>.
     * The start <code>MessagePart</code> is set in 
     * <code>setStartContentId(String)</code>
     * @return the content-id of the start <code>MessagePart</code> or
     *      <code>null</code> if the start <code>MessagePart</code> is not set.
     * @see #setStartContentId(String)
     */
    java.lang.String getStartContentId();
    
    /**
     * Gets the subject of the multipart message.
     * @return the message subject as a <code>String</code> or <code>null</code>
     *      if this value is not present.
     * @see #setSubject
     */
    java.lang.String getSubject();
    
    /**
     * Removes an address from the multipart message.
     * @param type the address type ("to", "cc", "bcc", or "from") as a
     *      <code>String</code>
     * @param address the address as a <code>String</code>
     * @return <code>true</code> if it was possible to delete the address, else
     *      <code>false</code>
     * @throws NullPointerException is type is <code>null</code>
     * @throws java.lang.IllegalArgumentException if type is none of "to", "cc",
     *      "bcc", or "from"
     */
    boolean removeAddress(java.lang.String type, java.lang.String address);
    
    /**
     * Removes all addresses of types "to", "cc", "bcc" from the multipart 
     * message.
     * @see #setAddress(String)
     * @see #addAddress(String, String)
     */
    void removeAddresses();
    
    /**
     * Removes all addresses of the specified type from the multipart message.
     * @param type the address type ("to", "cc", "bcc", or "from") as a 
     *      <code>String</code>
     * @throws NullPointerException if type is <code>null</code>
     * @throws java.lang.IllegalArgumentException if type is none of "to", "cc",
     *      "bcc", or "from"
     */
    void removeAddresses(java.lang.String type);
    
    /**
     * Removes a <code>MessagePart</code> from the multipart message.
     * @param part <code>MessagePart</code> to delete
     * @return <code>true</code> if it was possible to remove the
     *      <code>MessagePart</code>, else <code>false</code>
     * @throws NullPointerException if the parameter is <code>null</code>
     */
    boolean removeMessagePart(MessagePart part);    
    
    /**
     * Removes a <code>MessagePart</code> with the specific 
     * <code>contentID</code> from the multipart message.
     * @param contentID identifies which <code>MessagePart</code> must be
     *      deleted.
     * @return <code>true</code> if it was possible to remove the
     *      <code>MessagePart</code>, else <code>false</code>
     * @throws NullPointerException if the parameter is <code>null</code>
     */
    boolean removeMessagePartId(java.lang.String contentID);
    
    /**
     * Removes <code>MessagePart</code>s with the specific content location
     * from the multipart message. All <code>MessagePart</code>s with the
     * specified <code>contentLocation</code> are removed.
     * @param contentLocation content location (file name) of the 
     *      <code>MessagePart</code>
     * @return <code>true</code> if it was possible to remove the
     *      <code>MessagePart</code>, else <code>false</code>
     * @throws NullPointerException if the parameter is <code>null</code>
     */
    boolean removeMessagePartLocation(java.lang.String contentLocation);
    
    /**
     * Sets the "to" address associated with this message. It works the same way
     * as <code>addAddress("to", addr)</code>. The address may be set to
     * <code>null</code>.
     * @param addr address for the message
     * @see #getAddress()
     * @see #addAddress(String, String)
     */
    void setAddress(java.lang.String addr);
    
    /**
     * Sets the specified header of the multipart message. The header value can
     * be <code>null</code>.
     * <p>
     * See Appendix D for known header fields.
     *
     * @see #getHeader(String)
     *
     * @param headerField the name of the header field as a <code>String</code>
     * @param headerValue the value of the header as a <code>String</code>
     *
     * @throws java.lang.IllegalArgumentException if <code>headerField</code> is
     *      unknown, or if <code>headerValue</code> is not correct (depends on
     *      <code>headerField</code>!)
     * @throws NullPointerException if <code>headerField</code> is 
     *      <code>null</code>
     * @throws SecurityException if the access to specified header field is
     *      restricted
     */
    void setHeader(java.lang.String headerField, java.lang.String headerValue);
    
    /**
     * Sets the <code>Content-ID</code> of the start <code>MessagePart</code> of
     * a multipart related message. The <code>Content-ID</code> may be set to
     * <code>null</code>. The <code>StartContentId</code> is set for the
     * MessagePart that is used to reference the other MessageParts of the
     * MultipartMessage for presentation or processing purposes.
     * @param contentId as a <code>String</code>
     * @throws java.lang.IllegalArgumentException if <code>contentId</code> is
     *      none of the added <code>MessagePart</code> objects matches the
     *      <code>contentId</code>
     * @see #getStartContentId()
     */
    void setStartContentId(java.lang.String contentId);
    
    /**
     * Sets the Subject of the multipart message. This value can be
     * <code>null</code>.
     * @param subject the message subject as a <code>String</code>
     * @see #getSubject()
     */
    void setSubject(java.lang.String subject);
}
