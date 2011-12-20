/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.lwuit.impl.blackberry;

import com.sun.lwuit.BrowserComponent;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.PeerComponent;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.VideoComponent;
import com.sun.lwuit.animations.Animation;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.impl.LWUITImplementation;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.EventDispatcher;
import com.sun.lwuit.util.Resources;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import javax.microedition.midlet.MIDlet;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
//#ifdef signed
//# import net.rim.device.api.system.CodeModuleGroup;
//# import net.rim.device.api.system.CodeModuleGroupManager;
//# import net.rim.blackberry.api.phone.Phone;
//# import net.rim.device.api.browser.field2.BrowserField;
//# import net.rim.device.api.browser.field2.BrowserFieldListener;
//# import net.rim.device.api.script.Scriptable;
//#endif
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.ActiveAutoTextEditField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
//#ifdef touch
//# import net.rim.device.api.system.Branding;
//# import org.w3c.dom.Document;
//#endif
/**
 * The implementation of the blackberry platform delegates the work to the underlying UI
 * application to allow for deep BB integration
 *
 * @author Shai Almog, Thorsten Schemm
 */
public class BlackBerryImplementation extends LWUITImplementation {
    static Hashtable fieldComponentMap = new Hashtable();
    static final int INVOKE_LATER_confirmControlView = 1;
    static final int INVOKE_LATER_finishEdit = 2;
    static final int INVOKE_LATER_initComponent = 3;
    static final int INVOKE_LATER_deinitialize = 4;
    static final int INVOKE_LATER_showNativeScreen = 5;
    static final int INVOKE_AND_WAIT_calcPreferredSize = 6;
    static final int INVOKE_AND_WAIT_setFocus = 7;
    static final int INVOKE_LATER_dirty = 8;
    static final int INVOKE_LATER_startMedia = 9;
    private static boolean minimizeOnEnd = true;

    // blackberry sometimes "breaks" the drawing color... No idea why this might happen...
    private int color;
    static final int MENU_SOFT_KEY = Keypad.KEY_MENU;
    static final int GAME_KEY_CODE_FIRE = -90;
    static final int GAME_KEY_CODE_UP = -91;
    static final int GAME_KEY_CODE_DOWN = -92;
    static final int GAME_KEY_CODE_LEFT = -93;
    static final int GAME_KEY_CODE_RIGHT = -94;
    BasicEditField nativeEdit;
    TextArea lightweightEdit;
    private BlackBerryCanvas canvas = createCanvas();
    private UiApplication app;
    private MIDlet midletInstance;
    static boolean midlet;
    private boolean initGetProperty = true;
    //#ifdef signed
//#     private static CodeModuleGroup group;
    //#endif
    private static EventDispatcher volumeListener;
    private NullField nullFld;

    BlackBerryCanvas createCanvas() {
        return new BlackBerryCanvas(this);
    }

