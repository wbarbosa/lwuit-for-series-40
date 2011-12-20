/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * A TiledLayer is a visual element composed of a grid of cells that can be filled with a set of tile images. This class allows large virtual layers to be created without the need for an extremely large Image. This technique is commonly used in 2D gaming platforms to create very large scrolling backgrounds,
 * A static tile set is created when the TiledLayer is instantiated; it can also be updated at any time using the setStaticTileSet(javax.microedition.lcdui.Image, int, int) method.
 * In addition to the static tile set, the developer can also define several animated tiles. An animated tile is a virtual tile that is dynamically associated with a static tile; the appearance of an animated tile will be that of the static tile that it is currently associated with.
 * Animated tiles allow the developer to change the appearance of a group of cells very easily. With the group of cells all filled with the animated tile, the appearance of the entire group can be changed by simply changing the static tile associated with the animated tile. This technique is very useful for animating large repeating areas without having to explicitly change the contents of numerous cells.
 * Animated tiles are created using the createAnimatedTile(int) method, which returns the index to be used for the new animated tile. The animated tile indices are always negative and consecutive, beginning with -1. Once created, the static tile associated with an animated tile can be changed using the setAnimatedTile(int, int) method.
 * The contents of each cell is specified by means of a tile index; a positive tile index refers to a static tile, and a negative tile index refers to an animated tile. A tile index of 0 indicates that the cell is empty; an empty cell is fully transparent and nothing is drawn in that area by the TiledLayer. By default, all cells contain tile index 0.
 * The contents of cells may be changed using setCell(int, int, int) and fillCells(int, int, int, int, int). Several cells may contain the same tile; however, a single cell cannot contain more than one tile. The following example illustrates how a simple background can be created using a TiledLayer. In this example, the area of water is filled with an animated tile having an index of -1, which is initially associated with static tile 5. The entire area of water may be animated by simply changing the associated static tile using setAnimatedTile(-1, 7).
 * The paint method will attempt to render the entire TiledLayer subject to the clip region of the Graphics object; the upper left corner of the TiledLayer is rendered at its current (x,y) position relative to the Graphics object's origin. The rendered region may be controlled by setting the clip region of the Graphics object accordingly.
 * Since: MIDP 2.0
 */
public class TiledLayer extends Layer{
    /**
     * Creates a new TiledLayer.
     * The TiledLayer's grid will be rows cells high and columns cells wide. All cells in the grid are initially empty (i.e. they contain tile index 0). The contents of the grid may be modified through the use of setCell(int, int, int) and fillCells(int, int, int, int, int).
     * The static tile set for the TiledLayer is created from the specified Image with each tile having the dimensions of tileWidth x tileHeight. The width of the source image must be an integer multiple of the tile width, and the height of the source image must be an integer multiple of the tile height; otherwise, an IllegalArgumentException is thrown;
     * The entire static tile set can be changed using setStaticTileSet(Image, int, int). These methods should be used sparingly since they are both memory and time consuming. Where possible, animated tiles should be used instead to animate tile appearance.
     * columns - the width of the TiledLayer, expressed as a number of cellsrows - the height of the TiledLayer, expressed as a number of cellsimage - the Image to use for creating the static tile settileWidth - the width in pixels of a single tiletileHeight - the height in pixels of a single tile
     * - if image is null
     * - if the number of rows or columns is less than 1
     * - if tileHeight or tileWidth is less than 1
     * - if the image width is not an integer multiple of the tileWidth
     * - if the image height is not an integer multiple of the tileHeight
     */
    public TiledLayer(int columns, int rows, Image image, int tileWidth, int tileHeight){
         //TODO codavaj!!
    }

    /**
     * Creates a new animated tile and returns the index that refers to the new animated tile. It is initially associated with the specified tile index (either a static tile or 0).
     * The indices for animated tiles are always negative. The first animated tile shall have the index -1, the second, -2, etc.
     */
    public int createAnimatedTile(int staticTileIndex){
        return 0; //TODO codavaj!!
    }

    /**
     * Fills a region cells with the specific tile. The cells may be filled with a static tile index, an animated tile index, or they may be left empty (index 0).
     */
    public void fillCells(int col, int row, int numCols, int numRows, int tileIndex){
        return; //TODO codavaj!!
    }

    /**
     * Gets the tile referenced by an animated tile.
     * Returns the tile index currently associated with the animated tile.
     */
    public int getAnimatedTile(int animatedTileIndex){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the contents of a cell.
     * Gets the index of the static or animated tile currently displayed in a cell. The returned index will be 0 if the cell is empty.
     */
    public int getCell(int col, int row){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the height of a single cell, in pixels.
     */
    public final int getCellHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the width of a single cell, in pixels.
     */
    public final int getCellWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the number of columns in the TiledLayer grid. The overall width of the TiledLayer, in pixels, may be obtained by calling
     * .
     */
    public final int getColumns(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the number of rows in the TiledLayer grid. The overall height of the TiledLayer, in pixels, may be obtained by calling
     * .
     */
    public final int getRows(){
        return 0; //TODO codavaj!!
    }

    /**
     * Draws the TiledLayer. The entire TiledLayer is rendered subject to the clip region of the Graphics object. The TiledLayer's upper left corner is rendered at the TiledLayer's current position relative to the origin of the Graphics object. The current position of the TiledLayer's upper-left corner can be retrieved by calling
     * and
     * . The appropriate use of a clip region and/or translation allows an arbitrary region of the TiledLayer to be rendered.
     * If the TiledLayer's Image is mutable, the TiledLayer is rendered using the current contents of the Image.
     */
    public final void paint(Graphics g){
        return; //TODO codavaj!!
    }

    /**
     * Associates an animated tile with the specified static tile.
     */
    public void setAnimatedTile(int animatedTileIndex, int staticTileIndex){
        return; //TODO codavaj!!
    }

    /**
     * Sets the contents of a cell.
     * The contents may be set to a static tile index, an animated tile index, or it may be left empty (index 0)
     */
    public void setCell(int col, int row, int tileIndex){
        return; //TODO codavaj!!
    }

    /**
     * Change the static tile set.
     * Replaces the current static tile set with a new static tile set. See the constructor TiledLayer(int, int, Image, int, int) for information on how the tiles are created from the image.
     * If the new static tile set has as many or more tiles than the previous static tile set, the the animated tiles and cell contents will be preserve. If not, the contents of the grid will be cleared (all cells will contain index 0) and all animated tiles will be deleted.
     */
    public void setStaticTileSet(Image image, int tileWidth, int tileHeight){
        return; //TODO codavaj!!
    }

}
