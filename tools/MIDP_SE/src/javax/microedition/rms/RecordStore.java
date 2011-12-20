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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class representing a record store. A record store consists of a collection of records which will remain persistent across multiple invocations of the MIDlet. The platform is responsible for making its best effort to maintain the integrity of the MIDlet's record stores throughout the normal use of the platform, including reboots, battery changes, etc.
 * Record stores are created in platform-dependent locations, which are not exposed to the MIDlets. The naming space for record stores is controlled at the MIDlet suite granularity. MIDlets within a MIDlet suite are allowed to create multiple record stores, as long as they are each given different names. When a MIDlet suite is removed from a platform all the record stores associated with its MIDlets will also be removed. MIDlets within a MIDlet suite can access each other's record stores directly. New APIs in MIDP 2.0 allow for the explicit sharing of record stores if the MIDlet creating the RecordStore chooses to give such permission.
 * Sharing is accomplished through the ability to name a RecordStore created by another MIDlet suite.
 * RecordStores are uniquely named using the unique name of the MIDlet suite plus the name of the RecordStore. MIDlet suites are identified by the MIDlet-Vendor and MIDlet-Name attributes from the application descriptor.
 * Access controls are defined when RecordStores to be shared are created. Access controls are enforced when RecordStores are opened. The access modes allow private use or shareable with any other MIDlet suite.
 * Record store names are case sensitive and may consist of any combination of between one and 32 Unicode characters inclusive. Record store names must be unique within the scope of a given MIDlet suite. In other words, MIDlets within a MIDlet suite are not allowed to create more than one record store with the same name, however a MIDlet in one MIDlet suite is allowed to have a record store with the same name as a MIDlet in another MIDlet suite. In that case, the record stores are still distinct and separate.
 * No locking operations are provided in this API. Record store implementations ensure that all individual record store operations are atomic, synchronous, and serialized, so no corruption will occur with multiple accesses. However, if a MIDlet uses multiple threads to access a record store, it is the MIDlet's responsibility to coordinate this access or unintended consequences may result. Similarly, if a platform performs transparent synchronization of a record store, it is the platform's responsibility to enforce exclusive access to the record store between the MIDlet and synchronization engine.
 * Records are uniquely identified within a given record store by their recordId, which is an integer value. This recordId is used as the primary key for the records. The first record created in a record store will have recordId equal to one (1). Each subsequent record added to a RecordStore will be assigned a recordId one greater than the record added before it. That is, if two records are added to a record store, and the first has a recordId of 'n', the next will have a recordId of 'n + 1'. MIDlets can create other sequences of the records in the RecordStore by using the RecordEnumeration class.
 * This record store uses long integers for time/date stamps, in the format used by System.currentTimeMillis(). The record store is time stamped with the last time it was modified. The record store also maintains a version number, which is an integer that is incremented for each operation that modifies the contents of the RecordStore. These are useful for synchronization engines as well as other things.
 * Since: MIDP 1.0
 */
public class RecordStore{
    /**
     * Authorization to allow access to any MIDlet suites. AUTHMODE_ANY has a value of 1.
     * See Also:Constant Field Values
     */
    public static final int AUTHMODE_ANY=1;

    /**
     * Authorization to allow access only to the current MIDlet suite. AUTHMODE_PRIVATE has a value of 0.
     * See Also:Constant Field Values
     */
    public static final int AUTHMODE_PRIVATE=0;
    
    private static Map<String, RecordStore> storage = new HashMap<String, RecordStore>();
    private String storeName;
    private Map<Integer, byte[]> data = new HashMap<Integer, byte[]>();
    private List<RecordListener> listeners = new ArrayList<RecordListener>();
    private long lastModified;
    private int version = 1;
    private int nextRecordId = 1;
    
    /**
     * Adds a new record to the record store. The recordId for this new record 
     * is returned. This is a blocking atomic operation. The record is written 
     * to persistent storage before the method returns.
     */
    public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException{
        if(offset != 0 || numBytes != data.length) {
            byte[] newArr = new byte[numBytes];
            System.arraycopy(data, offset, newArr, 0, numBytes);
            data = newArr;
        }
        this.data.put(nextRecordId, data);
        nextRecordId++;
        lastModified = System.currentTimeMillis();
        version++;
        return this.data.size(); //TODO codavaj!!
    }

