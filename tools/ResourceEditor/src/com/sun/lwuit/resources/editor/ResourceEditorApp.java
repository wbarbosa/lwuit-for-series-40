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

package com.sun.lwuit.resources.editor;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.NetworkManager;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.Arrays;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 *
 * @author Shai Almog
 */
public class ResourceEditorApp extends SingleFrameApplication {
    private File fileToLoad;
        
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new ResourceEditorView(this, fileToLoad));
        Image large = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/application64.png"));
        Image small = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/application48.png"));
        try {
            // setIconImages is only available in JDK 1.6
            getMainFrame().setIconImages(Arrays.asList(new Image[] {large, small}));
        } catch (Throwable err) {
            getMainFrame().setIconImage(small);
        }
    }

    @Override 
    protected void initialize(String[] argv) {
        if(argv != null && argv.length > 0) {
            File f = new File(argv[0]);
            if(f.exists()) {
                fileToLoad = f;
            }
        }
    }
 
    /**
     * A convenient static getter for the application instance.
     * @return the instance of ResourceEditorApp
     */
    public static ResourceEditorApp getApplication() {
        return (ResourceEditorApp) Application.getInstance();
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        Display.init(null);
        NetworkManager.getInstance().start();
        launch(new ResourceEditorApp(), args);
    }
}
