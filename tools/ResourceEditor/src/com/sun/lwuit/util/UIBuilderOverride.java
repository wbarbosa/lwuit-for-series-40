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

package com.sun.lwuit.util;

import com.sun.lwuit.resources.editor.editors.ActionCommand;
import com.sun.lwuit.resources.editor.editors.UserInterfaceEditor;

/**
 * Extends the UIBuilder from LWUIT to provide a callback on loading
 *
 * @author Shai Almog
 */
public class UIBuilderOverride extends UIBuilder {
    private UserInterfaceEditor ui;
    private String baseFormName;
    public UIBuilderOverride(UserInterfaceEditor ui) {
        this.ui = ui;
        registerCustom();
    }

    public static void registerCustom() {
        registerCustomComponent("Table", com.sun.lwuit.table.Table.class);
        registerCustomComponent("ContainerList", com.sun.lwuit.list.ContainerList.class);
        registerCustomComponent("ComponentGroup", com.sun.lwuit.ComponentGroup.class);
        registerCustomComponent("Tree", com.sun.lwuit.tree.Tree.class);
        registerCustomComponent("HTMLComponent", com.sun.lwuit.html.HTMLComponent.class);
        registerCustomComponent("RSSReader", com.sun.lwuit.io.ui.RSSReader.class);
        registerCustomComponent("FileTree", com.sun.lwuit.io.ui.FileTree.class);
        registerCustomComponent("WebBrowser", com.sun.lwuit.io.ui.WebBrowser.class);
    }

    void modifyingProperty(com.sun.lwuit.Component c, int p) {
        if(ui != null) {
            ui.setPropertyModified(c, p);
        }
    }

    void modifyingCustomProperty(com.sun.lwuit.Component c, String name) {
        if(ui != null) {
            ui.setCustomPropertyModified(c, name);
        }
    }

    public com.sun.lwuit.Command createCommandImpl(String commandName, com.sun.lwuit.Image icon, int commandId, String action, boolean isBack, String argument) {
        return new ActionCommand(commandName, icon, commandId, action, isBack, argument);
    }

    public static void setIgnorBaseForm(boolean b) {
        ignorBaseForm  = b;
    }

    void initBaseForm(String formName) {
        this.baseFormName = formName;
    }

    /**
     * @return the baseFormName
     */
    public String getBaseFormName() {
        return baseFormName;
    }

    /**
     * @param baseFormName the baseFormName to set
     */
    public void setBaseFormName(String baseFormName) {
        this.baseFormName = baseFormName;
    }

    protected void postCreateComponent(com.sun.lwuit.Component c) {
        c.setPropertyValue("$designMode", Boolean.TRUE);
    }
}
