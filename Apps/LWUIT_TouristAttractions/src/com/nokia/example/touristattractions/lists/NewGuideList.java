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
import com.nokia.example.touristattractions.views.NewGuideView;
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;

public class NewGuideList
    extends List
    implements ActionListener {

    private NewGuideView newGuideView;
    private NewGuideListModel listModel;
    private NewGuideListRenderer listRenderer;

    public NewGuideList(NewGuideView buyGuidesView) {
        super();
        this.newGuideView = buyGuidesView;
        listModel = new NewGuideListModel();
        listRenderer = new NewGuideListRenderer(this);

        this.setModel(listModel);
        this.setRenderer(listRenderer);
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getCommand() != null) {
            return;
        }
        
        int selectedIndex = ((List) e.getSource()).getSelectedIndex();
        final Guide guide = (Guide) listModel.getItemAt(selectedIndex);

        new Thread() {
            public void run() {
                /* Purchase/restore the clicked product */
                newGuideView.handleClickedProduct(guide);
                ((NewGuideListRenderer) NewGuideList.this.getRenderer())
                    .setReleased(false);
            }
        }.start();
    }

    public NewGuideView getNewGuideView() {
        return newGuideView;
    }

    public NewGuideListModel getListModel() {
        return listModel;
    }

    /* Add some additional functionality to the list model. */
    public class NewGuideListModel
        extends DefaultListModel {

        public boolean removeItem(Guide guide) {
            int size = this.getSize();
            
            for (int i = 0; i < size; i++) {
                Guide g = (Guide) listModel.getItemAt(i);
                
                if (g == guide) {
                    this.removeItem(i);
                    return true;
                }
            }
            return false;
        }
    }
}
