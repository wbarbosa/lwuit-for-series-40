/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.ComponentGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Vector;

/**
 * Simple demo showing off how a theme can be manipulated in LWUIT. Once the demo
 * is executed the theme changes completely for the rest of the application...
 * 
 * @author Shai Almog
 */
public class ThemeDemo extends Demo {

    /**
     * The full path to the storage location of the themes
     */
    //private static final String THEME_PATH = "/com/sun/lwuit/uidemo/themes/";
    /**
     * Names of the themes in the storage and display
     */
    private static final String[] THEMES = {"TimelineTheme", "TipsterTheme"};
    private static final String[] THEME_LABELS = {"Default (Timeline)", "Tipster"};
    private static Vector additionalThemes;
    private static Vector additionalThemeLabels;

    /**
     * Allows external platform specific themes
     *
     * @param resourceName the resource name
     * @param label the label of the theme
     */
    public static void addTheme(String resourceName, String label) {
        if(additionalThemes == null) {
            additionalThemes = new Vector();
            additionalThemeLabels = new Vector();
        }
        additionalThemes.addElement(resourceName);
        additionalThemeLabels.addElement(label);
    }

    public String getName() {
        return "Themes";
    }

    protected void executeDemo(Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        final ButtonGroup group1 = new ButtonGroup();
        Label title = new Label("Please choose a theme");
        title.setUIID("TitleLabel");
        f.addComponent(title);
        ComponentGroup cmpGroup = new ComponentGroup();
        f.addComponent(cmpGroup);
        for (int iter = 0; iter < THEME_LABELS.length; iter++) {
            RadioButton rb = new RadioButton(THEME_LABELS[iter]);
            group1.add(rb);
            cmpGroup.addComponent(rb);
        }
        if(additionalThemeLabels != null) {
            for (int iter = 0; iter < additionalThemeLabels.size(); iter++) {
                RadioButton rb = new RadioButton((String)additionalThemeLabels.elementAt(iter));
                group1.add(rb);
                cmpGroup.addComponent(rb);
            }
        }
        if(Display.getInstance().hasNativeTheme()) {
            RadioButton rb = new RadioButton("Native Theme");
            group1.add(rb);
            cmpGroup.addComponent(rb);
        }
        group1.setSelected(getSelectedThemeIndex());

        Button updateButton = new Button("Update Theme");
        updateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int newSelectedIndex = group1.getSelectedIndex();

                try {
                    if(newSelectedIndex >= THEMES.length) {
                        int i = newSelectedIndex - THEMES.length;
                        if(additionalThemeLabels != null && additionalThemeLabels.size() > i) {
                            Resources r = UIDemoMain.getResource((String)additionalThemes.elementAt(i));
                            UIManager.getInstance().setThemeProps(r.getTheme(r.getThemeResourceNames()[0]));
                        } else {
                            Display.getInstance().installNativeTheme();
                        }
                    } else {
                        Resources r = UIDemoMain.getResource(THEMES[newSelectedIndex]);
                        UIManager.getInstance().setThemeProps(r.getTheme(r.getThemeResourceNames()[0]));
                    }
                    Display.getInstance().getCurrent().refreshTheme();
                    UIDemoMain.getMainForm().refreshTheme();
                    
                    // patch to cause the main form to recalculate its size based on the LWUIT theme
                    //UIDemoMain.getInstance().calcGridSize();
                    UIDemoMain.getMainForm().revalidate();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        ComponentGroup buttonPanel = new ComponentGroup();
        buttonPanel.setElementUIID("ButtonGroup");
        buttonPanel.addComponent(updateButton);
        f.addComponent(buttonPanel);
    }

    private int getSelectedThemeIndex() {
        int selectedThemeIndex = 0;
        String themeName = UIManager.getInstance().getThemeName();
        if (themeName == null) {
            return 0;
        }
        themeName = themeName.toLowerCase();
        for (int i = 0; i < THEMES.length; i++) {
            if (THEMES[i].equals(themeName)) {
                selectedThemeIndex = i;
            }
        }
        return selectedThemeIndex;
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        return UIManager.getInstance().localize("themeHelp", "Help description");
    }
}
