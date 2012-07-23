/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.me.web.sample.local;

import com.sun.lwuit.Image;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.Component;
import com.sun.lwuit.plaf.Style;

/**
 * A "washing machine" progress animation that rotates the given image as an
 * animation. The image for rotation must be square.
 *
 * @author Shai Almog
 */
public class InfiniteProgressIndicator extends Component {
    private Image[] angles;
    private int angle;
    public InfiniteProgressIndicator(Image image) {
        Image fourtyFiveDeg = image.rotate(45);
        angles = new Image[] {image, fourtyFiveDeg, image.rotate(90), fourtyFiveDeg.rotate(90),
            image.rotate(180), fourtyFiveDeg.rotate(180), image.rotate(270), fourtyFiveDeg.rotate(270)};
        getStyle().setBgTransparency(0);
    }
    
    protected Dimension calcPreferredSize() {
        Style s = getStyle();
        return new Dimension(angles[0].getWidth() + s.getPadding(LEFT) + s.getPadding(RIGHT), 
            angles[0].getHeight() + s.getPadding(TOP) + s.getPadding(BOTTOM));
    }
    
    public void initComponent() {
        getComponentForm().registerAnimated(this);
    }
    
    public void paint(Graphics g) {
        Style s = getStyle();
        g.drawImage(angles[Math.abs(angle % angles.length)], getX() + s.getPadding(LEFT), getY() + s.getPadding(TOP));
    }
    
    public boolean animate() {
        angle++;
        return true;
    }
}
