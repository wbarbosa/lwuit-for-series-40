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
 * This interface defines the necessary methods and constants for an HTTP connection.
 * HTTP is a request-response protocol in which the parameters of request must be set before the request is sent. The connection exists in one of three states: Setup, in which the request parameters can be set Connected, in which request parameters have been sent and the response is expected Closed, the final state, in which the HTTP connection as been terminated The following methods may be invoked only in the Setup state: setRequestMethod setRequestProperty The transition from Setup to Connected is caused by any method that requires data to be sent to or received from the server.
 * The following methods cause the transition to the Connected state when the connection is in Setup state. openInputStream openDataInputStream getLength getType getEncoding getHeaderField getResponseCode getResponseMessage getHeaderFieldInt getHeaderFieldDate getExpiration getDate getLastModified getHeaderField getHeaderFieldKey
 * The following methods may be invoked while the connection is in Setup or Connected state. close getRequestMethod getRequestProperty getURL getProtocol getHost getFile getRef getPort getQuery
 * After an output stream has been opened by the openOutputStream or openDataOutputStream methods, attempts to change the request parameters via setRequestMethod or the setRequestProperty are ignored. Once the request parameters have been sent, these methods will throw an IOException. When an output stream is closed via the OutputStream.close or DataOutputStream.close methods, the connection enters the Connected state. When the output stream is flushed via the OutputStream.flush or DataOutputStream.flush methods, the request parameters MUST be sent along with any data written to the stream.
 * Example using StreamConnection
 * Simple read of a URL using StreamConnection. No HTTP specific behavior is needed or used. (Note: this example ignores all HTTP response headers and the HTTP response code. Since a proxy or server may have sent an error response page, an application can not distinquish which data is retreived in the InputStream.)
 * Connector.open is used to open URL and a StreamConnection is returned. From the StreamConnection the InputStream is opened. It is used to read every character until end of file (-1). If an exception is thrown the connection and stream are closed.
 * void getViaStreamConnection(String url) throws IOException { StreamConnection c = null; InputStream s = null; try { c = (StreamConnection)Connector.open(url); s = c.openInputStream(); int ch; while ((ch = s.read()) != -1) { ... } } finally { if (s != null) s.close(); if (c != null) c.close(); } }
 * Example using ContentConnection
 * Simple read of a URL using ContentConnection. No HTTP specific behavior is needed or used.
 * Connector.open is used to open url and a ContentConnection is returned. The ContentConnection may be able to provide the length. If the length is available, it is used to read the data in bulk. From the ContentConnection the InputStream is opened. It is used to read every character until end of file (-1). If an exception is thrown the connection and stream are closed.
 * void getViaContentConnection(String url) throws IOException { ContentConnection c = null; DataInputStream is = null; try { c = (ContentConnection)Connector.open(url); int len = (int)c.getLength(); dis = c.openDataInputStream(); if (len 0) { byte[] data = new byte[len]; dis.readFully(data); } else { int ch; while ((ch = dis.read()) != -1) { ... } } } finally { if (dis != null) dis.close(); if (c != null) c.close(); } }
 * Example using HttpConnection
 * Read the HTTP headers and the data using HttpConnection.
 * Connector.open is used to open url and a HttpConnection is returned. The HTTP headers are read and processed. If the length is available, it is used to read the data in bulk. From the HttpConnection the InputStream is opened. It is used to read every character until end of file (-1). If an exception is thrown the connection and stream are closed.
 * void getViaHttpConnection(String url) throws IOException { HttpConnection c = null; InputStream is = null; int rc; try { c = (HttpConnection)Connector.open(url); // Getting the response code will open the connection, // send the request, and read the HTTP response headers. // The headers are stored until requested. rc = c.getResponseCode(); if (rc != HttpConnection.HTTP_OK) { throw new IOException("HTTP response code: " + rc); } is = c.openInputStream(); // Get the ContentType String type = c.getType(); // Get the length and process the data int len = (int)c.getLength(); if (len 0) { int actual = 0; int bytesread = 0 ; byte[] data = new byte[len]; while ((bytesread != len) && (actual != -1)) { actual = is.read(data, bytesread, len - bytesread); bytesread += actual; } } else { int ch; while ((ch = is.read()) != -1) { ... } } } catch (ClassCastException e) { throw new IllegalArgumentException("Not an HTTP URL"); } finally { if (is != null) is.close(); if (c != null) c.close(); } }
 * Example using POST with HttpConnection
 * Post a request with some headers and content to the server and process the headers and content.
 * Connector.open is used to open url and a HttpConnection is returned. The request method is set to POST and request headers set. A simple command is written and flushed. The HTTP headers are read and processed. If the length is available, it is used to read the data in bulk. From the HttpConnection the InputStream is opened. It is used to read every character until end of file (-1). If an exception is thrown the connection and stream is closed.
 * void postViaHttpConnection(String url) throws IOException { HttpConnection c = null; InputStream is = null; OutputStream os = null; int rc; try { c = (HttpConnection)Connector.open(url); // Set the request method and headers c.setRequestMethod(HttpConnection.POST); c.setRequestProperty("If-Modified-Since", "29 Oct 1999 19:43:31 GMT"); c.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0"); c.setRequestProperty("Content-Language", "en-US"); // Getting the output stream may flush the headers os = c.openOutputStream(); os.write("LIST games\n".getBytes()); os.flush(); // Optional, getResponseCode will flush // Getting the response code will open the connection, // send the request, and read the HTTP response headers. // The headers are stored until requested. rc = c.getResponseCode(); if (rc != HttpConnection.HTTP_OK) { throw new IOException("HTTP response code: " + rc); } is = c.openInputStream(); // Get the ContentType String type = c.getType(); processType(type); // Get the length and process the data int len = (int)c.getLength(); if (len 0) { int actual = 0; int bytesread = 0 ; byte[] data = new byte[len]; while ((bytesread != len) && (actual != -1)) { actual = is.read(data, bytesread, len - bytesread); bytesread += actual; } process(data); } else { int ch; while ((ch = is.read()) != -1) { process((byte)ch); } } } catch (ClassCastException e) { throw new IllegalArgumentException("Not an HTTP URL"); } finally { if (is != null) is.close(); if (os != null) os.close(); if (c != null) c.close(); } }
 */
public interface HttpConnection extends ContentConnection{
    /**
     * HTTP Get method.
     * See Also:Constant Field Values
     */
    public static final java.lang.String GET="GET";

    /**
     * HTTP Head method.
     * See Also:Constant Field Values
     */
    public static final java.lang.String HEAD="HEAD";

    /**
     * 202: The request has been accepted for processing, but the processing has not been completed.
     * See Also:Constant Field Values
     */
    public static final int HTTP_ACCEPTED=202;

    /**
     * 502: The server, while acting as a gateway or proxy, received an invalid response from the upstream server it accessed in attempting to fulfill the request.
     * See Also:Constant Field Values
     */
    public static final int HTTP_BAD_GATEWAY=502;

    /**
     * 405: The method specified in the Request-Line is not allowed for the resource identified by the Request-URI.
     * See Also:Constant Field Values
     */
    public static final int HTTP_BAD_METHOD=405;

    /**
     * 400: The request could not be understood by the server due to malformed syntax.
     * See Also:Constant Field Values
     */
    public static final int HTTP_BAD_REQUEST=400;

    /**
     * 408: The client did not produce a request within the time that the server was prepared to wait. The client MAY repeat the request without modifications at any later time.
     * See Also:Constant Field Values
     */
    public static final int HTTP_CLIENT_TIMEOUT=408;

    /**
     * 409: The request could not be completed due to a conflict with the current state of the resource.
     * See Also:Constant Field Values
     */
    public static final int HTTP_CONFLICT=409;

    /**
     * 201: The request has been fulfilled and resulted in a new resource being created.
     * See Also:Constant Field Values
     */
    public static final int HTTP_CREATED=201;

    /**
     * 413: The server is refusing to process a request because the request entity is larger than the server is willing or able to process.
     * See Also:Constant Field Values
     */
    public static final int HTTP_ENTITY_TOO_LARGE=413;

    /**
     * 417: The expectation given in an Expect request-header field could not be met by this server, or, if the server is a proxy, the server has unambiguous evidence that the request could not be met by the next-hop server.
     * See Also:Constant Field Values
     */
    public static final int HTTP_EXPECT_FAILED=417;

    /**
     * 403: The server understood the request, but is refusing to fulfill it. Authorization will not help and the request SHOULD NOT be repeated.
     * See Also:Constant Field Values
     */
    public static final int HTTP_FORBIDDEN=403;

    /**
     * 504: The server, while acting as a gateway or proxy, did not receive a timely response from the upstream server specified by the URI or some other auxiliary server it needed to access in attempting to complete the request.
     * See Also:Constant Field Values
     */
    public static final int HTTP_GATEWAY_TIMEOUT=504;

    /**
     * 410: The requested resource is no longer available at the server and no forwarding address is known.
     * See Also:Constant Field Values
     */
    public static final int HTTP_GONE=410;

    /**
     * 500: The server encountered an unexpected condition which prevented it from fulfilling the request.
     * See Also:Constant Field Values
     */
    public static final int HTTP_INTERNAL_ERROR=500;

    /**
     * 411: The server refuses to accept the request without a defined Content- Length.
     * See Also:Constant Field Values
     */
    public static final int HTTP_LENGTH_REQUIRED=411;

    /**
     * 301: The requested resource has been assigned a new permanent URI and any future references to this resource SHOULD use one of the returned URIs.
     * See Also:Constant Field Values
     */
    public static final int HTTP_MOVED_PERM=301;

    /**
     * 302: The requested resource resides temporarily under a different URI. (Note: the name of this status code reflects the earlier publication of RFC2068, which was changed in RFC2616 from "moved temporalily" to "found". The semantics were not changed. The Location header indicates where the application should resend the request.)
     * See Also:Constant Field Values
     */
    public static final int HTTP_MOVED_TEMP=302;

    /**
     * 300: The requested resource corresponds to any one of a set of representations, each with its own specific location, and agent- driven negotiation information is being provided so that the user (or user agent) can select a preferred representation and redirect its request to that location.
     * See Also:Constant Field Values
     */
    public static final int HTTP_MULT_CHOICE=300;

    /**
     * 204: The server has fulfilled the request but does not need to return an entity-body, and might want to return updated meta-information.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NO_CONTENT=204;

    /**
     * 406: The resource identified by the request is only capable of generating response entities which have content characteristics not acceptable according to the accept headers sent in the request.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NOT_ACCEPTABLE=406;

    /**
     * 203: The returned meta-information in the entity-header is not the definitive set as available from the origin server.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NOT_AUTHORITATIVE=203;

    /**
     * 404: The server has not found anything matching the Request-URI. No indication is given of whether the condition is temporary or permanent.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NOT_FOUND=404;

    /**
     * 501: The server does not support the functionality required to fulfill the request.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NOT_IMPLEMENTED=501;

    /**
     * 304: If the client has performed a conditional GET request and access is allowed, but the document has not been modified, the server SHOULD respond with this status code.
     * See Also:Constant Field Values
     */
    public static final int HTTP_NOT_MODIFIED=304;

    /**
     * 200: The request has succeeded.
     * See Also:Constant Field Values
     */
    public static final int HTTP_OK=200;

    /**
     * 206: The server has fulfilled the partial GET request for the resource.
     * See Also:Constant Field Values
     */
    public static final int HTTP_PARTIAL=206;

    /**
     * 402: This code is reserved for future use.
     * See Also:Constant Field Values
     */
    public static final int HTTP_PAYMENT_REQUIRED=402;

    /**
     * 412: The precondition given in one or more of the request-header fields evaluated to false when it was tested on the server.
     * See Also:Constant Field Values
     */
    public static final int HTTP_PRECON_FAILED=412;

    /**
     * 407: This code is similar to 401 (Unauthorized), but indicates that the client must first authenticate itself with the proxy.
     * See Also:Constant Field Values
     */
    public static final int HTTP_PROXY_AUTH=407;

    /**
     * 414: The server is refusing to service the request because the Request-URI is longer than the server is willing to interpret.
     * See Also:Constant Field Values
     */
    public static final int HTTP_REQ_TOO_LONG=414;

    /**
     * 205: The server has fulfilled the request and the user agent SHOULD reset the document view which caused the request to be sent.
     * See Also:Constant Field Values
     */
    public static final int HTTP_RESET=205;

    /**
     * 303: The response to the request can be found under a different URI and SHOULD be retrieved using a GET method on that resource.
     * See Also:Constant Field Values
     */
    public static final int HTTP_SEE_OTHER=303;

    /**
     * 307: The requested resource resides temporarily under a different URI.
     * See Also:Constant Field Values
     */
    public static final int HTTP_TEMP_REDIRECT=307;

    /**
     * 401: The request requires user authentication. The response MUST include a WWW-Authenticate header field containing a challenge applicable to the requested resource.
     * See Also:Constant Field Values
     */
    public static final int HTTP_UNAUTHORIZED=401;

    /**
     * 503: The server is currently unable to handle the request due to a temporary overloading or maintenance of the server.
     * See Also:Constant Field Values
     */
    public static final int HTTP_UNAVAILABLE=503;

    /**
     * 416: A server SHOULD return a response with this status code if a request included a Range request-header field , and none of the range-specifier values in this field overlap the current extent of the selected resource, and the request did not include an If-Range request-header field.
     * See Also:Constant Field Values
     */
    public static final int HTTP_UNSUPPORTED_RANGE=416;

    /**
     * 415: The server is refusing to service the request because the entity of the request is in a format not supported by the requested resource for the requested method.
     * See Also:Constant Field Values
     */
    public static final int HTTP_UNSUPPORTED_TYPE=415;

    /**
     * 305: The requested resource MUST be accessed through the proxy given by the Location field.
     * See Also:Constant Field Values
     */
    public static final int HTTP_USE_PROXY=305;

    /**
     * 505: The server does not support, or refuses to support, the HTTP protocol version that was used in the request message.
     * See Also:Constant Field Values
     */
    public static final int HTTP_VERSION=505;

    /**
     * HTTP Post method.
     * See Also:Constant Field Values
     */
    public static final java.lang.String POST="POST";

    /**
     * Returns the value of the date header field.
     */
    public abstract long getDate() throws java.io.IOException;

    /**
     * Returns the value of the expires header field.
     */
    public abstract long getExpiration() throws java.io.IOException;

    /**
     * Returns the file portion of the URL of this HttpConnection.
     */
    public abstract java.lang.String getFile();

    /**
     * Gets a header field value by index.
     */
    public abstract java.lang.String getHeaderField(int n) throws java.io.IOException;

    /**
     * Returns the value of the named header field.
     */
    public abstract java.lang.String getHeaderField(java.lang.String name) throws java.io.IOException;

    /**
     * Returns the value of the named field parsed as date. The result is the number of milliseconds since January 1, 1970 GMT represented by the named field.
     * This form of getHeaderField exists because some connection types (e.g., http-ng) have pre-parsed headers. Classes for that connection type can override this method and short-circuit the parsing.
     */
    public abstract long getHeaderFieldDate(java.lang.String name, long def) throws java.io.IOException;

    /**
     * Returns the value of the named field parsed as a number.
     * This form of getHeaderField exists because some connection types (e.g., http-ng) have pre-parsed headers. Classes for that connection type can override this method and short-circuit the parsing.
     */
    public abstract int getHeaderFieldInt(java.lang.String name, int def) throws java.io.IOException;

    /**
     * Gets a header field key by index.
     */
    public abstract java.lang.String getHeaderFieldKey(int n) throws java.io.IOException;

    /**
     * Returns the host information of the URL of this HttpConnection. e.g. host name or IPv4 address
     */
    public abstract java.lang.String getHost();

    /**
     * Returns the value of the last-modified header field. The result is the number of milliseconds since January 1, 1970 GMT.
     */
    public abstract long getLastModified() throws java.io.IOException;

    /**
     * Returns the network port number of the URL for this HttpConnection.
     */
    public abstract int getPort();

    /**
     * Returns the protocol name of the URL of this HttpConnection. e.g., http or https
     */
    public abstract java.lang.String getProtocol();

    /**
     * Returns the query portion of the URL of this HttpConnection. RFC2396 defines the query component as the text after the first question-mark (?) character in the URL.
     */
    public abstract java.lang.String getQuery();

    /**
     * Returns the ref portion of the URL of this HttpConnection. RFC2396 specifies the optional fragment identifier as the the text after the crosshatch (#) character in the URL. This information may be used by the user agent as additional reference information after the resource is successfully retrieved. The format and interpretation of the fragment identifier is dependent on the media type[RFC2046] of the retrieved information.
     */
    public abstract java.lang.String getRef();

    /**
     * Get the current request method. e.g. HEAD, GET, POST The default value is GET.
     */
    public abstract java.lang.String getRequestMethod();

    /**
     * Returns the value of the named general request property for this connection.
     */
    public abstract java.lang.String getRequestProperty(java.lang.String key);

    /**
     * Returns the HTTP response status code. It parses responses like: HTTP/1.0 200 OK HTTP/1.0 401 Unauthorized and extracts the ints 200 and 401 respectively. from the response (i.e., the response is not valid HTTP).
     */
    public abstract int getResponseCode() throws java.io.IOException;

    /**
     * Gets the HTTP response message, if any, returned along with the response code from a server. From responses like: HTTP/1.0 200 OK HTTP/1.0 404 Not Found Extracts the Strings "OK" and "Not Found" respectively. Returns null if none could be discerned from the responses (the result was not valid HTTP).
     */
    public abstract java.lang.String getResponseMessage() throws java.io.IOException;

    /**
     * Return a string representation of the URL for this connection.
     */
    public abstract java.lang.String getURL();

    /**
     * Set the method for the URL request, one of: GET POST HEAD are legal, subject to protocol restrictions. The default method is GET.
     */
    public abstract void setRequestMethod(java.lang.String method) throws java.io.IOException;

    /**
     * Sets the general request property. If a property with the key already exists, overwrite its value with the new value.
     * Note: HTTP requires all request properties which can legally have multiple instances with the same key to use a comma-separated list syntax which enables multiple properties to be appended into a single property.
     */
    public abstract void setRequestProperty(java.lang.String key, java.lang.String value) throws java.io.IOException;

}
