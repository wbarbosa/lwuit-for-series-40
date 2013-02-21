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
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

public class ExclusiveListView extends ListView {

    private ButtonGroup radioButtonGroup;

    public ExclusiveListView() {
        setTitle("Exclusive");

        radioButtonGroup = new ButtonGroup();

        for (int i = 0; i < MAX_ITEMS; i++) {
            final RadioButton radioButton = new RadioButton(LIST_ITEM_TEXT
                    + (i + 1));
            radioButton.setUIID("ListItem");
            radioButtonGroup.add(radioButton);
            addComponent(radioButton);
            
            radioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {                   
                    showSelectedDialog();
                }
            });
            
        }
    }

    public int[] getSelectedIndices() {
        return new int[]{radioButtonGroup.getSelectedIndex()};
    }

    public void setSelectedIndices(int[] indices) {
        radioButtonGroup.setSelected(indices[0]);
    }
}
