/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.categorybardemo;

import com.sun.lwuit.Command;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.IOException;
import java.util.Stack;

/**
 * This view demonstrates how action button #1 can be used in LWUIT.
 */
public class AddContentView
    extends View
    implements ActionListener {

    private static final String CONTENT_TEXT =
        "Lorem ipsum dolor sit amet, "
        + "consectetuer adipiscing elit, sed diam nonummy nibh euismod "
        + "tincidunt ut laoreet dolore magna aliquam erat volutpat.";
    private Label emptyContentLabel;
    private Stack texts = new Stack();
    private Command add;
    private Command delete;

    /**
     * Constructor
     */
    public AddContentView() {
        super("add", "/categorybar_comments.png");

        setTitle("add content");
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        emptyContentLabel = new Label("Press '+' to add content.");
        addComponent(emptyContentLabel);
        
        try {
            delete = new Command("delete");
            add = new Command("add", Image.createImage("/add_icon.png"));
            setDefaultCommand(add);
            addCommand(add);
            addCommandListener(this);
        }
        catch (IOException e) {
            // should not occur
        }
    }

    /**
     * @see ActionListener#actionPerformed(com.sun.lwuit.events.ActionEvent) 
     */
    public void actionPerformed(ActionEvent ae) {
        Command cmd = ae.getCommand();
        
        if (cmd == add) {
            add();
        }
        else if (cmd == delete) {
            delete();
        }
    }

    /**
     * Add a text area to the view.
     */
    private void add() {
        removeComponent(emptyContentLabel);
        TextArea text = new TextArea();
        text.setEditable(false);
        text.setRows(2);
        text.setGrowByContent(true);
        text.setText(CONTENT_TEXT);
        addComponent(text);
        texts.push(text);
        addCommand(delete);
        
        revalidate();
    }

    /**
     * Delete a text area from the view.
     */
    private void delete() {
        if (texts.isEmpty()) {
            return;
        }
        
        TextArea text = (TextArea) texts.pop();
        removeComponent(text);

        if (texts.isEmpty()) {
            removeCommand(delete);
            addComponent(emptyContentLabel);            
        }
        revalidate();
    }
}
