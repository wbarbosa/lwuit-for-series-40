/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.lists;

import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.network.ImageOperation;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.View;
import com.nokia.example.touristattractions.views.components.MarkerComponent;
import com.sun.lwuit.*;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;

public class AttractionListRenderer
    extends Container
    implements ListCellRenderer {

    private AttractionList list;
    private Label thumbnailLabel;
    private Label nameLabel;
    private Label distanceLabel;
    private MarkerComponent markerComp;
    private boolean released = false;

    public AttractionListRenderer(AttractionList list) {
        this.list = list;
        
        setPreferredW(Display.getInstance().getDisplayWidth());
        setPreferredH(50);
        
        initializeStyles();
        createLayout();
    }
    
    /* Init and style components */
    private void initializeStyles() {        
        Style style = getStyle();
        style.setBgColor(Visual.LIST_FOCUS_COLOR);
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        
        thumbnailLabel = new Label();
        style = thumbnailLabel.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        
        nameLabel = new Label("");
        style = nameLabel.getStyle();
        style.setPadding(5, 0, 3, 0);
        style.setMargin(0, 0, 0, 0);
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        style.setFont(Visual.SMALL_BOLD_FONT);
        
        markerComp = new MarkerComponent();
        style = markerComp.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(10, 0, 0, 0);
        
        distanceLabel = new Label("");       
        style = distanceLabel.getStyle();
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        style.setFont(Visual.SMALL_FONT);
    }
    
    private void createLayout() {
        Container textContainer = new Container();
        textContainer.setLayout(new BorderLayout());
        textContainer.addComponent(BorderLayout.NORTH, nameLabel);
        textContainer.addComponent(BorderLayout.SOUTH, distanceLabel);

        Container leftContainer = new Container();
        leftContainer.addComponent(thumbnailLabel);
        leftContainer.addComponent(textContainer);

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        setLayout(borderLayout);        
        addComponent(BorderLayout.WEST, leftContainer);
        addComponent(BorderLayout.EAST, markerComp);        
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Component getListCellRendererComponent(List list, Object object, int index,
        boolean isSelected) {
        /* Change the appearance of components dynamically depending on the list item */
        final Attraction attraction = (Attraction) object;

        /* Set labels */
        nameLabel.setText(attraction.getName());
        distanceLabel.setText(attraction.getDistance());
        markerComp.setId(attraction.getId());

        /* Handle appearance of tapped item */
        Style style = this.getStyle();
        if (isSelected && (released || !Compatibility.TOUCH_SUPPORTED)) {
            style.setBgTransparency(0xff);
        }
        else {
            style.setBgTransparency(0x00);
        }

        /* Load thumbnail if not loaded */
        Image thumbnail = attraction.getThumbnail();
        if (thumbnail != null) {
            thumbnailLabel.setIcon(thumbnail);
        }
        else {
            thumbnailLabel.setIcon(View.defaultThumbnail);
            if (!attraction.isLoadingThumbnail()) {
                attraction.loadThumbnail(new ImageOperation.Listener() {

                    public void imageReceived(String url, Image loadedThumbnail) {
                        /* show when received */
                        attraction.setThumbnail(loadedThumbnail);
                        attraction.setLoadingThumbnail(false);
                        thumbnailLabel.setIcon(loadedThumbnail);
                        AttractionListRenderer.this.list.getAttractionView().repaint();
                    }

                    public void onNetworkFailure() {
                        attraction.setLoadingThumbnail(false);
                        Util.showAlert("Network error", "Check your connection.");
                    }
                });
            }
        }

        return this;
    }

    public Component getListFocusComponent(List list) {
        return null;
    }
}
