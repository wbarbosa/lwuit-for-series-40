/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.lists.GuideList;
import com.nokia.example.touristattractions.lists.GuideListRenderer;
import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.util.Compatibility;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

public class GuideView
    extends View
    implements ActionListener {

    private GuideList guideList;
    private Command oviCommand;
    private Command helpCommand;
    private Command aboutCommand;
    
    // Indicate if pointer has been dragged after press event
    private boolean dragStarted = false;

    public GuideView(TouristMidlet midlet) {
        super(midlet);

        setTitle("Select guide");
        getStyle().setBgColor(Visual.BACKGROUND_COLOR);
        guideList = new GuideList(this);
        guideList.setSelectedItem(null);

        addCommands();
    }
    
    private void addCommands() {
        addCommandListener(this);
        
        // If IAP supported add purchase command
        if (Compatibility.IAP_SUPPORTED) {
            // Add command icon only on FT devices
            Image oviCommandImage = null;
            if (Compatibility.SCREEN_SIZE == Compatibility.RES_240x400) {
                oviCommandImage = Util.loadImage("icons/ovi_store.png");
            }
            
            oviCommand = new Command("Buy guides", oviCommandImage) {
                public void actionPerformed(ActionEvent e) {
                }
            };
            addCommand(oviCommand);
        }
        
        helpCommand = new Command("Help");
        aboutCommand = new Command("About");
        addCommand(helpCommand);
        addCommand(aboutCommand);

        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent e) {
                GuideView.this.midlet.showAttractionView(); 
           }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);
    }

    /* some custom handling added for a list item highlight after tapping */
    public void pointerPressed(int x, int y) {
        ((GuideListRenderer) guideList.getRenderer()).setReleased(false);
        super.pointerPressed(x, y);
    }

    public void pointerDragged(int x, int y) {
        dragStarted = true;
        super.pointerDragged(x, y);
    }

    public void pointerReleased(int x, int y) {
        if (!dragStarted && !getMenuBar().contains(x, y)) {
            ((GuideListRenderer) guideList.getRenderer()).setReleased(true);
        }
        super.pointerReleased(x, y);
        dragStarted = false;
    }
    
    public void actionPerformed(ActionEvent e) {
        Command cmd = e.getCommand();
        if (cmd == oviCommand) {
            Display.getInstance().callSerially(
                new Runnable() {
                    public void run() {
                        GuideView.this.midlet.showBuyGuidesView();
                    }
                });
        }
        else if (cmd == aboutCommand) {
            midlet.showAboutView();
        }
        else if (cmd == helpCommand) {
            midlet.showHelpView();
        }
    }

    /*
     * Hack for lwuit s40 list bug
     */
    public void show() {
        super.show();
        if (!this.contains(guideList)) {
            addComponent(guideList);
            guideList.setSelectedItem(null);
        }
        this.repaint();
    }

    public GuideList getGuideList() {
        return guideList;
    }
}