    public void init(Object m) {
        if (m instanceof UiApplication) {
            app = (UiApplication) m;
        } else {
            if(m instanceof MIDlet) {
                midlet = true;
                midletInstance = (MIDlet)m;
            } else {
                midlet = false;
            }
            app = UiApplication.getUiApplication();
        }
        app.enableKeyUpEvents(true);
        if (!app.isHandlingEvents()) {
            new Thread() {

                public void run() {
                    app.enterEventDispatcher();
                }
            }.start();
        }
        Dialog.setCommandsAsButtons(true);
        UIManager.getInstance().addThemeRefreshListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Hashtable themeProps = new Hashtable();
                themeProps.put("SoftButton.margin", "0,0,0,0");
                themeProps.put("SoftButton.padding", "0,0,0,0");
                UIManager.getInstance().addThemeProps(themeProps);
            }
        });


    }

    public void confirmControlView() {
        InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_confirmControlView);
        i.fld = canvas;
        app.invokeLater(i);
    }

    protected void keyPressed(final int keyCode) {
        // expose the method to the canvas
        super.keyPressed(keyCode);
    }

    protected void keyReleased(final int keyCode) {
        // expose the method to the canvas
        super.keyReleased(keyCode);
    }

    public int getDisplayWidth() {
        // the alternative undeprecated API requires a signature
        return net.rim.device.api.ui.Graphics.getScreenWidth();
    }

    public int getDisplayHeight() {
        // the alternative undeprecated API requires a signature
        return net.rim.device.api.ui.Graphics.getScreenHeight();
    }

    public void editString(final Component cmp, final int maxSize, final int constraint, final String text, int keyCode) {
        TextArea txtCmp = (TextArea) cmp;
        if((txtCmp.getConstraint() & TextArea.NON_PREDICTIVE) == 0){
            nativeEdit(txtCmp, txtCmp.getMaxSize(), txtCmp.getConstraint(), txtCmp.getText(), keyCode);
        }
    }

    public void nativeEdit(final Component cmp, final int maxSize, final int constraint, String text, int keyCode) {
        //#ifdef touch
//#         BlackBerryVirtualKeyboard.blockFolding = true;
        //#endif
        if(nativeEdit != null){
            finishEdit(true);
        }

        lightweightEdit = (TextArea) cmp;
        if(keyCode > 0 && getKeyboardType() == Display.KEYBOARD_TYPE_QWERTY) {
            text += ((char)keyCode);
            lightweightEdit.setText(text);
        }
        class LightweightEdit implements Runnable, Animation {
            public void run() {
                long type = 0;
                int constraint = lightweightEdit.getConstraint();
                if((constraint & TextArea.DECIMAL) == TextArea.DECIMAL) {
                        type = BasicEditField.FILTER_REAL_NUMERIC;
                } else {
                    if((constraint & TextArea.EMAILADDR) == TextArea.EMAILADDR) {
                            type = BasicEditField.FILTER_EMAIL;
                    } else {
                        if((constraint & TextArea.NUMERIC) == TextArea.NUMERIC) {
                            type = BasicEditField.FILTER_NUMERIC;
                        } else {
                            if((constraint & TextArea.PHONENUMBER) == TextArea.PHONENUMBER) {
                                type = BasicEditField.FILTER_PHONE;
                            }
                        }
                    }
                }

                if(lightweightEdit.isSingleLineTextArea()) {
                    type |= BasicEditField.NO_NEWLINE;
                }

                if ((constraint & TextArea.PASSWORD) == TextArea.PASSWORD) {
                    nativeEdit = new BBPasswordEditField(lightweightEdit, type, maxSize);
                } else {
                    nativeEdit = new BBEditField(lightweightEdit, type, maxSize);
                }
                nativeEdit.setEditable(true);
                Font f = nativeEdit.getFont();
                if (f.getHeight() > lightweightEdit.getStyle().getFont().getHeight()) {
                    nativeEdit.setFont(f.derive(f.getStyle(), lightweightEdit.getStyle().getFont().getHeight()));
                }
                canvas.add(nativeEdit);
                nativeEdit.setCursorPosition(lightweightEdit.getText().length());

                try {
                    nativeEdit.setFocus();
                } catch (Throwable t) {
                    // no idea why this throws an exception sometimes
                    //t.printStackTrace();
                }
            }

            public boolean animate() { 
                BasicEditField ef = nativeEdit;
                Component lw = lightweightEdit;
                if (lw == null || lw.getComponentForm() != Display.getInstance().getCurrent()) {
                    Display.getInstance().getCurrent().deregisterAnimated(this);
                    finishEdit(false);
                } else {
                    if (ef != null) {
                        if (ef.isDirty()) {
                            lw.repaint();
                        }
                    }
                }
                return false;
            }

            public void paint(com.sun.lwuit.Graphics g) {
            }
        }
        LightweightEdit lw = new LightweightEdit();
        Display.getInstance().getCurrent().registerAnimated(lw);
        Application.getApplication().invokeLater(lw);
    }

    public void setCurrentForm(Form f) {
        super.setCurrentForm(f);

        nullFld = null;
        synchronized(UiApplication.getEventLock()) {
            while(canvas.getFieldCount() > 0) {
                canvas.delete(canvas.getField(0));
            }
        }
    }

    void finishEdit(final boolean canceled) {
        // assigning the field here prevents a null pointer exception in the layout
        BasicEditField be = nativeEdit;
        if (be != null) {
            if (!Application.isEventDispatchThread()) {
                InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_finishEdit);
                Application.getApplication().invokeLater(i);
                return;
            }
            if (!canceled && lightweightEdit != null) {
                Display.getInstance().onEditingComplete(lightweightEdit, be.getText());
            }
            lightweightEdit = null;
            nativeEdit = null;
            canvas.delete(be);
            flushGraphics();
            //#ifdef touch
//#             BlackBerryVirtualKeyboard.blockFolding = false;
            //#endif
            Display.getInstance().setShowVirtualKeyboard(false);
        }
    }

    public void setNativeCommands(Vector commands) {
        canvas.setNativeCommands(commands);
    }

    public void flushGraphics(int x, int y, int width, int height) {
        canvas.flush(x, y, width, height, app);
    }

    public void flushGraphics() {
        canvas.flush(0, 0, getDisplayWidth(), getDisplayHeight(), app);
    }

    public void getRGB(Object nativeImage, int[] arr, int offset, int x, int y, int width, int height) {
        Bitmap b = (Bitmap) nativeImage;
        b.getARGB(arr, offset, width, x, y, width, height);
    }

    public Object createImage(int[] rgb, int width, int height) {
        Bitmap image = new Bitmap(width, height);
        image.setARGB(rgb, 0, width, 0, 0, width, height);
        return image;
    }

    public Object createImage(String path) throws IOException {
        try {
            return createImage(com.sun.lwuit.Image.class.getResourceAsStream(path));
        } catch (RuntimeException err) {
            throw new IOException(err.toString());
        }
    }

    public Object createImage(InputStream i) throws IOException {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int len;
        while ((len = i.read(buf)) > -1) {
            out.write(buf, 0, len);
        }
        out.close();
        i.close();
        byte[] b = out.toByteArray();
        return Bitmap.createBitmapFromBytes(b, 0, b.length, 1);
    }

    public boolean isAlphaMutableImageSupported() {
        return true;
    }

    public Object createMutableImage(int width, int height, int fillColor) {
        Bitmap b = new Bitmap(width, height);
        Graphics g = new Graphics(b);
        if ((fillColor & 0xff000000) != 0xff000000) {
            g.setColor(fillColor & 0xffffff);
            int oldAlpha = g.getGlobalAlpha();
            g.setGlobalAlpha((fillColor >> 24) & 0xff);
            g.clear();
            g.setGlobalAlpha(oldAlpha);
        } else {
            g.setColor(fillColor & 0xffffff);
            g.fillRect(0, 0, width, height);
        }
        return b;
    }

    public Object createImage(byte[] bytes, int offset, int len) {
        return Bitmap.createBitmapFromBytes(bytes, offset, len, 1);
    }

    public int getImageWidth(Object i) {
        return ((Bitmap) i).getWidth();
    }

    public int getImageHeight(Object i) {
        return ((Bitmap) i).getHeight();
    }

    /**
     * @inheritDoc
     */
    public Object scale(Object nativeImage, int width, int height) {
        Bitmap image = (Bitmap) nativeImage;
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

    private void scaleArray(Bitmap currentImage, int srcWidth, int srcHeight, int height, int width, int[] currentArray, int[] destinationArray) {
        // Horizontal Resize
        int yRatio = (srcHeight << 16) / height;
        int xRatio = (srcWidth << 16) / width;
        int xPos = xRatio / 2;
        int yPos = yRatio / 2;

        // if there is more than 16bit color there is no point in using mutable
        // images since they won't save any memory
        for (int y = 0; y < height; y++) {
            int srcY = yPos >> 16;
            getRGB(currentImage, currentArray, 0, 0, srcY, srcWidth, 1);
            for (int x = 0; x < width; x++) {
                int srcX = xPos >> 16;
                int destPixel = x + y * width;
                if ((destPixel >= 0 && destPixel < destinationArray.length) && (srcX < currentArray.length)) {
                    destinationArray[destPixel] = currentArray[srcX];
                }
                xPos += xRatio;
            }
            yPos += yRatio;
            xPos = xRatio / 2;
        }
    }

    public int getSoftkeyCount() {
        return 1;
    }

    public int[] getSoftkeyCode(int index) {
        return new int[]{MENU_SOFT_KEY};
    }

    public int getClearKeyCode() {
        return Keypad.KEY_DELETE;
    }

    public int getBackspaceKeyCode() {
        return Keypad.KEY_BACKSPACE;
    }

    public int getBackKeyCode() {
        return Keypad.KEY_ESCAPE;
    }

    public int getGameAction(int keyCode) {
        switch (keyCode) {
            // the enter key should also map to fire
            case '\n':
            case GAME_KEY_CODE_FIRE:
                return Display.GAME_FIRE;
            case GAME_KEY_CODE_UP:
                return Display.GAME_UP;
            case GAME_KEY_CODE_DOWN:
                return Display.GAME_DOWN;
            case GAME_KEY_CODE_LEFT:
                return Display.GAME_LEFT;
            case GAME_KEY_CODE_RIGHT:
                return Display.GAME_RIGHT;
        }
        return 0;
    }

    public int getKeyCode(int gameAction) {
        switch (gameAction) {
            case Display.GAME_FIRE:
                return GAME_KEY_CODE_FIRE;
            case Display.GAME_UP:
                return GAME_KEY_CODE_UP;
            case Display.GAME_DOWN:
                return GAME_KEY_CODE_DOWN;
            case Display.GAME_LEFT:
                return GAME_KEY_CODE_LEFT;
            case Display.GAME_RIGHT:
                return GAME_KEY_CODE_RIGHT;
        }
        return 0;
    }

    public boolean isTouchDevice() {
        return canvas.isTouchDevice();
    }

    public int getColor(Object graphics) {
        if (graphics == canvas.getGlobalGraphics()) {
            // make sure color wasn't broken...
            ((Graphics) graphics).setColor(color);
            return color;
        }
        return ((Graphics) graphics).getColor() & 0xffffff;
    }

    public void setColor(Object graphics, int RGB) {
        if (graphics == canvas.getGlobalGraphics()) {
            color = RGB;
        }
        ((Graphics) graphics).setColor(RGB);
    }

    public boolean isAlphaGlobal() {
        return true;
    }

    public void setAlpha(Object graphics, int alpha) {
        ((Graphics) graphics).setGlobalAlpha(alpha);
    }

    public int getAlpha(Object graphics) {
        return ((Graphics) graphics).getGlobalAlpha();
    }

    public void setNativeFont(Object graphics, Object font) {
        ((Graphics) graphics).setFont(font(font));
    }

    public int getClipX(Object graphics) {
        XYRect r = new XYRect();
        ((Graphics) graphics).getAbsoluteClippingRect(r);
        return r.x;
    }

    public int getClipY(Object graphics) {
        XYRect r = new XYRect();
        ((Graphics) graphics).getAbsoluteClippingRect(r);
        return r.y;
    }

    public int getClipWidth(Object graphics) {
        XYRect r = new XYRect();
        ((Graphics) graphics).getAbsoluteClippingRect(r);
        return r.width;
    }

    public int getClipHeight(Object graphics) {
        XYRect r = new XYRect();
        ((Graphics) graphics).getAbsoluteClippingRect(r);
        return r.height;
    }

    public void setClip(Object graphics, int x, int y, int width, int height) {
        Graphics g = (net.rim.device.api.ui.Graphics) graphics;
        net.rim.device.api.ui.Font oldFont = g.getFont();
        int oldColor = g.getColor();
        int oldAlpha = g.getGlobalAlpha();
        while (g.getContextStackSize() > 1) {
            g.popContext();
        }
        g.pushRegion(x, y, width, height, 0, 0);
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        /**
         * applying a clip will automatically
         * reset some information that we need to keep track of
         * manually (it seems).
         */
        g.setFont(oldFont == null ? (net.rim.device.api.ui.Font)getDefaultFont() : oldFont);
        g.setColor(oldColor);
        g.setGlobalAlpha(oldAlpha);
    }

    public void clipRect(Object graphics, int x, int y, int width, int height) {
        Graphics g = (Graphics) graphics;
        g.pushRegion(x, y, width, height, 0, 0);
        g.translate(-g.getTranslateX(), -g.getTranslateY());
    }

    public void drawLine(Object graphics, int x1, int y1, int x2, int y2) {
        ((Graphics) graphics).drawLine(x1, y1, x2, y2);
    }

    public void fillRect(Object graphics, int x, int y, int width, int height) {
        ((Graphics) graphics).fillRect(x, y, width, height);
    }

    public void drawRect(Object graphics, int x, int y, int width, int height) {
        ((Graphics) graphics).drawRect(x, y, width, height);
    }

    public void drawRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        ((Graphics) graphics).drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        ((Graphics) graphics).fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        ((Graphics) graphics).fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        ((Graphics) graphics).drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawString(Object graphics, String str, int x, int y) {
        ((Graphics) graphics).drawText(str, x, y);
    }

    /**
     * @inheritDoc
     */
    public VideoComponent createVideoPeer(String url) throws IOException {
        try {
            Player p = Manager.createPlayer(url);
            p.realize();
            VideoControl vidc = (VideoControl) p.getControl("VideoControl");
            Field f = (Field) vidc.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
            RIMVideoComponent r = new RIMVideoComponent(f, p, vidc);
            r.putClientProperty("VideoControl", vidc);
            r.putClientProperty("Player", p);
            return r;
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
            VideoControl vidc = (VideoControl) p.getControl("VideoControl");
            Field f = (Field) vidc.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
            RIMVideoComponent r = new RIMVideoComponent(f, p, vidc);
            r.putClientProperty("VideoControl", vidc);
            r.putClientProperty("Player", p);
            return r;
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    class RIMVideoComponent extends VideoComponent {

        private boolean fullscreen;
        private Player p;
        private VideoControl c;
        private boolean started;

        RIMVideoComponent(Field f, Player p, VideoControl c) {
            super(f);
            this.p = p;
            this.c = c;
        }

        public int getMediaDuration() {
            return (int)(((Player) getNativePeer()).getDuration() / 1000);
        }

        public boolean isPlaying() {
            return ((Player) getNativePeer()).getState() == Player.STARTED;
        }

        public void setVisible(boolean b) {
            super.setVisible(b);
            try {
                c.setVisible(b);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        protected void onPositionSizeChange() {
            InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_dirty);
            i.fld = (Field) getNativePeer();
            app.invokeLater(i);
        }

        protected void initComponent() {
            InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_initComponent);
            i.fld = (Field) getNativePeer();
            app.invokeLater(i);
            if (started) {
                start();
            }
        }

        protected void deinitialize() {
            canvas.eventTarget = null;
            InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_deinitialize);
            i.fld = (Field) getNativePeer();
            app.invokeLater(i);
        }

        /**
         * @inheritDoc
         */
        public void paint(com.sun.lwuit.Graphics g) {
            /*if(isVisible()){
            try {
            if (isFullScreen()) {
            c.setDisplayLocation(0, 0);
            c.setDisplaySize(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
            } else {
            c.setDisplayLocation(getAbsoluteX(), getAbsoluteY());
            int w = getWidth();
            int h = getHeight();
            if (c.getDisplayWidth() != w || c.getDisplayHeight() != h) {
            c.setDisplaySize(w, h);
            }
            }
            } catch (MediaException ex) {
            ex.printStackTrace();
            }
            }*/
        }

        /*public boolean animate() {
        Field fld = (Field)getNativePeer();
        if(fld.isDirty()) {
        setShouldCalcPreferredSize(true);
        }
        return super.animate();
        }*/
        /**
         * @inheritDoc
         */
        protected Dimension calcPreferredSize() {
            return new Dimension(c.getDisplayWidth(), c.getDisplayHeight());
        }

        /**
         * Start media playback implicitly setting the component to visible
         */
        public void start() {
            if (isInitialized()) {
                InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_startMedia);
                i.v = p;
                app.invokeLater(i);
            } else {
                started = true;
            }
        }

        /**
         * Stope media playback
         */
        public void stop() {
            try {
                p.stop();
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
            p.setLoopCount(count);
        }

        /**
         * Return the duration of the media
         *
         * @return the duration of the media
         */
        public int getMediaTimeMS() {
            return (int) (p.getMediaTime() / 1000);
        }

        /**
         * "Jump" to a point in time within the media
         *
         * @param now the point in time to "Jump" to
         * @return the media time in microseconds
         */
        public int setMediaTimeMS(int now) {
            try {
                return (int) (p.setMediaTime(now * 1000) / 1000);
            } catch (MediaException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.toString());
            }
        }

        public void setFullScreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            try {
                c.setDisplayFullScreen(fullscreen);
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
            repaint();
        }

        public boolean isFullScreen() {
            return fullscreen;
        }
    }

    public void drawImage(Object graphics, Object img, int x, int y) {
        Bitmap b = (Bitmap) img;
        ((Graphics) graphics).drawBitmap(x, y, b.getWidth(), b.getHeight(), b, 0, 0);
    }

    public void drawRGB(Object graphics, int[] rgbData, int offset, int x, int y, int w, int h, boolean processAlpha) {
        Graphics nativeGraphics = (Graphics) graphics;
        int rgbX = x;
        int rgbY = y;

        //if the x or y are positive simply redirect the call to midp Graphics
        if (rgbX >= 0) {
            if (processAlpha) {
                nativeGraphics.drawARGB(rgbData, offset, w, rgbX, rgbY, w, h);
            } else {
                nativeGraphics.drawRGB(rgbData, offset, w, rgbX, rgbY, w, h);
            }
            return;
        }

        //if the translate causes us to paint out of the bounds
        //we will paint only the relevant rows row by row to avoid some devices bugs
        //such as BB that fails to paint if the coordinates are negative.
        if (rgbX < 0) {
            if (rgbX + w > 0) {
                if (w < rgbData.length) {
                    for (int i = 1; i <= rgbData.length / w; i++) {
                        offset = -rgbX + (w * (i - 1));
                        rgbY++;
                        if (rgbY >= 0) {
                            nativeGraphics.drawARGB(rgbData, offset, (w + rgbX), 0, rgbY, w + rgbX, 1);
                        }
                    }
                }
            }
        }
    }

    public Object getNativeGraphics() {
        return canvas.getGlobalGraphics();
    }

    public Object getNativeGraphics(Object image) {
        return new Graphics((Bitmap) image);
    }

    public boolean isAntiAliasingSupported() {
        return true;
    }

    public void setAntiAliased(Object graphics, boolean a) {
        ((Graphics) graphics).setDrawingStyle(Graphics.DRAWSTYLE_AALINES, a);
        ((Graphics) graphics).setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, a);
    }

    public boolean isAntiAliased(Object graphics) {
        return ((Graphics) graphics).isDrawingStyleSet(Graphics.DRAWSTYLE_AALINES);
    }

    public boolean isAntiAliasedTextSupported() {
        return false;
    }

    public void setAntiAliasedText(Object graphics, boolean a) {
        // this method uses undocumented behavior so it won't compile on different JDE versions
        /*Graphics g = (Graphics)graphics;
        Font f = g.getFont();
        if(a) {
        if(f.getAntialiasMode() != Font.ANTIALIAS_SUBPIXEL) {
        f = f.derive(f.getStyle(), f.getHeight(), Ui.UNITS_px, Font.ANTIALIAS_SUBPIXEL, f.getEffects());
        g.setFont(f);
        }
        } else {
        if(f.getAntialiasMode() == Font.ANTIALIAS_SUBPIXEL) {
        f = f.derive(f.getStyle(), f.getHeight(), Ui.UNITS_px, Font.ANTIALIAS_NONE, f.getEffects());
        g.setFont(f);
        }
        }*/
    }

    public boolean isAntiAliasedText(Object graphics) {
        // this method uses undocumented behavior so it won't compile on different JDE versions
        /*Graphics g = (Graphics)graphics;
        Font f = g.getFont();
        return f.getAntialiasMode() == Font.ANTIALIAS_SUBPIXEL;*/
        return false;
    }

    public int charsWidth(Object nativeFont, char[] ch, int offset, int length) {
        return font(nativeFont).getAdvance(ch, offset, length);
    }

    private Font font(Object nativeFont) {
        if (nativeFont == null) {
            return Font.getDefault();
        }
        return (Font) nativeFont;
    }

    public int stringWidth(Object nativeFont, String str) {
        return font(nativeFont).getAdvance(str);
    }

    public int charWidth(Object nativeFont, char ch) {
        return font(nativeFont).getAdvance(ch);
    }

    public int getHeight(Object nativeFont) {
        return font(nativeFont).getHeight();
    }

    public Object getDefaultFont() {
        return Font.getDefault();
    }

    public int getFace(Object nativeFont) {
        Font nf = (Font) font(nativeFont);
        int s = nf.getStyle();
        int result = 0;
        if ((s & Font.BOLD) == Font.BOLD) {
            result |= com.sun.lwuit.Font.STYLE_BOLD;
        }
        if ((s & Font.ITALIC) == Font.ITALIC) {
            result |= com.sun.lwuit.Font.STYLE_ITALIC;
        }
        if ((s & Font.UNDERLINED) == Font.UNDERLINED) {
            result |= com.sun.lwuit.Font.STYLE_UNDERLINED;
        }
        return result;
    }

    public int getSize(Object nativeFont) {
        Font nf = (Font) font(nativeFont);
        int bbSize = Font.getDefault().getHeight();
        int diff = Font.getDefault().getHeight() / 3;

        if (nf.getHeight() == bbSize - diff) {
            return com.sun.lwuit.Font.SIZE_SMALL;
        }
        if (nf.getHeight() == bbSize + diff) {
            return com.sun.lwuit.Font.SIZE_LARGE;
        }
        return com.sun.lwuit.Font.SIZE_MEDIUM;
    }

    public int getStyle(Object nativeFont) {
        Font nf = (Font) font(nativeFont);
        int s = nf.getStyle();
        if ((s & FontFamily.MONO_BITMAP_FONT) == FontFamily.MONO_BITMAP_FONT) {
            return com.sun.lwuit.Font.FACE_MONOSPACE;
        }
        if ((s & FontFamily.SCALABLE_FONT) == FontFamily.SCALABLE_FONT) {
            return com.sun.lwuit.Font.FACE_PROPORTIONAL;
        }
        return com.sun.lwuit.Font.FACE_SYSTEM;
    }

    public Object createFont(int face, int style, int size) {
        Font font = Font.getDefault();
        int bbSize = Font.getDefault().getHeight();
        int diff = Font.getDefault().getHeight() / 3;
        switch (size) {
            case com.sun.lwuit.Font.SIZE_SMALL:
                bbSize -= diff;
                break;
            case com.sun.lwuit.Font.SIZE_LARGE:
                bbSize += diff;
                break;
        }
        int bbStyle = Font.PLAIN;
        switch (style) {
            case com.sun.lwuit.Font.STYLE_BOLD:
                bbStyle = Font.BOLD;
                break;
            case com.sun.lwuit.Font.STYLE_ITALIC:
                bbStyle = Font.ITALIC;
                break;
            case com.sun.lwuit.Font.STYLE_PLAIN:
                bbStyle = Font.PLAIN;
                break;
            case com.sun.lwuit.Font.STYLE_UNDERLINED:
                bbStyle = Font.UNDERLINED;
                break;
            default:
                // probably bold/italic
                bbStyle = Font.BOLD | Font.ITALIC;
                break;
        }
        switch (face) {
            case com.sun.lwuit.Font.FACE_MONOSPACE:
                font = font.getFontFamily().getFont(bbStyle | FontFamily.MONO_BITMAP_FONT, bbSize);
                break;
            case com.sun.lwuit.Font.FACE_PROPORTIONAL:
                font = font.getFontFamily().getFont(bbStyle | FontFamily.SCALABLE_FONT, bbSize);
                break;
            case com.sun.lwuit.Font.FACE_SYSTEM:
                font = font.derive(bbStyle, bbSize, Ui.UNITS_px, Font.ANTIALIAS_SUBPIXEL, font.getEffects());
                break;
        }
        return font;
    }

    public boolean isNativeInputSupported() {
        return true;
    }

    public int getKeyboardType() {
        int keyT = Keypad.getHardwareLayout();
        switch (keyT) {
            case Keypad.HW_LAYOUT_LEGACY:
            case Keypad.HW_LAYOUT_32:
            case Keypad.HW_LAYOUT_39:
            case Keypad.HW_LAYOUT_PHONE:
                return Display.KEYBOARD_TYPE_QWERTY;
            case Keypad.HW_LAYOUT_REDUCED:
            case Keypad.HW_LAYOUT_REDUCED_24:
                return Display.KEYBOARD_TYPE_HALF_QWERTY;
            case 1230263636: // HW_LAYOUT_ITUT

                return Display.KEYBOARD_TYPE_NUMERIC;
            //#ifdef touch
//#             case Keypad.HW_LAYOUT_TOUCHSCREEN_12:
//#             case Keypad.HW_LAYOUT_TOUCHSCREEN_24:
//#             case Keypad.HW_LAYOUT_TOUCHSCREEN_29:
//#                 return Display.KEYBOARD_TYPE_VIRTUAL;
            //#endif
        }
        return Display.KEYBOARD_TYPE_QWERTY;
    }

    public boolean isMultiTouch() {
        return canvas.isMultiTouch();
    }

    public boolean isClickTouchScreen() {
        return canvas.isClickTouchScreen();
    }

    protected void pointerDragged(final int[] x, final int[] y) {
        super.pointerDragged(x, y);
    }

    protected void pointerPressed(final int[] x, final int[] y) {
        super.pointerPressed(x, y);
    }

    protected void pointerReleased(final int[] x, final int[] y) {
        super.pointerReleased(x, y);
    }

    protected void pointerHover(final int[] x, final int[] y) {
        super.pointerHover(x, y);
    }

    protected void pointerHoverPressed(final int[] x, final int[] y) {
        super.pointerHoverPressed(x, y);
    }

    protected void pointerHoverPressed(final int x, final int y) {
        super.pointerHoverPressed(x, y);
    }

    protected void pointerDragged(final int x, final int y) {
        super.pointerDragged(x, y);
    }

    protected void pointerPressed(final int x, final int y) {
        super.pointerPressed(x, y);
    }

    protected void pointerReleased(final int x, final int y) {
        super.pointerReleased(x, y);
    }

    protected void pointerHoverReleased(final int x, final int y) {
        super.pointerHoverReleased(x, y);
    }

    protected void pointerHoverReleased(final int[] x, final int[] y) {
        super.pointerHoverReleased(x, y);
    }

    protected void pointerHover(final int x, final int y) {
        super.pointerHover(x, y);
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
    }

    public boolean minimizeApplication() {
        app.requestBackground();
        return true;
    }

    public void restoreMinimizedApplication() {
        app.requestForeground();
    }

    public boolean isThirdSoftButton() {
        return false;
    }

    /**
     * Indicates whether the application should minimize or exit when the end key is pressed
     *
     * @return the minimizeOnEnd
     */
    public static boolean isMinimizeOnEnd() {
        return minimizeOnEnd;
    }

    /**
     * Indicates whether the application should minimize or exit when the end key is pressed
     * 
     * @param aMinimizeOnEnd the minimizeOnEnd to set
     */
    public static void setMinimizeOnEnd(boolean aMinimizeOnEnd) {
        minimizeOnEnd = aMinimizeOnEnd;
    }

    /**
     * Volume listener is invoked when the volume up/down buttons on the blackberry
     * device are pressed. The key event would be either Characters.CONTROL_VOLUME_UP
     * or Characters.CONTROL_VOLUME_DOWN
     *
     * @param al action listener callback
     */
    public static void addVolumeListener(ActionListener al) {
        if (volumeListener == null) {
            volumeListener = new EventDispatcher();
        }
        volumeListener.addListener(al);
    }

    /**
     * Remove the volume listener instance
     *
     * @param al action listener to remove
     */
    public static void removeVolumeListener(ActionListener al) {
        if (volumeListener != null) {
            volumeListener.removeListener(al);
            if (volumeListener.getListenerVector() == null || volumeListener.getListenerVector().size() == 0) {
                volumeListener = null;
            }
        }
    }

    static EventDispatcher getVolumeListener() {
        return volumeListener;
    }

    /**
     * @inheritDoc
     */
    public PeerComponent createNativePeer(Object nativeComponent) {
        if (nativeComponent instanceof Field) {
            if (nullFld == null) {
                nullFld = new NullField();
                nullFld.setFocusListener(new FocusChangeListener() {
                    public void focusChanged(Field field, int eventType) {
                        if(lightweightEdit != null) {
                            finishEdit(false);
                        }
                    }
                });
                canvas.add(nullFld);
            }
            final Field fld = (Field) nativeComponent;
            final PeerComponent peer = new PeerComponent(fld) {

                public boolean isFocusable() {
                    if (fld != null) {
                        return fld.isFocusable();
                    }
                    return super.isFocusable();
                }

                public void setFocus(boolean b) {
                    if (hasFocus() == b) {
                        return;
                    }
                    if (b) {
                        canvas.eventTarget = fld;
                        fld.setFocusListener(new FocusChangeListener() {

                            public void focusChanged(Field field, int eventType) {
                                if (getNativePeer() == fld && eventType == FocusChangeListener.FOCUS_LOST) {
                                    fld.setFocusListener(null);
                                    nullFld.setFocus();
                                    canvas.eventTarget = null;
                                    canvas.repeatLastNavigation();
                                }
                            }
                        });
                    } else {
                        fld.setFocusListener(null);
                        if (canvas.eventTarget == fld) {
                            canvas.eventTarget = null;
                        }
                    }
                    if (isInitialized()) {
                        InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_AND_WAIT_setFocus);
                        i.val = b;
                        i.fld = fld;
                        app.invokeAndWait(i);
                    } else {
                        super.setFocus(b);
                    }
                }

                public boolean animate() {
                    if (fld.isDirty()) {
                        repaint();
                    }
                    return super.animate();
                }

                protected Dimension calcPreferredSize() {
                    InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_AND_WAIT_calcPreferredSize);
                    i.dim = new Dimension();
                    i.fld = fld;
                    app.invokeAndWait(i);
                    return i.dim;
                }

                protected void onPositionSizeChange() {
                    InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_dirty);
                    i.fld = fld;
                    app.invokeLater(i);
                }

                protected void initComponent() {
                    fieldComponentMap.put(fld, this);
                    InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_initComponent);
                    i.fld = fld;
                    app.invokeLater(i);
                    setFocus(super.hasFocus());
                    if (hasFocus()) {
                        canvas.eventTarget = fld;
                    }
                    getComponentForm().registerAnimated(this);
                }

                protected void deinitialize() {
                    getComponentForm().deregisterAnimated(this);
                    canvas.eventTarget = null;
                    InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_deinitialize);
                    i.fld = fld;
                    app.invokeLater(i);
                    fieldComponentMap.remove(fld);
                }
            };
            fieldComponentMap.put(fld, peer);
            return peer;
        }

        throw new IllegalArgumentException(nativeComponent.getClass().getName());
    }

    /**
     * @inheritDoc
     */
    public void showNativeScreen(Object nativeFullScreenPeer) {
        InvokeLaterWrapper i = new InvokeLaterWrapper(INVOKE_LATER_showNativeScreen);
        i.fld = (Screen) nativeFullScreenPeer;
        app.invokeLater(i);
    }

    /**
     * #######################################################################
     * #######################################################################
     *
     * see editString() method
     */
    private class EditPopup extends PopupScreen implements FocusChangeListener, Runnable {

        protected final TextArea lightweightEdit;
        private boolean finished = false;
        private boolean cancel = false;
        private String okString;
        private String cancelString;
        protected final BasicEditField nativeEdit;

        protected EditPopup(TextArea lightweightEdit, boolean password, int maxSize) {
            super(new VerticalFieldManager(), Field.FOCUSABLE | Field.EDITABLE | Screen.DEFAULT_MENU);
            
            UIManager m = UIManager.getInstance();
            okString = m.localize("ok", "OK");
            cancelString = m.localize("cancel", "Cancel");
            this.lightweightEdit = lightweightEdit;
            long type = 0;
            switch (lightweightEdit.getConstraint()) {
                case TextArea.DECIMAL:
                    type = BasicEditField.FILTER_REAL_NUMERIC;
                    break;
                case TextArea.EMAILADDR:
                    type = BasicEditField.FILTER_EMAIL;
                    break;
                case TextArea.NUMERIC:
                    type = BasicEditField.FILTER_NUMERIC;
                    break;
                case TextArea.PHONENUMBER:
                    type = BasicEditField.FILTER_PHONE;
                    break;
            }
            if (password) {
                nativeEdit = new BBPasswordEditField(lightweightEdit, type, maxSize);
            } else {
                nativeEdit = new BBEditField(lightweightEdit, type, maxSize);
            }
            // using Field.EDITABLE flag now because of bug with DevTrack ID 354265 at
            // https://www.blackberry.com/jira/browse/JAVAAPI-101
            //nativeEdit.setEditable(true);
            net.rim.device.api.ui.Font f = nativeEdit.getFont();
            if (f.getHeight() > lightweightEdit.getStyle().getFont().getHeight()) {
                nativeEdit.setFont(f.derive(f.getStyle(),
                        lightweightEdit.getStyle().getFont().getHeight()));
            }
            add(nativeEdit);
            nativeEdit.setFocus();
            nativeEdit.setFocusListener(this);
        }

        protected void makeMenu(Menu menu, int arg1) {
            menu.add(new MenuItem(okString, 0, 99) {

                public void run() {
                    finishEdit();
                }
            });
            menu.add(new MenuItem(cancelString, 1, 100) {

                public void run() {
                    EditPopup.this.cancel = true;
                    finishEdit();
                }
            });
            super.makeMenu(menu, arg1);
        }

        protected void onMenuDismissed(Menu arg0) {
            super.onMenuDismissed(arg0);
            /**
             * at this point the EDT is still blocked, so we repaint the
             * screen with our old buffer and request some fresh paint
             * for the textfield.
             */
            this.invalidate();
        }

        protected void onExposed() {
            super.onExposed();
            /**
             * we reach this if the context menu opened another dialog
             * on top.
             *
             * at this point the EDT is still blocked, so we repaint the
             * screen with our old buffer and request some fresh paint
             * for the textfield.
             */
            this.invalidate();
        }

        protected boolean keyDown(int keycode, int time) {
            if (Keypad.key(keycode) == Keypad.KEY_ESCAPE) {
                this.cancel = true;
                finishEdit();
                return true;
            }
            if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
                /**
                 * single line text field accept input.
                 */
                if (this.lightweightEdit.isSingleLineTextArea()) {
                    finishEdit();
                    return true;
                }
            }
            return super.keyDown(keycode, time);
        }

        public void focusChanged(Field field, int type) {
            if (field == this.nativeEdit && type == FOCUS_LOST) {
                finishEdit();
            }
        }

        protected void startEdit() {
            BlackBerryImplementation.this.app.invokeAndWait(this);
        }

        public void run() {
            UiApplication.getUiApplication().pushScreen(this);
        }

        protected void finishEdit() {
            if (this.finished) {
                return;
            }
            this.finished = true;
            if (!this.cancel) {
                Display.getInstance().onEditingComplete(lightweightEdit, nativeEdit.getText());
            }
            if (isTouchDevice()) {
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        Display.getInstance().setShowVirtualKeyboard(false);
                    }
                });
            }
            UiApplication.getUiApplication().popScreen(this);
        }        
        
    }

    /**
     * #######################################################################
     * #######################################################################
     *
     * see editString() method
     */
    private class BBEditField extends ActiveAutoTextEditField {

        private TextArea lightweightEdit = null;
        
        private NativeEditCallback callback = new NativeEditCallback();
        
        public BBEditField(TextArea lightweightEdit, long type, int maxSize) {
            super("", lightweightEdit.getText(), maxSize, Field.EDITABLE | Field.FOCUSABLE | Field.SPELLCHECKABLE | type);
            this.lightweightEdit = lightweightEdit;

        }

        public void paintBackground(net.rim.device.api.ui.Graphics g) {
            g.setBackgroundColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.setColor(lightweightEdit.getSelectedStyle().getBgColor());
            super.paintBackground(g);
            g.setColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.fillRect(0, 0, this.getWidth(),
                    this.getHeight());
        }

        public void paint(net.rim.device.api.ui.Graphics g) {
            g.setBackgroundColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.setColor(lightweightEdit.getSelectedStyle().getFgColor());
            super.paint(g);
        }

        protected boolean keyDown(int keycode, int time) {
            if (Keypad.key(keycode) == Keypad.KEY_ESCAPE) {
                finishEdit(true);
                return true;
            }
            if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
                if (lightweightEdit.isSingleLineTextArea()) {
                    finishEdit(false);
                    return true;
                }
            }
            return super.keyDown(keycode, time);
        }

        protected boolean keyUp(int keycode, int time) {
            boolean b = super.keyUp(keycode, time);
            callback.onChanged();
            return b;
        }

        protected boolean navigationMovement(int dx, int dy, int status, int time) {
            boolean b = super.navigationMovement(dx, dy, status, time);
            callback.onChanged();
            return b;
        }
    }

    /**
     * #######################################################################
     * #######################################################################
     *
     * see editString() method
     */
    private class BBPasswordEditField extends PasswordEditField {

        private TextArea lightweightEdit = null;

        private NativeEditCallback callback = new NativeEditCallback();
        
        public BBPasswordEditField(TextArea lightweightEdit, long type, int maxSize) {
            super("", lightweightEdit.getText(), maxSize, Field.EDITABLE | Field.FOCUSABLE | type);
            this.lightweightEdit = lightweightEdit;
        }

        public void paintBackground(net.rim.device.api.ui.Graphics g) {
            g.setBackgroundColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.setColor(lightweightEdit.getSelectedStyle().getBgColor());
            super.paintBackground(g);
            g.setColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.fillRect(0, 0, this.getWidth(),
                    this.getHeight());
        }

        public void paint(net.rim.device.api.ui.Graphics g) {
            g.setBackgroundColor(lightweightEdit.getSelectedStyle().getBgColor());
            g.setColor(lightweightEdit.getSelectedStyle().getFgColor());
            super.paint(g);
        }

        protected boolean keyDown(int keycode, int time) {
            if (Keypad.key(keycode) == Keypad.KEY_ESCAPE) {
                finishEdit(true);
                return true;
            }
            if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
                if (lightweightEdit.isSingleLineTextArea()) {
                    finishEdit(false);
                    return true;
                }
            }
            return super.keyDown(keycode, time);
        }

        protected boolean keyUp(int keycode, int time) {
            callback.onChanged();
            return super.keyUp(keycode, time);
        }

        protected boolean navigationMovement(int dx, int dy, int status, int time) {
            callback.onChanged();
            return super.navigationMovement(dx, dy, status, time);
        }
    }

    class NativeEditCallback implements Runnable {

        private String text;
        private int cursorLocation;

        public void onChanged() {
            if (nativeEdit != null) {
                text = nativeEdit.getText();
                cursorLocation = nativeEdit.getCursorPosition();
                Display.getInstance().callSerially(this);
            }
        }

        public void run() {
            // prevent a race condition by copying the global scope
            TextArea t = lightweightEdit;
            if (t != null) {
                Dimension d = t.getPreferredSize();
                t.setText(text);
                Dimension current = t.getPreferredSize();

                Form f = t.getComponentForm();

                // allows the lightweight text area to grow to fill up the screen
                if (d.getHeight() != current.getHeight() || d.getWidth() != current.getWidth()) {
                    f.getComponentForm().revalidate();
                }

                int currentLineLength = 0;
                int numOfChars = 0;
                int currentY = 0;
                while (numOfChars <= cursorLocation && currentY < t.getLines()) {
                    String currentLine = t.getTextAt(currentY);
                    currentLineLength = currentLine.length();
                    if (numOfChars + currentLineLength < text.length() &&
                            (text.charAt(numOfChars + currentLineLength) == '\n' ||
                            text.charAt(numOfChars + currentLineLength) == ' ')) {
                        currentLineLength++;
                    }
                    numOfChars += currentLineLength;
                    currentY++;
                }
                int cursorY = Math.max(0, currentY - 1);
                com.sun.lwuit.Font textFont = t.getStyle().getFont();
                int rowsGap = t.getRowsGap();
                int lineHeight = textFont.getHeight() + rowsGap;
                t.scrollRectToVisible(t.getScrollX(), cursorY * lineHeight, t.getWidth(), lineHeight, t);
            }
        }
    }

    /**
     * This class is here to unify multiple invocations of invoke later
     */
    class InvokeLaterWrapper implements Runnable {

        public Field fld;
        public Dimension dim;
        public boolean val;
        private int i;
        public Player v;

        public InvokeLaterWrapper(int i) {
            this.i = i;
        }

        public void run() {
            switch (i) {
                case INVOKE_LATER_confirmControlView:
                    if (app.getActiveScreen() != canvas) {
                        app.pushScreen(canvas);
                    }
                    break;

                case INVOKE_LATER_finishEdit:
                    finishEdit(val);
                    break;

                case INVOKE_LATER_initComponent:
                    canvas.add(fld);
                    break;

                case INVOKE_LATER_deinitialize:
                    canvas.delete(fld);
                    break;

                case INVOKE_LATER_showNativeScreen:
                    app.pushScreen((Screen) fld);
                    break;

                case INVOKE_AND_WAIT_calcPreferredSize:
                    dim.setWidth(Math.max(fld.getWidth(), fld.getPreferredWidth()));
                    dim.setHeight(Math.max(fld.getHeight(), fld.getPreferredHeight()));
                    break;

                case INVOKE_AND_WAIT_setFocus:
                    try {
                        // Not sure why an exception is thrown here during a touch event... 
                        if (val) {
                            fld.setFocus();
                        } else {
                            nullFld.setFocus();
                        }
                    } catch (Throwable t) {
                        //t.printStackTrace();
                    }
                    break;
                case INVOKE_LATER_dirty:
                    if (fld.getScreen() == canvas) {
                        fld.setDirty(true);
                        canvas.updateRIMLayout();
                    }
                    break;
                case INVOKE_LATER_startMedia:
                    try {
                        v.start();
                    } catch (MediaException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void execute(String url) {
        if(midlet) {
            try {
                midletInstance.platformRequest(url);
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            // For some reason RIM decided to require signing for these API's
            // if you need this functionality and own a signature add a define to
            // signed
            //#ifdef signed
//#             net.rim.blackberry.api.browser.Browser.getDefaultSession().displayPage(url);
            //#endif
        }
    }

    public void exitApplication() {
        if(midlet) {
            midletInstance.notifyDestroyed();
        } else {
            System.exit(0);
        }
    }

    public String getProperty(String key, String defaultValue) {
        String val = null;
        if("OS".equals(key)) {
            return "RIM";
        }
        if("IMEI".equals(key)) {
            return GPRSInfo.imeiToString(GPRSInfo.getIMEI());
        }
        //#ifdef touch
//#         if ("User-Agent".equals(key)) {
//#             return "Blackberry" + DeviceInfo.getDeviceName() + "/" + DeviceInfo.getPlatformVersion()
//#                     + " Profile/" + System.getProperty("microedition.profiles")
//#                     + " Configuration/"
//#                     + System.getProperty("microedition.configuration")
//#                     + " VendorID/" + Branding.getVendorId();
//#         }
        //#endif
        
        if(midletInstance != null) {
            val = midletInstance.getAppProperty(key);
        } else {
            // For some reason RIM decided to require signing for these API's
            // if you need this functionality and own a signature add a define to
            // signed
            //#ifdef signed
//#             if("MSISDN".equals(key)) {
//#                 return Phone.getDevicePhoneNumber(true);
//#             }
//# 
//#             if(initGetProperty) {
//#                 initGetProperty = false;
//#                 ApplicationDescriptor ad = ApplicationDescriptor.currentApplicationDescriptor();
//#                 if(ad != null) {
//#                     String moduleName = ad.getModuleName();
//# 
//#                     if(moduleName != null) {
//#                         CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
//#                         if(allGroups != null) {
//#                             for (int i = 0; i < allGroups.length; i++) {
//#                                 if (allGroups[i].containsModule(moduleName)) {
//#                                     group = allGroups[i];
//#                                     break;
//#                                 }
//#                             }
//#                         }
//#                     }
//#                 }
//#             }
//#             if(group != null) {
//#                 val = group.getProperty(key);
//#             }
            //#endif
        }
        if(val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * @inheritDoc
     */
    public void playBuiltinSound(String soundIdentifier) {
        if(!playUserSound(soundIdentifier)) {
            // todo...
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

    //#ifdef NewOS
//#     public void copyToClipboard(Object obj) {
//#         if(obj instanceof String || obj instanceof StringBuffer) {
//#             net.rim.device.api.system.Clipboard.getClipboard().put(obj);
//#             super.copyToClipboard(null);
//#         } else {
//#             net.rim.device.api.system.Clipboard.getClipboard().put(null);
//#             super.copyToClipboard(obj);
//#         }
//#     }
//#
//#     public Object getPasteDataFromClipboard() {
//#         Object o = net.rim.device.api.system.Clipboard.getClipboard().get();
//#         if(o != null) {
//#             return o;
//#         }
//#         return super.getPasteDataFromClipboard();
//#     }
//#
//#     public boolean canForceOrientation() {
//#         return true;
//#     }
//#
//#     public void lockOrientation(boolean portrait) {
//#         net.rim.device.api.ui.UiEngineInstance ue;
//#         ue = net.rim.device.api.ui.Ui.getUiEngineInstance();
//#         if(portrait) {
//#             ue.setAcceptableDirections(net.rim.device.api.system.Display.DIRECTION_PORTRAIT);
//#         } else {
//#             ue.setAcceptableDirections(net.rim.device.api.system.Display.DIRECTION_LANDSCAPE);
//#         }
//#     }
    //#endif

    //#ifdef NewOSSigned
//#     public boolean isNativeBrowserComponentSupported() {
//#         return true;
//#     }
//# 
//#     public PeerComponent createBrowserComponent(Object browserComponent) {
//#         synchronized(UiApplication.getEventLock()) {
//#             BrowserField bff = new BrowserField();
//#             final BrowserComponent cmp = (BrowserComponent)browserComponent;
//#             bff.addListener(new BrowserFieldListener() {
//#                 public void documentError(BrowserField browserField, Document document) throws Exception {
//#                     super.documentError(browserField, document);
//#                     cmp.fireWebEvent("onError", new ActionEvent(document));
//#                 }
//# 
//#                 public void documentLoaded(BrowserField browserField, Document document) throws Exception {
//#                     super.documentLoaded(browserField, document);
//#                     cmp.fireWebEvent("onLoad", new ActionEvent(document));
//#                 }
//#             });
//#             return PeerComponent.create(bff);
//#         }
//#     }
//# 
//#     public void setBrowserProperty(PeerComponent browserPeer, String key, Object value) {
//#     }
//# 
//#     public String getBrowserTitle(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             return ((BrowserField)browserPeer.getNativePeer()).getDocumentTitle();
//#         }
//#     }
//# 
//#     public String getBrowserURL(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             return ((BrowserField)browserPeer.getNativePeer()).getDocumentUrl();
//#         }
//#     }
//# 
//#     public void setBrowserURL(PeerComponent browserPeer, String url) {
//#         if(url.startsWith("jar://")) {
//#             //ApplicationDescriptor ad = ApplicationDescriptor.currentApplicationDescriptor();
//#             //url = "cod://" + ad.getModuleName() +  url.substring(6);
//#             //super.setBrowserURL(browserPeer, url);
//#             //url = "local://" + url.substring(6);
//# 
//#             // load from jar:// URL's
//#             try {
//#                 InputStream i = Display.getInstance().getResourceAsStream(getClass(), url.substring(6));
//#                 if(i == null) {
//#                     System.out.println("Local resource not found: " + url);
//#                     return;
//#                 }
//#                 byte[] buffer = new byte[4096];
//#                 ByteArrayOutputStream bo = new ByteArrayOutputStream();
//#                 int size = i.read(buffer);
//#                 while(size > -1) {
//#                     bo.write(buffer, 0, size);
//#                     size = i.read(buffer);
//#                 }
//#                 i.close();
//#                 bo.close();
//#                 String htmlText = new String(bo.toByteArray(), "UTF-8");
//#                 int pos = url.lastIndexOf('/');
//#                 if(pos > 6) {
//#                     url = url.substring(6, pos);
//#                 } else {
//#                     url = "/";
//#                 }
//#                 String baseUrl = "local://" + url;
//#                 setBrowserPage(browserPeer, htmlText, baseUrl);
//#                 return;
//#             } catch (IOException ex) {
//#                 ex.printStackTrace();
//#             }
//#             return;
//#         }
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).requestContent(url);
//#         }
//#     }
//# 
//#     public void browserReload(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).refresh();
//#         }
//#     }
//# 
//#     public boolean browserHasBack(PeerComponent browserPeer) {
//#         return ((BrowserField)browserPeer.getNativePeer()).getHistory().canGoBack();
//#     }
//# 
//#     public boolean browserHasForward(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             return ((BrowserField)browserPeer.getNativePeer()).getHistory().canGoForward();
//#         }
//#     }
//# 
//#     public void browserBack(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).back();
//#         }
//#     }
//# 
//#     public void browserForward(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).forward();
//#         }
//#     }
//# 
//#     public void browserClearHistory(PeerComponent browserPeer) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).getHistory().clearHistory();
//#         }
//#     }
//# 
//#     public void setBrowserPage(PeerComponent browserPeer, String html, String baseUrl) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).displayContent(html, baseUrl);
//#         }
//#     }
//# 
//#     public void browserExecute(PeerComponent browserPeer, String javaScript) {
//#         synchronized(UiApplication.getEventLock()) {
//#             ((BrowserField)browserPeer.getNativePeer()).executeScript(javaScript);
//#         }
//#     }
//# 
//#     public void browserExposeInJavaScript(PeerComponent browserPeer, Object o, String name) {
//#         synchronized(UiApplication.getEventLock()) {
//#             try {
//#                 ((BrowserField) browserPeer.getNativePeer()).extendScriptEngine(name, (Scriptable) o);
//#             } catch (Exception ex) {
//#                 ex.printStackTrace();
//#             }
//#         }
//#     }
    //#endif

    private boolean testedNativeTheme;
    private boolean nativeThemeAvailable;

    public boolean hasNativeTheme() {
        if(!testedNativeTheme) {
            testedNativeTheme = true;
            try {
                InputStream is = getResourceAsStream(getClass(), "/blackberryTheme.res");
                nativeThemeAvailable = is != null;
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return nativeThemeAvailable;
    }

    /**
     * Installs the native theme, this is only applicable if hasNativeTheme() returned true. Notice that this method
     * might replace the DefaultLookAndFeel instance and the default transitions.
     */
    public void installNativeTheme() {
        if(nativeThemeAvailable) {
            try {
                InputStream is = getResourceAsStream(getClass(), "/blackberryTheme.res");
                Resources r = Resources.open(is);
                UIManager.getInstance().setThemeProps(r.getTheme(r.getThemeResourceNames()[0]));
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
