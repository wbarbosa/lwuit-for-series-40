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
 * An interface defining a comparator which compares two records (in an implementation-defined manner) to see if they match or what their relative sort order is. The application implements this interface to compare two candidate records. The return value must indicate the ordering of the two records. The compare method is called by RecordEnumeration to sort and return records in an application specified order. For example: RecordComparator c = new AddressRecordComparator(); if (c.compare(recordStore.getRecord(rec1), recordStore.getRecord(rec2)) == RecordComparator.PRECEDES) return rec1;
 * Since: MIDP 1.0
 */
public interface RecordComparator{
    /**
     * EQUIVALENT means that in terms of search or sort order, the two records are the same. This does not necessarily mean that the two records are identical.
     * The value of EQUIVALENT is 0.
     * See Also:Constant Field Values
     */
    public static final int EQUIVALENT=0;

    /**
     * FOLLOWS means that the left (first parameter) record follows the right (second parameter) record in terms of search or sort order.
     * The value of FOLLOWS is 1.
     * See Also:Constant Field Values
     */
    public static final int FOLLOWS=1;

    /**
     * PRECEDES means that the left (first parameter) record precedes the right (second parameter) record in terms of search or sort order.
     * The value of PRECEDES is -1.
     * See Also:Constant Field Values
     */
    public static final int PRECEDES=-1;

    /**
     * Returns RecordComparator.PRECEDES if rec1 precedes rec2 in sort order, or RecordComparator.FOLLOWS if rec1 follows rec2 in sort order, or RecordComparator.EQUIVALENT if rec1 and rec2 are equivalent in terms of sort order.
     */
    public abstract int compare(byte[] rec1, byte[] rec2);

}
