/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.util.ImageCache;
import com.nokia.example.lwuit.rlinks.util.ImageLoader;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.util.Resources;

/**
 * Thumbnail component
 */
public class Thumbnail
        extends Component {

    private static Image defaultImage;
    private static Image failImage;
    private static Image mask;
    private static Image bevel;
    private ImageCache imageCache;
    private Image thumbnail;
    private Listener listener;
    private String url = "";

    /**
     * An interface for notifying that an thumbnail has been updated.
     */
    public interface Listener {

        void imageLoaded();
    }

    /**
     * Constructor
     * @param url Local or external URL pointing to the image resource
     * @param imageCache Cache to store the thumbnail in
     * @param listener Thumbnail change listener
     */
    public Thumbnail(String url, ImageCache imageCache, Listener listener) {
        super();
        this.url = url;
        this.imageCache = imageCache;
        this.listener = listener;

        setFocusable(false);
        getStyle().setBgTransparency(0);
        setUIID("Label");

        if (defaultImage == null) {
            try {
                Resources res = Resources.open("/resources.res");
                defaultImage = res.getImage("default_thumbnail.png");
                failImage = res.getImage("fail_thumbnail.png");
                mask = res.getImage("thumbnail_mask.png");
                bevel = res.getImage("thumbnail_bevel.png");
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Calculate the preferred size
     * @return The size of the default thumbnail
     */
    protected Dimension calcPreferredSize() {
        Style style = getStyle();
        return new Dimension(defaultImage.getWidth() + style.getPadding(LEFT) + style.getPadding(RIGHT),
                             defaultImage.getHeight() + style.getPadding(TOP) + style.getPadding(BOTTOM));
    }

    /**
     * Paint thumbnail image
     * @param g
     */
    public void paint(Graphics g) {
        Image image = thumbnail;

        if (image == null) {
            image = loadImage();
        }

        Style style = getStyle();
        g.drawImage(image, getX() + style.getPadding(LEFT), getY() + style.getPadding(TOP));
    }

    /**
     * Load image either from cache, locally or online.
     */
    private Image loadImage() {

        Image image = ImageLoader.loadImageOnline(url, new ImageLoader.Listener() {

            public void imageLoaded(final Image image) {
                if (image == null) {
                    imageCache.put(url, failImage);
                    thumbnail = failImage;
                    listener.imageLoaded();
                    return;
                }

                int width = defaultImage.getWidth();
                int height = defaultImage.getHeight();

                Image processed = ImageLoader.scaleImage(image, width, height, true);

                // Mask thumbnail with surround shaped mask
                processed = ImageLoader.applyMask(processed, mask);

                // Draw the bevel edges
                Graphics g = processed.getGraphics();
                g.drawImage(bevel, 0, 0);

                imageCache.put(url, processed);
                thumbnail = processed;
                listener.imageLoaded();
            }
        }, imageCache);

        if (image == null) {
            image = defaultImage;
        }
        thumbnail = image;
        return image;
    }

    protected void deinitialize() {
        super.deinitialize();
        // Remove hard reference to the thumbnail. It's will still be in cache,
        // unless garbage collector deallocates it.
        thumbnail = null;
    }
}
