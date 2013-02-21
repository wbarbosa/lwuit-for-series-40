/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.listdemo.lists;

import com.nokia.example.listdemo.*;
import com.nokia.lwuit.templates.list.NokiaListCellRenderer;
import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.list.*;

public class ThumbnailsListView extends ListView {

    public ThumbnailsListView() {
        setTitle("Thumbnails");
        // Create list model.
        // Commands with icons are used here as list items for displaying the
        // thumbnails.
        ListModel model = new DefaultListModel();
        for (int i = 0; i < MAX_ITEMS; i++) {
            String text = LIST_ITEM_TEXT + (i + 1);
            Command item = new Command(text, DemoMidlet.loadImage("/thumbnail_"
                    + (i + 1) + ".png"));
            model.addItem(item);
        }

        // Create list based on the model
        list = new List(model);

        // Create and set list renderer
        ListCellRenderer renderer = new NokiaListCellRenderer();
        list.setRenderer(renderer);

        // Add ActionListener for the list
        list.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        showSelectedDialog();
                    }
                });

        addComponent(list);
        layoutContainer();
    }
}
