/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.listdemo;

import com.nokia.example.listdemo.lists.*;
import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.IOException;
import javax.microedition.midlet.*;

public class DemoMidlet
        extends MIDlet {

    private Form mainForm;
    private List mainMenuList;
    private Form[] listViews;
    private static DemoMidlet instance;

    /**
     * @see MIDlet#startApp()
     */
    protected void startApp()
            throws MIDletStateChangeException {
        Display.init(this); // initialize LWUIT Display        
        instance = this;

        // Initialize demos
        listViews = new Form[]{
            new ImplicitListView(),
            new ImplicitActionListView(),
            new ExclusiveListView(),
            new ExclusiveConfirmListView(),
            new MultipleListView(),
            new TruncatedListView(),
            new WrappedListView(),
            new ThumbnailsListView(),
            new TwoRowListView()
        };

        // Create main view
        mainForm = new Form(Compatibility.toLowerCaseIfFT("List examples"));
        mainForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        mainMenuList = new List(listViews);
        mainMenuList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                listViews[mainMenuList.getSelectedIndex()].show();
            }
        });
        mainForm.addComponent(mainMenuList);

        Command backCommand = new Command("Exit") {
            public void actionPerformed(ActionEvent e) {
                notifyDestroyed();
            }
        };
        mainForm.setBackCommand(backCommand);

        mainForm.show();
    }

    /**
     * @see MIDlet#pauseApp()
     */
    protected void pauseApp() {
    }

    /**
     * @see MIDlet#destroyApp(boolean)
     */
    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
    }

    /**
     * This method returns the application main view.
     *
     * @return main <code>Form</code>
     */
    public static Form getMainForm() {
        return instance.mainForm;
    }

    /**
     * Loads the image in the resources directory with the given path.
     *
     * @param path Path of the image.
     * @return Image found with the given path
     */
    public static Image loadImage(String path) {
        try {
            // load image
            return Image.createImage(path);
        } catch (IOException ioe) {
        }
        return null;
    }
}
