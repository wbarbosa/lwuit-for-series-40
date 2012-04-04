/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futurice.lwuit.proto;

import com.sun.lwuit.*;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.spinner.Spinner;
import javax.microedition.midlet.MIDlet;



/**
 * @author tkor
 */
public class Main extends MIDlet implements ActionListener {

    final static Command[] commands = {
        new Command("First"), new Command("Second"), new Command("Third"),
        new Command("Fourth"), new Command("Fifth")
    };
    int nextCommand = 0;
    // Generic 'back' used in every form
    Command backCommand = null;
    // The default form, shown at start
    Form mainForm = null;
    // Editor form
    Form editorForm = null;
    // Commands for the editor form
    Command editorClear = null;
    Command editorViewAll = null;
    Command editorBack = null;
    Command editorSearch = null;
    Command editorDummy1 = null;
    Command editorDummy2 = null;
    // 'search' field for editor form
    TextArea searchField = null;
    // Spinner demo form
    Form spinnerForm = null;
    // Slider demo form
    Form sliderForm = null;
    // Text field demo form
    Form textfieldForm = null;
    // Softkey action demo form
    Form softkeyForm = null;
    // tab demo form
    Form tabForm = null;

    public void actionPerformed(ActionEvent e) {
        Command c = e.getCommand();
        if (c == editorBack || c == editorViewAll) {
            // Replace default command like in default contacts app
            editorForm.removeCommand(editorViewAll);
            editorForm.addCommand(editorSearch);
            editorForm.setDefaultCommand(editorSearch);
            editorForm.getContentPane().removeComponent(searchField);
            // Replace our back with generic back
            editorForm.removeCommand(editorBack);
            editorForm.addCommand(backCommand);
            editorForm.setBackCommand(backCommand);
            // Redisplay
            editorForm.revalidate();
        } else if (c == editorSearch) {
            // Replace default command like in default contacts app
            editorForm.removeCommand(editorSearch);
            editorForm.addCommand(editorViewAll);
            editorForm.setDefaultCommand(editorViewAll);
            // show a search field
            editorForm.getContentPane().addComponent(BorderLayout.SOUTH, searchField);
            // Replace generic back with our back
            editorForm.removeCommand(backCommand);
            editorForm.addCommand(editorBack);
            editorForm.setBackCommand(editorBack);
            // Redisplay
            editorForm.revalidate();
        }
    }

