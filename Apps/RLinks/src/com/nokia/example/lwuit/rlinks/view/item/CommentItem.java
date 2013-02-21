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
import com.nokia.example.lwuit.rlinks.util.DatePrettyPrinter;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;

/**
 * A custom view item for representing a CommentThing.
 */
public class CommentItem
        extends ListItem {

    protected final CommentThing comment;
    protected CommentSelectionListener listener;
    protected Label caption = new Label("");
    protected Label subcaption = new Label("");
    protected TextArea body = new TextArea(2, 20);
    protected int itemIndex;

    /**
     * Interface used to signal comment selections.
     */
    public interface CommentSelectionListener {

        public void commentSelected(CommentThing comment, int itemIndex);
    }

    public CommentItem(CommentThing comment, CommentSelectionListener listener) {
        this.comment = comment;
        this.listener = listener;

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        addComponent(caption);
        addComponent(subcaption);
        addComponent(body);

        Style style = getUnselectedStyle();
        int level = comment.getLevel();
        int indent = level == 0 ? 1 : 1 + Math.max(0, 12 * level) - (5 * (level - 1));
        style.setMargin(Component.LEFT, indent);
        getSelectedStyle().setMargin(Component.LEFT, indent);
        getPressedStyle().setMargin(Component.LEFT, indent);

        caption.setText(comment.getAuthor());

        subcaption.setText(comment.getCreated() == null ? ""
                : DatePrettyPrinter.prettyPrint(comment.getCreated()) + comment.getFormattedScore());
        subcaption.setTickerEnabled(false);

        body.setUIID("Label");
        body.setGrowByContent(true);
        body.setText(comment.getBody());
        body.setEditable(false);
        style = body.getUnselectedStyle();
        body.setSelectedStyle(style);
        body.setPressedStyle(style);
    }

    public void actionPerformed(ActionEvent evt) {
        listener.commentSelected(comment, itemIndex);
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int index) {
        this.itemIndex = index;
    }
}
