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
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.CoordinateLayout;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * A component showing general info about the application.
 */
public class InfoComponent
    extends Container {

    private static final String PROJECT_URL = "http://projects.developer.nokia.com/LWUIT_Puzzle";
    private static final String VERSION = "1.1";
    private MiddleContainer middleContainer;
    private Image infoImg;
    private Button urlButton;
    private Label versionLabel;

    public InfoComponent(MiddleContainer middleContainer) {
        this.middleContainer = middleContainer;

        infoImg = ImageUtil.loadImage("dialog_info.png");
        getStyle().setBgImage(infoImg);

        initUrlButton();
        versionLabel = new Label(ImageUtil.getVersionNumberAsImage(VERSION));

        // Init layout and coordinates for version and
        // URL components related to this component
        CoordinateLayout infoLayout = new CoordinateLayout(this.getPreferredW(),
            this.getPreferredH());
        setLayout(infoLayout);

        versionLabel.getStyle().setBgTransparency(0x00);
        versionLabel.setX(125);
        versionLabel.setY(35);

        urlButton.setX(14);
        urlButton.setY(this.getPreferredH() - urlButton.getPreferredH() - 13);

        // Make url button selected only on non-touch devices
        if (!Compatibility.TOUCH_SUPPORTED) {
            urlButton.setSelected(true);
        }

        addComponent(versionLabel);
        addComponent(urlButton);
    }
    
    public void keyPressed(int gameAction) {
        switch (gameAction) {
            case Display.GAME_FIRE:
                urlButton.onPress();
                break;
        }
    }

    public void keyReleased(int gameAction) {
        switch (gameAction) {
            case Display.GAME_FIRE:
                urlButton.onRelease();
                break;
        }
    }

    public MiddleContainer getMiddleContainer() {
        return middleContainer;
    }

    public Button getUrlButton() {
        return urlButton;
    }

    private void initUrlButton() {
        urlButton = new Button("button_link.png", "button_link_pressed.png",
            "button_link_selected.png", null) {

            public void onClick() {
                try {
                    middleContainer.showPuzzle();
                    getMiddleContainer().getGameView().getMidlet().platformRequest(PROJECT_URL);
                }
                catch (ConnectionNotFoundException e) {
                }
            }
        };
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(infoImg.getWidth(), infoImg.getHeight());
    }
}