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

package javax.microedition.media.control;
/**
 * GUIControl extends Control and is defined for controls that provide GUI functionalities.
 * Controls that support a GUI component should implement this interface.
 */
public interface GUIControl extends javax.microedition.media.Control{
    /**
     * This defines a mode on how the GUI is displayed. It is used in conjunction with
     * .
     * When USE_GUI_PRIMITIVE is specified for initDisplayMode, a GUI primitive will be returned. This object is where the GUI of this control will be displayed. It can be used in conjunction with other GUI objects, and conforms to the GUI behaviors as specified by the platform.
     * For a given platform, the object returned must implement or extend from the appropriate GUI primitive of the platform. For platforms that support only AWT such as some CDC implementations, the object must extend from java.awt.Component; for MIDP implementations with only LCDUI support, it must extend from javax.microedition.lcdui.Item.
     * In these cases, the arg argument must be null or a String that specifies the fully-qualified classname of the GUI primitive.
     * On some platforms that support multiple types of GUI primitives, the arg argument must be used to arbitrate among the options. The arg argument must be a String that specifies the fully-qualified classname of the GUI primitive to be returned by the method.
     * For example, a platform that supports both AWT and LCDUI must use either "java.awt.Component" or "javax.microedition.lcdui.Item" as the arg argument. The object returned will be of either type according to what's specified.
     * Here are some sample usage scenarios:
     * For CDC implementations with only AWT support: try { Player p = Manager.createPlayer("http://abc.mpg"); p.realize(); GUIControl gc; if ((gc = (GUIControl)p.getControl("GUIControl")) != null) add((Component)gc.initDisplayMode(GUIControl.USE_GUI_PRIMITIVE, null)); p.start(); } catch (MediaException pe) { } catch (IOException ioe) { }
     * For MIDP implementations with only LCDUI support: try { Player p = Manager.createPlayer("http://abc.mpg"); p.realize(); GUIControl gc; if ((gc = (GUIControl)p.getControl("GUIControl")) != null) { Form form = new Form("My GUI"); form.append((Item)gc.initDisplayMode(GUIControl.USE_GUI_PRIMITIVE, null)); Display.getDisplay().setCurrent(form); } p.start(); } catch (MediaException pe) { } catch (IOException ioe) { }
     * For implementations with both AWT and LCDUI support: try { Player p = Manager.createPlayer("http://abc.mpg"); p.realize(); GUIControl gc; if ((gc = (GUIControl)p.getControl("GUIControl")) != null) add((Component)gc.initDisplayMode(GUIControl.USE_GUI_PRIMITIVE, "java.awt.Component"); p.start(); } catch (MediaException pe) { } catch (IOException ioe) { }
     * Value 0 is assigned to USE_GUI_PRIMITIVE.
     * See Also:Constant Field Values
     */
    static final int USE_GUI_PRIMITIVE=0;

    /**
     * Initialize the mode on how the GUI is displayed.
     */
    abstract java.lang.Object initDisplayMode(int mode, java.lang.Object arg);

}
