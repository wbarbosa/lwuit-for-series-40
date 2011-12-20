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

package com.sun.lwuit.impl.swing;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.impl.LWUITImplementation;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * An implementation of LWUIT based on Swing
 *
 * @author Shai Almog
 */
public class SwingImplementation extends LWUITImplementation {
    private static int medianFontSize = 11;
    private static int smallFontSize = 9;
    private static int largeFontSize = 14;
    private static int softbuttonCount = 2;
    private static float scale = 1;
    private static boolean tablet;
    private static String DEFAULT_FONT = "Arial-plain-11";
    public static void setFontSize(int medium, int small, int large) {
        medianFontSize = medium;
        smallFontSize = small;
        largeFontSize = large;
        DEFAULT_FONT = "Arial-plain-" + medium;
    }

    private class C extends JComponent implements KeyListener, MouseListener, MouseMotionListener, HierarchyBoundsListener {
        private BufferedImage buffer;
        boolean painted;
        private int bufferedImageType = BufferedImage.TYPE_INT_RGB;
        C() {
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            addHierarchyBoundsListener(this);
            setFocusable(true);
            requestFocus();
        }
        
        public void setBufferedImageType(int bufferedImageType_) {
            if(bufferedImageType_ != bufferedImageType) {
                bufferedImageType = bufferedImageType_;
                buffer = createBufferedImage();
            }
        }

        public int getBufferedImageType() {
            return bufferedImageType;
        }

