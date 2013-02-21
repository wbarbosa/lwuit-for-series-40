/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.components;

import com.nokia.example.slidepuzzle.puzzle.Puzzle;
import com.nokia.example.slidepuzzle.puzzle.Tile;
import com.nokia.example.slidepuzzle.puzzle.TileMover;
import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.util.Dir;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.nokia.example.slidepuzzle.util.RMSHelper;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;
import java.io.*;
import java.util.Vector;

public class PuzzleComponent
    extends Component {

    public static final int GRID_SIZE = 3; // size of the puzzle in tiles
    public static int TILE_SIZE; // size of a tile in pixels
    public static int BORDER_WIDTH; // thickness of the border around puzzle
    private MiddleContainer middleContainer;
    private Vector puzzles; // list of puzzles
    private Puzzle currentPuzzle; // current active puzzle
    private Image backgroundImg;
    private TileMover tileMover; // helper object to move/shuffle tiles

    public PuzzleComponent(final MiddleContainer middleContainer) {
        super();
        this.middleContainer = middleContainer;

        // Set background
        backgroundImg = ImageUtil.loadImage("puzzle_frame.png");
        getStyle().setBgTransparency(0x00);
        getSelectedStyle().setBgTransparency(0x00);

        TILE_SIZE = (int) Math.ceil(((float) Compatibility.PUZZLE_SIZE) / ((float) GRID_SIZE));
        BORDER_WIDTH = (backgroundImg.getWidth() - Compatibility.PUZZLE_SIZE) / 2;
        tileMover = new TileMover(this);

        // Pre-init puzzles
        puzzles = new Vector();
        puzzles.addElement(new Puzzle(this, "puzzle_telephone.png"));
        puzzles.addElement(new Puzzle(this, "puzzle_climber.png"));
        puzzles.addElement(new Puzzle(this, "puzzle_water.png"));

        // Load previous game if possible
        if (!loadGame()) {
            Puzzle firstPuzzle = (Puzzle) puzzles.firstElement();
            changePuzzle(firstPuzzle);
        }
    }

    /**
     * Creates and show a puzzle with a picture taken in camera view.
     *
     * @param pic Image taken with camera
     */
    public void showOwnPicPuzzle(Image pic) {
        if (pic != null) {
            int size = Compatibility.PUZZLE_SIZE;
            int picWidth = pic.getWidth();
            int picHeight = pic.getHeight();

            Image puzzleImage;
            if (picWidth >= size && picHeight >= size) {
                puzzleImage = pic.subImage(
                    (picWidth - size) / 2, (picHeight - size) / 2, size, size,
                    false);
            }
            else {
                puzzleImage = pic.scaled(size, size);
            }

            Puzzle newPuzzle = new Puzzle(this, puzzleImage);
            puzzles.addElement(newPuzzle);
            changePuzzle(newPuzzle);
        }

        middleContainer.getGameView().show();
        middleContainer.showPuzzle();
    }

    /**
     * Make the given puzzle active.
     *
     * @param puzzle Puzzle to be made active
     */
    public final void changePuzzle(Puzzle puzzle) {
        tileMover.stopMoves();
        puzzle.setShuffling(true);
        
        if (currentPuzzle != null) {
            // Free the previous puzzle
            currentPuzzle.free();
            if (currentPuzzle.isOwnPuzzle()) {
                puzzles.removeElement(currentPuzzle);
            }
        }
        
        puzzle.init();
        currentPuzzle = puzzle;
        middleContainer.getGameView().clearMoves();
        
        new Thread() {
            public void run() {
                try {
                    // Show the picture for a second, then shuffle
                    Thread.sleep(1000);
                    currentPuzzle.shuffleTiles();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void paint(Graphics g) {
        g.drawImage(backgroundImg, getX(), getY());
        currentPuzzle.paint(g);
    }

    public void pointerPressed(int x, int y) {
        currentPuzzle.pointerPressed(x - getAbsoluteX(), y - getAbsoluteY());
    }

    public void pointerDragged(int x, int y) {
        currentPuzzle.pointerDragged(x - getAbsoluteX(), y - getAbsoluteY());
    }

    public void pointerReleased(int x, int y) {
        currentPuzzle.pointerReleased(x - getAbsoluteX(), y - getAbsoluteY());
    }

    public void keyPressed(int gameAction) {
        switch (gameAction) {
            case Display.GAME_LEFT:
                currentPuzzle.moveKeyPressed(Dir.LEFT);
                break;
            case Display.GAME_RIGHT:
                currentPuzzle.moveKeyPressed(Dir.RIGHT);
                break;
            case Display.GAME_UP:
                currentPuzzle.moveKeyPressed(Dir.UP);
                break;
            case Display.GAME_DOWN:
                currentPuzzle.moveKeyPressed(Dir.DOWN);
                break;
        }
    }

    /**
     * Changes to the next puzzle.
     */
    public void nextPuzzle() {
        int currentIndex = puzzles.indexOf(currentPuzzle);
        currentIndex++;
        
        if (currentIndex >= puzzles.size()) {
            currentIndex = 0;
        }
        
        changePuzzle((Puzzle) puzzles.elementAt(currentIndex));
    }

    /**
     * Shuffles the current puzzle.
     */
    public void shuffleButton() {
        middleContainer.showPuzzle();
        currentPuzzle.shuffleTiles();
    }

    /**
     * Changes to the next puzzle.
     */
    public void nextButton() {
        middleContainer.showPuzzle();
        nextPuzzle();
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);

            // Write index of current puzzle
            dout.writeInt(puzzles.indexOf(currentPuzzle));

            // Write order of tiles
            Tile[][] tiles = currentPuzzle.getTiles();
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    dout.writeInt(tiles[j][i].getGridX());
                    dout.writeInt(tiles[j][i].getGridY());
                }
            }
            
            // Write coordinates of the empty tile
            dout.writeInt(currentPuzzle.getEmptyTile().getGridX());
            dout.writeInt(currentPuzzle.getEmptyTile().getGridY());
            
            // Write number of moves
            dout.writeInt(middleContainer.getGameView().getMoves());

            RMSHelper.save(bout.toByteArray());
            bout.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    public MiddleContainer getMiddleContainer() {
        return middleContainer;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public TileMover getTileMover() {
        return tileMover;
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(
            Compatibility.PUZZLE_SIZE + BORDER_WIDTH * 2,
            Compatibility.PUZZLE_SIZE + BORDER_WIDTH * 2);
    }

    private boolean loadGame() {
        byte[] state = RMSHelper.load();
        if (state == null) {
            return false;
        }
        
        DataInputStream din;
        try {
            din = new DataInputStream(new ByteArrayInputStream(state));

            // Read index of player puzzle
            Puzzle loadedPuzzle = (Puzzle) puzzles.elementAt(din.readInt());

            // Read the order of tiles
            int[] gridXs = new int[GRID_SIZE * GRID_SIZE];
            int[] gridYs = new int[GRID_SIZE * GRID_SIZE];
            
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int index = i * GRID_SIZE + j;
                    gridXs[index] = din.readInt();
                    gridYs[index] = din.readInt();
                }
            }
            
            // Init puzzle
            loadedPuzzle.init(gridXs, gridYs);
            
            // Read empty tile and set it
            loadedPuzzle.setEmptyTile(din.readInt(), din.readInt());
            
            // Read number of moves and show it
            middleContainer.getGameView().setMoves(din.readInt());
            
            currentPuzzle = loadedPuzzle;
            
            din.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }

        return true;
    }
}