/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.categorybardemo;

import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.ElementListener;
import com.nokia.mid.ui.IconCommand;
import com.sun.lwuit.Display;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class DemoMidlet
    extends MIDlet
    implements ElementListener {

    private Vector views;
    private CategoryBar categoryBar;

    /**
     * @see MIDlet#startApp() 
     */
    protected void startApp()
        throws MIDletStateChangeException {
        // Initialize LWUIT Display
        Display.init(this);

        // Initialize views.
        views = new Vector();
        views.addElement(new AddContentView());
        showView(0);
        views.addElement(new ImageView());
        views.addElement(new AboutView(
            getAppProperty("MIDlet-Name"),
            getAppProperty("MIDlet-Vendor"),
            getAppProperty("MIDlet-Version")));

        // Create icon commands for a category bar.
        IconCommand[] iconCommands = new IconCommand[views.size()];
        
        for (int i = 0; i < iconCommands.length; i++) {
            Image icon = (Image) ImageUtil.loadImage(
                ((View) views.elementAt(i)).getIconPath()).getImage();
            iconCommands[i] = new IconCommand(
                ((View) views.elementAt(i)).getLabel(),
                icon,
                null,
                Command.SCREEN, 1);
        }

        // Create a category bar using the icon commands.
        categoryBar = new CategoryBar(iconCommands, true);
        // Set an element listener for the category bar.
        categoryBar.setElementListener(DemoMidlet.this);
        // Show the category bar.
        categoryBar.setVisibility(true);
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
     * @see ElementListener#notifyElementSelected(com.nokia.mid.ui.CategoryBar, int) 
     */
    public void notifyElementSelected(CategoryBar bar, int selectedIndex) {
        try {
            // An icon command in the category bar was pressed.
            showView(selectedIndex);
        }
        catch (IndexOutOfBoundsException e) {
            // Back button was pressed
            notifyDestroyed();
        }
    }

    private void showView(int index) {
        ((View) views.elementAt(index)).show();
    }
}
