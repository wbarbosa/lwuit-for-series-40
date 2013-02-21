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
import java.util.Vector;
import org.xml.sax.Attributes;

/**
 * Parses the server guides response.
 */
public class NewGuidesParser
    extends Parser {

    private final Vector guides = new Vector();
    private Guide guide = null;

    public NewGuidesParser() {
    }

    public Vector getGuides() {
        return guides;
    }

    /**
     * @see Parser#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    public final void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equals("Product")) {
            guide = new Guide();
        }
    }

    /**
     * @see Parser#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public final void endElement(String uri, String localName, String qName) {
        String chars = getChars();
        
        if (qName.equals("Product")) {
            guides.addElement(guide);
            guide = null;
        }
        else if (qName.equals("Id")) {
            guide.setId(chars);
        }
        else if (qName.equals("Image")) {
            guide.setImageUrl(chars);
        }
        else if (qName.equals("Restorable")) {
            guide.setRestorable("yes".equals(chars));
        }
    }
}
