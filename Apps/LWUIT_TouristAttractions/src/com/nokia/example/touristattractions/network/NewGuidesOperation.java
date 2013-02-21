/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import com.nokia.example.touristattractions.util.UrlEncoder;
import java.util.Vector;

/**
 * Operation for retrieving new guides.
 */
public final class NewGuidesOperation
    extends NetworkOperation {

    /**
     * Listener interface
     */
    public interface Listener {

        /**
         * Returns the retrieved guides or null in case of an error.
         * 
         * @param comments
         */
        public void guidesReceived(Vector guides);
    }
    private final Listener listener;
    private final String url;
    private final String account;

    /**
     * Cosntructor
     * 
     * @param listener
     * @param url Url from which the guides are being downloaded
     */
    public NewGuidesOperation(Listener listener, String url, String account) {
        this.listener = listener;
        this.url = url;
        this.account = account;
    }

    /**
     * Starts the operation.
     */
    public final void start() {
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
        return url;
    }

    /**
     * @see NetworkOperation#getData() 
     */
    public final String getData() {
        String data = "contentType=xml";
        if (account != null) {
            data += "&account=" + UrlEncoder.encode(account);
        }
        return data;
    }

    /**
     * Parses the server response and calls the listener.   
     * 
     * @param response
     */
    public final void networkHttpPostResponse(String response) {
        try {
            NewGuidesParser handler = new NewGuidesParser();
            parse(response, handler);

            listener.guidesReceived(handler.getGuides());
        }
        catch (ParseException e) {
            listener.guidesReceived(null);
        }
    }
}
