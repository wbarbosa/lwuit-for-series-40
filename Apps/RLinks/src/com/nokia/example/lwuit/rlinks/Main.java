/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks;

import com.nokia.example.lwuit.rlinks.model.LinkThing;
import com.nokia.example.lwuit.rlinks.util.ViewCache;
import com.nokia.example.lwuit.rlinks.view.BackCommandListener;
import com.nokia.example.lwuit.rlinks.view.BaseFormView.CategorySelectionListener;
import com.nokia.example.lwuit.rlinks.view.CommentsView;
import com.nokia.example.lwuit.rlinks.view.LinksView;
import com.nokia.example.lwuit.rlinks.view.SplashView;
import com.nokia.example.lwuit.rlinks.view.item.LinkItem.LinkSelectionListener;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import javax.microedition.midlet.*;

public class Main
        extends MIDlet
        implements BackCommandListener, LinkSelectionListener, CategorySelectionListener {

    private static Main self;
    private static Resources res;
    private static Resources theme;
    private boolean initialized = false;
    private LinksView previousLinksView;
    private ViewCache viewCache = new ViewCache();
    private SessionManager session = SessionManager.getInstance();

    /**
     * @return MIDlet object
     */
    public static Main getInstance() {
        return self;
    }

    public void startApp() {
        if (!initialized) {
            self = this;

            long start = System.currentTimeMillis();

            // Start initializing links view in the background
            Display.init(this);
            new SplashView().show();

            loadResources();
            loadTheme();

            long difference = System.currentTimeMillis() - start;

            try {
                Thread.sleep(Math.max(0, 1000 - difference));
            }
            catch (InterruptedException e) {
            }

            showLinksView(session.getCategory());
            Display.getInstance().setForceFullScreen(false);
            initialized = true;
        }
    }

    public static void loadResources() {
        try {
            res = Resources.open("/resources.res");
        }
        catch (Throwable ex) {
            Dialog.show("Exception", ex.getMessage(), "OK", null);
        }
    }

    public static void loadTheme() {
        try {
            theme = Resources.open("/theme.res");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
        }
        catch (Throwable ex) {
            Dialog.show("Exception", ex.getMessage(), "OK", null);
        }
    }

    /**
     * Used instead of using the Resources API to allow us to fetch locally downloaded
     * resources
     *
     * @param name the name of the resource
     * @return a resources object
     */
    public static Resources getResources() {
        return res;
    }

    /**
     * Show a view with links for the given category.
     *
     * @param category Name of category, or <em>null</em> to show popular links
     */
    private void showLinksView(String category) {
        final String cacheKey = category == null ? "frontpage" : category;
        final LinksView linksView;
        if (viewCache.contains(cacheKey)) {
            linksView = (LinksView) viewCache.get(cacheKey);
        }
        else {
            linksView = new LinksView(category, this, this, this);
            viewCache.put(cacheKey, linksView);
        }
        previousLinksView = linksView;
        linksView.show();
    }

    /**
     * Show comments for a given reddit Link (as selected in the Links view).
     *
     * @param link A reddit link item whose comments to show
     */
    private void showCommentsView(LinkThing link) {
        CommentsView commentsView;
        if (viewCache.contains(link)) {
            commentsView = (CommentsView) viewCache.get(link);
        }
        else {
            commentsView = new CommentsView(link, this);
            viewCache.put(link, commentsView);
        }
        commentsView.show();
    }

    /**
     * Show an alert message on the screen.
     *
     * @param title
     * @param alertText
     * @param type
     */
    public final void showAlertMessage(String title, String alertText, int type) {
        Dialog.show(title, alertText, type, null, "OK", null);
    }

    /**
     * A handler for a category change.
     *
     * @param category Name of selected category
     */
    public void categorySelected(String category) {
        session.setCategory(category);
        showLinksView(category);
    }

    /**
     * Handler for a link selection.
     *
     * @param link
     */
    public void linkSelected(LinkThing link) {
        showCommentsView(link);
    }

    /**
     * Handler for 'Back' option.
     */
    public void backCommanded() {
        previousLinksView.show();
    }

    /**
     * Handler for 'Exit' option.
     */
    public void exitCommanded() {
        exit();
    }

    /**
     * Exit the application.
     */
    public void exit() {
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * @see MIDlet#pauseApp()
     */
    public void pauseApp() {
    }

    /**
     * @see MIDlet#destroyApp(boolean)
     */
    public void destroyApp(boolean unconditional) {
    }

    /**
     * @return name of the MIDlet.
     */
    public String getName() {
        return getAppProperty("MIDlet-Name");
    }

    /**
     * @return vendor of the MIDlet.
     */
    public String getVendor() {
        return getAppProperty("MIDlet-Vendor");
    }

    /**
     * @return version of the MIDlet.
     */
    public String getVersion() {
        return getAppProperty("MIDlet-Version");
    }

    /*
     * Check whether TestMode has been set on in JAD or Manifest.
     */
    public boolean isInTestMode() {
        return "on".equalsIgnoreCase(getAppProperty("TestMode"));
    }
}
