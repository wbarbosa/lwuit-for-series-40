/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.data;

import com.nokia.example.birthdays.BirthdayMidlet;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;

/**
 * Access and create new Birthdays via the PIM API.
 * 
 * Does not care about the big picture, only relays data.
 */
public class PIMContactHandler {
    
    public interface ContactFilter {
        public Object filterContact(Contact contact);
    }        
    
    private final long NOW_MILLIS = new Date().getTime();

    private static PIMContactHandler instance;    

    public static PIMContactHandler getInstance() {
        if (instance == null) {
            instance = new PIMContactHandler();
        }
        return instance;
    }
    
    /**
     * Update or create a Contact with birthday in the phone memory.
     * 
     * @param birthday Birthday object
     * @param contact Contact object to update, or null to create new one
     */
    public void addBirthday(Birthday birthday)
        throws SecurityException, PIMException {
        
        ContactList contactList =
            BirthdayMidlet.getInstance().getPIMContactList();
        String[] names =
            new String[contactList.stringArraySize(Contact.NAME)];            
        names[Contact.NAME_GIVEN] = birthday.getName();

        // Create contact with a name if one doesn't exist yet
        Contact contact = birthday.getContact();
        if (contact == null) {
            contact = contactList.createContact();
            contact.addStringArray(Contact.NAME, PIMItem.ATTR_NONE, names);
        }
        
        contact.addDate(Contact.BIRTHDAY, PIMItem.ATTR_NONE,
            birthday.getDate().getTime());

        // Save Contact and close the PIM access
        contact.commit();
    }

    /**
     * Read contacts and create a Vector of Birthdays.
     * 
     * @return Vector object of birthdays for phone contacts
     * @throws PIMNotAccessibleException 
     */
    public Vector getBirthdays() throws PIMNotAccessibleException {
        return getContactsWithFilter(new ContactFilter() {
            
            // Only get contacts with birthdays
            public Object filterContact(Contact contact) {
                Birthday birthday = createBirthdayFromContact(contact);
                if (birthday == null) {
                    return null;
                }
                return birthday;
            }
        });
    }
    
    /**
     * Get a list of contacts with no assigned birthdays.
     * 
     * @return Vector of Contact objects
     * @throws PIMNotAccessibleException 
     */
    public Vector getContactsWithoutBirthday() throws PIMNotAccessibleException {
        return getContactsWithFilter(new ContactFilter() {
            
            // Only include Contacts with no birthday assigned
            public Object filterContact(Contact contact) {                
                if (contact.countValues(Contact.BIRTHDAY) < 1) {
                    return contact;
                }
                return null;
            }
        });
    }    
    
    /**
     * An accessory method for getting lists of contacts from the phone
     * contact list. The method uses a filter object to decide which object
     * should be included in the returned list.
     * 
     * @param filter Filter to decide what the resulting list will contain
     * @return A filtered list of objects
     * @throws PIMNotAccessibleException 
     */
    private Vector getContactsWithFilter(ContactFilter filter)
        throws PIMNotAccessibleException {
        
        Vector contacts = new Vector();
        ContactList contactList =
            BirthdayMidlet.getInstance().getPIMContactList();
        Enumeration contactItems = null;
        
        try {
            contactItems = contactList.items();
        }
        catch (Exception e) {
            throw new PIMNotAccessibleException(e.getMessage());
        }
        
        Contact contact = null;
        while (contactItems.hasMoreElements()) {
            contact = (Contact) contactItems.nextElement();
            
            // Let the filter decide if the Contact should be included or not.
            // If the Filter returns a non-null value, it will be added in
            // the Vector.
            Object filteredContact = filter.filterContact(contact);
            if (filteredContact != null) {
                contacts.addElement(filteredContact);
            }            
        }        
        return contacts;        
    }
    
    private Birthday createBirthdayFromContact(Contact contact) {
        // To make a sensible display item, the Contact needs to have both
        // a name and a birthday
        if (contact.countValues(Contact.BIRTHDAY) < 1 ||
            contact.countValues(Contact.FORMATTED_NAME) < 1) {
            return null;
        }
        
        // Only include birthdays for people that have been born
        long birthdayMillis = contact.getDate(Contact.BIRTHDAY, 0);
        if (birthdayMillis > NOW_MILLIS) {
            return null;
        }

        return new Birthday(
            contact.getString(Contact.FORMATTED_NAME, 0),
            new Date(birthdayMillis),
            contact);
    }
}