    public void startApp() {
        Display.init(this);
        /*try {
            Resources res = Resources.open("/woody.res");
            UIManager.getInstance().setThemeProps(res.getTheme("Theme 1"));
        }catch(IOException err) {
            err.printStackTrace();
        }*/
        mainForm = new Form();
        mainForm.setTransitionInAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 1000));
        mainForm.setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 1000));
        mainForm.setTitle("LWUIT DemoS");
        mainForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        mainForm.addComponent(new Button(new Command("Spinner demo") {
            public void actionPerformed(ActionEvent e) {
                spinnerForm.show();
            }
        }));
        mainForm.addComponent(new Button(new Command("Slider demo") {
            public void actionPerformed(ActionEvent e) {
                sliderForm.show();
            }
        }));
        mainForm.addComponent(new Button(new Command("Textfield demo") {
            public void actionPerformed(ActionEvent e) {
                textfieldForm.show();
            }
        }));
        mainForm.addComponent(new Button(new Command("Editor demo") {
            public void actionPerformed(ActionEvent e) {
                editorForm.show();
            }
        }));
        mainForm.addComponent(new Button(new Command("Tab demo") {
            public void actionPerformed(ActionEvent evt) {
                tabForm.show();
            }
        }));
        mainForm.addComponent(new Button(new Command("Show dialog") {
            public void actionPerformed(ActionEvent evt) {
                Dialog.show("Moro", "Tere", "OK", "Cancel");
            }
        }));
        backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent evt) {
                mainForm.showBack();
            }
        };
        
        /****************************
         * Create spinner demo form *
         ****************************/
        spinnerForm = new Form("Spinner demo");
        spinnerForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Spinner spinner = Spinner.create(0, 3, 1, 1);
        spinnerForm.addComponent(spinner);
        for(int i = 0; i < 10; i++) {
            spinnerForm.addComponent(new Label("A label component"));
        }
        spinnerForm.addCommand(backCommand);
        spinnerForm.setBackCommand(backCommand);

        /***************************
         * Create slider demo form *
         ***************************/
        sliderForm = new Form("Slider demo");
        sliderForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Slider slider = new Slider();
        slider.setMaxValue(10);
        slider.setMinValue(0);
        slider.setEditable(true);
        sliderForm.addComponent(slider);
        sliderForm.addCommand(backCommand);
        sliderForm.setBackCommand(backCommand);
        
        /******************************
         * Create textfield demo form *
         ******************************/
        textfieldForm = new Form("Textfield demo");
        textfieldForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        final ComponentGroup cg = new ComponentGroup();
        
        cg.addComponent(new Label("text area"));
        cg.addComponent(new TextArea(2, 3, TextArea.ANY));
        cg.addComponent(new Label("textfield"));
        cg.addComponent(TextField.create());
        cg.addComponent(new Label("with create method"));
        final TextArea tf = TextField.create();
        cg.addComponent(tf);
        final Label lb = new Label("Replaced!");
        Button b = new Button("animate");
        cg.addComponent(b);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if(tf.getComponentForm() != null) {
                cg.replaceAndWait(tf, lb,
                            CommonTransitions.createSlide(
                                CommonTransitions.SLIDE_VERTICAL, 
                                true, 
                                500), 
                            1000);
                }else if(lb.getComponentForm() != null) {
                   cg.replaceAndWait(lb, tf,
                            CommonTransitions.createSlide(
                                CommonTransitions.SLIDE_VERTICAL, 
                                true, 
                                500), 
                            1000); 
                }
            }
        });
        
                     
        textfieldForm.addComponent(cg);
        textfieldForm.addCommand(backCommand);
        textfieldForm.setBackCommand(backCommand);

        /****************************
         * Create softkey demo form *
         ****************************/
        softkeyForm = new Form("Softkey demo");
        softkeyForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        softkeyForm.addComponent(new Button(new Command("+button") {
            public void actionPerformed(ActionEvent evt) {
                System.out.println(nextCommand + " < " + commands.length + "?");
                if (nextCommand < commands.length) {
                    mainForm.addCommand(commands[nextCommand++]);
                }
            }
        }));
        softkeyForm.addComponent(new Button(new Command("-button") {
            public void actionPerformed(ActionEvent evt) {
                if (nextCommand > 0) {
                    mainForm.removeCommand(commands[nextCommand - 1]);
                    nextCommand--;
                }
            }
        }));
        softkeyForm.addComponent(new Button(new Command("+back") {
            public void actionPerformed(ActionEvent evt) {
                mainForm.addCommand(backCommand);
                mainForm.setBackCommand(backCommand);
            }
        }));
        softkeyForm.addComponent(new Button(new Command("-back") {
            public void actionPerformed(ActionEvent evt) {
                mainForm.removeCommand(backCommand);
                mainForm.setBackCommand(null);
            }
        }));
        softkeyForm.addCommand(backCommand);
        softkeyForm.setBackCommand(backCommand);

        /**************************************
         * Create the 'editor' type form here *
         **************************************/
        editorForm = new Form("Contacts");
        editorForm.getContentPane().setLayout(new BorderLayout());
        // List of things
        final String items[] = {
            "Johann Gambolputty", "Eric Praline", "Spiny Norman", "Luigi Vercotti",
            "Ron Obvious", "Doug Piranha", "Dinsdale Piranha", "Harry Organs",
            "Mr. Anemone"
        };
        final List list = new List(items);
        editorForm.getContentPane().addComponent(BorderLayout.CENTER, list);
        searchField = TextField.create();
        // Creat commands for the editor form:
        // (we're emulating the contacts app here)
        editorClear = new Command("Clear");
        editorViewAll = new Command("View All");
        editorBack = new Command("Back");
        editorSearch = new Command("Search");
        editorDummy1 = new Command("Foo");
        editorDummy2 = new Command("Bar");
        // All commands go through this
        editorForm.addCommandListener(this);
        // At first there's options, search and exit
        editorForm.addCommand(backCommand);
        editorForm.setBackCommand(backCommand);
        editorForm.addCommand(editorSearch);
        editorForm.setDefaultCommand(editorSearch);
        editorForm.addCommand(editorDummy1);
        editorForm.addCommand(editorDummy2);

        /*****************
         * Tab demo form *
         *****************/
        tabForm = new Form("Tabs");
        tabForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Tabs tabs = new Tabs();
        tabs.addTab("First", new Label("First"));
        tabs.addTab("Second", new Label("Second"));
        tabs.addTab("Third", new Label("Third"));
        tabForm.addComponent(tabs);
        tabForm.addCommand(backCommand);
        tabForm.setBackCommand(backCommand);
        
        
        mainForm.show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
