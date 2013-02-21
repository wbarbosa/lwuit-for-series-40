/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Abstract class for network operations.
 */
public abstract class NetworkOperation {

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;

    /**
     * Parses XML using a given event handler.
     * 
     * @param response XML to be parsed
     * @param handler Event handler
     * @throws ParseError
     */
    protected void parse(String response, DefaultHandler handler)
        throws ParseException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputSource source =
                new InputSource(
                new ByteArrayInputStream(response.getBytes()));
            parser.parse(source, handler);
        }
        catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Starts the operation in asynchronous manner.
     */
    public abstract void start();

    /**
     * HTTP method the operation uses.
     * 
     * @return METHOD_GET or METHOD_POST
     */
    public int getMethod() {
        return METHOD_GET;
    }

    /**
     * URL for the operation.
     * 
     * @return URL
     */
    public abstract String getUrl();

    /**
     * Request data.
     * 
     * @return data
     */
    public String getData() {
        return "";
    }

    /**
     * Content type for the request data.
     * 
     * @return content type
     */
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    /**
     * Default implementation for interface callback.
     * 
     * @param response
     */
    public void networkHttpPostResponse(String response) {
        System.out.println("Post response IGNORED");
    }

    /**
     * Default implementation for interface callback.
     * 
     * @param data
     */
    public void networkHttpGetResponse(byte[] data) {
        System.out.println("Get response IGNORED");
    }
    
    public void onNetworkFailure() {
        System.out.println("Network failure");
    }
}
