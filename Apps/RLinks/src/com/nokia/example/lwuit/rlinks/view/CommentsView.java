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
import com.nokia.example.lwuit.rlinks.model.CommentThing;
import com.nokia.example.lwuit.rlinks.model.LinkThing;
import com.nokia.example.lwuit.rlinks.model.Voteable;
import com.nokia.example.lwuit.rlinks.network.HttpOperation;
import com.nokia.example.lwuit.rlinks.network.operation.CommentPostOperation;
import com.nokia.example.lwuit.rlinks.network.operation.CommentPostOperation.PostCommentListener;
import com.nokia.example.lwuit.rlinks.network.operation.CommentsLoadOperation;
import com.nokia.example.lwuit.rlinks.network.operation.CommentsLoadOperation.LoadCommentsListener;
import com.nokia.example.lwuit.rlinks.network.operation.MoreCommentsLoadOperation;
import com.nokia.example.lwuit.rlinks.view.CommentDetailsView.CommentDetailsBackListener;
import com.nokia.example.lwuit.rlinks.view.item.CommentItem;
import com.nokia.example.lwuit.rlinks.view.item.CommentItem.CommentSelectionListener;
import com.nokia.example.lwuit.rlinks.view.item.LinkItem;
import com.nokia.example.lwuit.rlinks.view.item.LinkItem.LinkSelectionListener;
import com.nokia.example.lwuit.rlinks.view.item.LinkMetadataItem;
import com.nokia.example.lwuit.rlinks.view.item.LoaderItem;
import com.nokia.example.lwuit.rlinks.view.item.MoreCommentsItem;
import com.nokia.example.lwuit.rlinks.view.item.TextArea;
import com.nokia.example.lwuit.rlinks.view.item.TextArea.TextChangedListener;
import com.nokia.example.lwuit.rlinks.view.item.VoteItem;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * View displaying comments for a Link.
 */
