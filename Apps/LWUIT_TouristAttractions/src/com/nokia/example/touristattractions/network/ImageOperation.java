/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import com.sun.lwuit.Image;

/**
 * Operation for loading images
 */
public final class ImageOperation
    extends NetworkOperation {

    /**
     * Listener interface
     */
    public interface Listener {

        /**
         * Called when a image has been loaded.
         *
         * @param url Url address identifying the image
         * @param Image Image object
         */
        public void imageReceived(String url, Image image);

        /**
         * Called when could not load image.
         */
        public void onNetworkFailure();
    }
    private String url;
    private Listener listener;

    /**
     * Constructor
     *
     * @param listener
     * @param url Url identifying the image to be loaded
     */
    public ImageOperation(Listener listener, String url) {
        this.listener = listener;
        this.url = url;
    }

    /**
     * Starts the operation.
     */
    public final void start() {
        Network.queue(this);
    }

    /**
     * @see NetworkOperation#getUrl()
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Calls the listener, no parsing needed here.
     */
    public final void networkHttpGetResponse(byte[] data) {
        if (data != null) {
            listener.imageReceived(url, Image.createImage(data, 0, data.length));
        }
    }

    public final void onNetworkFailure() {
        listener.onNetworkFailure();
    }
}