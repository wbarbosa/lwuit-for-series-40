/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser abstract class to be extended by specific parsers.
 * Based on the default SAX parser event handler.
 */
public abstract class Parser
    extends DefaultHandler {

    private StringBuffer chars = new StringBuffer();

    /**
     * Event handler for the start of an XML element. 
     * Clears the content string buffer so that a new string can be stored.
     * @see DefaultHandler#startElement(java.lang.String, java.lang.String, 
     * java.lang.String, org.xml.sax.Attributes) 
     */
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        chars = new StringBuffer();
    }

    /**
     * Character callback for content characters. Data is appended to the
     * string buffer.
     * @see DefaultHandler#characters(char[], int, int) 
     */
    public final void characters(char[] ch, int start, int length) {
        chars.append(ch, start, length);
    }

    /**
     * Retrieves the last content string and clears the string buffer.
     * @return content string
     */
    protected final String getChars() {
        // For some reason there is some extra whitespace around 
        // the characters sometimes. Trim those out.
        String c = chars.toString().trim();
        chars = new StringBuffer();
        return c;
    }
}
