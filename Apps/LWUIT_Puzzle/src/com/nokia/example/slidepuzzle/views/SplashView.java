/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.views;

import com.nokia.example.slidepuzzle.main.PuzzleMidlet;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * The view shown for a moment when starting the application.
 */
public class SplashView
    extends View {

    private Label titleImgLabel;
    private Image splashImage;
    private BackgroundPainter bgPainter;

    public SplashView(final PuzzleMidlet midlet) {
        super(midlet);

        bgPainter = new SplashView.BackgroundPainter();
        getStyle().setBgPainter(bgPainter);

        splashImage = ImageUtil.loadImage("title.png");
        titleImgLabel = new Label(splashImage);
        titleImgLabel.getStyle().setBgTransparency(0x00);

        // Add title label to the center of the screen
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER);
        setLayout(borderLayout);
        addComponent(BorderLayout.CENTER, titleImgLabel);
    }

    public Image getTitleImage() {
        return splashImage;
    }
    
    public Image getBgTexture() {
        return bgPainter.getBgTexture();
    }

    class BackgroundPainter
        implements Painter {

        private Image bgTexture;

        public BackgroundPainter() {
            bgTexture = ImageUtil.loadImage("background.png");
        }
        
        public Image getBgTexture() {
            return bgTexture;
        }

        public void paint(Graphics g, Rectangle rect) {
            int w = bgTexture.getWidth();
            int h = bgTexture.getHeight();
            
            for (int i = 0; i < getWidth() / w + 1; i++) {
                for (int j = 0; j < getHeight() / h + 1; j++) {
                    g.drawImage(bgTexture, i * w, j * h);
                }
            }
        }
    }
}