/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.view;

import com.nokia.example.birthdays.data.PIMNotAccessibleException;
import com.nokia.example.birthdays.view.listener.BackListener;
import com.nokia.example.birthdays.view.listener.ContactSelectionListener;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import javax.microedition.pim.Contact;

/**
 * The view used to choose the Contact for editing.
 */
public class ContactListView extends Form {
    
    private List contactList;
    private Command backCommand;
    private ContactListModel listModel;
    
    private ContactSelectionListener insertionListener;
    private BackListener backListener;
    
    public ContactListView(ContactSelectionListener insertionListener,
            BackListener backListener) throws PIMNotAccessibleException {
        
        super("Choose contact");
        
        this.insertionListener = insertionListener;
        this.backListener = backListener;
        
        addCommands();
        createList();
    }
    
    public void show() {
        // If there are no contacts without birthday, call the listener
        // directly to signal that there is nobody available for selection
        if (listModel.getSize() == 1) {
            insertionListener.contactSelected(null);
            return;
        }
        
        super.show();
    }
    
    public void refresh() throws PIMNotAccessibleException {
        listModel.refresh();
    }
    
    private void createList() throws PIMNotAccessibleException {
        contactList = new List();
        listModel = ContactListModel.getInstance();
        contactList.setModel(listModel);
        contactList.setRenderer(new ContactListItemRenderer());
        contactList.setCommandList(true);
        
        // When a Contact has been selected, notify the insertionListener
        contactList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Contact contact = (Contact) contactList.getSelectedItem();
                insertionListener.contactSelected(contact);
            }
        });
        
        addComponent(contactList);
    }
    
    private void addCommands() {
        backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent e) {
                backListener.backCommanded();
            }
        };
        addCommand(backCommand);
        setBackCommand(backCommand);
    }
}
