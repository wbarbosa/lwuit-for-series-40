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
package com.sun.lwuit.awtport;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.impl.LWUITImplementation;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * An implementation of LWUIT based on AWT
 *
 * @author Shai Almog
 */
public class AwtImpl extends LWUITImplementation {
    private static int medianFontSize = 15;
    private static int smallFontSize = 11;
    private static int largeFontSize = 19;
    private static boolean useNativeInput = true;
    static final int GAME_KEY_CODE_FIRE = -90;
    static final int GAME_KEY_CODE_UP = -91;
    static final int GAME_KEY_CODE_DOWN = -92;
    static final int GAME_KEY_CODE_LEFT = -93;
    static final int GAME_KEY_CODE_RIGHT = -94;
    private static int softkeyCount = 1;
    private static boolean tablet;
    private static String DEFAULT_FONT = "Arial-plain-11";
    public static void setFontSize(int medium, int small, int large) {
        medianFontSize = medium;
        smallFontSize = small;
        largeFontSize = large;
        DEFAULT_FONT = "Arial-plain-" + medium;
    }

    /**
     * @return the useNativeInput
     */
    public static boolean isUseNativeInput() {
        return useNativeInput;
    }

    /**
     * @param aUseNativeInput the useNativeInput to set
     */
    public static void setUseNativeInput(boolean aUseNativeInput) {
        useNativeInput = aUseNativeInput;
    }

    /**
     * @param aSoftkeyCount the softkeyCount to set
     */
    public static void setSoftkeyCount(int aSoftkeyCount) {
        softkeyCount = aSoftkeyCount;
    }

    private class C extends java.awt.Container implements KeyListener, MouseListener, MouseMotionListener, HierarchyBoundsListener {
        private BufferedImage buffer;
        boolean painted;
        private Graphics2D g2dInstance;
        C() {
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            addHierarchyBoundsListener(this);
            setFocusable(true);
            requestFocus();
        }

        public boolean isDoubleBuffered() {
            return true;
        }

        public boolean isOpaque() {
            return true;
        }

        public void update(java.awt.Graphics g) {
            paint(g);
        }

        public void blit() {
                if(buffer != null) {
                    java.awt.Graphics g = getGraphics();
                    if(g == null) {
                        return;
                    }
                    g.drawImage(buffer, 0, 0, this);
                    java.awt.Dimension d = getSize();
                    if(buffer.getWidth() != d.width || buffer.getHeight() != d.height) {
                        buffer = createBufferedImage();
                    }
                }
        }

        public void blit(int x, int y, int w, int h) {
                if(buffer != null) {
                    java.awt.Graphics g = getGraphics();
                    if(g == null) {
                        return;
                    }
                    g.setClip(x, y, w, h);
                    getGraphics().drawImage(buffer, 0, 0, this);
                    java.awt.Dimension d = getSize();
                    if(buffer.getWidth() != d.width || buffer.getHeight() != d.height) {
                        buffer = createBufferedImage();
                    }
                }
        }

        public void paint(java.awt.Graphics g) {
                if(buffer != null) {
                    getGraphics().drawImage(buffer, 0, 0, this);
                    java.awt.Dimension d = getSize();
                    if(buffer.getWidth() != d.width || buffer.getHeight() != d.height) {
                        buffer = createBufferedImage();
                    }
                }
        }

        public java.awt.Dimension getPreferredSize() {
            Form f = Display.getInstance().getCurrent();
            if(f != null) {
                return new java.awt.Dimension(f.getPreferredW(), f.getPreferredH());
            }
            return new java.awt.Dimension(800, 480);
        }

        public FontRenderContext getFRC() {
            return getGraphics2D().getFontRenderContext();
        }

        public Graphics2D getGraphics2D() {
            if(buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
                buffer = createBufferedImage();
            }
            if(g2dInstance == null) {
                g2dInstance = buffer.createGraphics();
            }
            return g2dInstance;
        }

