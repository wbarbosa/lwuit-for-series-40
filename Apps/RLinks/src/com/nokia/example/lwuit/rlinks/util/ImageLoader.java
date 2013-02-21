/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.util;

import com.nokia.example.lwuit.rlinks.network.operation.ImageLoadOperation;
import com.nokia.lwuit.ImageUtils;
import com.sun.lwuit.Image;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility for loading image resources.
 */
public final class ImageLoader {

    /**
     * An interface for notifying that an image has been loaded.
     */
    public interface Listener {

        void imageLoaded(Image image);
    }

    /**
     * Prevent instantiation of this class
     */
    private ImageLoader() {
    }

    /**
     * Loads an image from resources and returns it.
     *
     * Caches all loaded images in hopes of saving some memory.
     *
     * @param imagePath
     * @param cache
     * @return
     * @throws IOException
     */
    public static Image loadImage(final String imagePath, final ImageCache cache)
            throws IOException {
        Image image = null;
        if (cache != null) {
            image = cache.get(imagePath);
        }
        if (image == null) {
            InputStream in = ImageLoader.class.getResourceAsStream(imagePath);
            if (in == null) {
                throw new IOException("Image not found.");
            }
            image = Image.createImage(in);
            if (cache != null) {
                cache.put(imagePath, image);
            }
        }
        return image;
    }

    /**
     * Load an image from resources or network.
     *
     * Caches all loaded images.
     *
     * @param url URL to load image from
     * @param defaultImage A default image which is returned while the image is
     * loaded from network
     * @param listener Listener which is notified when the image is loaded from
     * network
     * @param cache
     * @return The image or while the image is loaded from the network the
     * default image
     */
    public static Image loadImageOnline(final String url, final Listener listener, final ImageCache cache) {
        try {
            return loadImage(url, cache);
        }
        catch (IOException e) {

            new ImageLoadOperation(url, new ImageLoadOperation.Listener() {

                public void imageReceived(String url, byte[] data) {
                    Image image = null;
                    try {
                        image = Image.createImage(data, 0, data.length);
                    }
                    catch (IllegalArgumentException e) {
                    }
                    catch (NullPointerException e) {
                    }

                    if (cache != null) {
                        cache.put(url, image);
                    }
                    listener.imageLoaded(image);
                }
            }).start();
        }
        return null;
    }

    /**
     * Scales image to given size
     * @param source Source image
     * @param width Target width
     * @param height Target height
     * @param preserveAspectRatio Indicates whether to preserve aspect ratio while scaling.
     * When set to true, the image will be scaled to fill the area and then cropped to the given size.
     * @return Image scaled to the target size
     */
    public static Image scaleImage(Image source, int width, int height, boolean preserveAspectRatio) {
        if (source == null || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Cannot scale image with the provided arguments.");
        }

        Image scaled = null;
        if (!preserveAspectRatio) {
            scaled = source.scaled(width, height);
        }
        else if (source.getWidth() > width && source.getHeight() > height) {
            scaled = source.scaledSmallerRatio(width, height);
        }
        else {
            if (source.getWidth() / width < source.getHeight() / height) {
                scaled = source.scaledWidth(width);
            }
            else {
                scaled = source.scaledHeight(height);
            }
        }
        return scaled.subImage((scaled.getWidth() - width) / 2, (scaled.getHeight() - height) / 2,
                               width, height, true);
    }

    /**
     * Applies given mask to image. Uses the alpha channel of the mask image to determine transparency.
     * @param source Image to apply mask to
     * @param mask Image mask
     * @return Masked image
     */
    public static Image applyMask(Image source, Image mask) {
        Image result = null;
        try {
            javax.microedition.lcdui.Image masked = ImageUtils.drawMaskedImage(
                    (javax.microedition.lcdui.Image) source.getImage(),
                    (javax.microedition.lcdui.Image) mask.getImage());
            result = Image.createImage(masked);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Cannot mask image with the provided arguments.");
        }
        return result;
    }
}
