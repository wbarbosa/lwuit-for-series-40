/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futurice.lwuit.proto;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Slider;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.spinner.Spinner;
import javax.microedition.midlet.*;

/**
 * @author tkor
 */
public class Main extends MIDlet implements ActionListener {

    final static Command[] commands = {
        new Command("First"), new Command("Second"), new Command("Third"),
        new Command("Fourth"), new Command("Fifth")
    };
    int nextCommand = 0;
    Command backCommand = new Command("Back");

    // The default form, shown at start
    Form f = null;

    // Editor form
    Form editorForm = null;
    // Commands for the editor form
    Command editorExit = null;
    Command editorClear = null;
    Command editorViewAll = null;
    Command editorBack = null;
    Command editorSearch = null;
    Command editorDummy1 = null;
    Command editorDummy2 = null;
    // 'search' field for editor form
    TextArea searchField = null;

    public void actionPerformed(ActionEvent e) {
        Command c = e.getCommand();
        if (c == editorExit) {
            f.showBack();
        } else if (c == editorBack || c == editorViewAll) {
            // Replace default command like in default contacts app
            editorForm.removeCommand(editorViewAll);
            editorForm.addCommand(editorSearch);
            editorForm.setDefaultCommand(editorSearch);
            editorForm.getContentPane().removeComponent(searchField);
            // Replace back with exit
            editorForm.removeCommand(editorBack);
            editorForm.addCommand(editorExit);
            editorForm.setBackCommand(editorExit);
            // Redisplay
            editorForm.revalidate();
        } else if (c == editorSearch) {
            // Replace default command like in default contacts app
            editorForm.removeCommand(editorSearch);
            editorForm.addCommand(editorViewAll);
            editorForm.setDefaultCommand(editorViewAll);
            // show a search field
            editorForm.getContentPane().addComponent(BorderLayout.SOUTH, searchField);
            // Replace 'exit' with back
            editorForm.removeCommand(editorExit);
            editorForm.addCommand(editorBack);
            editorForm.setBackCommand(editorBack);
            // Redisplay
            editorForm.revalidate();
        }
    }

    public void startApp() {
        try {
            Display.init(Main.this);
            f = new Form();
            f.setTitle("TextArea test");
            f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            final Label l = new Label();
            Spinner spinner = Spinner.create(0, 100, 20, 1);
            f.addComponent(spinner);
            Slider slider = new Slider();
            slider.setMaxValue(10);
            slider.setMinValue(0);

            slider.setEditable(true);
            f.addComponent(slider);
            TextArea area7 = new TextArea(2, 3, TextArea.ANY);

            f.addComponent(area7);
            f.addComponent(new Label("textfield"));
            f.addComponent(TextField.create());
            f.addComponent(new Label("with create method"));
            f.addComponent(TextField.create());

            // Print something for all commands
            f.addCommandListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    System.out.println("actionPerformed: " + evt.getCommand().getCommandName());
                }
            });

            // Button for adding commands
            Button b = new Button("+button");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    System.out.println(nextCommand + " < " + commands.length + "?");
                    if (nextCommand < commands.length) {
                        f.addCommand(commands[nextCommand++]);
                    }
                }
            });
            f.addComponent(b);

            // Button for removing commands
            b = new Button("-button");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (nextCommand > 0) {
                        f.removeCommand(commands[nextCommand - 1]);
                        nextCommand--;
                    }
                }
            });
            f.addComponent(b);

            // Button for adding the back command
            b = new Button("+back");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    f.addCommand(backCommand);
                    f.setBackCommand(backCommand);
                }
            });
            f.addComponent(b);

            // Button for removing the back command
            b = new Button("-back");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    f.removeCommand(backCommand);
                    f.setBackCommand(null);
                }
            });
            f.addComponent(b);

            /**************************************
             * Create the 'editor' type form here *
             **************************************/
            editorForm = new Form("Contacts");
            editorForm.getContentPane().setLayout(new BorderLayout());
            // Button to access editor
            b = new Button("Editor test");
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editorForm.show();
                }
            });
            f.addComponent(b);
            // List of things
            final String items[] = {
                "Item 1", "Item 2", "Item 3", "Item 4"
            };
            final List list = new List(items);
            editorForm.getContentPane().addComponent(BorderLayout.CENTER, list);
            searchField = TextField.create();
            // Creat commands for the editor form:
            // (we're emulating the contacts app here)
            editorExit = new Command("Exit");
            editorClear = new Command("Clear");
            editorViewAll = new Command("View All");
            editorBack = new Command("Back");
            editorSearch = new Command("Search");
            editorDummy1 = new Command("Foo");
            editorDummy2 = new Command("Bar");
            // All commands go through this
            editorForm.addCommandListener(this);
            // At first there's options, search and exit
            editorForm.addCommand(editorExit);
            editorForm.setBackCommand(editorExit);
            editorForm.addCommand(editorSearch);
            editorForm.setDefaultCommand(editorSearch);
            editorForm.addCommand(editorDummy1);
            editorForm.addCommand(editorDummy2);

            /**************************************
             *     End of 'editor' type form      *
             **************************************/
            f.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
