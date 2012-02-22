/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package net.java.dev.lwuit.speed;

import com.sun.lwuit.Button;
import com.sun.lwuit.events.ActionEvent;
import javax.microedition.midlet.MIDlet;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;

/**
 * Main class for the benchmark MIDlet
 * 
 * @author  Shai Almog
 */
public class SpeedMIDlet extends MIDlet {
    private boolean started;
    public static SpeedMIDlet instance;
    protected void startApp() {
        Display.init(this);
        instance = this;

        // distinguish between start and resume from pause
        if(!started) {
            started = true;
                        
            try {
                com.sun.lwuit.util.Resources res = com.sun.lwuit.util.Resources.open("/oceanfishTheme.res");
                com.sun.lwuit.plaf.UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            } catch(java.io.IOException err) {
                err.printStackTrace();
            }
            Form benchForm = new Form("Benchmark");
            benchForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            benchForm.addComponent(new Label("To Begin Press Start"));
            Button start = new Button("Start");
            benchForm.addComponent(start);
            start.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    Display.getInstance().setFramerate(1000);
                    UIManager.getInstance().getLookAndFeel().setDefaultSmoothScrolling(false);
                    new FramerateTest();
                }
            });
            benchForm.show();
        }
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) {
    }
}
