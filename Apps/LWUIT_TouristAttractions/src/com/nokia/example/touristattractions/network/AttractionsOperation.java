/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.util.UrlEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Operation for retrieving guide attractions.
 */
public final class AttractionsOperation
    extends NetworkOperation {

    /**
     * Listener interface
     */
    public interface Listener {

        /**
         * Returns the retrieved attractions or null in case of an error.
         *
         * @param attractions
         */
        public void attractionsReceived(Vector attractions);
    }
    private Guide guide;
    private Listener listener;

    /**
     * Cosntructor
     *
     * @param listener
     * @param guide Guide from which the attarctions are being downloaded
     */
    public AttractionsOperation(Listener listener, Guide guide) {
        this.listener = listener;
        this.guide = guide;
    }

    /**
     * Starts the operation.
     */
    public final void start() {
        new Thread() {
            public void run() {
                // Try to read guide locally
                try {
                    startLocal();
                }
                catch (IOException e) {
                    // Not available locally. Try on-line.
                    startNetwork();
                }
            }
        }.start();
    }

    private void startLocal()
        throws IOException {
        InputStream is = getClass().getResourceAsStream(guide.getUrl());
        if (is == null) {
            throw new IOException();
        }
        
        StringBuffer sb = new StringBuffer();
        int chars = 0;
        while ((chars = is.read()) != -1) {
            sb.append((char) chars);
        }
        
        // Parse local xml
        parseAttractions(sb.toString());
    }

    private void startNetwork() {
        Network.queue(this);
    }

    /**
     * @see NetworkOperation#getMethod()
     */
    public final int getMethod() {
        return METHOD_POST;
    }

    /**
     * @see NetworkOperation#getUrl()
     */
    public final String getUrl() {
        return guide.getUrl();
    }

    /**
     * @see NetworkOperation#getData()
     */
    public final String getData() {
        return "purchaseTicket=" + UrlEncoder.encode(guide.getPurchaseTicket())
            + "&account=" + UrlEncoder.encode(guide.getAccount());
    }

    /**
     * Parses the server response and calls the listener.
     *
     * @param response
     */
    public final void networkHttpPostResponse(String response) {
        parseAttractions(response);
    }

    private void parseAttractions(String xml) {
        try {
            AttractionsParser handler = new AttractionsParser(guide);
            parse(xml, handler);

            listener.attractionsReceived(handler.getAttractions());
        }
        catch (ParseException e) {
            listener.attractionsReceived(null);
        }
    }
}
