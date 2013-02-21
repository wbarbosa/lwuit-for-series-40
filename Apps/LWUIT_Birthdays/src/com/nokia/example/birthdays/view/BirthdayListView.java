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

import com.nokia.example.birthdays.data.BirthdayListModel;
import com.nokia.example.birthdays.data.PIMNotAccessibleException;
import com.nokia.example.birthdays.util.Commands;
import com.nokia.example.birthdays.util.Compatibility;
import com.nokia.example.birthdays.view.listener.BackListener;
import com.nokia.example.birthdays.view.listener.ContactSelectionListener;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;

/*
 * View that displays a list of upcoming birthdays, and allows creating
 * a new birthday entry.
 * 
 * This view is intentionally very thing. The core logic for handling
 * retrieval and addition of contacts is contained in the BirthdayListModel.
 */
public class BirthdayListView extends Form {

    private Command addCommand;
    private Command backCommand;
    
    private List birthdayList;
    private BirthdayListModel listModel;
    private ContactSelectionListener contactListener;
    private BackListener backListener;

    public BirthdayListView(ContactSelectionListener contactListener,
        final BackListener backListener) throws PIMNotAccessibleException {
        super("Birthdays");
        this.contactListener = contactListener;
        this.backListener = backListener;
        
        addCommands();
        createList();
    }
    
    private void createList() throws PIMNotAccessibleException {        
        birthdayList = new List();
        
        listModel = BirthdayListModel.getInstance();
        birthdayList.setModel(listModel);
        birthdayList.setRenderer(new BirthdayListItemRenderer());
        
        addComponent(birthdayList);
    }
    
    private void addCommands() {
        // When running on a full touch device, add the command icon
        Image addCommandImage = null;
        if (Compatibility.isFullTouch()) {
            addCommandImage = Commands.loadImage(Commands.ADD_COMMAND_IMAGE);
        }
        
        addCommand = new Command("Add", addCommandImage) {
            public void actionPerformed(ActionEvent e) {
                contactListener.contactSelected(null);
            }
        };
        addCommand(addCommand);
        setDefaultCommand(addCommand);

        backCommand = new Command("Exit") {
            public void actionPerformed(ActionEvent e) {
                backListener.backCommanded();                
            }
        };
        addCommand(backCommand);
        setBackCommand(backCommand);        
    }
}
