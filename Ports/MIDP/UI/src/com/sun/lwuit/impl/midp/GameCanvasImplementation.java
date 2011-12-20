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
package com.sun.lwuit.impl.midp;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.PeerComponent;
import com.sun.lwuit.TextArea;
    import com.sun.lwuit.VideoComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.plaf.UIManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.media.protocol.DataSource;

/**
 * An implementation of LWUIT based on game canvas, this is the default implementation
 * class for LWUIT which most customizers should extend to enhance.
 *
 * @author Shai Almog
 */
public class GameCanvasImplementation extends LWUITImplementation {
    private static boolean NOKIA = false;
    private boolean minimized;
    private MIDlet mid;
    private Object currentlyPlayingAudio;

    /**
     * On some devices getKeyCode returns numeric values for game actions,
     * this breaks the code since we filter these values. We pick unused 
     * negative values for game keys and assign them to game keys for getKeyCode.
     */
    private static int[] portableKeyCodes;
    private static int[] portableKeyCodeValues;
    private int alpha = 255;
    private int[] rgbArr;

    private Canvas canvas;
    private class C extends GameCanvas implements CommandListener, Runnable {
        private boolean done;
        private Command[] currentCommands;
        class MIDP2LWUITCommand extends Command {
            com.sun.lwuit.Command internal;
            public MIDP2LWUITCommand(com.sun.lwuit.Command c, int offset) {
                super(c.getCommandName(), Command.SCREEN, offset);
                internal = c;
            }
            public MIDP2LWUITCommand(com.sun.lwuit.Command c, int type, int offset) {
                super(c.getCommandName(), type, offset);
                internal = c;
            }
        }

        public void setCommands(Vector v) {
            if(currentCommands != null) {
                for(int iter = 0 ; iter < currentCommands.length ; iter++) {
                    removeCommand(currentCommands[iter]);
                }
            }
            setCommandListener(this);
            currentCommands = new Command[v.size()];
            com.sun.lwuit.Command backCommand = null;
            if(Display.getInstance().getCurrent() != null) {
                backCommand = Display.getInstance().getCurrent().getBackCommand();
            }
            for(int iter = 0 ; iter < currentCommands.length ; iter++) {
                com.sun.lwuit.Command current = (com.sun.lwuit.Command)v.elementAt(iter);
                if(current == backCommand) {
                    currentCommands[iter] = new MIDP2LWUITCommand(current, Command.BACK, iter + 1);
                } else {
                    if(iter == 0) {
                        currentCommands[iter] = new MIDP2LWUITCommand(current, Command.OK, iter + 1);
                    } else {
                        currentCommands[iter] = new MIDP2LWUITCommand(current, iter + 1);
                    }
                }
                addCommand(currentCommands[iter]);
            }
        }

