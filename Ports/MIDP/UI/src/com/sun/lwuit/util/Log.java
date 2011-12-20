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
package com.sun.lwuit.util;

import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;


/**
 * Pluggable logging framework that allows a developer to log into storage
 * using the file connector API. It is highly recommended to use this 
 * class coupled with Netbeans preprocessing tags to reduce its overhead
 * completely in runtime.
 *
 * @author Shai Almog
 * @deprecated use LWUIT4IO's log API instead of this class, this class will
 * be removed in the next iteration of LWUIT!
 */
public class Log {
    /**
     * Constant indicating the logging level Debug is the default and the lowest level
     * followed by info, warning and error
     */
    public static final int DEBUG = 1;

    /**
     * Constant indicating the logging level Debug is the default and the lowest level
     * followed by info, warning and error
     */
    public static final int INFO = 2;

    /**
     * Constant indicating the logging level Debug is the default and the lowest level
     * followed by info, warning and error
     */
    public static final int WARNING = 3;

    /**
     * Constant indicating the logging level Debug is the default and the lowest level
     * followed by info, warning and error
     */
    public static final int ERROR = 4;
    
    
    private int level = DEBUG;
    private static Log instance = new Log();
    private long zeroTime = System.currentTimeMillis();
    private Writer output;
    private boolean fileWriteEnabled = false;//System.getProperty("microedition.io.file.FileConnection.version") != null;
    private String fileURL = null;
    
    /**
     * Installs a log subclass that can replace the logging destination/behavior
     * 
     * @param newInstance the new instance for the Log object
     */
    public static void install(Log newInstance) {
        instance = newInstance;
    }
    
    /**
     * Default println method invokes the print instance method, uses DEBUG level
     * 
     * @param text the text to print
     */
    public static void p(String text) {
        p(text, DEBUG);
    }
    
    /**
     * Default println method invokes the print instance method, uses given level
     * 
     * @param text the text to print
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    public static void p(String text, int level) {
        instance.print(text, level);
    }
    
    /**
     * Default log implementation prints to the console and the file connector
     * if applicable. Also prepends the thread information and time before 
     * 
     * @param text the text to print
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    protected void print(String text, int level) {
        if(this.level > level) {
            return;
        }
        text = getThreadAndTimeStamp() + " - " + text;
        System.out.println(text);
        if(isFileWriteEnabled()) {
            try {
                synchronized(this) {
                    Writer w = getWriter();
                    w.write(text + "\n");
                    w.close();
                    output = null;
                }
            } catch(Throwable err) {
                err.printStackTrace();
                setFileWriteEnabled(false);
            }
        } else {
            try {
                synchronized(this) {
                    RecordStore outputStore = RecordStore.openRecordStore("log", true);
                    byte[] bytes = text.getBytes();
                    outputStore.addRecord(bytes, 0, bytes.length);
                    outputStore.closeRecordStore();
                }
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Default method for creating the output writer into which we write, this method
     * creates a simple log file using the file connector
     * 
     * @return writer object
     * @throws IOException when thrown by the connector
     */
    protected Writer createWriter() throws IOException {
        try {
            if(instance.getFileURL() == null) {
                instance.setFileURL("file:///" + FileSystemRegistry.listRoots().nextElement() + "/lwuit.log");
            }
            FileConnection con = (FileConnection)Connector.open(instance.getFileURL(), Connector.READ_WRITE);
            if(con.exists()) {
                return new OutputStreamWriter(con.openOutputStream(con.fileSize()));
            }
            con.create();
            return new OutputStreamWriter(con.openOutputStream());
        } catch(Exception err) {
            setFileWriteEnabled(false);
            // currently return a "dummy" writer so we won't fail on device
            return new OutputStreamWriter(new ByteArrayOutputStream());
        }
    }
    
    private Writer getWriter() throws IOException {
        if(output == null) {
            output = createWriter();
        }
        return output;
    }

    /**
     * Returns a simple string containing a timestamp and thread name.
     * 
     * @return timestamp string for use in the log
     */
    protected String getThreadAndTimeStamp() {
        long time = System.currentTimeMillis() - zeroTime;
        long milli = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60; 
        time /= 60;
        long hour = time % 60; 
        
        return "[" + Thread.currentThread().getName() + "] " + hour  + ":" + min + ":" + sec + "," + milli;
    }
    
    /**
     * Sets the logging level for printing log details, the lower the value 
     * the more verbose would the printouts be
     * 
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    public static void setLevel(int level) {
        instance.level = level;
    }

    /**
     * Returns the logging level for printing log details, the lower the value 
     * the more verbose would the printouts be
     * 
     * @return one of DEBUG, INFO, WARNING, ERROR
     */
    public static int getLevel() {
        return instance.level;
    }
    
    /**
     * Returns the contents of the log as a single long string to be displayed by
     * the application any way it sees fit
     * 
     * @return string containing the whole log
     */
    public static String getLogContent() {
        try {
            String text = "";
            if(instance.isFileWriteEnabled()) {
                if(instance.getFileURL() == null) {
                    instance.setFileURL("file:///" + FileSystemRegistry.listRoots().nextElement() + "/lwuit.log");
                }
                FileConnection con = (FileConnection) Connector.open(instance.getFileURL(),Connector.READ);
                Reader r = new InputStreamReader(con.openInputStream());
                char[] buffer = new char[1024];
                int size = r.read(buffer);
                while(size > -1) {
                    text += new String(buffer, 0, size);
                    size = r.read(buffer);
                }
                r.close();
            } else {
                RecordStore store = RecordStore.openRecordStore("log", true);
                int size = store.getNumRecords();
                for(int iter = 1 ; iter <= size ; iter++) {
                    text += new String(store.getRecord(iter));
                }
                store.closeRecordStore();
            }
            return text;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    /**
     * Places a form with the log as a TextArea on the screen, this method can
     * be attached to appear at a given time or using a fixed global key. Using
     * this method might cause a problem with further log output
     */
    public static void showLog() {
        try {
            String text = getLogContent();
            TextArea area = new TextArea(text, 5, 20);
            Form f = new Form("Log");
            f.setScrollable(false);
            final Form current = Display.getInstance().getCurrent();
            Command back = new Command("Back") {
                public void actionPerformed(ActionEvent ev) {
                    current.show();
                }
            };
            f.addCommand(back);
            f.setBackCommand(back);
            f.setLayout(new BorderLayout());
            f.addComponent(BorderLayout.CENTER, area);
            f.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of the log
     * 
     * @return the singleton instance of the log
     */
    public static Log getInstance() {
        return instance;
    }

    /**
     * Indicates whether GCF's file writing should be used to generate the log file
     *
     * @return the fileWriteEnabled
     */
    public boolean isFileWriteEnabled() {
        return fileWriteEnabled;
    }

    /**
     * Indicates whether GCF's file writing should be used to generate the log file
     * 
     * @param fileWriteEnabled the fileWriteEnabled to set
     */
    public void setFileWriteEnabled(boolean fileWriteEnabled) {
        this.fileWriteEnabled = fileWriteEnabled;
    }

    /**
     * Indicates the URL where the log file is saved
     *
     * @return the fileURL
     */
    public String getFileURL() {
        return fileURL;
    }

    /**
     * Indicates the URL where the log file is saved
     *
     * @param fileURL the fileURL to set
     */
    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
