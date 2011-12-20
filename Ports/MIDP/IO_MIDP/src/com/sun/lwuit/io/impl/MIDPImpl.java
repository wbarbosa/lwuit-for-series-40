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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * Implementation of the IO framework on top of MIDP/CLDC GCF
 *
 * @author Shai Almog
 */
public class MIDPImpl extends IOImplementation {
    private short currentKey = 1;

    /**
     * The File Allocation Table assigns user based file names to RMS storage
     */
    private Hashtable fat = new Hashtable();


    /**
     * Initializes various internal states
     */
    protected MIDPImpl() {
        RecordEnumeration e = null;
        RecordStore r = null;
        try {
            r = RecordStore.openRecordStore("FAT", true);
            if (r.getNumRecords() > 0) {
                e = r.enumerateRecords(null, null, false);
                while (e.hasNextElement()) {
                    byte[] rec = e.nextRecord();
                    ByteArrayInputStream bi = new ByteArrayInputStream(rec);
                    DataInputStream di = new DataInputStream(bi);
                    String name = di.readUTF();
                    short key = di.readShort();
                    di.close();
                    bi.close();
                    fat.put(name, new Short(key));
                    if(key >= currentKey) {
                        currentKey += key;
                    }
                }
                e.destroy();
                e = null;
            }
            r.closeRecordStore();
            r = null;
        } catch (Exception ex) {
            //#ifndef RIM
            ex.printStackTrace();
            //#else
//#             System.out.println("Exception in object store constructor " + ex);
            //#endif
            cleanup(r);
            cleanup(e);
        }
    }

    /**
     * @inheritDoc
     */
    public Object connect(String url, boolean read, boolean write) throws IOException {
        int mode;
        if(read && write) {
            mode = Connector.READ_WRITE;
        } else {
            if(write) {
                mode = Connector.WRITE;
            } else {
                mode = Connector.READ;
            }
        }
        return Connector.open(url, mode);
    }

