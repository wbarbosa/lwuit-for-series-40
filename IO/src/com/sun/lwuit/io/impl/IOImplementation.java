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

package com.sun.lwuit.io.impl;

import com.sun.lwuit.Display;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.Cookie;
import com.sun.lwuit.io.FileSystemStorage;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.Storage;
import com.sun.lwuit.util.EventDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Represents the implementation of the networking/filesystem abstraction layer.
 * This class can be replaced to implement various supported device types and hide
 * device bugs
 *
 * @author Shai Almog
 */
public abstract class IOImplementation {
    private static IOImplementation INSTANCE;

    static {
        new PlatformImplementation();
    }

    private Object storageData;
    private Hashtable cookies;
    private ActionListener logger;

    /**
     * Creating the subclass instance allows the singleton to initialize
     */
    protected IOImplementation() {
        INSTANCE = this;
    }

    public void addCookie(Cookie [] cookiesArray) {
        if(cookies == null){
            cookies = new Hashtable();
        }
        for (int i = 0; i < cookiesArray.length; i++) {
            Cookie cookie = cookiesArray[i];
            Hashtable h = (Hashtable)cookies.get(cookie.getDomain());
            if(h == null){
                h = new Hashtable();
                cookies.put(cookie.getDomain(), h);
            }
            h.put(cookie.getName(), cookie);
        }
        
        if(Cookie.isAutoStored()){
            if(Storage.isInitialized()){
                if(Storage.getInstance().exists(Cookie.STORAGE_NAME)){
                    Storage.getInstance().deleteStorageFile(Cookie.STORAGE_NAME);
                }
                Storage.getInstance().writeObject(Cookie.STORAGE_NAME, cookies);
            }else{
               System.out.println("Warning: Storage is not initialized");
            }
        }
    }

    /**
     * Adds/replaces a cookie to be sent to the given domain
     * 
     * @param c cookie to add
     */
    public void addCookie(Cookie c) {
        if(cookies == null){
            cookies = new Hashtable();
        }
        Hashtable h = (Hashtable)cookies.get(c.getDomain());
        if(h == null){
            h = new Hashtable();
            cookies.put(c.getDomain(), h);
        }
        h.put(c.getName(), c);
        if(Cookie.isAutoStored()){
            if(Storage.isInitialized()){
                if(Storage.getInstance().exists(Cookie.STORAGE_NAME)){
                    Storage.getInstance().deleteStorageFile(Cookie.STORAGE_NAME);
                }
                Storage.getInstance().writeObject(Cookie.STORAGE_NAME, cookies);
            }else{
               System.out.println("Warning: Storage is not initialized");
            }
        }
    }

    /**
     * Returns the domain for the given URL
     * 
     * @param url a url
     * @return the domain
     */
    public String getURLDomain(String url) {
        String domain = url.substring(url.indexOf("//") + 2);
        int i = domain.indexOf('/');
        if(i > -1) {
            domain = domain.substring(0, i);
        }
        return domain;
    }

    /**
     * Returns the cookies for this URL
     * 
     * @param url the url on which we are checking for cookies
     * @return the cookies to submit to the given URL
     */
    public Vector getCookiesForURL(String url) {
        Vector response = null;
        if (Cookie.isAutoStored()) {
            if (Storage.isInitialized()) {
                cookies = (Hashtable) Storage.getInstance().readObject(Cookie.STORAGE_NAME);
            } else {
                System.out.println("Warning: Storage is not initialized");
            }
        }

        if(cookies != null && cookies.size() > 0) {
            String domain = getURLDomain(url);
            Enumeration e = cookies.keys();
            while (e.hasMoreElements()) {
                String domainKey = (String) e.nextElement();
                if (domain.indexOf(domainKey) > -1) {
                    Hashtable h = (Hashtable) cookies.get(domainKey);
                    if (h != null) {
                        Enumeration enumCookies = h.elements();
                        if(response == null){
                            response = new Vector();
                        }
                        while (enumCookies.hasMoreElements()) {
                            response.addElement(enumCookies.nextElement());
                        }
                    }
                }
            }
        }
        return response;
    }

    /**
     * Returns the instance of the IO implementation
     *
     * @return the io implementation instance
     */
    public static IOImplementation getInstance() {
        return INSTANCE;
    }

