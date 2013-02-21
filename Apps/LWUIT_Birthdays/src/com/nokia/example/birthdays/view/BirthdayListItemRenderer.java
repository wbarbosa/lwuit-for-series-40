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

import com.nokia.example.birthdays.Visual;
import com.nokia.example.birthdays.data.Birthday;
import com.nokia.example.birthdays.util.BirthdayPrettyPrinter;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;

/**
 * A renderer for a Birthday list item.
 */
public class BirthdayListItemRenderer
    extends Container
    implements ListCellRenderer {
    
    private Label nameLabel;
    private Label dateLabel;
    
    public BirthdayListItemRenderer() {
        setPreferredW(Display.getInstance().getDisplayWidth());
        setPreferredH(40);
        
        initializeStyles();
        createLayout();
    }
    
    private void initializeStyles() {
        Style style = getStyle();
        style.setPadding(0, 0, 5, 5);
        style.setMargin(5, 5, 0, 0);
    
        nameLabel = new Label();
        style = nameLabel.getStyle();
        style.setFont(Visual.MEDIUM_FONT);
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);

        dateLabel = new Label();
        style = dateLabel.getStyle();
        style.setFont(Visual.SMALL_FONT);
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
    }
    
    private void createLayout() {
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        
        addComponent(BorderLayout.NORTH, nameLabel);
        addComponent(BorderLayout.SOUTH, dateLabel);
    }

    public Component getListCellRendererComponent(List list, Object object,
        int index, boolean isSelected) {
        
        final Birthday birthday = (Birthday) object;
        
        // A null component at index 0 is used to signal that the list is
        // empty. While this *can* be done with list.setHint(text), in this
        // case we'll customize the item to appear nicer.
        if (birthday == null) {
            nameLabel.setText("No birthdays to show");
            dateLabel.setText("Why not add one?");
            return this;
        }
        
        // Frank, 3
        // 14 Jan 2009 (in 4 months)        
        nameLabel.setText(birthday.getName() + ", " +
            BirthdayPrettyPrinter.getFormattedAgeOnNextBirthday(birthday));
        
        dateLabel.setText(
            BirthdayPrettyPrinter.getFormattedBirthDate(birthday) + " (" +
            BirthdayPrettyPrinter.getTimeUntilNextOccurrence(birthday) + ")");
        
        return this;
    }

    public Component getListFocusComponent(List list) {
        return null;
    }
}
