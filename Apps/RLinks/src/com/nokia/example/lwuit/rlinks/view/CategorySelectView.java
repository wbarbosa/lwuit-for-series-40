/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view;

import com.nokia.example.lwuit.rlinks.view.item.CategoryItem;
import com.sun.lwuit.layouts.FlowLayout;

/**
 * A view for selecting active category (subreddit).
 */
public class CategorySelectView
        extends BaseFormView {

    /**
     * A prepopulated list of common subreddits.
     * There are tens of thousands in total.
     */
    private static final String[] CATEGORIES = {
        "AdviceAnimals",
        "AskReddit",
        "askscience",
        "aww",
        "bestof",
        "funny",
        "gaming",
        "IAmA",
        "Music",
        "nokia",
        "pics",
        "politics",
        "science",
        "technology",
        "todayilearned",
        "worldnews",
        "WTF"
    };
    private final CategorySelectionListener categoryListener;
    private final String currentCategory;

    /**
     * Create a new view.
     *
     * @param currentCategory Currently selected category (null if none)
     * @param categoryListener Listener to signal about category changes
     * @param backListener Listener to signal about back button presses
     */
    public CategorySelectView(String currentCategory, BackCommandListener backListener,
                              CategorySelectionListener categoryListener) {
        super("Select category", backListener);

        this.currentCategory = currentCategory;
        this.categoryListener = categoryListener;

        setLayout(new FlowLayout());

        setupCommands();
    }

    protected final void setupCommands() {
        addCommand(backCommand);
        setBackCommand(backCommand);
    }

    public void show() {
        CategoryItem item;

        // Add default item 'Top links'
        item = new CategoryItem(null, categoryListener);
        if (currentCategory == null) {
            item.setSelected(true);
        }
        addComponent(item);

        // Add other items for categories
        for (int i = 0, len = CATEGORIES.length; i < len; i++) {
            item = new CategoryItem(CATEGORIES[i], categoryListener);
            if (CATEGORIES[i].equals(currentCategory)) {
                item.setSelected(true);
            }
            addComponent(item);
        }
        super.show();
    }
}