    /**
     * Connects to a given URL, returns a connection object to be used with the implementation
     * later
     *
     * @param url the URL to connect to
     * @param read indicates wheher the connection will be read from
     * @param write indicates whether writing will ocurre into the connection
     * @return a URL instance
     */
    public abstract Object connect(String url, boolean read, boolean write) throws IOException;

    /**
     * Indicates the HTTP header value for an HTTP connection
     *
     * @param connection the connection object
     * @param key the key for the header
     * @param val the value for the header
     */
    public abstract void setHeader(Object connection, String key, String val);

    /**
     * Closes the object (connection, stream etc.) without throwing any exception, even if the
     * object is null
     *
     * @param o Connection, Stream or other closeable object
     */
    public void cleanup(Object o) {
        try {
            if(o != null) {
                if(o instanceof InputStream) {
                    ((InputStream) o).close();
                }
                if(o instanceof OutputStream) {
                    ((OutputStream) o).close();
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the content length for this connection
     * 
     * @param connection the connection 
     * @return the content length
     */
    public abstract int getContentLength(Object connection);

    /**
     * Returns an output stream for the given connection
     *
     * @param connection the connection to open an output stream on
     * @return the created output stream
     * @throws IOException thrown by underlying implemnetation
     */
    public abstract OutputStream openOutputStream(Object connection) throws IOException;

    /**
     * Returns an output stream for the given connection
     *
     * @param connection the connection to open an output stream on
     * @param offset position in the file
     * @return the created output stream
     * @throws IOException thrown by underlying implemnetation
     */
    public abstract OutputStream openOutputStream(Object connection, int offset) throws IOException;

    /**
     * Returns an input stream for the given connection
     *
     * @param connection the connection to open an input stream on
     * @return the created input stream
     * @throws IOException thrown by underlying implemnetation
     */
    public abstract InputStream openInputStream(Object connection) throws IOException;

    /**
     * Returns an output stream for the given file
     *
     * @param file the file to which we should open a stream
     * @return the created output stream
     * @throws IOException thrown by underlying implemnetation
     */
    public OutputStream openFileOutputStream(String file) throws IOException {
        return openOutputStream(file);
    }

    /**
     * Returns an input stream for the given connection
     *
     * @param file the file to which we should open a stream
     * @return the created input stream
     * @throws IOException thrown by underlying implemnetation
     */
    public InputStream openFileInputStream(String file) throws IOException {
        return openInputStream(file);
    }

    /**
     * Indicates the whether the request method is GET or POST
     *
     * @param connection the connection object
     * @param p true for post false for get
     */
    public abstract void setPostRequest(Object connection, boolean p);

    /**
     * Returns the server response code for the request
     *
     * @param connection the connection object
     * @return a numeric HTTP response code
     * @throws IOException if the request failed
     */
    public abstract int getResponseCode(Object connection) throws IOException;

    /**
     * Returns the server response message for the request
     *
     * @param connection the connection object
     * @return a text message to go along with the response code
     * @throws IOException if the request failed
     */
    public abstract String getResponseMessage(Object connection) throws IOException;

    /**
     * Returns the HTTP response header field
     *
     * @param name field name for http header
     * @param connection the connection object
     * @return the value of the header field
     * @throws IOException if the request failed
     */
    public abstract String getHeaderField(String name, Object connection) throws IOException;

    /**
     * Returns the HTTP response header fields, returns optionally more than one result or null if
     * no field is present.
     *
     * @param name field name for http header
     * @param connection the connection object
     * @return the values of the header fields
     * @throws IOException if the request failed
     */
    public abstract String[] getHeaderFields(String name, Object connection) throws IOException;

    /**
     * Indicates whether the underlying implementation supports the notion of a network operation
     * timeout. If not timeout is "faked"
     * @return true if HTTP timeout can be configured for this IO implementation
     */
    public boolean isTimeoutSupported() {
        return false;
    }

    /**
     * This will work only if http timeout is supported
     * 
     * @param t time in milliseconds
     */
    public void setTimeout(int t) {
    }

    /**
     * Flush the storage cache allowing implementations that cache storage objects
     * to store
     */
    public void flushStorageCache() {
    }


    /**
     * The storage data is used by some storage implementations (e.g. CDC) to place the
     * storage object in a "proper" location matching the application name. This needs to
     * be set by the user, the name might be ignored in platforms (such as MIDP) where storage
     * is mapped to a native application specific storage.
     *
     * @param storageData the name for the storage or its context
     */
    public void setStorageData(Object storageData) {
        this.storageData = storageData;
    }

    /**
     * The storage data is used by some storage implementations (e.g. CDC) to place the
     * storage object in a "proper" location matching the application name. This needs to
     * be set by the user, the name might be ignored in platforms (such as MIDP) where storage
     * is mapped to a native application specific storage.
     *
     * @return the name for the storage
     */
    public Object getStorageData() {
        return storageData;
    }

    /**
     * Deletes the given file name from the storage
     *
     * @param name the name of the storage file
     */
    public abstract void deleteStorageFile(String name);

    /**
     * Deletes all the files in the application storage
     */
    public void clearStorage() {
        String[] l = listStorageEntries();
        for(int iter = 0 ; iter < l.length ; iter++) {
            deleteStorageFile(l[iter]);
        }
    }

    /**
     * Creates an output stream to the storage with the given name
     *
     * @param name the storage file name
     * @return an output stream of limited capcity
     */
    public abstract OutputStream createStorageOutputStream(String name) throws IOException;

    /**
     * Creates an input stream to the given storage source file
     *
     * @param name the name of the source file
     * @return the input stream
     */
    public abstract InputStream createStorageInputStream(String name) throws IOException;

    /**
     * Returns true if the given storage file exists
     *
     * @param name the storage file name
     * @return true if it exists
     */
    public abstract boolean storageFileExists(String name);

    /**
     * Lists the names of the storage files
     *
     * @return the names of all the storage files
     */
    public abstract String[] listStorageEntries();

    /**
     * Returns the filesystem roots from which the structure of the file system
     * can be traversed
     *
     * @return the roots of the filesystem
     */
    public abstract String[] listFilesystemRoots();

    /**
     * Lists the files within the given directory, returns relative file names and not
     * full file names.
     *
     * @param directory the directory in which files should be listed
     * @return array of file names
     */
    public abstract String[] listFiles(String directory) throws IOException;

    /**
     * Returns the size of the given root directory
     *
     * @param root the root directory in the filesystem
     * @return the byte size of the directory
     */
    public abstract long getRootSizeBytes(String root);

    /**
     * Returns the available space in the given root directory
     *
     * @param root the root directory in the filesystem
     * @return the bytes available in the directory
     */
    public abstract long getRootAvailableSpace(String root);

    /**
     * Creates the given directory
     *
     * @param directory the directory name to create
     */
    public abstract void mkdir(String directory);

    /**
     * Deletes the specific file
     *
     * @param file file to delete
     */
    public abstract void deleteFile(String file);

    /**
     * Indicates the hidden state of the file
     *
     * @param file file
     * @return true for a hidden file
     */
    public abstract boolean isHidden(String file);

    /**
     * Toggles the hidden state of the file
     *
     * @param file file
     * @param h hidden state
     */
    public abstract void setHidden(String file, boolean h);

    /**
     * Returns the length of the file
     *
     * @param file file
     * @return length of said file
     */
    public abstract long getFileLength(String file);

    /**
     * Indicates whether the given file is a directory
     *
     * @param file file
     * @return true if its a directory
     */
    public abstract boolean isDirectory(String file);

    /**
     * Indicates whether the given file exists
     *
     * @param file file
     * @return true if it exists
     */
    public abstract boolean exists(String file);

    /**
     * Renames a file to the given name, expects the new name to be relative to the
     * current directory
     *
     * @param file absolute file name
     * @param newName relative new name
     */
    public abstract void rename(String file, String newName);

    /**
     * Returns the file system separator char normally '/'
     *
     * @return the separator char
     */
    public abstract char getFileSystemSeparator();

    /**
     * Indicates whether looking up an access point is supported by this device
     * 
     * @return true if access point lookup is supported
     */
    public boolean isAPSupported() {
        return false;
    }

    /**
     * Returns the ids of the access points available if supported
     *
     * @return ids of access points
     */
    public String[] getAPIds() {
       return null;
    }

    /**
     * Returns the type of the access point
     *
     * @param id access point id
     * @return one of the supported access point types from network manager
     */
    public int getAPType(String id) {
        return NetworkManager.ACCESS_POINT_TYPE_UNKNOWN;
    }

    /**
     * Returns the user displayable name for the given access point
     *
     * @param id the id of the access point
     * @return the name of the access point
     */
    public String getAPName(String id) {
        return null;
    }

    /**
     * Returns the id of the current access point
     *
     * @return id of the current access point
     */
    public String getCurrentAccessPoint() {
        return null;
    }

    /**
     * Returns the id of the current access point
     *
     * @param id id of the current access point
     */
    public void setCurrentAccessPoint(String id) {
    }

    /**
     * For some reason the standard code for writing UTF8 output in a server request
     * doesn't work as expected on SE/CDC stacks.
     *
     * @return true if the getBytes() approach should be used
     */
    public boolean shouldWriteUTFAsGetBytes() {
        return false;
    }

    /**
     * Some devices need more elaborate thread creation logic e.g. to increase the
     * default stack size or might use a pooling strategy
     *
     * @param name the name of the thread
     * @param r the runnable
     */
    public void startThread(String name, Runnable r) {
        new Thread(r, name).start();
    }

    /**
     * Allows binding logic to occur before closing the output stream
     * such as syncing
     *
     * @param s the closing stream
     */
    public void closingOutput(OutputStream s) {
    }

    /**
     * Allows the logger to print the stack trace into the log when the native
     * platform supports that
     *
     * @param t the exception
     * @param o the writer
     */
    public void printStackTraceToStream(Throwable t, Writer o) {
    }

    /**
     * This method is useful strictly for debugging, the logger can use it to track
     * file opening/closing thus detecting potential file resource leaks that
     * can cause serious problems in some OS's.
     *
     * @param al action listener to receive the callback
     */
    public void setLogListener(ActionListener al) {
        logger = al;
    }

    /**
     * Indicates whether logging is turned on
     * @return true or false
     */
    protected boolean isLogged() {
        return logger != null;
    }

    /**
     * Dispatch the message to the logger
     * @param content content of the message
     */
    protected void log(String content) {
        logger.actionPerformed(new ActionEvent(content));
    }

    /**
     * Logs the creation of a stream
     *
     * @param name the name of the stream
     * @param isInput whether the stream is an input or output stream
     * @param count the number of streams of this type
     */
    public void logStreamCreate(String name, boolean isInput, int count) {
        if(isLogged()) {
            if(isInput) {
                log("Creating input stream " + name + " total streams: " + count);
            } else {
                log("Creating output stream " + name + " total streams: " + count);
            }
        }
    }


    /**
     * Logs the closing of a stream
     *
     * @param name the name of the stream
     * @param isInput whether the stream is an input or output stream
     * @param count the number of streams of this type
     */
    public void logStreamClose(String name, boolean isInput, int count) {
        if(isLogged()) {
            if(isInput) {
                log("Closing input stream " + name + " remaining streams: " + count);
            } else {
                log("Closing output stream " + name + " remaining streams: " + count);
            }
        }
    }

    /**
     * Logs the closing of a stream
     *
     * @param name the name of the stream
     * @param isInput whether the stream is an input or output stream
     */
    public void logStreamDoubleClose(String name, boolean isInput) {
        if(isLogged()) {
            if(isInput) {
                log("Double closing input stream " + name);
            } else {
                log("Double closing output stream " + name);
            }
        }
    }

    /**
     * Returns the type of the root often by guessing
     *
     * @param root the root whose type we are checking
     * @return one of the type constants above
     */
    public int getRootType(String root) {
        root = root.toLowerCase();
        String sdCard = Display.getInstance().getProperty("sdcard", null);
        if(sdCard != null) {
            if(root.indexOf(sdCard) > -1) {
                return FileSystemStorage.ROOT_TYPE_SDCARD;
            }
        } else {
            if(root.indexOf("file:///f:") > -1 || root.indexOf("file:///e:") > -1 || root.indexOf("memorycard") > -1 ||
                    root.indexOf("mmc") > -1 || root.indexOf("sdcard") > -1 ||
                    root.indexOf("store") > -1) {
                return FileSystemStorage.ROOT_TYPE_SDCARD;
            }
        }
        if(root.indexOf("c:") > -1 || root.indexOf("phone memory") > -1 || root.indexOf("store") > -1) {
            return FileSystemStorage.ROOT_TYPE_MAINSTORAGE;
        }
        return FileSystemStorage.ROOT_TYPE_UNKNOWN;
    }
}