        private BufferedImage createBufferedImage() {
            g2dInstance = null;
            return new BufferedImage(Math.max(20, getWidth()), Math.max(20, getHeight()), BufferedImage.TYPE_INT_RGB);
        }

        public void validate() {
            super.validate();
            java.awt.Dimension d = getPreferredSize();
            if(buffer == null || d.width != buffer.getWidth() || d.height != buffer.getHeight()) {
                buffer = createBufferedImage();
            }
            Form current = getCurrentForm();
            if(current == null) {
                return;
            }
        }


        private int getCode(java.awt.event.KeyEvent evt) {
            switch(evt.getKeyCode()) {
                case KeyEvent.VK_UP:
                    return GAME_KEY_CODE_UP;
                case KeyEvent.VK_DOWN:
                    return GAME_KEY_CODE_DOWN;
                case KeyEvent.VK_LEFT:
                    return GAME_KEY_CODE_LEFT;
                case KeyEvent.VK_RIGHT:
                    return GAME_KEY_CODE_RIGHT;
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_ENTER:
                    return GAME_KEY_CODE_FIRE;
            }
            return evt.getKeyCode();
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            // block key combos that might generate unreadable events
            if(e.isAltDown() || e.isControlDown() || e.isMetaDown() || e.isAltGraphDown()) {
                return;
            }
            AwtImpl.this.keyPressed(getCode(e));
        }

        public void keyReleased(KeyEvent e) {
            // block key combos that might generate unreadable events
            if(e.isAltDown() || e.isControlDown() || e.isMetaDown() || e.isAltGraphDown()) {
                return;
            }
            AwtImpl.this.keyReleased(getCode(e));
        }

        public void mouseClicked(MouseEvent e) {
            e.consume();
        }

        private int scaleCoordinate(int coordinate) {
            return coordinate;
        }

        public void mousePressed(MouseEvent e) {
            e.consume();
            if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                AwtImpl.this.pointerPressed(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
                requestFocus();
            }
        }

        public void mouseReleased(MouseEvent e) {
            e.consume();
            if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                AwtImpl.this.pointerReleased(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
            }
        }

        public void mouseEntered(MouseEvent e) {
            e.consume();
        }

        public void mouseExited(MouseEvent e) {
            e.consume();
        }

        public void mouseDragged(MouseEvent e) {
            e.consume();
            if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                AwtImpl.this.pointerDragged(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
            }
        }

        public void mouseMoved(MouseEvent e) {
            e.consume();
        }

        public void ancestorMoved(HierarchyEvent e) {
        }

        public void ancestorResized(HierarchyEvent e) {
            AwtImpl.this.sizeChanged(getWidth(), getHeight());
        }

    }

    private C canvas;

    public AwtImpl() {
        canvas = new C();
    }

    public void paintDirty() {
        super.paintDirty();
    }

    /**
     * @inheritDoc
     */
    public void deinitialize() {
        if(canvas.getParent() != null) {
            canvas.getParent().remove(canvas);
        }
    }

