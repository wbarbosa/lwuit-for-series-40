/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.lists;

import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.network.ImageOperation;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.View;
import com.sun.lwuit.*;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;

public class GuideListRenderer
    extends Container
    implements ListCellRenderer {

    private GuideList list;
    private Label cityLabel;
    private Label imageLabel;
    private boolean released = false;

    public GuideListRenderer(GuideList list) {
        this.list = list;
        
        setPreferredW(Display.getInstance().getDisplayWidth());
        setPreferredH(50);
        
        initializeAndStyleComponents();
    }
    
    private void initializeAndStyleComponents() {
        Style style = getStyle();
        style.setBgColor(Visual.LIST_FOCUS_COLOR);
        
        cityLabel = new Label("");
        style = cityLabel.getStyle();
        style.setPadding(5, 0, 3, 0);
        style.setMargin(0, 0, 0, 0);
        style.setFgColor(Visual.LIST_PRIMARY_COLOR);
        style.setFont(Visual.MEDIUM_BOLD_FONT);
        
        imageLabel = new Label();
        style = imageLabel.getStyle();
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);

        addComponent(imageLabel);
        addComponent(cityLabel);        
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Component getListCellRendererComponent(List list, Object object, int index,
        boolean isSelected) {
        /* Change the appearance of components dynamically depending on the list item */
        final Guide guide = (Guide) object;

        /* Handle appearance of tapped item */
        if (isSelected && (released || !Compatibility.TOUCH_SUPPORTED)) {
            getStyle().setBgTransparency(255);
        }
        else {
            getStyle().setBgTransparency(0);
        }

        cityLabel.setText(guide.getCity());

        /* Load thumbnail if not loaded */
        Image image = guide.getImage();
        if (image != null) {
            imageLabel.setIcon(image);
        }
        else {
            imageLabel.setIcon(View.defaultThumbnail);
            
            if (!guide.isLoadingImage()) {
                guide.loadImage(new ImageOperation.Listener() {
                    public void imageReceived(String url, Image loadedImage) {
                        /* Show when received */
                        guide.setImage(loadedImage);
                        guide.setLoadingImage(false);
                        imageLabel.setIcon(loadedImage);
                        GuideListRenderer.this.list.getGuideView().repaint();
                    }

                    public void onNetworkFailure() {
                        guide.setLoadingImage(false);
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
