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
import com.nokia.example.slidepuzzle.components.HelpComponent;
import com.nokia.example.slidepuzzle.components.MiddleContainer;
import com.nokia.example.slidepuzzle.main.PuzzleMidlet;
import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.*;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.plaf.Style;

public class GameView
    extends View {

    // Containers
    private Container movesContainer; // contains move counter icon and the actual counter
    private MiddleContainer middleContainer; // holds all components in the middle of the screen
    private Container mainContainer;
    
    // Buttons
    private Button menuButton;
    private Button backButton;
    
    // Labels
    private Label titleLabel;
    private Label movesImgLabel;
    private Label movesLabel;
    
    // Tile move counter
    private int moves = 0;
    
    // Currently pressed component
    private Component pressedCmp;

    public GameView(final PuzzleMidlet midlet) {
        super(midlet);
        
        getStyle().setBgPainter(new BackgroundPainter());
        initContainers();
        
        if (Compatibility.SCREEN_SIZE != Compatibility.RES_320x240) {
            createLayoutForPortrait();
        }
        else {
            createLayoutForLandscape();
        }

        this.setFocusable(true);
    }
    
    private void initContainers() {
        // Init move counter labels
        movesImgLabel = new Label(ImageUtil.loadImage("icon_moves.png"));
        movesImgLabel.getStyle().setBgTransparency(0x00);
        movesLabel = new Label(ImageUtil.getNumberAsImage(moves));
        movesLabel.setPreferredW(3 * Compatibility.DIGIT_WIDTH + 10);
        movesLabel.getStyle().setBgTransparency(0x00);

        // Init middle container
        int extraMargin = calcExtraMargin();        
        middleContainer = new MiddleContainer(this);
        middleContainer.getStyle().setPadding(0, 0, 0, 0);
        middleContainer.getStyle().setMargin(0, (int) (1.7f * extraMargin), 0, 0);

        // Init menu and back buttons
        initButtons();

        // Init title label
        Image splashImage = midlet.getSplashView().getTitleImage();
        titleLabel = new Label(Compatibility.SCREEN_SIZE == Compatibility.RES_320x240
            ? ImageUtil.loadImage("title_rotated.png").scaledWidth(70) : splashImage);
        Style titleStyle = titleLabel.getStyle();
        titleStyle.setMargin((int) (1.5f * extraMargin), 0, (getWidth() - titleLabel.getPreferredW())
            / 2, 0);
        titleStyle.setPadding(0, 0, 0, 0);
        titleStyle.setBgTransparency(0x00);

        // Init move counter container
        movesContainer = new Container();
        movesContainer.getStyle().setPadding(TOP, 10);
        movesContainer.addComponent(movesImgLabel);
        movesContainer.addComponent(movesLabel);

        // Init the main container
        mainContainer = new Container();
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER);
        mainContainer.setLayout(borderLayout);
        mainContainer.getStyle().setMargin(0, 0, 0, 0);
        mainContainer.getStyle().setPadding(0, 0, 0, 0);
        mainContainer.setPreferredW(this.getWidth());
        mainContainer.setPreferredH(this.getHeight());        
    }
    
    private void createLayoutForPortrait() {
        Container footerContainer = new Container();
        footerContainer.setPreferredH(menuButton.getPreferredH());
        BorderLayout footerLayout = new BorderLayout();
        footerLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER);
        footerContainer.setLayout(footerLayout);
        footerContainer.addComponent(BorderLayout.WEST, menuButton);
        footerContainer.addComponent(BorderLayout.CENTER, movesContainer);
        footerContainer.addComponent(BorderLayout.EAST, backButton);

        mainContainer.addComponent(BorderLayout.NORTH, titleLabel);
        mainContainer.addComponent(BorderLayout.CENTER, middleContainer);
        mainContainer.addComponent(BorderLayout.SOUTH, footerContainer);

        addComponent(mainContainer);        
    }
    
    private void createLayoutForLandscape() {
        Container leftSideContainer = new Container();
        BorderLayout leftLayout = new BorderLayout();
        leftSideContainer.setLayout(leftLayout);
        leftSideContainer.addComponent(BorderLayout.SOUTH, menuButton);

        movesContainer.getStyle().setAlignment(RIGHT);
        backButton.getStyle().setAlignment(RIGHT);

        Container rightSideContainer = new Container();
        BorderLayout rightLayout = new BorderLayout();
        rightSideContainer.setLayout(rightLayout);
        rightSideContainer.addComponent(BorderLayout.NORTH, movesContainer);
        rightSideContainer.addComponent(BorderLayout.SOUTH, backButton);
        rightSideContainer.setPreferredW(backButton.getPreferredW());
        rightSideContainer.setWidth(rightSideContainer.getPreferredW());

        menuButton.getStyle().setAlignment(BOTTOM);

        mainContainer.addComponent(BorderLayout.WEST, leftSideContainer);
        mainContainer.addComponent(BorderLayout.CENTER, middleContainer);
        mainContainer.addComponent(BorderLayout.EAST, rightSideContainer);

        mainContainer.setX(0);
        mainContainer.setY(0);

        titleLabel.setX(0);
        titleLabel.setY(0);

        CoordinateLayout rootLayout = new CoordinateLayout(this.getWidth(), this.getHeight());
        setLayout(rootLayout);
        addComponent(mainContainer);
        addComponent(titleLabel);        
    }
    

    /**
     * Receive key events and delegate them to the correct components to be handled.
     *
     * @param keyCode Key code of the pressed key
     */
    public void keyPressed(int keyCode) {
        /* dispatch key events to the correct component */
        switch (keyCode) {
            case -6: // left cmd key
                menuButton.onPress();
                return;
            case -7: // right cmd key
                backButton.onPress();
                return;
        }
        middleContainer.keyPressed(getGameAction(keyCode));
    }

    /**
     * Receive key events and delegate them to the correct components to be handled.
     *
     * @param keyCode Key code of the released key
     */
    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case -6: // left cmd key
                menuButton.onRelease();
                return;
            case -7: // right cmd key
                backButton.onRelease();
                return;
        }
        middleContainer.keyReleased(getGameAction(keyCode));
    }

    public void pointerPressed(int x, int y) {
        // Obtain the correct component that should handle the pointer event
        if (middleContainer.contains(x, y)) {
            pressedCmp = middleContainer.getPressedCmp(x, y);
            HelpComponent helpCmp = middleContainer.getHelpCmp();

            if (pressedCmp != null && !helpCmp.contains(x, y) &&
                middleContainer.getCurrent() == helpCmp) {
                middleContainer.showPuzzle();
            }
        }
        else {
            pressedCmp = getComponentAt(x, y);
            if (pressedCmp != null && pressedCmp != menuButton &&
                pressedCmp != backButton) {
                middleContainer.showPuzzle();
            }
        }

        if (pressedCmp != null) {
            pressedCmp.pointerPressed(x, y);
        }
    }

    public void pointerDragged(int x, int y) {
        if (pressedCmp != null) {
            pressedCmp.pointerDragged(x, y);
        }
    }

    public void pointerReleased(int x, int y) {
        if (pressedCmp != null) {
            pressedCmp.pointerReleased(x, y);
        }
    }

    /**
     * Increments tile move count by 1.
     */
    public void incrementMoves() {
        movesLabel.setIcon(ImageUtil.getNumberAsImage(++moves));
    }

    /**
     * Sets the number of tile moves.
     *
     * @param newMoves Number of tile moves.
     */
    public void setMoves(int newMoves) {
        moves = newMoves;
        movesLabel.setIcon(ImageUtil.getNumberAsImage(moves));
    }

    /**
     * Sets the number of tile moves to 0.
     */
    public void clearMoves() {
        setMoves(0);
    }

    public int getMoves() {
        return moves;
    }

    public MiddleContainer getMiddleContainer() {
        return middleContainer;
    }

    public void setMenuButtonEnabled(boolean enabled) {
        menuButton.setEnabled(enabled);
    }

    public void repaintTitleLabel() {
        if (titleLabel != null) {
            titleLabel.repaint();
        }
    }

    private int getGameAction(int keyCode) {
        int gameAction = Display.getInstance().getGameAction(keyCode);
        
        // Convert digit buttons to arrow events
        switch (keyCode) {
            case '4':
                gameAction = Display.GAME_LEFT;
                break;
            case '6':
                gameAction = Display.GAME_RIGHT;
                break;
            case '2':
                gameAction = Display.GAME_UP;
                break;
            case '8':
                gameAction = Display.GAME_DOWN;
                break;
            case '5':
                gameAction = Display.GAME_FIRE;
                break;
        }
        
        return gameAction;
    }

    private int calcExtraMargin() {
        if (Compatibility.SCREEN_SIZE == Compatibility.RES_240x400) {
            return 15;
        }
        return 0;
    }

    private void initButtons() {
        menuButton = new Button("softkey_menu.png",
            "softkey_menu_pressed.png", null,
            "softkey_menu_disabled.png") {

            public void onClick() {
                middleContainer.showMenu();
            }
        };
        
        backButton = new Button("softkey_back.png",
            "softkey_back_pressed.png", null,
            "softkey_back_disabled.png") {

            public void onClick() {
                middleContainer.onBack();
            }
        };
    }

    class BackgroundPainter
        implements Painter {

        private Image bgTexture;

        public BackgroundPainter() {
            bgTexture = midlet.getSplashView().getBgTexture();
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
