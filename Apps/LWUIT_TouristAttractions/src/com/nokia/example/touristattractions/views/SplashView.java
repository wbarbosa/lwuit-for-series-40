/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.sun.lwuit.*;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;

public class SplashView
    extends Form {

    private static final int SPRITE_LENGTH = 24; // number of spinner angles
    private Image splashBottomImg; // splash city image
    private Image[] spinnerImgs = new Image[SPRITE_LENGTH]; // spinner images
    private int tileWidth; // width of a single sprite tile image
    private int currentTile = 0; // spinner index
    private Container centerContainer;
    private Label touristLabel, attractionsLabel;
    private Label spinnerLabel;
    private boolean spinning = true;

    public SplashView() {
        getStyle().setBgPainter(new BackgroundPainter());
        
        hideDefaultTitle();
        createComponents();
        setStyles();
        createLayout();
        startSpin();
    }
    
    private void createComponents() {
        splashBottomImg = Util.loadImage("attractions_2.0.png");
        splashBottomImg = splashBottomImg.scaled(Compatibility.WIDTH,
            this.getHeight() / 3);

        // Init spinner
        Image spinnerSprite = Util.loadImage("loader_main.png");
        tileWidth = spinnerSprite.getWidth() / SPRITE_LENGTH;

        for (int i = 0; i < SPRITE_LENGTH; i++) {
            spinnerImgs[i] = spinnerSprite.subImage(i * tileWidth, 0,
                tileWidth, tileWidth, true);
        }
        spinnerLabel = new Label(spinnerImgs[currentTile]);

        // Init text labels
        touristLabel = new Label("Tourist");
        attractionsLabel = new Label("Attractions");        
    }

    private void setStyles() {
        Style style;
        /* resolution based style changes */
        if (Compatibility.HEIGHT <= 240) {
            style = touristLabel.getStyle();
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
            style = attractionsLabel.getStyle();
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
            style = spinnerLabel.getStyle();
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
        }

        style = touristLabel.getStyle();
        style.setFgColor(Visual.SPLASH_TEXT_COLOR);
        style.setFont(Visual.LARGE_BOLD_FONT);

        attractionsLabel.setPreferredW(this.getWidth());
        style = attractionsLabel.getStyle();
        style.setAlignment(CENTER);
        style.setFgColor(Visual.SPLASH_TEXT_COLOR);
        style.setFont(Visual.MEDIUM_BOLD_FONT);        
    }
    
    private void createLayout() {
        FlowLayout flowLayout = new FlowLayout(CENTER);
        centerContainer = new Container();
        centerContainer.setLayout(flowLayout);
        centerContainer.addComponent(touristLabel);
        centerContainer.addComponent(attractionsLabel);
        centerContainer.addComponent(spinnerLabel);
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(borderLayout);
        addComponent(BorderLayout.CENTER, centerContainer);
    }
    
    /**
     * Start the spinner.
     */
    private void startSpin() {
        new Thread() {
            public void run() {
                while (spinning) {
                    if (currentTile == SPRITE_LENGTH - 1) {
                        currentTile = 0;
                    }
                    else {
                        currentTile++;
                    }
                    spinnerLabel.setIcon(spinnerImgs[currentTile]);
                    spinnerLabel.repaint();
                    touristLabel.repaint();
                    attractionsLabel.repaint();
                    try {
                        Thread.sleep(1000 / TouristMidlet.SPINNER_FPS);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }.start();        
    }
    
    /**
     * Stop the spinner.
     */
    public void stopSpin() {
        spinning = false;
    }

    private void hideDefaultTitle() {
        getTitleArea().getStyle().setPadding(0, 0, 0, 0);
        getTitleArea().setVisible(false);
        getTitleComponent().setVisible(false);
    }

    /**
     * LWUIT painter that renders the background of this view.
     */
    class BackgroundPainter
        implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            int height = getHeight();
            int width = getWidth();

            g.setColor(Visual.SPLASH_BACKGROUND_COLOR);
            g.fillRect(0, 0, width, height / 3);

            g.setColor(Visual.DARK_BACKGROUND_COLOR);
            g.fillRect(0, height / 3, width, 2 * height / 3);

            g.drawImage(splashBottomImg, 0, height - splashBottomImg.getHeight());
        }
    }
}
