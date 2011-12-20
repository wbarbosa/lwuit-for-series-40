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

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.util.Resources;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.m2g.ExternalResourceHandler;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;
import javax.microedition.m2g.ScalableImage;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGSVGElement;

/**
 * LWUIT Implementation based on game canvas that adds support for drawing
 * SVG Images using JSR 226. To use this implementation class use
 * SVGImplementation.init();
 *
 * @author Shai Almog
 */
public class SVGImplementation extends GameCanvasImplementation {
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private int id = 0;
    private static int idCounter = 0;

    /**
     * @inheritDoc
     */
    public void getRGB(Object nativeImage, int[] arr, int offset, int x, int y, int width, int height) {
        if(nativeImage instanceof ScalableImage) {
            ScalableImage s = (ScalableImage)nativeImage;
            ScalableGraphics svgGraphics = ScalableGraphics.createInstance();
            int w = getImageWidth(nativeImage);
            int h = getImageHeight(nativeImage);
            javax.microedition.lcdui.Image i = javax.microedition.lcdui.Image.createImage(w, h);
            javax.microedition.lcdui.Graphics gr = (javax.microedition.lcdui.Graphics)i.getGraphics();

            svgGraphics.bindTarget(gr);

            svgGraphics.render(0, 0, s);

            svgGraphics.releaseTarget();
            i.getRGB(arr, offset, width, x, y, width, height);
        } else {
            super.getRGB(nativeImage, arr, offset, x, y, width, height);
        }
    }

    /**
     * @inheritDoc
     */
    public Object scale(Object nativeImage, int width, int height) {
        if(nativeImage instanceof ScalableImage) {
            ScalableImage s = (ScalableImage)nativeImage;
            s.setViewportWidth(width);
            s.setViewportHeight(height);
            return s;
        }

        return super.scale(nativeImage, width, height);
    }

    /**
     * @inheritDoc
     */
    public boolean isOpaque(Image lwuitImage, Object nativeImage) {
        if(nativeImage instanceof ScalableImage) {
            return false;
        }
        return super.isOpaque(lwuitImage, nativeImage);
    }

    /**
     * @inheritDoc
     */
    public int getImageWidth(Object i) {
        if(i instanceof ScalableImage) {
            return ((ScalableImage)i).getViewportWidth();
        }
        return super.getImageWidth(i);
    }

    /**
     * @inheritDoc
     */
    public int getImageHeight(Object i) {
        if(i instanceof ScalableImage) {
            return ((ScalableImage)i).getViewportHeight();
        }
        return super.getImageHeight(i);
    }

    /**
     * @inheritDoc
     */
    public boolean animateImage(Object nativeImage, long lastFrame) {
        if(nativeImage instanceof SVGImage) {
            SVGImage im = (SVGImage)nativeImage;
            long currentTime = System.currentTimeMillis();
            im.incrementTime((currentTime - lastFrame) / 1000.0f);
            return true;
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    public void drawImage(Object graphics, Object img, int x, int y) {
        if(img instanceof SVGImage) {
            ScalableGraphics svgGraphics = ScalableGraphics.createInstance();
            javax.microedition.lcdui.Graphics gr = (javax.microedition.lcdui.Graphics)graphics;


            gr.setClip(gr.getClipX(), gr.getClipY(), gr.getClipWidth(), gr.getClipHeight());

            svgGraphics.bindTarget(gr);

            svgGraphics.render(x, y, (SVGImage)img);

            svgGraphics.releaseTarget();
        } else {
            super.drawImage(graphics, img, x, y);
        }
    }

    /**
     * @inheritDoc
     */
    public Object getSVGDocument(Object svgImage) {
        return getSVGElement((SVGImage)svgImage);
    }

    private SVGSVGElement getSVGElement(SVGImage im){
        SVGSVGElement retVal = null;
        Document dom = im.getDocument();
        retVal = (SVGSVGElement)dom.getElementById(this.getClass().getName() + id);
        if(retVal == null){
            retVal = (SVGSVGElement) dom.createElementNS(SVG_NAMESPACE, "svg");
            id = idCounter++;
            retVal.setId(this.getClass().getName() + id);
        }
        return retVal;
    }

    /**
     * @inheritDoc
     */
    public Object rotate(Object image, int degrees) {
        if(image instanceof SVGImage) {
            SVGSVGElement e = getSVGElement((SVGImage)image);
            SVGPoint p = e.getCurrentTranslate();
            p.setX(-getImageWidth(image) / 2);
            p.setY(-getImageHeight(image) / 2);
            e.setCurrentRotate(degrees);
            p.setX(0);
            p.setY(0);
            return image;
        }
        return super.rotate(image, degrees);
    }

    /**
     * @inheritDoc
     */
    public boolean isSVGSupported() {
        return true;
    }

    /**
     * Resolve a reference into the resource url
     */
    private InputStream openResURL(String url) throws IOException {
        int pos = url.indexOf("!", 7);
        Resources r = Resources.open(url.substring(6, pos));
        return r.getData(url.substring(pos + 1));
    }

    class Handler implements ExternalResourceHandler {
        private String baseURL;
        public Handler(String baseURL) {
            this.baseURL = baseURL;
        }
        public void requestResource(ScalableImage i, String a) {
            InputStream s = null;
            try {
                if(a.indexOf(':') > -1) {
                    if(a.startsWith("res:")) {
                        s = openResURL(a);
                    } else {
                        s = Connector.openInputStream(a);
                    }
                    i.requestCompleted(a, s);
                } else {
                    if(baseURL != null && baseURL.indexOf(':') > -1) {
                        String u = baseURL;
                        if(!baseURL.endsWith("/")) {
                            u += "/";
                        }
                        if(u.startsWith("res:")) {
                            s = openResURL(u + a);
                        } else {
                            s = Connector.openInputStream(u + a);
                        }
                        i.requestCompleted(a, s);
                    } else {
                        s = Display.getInstance().getResourceAsStream(getClass(), a);
                        i.requestCompleted(a, s);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    s.close();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    public Object createSVGImage(final String baseURL, byte[] data) throws IOException {
        ScalableImage instance = ScalableImage.createImage(new ByteArrayInputStream(data), new Handler(baseURL));
        return instance;
    }

}
