/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.contactbookdemo;

import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Enumeration;
import javax.microedition.midlet.MIDlet;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

public class ContactBookDemo
    extends MIDlet {

    private List contactList = null;
    private PIM pim = null;

    public void startApp() {
        Display.init(this);
        Form form = new Form("ContactBookDemo");

        Command exitCommand = new Command("Exit") {

            public void actionPerformed(ActionEvent e) {
                notifyDestroyed();
            }
        };
        form.addCommand(exitCommand);
        form.setBackCommand(exitCommand);

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        contactList = new List();
        pim = PIM.getInstance();
        form.addComponent(contactList);

        final TextArea searchField = TextField.create();
        form.addComponent(searchField);
        
        Button searchButton = new Button("Search");
        searchButton.setPreferredW(form.getWidth() / 2 - 5);
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                populateContactList(searchField.getText());
            }
        });
        Button clearButton = new Button("Clear");
        clearButton.setPreferredW(form.getWidth() / 2 - 5);
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                searchField.setText("");
                populateContactList("");
            }
        });
        Container buttonContainer = new Container();
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.addComponent(BorderLayout.WEST, searchButton);
        buttonContainer.addComponent(BorderLayout.EAST, clearButton);
        form.addComponent(buttonContainer);

        populateContactList(searchField.getText());

        form.show();
        form.setFocused(searchField);
    }

    private void populateContactList(String searchTerm) {
        contactList.setModel(new DefaultListModel());
        try {
            String[] pimListNames = pim.listPIMLists(PIM.CONTACT_LIST);
            for (int i = 0; i < pimListNames.length; ++i) {
                ContactList cl = (ContactList) pim.openPIMList(
                    PIM.CONTACT_LIST, PIM.READ_ONLY, pimListNames[i]);
                Enumeration items = cl.items(searchTerm);
                while (items.hasMoreElements()) {
                    Contact c = (Contact) items.nextElement();
                    contactList.addItem(c.getString(Contact.FORMATTED_NAME, 0));
                }
            }
        }
        catch (PIMException ex) {
            ex.printStackTrace();
        }
        if (contactList.getModel().getSize() == 0) {
            contactList.addItem("No matches");
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
