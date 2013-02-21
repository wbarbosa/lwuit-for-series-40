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
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Enumeration;
import java.util.Vector;

public class MultipleListView extends ListView {

    private Vector checkBoxVector;

    public MultipleListView() {
        setTitle("Multiple");

        // The list constists of CheckBoxes in a Form using vertical BoxLayout.
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        checkBoxVector = new Vector();
        for (int i = 0; i < MAX_ITEMS; i++) {
            CheckBox checkBox = new CheckBox(LIST_ITEM_TEXT + (i + 1));
            checkBox.setUIID("ListItem");
            checkBoxVector.addElement(checkBox);
            addComponent(i, checkBox);
        }

        // Create and add Command for confirming the selection
        Command doneCommand = new Command(Compatibility.toLowerCaseIfFT("Done")) {
            public void actionPerformed(ActionEvent e) {
                showSelectedDialog();
            }
        };
        if (Display.getInstance().isTouchScreenDevice()) {
            setDefaultCommand(doneCommand);
        }
        addCommand(doneCommand);

        setShowConfirmationQuery(true);
    }
    
    /**
     * Returns an array with the indices of the selected items in the list.
     * Overrides the getSelectedIndices in ListView class.
     */
    public int[] getSelectedIndices() {
        // Count the number of selected items
        int numOfSelectedItems = 0;
        Enumeration e = checkBoxVector.elements();
        while (e.hasMoreElements()) {
            CheckBox checkBox = (CheckBox) e.nextElement();
            if (checkBox.isSelected()) {
                numOfSelectedItems++;
            }
        }
        
        // Create array that holds the indices of selected items
        int[] indices = new int[numOfSelectedItems];
        
        // Find the indices of selected items and store them in the array
        int itemIndex = 0;
        int indicesIndex = 0;
        e = checkBoxVector.elements();
        while (e.hasMoreElements()) {
            CheckBox checkBox = (CheckBox) e.nextElement();
            if (checkBox.isSelected()) {
                indices[indicesIndex] = itemIndex;
                indicesIndex++;
            }
            itemIndex++;
        }
        
        return indices;
    }

    /**
     * Sets list items as selected according to the array of indices passed as
     * parameter. Overrides the setSelectedIndices in ListView class.
     */
    public void setSelectedIndices(int[] indices) {
        Enumeration e = checkBoxVector.elements();
        while (e.hasMoreElements()) {
            ((CheckBox) e.nextElement()).setSelected(false);
        }
        for (int i = 0; i < indices.length; i++) {
            ((CheckBox) checkBoxVector.elementAt(indices[i])).setSelected(true);
        }
    }
}
