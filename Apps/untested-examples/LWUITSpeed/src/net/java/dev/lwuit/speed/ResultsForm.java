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

import com.sun.lwuit.Command;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.M3G;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.GridLayout;

/**
 * Simple form outlining the results of the run
 *
 * @author Shai Almog
 */
public class ResultsForm extends Form {
    public ResultsForm() {
        int score;
        if(M3G.isM3GSupported()) {
            setLayout(new GridLayout(8, 2));
            score = FramerateTest.getFramecount() + DrawingRate.getFramecount() +
                GradientRate.getFramecount() + BitmapFontRate.getFramecount() +
                ScalingRate.getFramecount() + Framerate3D.getFramecount() + IndexedImageTest.getFramecount() +
                ListTest.getFramecount() + ComponentTest.getFramecount();
        } else {
            setLayout(new GridLayout(7, 2));
            score = FramerateTest.getFramecount() + DrawingRate.getFramecount() +
                GradientRate.getFramecount() + BitmapFontRate.getFramecount() +
                ScalingRate.getFramecount()+ IndexedImageTest.getFramecount() +
                ListTest.getFramecount() + ComponentTest.getFramecount();
        }
        addComponent(createLabel("Framerate/Blit"));
        addComponent(createLabel(FramerateTest.getAverageFramerate() + "fps"));
        addComponent(createLabel("Drawing 2D"));
        addComponent(createLabel(DrawingRate.getAverageFramerate() + "fps"));
        addComponent(createLabel("Gradient"));
        addComponent(createLabel(GradientRate.getAverageFramerate() + "fps"));
        addComponent(createLabel("Bitmap Fonts"));
        addComponent(createLabel(BitmapFontRate.getAverageFramerate() + "fps"));
        addComponent(createLabel("Scaling"));
        addComponent(createLabel(ScalingRate.getAverageFramerate() + "fps"));
        addComponent(createLabel("Indexed Image"));
        addComponent(createLabel(IndexedImageTest.getAverageFramerate() + "fps"));
        if(M3G.isM3GSupported()) {
            addComponent(createLabel("3D"));
            addComponent(createLabel(Framerate3D.getAverageFramerate() + "fps"));
        }
        addComponent(createLabel("List"));
        addComponent(createLabel(ListTest.getAverageFramerate() + "fps"));
        addComponent(createLabel("Components"));
        addComponent(createLabel(ComponentTest.getAverageFramerate() + "fps"));
        
        addComponent(createBoldLabel("Score"));
        addComponent(createBoldLabel("" + score));
        addCommand(new Command("Exit") {
            public void actionPerformed(ActionEvent ev) {
                SpeedMIDlet.instance.notifyDestroyed();
            }
        });
        show();
    }
    
    private Label createLabel(String s) {
        Label l = new Label(s);
        l.getStyle().setBgTransparency(100);
        l.setFocusable(true);
        return l;
    }

    private Label createBoldLabel(String s) {
        Label l = new Label(s);
        l.getStyle().setBgTransparency(100);
        l.getStyle().setFont(Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        l.setFocusable(true);
        return l;
    }
}
