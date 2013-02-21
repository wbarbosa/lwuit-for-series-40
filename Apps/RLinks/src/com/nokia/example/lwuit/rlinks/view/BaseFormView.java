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
import com.nokia.example.lwuit.rlinks.SessionManager;
import com.nokia.example.lwuit.rlinks.network.operation.LoginOperation.LoginListener;
import com.nokia.example.lwuit.rlinks.util.ImageCache;
import com.nokia.example.lwuit.rlinks.view.item.LoginStatusItem;
import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;

/**
 * A convenience base class for custom views based on Form.
 */
public abstract class BaseFormView
        extends com.sun.lwuit.Form
        implements BackCommandListener, ActionListener {

    private static final String LOGIN_REQUIRED_LABEL = "Login required";
    private static final String LOGIN_REQUIRED_TEXT = "You need to be "
            + "logged in to comment or vote. Do you want to log in?";
    private static final String LOGIN_REQUIRED_YES = "Yes";
    private static final String LOGIN_REQUIRED_NO = "No";
    protected static final ImageCache imageCache = new ImageCache();
    protected final BaseFormView self = this;
    protected final Command exitCommand = new Command("Exit");
    protected final Command backCommand = new Command("Back");
    protected final Command aboutCommand = new Command("About");
    protected Command loginCommand = new Command("Login");
    protected Command logoutCommand = new Command("Logout");
    protected final SessionManager session = SessionManager.getInstance();
    protected LoginStatusItem loginStatusItem;
    protected final BackCommandListener backListener;

    /**
     * Listener for category changes.
     */
    public static interface CategorySelectionListener {

        public void categorySelected(String category);
    }

    protected static void setDisplay(Displayable display) {
        Display.getDisplay(Main.getInstance()).setCurrent(display);
    }

    protected static void setItem(Item item) {
        Display.getDisplay(Main.getInstance()).setCurrentItem(item);
    }

    protected static void showNetworkError() {
        Main.getInstance().showAlertMessage(
                "Network error", "Couldn't load data. Please try again.",
                Dialog.TYPE_INFO);
    }

    public BaseFormView(String title, BackCommandListener backListener) {
        super(title);
        this.backListener = backListener;
        addCommandListener(this);
    }

    protected abstract void setupCommands();

    protected void showAboutView() {
        final AboutView aboutView = new AboutView(this);
        aboutView.show();
    }

    /**
     * Show a login required message on the screen.
     *
     * @param title
     * @param alertText
     * @param type
     */
    public final void showLoginRequiredMessage() {
        if (Dialog.show(LOGIN_REQUIRED_LABEL, LOGIN_REQUIRED_TEXT,
                        Dialog.TYPE_INFO, null, LOGIN_REQUIRED_YES,
                        LOGIN_REQUIRED_NO)) {
            showLoginView();
        }
    }

    /**
     * Show the Login view.
     */
    protected void showLoginView() {
        final LoginView lv = new LoginView(this, new LoginListener() {

            public void loginSucceeded(String username, String modhash) {
                // Refresh commands to reflect the current login status                
                self.show();
                setupCommands();
                loginStatusItem.updateStatus();
            }

            public void loginFailed(String reason) {
            }
        });
        lv.show();
    }

    protected void setupLoginCommands() {
        if (session.isLoggedIn()) {
            removeCommand(loginCommand);
            addCommand(logoutCommand);
        }
        else {
            removeCommand(logoutCommand);
            addCommand(loginCommand);
        }
    }

    /**
     * Add item to show current Login status. Tapping on it will also show
     * the login view.
     */
    protected void addLoginStatusItem() {
        // The login status item is the first item in the view. If it's
        // not, then let's not add it (again).
        if (loginStatusItem == null) {
            loginStatusItem = new LoginStatusItem();
            loginStatusItem.updateStatus();
            loginStatusItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (!session.isLoggedIn()) {
                        showLoginView();
                    }
                }
            });
            addComponent(0, loginStatusItem);
        }
    }

    /**
     * Handle command actions
     * @param evt
     */
    public void actionPerformed(ActionEvent ae) {
        Command cmd = ae.getCommand();
        if (cmd == exitCommand) {
            Main.getInstance().exitCommanded();
        }
        else if (cmd == backCommand) {
            if (backListener != null) {
                backListener.backCommanded();
            }
        }
        else if (cmd == aboutCommand) {
            showAboutView();
        }
        else if (cmd == loginCommand) {
            showLoginView();
        }
        else if (cmd == logoutCommand) {
            session.setLoggedOut();
            setupCommands();
            loginStatusItem.updateStatus();
        }
    }

    public void backCommanded() {
        show();
    }
}