    /**
     * Adds the specified RecordListener. If the specified listener is already 
     * registered, it will not be added a second time. When a record store is 
     * closed, all listeners are removed.
     */
    public void addRecordListener(RecordListener listener){
        if(listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * This method is called when the MIDlet requests to have the record store 
     * closed. Note that the record store will not actually be closed until 
     * closeRecordStore() is called as many times as openRecordStore() was called. 
     * In other words, the MIDlet needs to make a balanced number of close calls 
     * as open calls before the record store is closed.
     * When the record store is closed, all listeners are removed and all 
     * RecordEnumerations associated with it become invalid. If the MIDlet 
     * attempts to perform operations on the RecordStore object after it has 
     * been closed, the methods will throw a RecordStoreNotOpenException.
     */
    public void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException{
    }

    /**
     * The record is deleted from the record store. The recordId for this record 
     * is NOT reused.
     */
    public void deleteRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
        data.remove(recordId);
        lastModified = System.currentTimeMillis();
        version++;
    }

    /**
     * Deletes the named record store. MIDlet suites are only allowed to delete their own record stores. If the named record store is open (by a MIDlet in this suite or a MIDlet in a different MIDlet suite) when this method is called, a RecordStoreException will be thrown. If the named record store does not exist a RecordStoreNotFoundException will be thrown. Calling this method does NOT result in recordDeleted calls to any registered listeners of this RecordStore.
     */
    public static void deleteRecordStore(java.lang.String recordStoreName) throws RecordStoreException, RecordStoreNotFoundException{
        storage.remove(recordStoreName);
    }

    /**
     * Returns an enumeration for traversing a set of records in the record store 
     * in an optionally specified order. The filter, if non-null, will be used to 
     * determine what subset of the record store records will be used.
     * The comparator, if non-null, will be used to determine the order in which 
     * the records are returned. If both the filter and comparator is null, the 
     * enumeration will traverse all records in the record store in an undefined 
     * order. This is the most efficient way to traverse all of the records in a 
     * record store. If a filter is used with a null comparator, the enumeration 
     * will traverse the filtered records in an undefined order. The first call 
     * to RecordEnumeration.nextRecord() returns the record data from the first 
     * record in the sequence. Subsequent calls to RecordEnumeration.nextRecord() 
     * return the next consecutive record's data. To return the record data from 
     * the previous consecutive from any given point in the enumeration, call 
     * previousRecord(). On the other hand, if after creation the first call is 
     * to previousRecord(), the record data of the last element of the 
     * enumeration will be returned. Each subsequent call to previousRecord() 
     * will step backwards through the sequence.
     */
    public RecordEnumeration enumerateRecords(final RecordFilter filter, final RecordComparator comparator, final boolean keep) throws RecordStoreNotOpenException {
        final Integer[] keys = new Integer[data.size()];
        data.keySet().toArray(keys);
        return new RecordEnumeration() {
            private int offset = 0;
            private boolean updated = keep;
            
            /**
             * Frees internal resources used by this RecordEnumeration. MIDlets 
             * should call this method when they are done using a RecordEnumeration. 
             * If a MIDlet tries to use a RecordEnumeration after this method has 
             * been called, it will throw a IllegalStateException. Note that 
             * this method is used for manually aiding in the minimization of 
             * immediate resource requirements when this enumeration is no longer needed.
             */
            public void destroy() {
            }

            /**
             * Returns true if more elements exist in the next direction.
             */
            public boolean hasNextElement() {
                return offset < data.size();
            }

            /**
             * Returns true if more elements exist in the previous direction.
             */
            public boolean hasPreviousElement() {
                return offset > 0;
            }

            /**
             * Returns true if the enumeration keeps its enumeration current with 
             * any changes in the records.
             */
            public boolean isKeptUpdated() {
                return updated;
            }

            /**
             * Used to set whether the enumeration will be keep its internal index 
             * up to date with the record store record additions/deletions/changes. 
             * Note that this should be used carefully due to the potential performance problems associated with maintaining the enumeration with every change.
             */
            public void keepUpdated(boolean keepUpdated) {
                updated = keepUpdated;
            }

            /**
             * Returns a copy of the next record in this enumeration, where next 
             * is defined by the comparator and/or filter supplied in the 
             * constructor of this enumerator. The byte array returned is a copy 
             * of the record. Any changes made to this array will NOT be reflected 
             * in the record store. After calling this method, the enumeration is 
             * advanced to the next available record.
             */
            public byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
                byte[] recData = data.get(keys[offset]);
                offset++;
                return recData;
            }

            /**
             * Returns the recordId of the next record in this enumeration, where 
             * next is defined by the comparator and/or filter supplied in the 
             * constructor of this enumerator. After calling this method, the 
             * enumeration is advanced to the next available record.
             */
            public int nextRecordId() throws InvalidRecordIDException {
                int id = keys[offset];
                offset++;
                return id;
            }

            /**
             * Returns the number of records available in this enumeration's set. 
             * That is, the number of records that have matched the filter criterion. 
             * Note that this forces the RecordEnumeration to fully build the 
             * enumeration by applying the filter to all records, which may take 
             * a non-trivial amount of time if there are a lot of records in the record store.
             */
            public int numRecords() {
                return keys.length;
            }

            /**
             * Returns a copy of the previous record in this enumeration, where 
             * previous is defined by the comparator and/or filter supplied in 
             * the constructor of this enumerator. The byte array returned is a 
             * copy of the record. Any changes made to this array will NOT be 
             * reflected in the record store. After calling this method, the 
             * enumeration is advanced to the next (previous) available record.
             */
            public byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
                byte[] d = data.get(keys[offset]);
                offset--;
                return d;
            }

            /**
             * Returns the recordId of the previous record in this enumeration, 
             * where previous is defined by the comparator and/or filter supplied 
             * in the constructor of this enumerator. After calling this method, 
             * the enumeration is advanced to the next (previous) available record.
             */
            public int previousRecordId() throws InvalidRecordIDException {
                int id = keys[offset];
                offset--;
                return id;
            }

            /**
             * Request that the enumeration be updated to reflect the current record 
             * set. Useful for when a MIDlet makes a number of changes to the record 
             * store, and then wants an existing RecordEnumeration to enumerate the 
             * new changes.
             */
            public void rebuild() {
            }

            /**
             * Returns the enumeration index to the same state as right after the 
             * enumeration was created.
             */
            public void reset() {
                offset = 0;
            }
        }; 
    }

    /**
     * Returns the last time the record store was modified, in the format used by System.currentTimeMillis().
     */
    public long getLastModified() throws RecordStoreNotOpenException{
        return lastModified; 
    }

    /**
     * Returns the name of this RecordStore.
     */
    public java.lang.String getName() throws RecordStoreNotOpenException{
        return storeName;
    }

    /**
     * Returns the recordId of the next record to be added to the record store. This can be useful for setting up pseudo-relational relationships. That is, if you have two or more record stores whose records need to refer to one another, you can predetermine the recordIds of the records that will be created in one record store, before populating the fields and allocating the record in another record store. Note that the recordId returned is only valid while the record store remains open and until a call to addRecord().
     */
    public int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException{
        return nextRecordId; 
    }

    /**
     * Returns the number of records currently in the record store.
     */
    public int getNumRecords() throws RecordStoreNotOpenException{
        return data.size(); 
    }

    /**
     * Returns a copy of the data stored in the given record.
     */
    public byte[] getRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
        return data.get(recordId); 
    }

    /**
     * Returns the data stored in the given record.
     */
    public int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
        byte[] d = data.get(recordId);
        System.arraycopy(d, 0, buffer, offset, d.length);
        return d.length; 
    }

    /**
     * Returns the size (in bytes) of the MIDlet data available in the given record.
     */
    public int getRecordSize(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
        return data.get(recordId).length; 
    }

    /**
     * Returns the amount of space, in bytes, that the record store occupies. 
     * The size returned includes any overhead associated with the implementation, 
     * such as the data structures used to hold the state of the record store, etc.
     */
    public int getSize() throws RecordStoreNotOpenException{
        int size = 0;
        for(byte[] a : data.values()) {
            size += a.length;
        }
        return size; 
    }

    /**
     * Returns the amount of additional room (in bytes) available for this record store to grow. Note that this is not necessarily the amount of extra MIDlet-level data which can be stored, as implementations may store additional data structures with each record to support integration with native applications, synchronization, etc.
     */
    public int getSizeAvailable() throws RecordStoreNotOpenException{
        return 128 * 1024; 
    }

    /**
     * Each time a record store is modified (by addRecord, setRecord, or deleteRecord methods) its version is incremented. This can be used by MIDlets to quickly tell if anything has been modified. The initial version number is implementation dependent. The increment is a positive integer greater than 0. The version number increases only when the RecordStore is updated. The increment value need not be constant and may vary with each update.
     */
    public int getVersion() throws RecordStoreNotOpenException{
        return 0; 
    }

    /**
     * Returns an array of the names of record stores owned by the MIDlet suite. Note that if the MIDlet suite does not have any record stores, this function will return null. The order of RecordStore names returned is implementation dependent.
     */
    public static java.lang.String[] listRecordStores(){
        String[] s = new String[storage.size()];
        storage.keySet().toArray(s);
        return s;
    }

    /**
     * Open (and possibly create) a record store associated with the given MIDlet suite. If this method is called by a MIDlet when the record store is already open by a MIDlet in the MIDlet suite, this method returns a reference to the same RecordStore object.
     */
    public static RecordStore openRecordStore(java.lang.String recordStoreName, boolean createIfNecessary) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException{
        RecordStore r = storage.get(recordStoreName);
        if(r == null) {
            if(!createIfNecessary) {
                throw new RecordStoreNotFoundException(recordStoreName + " not found");
            }
            r = new RecordStore();
            r.storeName = recordStoreName;
            storage.put(recordStoreName, r);
        }
        return r;
    }

    /**
     * Open (and possibly create) a record store that can be shared with other MIDlet suites. The RecordStore is owned by the current MIDlet suite. The authorization mode is set when the record store is created, as follows: AUTHMODE_PRIVATE - Only allows the MIDlet suite that created the RecordStore to access it. This case behaves identically to openRecordStore(recordStoreName, createIfNecessary). AUTHMODE_ANY - Allows any MIDlet to access the RecordStore. Note that this makes your recordStore accessible by any other MIDlet on the device. This could have privacy and security issues depending on the data being shared. Please use carefully.
     * The owning MIDlet suite may always access the RecordStore and always has access to write and update the store.
     * If this method is called by a MIDlet when the record store is already open by a MIDlet in the MIDlet suite, this method returns a reference to the same RecordStore object.
     */
    public static RecordStore openRecordStore(java.lang.String recordStoreName, boolean createIfNecessary, int authmode, boolean writable) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException{
        return openRecordStore(recordStoreName, createIfNecessary);
    }

    /**
     * Open a record store associated with the named MIDlet suite. The MIDlet suite is identified by MIDlet vendor and MIDlet name. Access is granted only if the authorization mode of the RecordStore allows access by the current MIDlet suite. Access is limited by the authorization mode set when the record store was created: AUTHMODE_PRIVATE - Succeeds only if vendorName and suiteName identify the current MIDlet suite; this case behaves identically to openRecordStore(recordStoreName, createIfNecessary). AUTHMODE_ANY - Always succeeds. Note that this makes your recordStore accessible by any other MIDlet on the device. This could have privacy and security issues depending on the data being shared. Please use carefully. Untrusted MIDlet suites are allowed to share data but this is not recommended. The authenticity of the origin of untrusted MIDlet suites cannot be verified so shared data may be used unscrupulously.
     * If this method is called by a MIDlet when the record store is already open by a MIDlet in the MIDlet suite, this method returns a reference to the same RecordStore object.
     * If a MIDlet calls this method to open a record store from its own suite, the behavior is identical to calling: openRecordStore(recordStoreName, false)
     */
    public static RecordStore openRecordStore(java.lang.String recordStoreName, java.lang.String vendorName, java.lang.String suiteName) throws RecordStoreException, RecordStoreNotFoundException{
        return openRecordStore(recordStoreName, true);
    }

    /**
     * Removes the specified RecordListener. If the specified listener is not registered, this method does nothing.
     */
    public void removeRecordListener(RecordListener listener){
        listeners.remove(listener);
    }

    /**
     * Changes the access mode for this RecordStore. The authorization mode choices are: AUTHMODE_PRIVATE - Only allows the MIDlet suite that created the RecordStore to access it. This case behaves identically to openRecordStore(recordStoreName, createIfNecessary). AUTHMODE_ANY - Allows any MIDlet to access the RecordStore. Note that this makes your recordStore accessible by any other MIDlet on the device. This could have privacy and security issues depending on the data being shared. Please use carefully.
     * The owning MIDlet suite may always access the RecordStore and always has access to write and update the store. Only the owning MIDlet suite can change the mode of a RecordStore.
     */
    public void setMode(int authmode, boolean writable) throws RecordStoreException{
        
    }

    /**
     * Sets the data in the given record to that passed in. After this method returns, a call to getRecord(int recordId) will return an array of numBytes size containing the data supplied here.
     */
    public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, RecordStoreFullException{
        if(offset != 0 || numBytes != newData.length) {
            byte[] newArr = new byte[numBytes];
            System.arraycopy(newData, offset, newArr, 0, numBytes);
            newData = newArr;
        }
        data.put(recordId, newData);
        lastModified = System.currentTimeMillis();
        version++;
    }

}
