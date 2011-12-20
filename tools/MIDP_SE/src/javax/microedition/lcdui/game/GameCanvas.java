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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * The GameCanvas class provides the basis for a game user interface. In addition to the features inherited from Canvas (commands, input events, etc.) it also provides game-specific capabilities such as an off-screen graphics buffer and the ability to query key status.
 * A dedicated buffer is created for each GameCanvas instance. Since a unique buffer is provided for each GameCanvas instance, it is preferable to re-use a single GameCanvas instance in the interests of minimizing heap usage. The developer can assume that the contents of this buffer are modified only by calls to the Graphics object(s) obtained from the GameCanvas instance; the contents are not modified by external sources such as other MIDlets or system-level notifications. The buffer is initially filled with white pixels.
 * The buffer's size is set to the maximum dimensions of the GameCanvas. However, the area that may be flushed is limited by the current dimensions of the GameCanvas (as influenced by the presence of a Ticker, Commands, etc.) when the flush is requested. The current dimensions of the GameCanvas may be obtained by calling getWidth and getHeight.
 * A game may provide its own thread to run the game loop. A typical loop will check for input, implement the game logic, and then render the updated user interface. The following code illustrates the structure of a typcial game loop: // Get the Graphics object for the off-screen buffer Graphics g = getGraphics(); while (true) { // Check user input and update positions if necessary int keyState = getKeyStates(); if ((keyState & LEFT_PRESSED) != 0) { sprite.move(-1, 0); } else if ((keyState & RIGHT_PRESSED) != 0) { sprite.move(1, 0); } // Clear the background to white g.setColor(0xFFFFFF); g.fillRect(0,0,getWidth(), getHeight()); // Draw the Sprite sprite.paint(g); // Flush the off-screen buffer flushGraphics(); }
 * Since: MIDP 2.0
 */
public abstract class GameCanvas extends Canvas{
    /**
     * The bit representing the DOWN key. This constant has a value of 0x0040 (1 << Canvas.DOWN).
     * See Also:Constant Field Values
     */
    public static final int DOWN_PRESSED=64;

    /**
     * The bit representing the FIRE key. This constant has a value of 0x0100 (1 << Canvas.FIRE).
     * See Also:Constant Field Values
     */
    public static final int FIRE_PRESSED=256;

    /**
     * The bit representing the GAME_A key (may not be supported on all devices). This constant has a value of 0x0200 (1 << Canvas.GAME_A).
     * See Also:Constant Field Values
     */
    public static final int GAME_A_PRESSED=512;

    /**
     * The bit representing the GAME_B key (may not be supported on all devices). This constant has a value of 0x0400 (1 << Canvas.GAME_B).
     * See Also:Constant Field Values
     */
    public static final int GAME_B_PRESSED=1024;

    /**
     * The bit representing the GAME_C key (may not be supported on all devices). This constant has a value of 0x0800 (1 << Canvas.GAME_C).
     * See Also:Constant Field Values
     */
    public static final int GAME_C_PRESSED=2048;

    /**
     * The bit representing the GAME_D key (may not be supported on all devices). This constant has a value of 0x1000 (1 << Canvas.GAME_D).
     * See Also:Constant Field Values
     */
    public static final int GAME_D_PRESSED=4096;

    /**
     * The bit representing the LEFT key. This constant has a value of 0x0004 (1 << Canvas.LEFT).
     * See Also:Constant Field Values
     */
    public static final int LEFT_PRESSED=4;

    /**
     * The bit representing the RIGHT key. This constant has a value of 0x0020 (1 << Canvas.RIGHT).
     * See Also:Constant Field Values
     */
    public static final int RIGHT_PRESSED=32;

    /**
     * The bit representing the UP key. This constant has a value of 0x0002 (1 << Canvas.UP).
     * See Also:Constant Field Values
     */
    public static final int UP_PRESSED=2;