    /**
     * @inheritDoc
     */
    public void setHeader(Object connection, String key, String val) {
        try {
            ((HttpConnection)connection).setRequestProperty(key, val);
        } catch(IOException err) {
            // this exception doesn't make sense since at this point no connection is in place
            err.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public int getContentLength(Object connection) {
        return (int)((HttpConnection)connection).getLength();
    }

    /**
     * @inheritDoc
     */
    public void cleanup(Object o) {
        try {
            if(o != null) {
                if(o instanceof Connection) {
                    ((Connection) o).close();
                    return;
                } 
                if(o instanceof RecordEnumeration) {
                    ((RecordEnumeration) o).destroy();
                    return;
                }
                if(o instanceof RecordStore) {
                    ((RecordStore) o).closeRecordStore();
                    return;
                }
                super.cleanup(o);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public OutputStream openOutputStream(Object connection) throws IOException {
        if(connection instanceof String) {
            FileConnection fc = (FileConnection)Connector.open((String)connection, Connector.READ_WRITE);
            if(!fc.exists()) {
                fc.create();
            }
            BufferedOutputStream o = new BufferedOutputStream(fc.openOutputStream(), (String)connection);
            o.setConnection(fc);
            return o;
        }
        return new BufferedOutputStream(((HttpConnection)connection).openOutputStream(), ((HttpConnection)connection).getURL());
    }

    /**
     * @inheritDoc
     */
    public OutputStream openOutputStream(Object connection, int offset) throws IOException {
        FileConnection fc = (FileConnection)Connector.open((String)connection, Connector.READ_WRITE);
        if(!fc.exists()) {
            fc.create();
        }
        BufferedOutputStream o = new BufferedOutputStream(fc.openOutputStream(offset), (String)connection);
        o.setConnection(fc);
        return o;
    }

    /**
     * @inheritDoc
     */
    public InputStream openInputStream(Object connection) throws IOException {
        if(connection instanceof String) {
            FileConnection fc = (FileConnection)Connector.open((String)connection, Connector.READ);
            BufferedInputStream o = new BufferedInputStream(fc.openInputStream(), (String)connection);
            o.setConnection(fc);
            return o;
        }
        return new BufferedInputStream(((HttpConnection)connection).openInputStream(), ((HttpConnection)connection).getURL());
    }

    /**
     * @inheritDoc
     */
    public void setPostRequest(Object connection, boolean p) {
        try {
            if(p) {
                ((HttpConnection)connection).setRequestMethod(HttpConnection.POST);
            } else {
                ((HttpConnection)connection).setRequestMethod(HttpConnection.GET);
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
        return ((HttpConnection)connection).getResponseCode();
    }

    /**
     * @inheritDoc
     */
    public String getResponseMessage(Object connection) throws IOException {
        return ((HttpConnection)connection).getResponseMessage();
    }

    /**
     * @inheritDoc
     */
    public String getHeaderField(String name, Object connection) throws IOException {
        return ((HttpConnection)connection).getHeaderField(name);
    }

    /**
     * @inheritDoc
     */
    public String[] getHeaderFields(String name, Object connection) throws IOException {
        HttpConnection c = (HttpConnection)connection;
        Vector r = new Vector();
        int i = 0;
        while (c.getHeaderFieldKey(i) != null) {
            if (c.getHeaderFieldKey(i).equalsIgnoreCase(name)) {
                String val = c.getHeaderField(i);
                r.addElement(val);
            }
            i++;
        }
        
        if(r.size() == 0) {
            return null;
        }
        String[] response = new String[r.size()];
        for(int iter = 0 ; iter < response.length ; iter++) {
            response[iter] = (String)r.elementAt(iter);
        }
        return response;
    }

    /**
     * @inheritDoc
     */
    public void deleteStorageFile(String name) {
        Short key = (Short)fat.get(name);
        fat.remove(name);
        resaveFat();
        if(key != null) {
            try {
                for(char c = 'A' ; c < 'Z' ; c++) {
                    RecordStore.deleteRecordStore("" + c + key);
                }
            } catch(RecordStoreException e) {}
        }
    }

    private void resaveFat() {
        RecordStore r = null;
        RecordEnumeration e = null;
        try {
            r = RecordStore.openRecordStore("FAT", true);
            Vector keys = new Vector();
            Enumeration fatKeys = fat.keys();
            while(fatKeys.hasMoreElements()) {
                keys.addElement(fatKeys.nextElement());
            }
            e = r.enumerateRecords(null, null, false);
            while (e.hasNextElement()) {
                int recordId = e.nextRecordId();
                byte[] rec = r.getRecord(recordId);
                ByteArrayInputStream bi = new ByteArrayInputStream(rec);
                DataInputStream di = new DataInputStream(bi);
                String name = di.readUTF();
                short key = di.readShort();
                di.close();
                bi.close();
                Short fatKey = (Short)fat.get(name);
                if(fatKey == null) {
                    // we need to remove this record...
                    r.deleteRecord(recordId);
                } else {
                    // we need to update the record
                    if(fatKey.shortValue() != key) {
                        byte[] bd = toRecord(name, fatKey.shortValue());
                        r.setRecord(recordId, bd, 0, bd.length);
                    }
                }
                keys.removeElement(name);
            }
            e.destroy();
            e = null;

            Enumeration remainingKeys = keys.elements();
            while(remainingKeys.hasMoreElements()) {
                String name = (String)remainingKeys.nextElement();
                Short key = (Short)fat.get(name);
                byte[] bd = toRecord(name, key.shortValue());
                r.addRecord(bd, 0, bd.length);
            }
            r.closeRecordStore();
        } catch(Exception err) {
            // This might be a valid exception and some platforms (e..g. RIM) don't respond well to PST
            //err.printStackTrace();
            cleanup(e);
            cleanup(r);
        }
    }

    private byte[] toRecord(String name, short key) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(bo);
        d.writeUTF(name);
        d.writeShort(key);
        d.close();
        bo.close();
        return bo.toByteArray();
    }

    /**
     * @inheritDoc
     */
    public OutputStream openFileOutputStream(String file) throws IOException {
        return openOutputStream(file);
    }

    /**
     * @inheritDoc
     */
    public InputStream openFileInputStream(String file) throws IOException {
        return openInputStream(file);
    }

    private class RMSOutputStream extends OutputStream {
        private short key;
        private char letter = 'A';
        private ByteArrayOutputStream cache;
        private int offset;
        public RMSOutputStream(short key) {
            this.key =  key;

            // first we need to cleanup existing files
            try {
                for(char c = 'A' ; c < 'Z' ; c++) {
                    RecordStore.deleteRecordStore("" + c + key);
                }
            } catch(RecordStoreException e) {}

            cache = new ByteArrayOutputStream();
        }

        public void close() throws IOException {
            flush();
            cache = null;
        }

        public void flush() throws IOException {
            if(cache != null) {
                byte[] data = cache.toByteArray();
                if(data.length > 0) {
                    RecordStore r = null;
                    try {
                        r = RecordStore.openRecordStore("" + letter + key, true);
                        r.addRecord(data, 0, data.length);
                        r.closeRecordStore();
                        if(letter == 'Z') {
                            letter = 'a';
                        } else {
                            letter++;
                        }
                        cache.reset();
                    } catch (RecordStoreException ex) {
                        ex.printStackTrace();
                        cleanup(r);
                        throw new IOException(ex.toString());
                    }
                }
            }
        }

        public void write(byte[] arg0) throws IOException {
            cache.write(arg0);
            if(cache.size() > 32536) {
                flush();
            }
        }

        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
            cache.write(arg0, arg1, arg2);
            if(cache.size() > 32536) {
                flush();
            }
        }

        public void write(int arg0) throws IOException {
            cache.write(arg0);
            if(cache.size() > 32536) {
                flush();
            }
        }
    }

    private class RMSInputStream extends InputStream {
        private InputStream current;
        private int offset;
        private short key;
        public RMSInputStream(short key) throws IOException {
            this.key = key;
            RecordStore r = null;
            RecordEnumeration e = null;
            char letter = 'A';
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                r = open("" + letter + key);
                while(r != null) {
                    e = r.enumerateRecords(null, null, false);
                    while(e.hasNextElement()) {
                        byte[] data = e.nextRecord();
                        os.write(data);
                    }
                    e.destroy();
                    r.closeRecordStore();
                    letter++;
                    r = open("" + letter + key);
                    if(letter == 'Z') {
                        letter = 'a' - ((char)1);
                    }
                }
                os.close();
                current = new ByteArrayInputStream(os.toByteArray());
            } catch (RecordStoreException ex) {
                //#ifndef RIM
                ex.printStackTrace();
                //#else
//#                 System.out.println("Exception in object store input stream constructor: " + ex);
                //#endif
                cleanup(r);
                cleanup(e);
                throw new IOException(ex.toString());
            }
        }
        private RecordStore open(String s) {
            try {
                return RecordStore.openRecordStore(s, false);
            } catch (RecordStoreException ex) {
                return null;
            }
        }

        public long skip(long n) throws IOException {
            return super.skip(n);
        }

        public void close() throws IOException {
            current.close();
        }

        public int read(byte[] arg0) throws IOException {
            int r  = current.read(arg0);
            offset += r;
            return r;
        }

        public int read(byte[] arg0, int arg1, int arg2) throws IOException {
            int r = current.read(arg0, arg1, arg2);
            offset += r;
            return r;
        }

        public int read() throws IOException {
            offset++;
            return current.read();
        }

    }

    /**
     * @inheritDoc
     */
    public OutputStream createStorageOutputStream(String name) throws IOException {
        RecordStore r = null;
        RMSOutputStream os = null;
        DataOutputStream out = null;
        try {
            Short key = (Short)fat.get(name);
            if(key == null) {
                // need to add a key to the FAT
                key = new Short(currentKey);
                fat.put(name, key);
                r = RecordStore.openRecordStore("FAT", true);
                byte[] data = toRecord(name, currentKey);
                currentKey++;
                r.addRecord(data, 0, data.length);
                r.closeRecordStore();
                r = null;
            }
            os = new RMSOutputStream(key.shortValue());
            return os;
        } catch(Exception err) {
            cleanup(r);
            cleanup(os);
            cleanup(out);
            throw new IOException(err.getMessage());
        }
    }

    /**
     * @inheritDoc
     */
    public InputStream createStorageInputStream(String name) throws IOException {
        Short key = (Short)fat.get(name);
        if(key == null) {
            return null;
        }

        try {
            return new RMSInputStream(key.shortValue());
        } catch(Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public boolean storageFileExists(String name) {
        if(name == null) {
            return false;
        }
        return fat.containsKey(name);
    }

    /**
     * @inheritDoc
     */
    public String[] listStorageEntries() {
        String[] a = new String[fat.size()];
        Enumeration e = fat.keys();
        int i = 0;
        while(e.hasMoreElements()) {
            a[i] = (String)e.nextElement();
            i++;
        }
        return a;
    }

    /**
     * @inheritDoc
     */
    public String[] listFilesystemRoots() {
        String[] res = enumToStringArr(FileSystemRegistry.listRoots());
        for(int iter = 0 ; iter < res.length ; iter++) {
            res[iter] = "file:///" + res[iter];
        }
        return res;
    }

    private String[] enumToStringArr(Enumeration e) {
        Vector v = new Vector();
        while(e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }
        String[] response = new String[v.size()];
        for(int iter = 0 ; iter < response.length ; iter++) {
            response[iter] = (String)v.elementAt(iter);
        }
        return response;
    }

    /**
     * @inheritDoc
     */
    public String[] listFiles(String directory) throws IOException {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(directory, Connector.READ);
            return enumToStringArr(fc.list());
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public long getRootSizeBytes(String root) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(root, Connector.READ);
            return fc.totalSize();
        } catch(IOException err) {
            err.printStackTrace();
            return -1;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public long getRootAvailableSpace(String root) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(root, Connector.READ);
            return fc.availableSize();
        } catch(IOException err) {
            err.printStackTrace();
            return -1;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public void mkdir(String directory) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(directory, Connector.READ_WRITE);
            fc.mkdir();
        } catch(IOException err) {
            err.printStackTrace();
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public void deleteFile(String file) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.WRITE);
            fc.delete();
        } catch(IOException err) {
            err.printStackTrace();
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public boolean isHidden(String file) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ);
            return fc.isHidden();
        } catch(IOException err) {
            err.printStackTrace();
            return false;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public void setHidden(String file, boolean h) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ_WRITE);
            fc.setHidden(h);
        } catch(IOException err) {
            err.printStackTrace();
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public long getFileLength(String file) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ);
            return fc.fileSize();
        } catch(IOException err) {
            err.printStackTrace();
            return -1;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public boolean isDirectory(String file) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ);
            return fc.isDirectory();
        } catch(IOException err) {
            err.printStackTrace();
            return false;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public char getFileSystemSeparator() {
        return '/';
    }

    /**
     * @inheritDoc
     */
    public boolean exists(String file) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ);
            return fc.exists();
        } catch(IOException err) {
            err.printStackTrace();
            return false;
        } finally {
            cleanup(fc);
        }
    }

    /**
     * @inheritDoc
     */
    public void rename(String file, String newName) {
        FileConnection fc = null;
        try {
            fc = (FileConnection)Connector.open(file, Connector.READ_WRITE);
            fc.rename(newName);
        } catch(IOException err) {
            err.printStackTrace();
        } finally {
            cleanup(fc);
        }
    }
}
