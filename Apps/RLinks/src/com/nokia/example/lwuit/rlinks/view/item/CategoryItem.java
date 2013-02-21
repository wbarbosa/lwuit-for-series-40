/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.view.BaseFormView.CategorySelectionListener;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.plaf.Style;

/**
 * A custom view item for representing a category.
 */
public class CategoryItem
        extends ListItem {

    private static final String DEFAULT_CATEGORY_NAME = "Popular (default)";
    private static final Font regular = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    private static final Font bold = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private final CategorySelectionListener listener;
    private final Label title = new Label(DEFAULT_CATEGORY_NAME);
    private final String category;

    /**
     * Create a CategoryItem.
     *
     * @param category Category name represented by this item
     * @param preferredWidth Preferred width
     * @param listener Listener to signal of category selections
     */
    public CategoryItem(String category, CategorySelectionListener listener) {
        this.category = category;
        this.listener = listener;
        title.setUIID("CategoryLabel");
        title.setText(category != null ? category : DEFAULT_CATEGORY_NAME);
        addComponent(title);
    }

    public void setSelected(boolean selected) {
        Style style = title.getStyle();
        style.setFont(selected ? bold : regular);
        title.setSelectedStyle(style);
        title.setPressedStyle(style);
    }

    public void actionPerformed(ActionEvent evt) {
        listener.categorySelected(category);
    }
}
