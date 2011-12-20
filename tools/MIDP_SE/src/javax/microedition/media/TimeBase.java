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
 * A TimeBase is a constantly ticking source of time. It measures the progress of time and provides the basic means for synchronizing media playback for Players.
 * A TimeBase measures time in microseconds in order to provide the necessary resolution for synchronization. It is acknowledged that some implementations may not be able to support time resolution in the microseconds range. For such implementations, the internal representation of time can be done within their limits. But the time reported via the API must be scaled to the microseconds range.
 * Manager.getSystemTimeBase provides the default TimeBase used by the system.
 * See Also:Player
 */
public interface TimeBase{
    /**
     * Get the current time of this TimeBase. The values returned must be non-negative and non-decreasing over time.
     */
    abstract long getTime();

}
