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
import com.nokia.example.slidepuzzle.views.GameView;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.CoordinateLayout;

public class MiddleContainer
    extends Container {

    public static final int FPS = 30; // frame rate of the component
    private static final int SHADOW_PADDING = 5; // shadow in pixels in pop-up components
    private GameView view;
    private PuzzleComponent puzzleCmp;
    private MenuComponent menuCmp;
    private HelpComponent helpCmp;
    private InfoComponent infoCmp;
    private Component current;
    private long lastTime; // needed to paint with correct intervals

    public MiddleContainer(final GameView view) {
        this.view = view;

        initComponentsAndLayout();

        new Thread() {
            public void run() {
                while (true) {
                    view.repaint();

                    // On 320x240, the title bar partly overlaps the puzzle, so
                    // repaint it
                    if (Compatibility.SCREEN_SIZE == Compatibility.RES_320x240) {
                        view.repaintTitleLabel();
                    }

                    // Sleep at least 1 ms to ensure time for EDT
                    long delay = System.currentTimeMillis() - lastTime;
                    lastTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(Math.max(1, 1000 / FPS - delay));
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }
    
    private void initComponentsAndLayout() {
        this.puzzleCmp = new PuzzleComponent(this);
        this.menuCmp = new MenuComponent(this);
        this.helpCmp = new HelpComponent();
        this.infoCmp = new InfoComponent(this);

        current = puzzleCmp;

        // Adjust different components that will be shown in the middle area
        CoordinateLayout layout = new CoordinateLayout(this.getPreferredW(), this.getPreferredH());
        this.setLayout(layout);

        puzzleCmp.setX(0);
        puzzleCmp.setY((getPreferredH() - puzzleCmp.getPreferredH()) / 2);

        menuCmp.setX((getPreferredW() - menuCmp.getPreferredW() + SHADOW_PADDING) / 2);
        menuCmp.setY(0);

        helpCmp.setX((getPreferredW() - helpCmp.getPreferredW() + SHADOW_PADDING) / 2);
        helpCmp.setY((getPreferredH() - helpCmp.getPreferredH()) / 2);

        infoCmp.setX((getPreferredW() - infoCmp.getPreferredW() + SHADOW_PADDING) / 2);
        infoCmp.setY(0);
        
        // As default, show only puzzle
        menuCmp.setVisible(false);
        helpCmp.setVisible(false);
        infoCmp.setVisible(false);

        // However, add them all to this container
        addComponent(puzzleCmp);
        addComponent(menuCmp);
        addComponent(helpCmp);
        addComponent(infoCmp);        
    }

    public void keyPressed(int gameAction) {
        current.keyPressed(gameAction);
    }

    public void keyReleased(int gameAction) {
        current.keyReleased(gameAction);
    }

    /**
     * Returns the top component at the given point.
     *
     * @param x x coordinate of the given point
     * @param y y coordinate of the given point
     * @return Top component at the given point
     */
    public Component getPressedCmp(int x, int y) {
        if (current == menuCmp) {
            return menuCmp.getPressedCmp(x, y);
        }
        
        if (current == infoCmp) {
            return infoCmp.getUrlButton();
        }
        
        return current;
    }

    public HelpComponent getHelpCmp() {
        return helpCmp;
    }

    /**
     * Handles the back button event.
     */
    public void onBack() {
        if (current == puzzleCmp) {
            view.getMidlet().destroyApp(true);
        }
        else if (current == menuCmp) {
            showPuzzle();
        }
        else if (current == helpCmp) {
            showMenu();
        }
        else if (current == infoCmp) {
            showMenu();
        }
    }

    public void showCamera() {
        view.getMidlet().getCameraView().showAndStartCamera();
        view.repaint();
    }

    public void showPuzzle() {
        menuCmp.setVisible(false);
        helpCmp.setVisible(false);
        infoCmp.setVisible(false);
        current = puzzleCmp;
        view.setMenuButtonEnabled(true);
        view.repaint();
    }

    public void showHelp() {
        helpCmp.setVisible(true);
        menuCmp.setVisible(false);
        current = helpCmp;
        view.repaint();
    }

    public void showInfo() {
        infoCmp.setVisible(true);
        menuCmp.setVisible(false);
        current = infoCmp;
        view.repaint();
    }

    public void showMenu() {
        menuCmp.setVisible(true);
        helpCmp.setVisible(false);
        infoCmp.setVisible(false);
        current = menuCmp;
        view.setMenuButtonEnabled(false);
        view.repaint();
    }

    public GameView getGameView() {
        return view;
    }

    public Component getCurrent() {
        return current;
    }

    public PuzzleComponent getPuzzleComponent() {
        return puzzleCmp;
    }

    public MenuComponent getMenuDialog() {
        return menuCmp;
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(puzzleCmp.getPreferredW(), menuCmp.getPreferredH());
    }
}
