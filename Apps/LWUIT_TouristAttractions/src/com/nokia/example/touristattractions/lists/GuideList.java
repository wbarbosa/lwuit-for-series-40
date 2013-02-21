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
import com.nokia.example.touristattractions.views.GuideView;
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;

public class GuideList
    extends List
    implements ActionListener {

    private GuideView guideView;
    private GuideListModel listModel;
    private GuideListRenderer listRenderer;
    private Guide helsinki;

    public GuideList(GuideView guideView) {
        super();
        this.guideView = guideView;

        /* Load and add default (Helsinki) and purchased guides */
        loadHelsinkiGuide();
        loadPurchasedGuides();

        listRenderer = new GuideListRenderer(this);
        this.setModel(listModel);
        this.setRenderer(listRenderer);
        this.addActionListener(this);
    }
    
    private void loadHelsinkiGuide() {
        helsinki = new Guide();
        helsinki.setId("helsinki");
        helsinki.setUrl("/guides/helsinki/guide.xml");
        helsinki.setImageUrl("/guides/helsinki/guide.png");
        helsinki.setCity("Helsinki Highlights");
        helsinki.loadAttractions();

        listModel = new GuideListModel();
        listModel.addItem(helsinki);
    }
    
    private void loadPurchasedGuides() {
        Vector newGuides = guideView.getMidlet().loadNewGuides();
        for (int i = 0; i < newGuides.size(); i++) {
            listModel.addItem((Guide) newGuides.elementAt(i));
        }        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getCommand() != null) {
            return;
        }
        
        final int selectedIndex = ((List) e.getSource()).getSelectedIndex();
        new Thread() {
            public void run() {
                /* Show attractions of the clicked guide */
                guideView.getMidlet().showAttractionView(
                    (Guide) listModel.getItemAt(selectedIndex));
                ((GuideListRenderer) GuideList.this.getRenderer())
                    .setReleased(false);
            }
        }.start();
    }

    public GuideListModel getListModel() {
        return listModel;
    }

    public Guide getHelsinkiGuide() {
        return helsinki;
    }

    public GuideView getGuideView() {
        return guideView;
    }

    /* Add some additional functionality to the list model. */
    public class GuideListModel
        extends DefaultListModel {

        public Vector getItems() {
            Vector items = new Vector();
            int size = this.getSize();
            
            for (int i = 0; i < size; i++) {
                items.addElement(this.getItemAt(i));
            }
            return items;
        }
    }
}
