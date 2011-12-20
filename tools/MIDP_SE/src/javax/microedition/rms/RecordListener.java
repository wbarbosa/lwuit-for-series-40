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
 * A listener interface for receiving Record Changed/Added/Deleted events from a record store.
 * Since: MIDP 1.0 See Also:RecordStore.addRecordListener(javax.microedition.rms.RecordListener)
 */
public interface RecordListener{
    /**
     * Called when a record has been added to a record store.
     */
    public abstract void recordAdded(RecordStore recordStore, int recordId);

    /**
     * Called after a record in a record store has been changed. If the implementation of this method retrieves the record, it will receive the changed version.
     */
    public abstract void recordChanged(RecordStore recordStore, int recordId);

    /**
     * Called after a record has been deleted from a record store. If the implementation of this method tries to retrieve the record from the record store, an InvalidRecordIDException will be thrown.
     */
    public abstract void recordDeleted(RecordStore recordStore, int recordId);

}
