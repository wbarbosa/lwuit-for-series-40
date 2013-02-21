/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views.components;

import com.nokia.example.touristattractions.models.Attraction;
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.models.Self;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.MapView;
import com.nokia.lwuit.GestureHandler;
import com.nokia.maps.common.ApplicationContext;
import com.nokia.maps.common.GeoCoordinate;
import com.nokia.maps.map.MapCircle;
import com.nokia.maps.map.MapDisplay;
import com.nokia.maps.map.MapFactory;
import com.nokia.maps.map.MapListener;
import com.nokia.maps.map.MapMarker;
import com.nokia.maps.map.Point;
import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Dimension;
import java.io.IOException;
import java.util.Vector;

/**
 * Component holding map. It uses Nokia Maps J2ME API.
 */
public class MapComponent
    extends Component
    implements MapListener {

    private static final int DEFAULT_ZOOM = 14; // default zoom level (0-20)
    private static final String APP_ID = "rdDjsQ0xruTZCiwWjo01"; // needed to use the API
    private static final String APP_TOKEN = "EdMChGcbrECxiMbgCxS_sw"; // needed to use the API
    private int zoom = DEFAULT_ZOOM;
    private double lat, lon; // latitude, longitude
    private MapView mapView;
    private MapFactory mapFactory;
    private MapDisplay map;
    private boolean loading = false; // loading indicator
    
    /* Zoom buttons */
    private Image zoomOutImage, zoomInImage;
    private int zoomOutX, zoomOutY, zoomInX, zoomInY;
    
    /* Panning indicator and panning points */
    private boolean panning = false;
    private boolean pinching = false;
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private GeoCoordinate center;
    
    /* Attractions for markers */
    private Vector attractions;
    
    /* Marker for current positions */
    private MapCircle currentPositionCircle;

    public MapComponent(MapView mapView) {
        this.mapView = mapView;

        createMap();
        
        try {
            Class c = Class.forName("com.nokia.mid.ui.gestures.GestureInteractiveZone");
            GestureHandler.setFormGestureHandler(mapView, new GestureHandler() {
                public void gestureAction(GestureEvent ge) {
                    int eventType = ge.getType();
                    switch (eventType) {
                        case GestureInteractiveZone.GESTURE_RECOGNITION_START:
                            pinching = false;
                            center = map.getCenter();
                            break;
                        case GestureInteractiveZone.GESTURE_RECOGNITION_END:
                            
                            break;
                        case GestureInteractiveZone.GESTURE_PINCH: // Pinch detected
                            pinching = true;
                            int curPinchDistance = ge.getPinchDistanceCurrent();
                            int startingPinchDistance = ge
                                    .getPinchDistanceStarting();
                            int zoomNew = zoom
                                    + (int) ((curPinchDistance - startingPinchDistance) / 100);
                            if (zoomNew > map.getMaxZoomLevel()) {
                                zoomNew = (int) map.getMaxZoomLevel();
                            }
                            if (zoomNew < map.getMinZoomLevel()) {
                                zoomNew = (int) map.getMinZoomLevel();
                            }
                            if (zoomNew != zoom) {
                                map.setZoomLevel(zoomNew, 0, 0);
                                //map.setCenter(orgCenter);
                            }
                            zoom = map.getZoomLevel();
                            map.setCenter(center);
                            break;
                    }
                }
            });
        }
        catch (ClassNotFoundException e) {}
        
        // Paddings and margins not needed
        this.getStyle().setPadding(0, 0, 0, 0);
        this.getStyle().setMargin(0, 0, 0, 0);
    }
    
    private void createMap() {
        ApplicationContext.getInstance().setAppID(APP_ID);
        ApplicationContext.getInstance().setToken(APP_TOKEN);
        mapFactory = MapFactory.createMapFactory(
            128,
            mapView.getWidth(),
            mapView.getHeight());

        this.map = mapFactory.createMapDisplay();
        map.setMapListener(this);        
        
        // Zoom button images
        zoomOutImage = Util.loadImage("icons/zoom_out.png");
        zoomInImage = Util.loadImage("icons/zoom_in.png");
    }

    /**
     * Add the markers to the map.
     */
    public final void addMarkers(Guide guide) {
        // Update attractions
        Vector attrs = guide.getAttractions();
        if (attrs == attractions || attrs == null) {
            return;
        }
        attractions = attrs;

        map.removeAllMapObjects(); // remove all markers
        if (currentPositionCircle != null) {
            map.addMapObject(currentPositionCircle);
        }

        // Add a marker for each attraction
        for (int i = 0, size = attractions.size(); i < size; i++) {
            Attraction attr = (Attraction) attractions.elementAt(i);
            try {
                javax.microedition.lcdui.Image image = Util.loadMapMarker(
                    attr.getId(), "/location.png");
                GeoCoordinate c = attr.getLocation();
                MapMarker marker = mapFactory.createMapMarker(c, image);
                marker.setAnchor(new Point(image.getWidth() / 2,
                    image.getHeight()));
                map.addMapObject(marker);
            }
            catch (IOException e) {
            }
        }
    }

    /**
     * Refresh the circle showing the user's current position.
     */
    public final void refreshCurrentPositionCircle() {
        GeoCoordinate currentPosition = Self.getCurrentPosition();
        if (currentPosition == null) {
            return;
        }
        
        int accuracy = Self.getAccuracy();
        if (currentPositionCircle == null) {
            currentPositionCircle = mapFactory.createMapCircle(accuracy,
                currentPosition);
            currentPositionCircle.setColor(Visual.MAP_CIRCLE_COLOR);
            map.addMapObject(currentPositionCircle);
        }
        else {
            currentPositionCircle.setCenter(currentPosition);
            currentPositionCircle.setRadius(accuracy);
        }
    }

    public void paint(Graphics g) {
        int baseX = getX();
        int baseY = getY();

        javax.microedition.lcdui.Graphics ng =
            (javax.microedition.lcdui.Graphics) g.beginNativeGraphicsAccess();

        // On some devices, clipping must be used to prevent painting over
        // title bar or menu bar
        if (Compatibility.SCREEN_SIZE == Compatibility.RES_240x320 ||
            Compatibility.SCREEN_SIZE == Compatibility.RES_320x240) {
            ng.setClip(0, mapView.getTitleComponent().getHeight(),
                mapView.getWidth(), mapView.getContentAreaHeight());
        }

        map.paint(ng);
        g.endNativeGraphicsAccess();

        if (Compatibility.TOUCH_SUPPORTED) {
            drawZoomButtons(g, baseX, baseY);
        }
    }

    /* Handle panning and zooming with keys */
    public void keyPressed(int keyCode) {
        moveMap(keyCode);
    }

    public void keyRepeated(int keyCode) {
        moveMap(keyCode);
    }

    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case 42: // * char
                zoomOut();
                break;
            case 35: // # char
                zoomIn();
                break;
        }
    }

    public void pointerPressed(int x, int y) {
        // Handle only if not loading at the moment
        if (loading) {
            return;
        }
        
        if (x > zoomOutX && x < zoomOutX + zoomOutImage.getWidth() &&
            y > zoomOutY && y < zoomOutY + zoomOutImage.getHeight()) {
            zoomOut();
        }
        else if (x > zoomInX && x < zoomInX + zoomInImage.getWidth() &&
                 y > zoomInY && y < zoomInY + zoomInImage.getHeight()) {
            zoomIn();
        }
        else {
            if (pinching) {
                return;
            }
            // If zoom button not clicked, handle as panning
            panning = true;
            startPoint.setX(x);
            startPoint.setY(y);
        }
    }

    /* Handle panning */
    public void pointerDragged(int x, int y) {
        if (!panning || pinching) {
            return;
        }
        
        endPoint.setX(x);
        endPoint.setY(y);
        map.pan(endPoint, startPoint);
        startPoint.setX(x);
        startPoint.setY(y);
    }

    public void pointerReleased(int x, int y) {
        if (!panning || pinching) {
            return;
        }
        
        endPoint.setX(x);
        endPoint.setY(y);
        map.pan(startPoint, endPoint);
        panning = false;
    }

    /**
     * Update map latitude and longitude.
     * 
     * @param lat New latitude
     * @param lon New longitude
     */
    public void update(double lat, double lon) {
        update(lat, lon, DEFAULT_ZOOM);
    }

    /**
     * Update map latitude, longitude and zoom level.
     * 
     * @param lat New latitude
     * @param lon New longitude
     * @param zoom New zoom level
     */
    public void update(double lat, double lon, int zoom) {
        if (loading) {
            return;
        }
        
        loading = true;
        repaint();

        this.lat = lat;
        this.lon = lon;
        map.setCenter(new GeoCoordinate(lat, lon, 0));
        map.setZoomLevel(zoom, 0, 0);
        loading = false;
        mapView.showMap();
        repaint();
    }

    /**
     * Repaint as content is loaded completely.
     */
    public void onMapContentComplete() {
        mapView.repaint();
    }
    
    /**
     * Repaint as content is updated.
     */
    public void onMapContentUpdated() {
        mapView.repaint();
    }

    /**
     * Show error message and switch to details view when error.
     * 
     * @param string Error string
     * @param throwable Throwable caused by error
     * @param bln true if the error is critical
     */
    public void onMapUpdateError(String string, Throwable throwable, boolean bln) {
        Util.showAlert("Network error", "Check your connection.");
        mapView.getMidlet().showDetailView(mapView.getCurrentAttraction());
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(mapView.getLayoutWidth(), mapView.getLayoutHeight());
    }

    private void drawZoomButtons(Graphics g, int baseX, int baseY) {
        zoomOutX = baseX + getWidth() / 3 - zoomOutImage.getWidth() / 2;
        zoomOutY = baseY + getHeight() - zoomOutImage.getHeight() - mapView.getMenuBar().getHeight() - 6;        
        zoomInX = baseX + 2 * getWidth() / 3 - zoomOutImage.getWidth() / 2;
        zoomInY = zoomOutY;

        int offsetY = 0;
        if (Compatibility.SCREEN_SIZE == Compatibility.RES_240x320) {
            offsetY = 40;
        }
        g.drawImage(zoomOutImage, zoomOutX, zoomOutY - offsetY);
        g.drawImage(zoomInImage, zoomInX, zoomInY - offsetY);
    }

    private void moveMap(int keyCode) {
        int gameAction = Display.getInstance().getGameAction(keyCode);

        int newX = startPoint.getX();
        int newY = startPoint.getY();
        
        switch (gameAction) {
            case Display.GAME_LEFT:
                newX = startPoint.getX() - Compatibility.WIDTH / 25;
                break;
            case Display.GAME_RIGHT:
                newX = startPoint.getX() + Compatibility.WIDTH / 25;
                break;
            case Display.GAME_UP:
                newY = startPoint.getY() - Compatibility.WIDTH / 25;
                break;
            case Display.GAME_DOWN:
                newY = startPoint.getY() + Compatibility.WIDTH / 25;
                break;
            default:
                return;
        }
        
        endPoint.setX(newX);
        endPoint.setY(newY);
        map.pan(startPoint, endPoint);
        startPoint.setX(newX);
        startPoint.setY(newY);
    }

    private void zoomIn() {
        zoom++;
        if (zoom > 20) {
            zoom = 20;
        }
        update(lat, lon, zoom);
    }

    private void zoomOut() {
        zoom--;
        if (zoom < 0) {
            zoom = 0;
        }
        update(lat, lon, zoom);
    }
}
