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

import javax.swing.JFrame;
import javax.swing.JRootPane;


/**
 * This class is based on the old app framework API allowing us to migrate the
 * code to remove the dependency on that framework
 *
 * @author Shai Almog
 */
public class FrameView extends View {
    private JFrame frame = null;
    public FrameView(Application application) {
        super(application);
    }

    public JFrame getFrame() {
	if (frame == null) {
	    frame = new JFrame("Resource Editor");
        frame.getAccessibleContext().setAccessibleName("Resource Editor");
        frame.getAccessibleContext().setAccessibleDescription("Resource Editor");
	}
	return frame;
    }

    public void setFrame(JFrame frame) {
	this.frame = frame;
    }

    public JRootPane getRootPane() {
        return getFrame().getRootPane();
    }
}
