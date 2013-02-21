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
import com.nokia.example.lwuit.rlinks.model.LinkThing;
import com.nokia.example.lwuit.rlinks.network.HttpClient;
import com.nokia.example.lwuit.rlinks.network.HttpOperation;
import com.nokia.example.lwuit.rlinks.network.operation.LinksLoadOperation;
import com.nokia.example.lwuit.rlinks.network.operation.LinksLoadOperation.LoadLinksListener;
import com.nokia.example.lwuit.rlinks.view.item.LinkItem;
import com.nokia.example.lwuit.rlinks.view.item.LinkItem.LinkSelectionListener;
import com.nokia.example.lwuit.rlinks.view.item.LoaderItem;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;

/**
 * The LinksView is the default view of the application. It lists all links under a certain reddit (category).
 */
public class LinksView
        extends BaseFormView
        implements LoadLinksListener {

    private final Command categoryCommand = new Command("Category");
    private final Command refreshCommand;
    private final String category;
    private final CategorySelectionListener categoryListener;
    private final LinkSelectionListener linkListener;
    private HttpOperation operation;
    private Container linkList;
    private LoaderItem loaderItem;
    private static boolean firstShow = true;
    private volatile boolean refreshing = false;

    /**
     * Create a LinksView.
     *
     * @param category Category to show links for
     * @param categoryListener Listener to signal of category change
     * @param linkListener Listener to signal of link being selected
     */
    public LinksView(String category, BackCommandListener backListener, CategorySelectionListener categoryListener,
                     LinkSelectionListener linkListener) {
        super("RLinks", backListener);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        this.category = category;
        this.categoryListener = categoryListener;
        this.linkListener = linkListener;

        clearList();

        loaderItem = new LoaderItem("loading_rlinks.png", 12);

        if (Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
            Image icon = Main.getResources().getImage("refresh.png");
            refreshCommand = new Command("Refresh", icon);
        }
        else {
            refreshCommand = new Command("Refresh");
        }
    }

    protected final void setupCommands() {
        removeAllCommands();

        setBackCommand(exitCommand);
        addCommand(aboutCommand);
        setupLoginCommands();
        addCommand(categoryCommand);
        setDefaultCommand(refreshCommand);
    }

    /**
     * Show the view.
     */
    public void show() {
        if (firstShow) {
            if (!requireNetworkAccess()) {
                return;
            }
            firstShow = false;
        }

        setupCommands();
        setTitle(category == null ? "RLinks - Popular" : "/r/" + category);
        addLoginStatusItem();
        loadLinks();
        super.show();
    }

    /**
     * Refresh the view.
     */
    private void refresh() {
        if (refreshing || operation != null && !operation.isFinished()) {
            return;
        }
        operation = null;
        refreshing = true;

        clearList();
        show();
    }

    /**
     * Load links to be shown in the view.
     */
    private void loadLinks() {
        // Do not reload if already loaded, or if in process
        if (HttpOperation.reloadNeeded(operation)) {
            // Show loading indicator
            refreshing = true;
            addComponent(loaderItem);
            operation = new LinksLoadOperation(category, this);
            operation.start();
        }
    }

    /**
     * Handle displaying incoming links.
     *
     * @param links Vector of LinkThing items received
     */
    public void linksReceived(Vector links) {

        Display d = Display.getInstance();

        if (links == null) {
            // UI should always be modified in EDT
            d.callSerially(new Runnable() {

                public void run() {
                    removeComponent(loaderItem);
                    refreshing = false;
                    showNetworkError();
                }
            });
            return;
        }

        final boolean showSubreddit = category == null;
        for (int i = 0, len = links.size(); i < len; i++) {
            try {
                final int index = i;
                final LinkThing link = (LinkThing) links.elementAt(index);
                final LinkItem item = new LinkItem(link, linkListener, showSubreddit, imageCache);

                Runnable addItem = new Runnable() {

                    public void run() {

                        linkList.addComponent(item);
                    }
                };

                if (i < 7 || i == len - 1) {
                    // Add one item at a time and repaint the form
                    d.callSeriallyAndWait(addItem);
                    repaint();
                }
                else {
                    // Add items outside the screen. No need to repaint.
                    d.callSerially(addItem);
                }
            }
            catch (OutOfMemoryError oome) {
                return;
            }
        }

        d.callSerially(new Runnable() {

            public void run() {
                removeComponent(loaderItem);
                refreshing = false;
            }
        });
    }

    /**
     * Handle command actions
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        Command cmd = ae.getCommand();
        if (cmd == backCommand) {
            // Going back equals going back to the first view (popular page)
            abortPendingOperations();
            categoryListener.categorySelected(null);
        }
        else if (cmd == categoryCommand) {
            showCategoryView();
        }
        else if (cmd == refreshCommand) {
            refresh();
        }
        super.actionPerformed(ae);
    }

    /**
     * Create empty list and remove old one
     * @return List
     */
    private void clearList() {
        if (linkList != null) {
            removeComponent(linkList);
            linkList = null;
        }
        linkList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        linkList.setFocusable(false);
        addComponent(linkList);
    }

    /**
     * Abort any pending loading operation.
     */
    private void abortPendingOperations() {
        if (operation != null && !operation.isFinished()) {
            operation.abort();
            removeComponent(loaderItem);
        }
    }

    /**
     * Show the category selection view.
     */
    private void showCategoryView() {
        final CategorySelectView csv = new CategorySelectView(category, this, categoryListener);
        csv.show();
    }

    private boolean requireNetworkAccess() {
        if (HttpClient.isAllowed()) {
            return true;
        }
        setTitle("Error");
        TextArea message = new TextArea("Please restart the application and allow network access.", 2, 2);
        message.setUIID("Label");
        addComponent(message);
        message.setGrowByContent(true);
        setBackCommand(exitCommand);
        super.show();
        return false;
    }
}
