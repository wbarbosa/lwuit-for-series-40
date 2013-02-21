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
import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.layouts.BorderLayout;

public class WrappedListView
        extends ListView {

    private int selectedIndex;

    public WrappedListView() {
        setTitle("Wrapped");

        // Create list by stacking TextAreas
        for (int i = 0; i < MAX_ITEMS; i++) {
            final int index = i;
            
            String text = LIST_ITEM_TEXT + (i + 1) + LONG_TEXT;

            ListItem listItem = new ListItem(text, new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    selectedIndex = index;
                    showSelectedDialog();
                }
            });

            // Finally add the ListItem to the Form
            addComponent(listItem);
        }

        layoutContainer();
    }

    public int[] getSelectedIndices() {
        return new int[]{selectedIndex};
    }

    public void setSelectedIndices(int[] indices) {
        if (indices.length > 0) {
            selectedIndex = indices[0];
        }
    }

    /**
    * A clickable container for creating lists with variable height.
    */
    public static class ListItem
            extends Container {

        private final Button leadComponent = new Button();

        public ListItem(String text, ActionListener listener) {
            setLayout(new BorderLayout());
            setUIID("ListItem");
            
            leadComponent.addActionListener(listener);
            
            // Add leadComponent to the container to gain traversal functionality
            addComponent(BorderLayout.NORTH, leadComponent);
            leadComponent.setVisible(false);

            setLeadComponent(leadComponent);
            
            // Create the TextArea and add it to the Container
            TextArea textArea = new TextArea();
            textArea.setText(text);
            textArea.setUIID("ListItem");
            textArea.setEditable(false);
            textArea.setRows(2);
            textArea.setGrowByContent(true);
            addComponent(BorderLayout.CENTER, textArea);
        }
        
        /**
         * Capture fire clicked event, so that lead component can be notified.
         * Cannot call leadComponents fireClicked directly, but this is what
         * Button would basically do. This would happen automatically in an
         * ideal world.
         */
        protected void fireClicked() {
            leadComponent.pressed();
            leadComponent.released();
        }
    }
}
