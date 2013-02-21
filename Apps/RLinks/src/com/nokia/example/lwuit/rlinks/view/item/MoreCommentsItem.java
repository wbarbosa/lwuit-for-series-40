/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.model.CommentThing;

public class MoreCommentsItem
        extends CommentItem {

    public MoreCommentsItem(CommentThing comment, CommentSelectionListener listener) {
        super(comment, listener);
        caption.setUIID("MoreCommentsLabel");
        caption.setText("Load more replies");
        removeComponent(subcaption);
        removeComponent(body);
    }

    public void setLoading() {
        caption.setText("Loading...");
    }
}
