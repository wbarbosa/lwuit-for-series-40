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

package javax.microedition.rms;
/**
 * An interface representing a bidirectional record store Record enumerator. The RecordEnumeration logically maintains a sequence of the recordId's of the records in a record store. The enumerator will iterate over all (or a subset, if an optional record filter has been supplied) of the records in an order determined by an optional record comparator.
 * By using an optional RecordFilter, a subset of the records can be chosen that match the supplied filter. This can be used for providing search capabilities.
 * By using an optional RecordComparator, the enumerator can index through the records in an order determined by the comparator. This can be used for providing sorting capabilities.
 * If, while indexing through the enumeration, some records are deleted from the record store, the recordId's returned by the enumeration may no longer represent valid records. To avoid this problem, the RecordEnumeration can optionally become a listener of the RecordStore and react to record additions and deletions by recreating its internal index. Use special care when using this option however, in that every record addition, change and deletion will cause the index to be rebuilt, which may have serious performance impacts.
 * If the RecordStore used by this RecordEnumeration is closed, this RecordEnumeration becomes invalid and all subsequent operations performed on it may give invalid results or throw a RecordStoreNotOpenException, even if the same RecordStore is later opened again. In addition, calls to hasNextElement() and hasPreviousElement() will return false.
 * The first call to nextRecord() returns the record data from the first record in the sequence. Subsequent calls to nextRecord() return the next consecutive record's data. To return the record data from the previous consecutive from any given point in the enumeration, call previousRecord(). On the other hand, if after creation, the first call is to previousRecord(), the record data of the last element of the enumeration will be returned. Each subsequent call to previousRecord() will step backwards through the sequence until the beginning is reached.
 * Final note, to do record store searches, create a RecordEnumeration with no RecordComparator, and an appropriate RecordFilter with the desired search criterion.
 * Since: MIDP 1.0
 */
public interface RecordEnumeration{
    /**
     * Frees internal resources used by this RecordEnumeration. MIDlets should call this method when they are done using a RecordEnumeration. If a MIDlet tries to use a RecordEnumeration after this method has been called, it will throw a IllegalStateException. Note that this method is used for manually aiding in the minimization of immediate resource requirements when this enumeration is no longer needed.
     */
    public abstract void destroy();

    /**
     * Returns true if more elements exist in the next direction.
     */
    public abstract boolean hasNextElement();

    /**
     * Returns true if more elements exist in the previous direction.
     */
    public abstract boolean hasPreviousElement();

    /**
     * Returns true if the enumeration keeps its enumeration current with any changes in the records.
     */
    public abstract boolean isKeptUpdated();

    /**
     * Used to set whether the enumeration will be keep its internal index up to date with the record store record additions/deletions/changes. Note that this should be used carefully due to the potential performance problems associated with maintaining the enumeration with every change.
     */
    public abstract void keepUpdated(boolean keepUpdated);

    /**
     * Returns a copy of the next record in this enumeration, where next is defined by the comparator and/or filter supplied in the constructor of this enumerator. The byte array returned is a copy of the record. Any changes made to this array will NOT be reflected in the record store. After calling this method, the enumeration is advanced to the next available record.
     */
    public abstract byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;

    /**
     * Returns the recordId of the next record in this enumeration, where next is defined by the comparator and/or filter supplied in the constructor of this enumerator. After calling this method, the enumeration is advanced to the next available record.
     */
    public abstract int nextRecordId() throws InvalidRecordIDException;

    /**
     * Returns the number of records available in this enumeration's set. That is, the number of records that have matched the filter criterion. Note that this forces the RecordEnumeration to fully build the enumeration by applying the filter to all records, which may take a non-trivial amount of time if there are a lot of records in the record store.
     */
    public abstract int numRecords();

    /**
     * Returns a copy of the previous record in this enumeration, where previous is defined by the comparator and/or filter supplied in the constructor of this enumerator. The byte array returned is a copy of the record. Any changes made to this array will NOT be reflected in the record store. After calling this method, the enumeration is advanced to the next (previous) available record.
     */
    public abstract byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;

    /**
     * Returns the recordId of the previous record in this enumeration, where previous is defined by the comparator and/or filter supplied in the constructor of this enumerator. After calling this method, the enumeration is advanced to the next (previous) available record.
     */
    public abstract int previousRecordId() throws InvalidRecordIDException;

    /**
     * Request that the enumeration be updated to reflect the current record set. Useful for when a MIDlet makes a number of changes to the record store, and then wants an existing RecordEnumeration to enumerate the new changes.
     */
    public abstract void rebuild();

    /**
     * Returns the enumeration index to the same state as right after the enumeration was created.
     */
    public abstract void reset();

}