        public void blit() {
                if(buffer != null) {
                    java.awt.Graphics g = getGraphics();
                    if(g == null) {
                        return;
                    }
                    if(scale != 1) {
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.scale(scale, scale);
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
                    if(scale != 1) {
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.scale(scale, scale);
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
                if(scale != 1) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.scale(scale, scale);
                }
                g.drawImage(buffer, 0, 0, null);
                java.awt.Dimension d = getPreferredSize();
                if(buffer.getWidth() != d.width || buffer.getHeight() != d.height) {
                    buffer = createBufferedImage();
                }
            }
        }
        
        public FontRenderContext getFRC() {
            return getGraphics2D().getFontRenderContext();
        }
        
        public Graphics2D getGraphics2D() {
            if(buffer == null) {
                buffer = createBufferedImage();
            }
            return buffer.createGraphics();
        }

        private BufferedImage createBufferedImage() {
            return new BufferedImage(Math.max(20, getPreferredSize().width), Math.max(20, getPreferredSize().height), bufferedImageType);
        }
        
        public void validate() {
            java.awt.Dimension d = getPreferredSize();
            if(buffer == null || d.width != buffer.getWidth() || d.height != buffer.getHeight()) {
                buffer = createBufferedImage();
            }
            Form current = getCurrentForm();
            if(current == null) {
                return;
            }
            if(d.width != current.getWidth() || d.height != current.getHeight()) {
                current.setSize(new com.sun.lwuit.geom.Dimension(getPreferredSize().width, getPreferredSize().height));
                current.revalidate();
            }             
        }
        
        private int getCode(java.awt.event.KeyEvent evt) {
            switch(evt.getKeyCode()) {
                case KeyEvent.VK_UP:
                    return com.sun.lwuit.Display.getInstance().getKeyCode(com.sun.lwuit.Display.GAME_UP);
                case KeyEvent.VK_DOWN:
                    return com.sun.lwuit.Display.getInstance().getKeyCode(com.sun.lwuit.Display.GAME_DOWN);
                case KeyEvent.VK_LEFT:
                    return com.sun.lwuit.Display.getInstance().getKeyCode(com.sun.lwuit.Display.GAME_LEFT);
                case KeyEvent.VK_RIGHT:
                    return com.sun.lwuit.Display.getInstance().getKeyCode(com.sun.lwuit.Display.GAME_RIGHT);
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_ENTER:
                    return com.sun.lwuit.Display.getInstance().getKeyCode(com.sun.lwuit.Display.GAME_FIRE);
            }
            return evt.getKeyCode();
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            SwingImplementation.this.keyPressed(getCode(e));
        }

        public void keyReleased(KeyEvent e) {
            SwingImplementation.this.keyReleased(getCode(e));
        }

        public void mouseClicked(MouseEvent e) {
        }

        private int scaleCoordinate(int coordinate) {
            if(scale != 1) {
                return (int) ( ((float)coordinate) * scale );
            }
            return coordinate;
        }

        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                SwingImplementation.this.pointerPressed(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
                requestFocus();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                SwingImplementation.this.pointerReleased(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                SwingImplementation.this.pointerDragged(scaleCoordinate(e.getX()), scaleCoordinate(e.getY()));
            }
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void ancestorMoved(HierarchyEvent e) {
        }

        public void ancestorResized(HierarchyEvent e) {
            SwingImplementation.this.sizeChanged(getPreferredSize().width, getPreferredSize().height);
        }
    }
    
    private static SwingImplementation instance;
    private C canvas;

    /**
     * The currently edited text component that will be updated after the dismissal
     * of the text box
     */
    private Component currentTextComponent;

    public SwingImplementation() {
        canvas = new C();
    }

    public static void scale(float size) {
        scale = size;
    }

    public void paintDirty() {
        super.paintDirty();
    }

    public static void setSoftButtonCount(int i) {
        softbuttonCount = i;
    }

    public void setImplementationSize(int w, int h) {
        canvas.setPreferredSize(new java.awt.Dimension(w, h));
        canvas.setMinimumSize(new java.awt.Dimension(w, h));
        canvas.setMaximumSize(new java.awt.Dimension(w, h));
        if(canvas.getParent() != null) {
            canvas.getParent().invalidate();
            canvas.getParent().validate();
        }
        canvas.revalidate();
        sizeChanged(w, h);
    }
    
    private static Class clsInstance;
    public static void setClassLoader(Class cls) {
        clsInstance = cls;
    }
    public static Class getClassLoader() {
        return clsInstance;
    }


    public void setBufferedImageType(int bufferedImageType_) {
        canvas.setBufferedImageType(bufferedImageType_);
    }

    public int getBufferedImageType() {
        return canvas.getBufferedImageType();
    }

    public JComponent getJComponent() {
        return canvas;
    }
    
    public static SwingImplementation getInstance() {
        return instance;
    }
    
    /**
     * @inheritDoc
     */
    public void init(Object m) {
        instance = this;
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
        return Math.max(canvas.getPreferredSize().width, 100);
    }

    /**
     * @inheritDoc
     */
    public int getDisplayHeight() {
        return Math.max(canvas.getPreferredSize().height, 100);
    }

    /**
     * @inheritDoc
     */
    public void editString(Component cmp, int maxSize, int constraint, String text, int keyCode) {
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
        if(nativeImage instanceof SVG) {
            getRGB(((SVG) nativeImage).getImg(), arr, offset, x, y, width, height);
            return;
        }
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
            InputStream i = clsInstance.getResourceAsStream(path);

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
            // prevents a security exception due to a JDK bug which for some stupid reason chooses
            // to create a temporary file in the spi of Image IO
            return ImageIO.read(new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes, offset, len)));
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
        if(i instanceof SVG) {
            return ((SVG) i).getImg().getWidth();
        }
        return ((BufferedImage)i).getWidth();
    }

    /**
     * @inheritDoc
     */
    public int getImageHeight(Object i) {
        if(i instanceof SVG) {
            return ((SVG) i).getImg().getHeight();
        }
        return ((BufferedImage)i).getHeight();
    }

    /**
     * @inheritDoc
     */
    public Object scale(Object nativeImage, int width, int height) {
        if(nativeImage instanceof SVG) {
            return scaleSVG(((SVG)nativeImage), width, height);
        }
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
        return softbuttonCount;
    }

