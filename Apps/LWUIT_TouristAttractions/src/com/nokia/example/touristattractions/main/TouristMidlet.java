/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.main;

import java.util.Vector;

import javax.microedition.midlet.MIDlet;

import com.nokia.example.touristattractions.location.LocationFinder;
import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.models.Self;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.GuideSaver;
import com.nokia.example.touristattractions.views.AboutView;
import com.nokia.example.touristattractions.views.AttractionView;
import com.nokia.example.touristattractions.views.DetailView;
import com.nokia.example.touristattractions.views.GuideView;
import com.nokia.example.touristattractions.views.HelpView;
import com.nokia.example.touristattractions.views.MapView;
import com.nokia.example.touristattractions.views.NewGuideView;
import com.nokia.example.touristattractions.views.SplashView;
import com.nokia.maps.common.GeoCoordinate;
import com.sun.lwuit.Display;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * The main application MIDlet and entry point.
 */
public class TouristMidlet
    extends MIDlet {

    /* Loading spinner frame rate */
    public static final int SPINNER_FPS = 25;
    
    /* Views of the application */
    private SplashView splashView;
    private AttractionView attractionView;
    private GuideView guideView;
    private DetailView detailView;
    private HelpView helpView;
    private AboutView aboutView;
    private MapView mapView;
    private NewGuideView buyGuidesView;
    
    /* Location helper class */
    private LocationFinder locationFinder;
    
    /* Helper class for saving/loading purchased guides to/from RMS */
    private GuideSaver guideSaver;

    public void startApp() {
        showSplashAndInitialize();
        showAttractionView(guideView.getGuideList().getHelsinkiGuide());
    }
    
    /**
     * Show splash screen and do preparations required for application startup.
     */
    private void showSplashAndInitialize() {
        /* Init and show splash view as early as possible */
        initializeDisplay();
        initializeStyles();
        splashView = new SplashView();        
        guideSaver = new GuideSaver(this);
        showSplashView();

        /* Continue initialization in the background */
        long start = System.currentTimeMillis();
        createViews();
        initLocationFinder();        
        
        /* Show splash screen for at least one second */
        long difference = System.currentTimeMillis() - start;
        try {
            Thread.sleep(Math.max(0, 1000 - difference));
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        // Get ready to show the next view
        Display.getInstance().setForceFullScreen(false);
        splashView.stopSpin();
    }
    
    /**
     * Prepare Display instance for showing things.
     */
    private void initializeDisplay() {
        Display.init(this);
        Display.getInstance().setForceFullScreen(true);
        
        if (Compatibility.TOUCH_SUPPORTED) {
            Display.getInstance().setPureTouch(true);
        }        
    }
    
    /*
     * Initialize basic style settings.
     */
    private void initializeStyles() {
        try {
            UIManager uiManager = UIManager.getInstance();
            Style style = new Style();
            style.setBgTransparency(0x00);
            uiManager.setComponentStyle("Label", style);
            
            style = new Style();
            style.setBorder(null);
            uiManager.setComponentStyle("TextArea", style);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Create application views.
     */
    private void createViews() {
        attractionView = new AttractionView(this);
        guideView = new GuideView(this);
        detailView = new DetailView(this);
        helpView = new HelpView(this);
        aboutView = new AboutView(this);
        mapView = new MapView(this);
        
        /* Guide buy view not needed if IAP not supported */
        if (Compatibility.IAP_SUPPORTED) {
            buyGuidesView = new NewGuideView(this);
        }
    }

    public Vector loadNewGuides() {
        return guideSaver.loadGuides();
    }

    public void saveNewGuides() {
        guideSaver.saveGuides();
    }

    public void showSplashView() {
        splashView.show();
    }

    public void showAttractionView() {
        attractionView.show();
    }

    /**
     * Show attraction view with attractions of the given guide
     *
     * @param guide Guide of whom attractions will be shown
     */
    public void showAttractionView(Guide guide) {
        attractionView.show(guide);
    }

    public void showGuideView() {
        guideView.show();
    }

    /**
     * Show detail view of the given attraction.
     *
     * @param attraction Attraction of whom details will be shown
     */
    public void showDetailView(Attraction attraction) {
        detailView.show(attraction);
    }

    public void showHelpView() {
        helpView.show();
    }

    public void showAboutView() {
        aboutView.show();
    }

    /**
     * Show map view.
     *
     * @param guide Current guide
     * @param currentAttraction Current Attraction
     * @param lat Latitude
     * @param lon Longitude
     * @param title Title of the map
     */
    public void showMapView(Guide guide, Attraction currentAttraction,
        double lat, double lon, String title) {
        mapView.show(guide, currentAttraction, lat, lon, title);
    }

    public void showBuyGuidesView() {
        buyGuidesView.show();
        buyGuidesView.repaint();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        stopLocationFinder();
    }

    /**
     * Refresh the user's current position on the map.
     */
    public final void refreshCurrentPositionOnMap() {
        if (mapView.isVisible()) {
            mapView.refreshCurrentPositionCircle();
        }
    }

    public GuideView getGuideView() {
        return guideView;
    }

    public AttractionView getAttractionView() {
        return attractionView;
    }

    private synchronized void startLocationFinder() {
        if (locationFinder != null) {
            locationFinder.start();
        }
    }

    private synchronized void stopLocationFinder() {
        if (locationFinder != null) {
            locationFinder.quit();
        }
    }

    private synchronized void initLocationFinder() {
        if (locationFinder == null) {
            locationFinder =
                LocationFinder.getFinder(new LocationFinder.Listener() {

                public void newLocation(double lat, double lon, int accuracy) {
                    Self.setCurrentPosition(new GeoCoordinate(
                        lat, lon, 0), accuracy);
                    refreshCurrentPositionOnMap();
                }
            });
        }
        startLocationFinder();
    }
}