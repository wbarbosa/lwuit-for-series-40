/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.util;

/**
 * A helper class for moving tiles to different directions.
 */
public class Dir {

    public static final Dir LEFT = new Dir(-1, 0);
    public static final Dir RIGHT = new Dir(1, 0);
    public static final Dir UP = new Dir(0, -1);
    public static final Dir DOWN = new Dir(0, 1);
    public static final Dir NONE = new Dir(0, 0);
    private int dX, dY;

    /**
     * Create a new direction
     * 
     * @param dX delta x of the direction in puzzle grid
     * @param dY delta y of the direction in puzzle grid
     */
    private Dir(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    /**
     * Return the opposite direction of the direction
     * 
     * @return the opposite direction of the direction
     */
    public Dir getOppositeDir() {
        if (this == LEFT) {
            return RIGHT;
        }
        if (this == RIGHT) {
            return LEFT;
        }
        if (this == UP) {
            return DOWN;
        }
        if (this == DOWN) {
            return UP;
        }
        return NONE;
    }

    public int getDX() {
        return dX;
    }

    public int getDY() {
        return dY;
    }
}
