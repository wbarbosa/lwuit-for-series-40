/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.components;

import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;

/**
 * A component showing advice on how to play the game.
 */
public class HelpComponent
    extends Component {

    private Image helpImg = null;

    public HelpComponent() {
        /* different help texts for non-touch/touch devices */
        if (Compatibility.TOUCH_SUPPORTED) {
            helpImg = ImageUtil.loadImage("dialog_help_touch.png");
        }
        else {
            helpImg = ImageUtil.loadImage("dialog_help.png");
        }

        getStyle().setBgImage(helpImg);
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(helpImg.getWidth(), helpImg.getHeight());
    }
}