/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.util;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class RMSHelper {

    private static String rsName = "slidePuzzle"; // name of the record store
    private static RecordStore rs;

    /**
     * Saves a byte array to the record store.
     *
     * @param data Byte array to be saved
     */
    public static void save(byte[] data) {
        // Open the record store
        rs = openRecordStore();

        try {
            /* if there are more than one record, clear record store first */
            if (rs.getNumRecords() != 1 || rs.getNextRecordID() != 2) {
                clearRecordStore();
                rs.addRecord(data, 0, data.length);
            }
            else {
                rs.setRecord(1, data, 0, data.length);
            }
        }
        catch (RecordStoreException rse) {
            rse.printStackTrace();
        }
        close();
    }

    /**
     * Loads the byte array saved in the record store.
     *
     * @return The loaded byte array
     */
    public static byte[] load() {
        rs = openRecordStore();

        try {
            byte[] record = null;
            if (rs.getNumRecords() == 1 && rs.getNextRecordID() == 2) {
                record = rs.getRecord(1);
            }
            close();
            return record;
        }
        catch (RecordStoreException rse) {
            rse.printStackTrace();
        }
        return null;
    }

    /**
     * Clears the record store.
     */
    public static void clearRecordStore() {
        if (rs == null) {
            rs = openRecordStore();
        }

        try {
            if (rs.getNumRecords() == 0) {
                return;
            }
            close();
            RecordStore.deleteRecordStore(rsName);
            openRecordStore();
        }
        catch (RecordStoreException rse) {
            rse.printStackTrace();
        }
    }

    /**
     * Closes the record store.
     */
    public static void close() {
        try {
            if (rs != null) {
                rs.closeRecordStore();
            }
        }
        catch (RecordStoreException rse) {
            rse.printStackTrace();
        }
    }

    private static RecordStore openRecordStore() {
        try {
            return RecordStore.openRecordStore(rsName, true);
        }
        catch (RecordStoreException rse) {
            rse.printStackTrace();
        }
        return null;
    }
}
