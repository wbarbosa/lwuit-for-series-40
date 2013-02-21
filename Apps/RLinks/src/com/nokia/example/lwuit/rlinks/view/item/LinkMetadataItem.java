/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.model.LinkThing;
import com.nokia.example.lwuit.rlinks.util.DatePrettyPrinter;
import com.sun.lwuit.Label;

/**
 * A custom view item for showing Link related metadata.
 */
public class LinkMetadataItem
        extends Item {

    private final Label author = new Label();
    private final Label created = new Label();

    /**
     * Create a LinkMetadataItem.
     *
     * @param link LinkThing related to this item
     * @param preferredWidth Preferred width
     */
    public LinkMetadataItem(LinkThing link) {
        addComponent(author);
        addComponent(created);

        author.setText("By " + link.getAuthor());
        created.setText(DatePrettyPrinter.prettyPrint(link.getCreated()));
    }
}