public class CommentsView
        extends BaseFormView
        implements PostCommentListener, LoadCommentsListener,
                   CommentSelectionListener, CommentDetailsBackListener,
                   TextChangedListener {

    private static final int COMMENT_CHUNK_SIZE = 10;
    private final Command refreshCommand = new Command("Refresh");
    private final Command replyCommand;
    private final Command disabledReplyCommand;
    private final Button replyButton;
    private final TextArea reply;
    private final LinkThing link;
    private Label comments;
    private LinkItem linkItem;
    private HttpOperation operation;
    private LoaderItem loaderItem;
    private Container commentList;
    private CommentPostOperation replyOperation;
    private boolean submitEnabled = false;
    private volatile boolean refreshing = false;
    private volatile boolean sendingReply = false;

    /**
     * Create a CommentsView.
     *
     * @param link Link item to show comments for
     * @param backListener Listener signaling back commands to
     */
    public CommentsView(LinkThing link, BackCommandListener backListener) {
        super("Comments", backListener);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        this.link = link;

        loaderItem = new LoaderItem("loading_rlinks.png", 12);

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
    }

    public final void setupCommands() {
        removeAllCommands();

        setBackCommand(backCommand);
        addCommand(refreshCommand);
        setupLoginCommands();
        addCommand(aboutCommand);
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

    public void show() {
        super.show();

        setupCommands();
        if (loginStatusItem != null) {
            loginStatusItem.updateStatus();
        }

        if (getContentPane().getComponentCount() > 1) {
            return;
        }

        addLoginStatusItem();

        addMetaItems();
        refresh();
    }

    private void refresh() {
        if (refreshing || operation != null && !operation.isFinished()) {
            return;
        }
        operation = null;
        refreshing = true;

        loadComments();

    }

    /**
     * Adds the meta items:
     *
     * - related link item as the topmost item in the list view. - item for
     * voting (when logged in) - item showing metadata (author, date)
     */
    private void addMetaItems() {
        addComponent(createLinkItem());
        Container items = new Container(new BorderLayout());
        addComponent(items);
        items.addComponent(BorderLayout.CENTER, new LinkMetadataItem(link));
        items.addComponent(BorderLayout.EAST, new VoteItem(link, this, new VoteItem.VoteListener() {

            public void voteSubmitted(int vote) {
                link.setVote(vote);
                linkItem.refreshScore();
                linkItem.repaint();
            }
        }));
        addComponent(reply);
        if (replyButton != null) {
            addComponent(replyButton);
        }

        updateCommentsLabel();
        comments.getStyle().setMargin(Component.TOP, 20);
        addComponent(comments);
    }

    private void updateCommentsLabel() {
        if (comments == null) {
            comments = new Label();
        }
        comments.setText(link.getNumComments() + " comments");
    }

    /**
     * Create a Link item to be shown as the topmost item in the view.
     *
     * Tapping on the link will prompt to open the link URL in the platform
     * browser.
     *
     * @return Link item
     */
    private LinkItem createLinkItem() {
        // A listener to invoke whenever the link gets selected; either by
        // direct touch selection or by the selection command
        final LinkSelectionListener selectionListener = new LinkSelectionListener() {

            public void linkSelected(LinkThing link) {
                try {
                    Main.getInstance().platformRequest(link.getUrl());
                }
                catch (ConnectionNotFoundException cnfe) {
                    System.out.println("Connection not found: " + cnfe.getMessage());
                }
            }
        };

        linkItem = new LinkItem(link, selectionListener, true, imageCache);

        return linkItem;
    }

    /**
     * Handle command actions
     *
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        Command cmd = ae.getCommand();
        Component cmp = ae.getComponent();
        if (cmd == backCommand) {
            abortLoadingComments();
        }
        else if (cmd == refreshCommand) {
            refresh();
        }
        else if (!sendingReply && (cmd != null && cmd == replyCommand ||
                cmp != null && cmp == replyButton)) {
            System.out.println("What");
            sendingReply = true;
            // Require logged-in user to access the Reply view
            if (!session.isLoggedIn()) {
                showLoginRequiredMessage();
                sendingReply = false;
                return;
            }
            sendReply();
        }
        super.actionPerformed(ae);
    }

    /**
     * Create empty list and remove old one
     *
     * @return List
     */
    private void clearList() {
        if (commentList != null) {
            commentList.removeAll();
            removeComponent(commentList);
            commentList = null;
        }
        commentList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(commentList);
    }

    /**
     * Load comments to be shown in the view.
     */
    private void loadComments() {
        if (link.getNumComments() == 0) {
            refreshing = false;
            return;
        }

        // Do not reload if already loaded, or if in process
        if (HttpOperation.reloadNeeded(operation)) {
            Display d = Display.getInstance();

            // Clear the view in the main UI thread after possible pending paint calls
            d.callSeriallyAndWait(new Runnable() {

                public void run() {
                    clearList();
                }
            });

            // Add 'Loading' item
            addComponent(loaderItem);
            revalidate();
            operation = new CommentsLoadOperation(link.getId(), COMMENT_CHUNK_SIZE, this);
            operation.start();
        }
    }

    /**
     * Abort any unfinished comment loading operations.
     */
    private void abortLoadingComments() {
        if (operation != null && !operation.isFinished()) {
            operation.abort();
            clearList();
            refreshing = false;
        }
    }

    /**
     * Load more comments for the given comment.
     *
     * This solution aims to be scalable in that it: - only gets one new child
     * item (and its children) at a time, - updates the list of child IDs for
     * the original selection, and - creates a new 'load more replies' item with
     * the indexes taken care of.
     *
     * When there are no more children to be loaded, a new 'Load more' item will
     * not be added.
     *
     * @param comment Comment to get children for
     * @param itemIndex Index of the 'Load more' item that was selected
     */
    private void loadMoreComments(final CommentThing comment, final int itemIndex) {
        if (refreshing) {
            return;
        }
        refreshing = true;

        final String[] childIds = comment.getChildIds();
        final MoreCommentsItem moreItem = ((MoreCommentsItem) commentList.getComponentAt(
                itemIndex));
        moreItem.setLoading();

        LoadCommentsListener listener = new LoadCommentsListener() {

            public void commentsReceived(final Vector comments) {
                Display d = Display.getInstance();

                // Update the view in the main UI thread
                d.callSeriallyAndWait(new Runnable() {

                    public void run() {
                        // Remove the original "Load more comments" item
                        commentList.removeComponent(moreItem);
                    }
                });

                // Insert items
                final int lastIndex = insertCommentItems(comments, itemIndex);

                d.callSerially(new Runnable() {

                    public void run() {
                        // If there's more content to be loaded, create a new
                        // 'Load more replies' item
                        if (childIds.length > 1) {
                            createMoreItem(comment, lastIndex + 1);
                        }

                        // Update indexes for items after the inserted items
                        for (int i = lastIndex, len = commentList.getComponentCount(); i < len; i++) {
                            CommentItem ci = (CommentItem) commentList.getComponentAt(i);
                            ci.setItemIndex(i);
                        }
                        revalidate();
                        refreshing = false;
                    }
                });
            }
        };
        // Load comments for the first child only, add the rest as "more" items
        operation = new MoreCommentsLoadOperation(link.getId(), childIds[0], comment.getLevel(),
                                                  COMMENT_CHUNK_SIZE, listener);

        operation.start();
    }

    /**
     * Insert given comment items at a specified index, maintaining the
     * 'itemIndex' property of the items.
     *
     * @param commentItems Array of comment items to add
     * @param startIndex Index to start adding at
     * @return The index of the last item that was added
     */
    private int insertCommentItems(final Vector comments, int startIndex) {
        int actualIdx = startIndex;
        Display d = Display.getInstance();

        for (int i = 0, len = comments.size(); i < len; i++) {

            final CommentThing comment = (CommentThing) comments.elementAt(i);
            final CommentItem ci;

            try {
                if (comment.getHiddenChildCount() > 0) {
                    ci = new MoreCommentsItem(comment, this);
                }
                else {
                    ci = new CommentItem(comment, this);
                }
            }
            catch (OutOfMemoryError oome) {
                return actualIdx;
            }

            actualIdx = startIndex + i;
            final int idx = actualIdx;

            Runnable addItem = new Runnable() {

                public void run() {
                    ci.setItemIndex(idx);
                    commentList.addComponent(idx, ci);
                }
            };

            if (i < 6 || i == len - 1) {
                // Add an item in UI thread and repaint
                d.callSeriallyAndWait(addItem);
                repaint();
            }
            else {
                d.callSerially(addItem);
            }
        }
        return actualIdx;


    }

    /**
     * Create a new 'Load more' item with the first child item ID removed.
     *
     * @param comment Comment to be updated
     * @param index Index of the item
     */
    private void createMoreItem(CommentThing comment, int index) {
        String[] childIds = comment.getChildIds();
        String[] newChildIds = new String[childIds.length - 1];

        try {
            System.arraycopy(childIds, 1, newChildIds, 0, childIds.length - 1);
        }
        catch (Exception e) {
            System.out.println("Arraycopy failed: " + e.getMessage());
        }
        comment.setChildIds(newChildIds);

        // Add a new More item
        CommentItem more = new MoreCommentsItem(comment, this);
        more.setItemIndex(index);
        commentList.addComponent(index, more);
    }

    /**
     * Process comments received.
     *
     * @param comments Vector of comments loaded
     */
    public void commentsReceived(Vector comments) {

        Display d = Display.getInstance();

        if (comments == null) {
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

        // Add the comment items to the view in the UI thread
        insertCommentItems(comments, commentList.getComponentCount());
        d.callSerially(new Runnable() {

            public void run() {
                // Remove 'Loading' item
                removeComponent(loaderItem);
                refreshing = false;
            }
        });


    }

    /**
     * Act on a comment (or "load more replies" item) being selected.
     *
     * @param comment Comment selected
     * @param itemIndex Its index in the list
     */
    public void commentSelected(final CommentThing comment, final int itemIndex) {
        final boolean operationPending = operation != null && !operation.isFinished();

        // Only allow one operation at a time
        if (operationPending) {
            return;
        }
        else if (comment.getHiddenChildCount() > 0) {
            loadMoreComments(comment, itemIndex);
        } // In case an ordinary Comment is selected, open up comment details view
        else {
            openDetailsView(comment);
        }
    }

    /**
     * Show detalis view for a given comment.
     *
     * @param comment Comment to show details for
     */
    private void openDetailsView(Voteable comment) {
        // When writing a top-level comment, the Thing replied to is the link.
        // When replying to comment, the Thing is the parent comment itself.
        final Voteable item = comment != null ? comment : link;

        CommentDetailsView cv = new CommentDetailsView(
                item,
                this,
                new VoteItem.VoteListener() {

                    public void voteSubmitted(int vote) {
                        item.setVote(vote);
                    }
                });
        cv.show();
    }

    /**
     * Show the 'Reply' view (a full-screen TextBox).
     */
    private void sendReply() {
        // Submit reply
        String modhash = session.getModhash();
        replyOperation = new CommentPostOperation(
                link.getName(),
                reply.getText(),
                modhash,
                (PostCommentListener) self);
        setTitle("Sending...");
        replyOperation.start();
    }

    public void commentingSucceeded(String thingName, String text) {
        setTitle("Comments");
        reply.setHint("Thanks for your comment!");
        reply.setText("");
        backCommanded(true);
        sendingReply = false;
    }

    public void commentingFailed(String thingName, String text) {
        setTitle("Comments");
        repaint();
        Main.getInstance().showAlertMessage(
                "Not sent",
                "The comment could not be sent. Please try again.",
                Dialog.TYPE_INFO);
        sendingReply = false;
    }

    /**
     * Handle text changes
     *
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

    public void backCommanded(boolean commentAdded) {
        // New comment added, refreshing
        if (commentAdded) {
            link.setNumComments(link.getNumComments() + 1);
            updateCommentsLabel();
            refresh();
        }
        super.backCommanded();
    }
}
