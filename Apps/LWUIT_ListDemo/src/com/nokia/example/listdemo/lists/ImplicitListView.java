/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.listdemo.lists;

import com.nokia.example.listdemo.ListView;
import com.nokia.lwuit.templates.list.NokiaListCellRenderer;
import com.sun.lwuit.List;
import com.sun.lwuit.events.*;
import com.sun.lwuit.list.*;

public class ImplicitListView extends ListView {

    public ImplicitListView() {
        setTitle("Implicit");

        // Create a list model
        ListModel model = new DefaultListModel();
        for (int i = 0; i < MAX_ITEMS; i++) {
            String text = LIST_ITEM_TEXT + (i + 1);
            model.addItem(text);
        }

        // Create a list based on the model
        list = new List(model);

        // Create and set thelist renderer
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
