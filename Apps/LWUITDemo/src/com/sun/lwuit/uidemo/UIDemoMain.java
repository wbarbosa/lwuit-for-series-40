/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.*;
import com.sun.lwuit.animations.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.layouts.*;
import com.sun.lwuit.plaf.*;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Bootstraps the UI toolkit demos
 *
 * @author Shai Almog
 */
public class UIDemoMain
    implements ActionListener {

    private static final int EXIT_COMMAND = 1;
    private static final int BACK_COMMAND = 3;
    private static final int ABOUT_COMMAND = 4;
    private static final int DRAG_MODE_COMMAND = 5;
    private static final int SCROLL_MODE_COMMAND = 6;
    private static final int RTL_COMMAND = 7;
    private static final int LANGUAGE_COMMAND = 8;
    private static final int DEMOS_COMMAND = 9;
    private static final Command exitCommand = new Command("Exit", EXIT_COMMAND);
    private static final Command backCommand = new Command("Back", BACK_COMMAND);
    private static final Command aboutCommand = new Command("About", ABOUT_COMMAND);
    private static final Command scrollModeCommand = new Command("Scroll", SCROLL_MODE_COMMAND);
    private static final Command rtlCommand = new Command("RTL", RTL_COMMAND);
    private static Command dragModeCommand; // Initialized later, needs an image resource
    static final Command showDemosCommand = new Command("Demos", DEMOS_COMMAND);
    

    /*
     * Used for Localization
     */
    private static final Command languageCommand = new Command("Chinese", LANGUAGE_COMMAND);
    private static String localLanguage;
    static final Demo[] DEMOS = new Demo[]{
        /* new ThemeDemo(), */new RenderingDemo(), new AnimationDemo(), new ButtonsDemo(),
        new TransitionDemo(), new FontDemo(), new TabsDemo(), new DialogDemo(),
        new LayoutDemo(), new ScrollDemo(), new TableDemo(), new TreeDemo(),
        new HTMLDemo()
    };
    private Demo currentDemo;
    private Hashtable demosHash = new Hashtable();
    private static Form mainMenu;
    private int cols = 0;
    private int elementWidth;
    private Resources res;
    private static UIDemoMain instance;
    private Container uiContent;
    private Container demoPanel;

    static UIDemoMain getInstance() {
        return instance;
    }

    public void startApp() {
        instance = this;
        try {
            localLanguage = System.getProperty("microedition.locale");
            if (!localLanguage.equalsIgnoreCase("en-US") && !localLanguage.equalsIgnoreCase("zh-CN")) {
                localLanguage = "en-US";
            }
        }
        catch (Throwable t) {
            localLanguage = "en-US";
            // can throw a security exception in an applet
        }
        try {

            //set the theme
            /* if(Display.getInstance().hasNativeTheme()) { Display.getInstance().installNativeTheme(); } else {
             * Resources theme = Resources.open("/TimelineTheme.res");
             * UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            } */
            //open the resources file that contains all the fonts
            res = Resources.open("/resources.res");

            //open the resources file that contains all the icons
            res = Resources.open("/images.res");

            setMainForm(res);
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            Dialog.show("Exception", ex.getMessage(), "OK", null);
        }
    }

    /**
     * Used instead of using the Resources API to allow us to fetch locally downloaded resources
     *
     * @param name the name of the resource
     * @return a resources object
     */
    public static Resources getResource(String name)
        throws IOException {
        return Resources.open("/" + name + ".res");
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) {
    }

    public static void setTransition(Transition in, Transition out) {
        mainMenu.setTransitionInAnimator(in);
        mainMenu.setTransitionOutAnimator(out);
        if (in != null) {
            UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(null);
            UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionOut(null);
        }
    }

    public static void setMenuTransition(Transition in, Transition out) {
        mainMenu.setMenuTransitions(in, out);
        UIManager.getInstance().getLookAndFeel().setDefaultMenuTransitionIn(null);
        UIManager.getInstance().getLookAndFeel().setDefaultMenuTransitionOut(null);
    }

    public static Form getMainForm() {
        return mainMenu;
    }

    public void calcGridSize() {
        // application logic determins the number of columns based on the screen size
        // this is why we need to be aware of screen size changes which is currently
        // only received using this approach
        int width = Display.getInstance().getDisplayWidth(); //get the display width

        elementWidth = 0;

        int buttonCount = uiContent.getComponentCount();
        for (int i = 0; i < buttonCount; i++) {
            elementWidth = Math.max(uiContent.getComponentAt(i).getPreferredW(), elementWidth);
        }

        //Calculate the number of columns for the GridLayout according to the
        //screen width
        if (elementWidth > 0) {
            cols = width / elementWidth;
        }
        else {
            cols = 4;
        }
        int rows = DEMOS.length / cols;
        GridLayout g = new GridLayout(Math.max(1, rows), cols);
        g.setAutoFit(true);
        uiContent.setLayout(g);
    }

    void setMainForm(Resources r) {
        Font.setBitmapFontEnabled(true);
        if (localLanguage != null) {
            try {
                Resources res = Resources.open("/resources.res");
                UIManager.getInstance().setResourceBundle(res.getL10N("localize", localLanguage));
                // don't use bitmap fonts for localization since we still want to run on low end devices!
                if (!"en".equals(localLanguage)) {
                    Font.setBitmapFontEnabled(false);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Form main;
        if (Display.getInstance().isTablet()) {
            main = new Form("");
            main.setLayout(new BorderLayout());
            demoPanel = new Container(new BorderLayout());
            main.addComponent(BorderLayout.CENTER, demoPanel);
            DEMOS[0].run(backCommand, UIDemoMain.this, demoPanel);
            main.setScrollable(false);
            Form uiContentParent = new Form("LWUIT Demo");
            uiContentParent.setHideInPortrait(true);
            uiContent = uiContentParent.getContentPane();
            uiContentParent.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            main.addComponent(BorderLayout.WEST, uiContentParent);
            main.addOrientationListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    Form f = (Form) demoPanel.getComponentAt(0);
                    if (Display.getInstance().isPortrait()) {
                        f.addCommand(showDemosCommand, 1);
                    }
                    else {
                        f.removeCommand(showDemosCommand);
                    }
                }
            });
        }
        else {
            main = new Form("LWUIT Demo");

            uiContent = main.getContentPane();
        }
        if (mainMenu != null) {
            main.setTransitionInAnimator(mainMenu.getTransitionInAnimator());
            main.setTransitionOutAnimator(mainMenu.getTransitionOutAnimator());
        }
        mainMenu = main;

        buildDemoMenu(r, uiContent);

        mainMenu.addCommand(exitCommand);
        mainMenu.setBackCommand(exitCommand);
        mainMenu.addCommand(aboutCommand);

        if (localLanguage == null) {
            localLanguage = "en-US";
        }
        if (localLanguage.equalsIgnoreCase("zh-CN")) {
            languageCommand.setCommandName("English");
        }
        else {
            languageCommand.setCommandName("Chinese");
        }
        mainMenu.addCommand(languageCommand);

        mainMenu.addCommand(rtlCommand);
        
        dragModeCommand = new Command("Drag", DRAG_MODE_COMMAND);
        if (Display.getInstance().isTouchScreenDevice()) {
            if (Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
                dragModeCommand = new Command("Drag",
                        r.getImage("Drag_mode.png"),
                        DRAG_MODE_COMMAND);
            }
            mainMenu.setDefaultCommand(dragModeCommand);
            mainMenu.addCommand(dragModeCommand);
        }

        mainMenu.addCommandListener(this);
        if (Display.getInstance().getCurrent() != null) {
            Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.
                createEmpty());
        }
        //mainMenu.show();
    }

    private void buildDemoMenu(Resources r, final Container uiContent) {
        Image[] selectedImages = new Image[DEMOS.length];
        Image[] unselectedImages = new Image[DEMOS.length];

        final ButtonActionListener bAListner = new ButtonActionListener();
        int textPosition = Label.BOTTOM;
        ButtonGroup bg = null;
        int alignment = Component.CENTER;
        if (Display.getInstance().isTablet()) {
            textPosition = Label.RIGHT;
            alignment = Component.LEFT;
            bg = new ButtonGroup();
        }
        for (int i = 0; i < DEMOS.length; i++) {
            Image temp = r.getImage(DEMOS[i].getName().toLowerCase() + "_sel.png");
            selectedImages[i] = temp;
            unselectedImages[i] = r.getImage(DEMOS[i].getName().toLowerCase() + ".png");
            Button btn;
            if (bg != null) {
                btn = new RadioButton(DEMOS[i].getName(), unselectedImages[i]);
                btn.setToggle(true);
                bg.add((RadioButton) btn);
                if (i == 0) {
                    ((RadioButton) btn).setSelected(true);
                }
            }
            else {
                btn = new Button(DEMOS[i].getName(), unselectedImages[i]);
            }
            final Button b = btn;
            b.setUIID("DemoButton");
            b.getUnselectedStyle().setAlignment(alignment);
            b.getSelectedStyle().setAlignment(alignment);
            b.getPressedStyle().setAlignment(alignment);
            b.setRolloverIcon(selectedImages[i]);
            b.setPressedIcon(selectedImages[i]);
            b.setTextPosition(textPosition);
            uiContent.addComponent(b);
            b.addActionListener(bAListner);
            demosHash.put(b, DEMOS[i]);
        }
        if (!Display.getInstance().isTablet()) {
            calcGridSize();
        }
    }

    /**
     * Invoked when a command is clicked. We could derive from Command but that would require 3
     * separate classes.
     */
    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        switch (cmd.getId()) {
            case EXIT_COMMAND:
                Display.getInstance().exitApplication();
                break;
            case BACK_COMMAND:
                currentDemo.cleanup();
                mainMenu.showBack();
                break;
            case ABOUT_COMMAND:
                Form aboutForm = new Form("About");
                aboutForm.setScrollable(true);
                aboutForm.setLayout(new BorderLayout());
                TextArea aboutText = new TextArea(getAboutText(), 5, 10);
                aboutText.setEditable(false);
                aboutForm.addComponent(BorderLayout.CENTER, aboutText);
                Command backFromAbout = new Command("Back") {

                    public void actionPerformed(ActionEvent evt) {
                        if (demoPanel != null) {
                            demoPanel.replace(demoPanel.getComponentAt(0), mainMenu, UIManager.
                                getInstance().getLookAndFeel().getDefaultFormTransitionOut().copy(
                                true));
                        }
                        else {
                            mainMenu.showBack();
                        }
                    }
                };
                aboutForm.addCommand(backFromAbout);
                aboutForm.setBackCommand(backFromAbout);
                if (demoPanel != null) {
                    demoPanel.replace(demoPanel.getComponentAt(0), aboutForm,
                        UIManager.getInstance().getLookAndFeel().getDefaultFormTransitionOut());
                }
                else {
                    aboutForm.show();
                }
                break;
            case DRAG_MODE_COMMAND:
                Container c = mainMenu.getContentPane();
                if (uiContent != null) {
                    c = uiContent;
                }
                c.setDropTarget(true);
                for (int iter = 0; iter < c.getComponentCount(); iter++) {
                    c.getComponentAt(iter).setDraggable(true);
                }
                mainMenu.removeCommand(dragModeCommand);
                mainMenu.setDefaultCommand(scrollModeCommand);
                mainMenu.addCommand(scrollModeCommand);
                break;
            case SCROLL_MODE_COMMAND:
                mainMenu.getContentPane().setDropTarget(false);
                for (int iter = 0; iter < mainMenu.getContentPane().getComponentCount(); iter++) {
                    mainMenu.getContentPane().getComponentAt(iter).setDraggable(false);
                }
                mainMenu.removeCommand(scrollModeCommand);
                mainMenu.setDefaultCommand(dragModeCommand); 
                mainMenu.addCommand(dragModeCommand);          
                break;
            case RTL_COMMAND:
                LookAndFeel laf = UIManager.getInstance().getLookAndFeel();
                laf.setRTL(!laf.isRTL());
                setMainForm(res);
                break;
            case DEMOS_COMMAND:
                Dialog popup = new Dialog("Demos");
                popup.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                popup.setScrollableY(true);
                buildDemoMenu(res, popup);
                popup.showPopupDialog(evt.getComponent());
                break;
            case LANGUAGE_COMMAND:
                if (localLanguage.equalsIgnoreCase("zh-CN")) {
                    localLanguage = "en-US";
                    languageCommand.setCommandName("Chinese");
                }
                else {
                    localLanguage = "zh-CN";
                    languageCommand.setCommandName("English");
                }
                setMainForm(res);
                break;
        }
    }

    private String getAboutText() {
        return UIManager.getInstance().localize("aboutString", "About text");
    }

    private class ButtonActionListener
        implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            Form f = ((Button) evt.getSource()).getComponentForm();
            if (f instanceof Dialog) {
                ((Dialog) f).dispose();
            }
            currentDemo = ((Demo) (demosHash.get(evt.getSource())));
            currentDemo.run(backCommand, UIDemoMain.this, demoPanel);
        }
    }

    static void executeDemo(int i) {
        DEMOS[i].run(backCommand, instance, instance.demoPanel);
    }
}
