/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.sun.lwuit.Command;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.plaf.Style;
import java.util.Vector;

public class HelpView
    extends View {

    // Help text to be shown
    private static final String TEXT = "The main view of the application shows "
        + "a list of attractions in the selected guide. "
        + "You can view details of an attraction by tapping it.\n\n"
        + "From the attractions list you can open the map to show your "
        + "current position and if your position is not available the "
        + "center of the guide. By opening the map from the details of an "
        + "attraction, the location of the attraction is shown.\n\n"
        + "From the guides list you can open a different guide or buy "
        + "new guides.";

    public HelpView(TouristMidlet midlet) {
        super(midlet);

        setTitle("Help");
        getStyle().setBgColor(Visual.BACKGROUND_COLOR);

        createTextLabels();
        addCommands();
    }

    private void createTextLabels() {
        Vector lines = Util.splitToLines(TEXT, Visual.SMALL_FONT, getWidth() - 15);

        // Style and add the text labels
        for (int i = 0; i < lines.size(); i++) {
            String text = (String) lines.elementAt(i);

            Label label = new Label(text);
            Style style = label.getStyle();
            style.setFont(Visual.SMALL_FONT);
            style.setFgColor(Visual.LIST_PRIMARY_COLOR);
            style.setPadding(0, 0, 3, 0);
            style.setMargin(0, 0, 0, 0);
            if (text.endsWith("by tapping it.") || text.endsWith("attraction is shown.")) {
                style.setPadding(BOTTOM, 17);
            }
            addComponent(label);
        }        
    }
    
    private void addCommands() {
        // Add back command
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                HelpView.this.midlet.showAttractionView();
            }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);
    }
}