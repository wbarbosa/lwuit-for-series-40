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
 * A Control object is used to control some media processing functions. The set of operations are usually functionally related. Thus a Control object provides a logical grouping of media processing functions.
 * Controls are obtained from Controllable. The Player interface extends Controllable. Therefore a Player implementation can use the Control interface to extend its media processing functions. For example, a Player can expose a VolumeControl to allow the volume level to be set.
 * Multiple Controls can be implemented by the same object. For example, an object can implement both VolumeControl and ToneControl. In this case, the object can be used for controlling both the volume and tone generation.
 * The javax.microedition.media.control package specifies a set of pre-defined Controls.
 * See Also:Controllable, Player
 */
public interface Control{
}
