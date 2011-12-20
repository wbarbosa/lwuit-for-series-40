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
 * This interface is used by applications which need to receive high-level events from the implementation. An application will provide an implementation of a CommandListener (typically by using a nested class or an inner class) and will then provide the instance to the addCommand method on a Displayable in order to receive high-level events on that screen.
 * The specification does not require the platform to create several threads for the event delivery. Thus, if a CommandListener method does not return or the return is not delayed, the system may be blocked. So, there is the following note to application developers:
 * Since: MIDP 1.0 See Also:Displayable.setCommandListener(javax.microedition.lcdui.CommandListener)
 */
public interface CommandListener{
    /**
     * Indicates that a command event has occurred on Displayable d.
     */
    public abstract void commandAction(Command c, Displayable d);

}
