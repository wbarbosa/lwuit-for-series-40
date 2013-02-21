/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.view.listener;

import javax.microedition.pim.Contact;

/**
 * Listener interface to signal a Contact has been selected.
 */
public interface ContactSelectionListener {

    public void contactSelected(Contact contact);
    
}
