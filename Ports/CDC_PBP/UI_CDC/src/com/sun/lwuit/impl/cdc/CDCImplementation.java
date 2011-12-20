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
package com.sun.lwuit.impl.cdc;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.impl.LWUITImplementation;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.StringTokenizer;

/**
 * An implementation of LWUIT to CDC PBP devices
 *
 * @author Chen Fishbein
 */
public class CDCImplementation extends LWUITImplementation {
    private static int id = 1;
    private static java.awt.MediaTracker tracker;
    private boolean systemDoubleBuffer = false;
    private int[] menuKey = new int[] {java.awt.event.KeyEvent.VK_F1};
    private final boolean useBufferedImages = true;
    private GraphicsConfiguration gc;
    private Runnable[] flushGraphicsCallbacks;

    private class C extends java.awt.Container implements KeyListener, MouseListener, MouseMotionListener {
        private BufferedImage buffer;
        C() {
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            setFocusable(true);
            requestFocus();
            class ResizeListener extends ComponentAdapter implements Runnable {
                public void componentResized(ComponentEvent e) {
                    if (getCurrentForm() != null) {
                        Display.getInstance().callSerially(this);
                    }
                }
                public void run() {
                    Form form = getCurrentForm();
                    int w = getWidth();
                    int h = getHeight();
                    if (form.getWidth() != w || form.getHeight() != h) {
                        form.setSize(new com.sun.lwuit.geom.Dimension(w, h));
                        form.revalidate();
                    }
                }
            }
            addComponentListener(new ResizeListener());
        }

        void flushGraphics() {
            flushGraphics(0, 0, getWidth(), getHeight());
        }

        void flushGraphics(int x, int y, int width, int height) {
            java.awt.Graphics g = getGraphics();
            g.setClip(x, y, width, height);
            paintBuffer(g);
            g.dispose();
        }

        public void paint(java.awt.Graphics g) {
            if (systemDoubleBuffer){
                super.paint(g);
            } else {
                paintBuffer(g);
            }
        }

        private void paintBuffer(java.awt.Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.Src);
            if(buffer != null){
                g.drawImage(buffer, 0, 0, null);
            }
        }

        public Graphics2D getGraphics2D() {
            if(systemDoubleBuffer){
                return (Graphics2D)super.getGraphics();
            }
            if(buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
                buffer = createBufferedImage();
            }
            return buffer.createGraphics();
        }

        private BufferedImage createBufferedImage() {
            if (!systemDoubleBuffer){
                int w = Math.max(20, getWidth());
                int h = Math.max(20, getHeight());
                return (BufferedImage) createMutableImage(w, h, 0xffffffff);
            }
            return null;
        }