        public void run() {
            while (!done) {
                synchronized (getDisplayLock()) {
                    try {
                        getDisplayLock().wait(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        public void setDone(boolean done) {
            this.done = done;
            synchronized (getDisplayLock()) {
                getDisplayLock().notify();
            }
        }

        public void commandAction(Command c, Displayable d) {
            if (d == currentTextBox) {
                display.setCurrent(canvas);
                if (c == CONFIRM_COMMAND) {
                    // confirm
                    String text = currentTextBox.getString();
                    getCurrentForm().setVisible(true);
                    Display.getInstance().onEditingComplete(currentTextComponent, text);
                }

                currentTextBox = null;
                ((C)canvas).setDone(true);
            } else {
                if(c instanceof MIDP2LWUITCommand) {
                    final com.sun.lwuit.Command cmd = ((MIDP2LWUITCommand)c).internal;
                    Display.getInstance().callSerially(new Runnable() {
                        public void run() {
                            Display.getInstance().getCurrent().dispatchCommand(cmd, new ActionEvent(cmd));
                        }
                    });
                }
            }
        }


        private javax.microedition.lcdui.Graphics gfx;

        C() {
            super(false);
        }

        public javax.microedition.lcdui.Graphics getGraphics() {

            if (gfx == null) {
                gfx = super.getGraphics();
            }
            return gfx;
        }

        protected void keyPressed(final int keyCode) {
            GameCanvasImplementation.this.keyPressed(keyCode);
        }

        protected void keyReleased(final int keyCode) {
            GameCanvasImplementation.this.keyReleased(keyCode);
        }

        protected void pointerDragged(final int x, final int y) {
            GameCanvasImplementation.this.pointerDragged(x, y);
        }

        protected void pointerPressed(final int x, final int y) {
            //solves an emulator bug(no impact on real devices)
            Display.getInstance().setTouchScreenDevice(true);
            GameCanvasImplementation.this.pointerPressed(x, y);
        }

        protected void pointerReleased(final int x, final int y) {
            GameCanvasImplementation.this.pointerReleased(x, y);
        }

        protected void sizeChanged(int w, int h) {
            GameCanvasImplementation.this.sizeChanged(w, h);
        }

        protected void hideNotify() {
            GameCanvasImplementation.this.hideNotify();
        }

        protected void showNotify() {
            GameCanvasImplementation.this.showNotify();
        }
        
        
    }
    private static final AlertType[] TYPES = new AlertType[]{
        AlertType.ALARM, AlertType.CONFIRMATION, AlertType.ERROR,
        AlertType.INFO, AlertType.WARNING
    };
    /**
     * The command used for accepting a text field change
     */
    static Command CONFIRM_COMMAND;

    /**
     * The end time of the last call to vibrate
     */
    private long lastVibrate;

    /**
     * The command used for canceling a text field change
     */
    static Command CANCEL_COMMAND;
    /**
     * The currently edited text box used to input into text field, this is a MIDP implementation
     * detail.
     */
    TextBox currentTextBox;
    /**
     * The currently edited text component that will be updated after the dismissal
     * of the text box
     */
    Component currentTextComponent;
    private boolean flushGraphicsBug;
    static javax.microedition.lcdui.Display display;
    /**
     * This member holds the left soft key value
     */
    static int[] leftSK = new int[]{-6};
    /**
     * This member holds the right soft key value
     */
    static int[] rightSK = new int[]{-7};
    /**
     * This member holds the back command key value
     */
    static int backSK = -11;
    /**
     * This member holds the clear command key value
     */
    static int clearSK = -8;
    static int backspaceSK = -8;
    /**
     * This flag indicates if the drawRGB method is able to draw negative x and y
     * In drawRGB method, some devices such as BlackBerry throw exceptions if you
     * try to give negative values to drawRGB method.
     */
    private static boolean drawNegativeOffsetsInRGB = true;

    /**
     * Allows a subclass to create its own canvas implemention
     * 
     * @return the canvas implementation
     */
    protected Canvas createCanvas() {
        return new C();
    }

    public GameCanvasImplementation() {
        // code should be in the init to allow assignment into implementation first
    }

    /**
     * @inheritDoc
     */
    public int getKeyboardType() {
        // See http://wiki.forum.nokia.com/index.php/How_to_utilize_different_keyboards_in_Java_ME for details on
        // Nokia keyboard types
        String keyboardType = System.getProperty("com.nokia.keyboard.type");
        if(keyboardType != null) {
            if("None".equalsIgnoreCase(keyboardType)) {
                if(isTouchDevice()) {
                    return Display.KEYBOARD_TYPE_VIRTUAL;
                }
                // annoying behavior of some phones where none is returned for numeric
                // see http://forums.java.net/jive/thread.jspa?messageID=400135
                return Display.KEYBOARD_TYPE_NUMERIC;
            }
            if("PhoneKeypad".equalsIgnoreCase(keyboardType)) {
                return Display.KEYBOARD_TYPE_NUMERIC;
            }
            if("HalfKeyboard".equalsIgnoreCase(keyboardType)) {
                return Display.KEYBOARD_TYPE_HALF_QWERTY;
            }
            if("FullKeyboard".equalsIgnoreCase(keyboardType)) {
                return Display.KEYBOARD_TYPE_QWERTY;
            }
            if("LimitedKeyboard4x10".equalsIgnoreCase(keyboardType)) {
                return Display.KEYBOARD_TYPE_QWERTY;
            }
            if("LimitedKeyboard3x11".equalsIgnoreCase(keyboardType)) {
                return Display.KEYBOARD_TYPE_QWERTY;
            }
        }
        return super.getKeyboardType();
    }

    /**
     * @inheritDoc
     */
    public void init(Object m) {
        canvas = createCanvas();
        canvas.setTitle(null);
        canvas.setFullScreenMode(!com.sun.lwuit.Display.getInstance().isNativeCommands());

        // disable the flashGraphics bug on Nokia phones
        String platform = System.getProperty("microedition.platform");
        if (platform != null && platform.toUpperCase().indexOf("NOKIA") >= 0) {
            flushGraphicsBug = false;
            NOKIA = true;

            // Symbian devices should yield a bit to let the paint thread complete its work
            // problem is we can't differentiate S40 from S60...
            Display.getInstance().setTransitionYield(1);
        } else {
            flushGraphicsBug = true;
            Display.getInstance().setTransitionYield(-1);
        }
        mid = (MIDlet)m;
        display = javax.microedition.lcdui.Display.getDisplay(mid);
        setSoftKeyCodes(mid);
    }

    private void setSoftKeyCodes(MIDlet m) {
        // initialy set known key codes
        setKnownSoftKeyCodes();

        try {
            // if the back key is assigned to a game action by mistake then
            // workaround it implicitly
            int game = getGameAction(backSK);
            if (game == GameCanvas.UP || game == GameCanvas.DOWN || game == GameCanvas.RIGHT ||
                    game == GameCanvas.LEFT || game == GameCanvas.FIRE) {
                backSK = -50000;
            }

        } catch (Exception ok) {
        }

        try {
            // if the clear key is assigned to a game action by mistake then
            // workaround it implicitly
            int game = getGameAction(clearSK);
            if (game == GameCanvas.UP || game == GameCanvas.DOWN || game == GameCanvas.RIGHT ||
                    game == GameCanvas.LEFT || game == GameCanvas.FIRE) {
                clearSK = -50000;
            }

            game = getGameAction(backspaceSK);
            if (game == GameCanvas.UP || game == GameCanvas.DOWN || game == GameCanvas.RIGHT ||
                    game == GameCanvas.LEFT || game == GameCanvas.FIRE) {
                backspaceSK = -50000;
            }

        } catch (Exception ok) {
        }

        // Then Check JAD file for overide specific key mapping
        String tmpSoftKey = m.getAppProperty("SoftKey-Right");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            rightSK[0] = Integer.valueOf(tmpSoftKey).intValue();
        }

        tmpSoftKey = m.getAppProperty("SoftKey-Right2");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            rightSK = new int[]{rightSK[0], Integer.valueOf(tmpSoftKey).intValue()};
        }

        tmpSoftKey = m.getAppProperty("SoftKey-Left");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            leftSK[0] = Integer.valueOf(tmpSoftKey).intValue();
        }

        tmpSoftKey = m.getAppProperty("SoftKey-Back");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            backSK = Integer.valueOf(tmpSoftKey).intValue();
        }

        tmpSoftKey = m.getAppProperty("SoftKey-Clear");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            clearSK = Integer.valueOf(tmpSoftKey).intValue();
        }

