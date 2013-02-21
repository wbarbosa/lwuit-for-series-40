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
import com.nokia.example.lwuit.rlinks.util.ImageCache;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;

/**
 * A custom view item representing a LinkThing.
 */
public class LinkItem
        extends ListItem {

    private final LinkThing link;
    private final LinkSelectionListener listener;
    private TextArea title = new TextArea(2, 2);
    private Label details = new Label("");
    private Label score = new Label("");

    /**
     * Interface used to signal link selections.
     */
    public interface LinkSelectionListener {

        public void linkSelected(LinkThing link);
    }

    /**
     * Create a Linkitem.
     *
     * @param link Link represented by the Linkitem
     * @param preferredWidth Preferred width
     * @param showSubreddit Whether the subreddit should be displayed
     * @param listener Listener to signal of link selections
     * @param imageCache A cache to store images into
     */
    public LinkItem(LinkThing link, LinkSelectionListener listener, boolean showSubreddit, ImageCache imageCache) {

        this.link = link;
        this.listener = listener;

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        Style style;

        Container content = new Container(new BorderLayout());
        content.addComponent(BorderLayout.CENTER, title);
        addComponent(content);

        Container footer = new Container(new BorderLayout());
        footer.addComponent(BorderLayout.CENTER, details);
        footer.addComponent(BorderLayout.EAST, score);
        addComponent(footer);

        title.setUIID("Label");
        title.setText(link.getTitle());
        title.setEditable(false);
        title.setGrowByContent(true);
        style = title.getStyle();
        style.setBgTransparency(0);
        title.setSelectedStyle(style);
        title.setPressedStyle(style);

        details.setText((showSubreddit ? link.getSubreddit() + " @ " : "") + link.getDomain());
        details.setTickerEnabled(false);
        style = details.getStyle();
        style.setBgTransparency(0);
        details.setPressedStyle(style);

        score.setText((link.getScore() > 0 ? "+" : "") + link.getScore());
        score.setTickerEnabled(false);
        style = score.getStyle();
        style.setBgTransparency(0);
        score.setPressedStyle(style);

        if (link.getThumbnail() != null) {
            Container flow = new Container();
            Thumbnail thumbnail = new Thumbnail(link.getThumbnail(), imageCache, new Thumbnail.Listener() {

                public void imageLoaded() {
                    repaint();
                }
            });
            flow.addComponent(thumbnail);
            content.addComponent(BorderLayout.EAST, flow);
        }
    }

    public LinkThing getLink() {
        return this.link;
    }

    public void actionPerformed(ActionEvent evt) {
        listener.linkSelected(link);
    }

    public void refreshScore() {
        score.setText((link.getScore() > 0 ? "+" : "") + link.getScore());
    }
}
