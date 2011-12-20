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

package javax.microedition.lcdui;
/**
 * This interface is used by applications which need to receive events that indicate changes in the internal state of the interactive items within a Form screen.
 * Since: MIDP 1.0 See Also:Form.setItemStateListener(ItemStateListener)
 */
public interface ItemStateListener{
    /**
     * Called when internal state of an Item has been changed by the user. This happens when the user: changes the set of selected values in a ChoiceGroup; adjusts the value of an interactive Gauge; enters or modifies the value in a TextField; enters a new date or time in a DateField; and
     * was called on an Item.
     * It is up to the device to decide when it considers a new value to have been entered into an Item. For example, implementations of text editing within a TextField vary greatly from device to device.
     * In general, it is not expected that the listener will be called after every change is made. However, if an item's value has been changed, the listener will be called to notify the application of the change before it is called for a change on another item, and before a command is delivered to the Form's CommandListener. For implementations that have the concept of an input focus, the listener should be called no later than when the focus moves away from an item whose state has been changed. The listener should be called only if the item's value has actually been changed.
     * The listener is not called if the application changes the value of an interactive item.
     */
    public abstract void itemStateChanged(Item item);

}
