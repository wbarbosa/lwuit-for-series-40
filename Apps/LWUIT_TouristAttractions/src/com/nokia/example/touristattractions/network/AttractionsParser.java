/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.models.Guide;
import java.util.Vector;
import org.xml.sax.Attributes;

/**
 * Parses the server attractions response.
 */
public final class AttractionsParser
    extends Parser {

    private final Guide guide;
    private final Vector attractions = new Vector();
    private Attraction attraction = null;

    /**
     * Constructor.
     * @param guide 
     */
    public AttractionsParser(Guide guide) {
        this.guide = guide;
    }

    public Vector getAttractions() {
        return attractions;
    }

    /**
     * @see Parser#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes) 
     */
    public final void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equals("guide")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                // Get names and values for each attribute
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);
                
                if (name.equals("country")) {
                    guide.setCountry(value);
                }
                else if (name.equals("latitude")) {
                    guide.setLatitude(value);
                }
                else if (name.equals("longitude")) {
                    guide.setLongitude(value);
                }
                else if (name.equals("urlPrefix")) {
                    guide.setUrlPrefix(value);
                }
            }
        }
        else if (qName.equals("attraction")) {
            attraction = new Attraction();
            attraction.setGuide(guide);
            
            // Process each attribute
            for (int i = 0; i < attributes.getLength(); i++) {
                // Get names and values for each attribute
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);
                
                if (name.equals("id")) {
                    attraction.setId(value);
                }
                else if (name.equals("name")) {
                    attraction.setName(value);
                }
                else if (name.equals("thumbnailUrl")) {
                    attraction.setThumbnailUrl(value);
                }
                else if (name.equals("type")) {
                    attraction.setType(value);
                }
            }
        }
    }

    /**
     * @see Parser#endElement(java.lang.String, java.lang.String, java.lang.String) 
     */
    public final void endElement(String uri, String localName, String qName) {
        String chars = getChars();
        
        if (qName.equals("attraction")) {
            attractions.addElement(attraction);
            attraction = null;
        }
        else if (qName.equals("street")) {
            attraction.setStreet(chars);
        }
        else if (qName.equals("latitude")) {
            attraction.setLatitude(chars);
        }
        else if (qName.equals("longitude")) {
            attraction.setLongitude(chars);
        }
        else if (qName.equals("imageUrl")) {
            attraction.setImageUrl(chars);
        }
        else if (qName.equals("description")) {
            attraction.setDescription(chars);
        }
    }
}