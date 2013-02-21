/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.puzzle;

import com.nokia.example.slidepuzzle.components.PuzzleComponent;
import com.nokia.example.slidepuzzle.util.Compatibility;
import com.nokia.example.slidepuzzle.util.Dir;
import com.nokia.example.slidepuzzle.util.ImageUtil;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import java.util.Random;

public class Puzzle {

    private static final Image EDGE_IMG = ImageUtil.loadImage("tile_edges.png");
    private static final Image FLAP_IMG = ImageUtil.loadImage("tile_flaps.png");
    private PuzzleComponent component; // the component holding puzzles
    private String imageFilename; // file name of the picture of the puzzle
    private Image image; // picture of the puzzle
    private Tile[][] tiles; // tile grid
    private Tile emptyTile = null; // the empty tile in the puzzle
    private Tile pressedTile = null; // the tile that is being dragged
    private int grabX = 0; // the x point where the tile is grabbed
    private int grabY = 0; // the y point where the tile is grabbed
    private boolean moving = false; // a tile is being moved
    private boolean shuffling = false; // the puzzle is being shuffled
    private boolean winEffect = false; // the winning effect is being shown
    private int winEffectAlpha = 255; // alpha value of the black box shown in the winning effect

    /**
     * Constructs a puzzle by the file name of its image. Does not load the image or shuffle yet.
     *
     * @param component PuzzleComponent that holds the puzzle
     * @param imageFilename File name of the puzzle picture
     */
    public Puzzle(PuzzleComponent component, String imageFilename) {
        this.component = component;
        this.imageFilename = imageFilename;
    }

    /**
     * Constructs a puzzle by image. Does not shuffle yet.
     *
     * @param component PuzzleComponent that holds the puzzle
     * @param image Picture of the puzzle
     */
    public Puzzle(PuzzleComponent component, Image image) {
        this.component = component;
        this.image = image;
    }

    /**
     * Initializes the puzzle.
     */
    public void init() {
        init(null, null);
    }

