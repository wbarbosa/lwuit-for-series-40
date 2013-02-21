/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view;

import com.nokia.example.lwuit.rlinks.Main;
import com.sun.lwuit.Button;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * A view showing information about the application.
 */
public class AboutView
        extends BaseFormView {

    private static final String ABOUT_TEXT =
            "A LWUIT example application demonstrating "
            + "how to read, comment and vote on Reddit posts.\n\n"
            + "This project is hosted at:";

    public AboutView(BackCommandListener backListener) {
        super("About", backListener);
        setScrollableY(true);
        Main main = Main.getInstance();
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        Image icon = null;
        try {
            icon = Image.createImage("/app_icon.png");
        }
        catch (IOException ioe) {
            System.out.println("Couldn't load app icon.");
        }

        Container infoWrapper = new Container(new BorderLayout());

        infoWrapper.addComponent(BorderLayout.WEST, new Label(icon));

        TextArea appInfo = new TextArea(main.getName() + "\nversion " + main.getVersion()
                + "\nby " + main.getVendor(), 2, 2);
        addComponent(infoWrapper);
        appInfo.setUIID("Label");
        appInfo.setEditable(false);
        appInfo.setGrowByContent(true);

        infoWrapper.addComponent(BorderLayout.CENTER, appInfo);

        TextArea aboutText = new TextArea(ABOUT_TEXT, 2, 2);
        addComponent(aboutText);
        aboutText.setUIID("Label");
        aboutText.setEditable(false);
        aboutText.setGrowByContent(true);

        Button link = new Button("projects.developer.nokia.com/LWUIT_RLinks");
        addComponent(link);
        link.startTicker();
        link.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Main midlet = Main.getInstance();
                try {
                    if (midlet.platformRequest("http://projects.developer.nokia.com/LWUIT_RLinks")) {
                        midlet.exit();
                    }
                }
                catch (ConnectionNotFoundException cnfe) {
                    System.out.println("Connection not found.");
                }
            }
        });
    }

    public void show() {
        setupCommands();
        super.show();
    }

    protected void setupCommands() {
        addCommand(backCommand);
        setBackCommand(backCommand);
    }
}