        tmpSoftKey = m.getAppProperty("SoftKey-Backspace");
        if (tmpSoftKey != null && !"".equals(tmpSoftKey)) {
            backspaceSK = Integer.valueOf(tmpSoftKey).intValue();
        }
// Check Third Soft Button is supported        

        tmpSoftKey = m.getAppProperty("isThirdSoftButtonSupported");
        if (tmpSoftKey != null && "true".equals(tmpSoftKey.toLowerCase().trim())) {
            Display.getInstance().setThirdSoftButton(true);
        }

    }

    private void setKnownSoftKeyCodes() {
        try {
            Class.forName("com.siemens.mp.game.Light");
            leftSK[0] = -1;
            rightSK[0] = -4;
            clearSK = -12;
            backspaceSK = -12;
            return;
        } catch (ClassNotFoundException _ex) {
        }
        try {
            Class.forName("com.motorola.phonebook.PhoneBookRecord");
            leftSK[0] = -21;
            rightSK[0] = -22;
            return;
        //maybe Motorola A1000 has different keyCodes
        //Left soft key: Key code -10,
        //Right soft key: Key code -11,
        } catch (ClassNotFoundException _ex) {
        }
        try {
            Class.forName("com.nokia.mid.ui.FullCanvas");
            leftSK[0] = -6;
            rightSK[0] = -7;

            clearSK = -8;
            backspaceSK = -8;

            // prevent this from breaking Sony Ericsson devices
            String p = System.getProperty("microedition.platform");
            if (p != null && p.toUpperCase().indexOf("NOKIA") >= 0) {
                backspaceSK = 8;
            }
            return;
        } catch (Throwable ex) {
        }
        try {
            Class.forName("net.rim.device.api.system.Application");
            //there are no soft keys on the Blackberry
            //instead use the Q and P keys
            leftSK[0] = 113;
            //some BB devices use O as the most right key
            rightSK = new int[]{112, 111};
            clearSK = 8;
            backspaceSK = 8;
            return;
        } catch (ClassNotFoundException ex) {
        }
        // detecting LG  
        try {
            // iden device
            Class.forName("com.mot.iden.util.Base64");
            clearSK = -5000;
            backspaceSK = -5000;
            leftSK[0] = -20;
            rightSK[0] = -21;
            setFireValue(-23);
            return;

        } catch (Throwable ex) {
        }
        try {
            Class.forName("mmpp.media.MediaPlayer");
            clearSK = -204;
            backspaceSK = -204;
        } catch (ClassNotFoundException ex) {
        }
        try {
            if (canvas.getKeyName(-6).toUpperCase().indexOf("SOFT") >= 0) {
                leftSK[0] = -6;
                rightSK[0] = -7;
                return;
            }

            if (canvas.getKeyName(21).toUpperCase().indexOf("SOFT") >= 0) {
                leftSK[0] = 21;
                rightSK[0] = 22;
                return;
            }
        } catch (Exception ex) {
        }
        boolean leftInit = false;
        boolean rightInit = false;
        for (int i = -127; i < 127; i++) {
            if (leftInit && rightInit) {  //I have added this if to avoid unnecessary loops
                return; //but the main reason is that sometimes after the correct negative values were initialized also positive
            }          // keycodes had "soft" in the name.

            try {
                if (canvas.getKeyName(i).toUpperCase().indexOf("SOFT") < 0) {
                    continue;
                }
                if (canvas.getKeyName(i).indexOf("1") >= 0) {
                    leftSK[0] = i;
                    leftInit = true;
                }

                if (canvas.getKeyName(i).indexOf("2") >= 0) {
                    rightSK[0] = i;
                    rightInit = true;
                }
                continue;
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Some devices (iden) map the fire key incorrectly, this method allows
     * us to add another button as fire for specific devices.
     * 
     * @param key keyCode to react as fire
     */
    void setFireValue(int key) {
        try {
            getKeyCode(0);
        } catch (Exception ignor) {
        }
        portableKeyCodeValues[4] = key;
    }

    /**
     * @inheritDoc
     */
    public void vibrate(final int duration) {
        // prevent double calls to vibrate since some devices (e.g. Samsung 6503)
        // crash when we do that
        long now = System.currentTimeMillis();
        if(now > lastVibrate) {
            lastVibrate = now + (duration * 3);
            display.vibrate(duration);
        }
    }

    /**
     * @inheritDoc
     */
    public void flashBacklight(int duration) {
        display.flashBacklight(duration);
    }

    /**
     * @inheritDoc
     */
    public int getDisplayWidth() {
        return canvas.getWidth();
    }

    /**
     * @inheritDoc
     */
    public int getDisplayHeight() {
        return canvas.getHeight();
    }

    /**
     * @inheritDoc
     */
    public void editString(Component cmp, int maxSize, int constraint, String text, int keyCode) {
        UIManager m = UIManager.getInstance();
        CONFIRM_COMMAND = new Command(m.localize("ok", "OK"), Command.OK, 1);
        CANCEL_COMMAND = new Command(m.localize("cancel", "Cancel"), Command.CANCEL, 2);
        currentTextBox = new TextBox("", "", maxSize, TextArea.ANY);
        currentTextBox.setCommandListener((CommandListener)canvas);
        currentTextBox.addCommand(CONFIRM_COMMAND);
        currentTextBox.addCommand(CANCEL_COMMAND);
        currentTextComponent = cmp;
        currentTextBox.setMaxSize(maxSize);
        currentTextBox.setString(text);
        currentTextBox.setConstraints(constraint);
        display.setCurrent(currentTextBox);
        ((C)canvas).setDone(false);
        Display.getInstance().invokeAndBlock(((C)canvas));
    }

    /**
     * @inheritDoc
     */
    public void saveTextEditingState() {
        if(currentTextBox != null) {
            String text = currentTextBox.getString();
            getCurrentForm().setVisible(true);
            Display.getInstance().onEditingComplete(currentTextComponent, text);
            currentTextBox = null;
            ((C)canvas).setDone(true);
        }
    }

    /**
     * Indicate to the implementation whether the flush graphics bug exists on this
     * device. By default the flushGraphics bug is set to "true" and only disabled
     * on handsets known 100% to be safe
     * 
     * @param flushGraphicsBug true if the bug exists on this device (the safe choice)
     * false for slightly higher performance.
     */
    public void setFlashGraphicsBug(boolean flushGraphicsBug) {
        this.flushGraphicsBug = flushGraphicsBug;
    }

    /**
     * Allows subclasses to access the canvas
     * 
     * @returns the canvas instance
     */
    protected final Canvas getCanvas() {
        return canvas;
    }

    /**
     * @inheritDoc
     */
    public void flushGraphics(int x, int y, int width, int height) {
        // disable flush graphics bug when media is playing to prevent the double buffer
        // from clearing the media and producing flickering
        Form current = getCurrentForm();
        if (!flushGraphicsBug || (current != null && current.hasMedia())) {
            ((C) canvas).flushGraphics(x, y, width, height);
        } else {
            ((C) canvas).flushGraphics();
        }

    }

    /**
     * @inheritDoc
     */
    public void flushGraphics() {
        ((C) canvas).flushGraphics();
    }

    /**
     * @inheritDoc
     */
    public void getRGB(Object nativeImage, int[] arr, int offset, int x, int y, int width, int height) {
        ((javax.microedition.lcdui.Image) nativeImage).getRGB(arr, offset, width, x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public Object createImage(
            int[] rgb, int width, int height) {
        return javax.microedition.lcdui.Image.createRGBImage(rgb, width, height, true);
    }

    /**
     * @inheritDoc
     */
    public Object createImage(
            String path) throws IOException {
        return javax.microedition.lcdui.Image.createImage(path);
    }

    /**
     * @inheritDoc
     */
    public Object createImage(
            InputStream i) throws IOException {
        return javax.microedition.lcdui.Image.createImage(i);
    }

    /**
     * @inheritDoc
     */
    public Object createMutableImage(
            int width, int height, int fillColor) {
        javax.microedition.lcdui.Image i = javax.microedition.lcdui.Image.createImage(width, height);
        if (fillColor != 0xffffffff) {
            javax.microedition.lcdui.Graphics g = i.getGraphics();
            g.setColor(fillColor);
            g.fillRect(0, 0, width, height);
            g.drawRect(0, 0, width-1, height-1);
        }
        return i;
    }

    /**
     * @inheritDoc
     */
    public Object createImage(
            byte[] bytes, int offset, int len) {
        return javax.microedition.lcdui.Image.createImage(bytes, offset, len);
    }

    /**
     * @inheritDoc
     */
    public int getImageWidth(Object i) {
        return ((javax.microedition.lcdui.Image) i).getWidth();
    }

    /**
     * @inheritDoc
     */
    public int getImageHeight(Object i) {
        return ((javax.microedition.lcdui.Image) i).getHeight();
    }

    /**
     * @inheritDoc
     */
    public Object scale(
            Object nativeImage, int width, int height) {
        javax.microedition.lcdui.Image image = (javax.microedition.lcdui.Image) nativeImage;
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        // no need to scale
        if (srcWidth == width && srcHeight == height) {
            return image;
        }

        int[] currentArray = new int[srcWidth];
        int[] destinationArray = new int[width * height];
        scaleArray(image, srcWidth, srcHeight, height, width, currentArray, destinationArray);

        return createImage(destinationArray, width, height);
    }

    private void scaleArray(javax.microedition.lcdui.Image currentImage, int srcWidth, int srcHeight, int height, int width, int[] currentArray, int[] destinationArray) {
        // Horizontal Resize
        int yRatio = (srcHeight << 16) / height;
        int xRatio = (srcWidth << 16) / width;
        int xPos = xRatio / 2;
        int yPos = yRatio / 2;

        // if there is more than 16bit color there is no point in using mutable
        // images since they won't save any memory
        for (int y = 0; y <
                height; y++) {
            int srcY = yPos >> 16;
            getRGB(currentImage, currentArray, 0, 0, srcY, srcWidth, 1);
            for (int x = 0; x <
                    width; x++) {
                int srcX = xPos >> 16;
                int destPixel = x + y * width;
                if ((destPixel >= 0 && destPixel < destinationArray.length) && (srcX < currentArray.length)) {
                    destinationArray[destPixel] = currentArray[srcX];
                }

                xPos += xRatio;
            }

            yPos += yRatio;
            xPos =
                    xRatio / 2;
        }

    }

    /**
     * @inheritDoc
     */
    public void drawImageRotated(Object graphics, Object img, int x, int y, int degrees) {
        javax.microedition.lcdui.Image i = (javax.microedition.lcdui.Image) img;
        int t;
        switch (degrees % 360) {
            case 0:
                drawImage(graphics, img, x, y);
                return;

            case 90:
                t = Sprite.TRANS_ROT90;
                break;

            case 180:
                t = Sprite.TRANS_ROT180;
                break;

            case 270:
                t = Sprite.TRANS_ROT270;
                break;

            default:
                throw new IllegalArgumentException("Unsupported value for drawImageRotated: " + degrees);
        }

        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawRegion(i, 0, 0, i.getWidth(),
                i.getHeight(), t, x, y,
                javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
    }

    /**
     * @inheritDoc
     */
    public boolean isRotationDrawingSupported() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public int getSoftkeyCount() {
        return 2;
    }

    /**
     * @inheritDoc
     */
    public int[] getSoftkeyCode(int index) {
        if (index == 0) {
            return leftSK;
        }

        if (index == 1) {
            return rightSK;
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    public int getClearKeyCode() {
        return clearSK;
    }

    /**
     * @inheritDoc
     */
    public int getBackspaceKeyCode() {
        return backspaceSK;
    }

    /**
     * @inheritDoc
     */
    public int getBackKeyCode() {
        return backSK;
    }

    /**
     * @inheritDoc
     */
    public int getGameAction(int keyCode) {
        try {
            // prevent game actions from being returned by numeric keypad thus screwing up
            // keypad based navigation and text input
            if (keyCode >= '0') {
                return 0;
            }

            if (portableKeyCodes != null) {
                for (int iter = 0; iter <
                        portableKeyCodeValues.length; iter++) {
                    if (portableKeyCodeValues[iter] == keyCode) {
                        return portableKeyCodes[iter];
                    }

                }
            }

            return canvas.getGameAction(keyCode);
        } catch (IllegalArgumentException err) {
            // this is a stupid MIDP requirement some implementations throw this
            // exception for some keys
            return 0;
        }

    }

    /**
     * @inheritDoc
     */
    public int getKeyCode(int gameAction) {
        if (portableKeyCodes == null) {
            portableKeyCodes = new int[]{Display.GAME_DOWN, Display.GAME_LEFT, Display.GAME_RIGHT, Display.GAME_UP, Display.GAME_FIRE};
            portableKeyCodeValues = new int[5];
            int currentValue = -500;
            int offset = 0;
            while (offset < portableKeyCodeValues.length) {
                currentValue--;
                try {
                    if (canvas.getGameAction(currentValue) != 0) {
                        continue;
                    }

                } catch (IllegalArgumentException ignor) {
                    // this is good, the game key is unassigned
                }
                portableKeyCodeValues[offset] = currentValue;
                offset++;

            }


        }
        for (int iter = 0; iter <
                portableKeyCodes.length; iter++) {
            if (portableKeyCodes[iter] == gameAction) {
                return portableKeyCodeValues[iter];
            }

        }
        return 0;
    }

    /**
     * @inheritDoc
     */
    public boolean isTouchDevice() {
        return canvas.hasPointerEvents();
    }

    /**
     * @inheritDoc
     */
    public void setNativeFont(Object graphics, Object font) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.setFont(font(font));
    }

    /**
     * @inheritDoc
     */
    public int getClipX(Object graphics) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        return nativeGraphics.getClipX();
    }

    /**
     * @inheritDoc
     */
    public int getClipY(Object graphics) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        return nativeGraphics.getClipY();

    }

    /**
     * @inheritDoc
     */
    public int getClipWidth(Object graphics) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        return nativeGraphics.getClipWidth();
    }

    /**
     * @inheritDoc
     */
    public int getClipHeight(Object graphics) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        return nativeGraphics.getClipHeight();
    }

    /**
     * @inheritDoc
     */
    public void setClip(Object graphics, int x, int y, int width, int height) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.setClip(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void clipRect(Object graphics, int x, int y, int width, int height) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.clipRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawLine(Object graphics, int x1, int y1, int x2, int y2) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawLine(x1, y1, x2, y2);
    }

    /**
     * @inheritDoc
     */
    public void fillRect(Object graphics, int x, int y, int w, int h) {
        if (isAlphaGlobal()) {
            javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
            nativeGraphics.fillRect(x, y, w, h);
            nativeGraphics.drawRect(x, y, w-1, h-1);
            return;

        }


        if (alpha == 0) {
            return;
        }

        if (alpha == 0xff) {
            javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
            nativeGraphics.fillRect(x, y, w, h);
            nativeGraphics.drawRect(x, y, w-1, h-1);
        } else {
            int transparencyLevel = alpha << 24;
            int color = (getColor(graphics) & 0x00FFFFFF);
            color = (color | transparencyLevel);


            if (rgbArr == null || rgbArr.length < w) {
                rgbArr = new int[w];
            }

            for (int i = 0; i <
                    w; i++) {
                rgbArr[i] = color;
            }

            int rgbX = x;
            int rgbY = y;


            if (rgbX < 0 && rgbX + w > 0) {
                w = rgbX + w;
                rgbX = 0;
            }

            if (w < 0) {
                return;
            }

            int clipY = getClipY(graphics);
            int clipHeight = getClipHeight(graphics);
            int clipBottomY = clipHeight + clipY;

            for (int i = 0; i < h; i++) {
                if (rgbX >= 0 && rgbY + i >= 0) {
                    int currentY = rgbY + i;
                    if(currentY >= clipY && currentY <= clipBottomY) {
                        drawRGB(graphics, rgbArr, 0, rgbX, currentY, w, 1, true);
                    }
                }

            }

        }
    }

    /**
     * @inheritDoc
     */
    public void drawRect(Object graphics, int x, int y, int width, int height) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void drawArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void setColor(Object graphics, int RGB) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.setColor(RGB);
    }

    /**
     * @inheritDoc
     */
    public int getColor(Object graphics) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        return nativeGraphics.getColor();
    }

    /**
     * @inheritDoc
     */
    public void setAlpha(Object graphics, int alpha) {
        this.alpha = alpha;
    }

    /**
     * @inheritDoc
     */
    public int getAlpha(Object graphics) {
        return alpha;
    }

    /**
     * @inheritDoc
     */
    public void drawString(Object graphics, String str, int x, int y) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawString(str, x, y, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
    }

    /**
     * @inheritDoc
     */
    public void drawImage(Object graphics, Object img, int x, int y) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.drawImage((javax.microedition.lcdui.Image) img, x, y, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
    }

    /**
     * @inheritDoc
     */
    public void fillTriangle(Object graphics, int x1, int y1, int x2, int y2, int x3, int y3) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        nativeGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
    }

