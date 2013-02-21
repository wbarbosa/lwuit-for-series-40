/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.views;

import com.nokia.example.slidepuzzle.components.Button;
import com.nokia.example.slidepuzzle.components.CameraComponent;
import com.nokia.example.slidepuzzle.main.PuzzleMidlet;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.*;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;

public class CameraView
    extends View {

    private CameraComponent cameraCmp;
    private Button takePicButton;
    private Button backButton;
    private Label loadingLabel;
    private Container buttonContainer;

    public CameraView(final PuzzleMidlet midlet) {
        super(midlet);

        // Init background
        getStyle().setBgPainter(new BackgroundPainter());

        initComponents();
        createLayout();
    }
    
    private void initComponents() {
        cameraCmp = new CameraComponent(this);

        takePicButton = new Button("softkey_camera_opaque.png",
            "softkey_camera_opaque_pressed.png", null, null) {

            public void onClick() {
                cameraCmp.takePicAndShow();
            }
        };

        backButton = new Button("softkey_back_opaque.png",
            "softkey_back_opaque_pressed.png", null, null) {

            public void onClick() {
                hideCamera();
                midlet.getGameView().show();
                midlet.getGameView().getMiddleContainer().showPuzzle();
            }
        };

        loadingLabel = new Label(ImageUtil.loadImage("loading.png"));
        loadingLabel.getStyle().setBgTransparency(0x00);        
    }
    
    private void createLayout() {
        BorderLayout buttonsLayout = new BorderLayout();
        buttonsLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);

        buttonContainer = new Container();
        buttonContainer.setPreferredW(this.getWidth());
        buttonContainer.setPreferredH(getMenuBarHeight());
        buttonContainer.setLayout(buttonsLayout);
        buttonContainer.addComponent(BorderLayout.CENTER, takePicButton);
        buttonContainer.addComponent(BorderLayout.EAST, backButton);

        BorderLayout mainLayout = new BorderLayout();
        mainLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        this.setLayout(mainLayout);

        addComponent(BorderLayout.NORTH, cameraCmp);
        addComponent(BorderLayout.SOUTH, buttonContainer);        
    }

    public void keyPressed(int keyCode) {
        if (keyCode == '5' || Display.getInstance().getGameAction(keyCode) == Display.GAME_FIRE) {
            takePicButton.onPress();
        }
        
        if (keyCode == -7) {
            backButton.onPress();
        }
    }

    public void keyReleased(int keyCode) {
        if (keyCode == '5' || Display.getInstance().getGameAction(keyCode) == Display.GAME_FIRE) {
            takePicButton.onRelease();
        }
        
        if (keyCode == -7) {
            backButton.onRelease();
        }
    }

    public void showAndStartCamera() {
        cameraCmp.start();
        this.show();
    }

    /**
     * Hides the camera.
     */
    public void hideCamera() {
        cameraCmp.stop();
    }

    public void showOwnPicPuzzle(Image snapshot) {
        midlet.getGameView().getMiddleContainer().getPuzzleComponent().showOwnPicPuzzle(snapshot);
    }

    /**
     * Returns the height of vertical space needed for buttons.
     *
     * @return Returns the height of vertical space needed for buttons in pixels
     */
    public int getMenuBarHeight() {
        return Math.max(takePicButton.getPreferredH(), backButton.getPreferredH());
    }

    /**
     * Shows/hides loading bar to indicate that camera is processing.
     * 
     * @param show true to show, false to hide the loading bar
     */
    public void showLoading(boolean show) {
        if (show) {
            buttonContainer.removeAll();
            buttonContainer.addComponent(BorderLayout.CENTER, loadingLabel);
        }
        else {
            buttonContainer.removeAll();
            buttonContainer.addComponent(BorderLayout.CENTER, takePicButton);
            buttonContainer.addComponent(BorderLayout.EAST, backButton);
        }
        this.repaint();
    }

    class BackgroundPainter
        implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            g.setColor(0x000000);
            g.fillRect(0, 0, rect.getSize().getWidth(), rect.getSize().getHeight());
        }
    }
}
