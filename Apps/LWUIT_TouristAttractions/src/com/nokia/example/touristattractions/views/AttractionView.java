/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.lists.AttractionList;
import com.nokia.example.touristattractions.lists.AttractionList.AttractionListModel;
import com.nokia.example.touristattractions.lists.AttractionListRenderer;
import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.models.Self;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.components.LoadingComponent;
import com.nokia.maps.common.GeoCoordinate;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

public class AttractionView
    extends View
    implements ActionListener {

    private AttractionList attractionList;
    private Guide guide; // current guide
    private LoadingComponent loadingComp;
    
    /* Commands */
    private Command guidesCommand;
    private Command aboutCommand;
    private Command helpCommand;
    private Command mapCommand;
    private Command backCommand;
    private Command selectCommand;
    
    /* Indicating if pointer has been dragged after press event */
    private boolean dragStarted = false;

    public AttractionView(TouristMidlet midlet) {
        super(midlet);

        getStyle().setBgColor(Visual.BACKGROUND_COLOR);

        attractionList = new AttractionList(this);
        attractionList.setSelectedItem(null);
        loadingComp = new LoadingComponent(this);

        addCommands();
    }
    
    private void addCommands() {
        addCommandListener(this);
        
        Image mapCommandImage = null;
        if (Compatibility.IS_FULLTOUCH) {
            mapCommandImage = Util.loadImage("icons/map.png");
        }
        mapCommand = new Command("Map", mapCommandImage);
        guidesCommand = new Command("Guides");        
        helpCommand = new Command("Help");
        aboutCommand = new Command("About");
        backCommand = new Command("Exit");
        selectCommand = new Command("Select");
        
        addCommand(mapCommand);
        addCommand(helpCommand);
        addCommand(aboutCommand);
        
        /* If IAP not supported, no need to get to the guides view */
        if (Compatibility.IAP_SUPPORTED) {
            addCommand(guidesCommand);
        }
        
        if (!Compatibility.TOUCH_SUPPORTED) {
            addCommand(selectCommand);
            setDefaultCommand(selectCommand);
        }
        else {
            setDefaultCommand(mapCommand);
        }

        addCommand(backCommand);
        setBackCommand(backCommand);
    }

    /**
     * Shows the view with the given guide.
     *
     * @param guide The guide of whom attractions to be shown
     */
    public void show(Guide guide) {
        this.guide = guide;
        attractionList.setGuide(guide);
        this.setTitle(guide.getCity());
        super.show();

        // Add list after showing form (hack for lwuit s40 list bug)
        if (!this.contains(attractionList)) {
            addComponent(attractionList);
        }
        repaint();
    }

    public Guide getGuide() {
        return guide;
    }

    /**
     * Show/hide loading component.
     *
     * @param show If true, the component is shown, otherwise hidden
     */
    public void showLoading(boolean show) {
        if (show) {
            if (!this.contains(loadingComp)) {
                addComponent(loadingComp);
                loadingComp.startSpin();
                repaint();
            }
        }
        else {
            if (this.contains(loadingComp)) {
                loadingComp.stopSpin();
                removeComponent(loadingComp);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Command cmd = e.getCommand();
        if (cmd != null) {
            // Free some memory
            freeThumbnails();
        }

        /* Handle different commands */
        if (cmd == aboutCommand) {
            midlet.showAboutView();
        }
        else if (cmd == helpCommand) {
            midlet.showHelpView();
        }
        else if (cmd == guidesCommand) {
            midlet.showGuideView();
        }
        else if (cmd == mapCommand) {
            GeoCoordinate loc = Self.getCurrentPosition();
            if (loc != null) {
                midlet.showMapView(guide, null, loc.getLatitude(), loc.getLongitude(), "My location");
            }
            else {
                Util.showAlert("Location error", "Could not get your location.");
            }
        }
        else if (cmd == selectCommand) {
            attractionList.actionPerformed(new ActionEvent(attractionList));
        }
        else if (cmd == backCommand) {
            Display.getInstance().exitApplication();
        }
    }

    /* some custom handling added for a list item highlight after tapping */
    public void pointerPressed(int x, int y) {
        ((AttractionListRenderer) attractionList.getRenderer()).setReleased(false);
        super.pointerPressed(x, y);
    }

    public void pointerDragged(int x, int y) {
        dragStarted = true;
        super.pointerDragged(x, y);
    }

    public void pointerReleased(int x, int y) {
        if (!dragStarted && !getMenuBar().contains(x, y)) {
            ((AttractionListRenderer) attractionList.getRenderer()).setReleased(true);
        }
        super.pointerReleased(x, y);
        dragStarted = false;
    }

    private void freeThumbnails() {
        AttractionListModel model = (AttractionListModel) attractionList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Attraction attraction = (Attraction) model.getItemAt(i);
            attraction.setThumbnail(null);
        }
    }
}
