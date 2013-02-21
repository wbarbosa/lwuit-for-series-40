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

import com.nokia.example.birthdays.BirthdayMidlet;
import com.nokia.example.birthdays.data.Birthday;
import com.nokia.example.birthdays.view.listener.BackListener;
import com.nokia.example.birthdays.view.listener.BirthdayCreationListener;
import com.sun.lwuit.Calendar;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import java.util.Date;
import javax.microedition.pim.Contact;

/**
 * View for creating a new birthday. Can be used to create either a new contact
 * or just add a birthday for an existing one.
 */
public class BirthdayCreateView extends Form {

    private Contact contact;
    private Command saveCommand;
    private Command backCommand;
    
    private Label nameLabel;
    private Label dateLabel;
    private Calendar calendar;
    private TextField nameField;
    

    public BirthdayCreateView(final Contact contact,
        final BirthdayCreationListener birthdayListener,
        final BackListener backListener) {
        
        super("Add birthday");
        this.contact = contact;
        
        createComponents();
        initializeCommands(birthdayListener, backListener);
    }
    
    private void createComponents() {
        nameLabel = new Label("Name");
        nameField = new TextField();
        nameField.setLabelForComponent(nameLabel);
        
        // If a contact has been assigned, use it to populate the name field
        if (contact != null) {            
            String name = contact.getString(Contact.FORMATTED_NAME, 0);
            nameField.setText(name);
            nameField.setEditable(false);
            
            // Make the TextField appear as a Label, to avoid branching
            // in code but still make it clear to the user that the field
            // is not editable.
            nameField.setUIID("label");
        }        
        
        dateLabel = new Label("Date of birth");
        calendar = new Calendar();
        calendar.setLabelForComponent(dateLabel);
        calendar.setDate(new Date(0));
        
        addComponent(nameLabel);
        addComponent(nameField);
        addComponent(dateLabel);
        addComponent(calendar);
    }
    
    private void initializeCommands(
        final BirthdayCreationListener birthdayListener,
        final BackListener backListener) {
        
        // 'Save' validates the created object and notifies the listener 
        saveCommand = new Command("Save") {
            public void actionPerformed(ActionEvent e) {
                validateAndSave(birthdayListener);
            }
        };
        addCommand(saveCommand);
        setDefaultCommand(saveCommand);

        // 'Back' notifies the backListener
        backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent e) {
                backListener.backCommanded();
            }
        };
        addCommand(backCommand);
        setBackCommand(backCommand);
    }
    
    private void validateAndSave(final BirthdayCreationListener birthdayListener) {
        Date selectedDate = calendar.getDate();
        if ("".equals(nameField.getText())) {
            BirthdayMidlet.getInstance().showErrorDialog(
                "Name empty", "Please enter a name.");
            return;
        }
        else if (selectedDate.getTime() > new Date().getTime()) {
            BirthdayMidlet.getInstance().showErrorDialog(
                "Invalid date", "Birthday must be in the past.");
            return;
        }
        
        // Set the event to happen at around 10am
        selectedDate = adjustTimeOfDay(selectedDate);
        birthdayListener.birthdayAdded(
            new Birthday(nameField.getText(), selectedDate, contact));
    }
    
    private Date adjustTimeOfDay(Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        c.set(java.util.Calendar.HOUR_OF_DAY, 10);
        
        return c.getTime();        
    }
}
