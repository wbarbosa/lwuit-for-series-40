/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.listdemo;

import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.layouts.*;

/**
 * This is a base class for the list demos. It shows the selected items on a
 * <code>Dialog</code> when the list demo is exited, and it shows a confirmation
 * query if Back button is pressed after making changes to the list.
 */
public abstract class ListView extends Form {

    public static final int MAX_ITEMS = 10;
    public final String LIST_ITEM_TEXT =
            Compatibility.toLowerCaseIfFT("List item ");
    public static final String LONG_TEXT =
            ", Lorem ipsum dolor sit amet, consectetur adipiscing "
            + "elit. Phasellus vel neque pulvinar libero vestibulum malesua.";
    protected List list;
    protected int[] originalSelectedIndices; // used when abandoning changes
    private boolean confirmationQuery = false;

    public ListView() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        // The list selection is stored every time the form is shown,
        // so we can see if changes have been made when the form is exited and
        // revert the changes if necessary.
        this.addShowListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                originalSelectedIndices = getSelectedIndices();
            }
        });

        // Add back command for exiting the Form.
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent e) {
                back();
            }
        };
        setBackCommand(backCommand);
    }

    /**
     * Shows a Dialog displaying the selected indices and then shows the MIDlet
     * main view.
     */    
    public void showSelectedDialog() {
        String text = (getSelectedIndices().length == 0 ? "No selected items." : "");
        for (int i = 0; i < getSelectedIndices().length; i++) {
            text += "List item "
                    + (getSelectedIndices()[i] + 1)
                    + " selected.\n";
        }

        Dialog.show(Compatibility.toLowerCaseIfFT("Selected"),
                text,
                new Command(Compatibility.toUpperCaseIfFT("Continue")),
                null,
                Dialog.TYPE_INFO,
                null,
                1500L);

        DemoMidlet.getMainForm().show();
    }

    /**
     * Returns an array containing the indices of selected list items
     * 
     * @return Array containing the selected indices.
     */     
    public int[] getSelectedIndices() {
        return new int[]{list.getSelectedIndex()};
    }

    /**
     * Sets the list items as selected / not selected based on the indices in
     * an array.
     * 
     * @param indices Array containing the selected indices.
     */  
    public void setSelectedIndices(int[] indices) {
        if (indices.length > 0) {
            list.setSelectedIndex(indices[0]);
        }
    }

    /**
     * Turns on or off the confirmation query that is displayed when list is
     * dismissed and changes have been made to the list selection.
     * 
     * @param value is the confirmation query shown.
     */  
    public void setShowConfirmationQuery(boolean value) {
        confirmationQuery = value;
    }

    /**
     * Returns true if the list selections have changed after displaying the
     * list. Otherwise returns false.
     * 
     * @return true if the list has changed, otherwise false.
     */   
    public boolean hasChanged() {
        int[] s = getSelectedIndices();
        if (s.length != originalSelectedIndices.length) {
            return true;
        }
        for (int i = 0; i < s.length; i++) {
            if (s[i] != originalSelectedIndices[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows the ListDemo main view. Displays a confirmation query if needed.
     */      
    protected void back() {
        if (hasChanged() && confirmationQuery) {
            int[] tmp = new int[originalSelectedIndices.length];
            System.arraycopy(originalSelectedIndices, 0, tmp, 0, tmp.length);
            if (Dialog.show("save changes", "Save changes made?", "Yes", "No")) {
                showSelectedDialog(); // Proceed with the changes
            } else {
                setSelectedIndices(tmp); // Abandon changes
                DemoMidlet.getMainForm().show();
            }
        } else {
            DemoMidlet.getMainForm().show();
        }
    }

    public void setTitle(String title) {
        super.setTitle(Compatibility.toLowerCaseIfFT(title));
    }

    public String toString() {
        return getTitle();
    }
}