/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.gesturesdemo;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.Layout;

public class ImageGridLayout extends Layout {

    private int columns;
    private Dimension dimension;

    public ImageGridLayout(int columns) {
        this.columns = columns;
        dimension = new Dimension(0, 0);
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getColumns() {
        return columns;
    }

    public void layoutContainer(Container parent) {
        int componentCount = parent.getComponentCount();
        int colWidth = parent.getWidth() / columns;

        for (int i = 0; i < componentCount; i++) {
            Component c = parent.getComponentAt(i);
            c.setWidth(colWidth);
            c.setHeight(colWidth);
            c.setX(i % columns * colWidth);
            c.setY((int) (i / columns) * colWidth);
        }
    }

    public Dimension getPreferredSize(Container parent) {
        int componentCount = parent.getComponentCount();
        int colWidth = parent.getWidth() / columns;
        int rows = componentCount / columns;
        dimension.setWidth(parent.getWidth());
        dimension.setHeight(rows * colWidth);
        return dimension;
    }
}
