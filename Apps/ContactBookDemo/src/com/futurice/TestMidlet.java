/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futurice;

import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.Storage;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Enumeration;
import javax.microedition.midlet.MIDlet;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

/**
 * @author ilau
 */
public class TestMidlet extends MIDlet {
    private List contactList = null;
    private Form form;
    private PIM pim = null;
    
    public void startApp() {
        Display.init(this);
        Storage.init("LwuitTest");
        form = new Form("Hello world");
        
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
        form.addComponent(searchButton);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                populateContactList(searchField.getText());
            }
        });
        populateContactList(searchField.getText());
        
        form.show();
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
        } catch (PIMException ex) {
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
