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

import com.sun.lwuit.resources.editor.ResourceEditorView;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This class is based on the old app framework API allowing us to migrate the
 * code to remove the dependency on that framework
 *
 * @author Shai Almog
 */
public abstract class Application {
    private static Application application = null;
    private static JWindow splash;

    protected Application() {
    }

    public static Application getInstance() {
        return application;
    }
    
    public static synchronized <T extends Application> void launch(final Application app, final String[] args) {
        application = app;
        splash = new JWindow((java.awt.Frame)null);
        java.awt.Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        splash.setLocation(d.width / 2 - 250, d.height / 2 - 250);
        splash.setLayout(new BorderLayout());
        splash.add(BorderLayout.CENTER, new JLabel(new ImageIcon(Application.class.getResource("/resource-splash1-w-copyr.png"))));
        splash.setSize(new java.awt.Dimension(500, 445));
        splash.setVisible(true);
	Runnable doCreateAndShowGUI = new Runnable() {
	    public void run() {
		try {
		    create(application);
                    application.initialize(args);
		    application.startup();
                    splash.dispose();
		}
		catch (Exception e) {
                    e.printStackTrace();
		}
	    }
	};
	SwingUtilities.invokeLater(doCreateAndShowGUI);
    }


    static Application create(Application application) throws Exception {
        try {
            System.setProperty("java.net.useSystemProxies", "true"); 
        }
        catch (SecurityException ignoreException) {
        }

        try {
            String plaf = Preferences.userNodeForPackage(ResourceEditorView.class).get("plaf", UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(plaf);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return application;
    }


    protected void initialize(String[] args) {
    }

    protected abstract void startup();

    protected void ready() {
    }

    protected void shutdown() {
    }


}
