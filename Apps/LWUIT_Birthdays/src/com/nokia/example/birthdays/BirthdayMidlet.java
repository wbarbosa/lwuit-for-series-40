/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays;

import com.nokia.example.birthdays.data.Birthday;
import com.nokia.example.birthdays.data.BirthdayListModel;
import com.nokia.example.birthdays.data.PIMNotAccessibleException;
import com.nokia.example.birthdays.util.Compatibility;
import com.nokia.example.birthdays.view.BirthdayCreateView;
import com.nokia.example.birthdays.view.BirthdayListView;
import com.nokia.example.birthdays.view.ContactListView;
import com.nokia.example.birthdays.view.listener.BackListener;
import com.nokia.example.birthdays.view.listener.BirthdayCreationListener;
import com.nokia.example.birthdays.view.listener.ContactSelectionListener;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

/**
 * Main application MIDlet. Takes care of the application life cycle and
 * handles transitions between views with listeners.
 */
public class BirthdayMidlet extends MIDlet {

    private static BirthdayMidlet instance;
    
    private BirthdayListView birthdaysListView;
    private ContactListView contactListView;
    private BirthdayCreateView birthdayCreateView;
    
    private ContactList pimContactList;

    /**
     * Singleton - return the MIDlet instance.
     * 
     * @return Instance of the running MIDlet.
     */
    public static BirthdayMidlet getInstance() {
        return instance;
    }
    
    /**
     * Get the PIM ContactList used to read and write Contacts from the phone.
     * Centrally accessed via the main midlet to minimize open/close calls.
     * 
     * @return PIM ContactList instance
     */
    public ContactList getPIMContactList() {
        return pimContactList;
    }

    public BirthdayMidlet() {}

    protected void startApp() throws MIDletStateChangeException {
        instance = this;
        
        // Initialize the Display and set an empty form to enable showing
        // a Dialog on top of it, if needed
        Display.init(this);        
        new Form().show();
        
        // Make sure the PIM API (phone contacts) are accessible and
        // shut down, if not
        boolean pimAccessible = openPIMConnection();        
        if (!pimAccessible) {
            shutDownOnPIMError();
            return;            
        }
        
        // Create application views and show the birthday list view
        createViews();
        birthdaysListView.show();
    }
    
    /**
     * Validate and open the PIM API connection.
     * 
     * @return True if PIM is available, false otherwise
     */
     private boolean openPIMConnection() {
        if (System.getProperty("microedition.pim.version") == null) {
            return false;
        }
            
        try {
            pimContactList = (ContactList)
                PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
            return true;
        }
        // Catch both PIMException and SecurityException
        catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Show an error message and shut down the application
     * in case the PIM API can't be accessed.
     */
    public void shutDownOnPIMError() {
        try {
            showErrorDialog("PIM API required",
                "The PIM API is required but could not be accessed.");
            destroyApp(true);
            notifyDestroyed();
        }
        catch (MIDletStateChangeException ex) {}
    }

    /**
     * Show an error dialog.
     * 
     * @param title Dialog title
     * @param message Dialog text
     */
    public void showErrorDialog(String title, String message) {
        Dialog.show(title, message, Compatibility.toLowerCaseIfFT("Ok"), null);
    }
    
    /**
     * Create application views. 
     */
    private void createViews() throws PIMNotAccessibleException {
        // The view that displays the list of upcoming birthdays
        birthdaysListView = new BirthdayListView(
            new ContactSelectionListener() {
                public void contactSelected(Contact contact) {
                    contactListView.show();
                }
            }, new BackListener() {
                public void backCommanded() {
                    try {
                        destroyApp(true);
                        notifyDestroyed();
                    }
                    catch (MIDletStateChangeException ex) {}
                }
            }
        );

        final BirthdayListModel listModel = BirthdayListModel.getInstance();
        
        // Whenever a new contact is selected for editing, a new instance of
        // BirthdayCreateView is created.
        ContactSelectionListener listener = new ContactSelectionListener() {            
            public void contactSelected(final Contact contact) {
                birthdayCreateView = new BirthdayCreateView(
                    contact,
                    new BirthdayCreationListener() {
                        public void birthdayAdded(Birthday birthday) {
                            try {
                                listModel.addItem(birthday);
                                birthdaysListView.show();
                                contactListView.refresh();
                            }
                            catch (PIMNotAccessibleException ex) {
                                showErrorDialog("Sorry",
                                    "The birthday could not be added. " +
                                    "Please try again.");
                            }
                        }
                    }, new BackListener() {
                        public void backCommanded() {
                            birthdaysListView.show();
                        }
                    }
                );
                birthdayCreateView.show();
            }
        };
        
        contactListView = new ContactListView(listener, new BackListener() {
            public void backCommanded() {
                birthdaysListView.show();
            }
        });
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unconditional)
        throws MIDletStateChangeException {
        
        if (pimContactList != null) {
            try {
                pimContactList.close();
            }
            catch (PIMException ex) {}
        }
    }
}
