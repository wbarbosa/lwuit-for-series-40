/*
 * Copyright � 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.data;

import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;

/**
 * A representation of the Birthday list handled by the application,
 * sorted in the order of next upcoming birthdays.
 */
public class BirthdayListModel
    extends DefaultListModel
    implements ListModel {
    
    private static BirthdayListModel instance;
    private BirthdaySorter sorter = new BirthdaySorter();
    private PIMContactHandler pimHandler;
    
    // The actual birthday List accessed by this ListModel
    private Vector birthdays;
    
    public static BirthdayListModel getInstance()
        throws PIMNotAccessibleException {
        
        if (instance == null) {
            instance = new BirthdayListModel();
        }
        return instance;
    }
    
    private BirthdayListModel() throws PIMNotAccessibleException {
        pimHandler = PIMContactHandler.getInstance();
        birthdays = pimHandler.getBirthdays();
        sorter.sort(birthdays);
    }
    
    public void addItem(Object o) throws PIMNotAccessibleException {
        Birthday birthday = (Birthday) o;
        try {
            pimHandler.addBirthday(birthday);
        }
        catch (Exception e) {
            throw new PIMNotAccessibleException(e.getMessage());
        }
        
        // Add new item and re-sort. Optimal way would be to insert
        // directly in the correct index.
        birthdays.insertElementAt(birthday, 0);
        sorter.sort(birthdays);
    }
    
    public Object getItemAt(int i) {
        if (birthdays.size() < i + 1) {
            return null;
        }
        return birthdays.elementAt(i);
    }

    public int getSize() {
        return Math.max(1, birthdays.size());
    }
}