        public void validate() {
            int w = getWidth();
            int h = getHeight();
            if (!systemDoubleBuffer) {
                if (buffer == null
                        || w != buffer.getWidth() || h != buffer.getHeight()) {
                    buffer = createBufferedImage();
                }
            }
            Form form = getCurrentForm();
            if (form == null) {
                return;
            }
            if (w != form.getWidth() || h != form.getHeight()) {
                form.setSize(new com.sun.lwuit.geom.Dimension(w, h));
                form.revalidate();
                form.repaint();
                CDCImplementation.this.sizeChanged(w, h);
            }

            form.repaint();
        }


        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            CDCImplementation.this.keyPressed(e.getKeyCode());
        }

        public void keyReleased(KeyEvent e) {
            CDCImplementation.this.keyReleased(e.getKeyCode());
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            CDCImplementation.this.pointerPressed(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {
            CDCImplementation.this.pointerReleased(e.getX(), e.getY());
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            CDCImplementation.this.pointerDragged(e.getX(), e.getY());
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    static class CDCFrame extends Frame {
        public CDCFrame(String title) {
            super(title);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        }

        public void update(java.awt.Graphics g) {            
            paint(g);
        }
    }

    private static CDCImplementation instance;
    private C canvas;

    private static final java.awt.Font DEFAULT_FONT = new java.awt.Font("Arial", java.awt.Font.PLAIN, 19);

    public CDCImplementation() {
        canvas = new C();
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    }


    public static CDCImplementation getInstance() {
        return instance;
    }

    /**
     * @inheritDoc
     */
    public void init(Object m) {
        instance = this;
        Container window;
        if(m instanceof Frame){
            window = (Frame)m;
        }else if (m instanceof Container){
            window = (Container)m;
        }else {
            String title = "LWUIT Application";
            window = new CDCFrame(title);
            window.setSize(window.getToolkit().getScreenSize());
        }

        window.setLayout(null);
        canvas.setBounds(0, 0,
                window.getWidth(),
                window.getHeight());

        window.add(canvas);
        window.addKeyListener(canvas);
        window.addMouseListener(canvas);
        window.addMouseMotionListener(canvas);
        window.setFocusable(true);
        window.setVisible(true);
        window.requestFocus();
        tracker = new java.awt.MediaTracker(canvas);
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
    public void editString(Component cmp, int maxSize, int constraint, String text, int initiatingKeycode) {        
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
        Runnable[] rs = flushGraphicsCallbacks;
        if (rs != null) {
            for (int i = 0; i < rs.length; i++) {
                rs[i].run();
            }
        }
        if (!systemDoubleBuffer) {
            canvas.flushGraphics(x, y, width, height);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * @inheritDoc
     */
    public void flushGraphics() {
        flushGraphics(0, 0, getDisplayWidth(), getDisplayHeight());
    }

    private BufferedImage createTranslucentImage(int width, int height) {
        return gc.createCompatibleImage(width, height);
    }

    /**
     * @inheritDoc
     */
    public void getRGB(Object nativeImage, int[] arr, int offset, int x, int y, int width, int height) {
        if (nativeImage instanceof BufferedImage) {
            ((BufferedImage)nativeImage).getRGB(x, y, width, height, arr, offset, width);
        } else {
            BufferedImage bi = createTranslucentImage(width, height);
            Graphics2D g = bi.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage((Image) nativeImage,
                    0, 0, width, height,
                    x, y, x + width, y + height, null);
            g.dispose();
            bi.getRGB(x, y, width, height, arr, offset, width);
            bi.flush();
        }
    }

    /**
     * @inheritDoc
     */
    public Object createImage(int[] rgb, int width, int height) {
        BufferedImage i = createTranslucentImage(width, height);
        i.setRGB(0, 0, width, height, rgb, 0, width);
        return i;
    }

    /**
     * @inheritDoc
     */
    public Object createImage(String path) throws IOException {
        return createImage(com.sun.lwuit.Image.class.getResourceAsStream(path));
    }


    /**
     * @inheritDoc
     */
    public Object createImage(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[256];
        int len;
        while ((len = is.read(buf)) > -1) {
            out.write(buf, 0, len);
        }

        java.awt.Image im = canvas.getToolkit().createImage(out.toByteArray());
        tracker.addImage(im, id);
        try {
            tracker.waitForID(id++);
            if (tracker.isErrorAny()) {
                System.out.println("Error loading image " + im);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            tracker.removeImage(im, id);
        }
        BufferedImage i = createTranslucentImage(
                im.getWidth(null), im.getHeight(null));
        i.getGraphics().drawImage(im, 0, 0, null);
        return i;
    }

    /**
     * @inheritDoc
     */
    public Object createMutableImage(int width, int height, int fillColor) {
        Image i;
        if (useBufferedImages) {
            i = createTranslucentImage(width, height);
        } else {
            i = canvas.createImage(width, height);
        }
        int a = (fillColor >> 24) & 0xff;
        if(a != 0) {
            Graphics2D g = (Graphics2D) i.getGraphics();
            g.setColor(new Color(fillColor, true));
            g.fillRect(0, 0, width, height);
            g.dispose();
        } else {
            Graphics2D g = (Graphics2D) i.getGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, width, height);
            g.dispose();
        }
        return i;
    }

    /**
     * @inheritDoc
     */
    public boolean isAlphaMutableImageSupported() {
        // it is a requirement of the CDC port to have mutable translucent images
        return true;
    }

    /**
     * @inheritDoc
     */
    public Object createImage(byte[] bytes, int offset, int len) {
        try {
            return createImage(new ByteArrayInputStream(bytes, offset, len));
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
        int w = ((Image) i).getWidth(null);
        return w;
    }

    /**
     * @inheritDoc
     */
    public int getImageHeight(Object i) {
        int h = ((Image) i).getHeight(null);
        return h;
    }

    /**
     * @inheritDoc
     */
    public Object scale(Object nativeImage, int width, int height) {
        Image image = (Image)nativeImage;
        int srcWidth = image.getWidth(null);
        int srcHeight = image.getHeight(null);

        // no need to scale
        if(srcWidth == width && srcHeight == height){
            return image;
        }
        Image dst = canvas.createImage(width, height);
        Graphics2D g = (Graphics2D) dst.getGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return dst;
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
        return 1;
    }

    /**
     * @inheritDoc
     */
    public int[] getSoftkeyCode(int index) {
        int[] code = null;
        switch (index) {
            case 0: code = menuKey; break;
        }
        return code;
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
        int action;
        switch(keyCode) {
            case KeyEvent.VK_UP:
                action = Display.GAME_UP;
                break;
            case KeyEvent.VK_DOWN:
                action = Display.GAME_DOWN;
                break;
            case KeyEvent.VK_RIGHT:
                action = Display.GAME_RIGHT;
                break;
            case KeyEvent.VK_LEFT:
                action = Display.GAME_LEFT;
                break;
            case KeyEvent.VK_ENTER:
                action = Display.GAME_FIRE;
                break;
            default:
                action = 0;
        }
        return action;
    }

    /**
     * @inheritDoc
     */
    public int getKeyCode(int gameAction) {
        int keyCode;
        switch(gameAction) {
            case Display.GAME_UP:
                keyCode = KeyEvent.VK_UP;
                break;
            case Display.GAME_DOWN:
                keyCode = KeyEvent.VK_DOWN;
                break;
            case Display.GAME_RIGHT:
                keyCode = KeyEvent.VK_RIGHT;
                break;
            case Display.GAME_LEFT:
                keyCode = KeyEvent.VK_LEFT;
                break;
             case Display.GAME_FIRE:
                keyCode = KeyEvent.VK_ENTER;
                break;
            default:
                keyCode = 0;
        }
        return gameAction;
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
        int clip = r == null ? 0 : r.x;
        return clip;
    }

    /**
     * @inheritDoc
     */
    public int getClipY(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        int clip = r == null ? 0 : r.y;
        return clip;
    }

    /**
     * @inheritDoc
     */
    public int getClipWidth(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        int clip = r == null ? Short.MAX_VALUE : r.width;
        return clip;
    }

    /**
     * @inheritDoc
     */
    public int getClipHeight(Object graphics) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        java.awt.Rectangle r = nativeGraphics.getClipBounds();
        int clip = r == null ? Short.MAX_VALUE : r.height;
        return clip;
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
        int rgb = nativeGraphics.getColor().getRGB() & 0xffffff;
        return rgb;
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
        int alpha = 255;
        if (c != null && c instanceof AlphaComposite) {
            alpha = (int) (((AlphaComposite) c).getAlpha() * 255.0);
        }
        return alpha;
    }

    /**
     * @inheritDoc
     */
    public void drawString(Object graphics, String str, int x, int y) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawString(str, x, y + nativeGraphics.getFont().getSize());
    }

    /**
     * @inheritDoc
     */
    public void drawImage(Object graphics, Object img, int x, int y) {
        Graphics2D nativeGraphics = (Graphics2D)graphics;
        nativeGraphics.drawImage((Image)img, x, y, null);
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
            cache = createTranslucentImage(w, h);
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
        return ((Image)image).getGraphics();
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
        int w = canvas.getFontMetrics(font(nativeFont)).stringWidth(str);
        return w;
    }

    /**
     * @inheritDoc
     */
    public int charWidth(Object nativeFont, char ch) {
        int w = canvas.getFontMetrics(font(nativeFont)).charWidth(ch);
        return w;
    }

    /**
     * @inheritDoc
     */
    public int getHeight(Object nativeFont) {
        int h = canvas.getFontMetrics(font(nativeFont)).getHeight();
        return h;
    }


    /**
     * @inheritDoc
     */
    public Object createFont(int face, int style, int size) {
        java.awt.Font font = DEFAULT_FONT;
        switch(face) {
            case Font.FACE_MONOSPACE:
                font = java.awt.Font.decode("Monospaced-plain-18");
                break;
            case Font.FACE_PROPORTIONAL:
                font = java.awt.Font.decode("SansSerif-plain-18");
                break;
            case Font.FACE_SYSTEM:
                font = java.awt.Font.decode("Arial-plain-18");
                break;
        }
        int awtStyle = java.awt.Font.PLAIN;
        switch(style) {
            case Font.STYLE_BOLD:
                awtStyle = java.awt.Font.BOLD;
                break;
            case Font.STYLE_ITALIC:
                awtStyle = java.awt.Font.ITALIC;
                break;
            case Font.STYLE_PLAIN:
                awtStyle = java.awt.Font.PLAIN;
                break;
            case Font.STYLE_UNDERLINED:
                // unsupported...
                awtStyle = java.awt.Font.PLAIN;
                break;
            default:
                // probably bold/italic
                awtStyle = java.awt.Font.BOLD | java.awt.Font.ITALIC;
                break;
        }
        switch(size) {
            case Font.SIZE_LARGE:
                font = new java.awt.Font(font.getName(), awtStyle, 20);
                //font.deriveFont(awtStyle, 14);
                break;
            case Font.SIZE_SMALL:
                font = new java.awt.Font(font.getName(), awtStyle, 16);
                break;
            default:
                font = new java.awt.Font(font.getName(), awtStyle, 18);
                break;
        }
        
        return font;
    }

    /**
     * @inheritDoc
     */
    public Object getDefaultFont() {
        Object f = DEFAULT_FONT;
        
        return f;
    }

    /**
     * @inheritDoc
     */
    public int getFace(Object nativeFont) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    public int getSize(Object nativeFont) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    public int getStyle(Object nativeFont) {
        return 0;
    }

    private java.awt.Font font(Object f) {
        if(f == null) {
            return DEFAULT_FONT;
        }
        return (java.awt.Font)f;
    }

    /**
     * @inheritDoc
     */
    public boolean isAntiAliasingSupported() {
        return false;
    }

    /**
     * @inheritDoc
     */
    public boolean isAntiAliasedTextSupported() {
        return false;
    }

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

    public boolean isTrueTypeSupported() {
        return false;
    }

    public Object loadNativeFont(String lookup) {
        Object font = java.awt.Font.decode(lookup);
        
        return font;
    }

    public String[] getFontPlatformNames() {
        String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        
        return names;
    }

    public boolean isLookupFontSupported() {
        return true;
    }

    public boolean isOpaque(com.sun.lwuit.Image lwuitImage, Object nativeImage) {
        return true;
    }


    public synchronized void addFlushGraphicsCallback(Runnable r) {
        if (flushGraphicsCallbacks == null) {
            flushGraphicsCallbacks = new Runnable[] { r };
        } else {
            Runnable[] rs =
                    new Runnable[flushGraphicsCallbacks.length + 1];
            System.arraycopy(flushGraphicsCallbacks, 0,

                    rs, 0, flushGraphicsCallbacks.length);
            rs[flushGraphicsCallbacks.length] = r;
            flushGraphicsCallbacks = rs;
        }
    }

    public void removeFlushGraphicsCallback(Runnable r) {
        if (flushGraphicsCallbacks != null) {
            for (int i = 0; i < flushGraphicsCallbacks.length; i++) {
                if (flushGraphicsCallbacks[i] == r) {
                    if (flushGraphicsCallbacks.length == 1) {
                        flushGraphicsCallbacks = null;
                    } else {
                        Runnable[] rs =
                                new Runnable[flushGraphicsCallbacks.length - 1];
                        System.arraycopy(flushGraphicsCallbacks, 0, rs, 0, i);
                        System.arraycopy(flushGraphicsCallbacks, i + 1,
                                rs, i, rs.length - i);
                        flushGraphicsCallbacks = rs;
                    }
                    return;
                }
            }
        }
    }

    /**
     * Contains pending calls to be executed when the EDT is idle
     */
    private RunnableQueue pendingIdleCalls = new RunnableQueue();
    private boolean edtIdle = false;

    /**
     * Calls the given Runnable object when the event dispatch thread has no
     * events to process.
     *
     * @param r Runnable that will be invoked on the EDT when the EDT
     * has no other work to do
     */
    public void callWhenIdle(Runnable r) {
        pendingIdleCalls.enqueue(r);
    }

    public void edtIdle(boolean idle) {
        edtIdle = idle;
        if (idle) {
            Runnable r;
            while ((r = pendingIdleCalls.dequeue()) != null) {
                r.run();
            }
        }
    }

    /**
     * An expanding circular buffer of Runnable objects.
     */
    private static class RunnableQueue {
        Runnable[] q;
        int first;
        int count;
        synchronized void enqueue(Runnable r) {
            if (q == null) {
                q = new Runnable[] { r } ;
                count = 1;
            } else {
                if (count == q.length) {
                    Runnable[] q_ = new Runnable[count << 1];
                    System.arraycopy(q, first, q_, 0, q.length - first);
                    System.arraycopy(q, 0, q_, q.length - first, first);
                    q = q_;
                    first = 0;
                }
                int next = first + count;
                if (next >= q.length) {
                    next -= q.length;
                }
                q[next] = r;
                count ++;
            }
        }
        synchronized Runnable dequeue() {
            if (count == 0) {
                return null;
            } else {
                Runnable r = q[first];
                first ++;
                if (first >= q.length) {
                    first -= q.length;
                }
                count --;
                return r;
            }
        }
        synchronized boolean isEmpty() {
            return count == 0;
        }
    }

}