    /**
     * @inheritDoc
     */
    public void init(Object m) {
        if(canvas.getParent() != null) {
            canvas.getParent().remove(canvas);
        }
        if(m != null && m instanceof java.awt.Container) {
            java.awt.Container cnt = (java.awt.Container)m;
            if(cnt.getLayout() instanceof java.awt.BorderLayout) {
                 cnt.add(java.awt.BorderLayout.CENTER, canvas);
            } else {
                 cnt.add(canvas);
            }
            
        } else {
            Frame frm = new Frame();
            frm.setUndecorated(true);
            frm.setLayout(new java.awt.BorderLayout());
            frm.add(java.awt.BorderLayout.CENTER, canvas);
            frm.addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    Display.getInstance().exitApplication();
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });
            frm.pack();
            frm.setExtendedState(Frame.MAXIMIZED_BOTH);
            frm.setVisible(true);
        }
        if(useNativeInput) {
            Display.getInstance().setDefaultVirtualKeyboard(null);
        }

        float factor = ((float)getDisplayHeight()) / 480.0f;
        
        // set a reasonable default font size
        setFontSize((int)(15.0f * factor), (int)(11.0f * factor), (int)(19.0f * factor));
    }

    /**
     * @inheritDoc
     */
    public void vibrate(int duration) {
    }

    /**
     * @inheritDoc
     */
    public void flashBacklight(int duration) {
    }

    /**
     * @inheritDoc
     */
    public int getDisplayWidth() {
        int w = canvas.getWidth();
        if(w < 10 && canvas.getParent() != null) {
            return canvas.getParent().getWidth();
        }
        return Math.max(w, 100);
    }

    /**
     * @inheritDoc
     */
    public int getDisplayHeight() {
        int h = canvas.getHeight();
        if(h < 10 && canvas.getParent() != null) {
            return canvas.getParent().getHeight();
        }
        return Math.max(h, 100);
    }

    /**
     * Creates a soft/weak reference to an object that allows it to be collected
     * yet caches it. This method is in the porting layer since CLDC only includes
     * weak references while some platforms include nothing at all and some include
     * the superior soft references.
     *
     * @param o object to cache
     * @return a caching object or null  if caching isn't supported
     */
    public Object createSoftWeakRef(Object o) {
        return new SoftReference(o);
    }

    /**
     * Extracts the hard reference from the soft/weak reference given
     *
     * @param o the reference returned by createSoftWeakRef
     * @return the original object submitted or null
     */
    public Object extractHardRef(Object o) {
        SoftReference w = (SoftReference)o;
        if(w != null) {
            return w.get();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public boolean isNativeInputSupported() {
        return useNativeInput;
    }
    
    /**
     * @inheritDoc
     */
    public void editString(final Component cmp, int maxSize, int constraint, String text, int keyCode) {
        java.awt.TextComponent awtTf;
        if(cmp instanceof com.sun.lwuit.TextField) {
            awtTf = new java.awt.TextField();
        } else {
            awtTf = new java.awt.TextArea("",0 , 0, java.awt.TextArea.SCROLLBARS_NONE);
        }
        final java.awt.TextComponent tf =awtTf;
        if(keyCode > 0) {
            text += ((char)keyCode);
            tf.setText(text);
            tf.setCaretPosition(text.length());
        } else {
            tf.setText(text);
        }
        canvas.add(tf);
        tf.setBounds(cmp.getAbsoluteX(), cmp.getAbsoluteY(), cmp.getWidth(), cmp.getHeight());
        tf.requestFocus();
        class Listener implements ActionListener, FocusListener, KeyListener {
            public void actionPerformed(ActionEvent e) {
                Display.getInstance().onEditingComplete(cmp, tf.getText());
                if(tf instanceof java.awt.TextField) {
                    ((java.awt.TextField)tf).removeActionListener(this);
                }
                tf.removeFocusListener(this);
                canvas.remove(tf);
                canvas.repaint();
            }

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                actionPerformed(null);
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(tf instanceof java.awt.TextField) {
                        actionPerformed(null);
                    } else {
                        if(tf.getCaretPosition() >= tf.getText().length() - 1) {
                            actionPerformed(null);
                        }
                    }
                    return;
                }
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    if(tf instanceof java.awt.TextField) {
                        actionPerformed(null);
                    } else {
                        if(tf.getCaretPosition() <= 2) {
                            actionPerformed(null);    
                        }
                    }
                    return;
                }
            }
        };
        final Listener l = new Listener();
        if(tf instanceof java.awt.TextField) {
            ((java.awt.TextField)tf).addActionListener(l);
        }
        tf.addKeyListener(l);
        tf.addFocusListener(l);
    }

    /**
     * @inheritDoc
     */
    public void saveTextEditingState() {
    }

    /**
     * @inheritDoc
     */
    public void flushGraphics(int x, int y, int width, int height) {
        canvas.blit(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void flushGraphics() {
        canvas.blit();
    }

    /**
     * @inheritDoc
     */
    public void getRGB(Object nativeImage, int[] arr, int offset, int x, int y, int width, int height) {
        ((BufferedImage)nativeImage).getRGB(x, y, width, height, arr, offset, width);
    }

    /**
     * @inheritDoc
     */
    public Object createImage(int[] rgb, int width, int height) {
        BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        i.setRGB(0, 0, width, height, rgb, 0, width);
        return i;
    }

    /**
     * @inheritDoc
     */
    public Object createImage(String path) throws IOException {
        try {
            InputStream i = getClass().getResourceAsStream(path);

            // prevents a security exception due to a JDK bug which for some stupid reason chooses
            // to create a temporary file in the spi of Image IO
            return ImageIO.read(new MemoryCacheImageInputStream(i));
        } catch(Throwable t) {
            t.printStackTrace();
            throw new IOException(t.toString());
        }
    }

    /**
     * @inheritDoc
     */
    public Object createImage(InputStream i) throws IOException {
        try {
            return ImageIO.read(i);
        } catch(Throwable t) {
            t.printStackTrace();
            throw new IOException(t.toString());
        }
    }

    /**
     * @inheritDoc
     */
    public Object createMutableImage(int width, int height, int fillColor) {
        int a = (fillColor >> 24) & 0xff;
        if(a == 0xff) {
            BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = b.createGraphics();
            g.setColor(new Color(fillColor));
            g.fillRect(0, 0, width, height);
            g.dispose();
            return b;
        }
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if(a != 0) {
            Graphics2D g = b.createGraphics();
            g.setColor(new Color(fillColor));
            g.fillRect(0, 0, width, height);
            g.dispose();
        }
        return b;
    }

    /**
     * @inheritDoc
     */
    public boolean isAlphaMutableImageSupported() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public Object createImage(byte[] bytes, int offset, int len) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes, offset, len));
        } catch (IOException ex) {
            // never happens
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    public int getImageWidth(Object i) {
        return ((BufferedImage)i).getWidth();
    }

    /**
     * @inheritDoc
     */
    public int getImageHeight(Object i) {
        return ((BufferedImage)i).getHeight();
    }

    /**
     * @inheritDoc
     */
    public Object scale(Object nativeImage, int width, int height) {
        BufferedImage image = (BufferedImage)nativeImage;
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        // no need to scale
        if(srcWidth == width && srcHeight == height){
            return image;
        }

        int[] currentArray = new int[srcWidth];
        int[] destinationArray = new int[width * height];
        scaleArray(image, srcWidth, srcHeight, height, width, currentArray, destinationArray);

        return createImage(destinationArray, width, height);
    }

    private void scaleArray(BufferedImage currentImage, int srcWidth, int srcHeight, int height, int width, int[] currentArray, int[] destinationArray) {
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

    private static int round(double d) {
        double f = Math.floor(d);
        double c = Math.ceil(d);
        if(c - d < d - f) {
            return (int)c;
        }
        return (int)f;
    }

    /**
     * @inheritDoc
     */
    public Object rotate(Object image, int degrees) {
        int width = getImageWidth(image);
        int height = getImageHeight(image);
        int[] arr = new int[width * height];
        int[] dest = new int[arr.length];
        getRGB(image, arr, 0, 0, 0, width, height);
        int centerX = width / 2;
        int centerY = height / 2;

        double radians = Math.toRadians(-degrees);
        double cosDeg = Math.cos(radians);
        double sinDeg = Math.sin(radians);
        for(int x = 0 ; x < width ; x++) {
            for(int y = 0 ; y < height ; y++) {
                int x2 = round(cosDeg * (x - centerX) - sinDeg * (y - centerY) + centerX);
                int y2 = round(sinDeg * (x - centerX) + cosDeg * (y - centerY) + centerY);
                if(!(x2 < 0 || y2 < 0 || x2 >= width || y2 >= height)) {
                    int destOffset = x2 + y2 * width;
                    if(destOffset >= 0 && destOffset < dest.length) {
                        dest[x + y * width] = arr[destOffset];
                    }
                }
            }
        }
        return createImage(dest, width, height);
    }

    /**
     * @inheritDoc
     */
    public int getSoftkeyCount() {
        return softkeyCount;
    }

    /**
     * @inheritDoc
     */
    public int[] getSoftkeyCode(int index) {
        switch(softkeyCount) {
            case 0:
                return null;
            case 2:
                if(index == 0) {
                    return new int[] {KeyEvent.VK_F1};
                } else {
                    return new int[] {KeyEvent.VK_F2};
                }
            default:
                return new int[] {KeyEvent.VK_F1};
        }
    }

    /**
     * @inheritDoc
     */
    public int getClearKeyCode() {
        return KeyEvent.VK_DELETE;
    }

    /**
     * @inheritDoc
     */
    public int getBackspaceKeyCode() {
        return KeyEvent.VK_BACK_SPACE;
    }

    /**
     * @inheritDoc
     */
    public int getBackKeyCode() {
        return KeyEvent.VK_ESCAPE;
    }

    /**
     * @inheritDoc
     */
    public int getGameAction(int keyCode) {
        switch(keyCode) {
        case GAME_KEY_CODE_UP:
            return Display.GAME_UP;
        case GAME_KEY_CODE_DOWN:
            return Display.GAME_DOWN;
        case GAME_KEY_CODE_RIGHT:
            return Display.GAME_RIGHT;
        case GAME_KEY_CODE_LEFT:
            return Display.GAME_LEFT;
        case GAME_KEY_CODE_FIRE:
            return Display.GAME_FIRE;
        }
        return 0;
    }

    /**
     * @inheritDoc
     */
    public int getKeyCode(int gameAction) {
        switch(gameAction) {
        case Display.GAME_UP:
            return GAME_KEY_CODE_UP;
        case Display.GAME_DOWN:
            return GAME_KEY_CODE_DOWN;
        case Display.GAME_RIGHT:
            return GAME_KEY_CODE_RIGHT;
        case Display.GAME_LEFT:
            return GAME_KEY_CODE_LEFT;
        case Display.GAME_FIRE:
            return GAME_KEY_CODE_FIRE;
        }
        return 0;
    }

    /**
     * @inheritDoc
     */
    public boolean isTouchDevice() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public void setNativeFont(Object graphics, Object font) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.setFont(font(font));
    }

    /**
     * @inheritDoc
     */
    public int getClipX(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            return 0;
        }
        return r.x;
    }

    /**
     * @inheritDoc
     */
    public int getClipY(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            return 0;
        }
        return r.y;
    }

    /**
     * @inheritDoc
     */
    public int getClipWidth(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            if(graphics instanceof NativeScreenGraphics) {
                NativeScreenGraphics ng = (NativeScreenGraphics)graphics;
                if(ng.sourceImage != null) {
                    return ng.sourceImage.getWidth();
                }
            }
            return getDisplayWidth();
        }
        return r.width;
    }

    /**
     * @inheritDoc
     */
    public int getClipHeight(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            if(graphics instanceof NativeScreenGraphics) {
                NativeScreenGraphics ng = (NativeScreenGraphics)graphics;
                if(ng.sourceImage != null) {
                    return ng.sourceImage.getHeight();
                }
            }
            return getDisplayHeight();
        }
        return r.height;
    }

    /**
     * @inheritDoc
     */
    public void setClip(Object graphics, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.setClip(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void clipRect(Object graphics, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.clipRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawLine(Object graphics, int x1, int y1, int x2, int y2) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawLine(x1, y1, x2, y2);
    }

    /**
     * @inheritDoc
     */
    public void fillRect(Object graphics, int x, int y, int w, int h) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.fillRect(x, y, w, h);
    }

    /**
     * @inheritDoc
     */
    public boolean isAlphaGlobal() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public void drawRect(Object graphics, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void drawArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void setColor(Object graphics, int RGB) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.setColor(new Color(RGB));
    }

    /**
     * @inheritDoc
     */
    public int getColor(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        return nativeGraphics.getColor().getRGB();
    }

    /**
     * @inheritDoc
     */
    public void setAlpha(Object graphics, int alpha) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        float a = ((float)alpha) / 255.0f;
        nativeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
    }

    /**
     * @inheritDoc
     */
    public int getAlpha(Object graphics) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        Object c = nativeGraphics.getComposite();
        if(c != null && c instanceof AlphaComposite) {
            return (int)(((AlphaComposite)c).getAlpha() * 255);
        }
        return 255;
    }

    /**
     * @inheritDoc
     */
    public void drawString(Object graphics, String str, int x, int y) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawString(str, x, y + nativeGraphics.getFontMetrics().getAscent());
    }

    /**
     * @inheritDoc
     */
    public void drawImage(Object graphics, Object img, int x, int y) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawImage((BufferedImage)img, x, y, null);
    }


    /**
     * @inheritDoc
     */
    public void fillTriangle(Object graphics, int x1, int y1, int x2, int y2, int x3, int y3) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.fillPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
    }

    private BufferedImage cache;

    /**
     * @inheritDoc
     */
    public void drawRGB(Object graphics, int[] rgbData, int offset, int x, int y, int w, int h, boolean processAlpha) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        if(cache == null || cache.getWidth() != w || cache.getHeight() != h) {
            cache = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }
        cache.setRGB(0, 0, w, h, rgbData, offset, w);
        nativeGraphics.drawImage(cache, x, y, null);
    }

    /**
     * @inheritDoc
     */
    public Object getNativeGraphics() {
        return new NativeScreenGraphics();
    }

    /**
     * @inheritDoc
     */
    public Object getNativeGraphics(Object image) {
        /*NativeScreenGraphics n = new NativeScreenGraphics();
        n.sourceImage = (BufferedImage)image;
        return n;*/
        return ((BufferedImage)image).getGraphics();
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
        return stringWidth(nativeFont, new String(ch, offset, length));
    }

    /**
     * @inheritDoc
     */
    public int stringWidth(Object nativeFont, String str) {
        return (int)Math.ceil(font(nativeFont).getStringBounds(str, canvas.getFRC()).getWidth());
    }

    /**
     * @inheritDoc
     */
    public int charWidth(Object nativeFont, char ch) {
        return (int)Math.ceil(font(nativeFont).getStringBounds("" + ch, canvas.getFRC()).getWidth());
    }

    /**
     * @inheritDoc
     */
    public int getHeight(Object nativeFont) {
        return font(nativeFont).getSize() + 1;
    }

    /**
     * @inheritDoc
     */
    public Object createFont(int face, int style, int size) {
        return new int[] {face, style, size};
    }

    private java.awt.Font createAWTFont(int[] i) {
        int face = i[0];
        int style = i[1];
        int size = i[2];
        String fontName;
        switch(face) {
            case Font.FACE_MONOSPACE:
                fontName = "Monospaced-";
                break;
            case Font.FACE_PROPORTIONAL:
                fontName = "SansSerif-";
                break;
            default: //Font.FACE_SYSTEM:
                fontName = "Arial-";
                break;
        }
        switch(style) {
            case Font.STYLE_BOLD:
                fontName += "bold-";
                break;
            case Font.STYLE_ITALIC:
                fontName += "italic-";
                break;
            case Font.STYLE_PLAIN:
                fontName += "plain-";
                break;
            case Font.STYLE_UNDERLINED:
                // unsupported...
                fontName += "plain-";
                break;
            default:
                // probably bold/italic
                fontName += "bold-";
                break;
        }
        switch(size) {
            case Font.SIZE_LARGE:
                fontName += largeFontSize;
                break;
            case Font.SIZE_SMALL:
                fontName += smallFontSize;
                break;
            default:
                fontName += medianFontSize;
                break;
        }
        return java.awt.Font.decode(fontName);
    }

    /**
     * @inheritDoc
     */
    public Object getDefaultFont() {
        return DEFAULT_FONT;
    }

    /**
     * @inheritDoc
     */
    public int getFace(Object nativeFont) {
        if(font(nativeFont).getFamily().equals("Monospaced")) {
            return Font.FACE_MONOSPACE;
        }
        if(font(nativeFont).getFamily().equals("SansSerif")) {
            return Font.FACE_PROPORTIONAL;
        }
        if(font(nativeFont).getFamily().equals("Arial")) {
            return Font.FACE_SYSTEM;
        }
        return Font.FACE_SYSTEM;
    }

    /**
     * @inheritDoc
     */
    public int getSize(Object nativeFont) {
        if(nativeFont == null) {
            return Font.SIZE_MEDIUM;
        }
        if(nativeFont instanceof int[]) {
            return ((int[])nativeFont)[2];
        }
        int size = font(nativeFont).getSize();
        if(size == largeFontSize) {
            return Font.SIZE_LARGE;
        }
        if(size == smallFontSize) {
            return Font.SIZE_SMALL;
        }
        return Font.SIZE_MEDIUM;
    }

    /**
     * @inheritDoc
     */
    public int getStyle(Object nativeFont) {
        if(font(nativeFont).isBold()) {
            if(font(nativeFont).isItalic()) {
                return Font.STYLE_BOLD | Font.STYLE_ITALIC;
            } else {
                return Font.STYLE_BOLD;
            }
        }
        if(font(nativeFont).isItalic()) {
            return Font.STYLE_ITALIC;
        }
        return Font.STYLE_PLAIN;
    }

    private java.awt.Font font(Object f) {
        if(f == null) {
            return java.awt.Font.decode(DEFAULT_FONT);
        }
        // for bitmap fonts
        if(f instanceof java.awt.Font) {
            return (java.awt.Font)f;
        }
        return createAWTFont((int[])f);
    }

    /**
     * @inheritDoc
     */
    public Object loadNativeFont(String lookup) {
        return java.awt.Font.decode(lookup.split(";")[0]);
   }

    /**
     * @inheritDoc
     */
    public void fillPolygon(Object graphics, int[] xPoints, int[] yPoints, int nPoints) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.fillPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * @inheritDoc
     */
    public void drawPolygon(Object graphics, int[] xPoints, int[] yPoints, int nPoints) {
        Graphics2D nativeGraphics = getGraphics(graphics);
        nativeGraphics.drawPolygon(xPoints, yPoints, nPoints);
    }


    @Override
    public boolean animateImage(Object nativeImage, long lastFrame) {
        return false;
    }


    @Override
    public Object createSVGImage(String baseURL, byte[] data) throws IOException {
        return null;
    }

    @Override
    public boolean isSVGSupported() {
        return false;
    }

    /**
     * @inheritDoc
     */
    public Object getSVGDocument(Object svgImage) {
        return svgImage;
    }

    /**
     * @inheritDoc
     */
    public void exitApplication() {
        try {
            System.exit(0);
        } catch(Throwable t) {
            System.out.println("Can't exit from applet");
        }
    }

    /**
     * @inheritDoc
     */
    public String getProperty(String key, String defaultValue) {
        if("OS".equals(key)) {
            return "SE";
        }
        String s = System.getProperty(key);
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
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Graphics2D getGraphics(Object nativeG) {
        if(nativeG instanceof Graphics2D) {
            return (Graphics2D)nativeG;
        }
        NativeScreenGraphics ng = (NativeScreenGraphics)nativeG;
        if(ng.sourceImage != null) {
            return ng.sourceImage.createGraphics();
        }
        return canvas.getGraphics2D();
    }

    /**
     * @inheritDoc
     */
    protected void playNativeBuiltinSound(Object data) {
        Toolkit.getDefaultToolkit().beep();
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
        return new AudioPlayer(uri, onCompletion);
    }

    /**
     * @inheritDoc
     */
    public Object createAudio(InputStream stream, String mimeType, Runnable onCompletion) throws IOException {
        return new AudioPlayer(stream, onCompletion);
    }

    /**
     * @inheritDoc
     */
    public void cleanupAudio(Object handle) {
        ((AudioPlayer)handle).cleanupAudio();
    }

    /**
     * @inheritDoc
     */
    public void playAudio(Object handle) {
        ((AudioPlayer)handle).playAudio();
    }

    /**
     * @inheritDoc
     */
    public void pauseAudio(Object handle) {
        ((AudioPlayer)handle).pauseAudio();
    }

    /**
     * @inheritDoc
     */
    public int getAudioTime(Object handle) {
        return ((AudioPlayer)handle).getAudioTime();
    }

    /**
     * @inheritDoc
     */
    public void setAudioTime(Object handle, int time) {
        ((AudioPlayer)handle).setAudioTime(time);
    }

    /**
     * @inheritDoc
     */
    public int getAudioDuration(Object handle) {
        return ((AudioPlayer)handle).getAudioDuration();
    }

    /**
     * @inheritDoc
     */
    public void setVolume(int vol) {
    }

    /**
     * @inheritDoc
     */
    public int getVolume() {
        return 100;
    }


    static class AudioPlayer {
        private Clip c;
        private Runnable onCompletion;

        public AudioPlayer(String uri, Runnable onCompletion) throws IOException {
            this.onCompletion = onCompletion;
            AudioInputStream is = null;
            try {
                try {
                    is = AudioSystem.getAudioInputStream(new URL(uri));
                } catch(MalformedURLException mal) {
                    is = AudioSystem.getAudioInputStream(new File(uri));
                }
                is = decode(is);
                c = AudioSystem.getClip();
                c.open(is);
            } catch(IOException ioErr) {
                throw ioErr;
            } catch(RuntimeException re) {
                throw re;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } 
        }

        private AudioInputStream decode(AudioInputStream in) {
                AudioInputStream din = null;
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                                                              baseFormat.getSampleRate(),
                                                                                              16,
                                                                                              baseFormat.getChannels(),
                                                                                              baseFormat.getChannels() * 2,
                                                                                              baseFormat.getSampleRate(),
                                                                                              false);
                return AudioSystem.getAudioInputStream(decodedFormat, in);
        }

        public AudioPlayer(InputStream stream, Runnable onCompletion) throws IOException {
            this.onCompletion = onCompletion;
            try {
                AudioInputStream is = AudioSystem.getAudioInputStream(stream);
                is = decode(is);
                c = AudioSystem.getClip();
                c.open(is);
            } catch(IOException ioErr) {
                throw ioErr;
            } catch(RuntimeException re) {
                throw re;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        public void cleanupAudio() {
            c.close();
        }

        public void playAudio() {
            c.start();
        }

        public void pauseAudio() {
            c.stop();
        }

        public int getAudioTime() {
            return (int)(c.getMicrosecondPosition()  / 1000);
        }

        public void setAudioTime(int time) {
            if(c.isRunning()) {
                c.stop();
                c.setMicrosecondPosition(time * 1000);
                c.start();
            } else {
                c.setMicrosecondPosition(time * 1000);
            }
        }

        public int getAudioDuration() {
            return (int)(c.getMicrosecondLength() / 1000);
        }

        public void setVolume(int vol) {
            // ???
        }

        public int getVolume() {
            return (int)(c.getLevel() * 100);
        }
    }

    private class NativeScreenGraphics {
        BufferedImage sourceImage;
        Graphics2D cachedGraphics;
    }

    public boolean isAffineSupported() {
        return true;
    }

    public void resetAffine(Object nativeGraphics) {
        Graphics2D g = getGraphics(nativeGraphics);
        g.setTransform(new AffineTransform());
    }

    public void scale(Object nativeGraphics, float x, float y) {
        Graphics2D g = getGraphics(nativeGraphics);
        g.scale(x, y);
    }

    public void rotate(Object nativeGraphics, float angle) {
        Graphics2D g = getGraphics(nativeGraphics);
        g.rotate(angle);
    }

    public void shear(Object nativeGraphics, float x, float y) {
        Graphics2D g = getGraphics(nativeGraphics);
        g.shear(x, y);
    }

    public boolean isTablet() {
        return tablet;
    }

    public static void setTablet(boolean b) {
        tablet = b;
    }
}
