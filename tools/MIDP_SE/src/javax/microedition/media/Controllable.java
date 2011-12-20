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

package javax.microedition.media;
/**
 * Controllable provides an interface for obtaining the Controls from an object like a Player. It provides methods to query all the supported Controls and to obtain a particular Control based on its class name.
 */
public interface Controllable{
    /**
     * Obtain the object that implements the specified Control interface.
     * If the specified Control interface is not supported then null is returned.
     * If the Controllable supports multiple objects that implement the same specified Control interface, only one of them will be returned. To obtain all the Control's of that type, use the getControls method and check the list for the requested type.
     */
    abstract javax.microedition.media.Control getControl(java.lang.String controlType);

    /**
     * Obtain the collection of Controls from the object that implements this interface.
     * Since a single object can implement multiple Control interfaces, it's necessary to check each object against different Control types. For example:
     * Controllable controllable; : Control cs[]; cs = controllable.getControls(); for (int i = 0; i < cs.length; i++) { if (cs[i] instanceof ControlTypeA) doSomethingA(); if (cs[i] instanceof ControlTypeB) doSomethingB(); // etc. }
     * The list of Control objects returned will not contain any duplicates. And the list will not change over time.
     * If no Control is supported, a zero length array is returned.
     */
    abstract javax.microedition.media.Control[] getControls();

}
