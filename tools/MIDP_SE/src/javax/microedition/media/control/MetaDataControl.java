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

package javax.microedition.media.control;
/**
 * MetaDataControl is used to retrieve metadata information included within the media streams. A MetaDataControl object recognizes and stores metadata and provides XML-like accessor methods to retrieve this information. Predefined keys are provided to refer to commonly used metadata fields (title, copyright, data, author).
 */
public interface MetaDataControl extends javax.microedition.media.Control{
    /**
     * Default key for AUTHOR information.
     * Value "author" is assigned to AUTHOR_KEY.
     * See Also:Constant Field Values
     */
    static final java.lang.String AUTHOR_KEY="author";

    /**
     * Default key for COPYRIGHT information.
     * Value "copyright" is assigned to COPYRIGHT_KEY.
     * See Also:Constant Field Values
     */
    static final java.lang.String COPYRIGHT_KEY="copyright";

    /**
     * Default key for DATE information.
     * Value "date" is assigned to DATE_KEY.
     * See Also:Constant Field Values
     */
    static final java.lang.String DATE_KEY="date";

    /**
     * Default key for TITLE information.
     * Value "title" is assigned to TITLE_KEY.
     * See Also:Constant Field Values
     */
    static final java.lang.String TITLE_KEY="title";

    /**
     * Return the list of keys for the available metadata values. The returned array must be an array with at least one key.
     */
    abstract java.lang.String[] getKeys();

    /**
     * Retrieve the value found in the metadata associated with the given key. Only keys obtained from getKeys are valid and can be used to retrieve metadata values. If null or an invalid key is used, an IllegalArgumentException will be thrown.
     * Some keys are valid but the associated metadata may not be available before a certain portion of the media is played. For example, some streaming media types may contain metadata that's stored at the end of the file. As a result, the metadata may not be available until the playback reaches the end of media. When that happens, calling getKeyValues with those keys will return null before the data is available. However, when the playback reaches the end of media, all metadata values must be made available.
     */
    abstract java.lang.String getKeyValue(java.lang.String key);

}