    /**
     * Creates a new instance of a GameCanvas. A new buffer is also created for the GameCanvas and is initially filled with white pixels.
     * If the developer only needs to query key status using the getKeyStates method, the regular key event mechanism can be suppressed for game keys while this GameCanvas is shown. If not needed by the application, the suppression of key events may improve performance by eliminating unnecessary system calls to keyPressed, keyRepeated and keyReleased methods.
     * If requested, key event suppression for a given GameCanvas is started when it is shown (i.e. when showNotify is called) and stopped when it is hidden (i.e. when hideNotify is called). Since the showing and hiding of screens is serialized with the event queue, this arrangement ensures that the suppression effects only those key events intended for the corresponding GameCanvas. Thus, if key events are being generated while another screen is still shown, those key events will continue to be queued and dispatched until that screen is hidden and the GameCanvas has replaced it.
     * Note that key events can be suppressed only for the defined game keys (UP, DOWN, FIRE, etc.); key events are always generated for all other keys.
     * suppressKeyEvents - true to suppress the regular key event mechanism for game keys, otherwise false.
     */
    protected GameCanvas(boolean suppressKeyEvents){
         //TODO codavaj!!
    }

    /**
     * Flushes the off-screen buffer to the display. The size of the flushed area is equal to the size of the GameCanvas. The contents of the off-screen buffer are not changed as a result of the flush operation. This method does not return until the flush has been completed, so the app may immediately begin to render the next frame to the same buffer once this method returns.
     * This method does nothing and returns immediately if the GameCanvas is not currently shown or the flush request cannot be honored because the system is busy.
     */
    public void flushGraphics(){
        return; //TODO codavaj!!
    }

    /**
     * Flushes the specified region of the off-screen buffer to the display. The contents of the off-screen buffer are not changed as a result of the flush operation. This method does not return until the flush has been completed, so the app may immediately begin to render the next frame to the same buffer once this method returns.
     * If the specified region extends beyond the current bounds of the GameCanvas, only the intersecting region is flushed. No pixels are flushed if the specified width or height is less than 1.
     * This method does nothing and returns immediately if the GameCanvas is not currently shown or the flush request cannot be honored because the system is busy.
     */
    public void flushGraphics(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Obtains the Graphics object for rendering a GameCanvas. The returned Graphics object renders to the off-screen buffer belonging to this GameCanvas.
     * Rendering operations do not appear on the display until flushGraphics() is called; flushing the buffer does not change its contents (the pixels are not cleared as a result of the flushing operation).
     * A new Graphics object is created and returned each time this method is called; therefore, the needed Graphics object(s) should be obtained before the game starts then re-used while the game is running. For each GameCanvas instance, all of the provided graphics objects will render to the same off-screen buffer.
     * The newly created Graphics object has the following properties:
     * the destination is this GameCanvas' buffer; the clip region encompasses the entire buffer; the current color is black; the font is the same as the font returned by
     * ; the stroke style is
     * ; and the origin of the coordinate system is located at the upper-left corner of the buffer.
     */
    protected Graphics getGraphics(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the states of the physical game keys. Each bit in the returned integer represents a specific key on the device. A key's bit will be 1 if the key is currently down or has been pressed at least once since the last time this method was called. The bit will be 0 if the key is currently up and has not been pressed at all since the last time this method was called. This latching behavior ensures that a rapid key press and release will always be caught by the game loop, regardless of how slowly the loop runs.
     * For example: // Get the key state and store it int keyState = getKeyStates(); if ((keyState & LEFT_KEY) != 0) { positionX--; } else if ((keyState & RIGHT_KEY) != 0) { positionX++; }
     * Calling this method has the side effect of clearing any latched state. Another call to getKeyStates immediately after a prior call will therefore report the system's best idea of the current state of the keys, the latched bits having been cleared by the first call.
     * Some devices may not be able to query the keypad hardware directly and therefore, this method may be implemented by monitoring key press and release events instead. Thus the state reported by getKeyStates might lag the actual state of the physical keys since the timeliness of the key information is be subject to the capabilities of each device. Also, some devices may be incapable of detecting simultaneous presses of multiple keys.
     * This method returns 0 unless the GameCanvas is currently visible as reported by Displayable.isShown(). Upon becoming visible, a GameCanvas will initially indicate that all keys are unpressed (0); if a key is held down while the GameCanvas is being shown, the key must be first released and then pressed in order for the key press to be reported by the GameCanvas.
     */
    public int getKeyStates(){
        return 0; //TODO codavaj!!
    }

    /**
     * Paints this GameCanvas. By default, this method renders the the off-screen buffer at (0,0). Rendering of the buffer is subject to the clip region and origin translation of the Graphics object.
     */
    public void paint(Graphics g){
        return; //TODO codavaj!!
    }

}
