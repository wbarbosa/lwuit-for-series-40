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

package net.java.lwuit.demo.iodemo;

import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.services.GoogleRESTService;
import com.sun.lwuit.layouts.BoxLayout;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.*;

/**
 * Main demo MIDlet
 *
 * @author Shai Almog
 */
public class MainMIDlet extends MIDlet implements ActionListener {
    public void startApp() {
        Display.init(this);
        Display.getInstance().callSerially(new Main(new Command("Exit") {
            public void actionPerformed(ActionEvent ev) {
                notifyDestroyed();
            }
        }, this));
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            List l = (List) evt.getSource();
            GoogleRESTService.ResultEntry e = (GoogleRESTService.ResultEntry) l.getSelectedItem();
            if(e.getStaticMapUrl() != null) {
                platformRequest(e.getStaticMapUrl());
            } else {
                platformRequest(e.getUrl());
            }
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
