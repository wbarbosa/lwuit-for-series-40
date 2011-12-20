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

package javax.microedition.io.file;

import java.io.*;
import java.util.*;

/**
 * The FileSystemRegistry is a central registry for file system listeners interested in the adding and removing (or mounting and unmounting) of file systems on a device.
 * Since: FileConnection 1.0 See Also:FileConnection, FileSystemListener
 */
public class FileSystemRegistry{
    /**
     * This method is used to register a FileSystemListener that is notified in case of adding and removing a new file system root. Multiple file system listeners can be added. If file systems are not supported on a device, false is returned from the method (this check is performed prior to security checks).
     */
    public static boolean addFileSystemListener(javax.microedition.io.file.FileSystemListener listener){
        return false; //TODO codavaj!!
    }

    /**
     * This method returns the currently mounted root file systems on a device as String objects in an Enumeration. If there are no roots available on the device, a zero length Enumeration is returned. If file systems are not supported on a device, a zero length Enumeration is also returned (this check is performed prior to security checks).
     * The first directory in the file URI is referred to as the root, which corresponds to a logical mount point for a particular storage unit or memory. Root strings are defined by the platform or implementation and can be a string of zero or more characters ("" can be a valid root string on some systems) followed by a trailing "/" to denote that the root is a directory. Each root string is guaranteed to uniquely refer to a root. Root names are device specific and are not required to adhere to any standard. Examples of possible root strings and how to open them include: Possible Root ValueOpening the Root CFCard/ Connector.open("file:///CFCard/"); SDCard/ Connector.open("file:///SDCard/"); MemoryStick/ Connector.open("file:///MemoryStick/"); C:/ Connector.open("file:///C:/"); / Connector.open("file:////");
     * The following is a sample showing the use of listRoots to retrieve the available size of all roots on a device: Enumeration rootEnum = FileSystemRegistry.listRoots(); while (e.hasMoreElements()) { String root = (String) e.nextElement(); FileConnection fc = Connector.open("file:///" + root); System.out.println(fc.availableSize()); }
     */
    public static java.util.Enumeration listRoots(){
        try {
            File f = File.createTempFile("aaaaa", "bbb").getParentFile();
            File rootOne = new File(f, "mainDeviceMemory");
            File rootTwo = new File(f, "sdCard");
            rootOne.mkdirs();
            rootTwo.mkdirs();
            Vector v = new Vector();
            String s = rootOne.toURI().toURL().toExternalForm();
            s = s.substring(s.indexOf('/') + 2);
            v.addElement(s);
            s = rootTwo.toURI().toURL().toExternalForm();
            s = s.substring(s.indexOf('/') + 2);
            v.addElement(s);
            return v.elements();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This method is used to remove a registered FileSystemListener. If file systems are not supported on a device, false is returned from the method.
     */
    public static boolean removeFileSystemListener(javax.microedition.io.file.FileSystemListener listener){
        return false; //TODO codavaj!!
    }

}
