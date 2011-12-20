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
 * The common superclass of all high-level user interface classes. The contents displayed and their interaction with the user are defined by subclasses.
 * Using subclass-defined methods, the application may change the contents of a Screen object while it is shown to the user. If this occurs, and the Screen object is visible, the display will be updated automatically. That is, the implementation will refresh the display in a timely fashion without waiting for any further action by the application. For example, suppose a List object is currently displayed, and every element of the List is visible. If the application inserts a new element at the beginning of the List, it is displayed immediately, and the other elements will be rearranged appropriately. There is no need for the application to call another method to refresh the display.
 * It is recommended that applications change the contents of a Screen only while it is not visible (that is, while another Displayable is current). Changing the contents of a Screen while it is visible may result in performance problems on some devices, and it may also be confusing if the Screen's contents changes while the user is interacting with it.
 * In MIDP 2.0 the four Screen methods that defined read/write ticker and title properties were moved to Displayable, Screen's superclass. The semantics of these methods have not changed.
 * Since: MIDP 1.0
 */
public abstract class Screen extends Displayable{
}
