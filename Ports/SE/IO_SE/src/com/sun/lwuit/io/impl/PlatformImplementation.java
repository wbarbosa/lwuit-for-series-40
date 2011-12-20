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

import com.sun.lwuit.io.util.BufferedInputStream;
import com.sun.lwuit.io.util.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * CDC/SE version of the IO porting layer
 *
 * @author Shai Almog
 */
public class PlatformImplementation extends IOImplementation {
    private File storageDir;

    private File getStorageDir() {
        if(storageDir == null) {
            storageDir = new File(System.getProperty("user.home"), "." + ((String)getStorageData()));
            storageDir.mkdirs();
        }
        return storageDir;
    }

    /**
     * @inheritDoc
     */
    public Object connect(String url, boolean read, boolean write) throws IOException {
        URL u = new URL(url);
        URLConnection con = u.openConnection();
        con.setDoInput(read);
        con.setDoOutput(write);
        return con;
    }

    /**
     * @inheritDoc
     */
    public void setHeader(Object connection, String key, String val) {
        ((URLConnection)connection).setRequestProperty(key, val);
    }

    /**
     * @inheritDoc
     */
    public OutputStream openOutputStream(Object connection) throws IOException {
        if(connection instanceof String) {
            FileOutputStream fc = new FileOutputStream((String)connection);
            BufferedOutputStream o = new BufferedOutputStream(fc, (String)connection);
            return o;
        }
        return new BufferedOutputStream(((URLConnection)connection).getOutputStream());
    }

    /**
     * @inheritDoc
     */
    public OutputStream openOutputStream(Object connection, int offset) throws IOException {
        RandomAccessFile rf = new RandomAccessFile((String)connection, "rw");
        rf.seek(offset);
        FileOutputStream fc = new FileOutputStream(rf.getFD());
        BufferedOutputStream o = new BufferedOutputStream(fc, (String)connection);
        o.setConnection(rf);
        return o;
    }

    /**
     * @inheritDoc
     */
    public InputStream openInputStream(Object connection) throws IOException {
        if(connection instanceof String) {
            FileInputStream fc = new FileInputStream((String)connection);
            BufferedInputStream o = new BufferedInputStream(fc, (String)connection);
            return o;
        }
        return new BufferedInputStream(((URLConnection)connection).getInputStream());
    }

    /**
     * @inheritDoc
     */
    public void setPostRequest(Object connection, boolean p) {
        try {
            if(p) {
                ((HttpURLConnection)connection).setRequestMethod("POST");
            } else {
                ((HttpURLConnection)connection).setRequestMethod("GET");
            }
        } catch(IOException err) {
            // an exception here doesn't make sense
            err.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public int getResponseCode(Object connection) throws IOException {
        return ((HttpURLConnection)connection).getResponseCode();
    }

    /**
     * @inheritDoc
     */
    public String getResponseMessage(Object connection) throws IOException {
        return ((HttpURLConnection)connection).getResponseMessage();
    }

    /**
     * @inheritDoc
     */
    public int getContentLength(Object connection) {
        return ((HttpURLConnection)connection).getContentLength();
    }

    /**
     * @inheritDoc
     */
    public String getHeaderField(String name, Object connection) throws IOException {
        return ((HttpURLConnection)connection).getHeaderField(name);
    }

    /**
     * @inheritDoc
     */
    public String[] getHeaderFields(String name, Object connection) throws IOException {
        HttpURLConnection c = (HttpURLConnection)connection;
        List r = new ArrayList();
        String ck = c.getHeaderFieldKey(0);
        for(int iter = 0 ; ck != null ; iter++) {
            if(ck.equalsIgnoreCase(name)) {
                r.add(c.getHeaderField(iter));
            }
            ck = c.getHeaderFieldKey(iter);
        }

        if(r.size() == 0) {
            return null;
        }
        String[] response = new String[r.size()];
        for(int iter = 0 ; iter < response.length ; iter++) {
            response[iter] = (String)r.get(iter);
        }
        return response;
    }

    /**
     * @inheritDoc
     */
    public void deleteStorageFile(String name) {
        new File(getStorageDir(), name).delete();
    }

    /**
     * @inheritDoc
     */
    public OutputStream createStorageOutputStream(String name) throws IOException {
        return new FileOutputStream(new File(getStorageDir(), name));
    }

    /**
     * @inheritDoc
     */
    public InputStream createStorageInputStream(String name) throws IOException {
        return new FileInputStream(new File(getStorageDir(), name));
    }

    /**
     * @inheritDoc
     */
    public boolean storageFileExists(String name) {
        return new File(getStorageDir(), name).exists();
    }

    /**
     * @inheritDoc
     */
    public String[] listStorageEntries() {
        return getStorageDir().list();
    }

    /**
     * @inheritDoc
     */
    public String[] listFilesystemRoots() {
        File[] f = File.listRoots();
        String[] roots = new String[f.length];
        for(int iter = 0 ; iter < f.length ; iter++) {
            roots[iter] = f[iter].getAbsolutePath();
        }
        return roots;
    }

    /**
     * @inheritDoc
     */
    public String[] listFiles(String directory) throws IOException {
        return new File(directory).list();
    }

    /**
     * @inheritDoc
     */
    public long getRootSizeBytes(String root) {
        return -1;
    }

    /**
     * @inheritDoc
     */
    public long getRootAvailableSpace(String root) {
        return -1;
    }

    /**
     * @inheritDoc
     */
    public void mkdir(String directory) {
        new File(directory).mkdirs();
    }

    /**
     * @inheritDoc
     */
    public void deleteFile(String file) {
        new File(file).delete();
    }

    /**
     * @inheritDoc
     */
    public boolean isHidden(String file) {
        return new File(file).isHidden();
    }

    /**
     * @inheritDoc
     */
    public void setHidden(String file, boolean h) {
    }

    /**
     * @inheritDoc
     */
    public long getFileLength(String file) {
        return new File(file).length();
    }

    /**
     * @inheritDoc
     */
    public boolean isDirectory(String file) {
        return new File(file).isDirectory();
    }

    /**
     * @inheritDoc
     */
    public char getFileSystemSeparator() {
        return File.separatorChar;
    }

    /**
     * @inheritDoc
     */
    public OutputStream openFileOutputStream(String file) throws IOException {
        return new FileOutputStream(file);
    }

    /**
     * @inheritDoc
     */
    public InputStream openFileInputStream(String file) throws IOException {
        return new FileInputStream(file);
    }

    /**
     * @inheritDoc
     */
    public boolean exists(String file) {
        return new File(file).exists();
    }

    /**
     * @inheritDoc
     */
    public void rename(String file, String newName) {
        new File(file).renameTo(new File(new File(file).getParentFile(), newName));
    }

    /**
     * @inheritDoc
     */
    public boolean shouldWriteUTFAsGetBytes() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public void printStackTraceToStream(Throwable t, Writer o) {
        PrintWriter p = new PrintWriter(o);
        t.printStackTrace(p);
    }
}
