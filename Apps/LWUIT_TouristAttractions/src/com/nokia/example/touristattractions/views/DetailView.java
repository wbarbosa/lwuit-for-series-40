/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.network.ImageOperation;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.maps.common.GeoCoordinate;
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;

public class DetailView
    extends View {

    private Attraction attraction; // attraction being shown
    private Container middleContainer;
    private Label imageLabel;
    private Label streetLabel;
    private Label distanceLabel;
    private TextArea description;

    public DetailView(TouristMidlet midlet) {
        super(midlet);

        initAndStyleComponents();
        createLayout();
        addCommands();
    }
    
    private void initAndStyleComponents() {
        // Style the components
        Style style = getStyle();
        getStyle().setBgColor(Visual.BACKGROUND_COLOR);
        
        streetLabel = new Label();
        style = streetLabel.getStyle();
        style.setFgColor(Visual.LIST_SECONDARY_COLOR);
        style.setFont(Visual.SMALL_BOLD_FONT);
        
        distanceLabel = new Label();
        style = distanceLabel.getStyle();
        style.setFgColor(Visual.LIST_SECONDARY_COLOR);
        style.setFont(Visual.SMALL_BOLD_FONT);
        style.setAlignment(RIGHT);
        
        imageLabel = new Label();
        imageLabel.getStyle().setAlignment(LEFT);
        
        description = new TextArea();
        description.setRows(2);
        description.setGrowByContent(true);
        description.setEditable(false);
        description.setUIID("Label");
        description.setFocusable(false);
        description.getStyle().setFont(Visual.SMALL_FONT);
        description.getStyle().setFgColor(Visual.LIST_PRIMARY_COLOR);

        revalidate();
    }

    private void createLayout() {
        middleContainer = new Container();
        middleContainer.setLayout(new BorderLayout());
        middleContainer.addComponent(BorderLayout.WEST, streetLabel);
        middleContainer.addComponent(BorderLayout.EAST, distanceLabel);

        Container c = new Container();
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        c.addComponent(imageLabel);
        c.addComponent(middleContainer);
        
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(c);
        addComponent(description);        
    }
    
    private void addCommands() {
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                attraction.setImage(null);
                DetailView.this.midlet.showAttractionView();
            }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);
        
        Image mapCommandImage = null;
        if (Compatibility.IS_FULLTOUCH) {
            mapCommandImage = Util.loadImage("icons/map.png");
        }
        Command mapCommand = new Command("Map", mapCommandImage) {
            public void actionPerformed(ActionEvent e) {
                attraction.setImage(null);
                GeoCoordinate loc = DetailView.this.attraction.getLocation();
                DetailView.this.midlet.showMapView(attraction.getGuide(),
                    attraction, loc.getLatitude(), loc.getLongitude(),
                    attraction.getName());
            }
        };
        setDefaultCommand(mapCommand);
        addCommand(mapCommand);        
    }
    
    /**
     * Show this view with the given attraction.
     *
     * @param attraction Attraction of whom details will be shown
     */
    public void show(Attraction attraction) {
        setAttraction(attraction);
        super.show();
    }

    private void setAttraction(final Attraction attraction) {
        this.attraction = attraction;
        setTitle(attraction.getName());

        streetLabel.setText(attraction.getStreet());
        distanceLabel.setText(attraction.getDistance());
        description.setText(attraction.getDescription());

        Image image = attraction.getImage();
        if (image != null) {
            imageLabel.setIcon(image);
        }
        else {
            // Image not loaded yet, set default thumbnail
            imageLabel.setIcon(View.defaultThumbnail);
            
            // Load the image in the background and paint it when received
            if (!attraction.isLoadingImage()) {
                attraction.loadImage(new ImageOperation.Listener() {
                    public void imageReceived(String url, Image loadedImage) {
                        attraction.setImage(loadedImage);
                        attraction.setLoadingImage(false);
                        imageLabel.setIcon(loadedImage);
                        revalidate();
                        repaint();
                    }

                    public void onNetworkFailure() {
                        Util.showAlert("Network error", "Check your connection.");
                    }
                });
            }
        }

        revalidate();
        imageLabel.scrollRectToVisible(0, 0, 10, 10, this); // scroll to the top of list
        repaint();
    }
}