    /**
     * @inheritDoc
     */
    public int[] getSoftkeyCode(int index) {
        if(index == 0) {
            return new int[] {KeyEvent.VK_F1};
        }
        if(index == 1) {
            return new int[] {KeyEvent.VK_F2};
        }
        return null;
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
        case KeyEvent.VK_UP:
            return Display.GAME_UP;
        case KeyEvent.VK_DOWN:
            return Display.GAME_DOWN;
        case KeyEvent.VK_RIGHT:
            return Display.GAME_RIGHT;
        case KeyEvent.VK_LEFT:
            return Display.GAME_LEFT;
        case KeyEvent.VK_SPACE:
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
            return KeyEvent.VK_UP;
        case Display.GAME_DOWN:
            return KeyEvent.VK_DOWN;
        case Display.GAME_RIGHT:
            return KeyEvent.VK_RIGHT;
        case Display.GAME_LEFT:
            return KeyEvent.VK_LEFT;
        case Display.GAME_FIRE:
            return KeyEvent.VK_SPACE;
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
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.setFont(font(font));
    }

    /**
     * @inheritDoc
     */
    public int getClipX(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
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
        Graphics2D nativeGraphics = (Graphics2D)graphics;
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
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            return 0;
        }
        return r.width;
    }

    /**
     * @inheritDoc
     */
    public int getClipHeight(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        if(r == null) {
            return 0;
        }
        return r.height;
    }

    /**
     * @inheritDoc
     */
    public void setClip(Object graphics, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.setClip(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void clipRect(Object graphics, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.clipRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawLine(Object graphics, int x1, int y1, int x2, int y2) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawLine(x1, y1, x2, y2);
    }

    /**
     * @inheritDoc
     */
    public void fillRect(Object graphics, int x, int y, int w, int h) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
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
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawRect(x, y, width, height);
    }

    /**
     * @inheritDoc
     */
    public void drawRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillRoundRect(Object graphics, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * @inheritDoc
     */
    public void fillArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void drawArc(Object graphics, int x, int y, int width, int height, int startAngle, int arcAngle) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * @inheritDoc
     */
    public void setColor(Object graphics, int RGB) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.setColor(new Color(RGB));
    }

    /**
     * @inheritDoc
     */
    public int getColor(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        return nativeGraphics.getColor().getRGB();
    }

    /**
     * @inheritDoc
     */
    public void setAlpha(Object graphics, int alpha) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        float a = ((float)alpha) / 255.0f;
        nativeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
    }
    
    /**
     * @inheritDoc
     */
    public int getAlpha(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
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
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawString(str, x, y + nativeGraphics.getFontMetrics().getAscent());
    }

    /**
     * @inheritDoc
     */
    public void drawImage(Object graphics, Object img, int x, int y) {
        if(img instanceof SVG) {
            drawSVGImage(graphics, (SVG)img, x, y);
        } else {
            Graphics2D nativeGraphics = (Graphics2D)graphics;
            nativeGraphics.drawImage((BufferedImage)img, x, y, null);
        }
    }


    /**
     * @inheritDoc
     */
    public void fillTriangle(Object graphics, int x1, int y1, int x2, int y2, int x3, int y3) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.fillPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
    }

    private BufferedImage cache;

    /**
     * @inheritDoc
     */
    public void drawRGB(Object graphics, int[] rgbData, int offset, int x, int y, int w, int h, boolean processAlpha) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
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
        return canvas.getGraphics2D();
    }

    /**
     * @inheritDoc
     */
    public Object getNativeGraphics(Object image) {
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
            return medianFontSize;
        }
        if(nativeFont instanceof int[]) {
            return ((int[])nativeFont)[2];
        }
        final int large = medianFontSize + 2;
        final int small = medianFontSize - 2;
        int size = font(nativeFont).getSize();
        if(size == large) {
            return Font.SIZE_LARGE;
        }
        if(size == small) {
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
        return java.awt.Font.decode(lookup);
    }

    /**
     * @inheritDoc
     */
    /*public void fillRadialGradient(Object graphics, int startColor, int endColor, int x, int y, int width, int height) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        Paint p = nativeGraphics.getPaint();
        nativeGraphics.setPaint(new GradientPaint(x + width / 2, y + height / 2, new Color(startColor), x, y, new Color(endColor)));
        nativeGraphics.fillArc(x, y, width, height, 0, 360);
        nativeGraphics.setPaint(p);
    }*/
    
    /**
     * @inheritDoc
     */
    /*public void fillLinearGradient(Object graphics, int startColor, int endColor, int x, int y, int width, int height, boolean horizontal) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        Paint p = nativeGraphics.getPaint();
        nativeGraphics.setPaint(new GradientPaint(x + width / 2, y + height / 2, new Color(startColor), x, y, new Color(endColor)));
        nativeGraphics.fillRect(x, y, width, height);
        nativeGraphics.setPaint(p);
    }*/
    
    /**
     * @inheritDoc
     */
    public void fillPolygon(Object graphics, int[] xPoints, int[] yPoints, int nPoints) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.fillPolygon(xPoints, yPoints, nPoints);
    }
    
    /**
     * @inheritDoc
     */
    public void drawPolygon(Object graphics, int[] xPoints, int[] yPoints, int nPoints) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawPolygon(xPoints, yPoints, nPoints);
    }


    @Override
    public boolean animateImage(Object nativeImage, long lastFrame) {
        return false;
    }

    private SVG scaleSVG(SVG s, int w, int h) {
        try {
            SVG dest = new SVG();
            dest.setBaseURL(s.getBaseURL());
            dest.setSvgData(s.getSvgData());
            org.apache.batik.transcoder.image.PNGTranscoder t = new org.apache.batik.transcoder.image.PNGTranscoder();
            org.apache.batik.transcoder.TranscoderInput i = new org.apache.batik.transcoder.TranscoderInput(new ByteArrayInputStream(s.getSvgData()));
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            org.apache.batik.transcoder.TranscoderOutput o = new org.apache.batik.transcoder.TranscoderOutput(bo);
            t.addTranscodingHint(org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_WIDTH, new Float(w));
            t.addTranscodingHint(org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_HEIGHT, new Float(h));
            t.transcode(i, o);
            bo.close();
            dest.setImg(ImageIO.read(new ByteArrayInputStream(bo.toByteArray())));
            return dest;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createSVGImage(String baseURL, byte[] data) throws IOException {
        try {
            SVG s = new SVG();
            s.setBaseURL(baseURL);
            s.setSvgData(data);
            org.apache.batik.transcoder.image.PNGTranscoder t = new org.apache.batik.transcoder.image.PNGTranscoder();
            org.apache.batik.transcoder.TranscoderInput i = new org.apache.batik.transcoder.TranscoderInput(new ByteArrayInputStream(s.getSvgData()));
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            org.apache.batik.transcoder.TranscoderOutput o = new org.apache.batik.transcoder.TranscoderOutput(bo);
            t.transcode(i, o);
            bo.close();
            s.setImg(ImageIO.read(new ByteArrayInputStream(bo.toByteArray())));
            return s;
        } catch (org.apache.batik.transcoder.TranscoderException ex) {
            ex.printStackTrace();
            throw new IOException(ex);
        }
    }

    @Override
    public boolean isSVGSupported() {
        return true;
    }

    private void drawSVGImage(Object graphics, SVG img, int x, int y) {
        drawImage(graphics,img.getImg(), x, y);
    }

    /**
     * @inheritDoc
     */
    public Object getSVGDocument(Object svgImage) {
        return svgImage;
    }

    public boolean isAffineSupported() {
        return true;
    }

    public void resetAffine(Object nativeGraphics) {
        Graphics2D g = (Graphics2D)nativeGraphics;
        g.setTransform(new AffineTransform());
    }

    public void scale(Object nativeGraphics, float x, float y) {
        Graphics2D g = (Graphics2D)nativeGraphics;
        g.scale(x, y);
    }

    public void rotate(Object nativeGraphics, float angle) {
        Graphics2D g = (Graphics2D)nativeGraphics;
        g.rotate(angle);
    }

    public void shear(Object nativeGraphics, float x, float y) {
        Graphics2D g = (Graphics2D)nativeGraphics;
        g.shear(x, y);
    }

    public boolean isTablet() {
        return tablet;
    }

    public static void setTablet(boolean b) {
        tablet = b;
    }
}
