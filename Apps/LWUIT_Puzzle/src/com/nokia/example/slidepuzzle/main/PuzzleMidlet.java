/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.main;

import com.nokia.example.slidepuzzle.components.PuzzleComponent;
import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.views.CameraView;
import com.nokia.example.slidepuzzle.views.GameView;
import com.nokia.example.slidepuzzle.views.SplashView;
import com.sun.lwuit.Display;
import javax.microedition.midlet.*;

public class PuzzleMidlet
    extends MIDlet {

    private GameView gameView; // the main view of the game
    private SplashView splashView; // the view shown at start-up
    private CameraView cameraView;

    public void startApp() {
        Display.init(this); // initialize LWUIT Display
        Display.getInstance().setForceFullScreen(true);
        Display.getInstance().setDragStartPercentage(0); // set drag events to start instantly

        splashView = new SplashView(this);
        splashView.show(); // show splash view
        
        // Start initializing game view in the background
        long start = System.currentTimeMillis();
        gameView = new GameView(this);
        if (Compatibility.CAMERA_SUPPORTED) {
            cameraView = new CameraView(this);
        }
        
        long difference = System.currentTimeMillis() - start;
        try {
            // Show splash view for at least a second
            Thread.sleep(Math.max(0, 1000 - difference));
        }
        catch (InterruptedException e) {
        }
        
        gameView.show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        PuzzleComponent component =
            gameView.getMiddleContainer().getPuzzleComponent();

        // If the picture of the current puzzle is not take
        // with phone camera, save game
        if (!component.getCurrentPuzzle().isOwnPuzzle()) {
            component.saveGame();
        }
        Display.getInstance().exitApplication();
    }

    public SplashView getSplashView() {
        return splashView;
    }

    public GameView getGameView() {
        return gameView;
    }

    public CameraView getCameraView() {
        return cameraView;
    }
}
