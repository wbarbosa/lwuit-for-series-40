/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.puzzle;

import com.nokia.example.slidepuzzle.components.MiddleContainer;
import com.nokia.example.slidepuzzle.components.PuzzleComponent;
import java.util.Vector;

public class TileMover {

    private static final int SPEED = 14; // increment of x/y coordinate per move step
    private final int TILE_SIZE; // size of tile in pixels
    private PuzzleComponent component;
    private Vector movingTiles; // list of moving tiles
    private MoverThread moverThread; // thread that moves tiles
    private boolean stop = false; // stop request

    public TileMover(PuzzleComponent component) {
        this.component = component;
        TILE_SIZE = component.getTileSize();
        movingTiles = new Vector();
        moverThread = new MoverThread();
    }

    /**
     * Starts to move the given tile to the target coordinates.
     *
     * @param tile Tile to be moved
     * @param targetGridX x coordinate of the target position in tiles
     * @param targetGridY y coordinate of the target position in tiles
     */
    public void moveTile(Tile tile, int targetGridX, int targetGridY) {
        tile.setTargetPosition(targetGridX * TILE_SIZE, targetGridY * TILE_SIZE);
        movingTiles.addElement(tile);
        if (!moverThread.isAlive()) {
            moverThread.start();
        }
    }

    /**
     * Makes the mover to stop moving tiles.
     */
    public void stopMoves() {
        if (moverThread.isAlive() && movingTiles.size() > 0) {
            stop = true;
        }
    }

    class MoverThread
        extends Thread {

        public void run() {
            while (true) {
                for (int i = 0; i < movingTiles.size(); i++) {
                    try {
                        if (stop) {
                            break;
                        }
                        Tile tile = (Tile) movingTiles.elementAt(i);
                        int targetX = tile.getTargetX();
                        int targetY = tile.getTargetY();
                        int x = tile.getX();
                        int y = tile.getY();

                        /* move tile towards target positions */
                        if (x != targetX || y != targetY) {
                            if (x < targetX) {
                                tile.increaseX(SPEED);
                                if (tile.getX() > targetX) {
                                    tile.setX(targetX);
                                }
                            }
                            else if (x > targetX) {
                                tile.increaseX(-SPEED);
                                if (tile.getX() < targetX) {
                                    tile.setX(targetX);
                                }
                            }
                            if (y < targetY) {
                                tile.increaseY(SPEED);
                                if (tile.getY() > targetY) {
                                    tile.setY(targetY);
                                }
                            }
                            else if (y > targetY) {
                                tile.increaseY(-SPEED);
                                if (tile.getY() < targetY) {
                                    tile.setY(targetY);
                                }
                            }
                        }
                        else {
                            /* tile is in its target position, remove it from the list */
                            movingTiles.removeElementAt(i);
                            i--;

                            /* set grid coordinates */
                            tile.setGridX(targetX / TILE_SIZE);
                            tile.setGridY(targetY / TILE_SIZE);

                            /* if no moving tiles left, inform the current puzzle */
                            if (movingTiles.isEmpty()) {
                                component.getCurrentPuzzle().onMovingStopped();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (stop) {
                    movingTiles.removeAllElements();
                    stop = false;
                }
                try {
                    Thread.sleep(MiddleContainer.FPS);
                }
                catch (InterruptedException ie) {
                }
            }
        }
    }
}
