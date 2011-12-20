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


package org.jdesktop.application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

/**
 * This class is based on the old app framework API allowing us to migrate the
 * code to remove the dependency on that framework
 *
 * @author Shai Almog
 */
public class View  {
    private final Application application;
    //private ResourceMap resourceMap = null;
    private JRootPane rootPane = null; 
    private JComponent component = null;
    private JMenuBar menuBar = null;
    private List<JToolBar> toolBars = Collections.emptyList();
    private JComponent toolBarsPanel = null;
    private JComponent statusBar = null;

    public View(Application application) {
        this.application = application;
    }

    public final Application getApplication() {
        return application;
    }

    /*public final ApplicationContext getContext() {
        return getApplication().getContext();
    }*/

    /*public ResourceMap getResourceMap() {
        if (resourceMap == null) {
            resourceMap = getContext().getResourceMap(getClass(), View.class);
        }
        return resourceMap;
    }*/

    public JRootPane getRootPane() {
        if (rootPane == null) {
            rootPane = new JRootPane();
            rootPane.setOpaque(true);
        }
        return rootPane;
    }

    private void replaceContentPaneChild(JComponent oldChild, JComponent newChild, String constraint) {
        Container contentPane = getRootPane().getContentPane();
        if (oldChild != null) {
            contentPane.remove(oldChild);
        }
        if (newChild != null) {
            contentPane.add(newChild, constraint);
        }
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        JComponent oldValue = this.component;
        this.component = component;
        replaceContentPaneChild(oldValue, this.component, BorderLayout.CENTER);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
        getRootPane().setJMenuBar(menuBar);
    }

    public List<JToolBar> getToolBars() {
        return toolBars;
    }

    public void setToolBars(List<JToolBar> toolBars) {
        this.toolBars = new ArrayList<JToolBar>(toolBars);
        JComponent oldToolBarsPanel = this.toolBarsPanel;
        JComponent newToolBarsPanel = null;
        if (this.toolBars.size() == 1) {
            newToolBarsPanel = toolBars.get(0);
        }
        else if (this.toolBars.size() > 1) {
            newToolBarsPanel = new JPanel();
            for(JComponent toolBar : this.toolBars) {
                newToolBarsPanel.add(toolBar);
            }
        }
        replaceContentPaneChild(oldToolBarsPanel, newToolBarsPanel, BorderLayout.NORTH);
    }

    public final JToolBar getToolBar() {
        List<JToolBar> toolBars = getToolBars();
        return (toolBars.size() == 0) ? null : toolBars.get(0);
    }

    public final void setToolBar(JToolBar toolBar) {
        List<JToolBar> toolBars = Collections.emptyList();
        if (toolBar != null) {
            toolBars = Collections.singletonList(toolBar);
        }
        setToolBars(toolBars);
    }

    public JComponent getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(JComponent statusBar) {
        JComponent oldValue = this.statusBar;
        this.statusBar = statusBar;
        replaceContentPaneChild(oldValue, this.statusBar, BorderLayout.SOUTH);        
    }

}
