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
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.views.components.LoadingComponent;
import com.nokia.example.touristattractions.views.components.MapComponent;
import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionEvent;

public class MapView
    extends View {

    private MapComponent mapComp;
    private LoadingComponent loadingComp;
    private Attraction currentAttraction; // current attraction to return back to the correct detail view

    public MapView(TouristMidlet midlet) {
        super(midlet);

        setScrollable(false);

        loadingComp = new LoadingComponent(this);
        mapComp = new MapComponent(this);
        loadingComp.startSpin();

        addCommands();
    }
    
    private void addCommands() {
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                if (currentAttraction != null) {
                    MapView.this.midlet.showDetailView(currentAttraction);
                }
                else {
                    MapView.this.midlet.showAttractionView();
                }
            }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);        
    }

    /**
     * Shows the actual map component (instead of loading component).
     */
    public void showMap() {
        if (!contains(mapComp)) {
            replace(loadingComp, mapComp, null);
            loadingComp.stopSpin();
            repaint();
        }
    }

    /**
     * Shows this view.
     *
     * @param guide Current guide
     * @param currentAttraction Current attraction
     * @param lat Latitude of the map center
     * @param lon Longitude of the map center
     * @param title Title of to be shown
     */
    public void show(Guide guide, Attraction currentAttraction, double lat, double lon, String title) {
        this.currentAttraction = currentAttraction;
        setTitle(title);
        getTitleArea().repaint();
        super.show();
        showLoading();
        mapComp.addMarkers(guide);
        refreshCurrentPositionCircle();
        mapComp.update(lat, lon);
    }

    public Attraction getCurrentAttraction() {
        return currentAttraction;
    }

    /**
     * Refreshes the position of the circle indicating current position.
     */
    public synchronized void refreshCurrentPositionCircle() {
        mapComp.refreshCurrentPositionCircle();
    }

    private void showLoading() {
        if (contains(loadingComp)) {
            return;
        }
        
        loadingComp.startSpin();
        if (contains(mapComp)) {
            replace(mapComp, loadingComp, null);
        }
        else {
            addComponent(loadingComp);
        }
        repaint();
    }
}