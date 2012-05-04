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

import com.sun.lwuit.resource.util.QuitAction;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;


/**
 * This class is based on the old app framework API allowing us to migrate the
 * code to remove the dependency on that framework
 *
 * @author Shai Almog
 */
public abstract class SingleFrameApplication extends Application {
    private FrameView mainView = null;

    public final JFrame getMainFrame() {
        return getMainView().getFrame();
    }

    protected final void setMainFrame(JFrame mainFrame) {
        getMainView().setFrame(mainFrame);
    }

    protected void show(JComponent c) {
	if (c == null) {
	    throw new IllegalArgumentException("null JComponent");
	}
	JFrame f = getMainFrame();
	f.getContentPane().add(c, BorderLayout.CENTER);
        f.addWindowListener(QuitAction.INSTANCE);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	f.setVisible(true);
    }

    public FrameView getMainView() {
        if (mainView == null) {
            mainView = new FrameView(this);
        }
        return mainView;
    }

    public void show(View view) {
        if ((mainView == null) && (view instanceof FrameView)) {
            mainView = (FrameView)view;
        }
        RootPaneContainer c = (RootPaneContainer)view.getRootPane().getParent();
        if(c instanceof JFrame) {
            QuitAction.INSTANCE.bindFrame((JFrame)c);
        }
	((Window)c).setVisible(true);
    }

}
