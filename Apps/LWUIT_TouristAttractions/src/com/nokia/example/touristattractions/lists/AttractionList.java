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
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.network.AttractionsOperation;
import com.nokia.example.touristattractions.views.AttractionView;
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;

public class AttractionList
    extends List
    implements ActionListener {

    private AttractionView attractionView;
    private AttractionListModel listModel;
    private AttractionListRenderer listRenderer;

    public AttractionList(AttractionView attractionView) {
        super();
        this.attractionView = attractionView;

        listModel = new AttractionListModel();
        listRenderer = new AttractionListRenderer(this);

        /* Set model, renderer and action listener */
        this.setModel(listModel);
        this.setRenderer(listRenderer);
        this.addActionListener(this);

        /* Styling */
        this.getStyle().setPadding(0, 0, 0, 0);
        this.getStyle().setMargin(0, 0, 0, 0);
        this.getSelectedStyle().setPadding(0, 0, 0, 0);
        this.getSelectedStyle().setMargin(0, 0, 0, 0);
    }

    public AttractionView getAttractionView() {
        return attractionView;
    }

    /**
     * Sets the current guide for this list of whom attractions will be shown.
     *
     * @param guide Guide of whom attractions will be shown
     */
    public void setGuide(final Guide guide) {
        listModel.removeAll();

        /* if attractions not loaded, load them */
        if (guide.isLoaded()) {
            updateAttractions(guide.getAttractions());
        }
        else {
            guide.loadAttractions(new AttractionsOperation.Listener() {
                public void attractionsReceived(Vector attractions) {
                    guide.setAttractions(attractions);
                    guide.setAttractionsLoaded(true);
                    AttractionList.this.updateAttractions(attractions);
                }
            });
        }
    }

    public void pointerDragged(int x, int y) {
        super.pointerDragged(x, y);
        this.setSelectedItem(null);
    }

    public void pointerReleased(int x, int y) {
        super.pointerReleased(x, y);
        this.setSelectedItem(null);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getCommand() == null) {
            final int selectedIndex = ((List) e.getSource()).getSelectedIndex();            
            new Thread() {
                public void run() {
                    /* show detail view */
                    attractionView.getMidlet().showDetailView(
                        (Attraction) listModel.getItemAt(selectedIndex));
                    ((AttractionListRenderer) AttractionList.this.getRenderer()).setReleased(false);
                }
            }.start();
        }
    }

    private void updateAttractions(Vector attractions) {
        for (int i = 0; i < attractions.size(); i++) {
            Attraction attraction = (Attraction) attractions.elementAt(i);
            attraction.updateDistance();
            listModel.addItem(attraction);
        }
        
        Display.getInstance().callSerially(
            new Runnable() {
                public void run() {
                    AttractionList.this.repaint();
                }
            });
    }

    /* Add a bit of additional functionality to the list model. */
    public class AttractionListModel
        extends DefaultListModel {

        public void addItem(Object item) {
            super.addItem(item);
            AttractionList.this.attractionView.showLoading(false);
        }
    }
}
