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

import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;

/**
 * Tests how fast drawing can be performed on the device with LWUIT
 *
 * @author Shai Almog
 */
public class GradientRate extends Form {
    private static int averageFramerate;
    
    private static final int TEST_DURATION = 10000;
    private static final int RAMP_UP_DURATION = 1000;
    private long startTime = System.currentTimeMillis();
    private static int paintCalls;
    
    public static int getFramecount() {
        return paintCalls;
    }
    public GradientRate() {
        new Thread() {
            public void run() {
                while(System.currentTimeMillis() - startTime < TEST_DURATION) {
                    repaint();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {}
                }
                averageFramerate = paintCalls / ((TEST_DURATION - RAMP_UP_DURATION) / 1000);
                new BitmapFontRate();
            }
        }.start();
        show();
    }
    
    public static int getAverageFramerate() {
        return averageFramerate;
    }
    
    public void paint(Graphics g) {
        if(System.currentTimeMillis() - startTime > RAMP_UP_DURATION) {
            paintCalls++;
        }
        int colorA = (int)(System.currentTimeMillis() % 0xffffff);
        int colorB = 0;
        if(paintCalls % 2 == 1) {
            colorB = 0xffffff;
            g.fillLinearGradient(colorA, colorB, 0, 0, getWidth(), getHeight(), true);
        } else {
            g.fillLinearGradient(colorA, colorB, 0, 0, getWidth(), getHeight(), false);
        }
        g.fillRadialGradient(colorA, colorB, 0, 0, getWidth(), getHeight());
    }
}
