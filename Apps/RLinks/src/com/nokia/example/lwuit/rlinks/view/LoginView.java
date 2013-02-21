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
import com.nokia.example.lwuit.rlinks.network.HttpOperation;
import com.nokia.example.lwuit.rlinks.network.operation.LoginOperation;
import com.nokia.example.lwuit.rlinks.network.operation.LoginOperation.LoginListener;
import com.nokia.example.lwuit.rlinks.view.item.LoaderItem;
import com.nokia.example.lwuit.rlinks.view.item.TextArea;
import com.nokia.example.lwuit.rlinks.view.item.TextArea.TextChangedListener;
import com.nokia.example.lwuit.rlinks.view.item.TextField;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * View for logging in.
 */
public class LoginView
        extends BaseFormView
        implements LoginListener, TextChangedListener, ActionListener {

    private Command submitCommand = new Command("Submit");
    private Button submit;
    private final LoginOperation.LoginListener loginListener;
    private final TextArea username;
    private final TextArea password;
    private volatile boolean sendingRequest = false;
    private HttpOperation loginOperation;
    private boolean loginShown = false;

    /**
     * Create a LoginView.
     *
     * @param loginListener Listener to signal of login events
     * @param backListener Listener to signal of back button presses
     */
    public LoginView(BackCommandListener backListener, LoginListener loginListener) {
        super("Login", backListener);
        this.loginListener = loginListener;

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        username = new TextArea(1, TextField.NON_PREDICTIVE, this);
        username.setText(session.getUsername() != null ? session.getUsername() : "");
        username.setMaxSize(20);
        username.setHint("Username");

        password = new TextArea(1, TextArea.PASSWORD, this);
        password.setMaxSize(40);
        password.setHint("Password");

        submit = new Button("Login");
        submit.addActionListener(this);
        submit.setEnabled(false);

        setupCommands();
    }

    protected final void setupCommands() {
        addCommand(backCommand);
        setBackCommand(backCommand);
    }

    public void show() {
        super.show();
        removeAll();
        addComponent(username);
        addComponent(password);
        if (!(Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE)) {
            addComponent(submit);
        }
    }

    /**
     * Handle command actions
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getCommand() == submitCommand || ae.getComponent() == submit) {
            if (username.getText().length() > 0 && password.getText().length() > 0) {
                submitLogin();
            }
        }
        super.actionPerformed(ae);
    }

    /**
     * Submit a login request.
     */
    private void submitLogin() {
        if (sendingRequest || loginOperation != null && !loginOperation.isFinished()) {
            return;
        }
        sendingRequest = true;
        final String user = username.getText();
        final String pass = password.getText();
        if (user == null || pass == null) {
            sendingRequest = false;
            return;
        }

        loginOperation = new LoginOperation(user.toLowerCase(), pass, this);
        loginOperation.start();

        removeAll();
        addComponent(new LoaderItem("loading_rlinks.png", 12));
    }

    /**
     * Handle and signal a successful login.
     */
    public void loginSucceeded(String username, String modhash) {
        session.setLoggedIn(username, modhash);
        loginListener.loginSucceeded(username, modhash);
        sendingRequest = false;
    }

    /**
     * Handle and signal a failed login.
     */
    public void loginFailed(String reason) {
        // Show an error message and refresh the view
        final String message =
                "Login failed"
                + (reason != null ? " (" + reason + ")" : "")
                + ". Please try again.";

        Main.getInstance().showAlertMessage(
                "Login failed",
                message,
                Dialog.TYPE_INFO);

        show();
        loginListener.loginFailed(reason);
        sendingRequest = false;
    }

    /**
     * Handle text changes
     * @param textArea
     */
    public void textChanged(com.sun.lwuit.TextArea textArea) {
        if (isInitialized()) {
            if (username.getText().length() == 0 || password.getText().length() == 0) {
                if (Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
                    removeCommand(submitCommand);
                }
                submit.setEnabled(false);
                loginShown = false;
            }
            else if (!loginShown) {
                if (Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
                    setDefaultCommand(submitCommand);
                }
                submit.setEnabled(true);
                loginShown = true;
            }
        }
    }
}
