/*
 *  Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.browser;

import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.impl.midp.VKBImplementationFactory;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;

/**
 * This MIDlet sets the theme and launches BrowserForm
 * This MIDlet considers the following JAD descriptors:
 *
 * Navigation Bar:
 * navbar_scalew (% of original width) - controls width of navigation buttons (useful for devices with high DPI). Default: 100
 * navbar_scaleh (% of original height) - controls width of navigation buttons (useful for devices with high DPI). Default: 100
 * navbar_display (true/false) - controls whether to show the navbar at start or not. Default: true
 * navbar_lock (true/false) - controls whether to allow navbar toggling.  Default: false
 * navbar_animate (true/false) - controls whether to animate navbar entry/exit or just appear/disapper w/o animation. Default: true
 *
 * General Appearance
 * use_bitmap_fonts (true/false) - true to use bitmap fonts, false for system. Default: false
 * menu_font (font name) - Indicates which font to use for the menubar.  Default: according to theme
 * vkb (true/false) - true to use LWUIT's Virtual keyboard, false otherwise.  Default: false
 * homepage (URL) - The homepage URL. Default: file:///index.html
 *
 * Cache/RMS:
 * cache_images (true/false) - Controls whether to cache images or not. Default: true
 * cache_html (true/false) - Controls whether to cache html documents or not. Default: false
 * rms_cookies / rms_formdata / rms_history / rms_cache (true/false) - Controls whether to save to RMS cookies/form/history/cache data.  Default: all true
 *
 * @author Ofir Leitner
 */
public class BrowserMIDlet extends javax.microedition.midlet.MIDlet {

    /**
     * {@inheritDoc}
     */
    protected void startApp() {
        try {
            Display.init(this);

            checkAppDescriptors();

            // Launch the form on the EDT
            Display.getInstance().callSerially(new Runnable() {
                public void run() {
                    new BrowserForm(BrowserMIDlet.this).show();
                }
            });
            
        } catch (Throwable ex) {
            ex.printStackTrace();
            Dialog.show("Exception", ex.getMessage(), "OK", null);
        }
    }

    private void checkAppDescriptors() {
            int scaleW=getIntAppProperty("navbar_scalew");
            int scaleH=getIntAppProperty("navbar_scaleh");
            if (scaleW>=50) {
                BrowserToolbar.scaleHorizontal=scaleW;
            }
            if (scaleH>=50) {
                BrowserToolbar.scaleVertical=scaleH;
            }

            BrowserForm.displayNavBarOnInit=getBooleanAppProperty("navbar_display", BrowserForm.displayNavBarOnInit);
            BrowserForm.lockNavBar=getBooleanAppProperty("navbar_lock", BrowserForm.lockNavBar);
            BrowserForm.useBitmapFonts=getBooleanAppProperty("use_bitmap_fonts", BrowserForm.useBitmapFonts);
            BrowserForm.animateToolbar=getBooleanAppProperty("navbar_animate", BrowserForm.animateToolbar);

            // Note that if rms_cache is turned off - then these won't help:
            HttpRequestHandler.CACHE_IMAGES=getBooleanAppProperty("cache_images", HttpRequestHandler.CACHE_IMAGES);
            HttpRequestHandler.CACHE_HTML=getBooleanAppProperty("cache_html", HttpRequestHandler.CACHE_HTML);
            HttpRequestHandler.CACHE_CSS=getBooleanAppProperty("cache_css", HttpRequestHandler.CACHE_CSS);

            for (int i=0;i<Storage.RMS_NAMES.length;i++) { //check rms_cookies , rms_formdata ,rms_history, rms_images
                Storage.RMS_ENABLED[i]=getBooleanAppProperty("rms_"+Storage.RMS_NAMES[i], Storage.RMS_ENABLED[i]);
            }

            String vkb=getAppProperty("vkb"); //Virtual keyboard
            if ((vkb!=null) && (vkb.equalsIgnoreCase("true"))) {
                VKBImplementationFactory.init();
            }

            BrowserForm.PAGE_HOME=getStringAppProperty("homepage",BrowserForm.PAGE_HOME);
            BrowserForm.menuFontName=getStringAppProperty("menu_font",BrowserForm.menuFontName);

    }

    private String getStringAppProperty(String key,String defaultVal) {
        String value=getAppProperty(key);
        if (value==null) {
            value=defaultVal;
        }
        //System.out.println(key+": "+value);
        return value;
    }

    private boolean getBooleanAppProperty(String key,boolean defaultVal) {
        String value=getAppProperty(key);
        boolean boolVal=defaultVal;
        if (value!=null) {
            boolVal=value.equalsIgnoreCase("true");
        }
        //System.out.println(key+": "+boolVal);
        return boolVal;
    }

    private int getIntAppProperty(String key) {
        String value=getAppProperty(key);
        int intVal=-1;
        if (value!=null) {
            try {
                intVal=Integer.parseInt(value);
            } catch (NumberFormatException nfe ) {

            }
        }
        //System.out.println(key+": "+intVal);
        return intVal;
    }

    /**
     * {@inheritDoc}
     */
    protected void pauseApp() {
    }

    /**
     * {@inheritDoc}
     */
    protected void destroyApp(boolean arg0) {
        //System.out.println("Saving cookies");
        saveToRMS();
    }

    /**
     * Saves all data to the RMS on application termination
     */
    void saveToRMS() {
        Storage.commitCookies();
        Storage.commitFormData();
        Storage.commitHistory();
    }

}
