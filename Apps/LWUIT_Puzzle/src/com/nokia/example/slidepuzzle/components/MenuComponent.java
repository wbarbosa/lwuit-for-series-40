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
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;
import java.util.Vector;

public class MenuComponent
    extends Container {

    private MiddleContainer middleContainer;
    private Image menuBgImg;
    
    // Menu buttons
    private Button shuffleButton;
    private Button nextButton;
    private Button helpButton;
    private Button infoButton;
    private Button cameraButton;
    
    // List of menu buttons
    private Vector buttons;
    
    // Button that is currently selected (on non-touch)
    private Button selected;

    public MenuComponent(final MiddleContainer middleContainer) {
        this.middleContainer = middleContainer;

        menuBgImg = ImageUtil.loadImage("dialog_menu.png");
        this.getStyle().setBgImage(menuBgImg);
        this.getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_ALIGNED_TOP_LEFT);
        initButtons();

        // Highlight selected button only if on non-touch
        if (!Compatibility.TOUCH_SUPPORTED) {
            selected = shuffleButton;
            selected.setSelected(true);
        }

        FlowLayout menuLayout = new FlowLayout(CENTER);
        this.setLayout(menuLayout);

        // Add buttons to the layout
        for (int i = 0; i < buttons.size(); i++) {
            Button b = (Button) buttons.elementAt(i);
            b.setWidth(b.getPreferredW());
            b.setHeight(b.getPreferredH());
            b.getStyle().setAlignment(Component.CENTER);
            int topMargin = Compatibility.CAMERA_SUPPORTED ? 11 : 20;
            b.getStyle().setMargin(topMargin, 0, 0, 3);
            
            addComponent(b);
        }
    }

    public void keyPressed(int gameAction) {
        // Handle switching between buttons and firing them on non-touch
        switch (gameAction) {
            case Display.GAME_FIRE:
                selected.onPress();
                break;
            case Display.GAME_UP:
                next();
                break;
            case Display.GAME_DOWN:
                previous();
                break;
        }
    }

    public void keyReleased(int gameAction) {
        switch (gameAction) {
            case Display.GAME_FIRE:
                selected.onRelease();
                break;
        }
    }

    /**
     * Highlight the next button in the button list.
     */
    public void next() {
        selected.setSelected(false);
        selected = selected.getNext();
        selected.setSelected(true);
    }

    /**
     * Highlight the previous button in the button list.
     */
    public void previous() {
        selected.setSelected(false);
        selected = selected.getPrevious();
        selected.setSelected(true);
    }

    /**
     * Return the top component (button) at the given coordinates.
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return Component at the given coordinates
     */
    public Component getPressedCmp(int x, int y) {
        for (int i = 0; i < buttons.size(); i++) {
            Button b = (Button) buttons.elementAt(i);
            
            if (b.contains(x, y)) {
                return b;
            }
        }
        return this;
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(menuBgImg.getWidth(), menuBgImg.getHeight());
    }

    private void initButtons() {
        shuffleButton = new Button("button_shuffle.png", "button_shuffle_pressed.png",
            "button_shuffle_selected.png", null) {

            public void onClick() {
                middleContainer.getPuzzleComponent().shuffleButton();
            }
        };
        
        nextButton = new Button("button_next.png",
            "button_next_pressed.png", "button_next_selected.png", null) {

            public void onClick() {
                middleContainer.getPuzzleComponent().nextButton();
            }
        };
        
        cameraButton = new Button("button_camera.png",
            "button_camera_pressed.png", "button_camera_selected.png", null) {

            public void onClick() {
                middleContainer.showCamera();
            }
        };
        
        helpButton = new Button("button_help.png",
            "button_help_pressed.png", "button_help_selected.png", null) {

            public void onClick() {
                middleContainer.showHelp();
            }
        };
        
        infoButton = new Button("button_info.png",
            "button_info_pressed.png", "button_info_selected.png", null) {

            public void onClick() {
                middleContainer.showInfo();
            }
        };

        infoButton.setNext(helpButton);
        
        if (Compatibility.CAMERA_SUPPORTED) {
            helpButton.setNext(cameraButton);
            cameraButton.setNext(nextButton);
        }
        else {
            helpButton.setNext(nextButton);
        }
        
        nextButton.setNext(shuffleButton);
        shuffleButton.setNext(infoButton);

        buttons = new Vector(5);
        buttons.addElement(shuffleButton);
        buttons.addElement(nextButton);
        
        if (Compatibility.CAMERA_SUPPORTED) {
            buttons.addElement(cameraButton);
        }
        buttons.addElement(helpButton);
        buttons.addElement(infoButton);
    }
}
