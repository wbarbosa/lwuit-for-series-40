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
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * Tests how fast blitting is performed on the device with LWUIT
 *
 * @author Shai Almog
 */
public class ComponentTest extends Form {
    private static int averageFramerate;
    private FocusMonitor monitor = new FocusMonitor();
    private static final int TEST_DURATION = 10000;
    private static final int RAMP_UP_DURATION = 1000;
    private long startTime = System.currentTimeMillis();
    private static int paintCalls;
    
    public static int getFramecount() {
        return paintCalls;
    }
    private int direction;
    public ComponentTest() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(wrap(new Button("Button 1")));
        addComponent(wrap(new Button("Button 2")));
        addComponent(wrap(new Button("Button 3")));
        addComponent(wrap(new RadioButton("Radio Button 1")));
        addComponent(wrap(new RadioButton("Radio Button 2")));
        addComponent(wrap(new RadioButton("Radio Button 3")));
        addComponent(wrap(new CheckBox("CheckBox 1")));
        addComponent(wrap(new CheckBox("CheckBox 2")));
        addComponent(wrap(new CheckBox("CheckBox 3")));
        addComponent(wrap(new TextField("TextField 1")));
        addComponent(wrap(new TextField("TextField 2")));
        addComponent(wrap(new TextField("TextField 3")));
        addComponent(wrap(new TextArea("TextArea 1")));
        addComponent(wrap(new TextArea("TextArea 2")));
        addComponent(wrap(new TextArea("TextArea 3")));
        addComponent(wrap(new ComboBox(new String[] {"Combo 1", ".......", "......"})));
        addComponent(wrap(new ComboBox(new String[] {"Combo 2", ".......", "......"})));
        addComponent(wrap(new ComboBox(new String[] {"Combo 3", ".......", "......"})));
        
        final int downKey = Display.getInstance().getKeyCode(Display.GAME_DOWN);
        final int upKey = Display.getInstance().getKeyCode(Display.GAME_UP);
        show();
        new Thread() {
            public void run() {
                int count = getComponentCount();
                int position = 0;
                direction = 1;
                while(System.currentTimeMillis() - startTime < TEST_DURATION) {
                    Display.getInstance().callSeriallyAndWait(new Runnable() {
                        public void run() {
                            if(direction > 0) {
                                ComponentTest.this.keyPressed(downKey);
                                ComponentTest.this.keyReleased(downKey);
                            } else {
                                ComponentTest.this.keyPressed(upKey);
                                ComponentTest.this.keyReleased(upKey);
                            }
                        }
                    });
                    if(position >= count) {
                        direction = -1;
                    } else {
                        if(position <= 0) {
                            direction = 1;
                        }
                    }
                }
                averageFramerate = paintCalls / ((TEST_DURATION - RAMP_UP_DURATION) / 1000);
                new ResultsForm();
            }
        }.start();
    }
    
    private Component wrap(Component c) {
        c.addFocusListener(monitor);
        return c;
    }
    
    public static int getAverageFramerate() {
        return averageFramerate;
    }
    
    class FocusMonitor implements FocusListener {

        public void focusGained(Component cmp) {
            if(System.currentTimeMillis() - startTime > RAMP_UP_DURATION) {
                paintCalls++;
            }
        }

        public void focusLost(Component cmp) {
        }
    }
}
