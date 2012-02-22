/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * Base class for a demo contains common code for all demo pages
 * 
 * @author Shai Almog
 */
public abstract class Demo {
    /**
     * returns the name of the demo to display in the list
     */
    public abstract String getName();

    /**
     * Invoked by the main code to start the demo
     */
    public final void run(final Command backCommand, ActionListener commandListener, final Container demoPanel) {
        final Form demoForm = new Form(getName());
        demoForm.addCommand(new Command("Help") {
            public void actionPerformed(ActionEvent evt) {
                Form helpForm = new Form("Help");
                helpForm.setLayout(new BorderLayout());
                TextArea helpText = new TextArea(getHelpImpl(), 5, 10);
                helpText.setEditable(false);
                helpForm.setScrollable(false);
                helpForm.addComponent(BorderLayout.CENTER, helpText);
                Command c = new Command("Back") {
                    public void actionPerformed(ActionEvent evt) {
                        if(demoPanel != null) {
                            demoPanel.replace(demoPanel.getComponentAt(0), demoForm, UIManager.getInstance().getLookAndFeel().getDefaultFormTransitionOut().copy(true));
                        } else {
                            demoForm.showBack();
                        }
                    }
                };
                helpForm.addCommand(c);
                helpForm.setBackCommand(c);
                if(demoPanel != null) {
                    demoPanel.replace(demoPanel.getComponentAt(0), helpForm, UIManager.getInstance().getLookAndFeel().getDefaultFormTransitionOut());
                } else {
                    helpForm.show();
                }
            }
        });
        if(demoPanel == null) {
            demoForm.addCommand(backCommand);
            demoForm.setBackCommand(backCommand);
        }
        demoForm.addCommandListener(commandListener);
        execute(demoForm);
        if(demoPanel != null) {
            if(Display.getInstance().isPortrait()) {
                demoForm.addCommand(UIDemoMain.showDemosCommand, 1);
            }
            demoPanel.removeAll();
            demoPanel.addComponent(BorderLayout.CENTER, demoForm);
            demoPanel.revalidate();
        } else {
            demoForm.show();
        }
    }
    
    /**
     * Returns the text that should appear in the help command
     */
    private String getHelpImpl() {
        String h = getHelp();
        return UIManager.getInstance().localize(h, h);
    }

    protected void addCommand(Command cmd, Container f) {
        ((Form)f.getParent()).addCommand(cmd);
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        // return a key value for localization
        String n = getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1) + ".help";
    }
    
    /**
     * The demo should place its UI into the given form 
     */
    protected final void execute(Form f) {
        executeDemo(f.getContentPane());
    }

    protected abstract void executeDemo(Container f);
  
    /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout with a minimum label width
     */
    protected Container createPair(String label, Component c, int minWidth) {
        Container pair;
        Label l =  new Label(label);
        Dimension d = l.getPreferredSize();
        d.setWidth(Math.max(d.getWidth(), minWidth));
        l.setPreferredSize(d);
        c.setLabelForComponent(l);
        if(UIManager.getInstance().isThemeConstant("ComponentGroupBool", false)) {
            pair = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            pair.addComponent(l);
            l.setUIID("TitleLabel");
            ComponentGroup g = new ComponentGroup();
            g.addComponent(c);
            pair.addComponent(g);
        } else {
            pair = new Container(new BorderLayout());
            pair.addComponent(BorderLayout.WEST,l);
            pair.addComponent(BorderLayout.CENTER, c);
        }
        return pair;
    }
    
      /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout
     */
     protected Container createPair(String label, Component c) {
         return createPair(label,c,0);
     }
    
     public void cleanup() {
     }
}
