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
import com.nokia.example.lwuit.rlinks.model.Voteable;
import com.nokia.example.lwuit.rlinks.network.operation.CommentPostOperation;
import com.nokia.example.lwuit.rlinks.network.operation.CommentPostOperation.PostCommentListener;
import com.nokia.example.lwuit.rlinks.view.item.TextArea;
import com.nokia.example.lwuit.rlinks.view.item.TextArea.TextChangedListener;
import com.nokia.example.lwuit.rlinks.view.item.VoteItem;
import com.nokia.example.lwuit.rlinks.view.item.VoteItem.VoteListener;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * View for voting on and replying to a comment.
 *
 * This view shows three items by default:
 * - a "Replying to <author name>:" label
 * - a truncated preview of the item replying to
 * - VoteItem displaying vote up / down controls
 *
 * It also provides a 'Reply' command which displays a TextBox text input
 * that can be used
 */
public class CommentDetailsView
        extends BaseFormView
        implements PostCommentListener, TextChangedListener {

    private static final int DESCRIPTION_MAX_LENGTH = 150;
    private final Command replyCommand;
    private final Command disabledReplyCommand;
    private final Button replyButton;
    private final CommentDetailsBackListener commentBackListener;
    private final Voteable item;
    private final VoteItem voteItem;
    private final TextArea title;
    private final TextArea description;
    private final TextArea reply;
    private CommentPostOperation replyOperation;
    private boolean submitEnabled = false;
    private volatile boolean sendingReply = false;

    public static interface CommentDetailsBackListener {

        public void backCommanded(boolean commentAdded);
    }

    /**
     * Create a CommentDetailsView.
     *
     * @param item A Comment or Link whose details to show
     * @param backListener Listener to signal of back presses
     * @param voteListener Listener to signal of voting results
     */
    public CommentDetailsView(Voteable item,
                              CommentDetailsBackListener backListener,
                              VoteListener voteListener) {
        super("Reply", null);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        this.item = item;
        this.commentBackListener = backListener;

        title = new TextArea(2, this);
        title.setText("Replying to " + item.getAuthor() + ":");
        title.setEditable(false);
        title.setUIID("Label");

        description = new TextArea(2, this);
        description.setGrowByContent(true);
        description.setText("\"" + truncate(item.getText()) + "\"");
        description.setEditable(false);
        description.setUIID("Label");
        voteItem = new VoteItem(item, this, voteListener);

        reply = new TextArea(3, this);
        reply.setGrowByContent(true);
        reply.setHint("Write reply");
        reply.setMaxSize(1000);

        Display d = Display.getInstance();
        if (d.getDeviceType() == Display.FULL_TOUCH_DEVICE) {
            Image enabled = Main.getResources().getImage("reply.png");
            Image disabled = Main.getResources().getImage("reply_disabled.png");
            replyCommand = new Command("Submit", enabled);
            disabledReplyCommand = new Command("Submit", disabled);
            replyButton = null;
        }
        else if (d.getDeviceType() == Display.TOUCH_AND_TYPE_DEVICE) {
            replyCommand = new Command("Submit");
            disabledReplyCommand = null;
            replyButton = null;
        }
        else {
            replyButton = new Button("Submit");
            replyButton.addActionListener(self);
            replyButton.setEnabled(false);
            replyCommand = null;
            disabledReplyCommand = null;
        }

        setupCommands();
    }

    public final void setupCommands() {
        removeAllCommands();

        setBackCommand(backCommand);
        setupLoginCommands();
        setupDefaultCommands();
    }

    private void setupDefaultCommands() {
        if (submitEnabled && replyCommand != null) {
            setDefaultCommand(replyCommand);
            removeCommand(disabledReplyCommand);
        }
        else if (disabledReplyCommand != null) {
            setDefaultCommand(disabledReplyCommand);
            removeCommand(replyCommand);
        }
        else {
            setDefaultCommand(session.isLoggedIn() ? logoutCommand : loginCommand);
            removeCommand(replyCommand);
        }
    }

    private String truncate(String text) {
        if (text.length() > DESCRIPTION_MAX_LENGTH) {
            return text.substring(0, DESCRIPTION_MAX_LENGTH) + "...";
        }
        return text;
    }

    /**
     * Show the view contents.
     */
    public void show() {
        setupCommands();
        if (loginStatusItem != null) {
            loginStatusItem.updateStatus();
        }

        if (getContentPane().getComponentCount() > 0) {
            super.show();
            return;
        }

        addLoginStatusItem();
        addComponent(title);
        addComponent(description);
        addComponent(voteItem);
        addComponent(reply);
        super.show();
    }

    /**
     * Handle command actions
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        Command cmd = ae.getCommand();
        Component cmp = ae.getComponent();
        
        if (cmd == backCommand) {
            commentBackListener.backCommanded(false);
        }
        else if (!sendingReply && (cmd != null && cmd == replyCommand ||
                cmp != null && cmp == replyButton)) {
            sendingReply = true;
            // Require logged-in user to access the Reply view
            if (!session.isLoggedIn()) {
                showLoginRequiredMessage();
                sendingReply = false;
                return;
            }
            sendReply();
        }
        else {
            super.actionPerformed(ae);
        }
    }

    /**
     * Show the 'Reply' view (a full-screen TextBox).
     */
    private void sendReply() {
        // Submit reply
        String modhash = session.getModhash();
        replyOperation = new CommentPostOperation(
                item.getName(),
                reply.getText(),
                modhash,
                (PostCommentListener) self);
        setTitle("Sending...");
        replyOperation.start();
    }

    public void commentingSucceeded(String thingName, String text) {
        setTitle("Success!");
        commentBackListener.backCommanded(true);
        sendingReply = false;
    }

    public void commentingFailed(String thingName, String text) {
        setTitle("Reply");
        Main.getInstance().showAlertMessage(
                "Not sent",
                "The comment could not be sent. Please try again.",
                Dialog.TYPE_INFO);
        sendingReply = false;
    }

    /**
     * Handle text changes
     * @param textArea
     */
    public void textChanged(com.sun.lwuit.TextArea textArea) {
        if (reply.getText().length() == 0) {
            submitEnabled = false;
            if (replyButton != null) {
                replyButton.setEnabled(false);
            }
            else {
                setupDefaultCommands();
            }
        }
        else if (!submitEnabled) {
            submitEnabled = true;
            if (replyButton != null) {
                replyButton.setEnabled(true);
            }
            else {
                setupDefaultCommands();
            }
        }
    }
}
