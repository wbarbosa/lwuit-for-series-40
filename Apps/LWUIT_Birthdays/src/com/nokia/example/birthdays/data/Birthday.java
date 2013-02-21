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

import java.util.Calendar;
import java.util.Date;
import javax.microedition.pim.Contact;

/**
 * A representation of a person's birthday. Contains a reference to a
 * PIM Contact if one is available.
 */
public class Birthday {
    
    private String name;
    private Calendar birthday = Calendar.getInstance();
    private Contact contact;

    public Birthday(String name, Date date, Contact contact) {
        this.name = name;
        this.birthday.setTime(date);
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return birthday.getTime();
    }
    
    public Contact getContact() {
        return contact;
    }

    public String toString() {
        return name + " (" + birthday.getTime() + ")";
    }
}