    /**
     * Initializes the puzzle with given tile coordinates.
     *
     * @param gridXs x coordinates of the tiles
     * @param gridYs y coordinates of the tiles
     */
    public void init(int[] gridXs, int[] gridYs) {
        boolean loadedPositions = false;
        if (gridXs != null && gridYs != null) {
            loadedPositions = true;
        }

        int gridSize = PuzzleComponent.GRID_SIZE;
        int tileSize = PuzzleComponent.TILE_SIZE;
        tiles = new Tile[gridSize][gridSize];
        // if image not already loaded, load it
        if (image == null) {
            image = ImageUtil.loadImage(imageFilename);
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                /* Take the correct sub image from puzzle image and set it as tile image. Draw also an image
                 * containing edges on the sub image. */
                Image tileImg = Image.createImage(tileSize, tileSize);
                Graphics g = tileImg.getGraphics();
                g.drawImage(image.subImage(j * tileSize, i * tileSize,
                    tileSize, tileSize, false), 0, 0);
                g.drawImage(EDGE_IMG, 0, 0);
                if (loadedPositions) {
                    tiles[j][i] = new Tile(this, tileImg, gridXs[i * gridSize + j], gridYs[i
                        * gridSize + j]);
                }
                else {
                    tiles[j][i] = new Tile(this, tileImg, j, i);
                }
            }
        }
        image = null;
    }

    /**
     * Makes the tiles and their images eligible for garbage collection.
     */
    public void free() {
        tiles = null;
    }

    /**
     * Shuffles the tiles.
     */
    public void shuffleTiles() {
        shuffling = true;
        component.getMiddleContainer().getGameView().clearMoves();
        emptyTile = null;

        randomStartPositions();
        moveTilesToStartPositions();
    }

    /**
     * Tells whether the picture of the puzzle is self-taken in 'Take pic' menu item
     *
     * @return true if the puzzle picture is self-taken, false otherwise
     */
    public boolean isOwnPuzzle() {
        return imageFilename == null;
    }

    /**
     * A callback method called when the TileMover has finished all its movings
     */
    public void onMovingStopped() {
        // set empty tile
        if (shuffling) {
            emptyTile = tiles[PuzzleComponent.GRID_SIZE - 1][PuzzleComponent.GRID_SIZE - 1];
            shuffling = false;
        }

        checkIfCorrect();
        moving = false;
        component.repaint();
    }

    public void paint(Graphics g) {
        if (tiles == null) {
            return;
        }

        /* use base coordinates as puzzle related origo */
        int baseX = component.getX() + PuzzleComponent.BORDER_WIDTH;
        int baseY = component.getY() + PuzzleComponent.BORDER_WIDTH;

        Tile tile = null;

        /* draw tile flaps */
        if (!shuffling) {
            int flapX = -9;
            int flapY = 9 + PuzzleComponent.TILE_SIZE - FLAP_IMG.getHeight();
            for (int i = 0; i < PuzzleComponent.GRID_SIZE; i++) {
                for (int j = 0; j < PuzzleComponent.GRID_SIZE; j++) {
                    tile = tiles[j][i];
                    // clip to prevent drawing flaps outside the puzzle area
                    g.clipRect(baseX, baseY, Compatibility.PUZZLE_SIZE, Compatibility.PUZZLE_SIZE);
                    if (tile != emptyTile) {
                        g.drawImage(FLAP_IMG, baseX + tile.getX() + flapX, baseY + tile.getY()
                            + flapY);
                    }
                }
            }
        }

        /* draw actual tiles */
        for (int i = 0; i < PuzzleComponent.GRID_SIZE; i++) {
            for (int j = 0; j < PuzzleComponent.GRID_SIZE; j++) {
                tile = tiles[j][i];
                Image img = tile.getImage();
                if (tile != emptyTile) {
                    g.drawImage(img, baseX + tile.getX(), baseY + tile.getY());
                }
            }
        }

        /* winning effect */
        if (winEffect) {
            g.setColor(0x000000);
            g.setAlpha(winEffectAlpha);
            g.fillRect(baseX, baseY, PuzzleComponent.GRID_SIZE * PuzzleComponent.TILE_SIZE,
                PuzzleComponent.GRID_SIZE * PuzzleComponent.TILE_SIZE);
        }
    }

    public void pointerPressed(int x, int y) {
        int gridX = x / PuzzleComponent.TILE_SIZE;
        int gridY = y / PuzzleComponent.TILE_SIZE;

        Tile tile = getTileAt(gridX, gridY);
        if (tile != null && tile != emptyTile && !moving && !shuffling) {
            /* grab the correct tile */
            grabX = x - gridX * PuzzleComponent.TILE_SIZE;
            grabY = y - gridY * PuzzleComponent.TILE_SIZE;
            pressedTile = getTileAt(gridX, gridY);
        }
    }

    public void pointerDragged(int x, int y) {
        if (pressedTile != null && emptyTile != null) {
            /* determine the direction (x or y) */
            if (emptyTile.getGridY() == pressedTile.getGridY()) {
                int pressedTileX = pressedTile.getGridX() * PuzzleComponent.TILE_SIZE;
                /* determine the direction (+ or -) */
                if (emptyTile.getGridX() - pressedTile.getGridX() == 1) {
                    pressedTile.setX(Math.max(Math.min(x - grabX, emptyTile.getX()), pressedTileX));
                }
                else if (pressedTile.getGridX() - emptyTile.getGridX() == 1) {
                    pressedTile.setX(Math.min(Math.max(x - grabX, emptyTile.getX()), pressedTileX));
                }
            }
            else if (emptyTile.getGridX() == pressedTile.getGridX()) {
                int pressedTileY = pressedTile.getGridY() * PuzzleComponent.TILE_SIZE;
                /* determine the direction (+ or -) */
                if (emptyTile.getGridY() - pressedTile.getGridY() == 1) {
                    pressedTile.setY(Math.max(Math.min(y - grabY, emptyTile.getY()), pressedTileY));
                }
                else if (pressedTile.getGridY() - emptyTile.getGridY() == 1) {
                    pressedTile.setY(Math.min(Math.max(y - grabY, emptyTile.getY()), pressedTileY));
                }
            }
        }
    }

    public void pointerReleased(int x, int y) {
        releaseDraggedTile();
    }

    /*
     * Releases the dragged tile. If the release happens near empty tile, it is moved there.
     * Otherwise the tile is returned to the start position.
     */
    public void releaseDraggedTile() {
        if (pressedTile != null) {
            // shorter refs
            int size = PuzzleComponent.TILE_SIZE;
            Tile pt = pressedTile;
            Tile et = emptyTile;
            int px = pt.getX();
            int py = pt.getY();
            int ex = et.getX();
            int ey = et.getY();

            if (Math.abs(px - ex) < size / 2 && Math.abs(py - ey) < size / 2) {
                /* swap positions of empty tile and pressed tile */
                int tempGridX = pt.getGridX();
                int tempGridY = pt.getGridY();
                pt.setGridX(et.getGridX());
                pt.setGridY(et.getGridY());
                pt.setX(ex);
                pt.setY(ey);
                et.setGridX(tempGridX);
                et.setGridY(tempGridY);
                et.setX(tempGridX * size);
                et.setY(tempGridY * size);
                component.getMiddleContainer().getGameView().incrementMoves();
            }
            else {
                /* return pressed tile back */
                pt.setX(pt.getGridX() * size);
                pt.setY(pt.getGridY() * size);
            }
            pressedTile = null;
        }
        checkIfCorrect();
        component.repaint();
    }

    /**
     * Handles key press events that could make a tile to move
     *
     * @param dir Direction of the key event
     */
    public void moveKeyPressed(Dir dir) {
        if (shuffling)
            return;
        /* obtain the tile to be moved (next to the empty tile) */
        dir = dir.getOppositeDir();
        int gridX = emptyTile.getGridX() + dir.getDX();
        int gridY = emptyTile.getGridY() + dir.getDY();

        // try to move the tile
        tryToMove(gridX, gridY);
    }

    /**
     * Makes the tile at the given coordinates to move if allowed.
     *
     * @param gridX grid x coordinate of the tile
     * @param gridY grid y coordinate of the tile
     */
    public void tryToMove(int gridX, int gridY) {
        if (movingAllowed(gridX, gridY)) {
            Tile tile = getTileAt(gridX, gridY);
            moveTile(tile);
        }
    }

    /**
     * Makes the tile at the given coordinates empty
     *
     * @param gridX grid x coordinate of the tile
     * @param gridY grid y coordinate of the tile
     */
    public void setEmptyTile(int gridX, int gridY) {
        Tile tile = getTileAt(gridX, gridY);
        emptyTile = tile;
    }

    /**
     * Returns the tile at the given coordinates.
     *
     * @param gridX grid x coordinate of the tile
     * @param gridY grid y coordinate of the tile
     * @return Tile at the given coordinates
     */
    public Tile getTileAt(int gridX, int gridY) {
        if (gridX < 0 || gridY < 0 || gridX >= PuzzleComponent.GRID_SIZE || gridY
            >= PuzzleComponent.GRID_SIZE) {
            return null;
        }

        for (int i = 0; i < PuzzleComponent.GRID_SIZE; i++) {
            for (int j = 0; j < PuzzleComponent.GRID_SIZE; j++) {
                Tile tile = tiles[j][i];
                if (tile.getGridX() == gridX && tile.getGridY() == gridY) {
                    return tile;
                }
            }
        }
        return null;
    }

    public Tile getEmptyTile() {
        return emptyTile;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public PuzzleComponent getComponent() {
        return component;
    }

    public void setShuffling(boolean shuffling) {
        this.shuffling = shuffling;
    }

    public boolean isMotionOngoing() {
        return shuffling || moving || pressedTile != null || winEffect;
    }

    /**
     * Checks if the order of the tiles is correct if it is, a winning effect is shown.
     */
    private void checkIfCorrect() {
        if (component.getMiddleContainer().getGameView().getMoves() == 0 || shuffling) {
            return;
        }
        /* check correctness */
        boolean correct = true;
        for (int i = 0; i < PuzzleComponent.GRID_SIZE; i++) {
            for (int j = 0; j < PuzzleComponent.GRID_SIZE; j++) {
                Tile tile = tiles[j][i];
                if ((tile.getGridX() != j || tile.getGridY() != i) && !(i == PuzzleComponent.GRID_SIZE
                    - 1 && j == PuzzleComponent.GRID_SIZE
                    - 1)) {
                    correct = false;
                }
            }
        }

        /* if correct, trigger winning effect */
        if (correct) {
            winEffect = true;
            emptyTile = null;
            winEffectAlpha = 255;
            new Thread() {

                public void run() {
                    /* create a fading black box over picture */
                    while (winEffectAlpha > 0) {
                        winEffectAlpha -= 5;
                        try {
                            Thread.sleep(40);
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }

                    /* after fading, show the picutre for a second */
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    /* proceed to next puzzle */
                    winEffect = false;
                    component.nextPuzzle();
                }
            }.start();
        }
    }

    /**
     * Makes a tile to move.
     *
     * @param tile Tile to be moved
     */
    private void moveTile(Tile tile) {
        moving = true;
        component.getMiddleContainer().getGameView().incrementMoves();
        TileMover mover = component.getTileMover();
        mover.moveTile(tile, emptyTile.getGridX(), emptyTile.getGridY());
        mover.moveTile(emptyTile, tile.getGridX(), tile.getGridY());
    }

    /**
     * Tells whether moving of the given tile is allowed. Moving is allowed if 1. the puzzle is not
     * being shuffled 2. any tile is not being moved 3. there is a tile at the given coordinates 4.
     * the tile to be moved is next to the empty tile
     *
     * @param gridX grid x coordinate of the tile
     * @param gridY grid y coordinate of the tile
     * @return true if moving is allowed, false otherwise
     */
    private boolean movingAllowed(int gridX, int gridY) {
        return !shuffling && !moving && gridX >= 0 && gridX < PuzzleComponent.GRID_SIZE && gridY
            >= 0 && gridY < PuzzleComponent.GRID_SIZE && getTileAt(gridX, gridY).nextToEmptyTile();
    }

    /**
     * Generates random start positions for the tiles of this puzzle.
     */
    private void randomStartPositions() {
        int gridSize = PuzzleComponent.GRID_SIZE;
        int max = gridSize * gridSize;

        /* array positions contains the order of the tiles so that for example if positions[n] = m, the tile
         * at coordinates (n % gridSize, n / gridSize) holds the tile at tiles[m % gridSize][m /
         * gridSize] */
        int[] positions = new int[max];
        Random random = new Random();
        boolean solvable = false;
        do {
            // clear positions
            int len = positions.length;
            for (int i = 0; i < len; i++) {
                positions[i] = -1;
            }

            // generate order
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int cand;
                    while (positions[cand = random.nextInt(max)] >= 0 || (cand % gridSize == i && cand
                        / gridSize == j && i != gridSize - 1 && j != gridSize - 1))
                    ;

                    positions[cand] = i * gridSize + j;
                }
            }

            // check solvability (http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html)
            int inversions = 0;
            for (int i = 0; i < len; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (positions[i] == len - 1 || positions[j] == len - 1) {
                        continue;
                    }
                    if (positions[i] > positions[j]) {
                        inversions++;
                    }
                }
            }
            // if solvable, set tile coordinates
            if (inversions % 2 == 0) {
                solvable = true;
                for (int i = 0; i < len; i++) {
                    int x = positions[i] % gridSize;
                    int y = positions[i] / gridSize;
                    tiles[x][y].setStartPosition(i % gridSize, i / gridSize);
                }
            }
            // else try again with new order
        }
        while (!solvable);
    }

    /**
     * Makes all tiles to move towards their start positions.
     */
    private void moveTilesToStartPositions() {
        for (int i = 0; i < PuzzleComponent.GRID_SIZE; i++) {
            for (int j = 0; j < PuzzleComponent.GRID_SIZE; j++) {
                Tile tile = tiles[j][i];
                component.getTileMover().moveTile(tile, tile.getStartGridX(), tile.getStartGridY());
            }
        }
    }
}
