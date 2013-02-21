/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.puzzle;

import com.sun.lwuit.Image;

public class Tile {

    private final int TILE_SIZE; // size of a tile in pixels
    private Puzzle puzzle; // the puzzle this tile belongs to
    private Image image; // image of this tile
    private int gridX, gridY; // coordinates of this tile in tiles
    private int x, y; // pixel coordinates of this tile (relative to puzzle)
    private int targetX, targetY; // pixels coordinates of the target point of moving
    private int startGridX, startGridY; // grid coordinates of the start position of this tile

    public Tile(Puzzle puzzle, Image image, int gridX, int gridY) {
        this.puzzle = puzzle;
        this.image = image;
        this.gridX = gridX;
        this.gridY = gridY;
        TILE_SIZE = puzzle.getComponent().getTileSize();
        this.x = gridX * TILE_SIZE;
        this.y = gridY * TILE_SIZE;
    }

    /**
     * Tells whether this tile is next to the empty tile.
     *
     * @return true it the tile is next to the empty tile, false otherwise
     */
    public boolean nextToEmptyTile() {
        Tile emptyTile = puzzle.getEmptyTile();
        if (emptyTile == null) {
            return false;
        }

        int eX = emptyTile.getGridX();
        int eY = emptyTile.getGridY();

        return (gridX == eX && Math.abs(gridY - eY) == 1) || (gridY == eY && Math.abs(gridX - eX)
            == 1);
    }

    /**
     * Increases the x coordinate (in pixels) of the tile by n.
     *
     * @param n increase of x in pixels (can be negative)
     */
    public void increaseX(int n) {
        x += n;
    }

    /**
     * Increases the y coordinate (in pixels) of the tile by n.
     *
     * @param n increase of yin pixels (can be negative)
     */
    public void increaseY(int n) {
        y += n;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void setStartPosition(int startGridX, int startGridY) {
        this.startGridX = startGridX;
        this.startGridY = startGridY;
    }

    public void setTargetPosition(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public int getStartGridX() {
        return startGridX;
    }

    public int getStartGridY() {
        return startGridY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }
}