    /**
     * @inheritDoc
     */
    public void drawRGB(Object graphics, int[] rgbData, int offset, int x, int y, int w, int h, boolean processAlpha) {
        javax.microedition.lcdui.Graphics nativeGraphics = (javax.microedition.lcdui.Graphics) graphics;
        int rgbX = x;
        int rgbY = y;

        //if the x or y are positive simply redirect the call to midp Graphics
        if (rgbX >= 0 && rgbY >= 0) {
            nativeGraphics.drawRGB(rgbData, offset, w, rgbX, rgbY, w, h, processAlpha);
            return;

        }

        //first time try to draw with negative indexes

        if (drawNegativeOffsetsInRGB) {
            try {
                nativeGraphics.drawRGB(rgbData, offset, w, rgbX, rgbY, w, h, processAlpha);
                return;

            } catch (RuntimeException e) {
                //if you failed it might be because you tried to paint with negative
                //indexes
                drawNegativeOffsetsInRGB = false;
            }

        }

        //if the translate causes us to paint out of the bounds
        //we will paint only the relevant rows row by row to avoid some devices bugs
        //such as BB that fails to paint if the coordinates are negative.
        if (rgbX < 0 && rgbX + w > 0) {
            if (w < rgbData.length) {
                for (int i = 1; i <=
                        rgbData.length / w; i++) {
                    offset = -rgbX + (w * (i - 1));
                    rgbY++;

                    if (rgbY >= 0) {
                        nativeGraphics.drawRGB(rgbData, offset, (w + rgbX), 0, rgbY, w + rgbX, 1, processAlpha);
                    }

                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    public Object getVideoControl(
            Object player) {
        VideoControl vidc = (VideoControl) ((Player) player).getControl("VideoControl");
        vidc.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
        return vidc;
    }

    /**
     * @inheritDoc
     */
    public int numAlphaLevels() {
        return display.numAlphaLevels();
    }

    /**
     * @inheritDoc
     */
    public int numColors() {
        return display.numColors();
    }

    /**
     * @inheritDoc
     */
    public void playDialogSound(int type) {
        type--;
        if(type >= 0 && type < TYPES.length) {
            TYPES[type].playSound(display);
        }
    }

    /**
     * @inheritDoc
     */
    public void confirmControlView() {
        if (display == null) {
            throw new IllegalStateException("First call Display.setDisplay(javax.microedition.lcdui.Display d) method");
        }

        if (display.getCurrent() != canvas || !canvas.isShown()) {
            setCurrent(canvas);
        }

    }

    /**
     * @inheritDoc
     */
    private void setCurrent(Displayable d) {
        if (display == null) {
            throw new IllegalStateException("First call Display.setDisplay(javax.microedition.lcdui.Display d) method");
        }

        if(!minimized) {
            if (d instanceof Canvas) {
                ((Canvas) d).setFullScreenMode(!com.sun.lwuit.Display.getInstance().isNativeCommands());
            }

            display.setCurrent(d);
        }
    }

    /**
     * @inheritDoc
     */
    public Object getNativeGraphics() {
        return ((C) canvas).getGraphics();
    }

    /**
     * @inheritDoc
     */
    public Object getNativeGraphics(
            Object image) {
        return ((javax.microedition.lcdui.Image) image).getGraphics();
    }

    /**
     * @inheritDoc
     */
    public void translate(Object graphics, int x, int y) {
        // does nothing, we expect translate to occur in the graphics for
        // better device portability
    }

    /**
     * @inheritDoc
     */
    public int getTranslateX(Object graphics) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    public int getTranslateY(Object graphics) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    public int charsWidth(Object nativeFont, char[] ch, int offset, int length) {
        if(NOKIA) {
            // charsWidth is MUCH MUCH MUCH slower on S40 devices than stringWidth.
            return font(nativeFont).stringWidth(new String(ch, offset, length));
        }
        return font(nativeFont).charsWidth(ch, offset, length);
    }

    /**
     * @inheritDoc
     */
    public int stringWidth(Object nativeFont, String str) {
        return font(nativeFont).stringWidth(str);
    }

    /**
     * @inheritDoc
     */
    public int charWidth(Object nativeFont, char ch) {
        return font(nativeFont).charWidth(ch);
    }

    /**
     * @inheritDoc
     */
    public int getHeight(Object nativeFont) {
        return font(nativeFont).getHeight();
    }

    /**
     * @inheritDoc
     */
    public Object createFont(
            int face, int style, int size) {
        return javax.microedition.lcdui.Font.getFont(face, style, size);
    }

    /**
     * @inheritDoc
     */
    public Object getDefaultFont() {
        return javax.microedition.lcdui.Font.getDefaultFont();
    }

    /**
     * @inheritDoc
     */
    public int getFace(Object nativeFont) {
        return font(nativeFont).getFace();
    }

    /**
     * @inheritDoc
     */
    public int getSize(Object nativeFont) {
        return font(nativeFont).getSize();
    }

    /**
     * @inheritDoc
     */
    public int getStyle(Object nativeFont) {
        return font(nativeFont).getStyle();
    }

    private javax.microedition.lcdui.Font font(Object f) {
        if (f == null) {
            return (javax.microedition.lcdui.Font) getDefaultFont();
        }

        return (javax.microedition.lcdui.Font) f;
    }

    /**
     * @inheritDoc
     */
    public VideoComponent createVideoPeer(String url) throws IOException {
        try {
            Player p = Manager.createPlayer(url);
            p.realize();
            return new MIDPVideoComponent(p);
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    /**
     * @inheritDoc
     */
    public VideoComponent createVideoPeer(InputStream stream, String type) throws IOException {
        try {
            Player p = Manager.createPlayer(stream, type);
            p.realize();
            return new MIDPVideoComponent(p);
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    class MIDPVideoComponent extends VideoComponent {
        private boolean fullscreen;
        
        MIDPVideoComponent(Player p) {
            super(p);
            putClientProperty("Player", p);
        }

        public int getMediaDuration() {
            return (int)(((Player) getNativePeer()).getDuration() / 1000);
        }

        public boolean isPlaying() {
            return ((Player) getNativePeer()).getState() == Player.STARTED;
        }

        public void setVisible(boolean b) {
            super.setVisible(b);
            getVideoControl(this).setVisible(b);
        }

        /**
         * @inheritDoc
         */
        public void paint(com.sun.lwuit.Graphics g) {
            if(isVisible()){
                try {
                    VideoControl vidc = (VideoControl) getVideoControl(this);
                    if (isFullScreen()) {
                        vidc.setDisplayLocation(0, 0);
                        vidc.setDisplaySize(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
                    } else {
                        vidc.setDisplayLocation(getAbsoluteX(), getAbsoluteY());
                        int w = getWidth();
                        int h = getHeight();
                        if (vidc.getDisplayWidth() != w || vidc.getDisplayHeight() != h) {
                            vidc.setDisplaySize(w, h);
                        }
                    }
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }
            }
        }


        /**
         * @inheritDoc
         */
        protected Dimension calcPreferredSize() {
            VideoControl v = getVideoControl(this);
            return new Dimension(v.getDisplayWidth(), v.getDisplayHeight());
        }

        /**
         * Start media playback implicitly setting the component to visible
         */
        public void start() {
            try {
                getVideoControl(this).setVisible(true);
                ((Player) getNativePeer()).start();
            } catch (MediaException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.toString());
            }
        }

        /**
         * Stope media playback
         */
        public void stop() {
            try {
                ((Player) getNativePeer()).stop();
            } catch (MediaException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.toString());
            }
        }

        /**
         * Set the number of times the media should loop
         *
         * @param count the number of times the media should loop
         */
        public void setLoopCount(int count) {
            ((Player) getNativePeer()).setLoopCount(count);
        }

        /**
         * Return the duration of the media
         *
         * @return the duration of the media
         */
        public int getMediaTimeMS() {
            return (int)(((Player) getNativePeer()).getMediaTime() / 1000);
        }

        /**
         * "Jump" to a point in time within the media
         *
         * @param now the point in time to "Jump" to
         * @return the media time in microseconds
         */
        public int setMediaTimeMS(int now) {
            try {
                return (int)(((Player) getNativePeer()).setMediaTime(now * 1000) / 1000);
            } catch (MediaException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.toString());
            }
        }

        public void setFullScreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            repaint();
        }

        public boolean isFullScreen() {
            return fullscreen;
        }
    }

    private VideoControl getVideoControl(VideoComponent c) {
        VideoControl vidc = (VideoControl)c.getClientProperty("VideoControl");
        if(vidc != null) {
            return vidc;
        }
        vidc = (VideoControl) ((Player) c.getNativePeer()).getControl("VideoControl");
        vidc.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
        c.putClientProperty("VideoControl", vidc);
        return vidc;

    }


    /**
     * @inheritDoc
     */
    public Object createVideoComponent(
            Object player) {
        if (((Player) player).getState() < Player.REALIZED) {
            throw new IllegalArgumentException("player must be in a realized state");
        }

        return getVideoControl(player);
    }

    /**
     * @inheritDoc
     */
    public int getVideoWidth(Object videoControl) {
        return ((VideoControl) videoControl).getSourceWidth();
    }

    /**
     * @inheritDoc
     */
    public int getVideoHeight(Object videoControl) {
        return ((VideoControl) videoControl).getSourceHeight();
    }

    /**
     * @inheritDoc
     */
    public void setVideoVisible(Object vc, boolean visible) {
        ((VideoControl) vc).setVisible(visible);
    }

    /**
     * @inheritDoc
     */
    public void startVideo(Object player, Object videoControl) {
        try {
            ((VideoControl) videoControl).setVisible(true);
            ((Player) player).start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @inheritDoc
     */
    public void stopVideo(Object player, Object videoControl) {
        try {
            ((Player) player).stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @inheritDoc
     */
    public void setVideoLoopCount(Object player, int count) {
        ((Player) player).setLoopCount(count);
    }

    /**
     * @inheritDoc
     */
    public long getMediaTime(Object player) {
        return ((Player) player).getMediaTime();
    }

    /**
     * @inheritDoc
     */
    public long setMediaTime(Object player, long now) {
        try {
            return ((Player) player).setMediaTime(now);
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }

    }

    /**
     * @inheritDoc
     */
    public void paintVideo(Component cmp, boolean fullScreen, Object nativeGraphics, Object video,
            Object player) {
        try {
            VideoControl vidc = (VideoControl) video;
            if (fullScreen) {
                vidc.setDisplayLocation(0, 0);
                vidc.setDisplaySize(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
            } else {
                vidc.setDisplayLocation(cmp.getAbsoluteX(), cmp.getAbsoluteY());
                int w = cmp.getWidth();
                int h = cmp.getHeight();
                if (vidc.getDisplayWidth() != w || vidc.getDisplayHeight() != h) {
                    vidc.setDisplaySize(w, h);
                }

            }
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public boolean minimizeApplication() {
        try {
            minimized = true;
            display.setCurrent(null);
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    public void restoreMinimizedApplication() {
        try {
            minimized = false;
            display.setCurrent(canvas);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public boolean isMinimized() {
        return minimized;
    }

    /**
     * @inheritDoc
     */
    public void showNativeScreen(Object nativeFullScreenPeer) {
        display.setCurrent((Displayable)nativeFullScreenPeer);
    }


    /**
     * @inheritDoc
     */
    public void setNativeCommands(Vector commands) {
        canvas.setFullScreenMode(!com.sun.lwuit.Display.getInstance().isNativeCommands());
        ((C)canvas).setCommands(commands);
    }


    /**
     * Exits the application...
     */
    public void exitApplication() {
        mid.notifyDestroyed();
    }

    /**
     * @inheritDoc
     */
    public String getProperty(String key, String defaultValue) {
        if("AppName".equals(key)) {
            return mid.getAppProperty("MIDlet-Name");
        }
        if("AppVersion".equals(key)) {
            return mid.getAppProperty("MIDlet-Version");
        }
        if("Platform".equals(key)) {
            return System.getProperty("microedition.platform");
        }
        if("OS".equals(key)) {
            return "J2ME";
        }

        if ("IMEI".equals(key)) {
            String imei = null;
            imei = System.getProperty("phone.imei");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.nokia.IMEI");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.nokia.mid.imei");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.sonyericsson.imei");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("IMEI");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.motorola.IMEI");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.samsung.imei");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.siemens.imei");
            if(imei != null){
                return imei;
            }
            imei = System.getProperty("com.lge.imei");
            if(imei != null){
                return imei;
            }
        }

        String s = mid.getAppProperty(key);
        if(s == null) {
            return defaultValue;
        }
        return s;
    }

    /**
     * @inheritDoc
     */
    public void execute(String url) {
        try {
            mid.platformRequest(url);
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public void playBuiltinSound(String soundIdentifier) {
        if(!playUserSound(soundIdentifier)) {
            if(soundIdentifier.equals(Display.SOUND_TYPE_ALARM)) {
                AlertType.ALARM.playSound(display);
                return;
            }
            if(soundIdentifier.equals(Display.SOUND_TYPE_CONFIRMATION)) {
                AlertType.CONFIRMATION.playSound(display);
                return;
            }
            if(soundIdentifier.equals(Display.SOUND_TYPE_ERROR)) {
                AlertType.ERROR.playSound(display);
                return;
            }
            if(soundIdentifier.equals(Display.SOUND_TYPE_INFO)) {
                AlertType.INFO.playSound(display);
                return;
            }
            if(soundIdentifier.equals(Display.SOUND_TYPE_WARNING)) {
                AlertType.WARNING.playSound(display);
                return;
            }
        }
    }

    /**
     * @inheritDoc
     */
    protected void playNativeBuiltinSound(Object data) {
        try {
            try {
                Object o = createAudio(new ByteArrayInputStream((byte[]) data), "audio/mpeg", null);
                playAudio(o);
            } catch(Exception err) {
                // some simulators take issue with the audio/mpeg string but the mp3 string
                // works fine
                Object o = createAudio(new ByteArrayInputStream((byte[]) data), "audio/mp3", null);
                playAudio(o);
            }
        } catch (IOException ex) {
            // not likely since the stream is a byte array input stream
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public boolean isBuiltinSoundAvailable(String soundIdentifier) {
        if(soundIdentifier.equals(Display.SOUND_TYPE_ALARM)) {
            return true;
        }
        if(soundIdentifier.equals(Display.SOUND_TYPE_CONFIRMATION)) {
            return true;
        }
        if(soundIdentifier.equals(Display.SOUND_TYPE_ERROR)) {
            return true;
        }
        if(soundIdentifier.equals(Display.SOUND_TYPE_INFO)) {
            return true;
        }
        if(soundIdentifier.equals(Display.SOUND_TYPE_WARNING)) {
            return true;
        }
        return super.isBuiltinSoundAvailable(soundIdentifier);
    }

    /**
     * @inheritDoc
     */
    public Object createAudio(String uri, Runnable onCompletion) throws IOException {
        return MMAPIPlayer.createAudio(uri, onCompletion);
    }

    /**
     * @inheritDoc
     */
    public Object createAudio(InputStream stream, String mimeType, Runnable onCompletion) throws IOException {
        return MMAPIPlayer.createAudio(stream, mimeType, onCompletion);
    }

    /**
     * @inheritDoc
     */
    public void cleanupAudio(Object handle) {
        ((MMAPIPlayer)handle).cleanupAudio();
    }

    /**
     * @inheritDoc
     */
    public void playAudio(Object handle) {
        ((MMAPIPlayer)handle).playAudio();
    }

    /**
     * @inheritDoc
     */
    public void pauseAudio(Object handle) {
        ((MMAPIPlayer)handle).pauseAudio();
    }

    /**
     * @inheritDoc
     */
    public int getAudioTime(Object handle) {
        return ((MMAPIPlayer)handle).getAudioTime();
    }

    /**
     * @inheritDoc
     */
    public void setAudioTime(Object handle, int time) {
        ((MMAPIPlayer)handle).setAudioTime(time);
    }

    /**
     * @inheritDoc
     */
    public int getAudioDuration(Object handle) {
        return ((MMAPIPlayer)handle).getAudioDuration();
    }

    /**
     * @inheritDoc
     */
    public void setVolume(int vol) {
        MMAPIPlayer.setVolume(vol);
    }

    /**
     * @inheritDoc
     */
    public int getVolume() {
        return MMAPIPlayer.getVolume();
    }
}
