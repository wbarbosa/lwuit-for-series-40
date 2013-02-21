/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.Main;
import com.nokia.example.lwuit.rlinks.SessionManager;
import com.nokia.example.lwuit.rlinks.model.Voteable;
import com.nokia.example.lwuit.rlinks.network.HttpOperation;
import com.nokia.example.lwuit.rlinks.network.operation.VotePostOperation;
import com.nokia.example.lwuit.rlinks.network.operation.VotePostOperation.PostVoteListener;
import com.nokia.example.lwuit.rlinks.view.BaseFormView;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.util.Resources;

/**
 * A custom view item for voting on a Voteable (a LinkThing or a CommentThing).
 */
public class VoteItem
        extends Item
        implements ActionListener {

    protected static Image voteDownActiveImage;
    protected static Image voteDownInactiveImage;
    protected static Image voteUpActiveImage;
    protected static Image voteUpInactiveImage;
    protected final VoteListener listener;
    protected final Voteable item;
    protected final SessionManager session = SessionManager.getInstance();
    protected final BaseFormView parent;
    protected final Button voteDown = new Button();
    protected final Button voteUp = new Button();

    /**
     * Listener interface used to signal an item has been voted on.
     */
    public interface VoteListener {

        public void voteSubmitted(int vote);
    }

    /**
     * Create a VoteItem.
     *
     * @param item Item we're voting on
     * @param preferredWidth Preferred width
     * @param listener Listener to signal of vote results
     * @param form parent BaseFormView
     */
    public VoteItem(Voteable item, BaseFormView parent, VoteListener listener) {

        this.item = item;
        this.parent = parent;

        this.listener = listener != null ? listener : new VoteListener() {

            public void voteSubmitted(int vote) {
            }
        };

        if (voteDownInactiveImage == null) {
            loadImages();
        }
        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        addComponent(voteUp);
        addComponent(voteDown);

        voteUp.setUIID("ActionButton");
        voteUp.setIcon(voteUpInactiveImage);
        voteUp.addActionListener(this);

        voteDown.setUIID("ActionButton");
        voteDown.setIcon(voteDownInactiveImage);
        voteDown.addActionListener(this);
    }

    /**
     * React to voting actions
     * @param evt
     */
    public void actionPerformed(ActionEvent evt) {
        if (!session.isLoggedIn()) {
            parent.showLoginRequiredMessage();
            return;
        }

        Component cmp = evt.getComponent();
        if (cmp == voteUp) {
            voteItemPressed(VotePostOperation.VOTE_UP);
        }
        else if (cmp == voteDown) {
            voteItemPressed(VotePostOperation.VOTE_DOWN);
        }
    }

    /**
     * Act on a voting request: down or up.
     *
     * - Selecting an already active vote item nullifies the existing vote.
     * - Selecting a previously unselected up or down changes the vote.
     *
     * @param requestedVote
     */
    private void voteItemPressed(int requestedVote) {
        // Nullifying vote if the same option is selected again
        final int oldVote = item.getVote();
        if (requestedVote == oldVote) {
            requestedVote = VotePostOperation.VOTE_NONE;
        }

        // Store the old vote in case the voting fails
        item.setVote(requestedVote);
        updateVotes(requestedVote);
        repaint();
        listener.voteSubmitted(requestedVote);

        HttpOperation voteOperation = new VotePostOperation(
                item.getName(),
                requestedVote,
                session.getModhash(),
                new PostVoteListener() {

                    public void votingSucceeded(String thingName, int vote) {
                        listener.voteSubmitted(vote);
                    }

                    public void votingFailed(String thingName, int vote) {
                        // In case the vote wasn't successful, reflect that in the UI
                        item.setVote(oldVote);
                        updateVotes(oldVote);
                        repaint();
                        listener.voteSubmitted(oldVote);
                    }
                });
        voteOperation.start();
    }

    /**
     * Update vote icons
     * @param vote
     */
    private void updateVotes(int vote) {
        voteUp.setIcon(vote == 1 ? voteUpActiveImage : voteUpInactiveImage);
        voteDown.setIcon(vote == -1 ? voteDownActiveImage : voteDownInactiveImage);
    }

    /**
     * Fetch images from resources
     */
    private void loadImages() {
        Resources res = Main.getResources();
        voteDownInactiveImage = res.getImage("down_inactive.png");
        voteDownActiveImage = res.getImage("down_active.png");
        voteUpInactiveImage = res.getImage("up_inactive.png");
        voteUpActiveImage = res.getImage("up_active.png");
    }
}
