/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.util;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.models.Guide;
import java.io.*;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * Class that helps to save purchased guides to RMS and load them from there.
 */
public class GuideSaver {

    private TouristMidlet midlet;

    public GuideSaver(TouristMidlet midlet) {
        this.midlet = midlet;
    }

    /**
     * Save guides to RMS.
     */
    public final void saveGuides() {
        try {
            RecordStore store = RecordStore.openRecordStore("Guides", true);
            if (store.getNumRecords() == 0) {
                store.addRecord(null, 0, 0);
            }
            
            byte[] data = guidesState();
            store.setRecord(getRecordId(store), data, 0, data.length);
            store.closeRecordStore();
        }
        catch (Exception e) {
            try {
                RecordStore.deleteRecordStore("Guides");
            }
            catch (RecordStoreException rse) {
                rse.printStackTrace();
            }
        }
    }

    /**
     * Load guides from RMS.
     */
    public final Vector loadGuides() {
        try {
            RecordStore store = RecordStore.openRecordStore("Guides", true);
            if (store.getNumRecords() == 0) {
                return new Vector();
            }
            
            Vector newGuides = loadGuides(store.getRecord(getRecordId(store)));
            store.closeRecordStore();
            return newGuides;
        }
        catch (RecordStoreException e) {
            return new Vector();
        }
    }

    private int getRecordId(RecordStore store)
        throws RecordStoreException {
        RecordEnumeration e = store.enumerateRecords(null, null, false);
        
        try {
            return e.nextRecordId();
        }
        finally {
            e.destroy();
        }
    }

    private byte[] guidesState() {
        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            Vector guides =
                midlet.getGuideView().getGuideList().getListModel().getItems();
            int size = guides.size();
            dout.writeInt(size - 1);
            
            for (int i = 1; i < size; i++) {
                ((Guide) guides.elementAt(i)).writeTo(dout);
            }
            return bout.toByteArray();
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (bout != null) {
                    bout.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    private Vector loadGuides(byte[] record) {
        if (record == null) {
            return new Vector();
        }
        try {
            DataInputStream din = new DataInputStream(new ByteArrayInputStream(
                record));
            Vector newGuides = new Vector();
            
            int size = din.readInt();
            while (size > 0) {
                newGuides.addElement(Guide.readFrom(din));
                size--;
            }
            return newGuides;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new Vector();
    }
}
