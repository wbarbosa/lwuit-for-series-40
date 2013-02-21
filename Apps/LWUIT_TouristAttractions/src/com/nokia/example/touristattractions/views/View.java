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
import com.sun.lwuit.*;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.list.DefaultListCellRenderer;

/**
 * An abstract implementation for Tourist Attraction view. Extends LWUIT form.
 */
public abstract class View
    extends Form {

    public static Image defaultThumbnail; // default image for images that are being loaded
    protected TouristMidlet midlet;

    public View(TouristMidlet midlet) {
        super();
        this.midlet = midlet;

        // Style title bar
        getTitleArea().getStyle().setPadding(0, 0, 0, 0);
        getTitleStyle().setBgPainter(new TitleBgPainter());
        getTitleStyle().setFgColor(0xeeeeee);
        getTitleComponent().getStyle().setPadding(LEFT, 16);

        // Style command menu
        TouristMenuBar menuBar = new TouristMenuBar();
        menuBar.init();
        this.setMenuBar(menuBar);
        this.getMenuBar().setMenuCellRenderer(
            new DefaultListCellRenderer(false));

        defaultThumbnail = Util.loadImage("thumbnail.png");
    }

    public TouristMidlet getMidlet() {
        return midlet;
    }

    public int getContentAreaHeight() {
        return getHeight() - getTitleComponent().getHeight() -
            getMenuBar().getHeight();
    }

    /**
     * New menu bar for application specified command menu dialog.
     */
    class TouristMenuBar
        extends MenuBar {

        public TouristMenuBar() {
            super();
        }

        protected Command showMenuDialog(Dialog menu) {
            int marginLeft = (int) (this.getWidth() * 0.25f);
            int marginRight = 0;
            int commandCount = this.getCommandCount();
            
            int height = (menu.getContentPane().getComponentAt(0)
                .getPreferredH() / commandCount) * (commandCount + 2);
            height = height + 5;

            height = View.this.getHeight() - height;
            if (height < 0) {
                height = 0;
            }
            return menu.show(height, 0, marginLeft, marginRight, true);
        }

        public void init() {
            this.initMenuBar(View.this);
        }
    }

    class TitleBgPainter
        implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            g.fillLinearGradient(0x333333, 0x444444, 0, 0,
                rect.getSize().getWidth(), rect.getSize(). getHeight(), false);
        }
    }
}
