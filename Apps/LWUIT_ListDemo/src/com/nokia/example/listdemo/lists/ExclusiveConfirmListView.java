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
import com.sun.lwuit.events.*;

public class ExclusiveConfirmListView extends ListView {

    private ButtonGroup radioButtonGroup;

    public ExclusiveConfirmListView() {
        setTitle("Exclusive + confirm");

        radioButtonGroup = new ButtonGroup();

        for (int i = 0; i < MAX_ITEMS; i++) {
            final RadioButton radioButton = new RadioButton(LIST_ITEM_TEXT
                    + (i + 1));
            radioButton.setUIID("ListItem");
            radioButtonGroup.add(radioButton);
            addComponent(radioButton);
        }
        radioButtonGroup.setSelected(0);

        // Create and add Command for confirming the selection
        Command doneCommand = new Command(Compatibility.toLowerCaseIfFT("Done")) {
            public void actionPerformed(ActionEvent e) {
                showSelectedDialog();
            }
        };
        setDefaultCommand(doneCommand);
        addCommand(doneCommand);

        setShowConfirmationQuery(true);

        layoutContainer();
    }

    public int[] getSelectedIndices() {
        return new int[]{radioButtonGroup.getSelectedIndex()};
    }

    public void setSelectedIndices(int[] indices) {
        radioButtonGroup.setSelected(indices[0]);
    }
}
