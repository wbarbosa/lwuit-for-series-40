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
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;
import javax.microedition.pim.Contact;

/**
 * A renderer for a Contact list item.
 */
public class ContactListItemRenderer
    extends Container
    implements ListCellRenderer {
    
    private Label label;
    private Label focusComponent;
    
    public ContactListItemRenderer() {
        setPreferredW(Display.getInstance().getDisplayWidth());
        setPreferredH(35);
        
        initializeStyles();
        createLayout();
    }
    
    private void initializeStyles() {
        Style style = getStyle();
        style.setPadding(0, 0, 5, 5);
        style.setMargin(5, 5, 0, 0);
    
        label = new Label();
        label.getStyle().setFont(Visual.MEDIUM_FONT);
        label.getStyle().setBgTransparency(0);

        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
    }
    
    private void createLayout() {
        setLayout(new FlowLayout());
        addComponent(label);
    }

    public Component getListCellRendererComponent(List list, Object object,
        int index, boolean isSelected) {
        
        // Make sure selection is properly drawn also in non-touch devices
        // (not needed for full touch)
        if (!Display.getInstance().shouldRenderSelection(list)) {
            isSelected = false;
        }
        setFocus(isSelected);
        
        String text = "Create new contact";
        if (index > 0) {
            text = ((Contact) object)
                .getString(Contact.FORMATTED_NAME, Contact.ATTR_NONE);        
        }
        label.setText(text);
        
        return this;
    }

    public Component getListFocusComponent(List list) {
        if (focusComponent == null) {
            focusComponent = new Label("");
            focusComponent.setUIID("ListRendererFocus");
            focusComponent.setFocus(true);
            focusComponent.setCellRenderer(true);
        }
        return focusComponent;
    }
}
