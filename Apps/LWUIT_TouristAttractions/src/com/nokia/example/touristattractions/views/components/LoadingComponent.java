/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views.components;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.View;
import com.sun.lwuit.*;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;

/**
 * Component that indicates that something is being loaded. The component
 * contains a rotating plane and text "Loading..."
 */
public class LoadingComponent
    extends Container {

    private static final int SPRITE_LENGTH = 24; // number of different angles
    private static Image[] spinnerImgs = new Image[SPRITE_LENGTH]; // sprite images
    private static int tileWidth; // width of a single sprite image
    private int currentTile = 0; // sprite index
    private View parent;
    private Label spinnerLabel;
    private Label loadingLabel;
    private Thread spinThread;
    private boolean spinning = false;

    public LoadingComponent(View parent) {
        this.parent = parent;

        getStyle().setBgPainter(new BackgroundPainter());
        createComponents();
        createLayout();
    }
    
    private void createComponents() {
        // Init spinner
        Image spinnerSprite = Util.loadImage("loader_content.png");
        tileWidth = spinnerSprite.getWidth() / SPRITE_LENGTH;

        for (int i = 0; i < SPRITE_LENGTH; i++) {
            spinnerImgs[i] =
                spinnerSprite.subImage(i * tileWidth, 0, tileWidth, tileWidth, true);
        }
        spinnerLabel = new Label(spinnerImgs[0]);
        spinnerLabel.getStyle().setPadding(0, 0, 70, 70);
        
        // Init loading label
        loadingLabel = new Label("Loading...");
        loadingLabel.getStyle().setFgColor(Visual.LIST_PRIMARY_COLOR);
        
    }
    
    private void createLayout() {
        Container centerContainer = new Container();
        FlowLayout flowLayout = new FlowLayout(CENTER);
        centerContainer.setLayout(flowLayout);

        centerContainer.addComponent(spinnerLabel);
        centerContainer.addComponent(loadingLabel);

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(borderLayout);
        addComponent(BorderLayout.CENTER, centerContainer);        
    }

    /**
     * Starts spinning of the plane spinner.
     */
    public void startSpin() {
        spinThread = new Thread() {
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
                    try {
                        Thread.sleep(1000 / TouristMidlet.SPINNER_FPS);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        };
        
        if (!spinThread.isAlive()) {
            spinning = true;
            spinThread.start();
        }
    }

    /**
     * Stop spinning.
     */
    public void stopSpin() {
        spinning = false;
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(parent.getWidth(), parent.getContentAreaHeight());
    }

    class BackgroundPainter
        implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            g.setColor(Visual.BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
