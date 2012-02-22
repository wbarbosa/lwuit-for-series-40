/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.Display;

/**
 * @author Chen Fishbein
 */
public class LWUITXlet implements javax.microedition.xlet.Xlet {
    /**
     * Default constructor without arguments should be.
     */
    public LWUITXlet() {
    }

    /**
     * Put your initialization here, not in constructor.
     * If something goes wrong, XletStateChangeException
     * should be thrown.
     */
    public void initXlet(javax.microedition.xlet.XletContext context)
        throws javax.microedition.xlet.XletStateChangeException {
        System.out.println("xlet");
        try {
            Display.init(context.getContainer());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Xlet will be started here.
     * If something goes wrong, XletStateChangeException
     * should be thrown.
     */
    public void startXlet() throws javax.microedition.xlet.XletStateChangeException {
        start();
    }

    public static void main(String [] args){
        System.out.println("main");
        try {
            Display.init(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new LWUITXlet().start();
    }


    public void start() {
        Display.getInstance().callSerially(new Runnable() {

            public void run() {
                new UIDemoMain().startApp();
            }
        });
    }
    /**
     * Free resources, stop unnecessary threads, remove
     * itself from the screen.
     */
    public void pauseXlet() {
        // TODO implement
    }

    /**
     * Destroy yout xlet here.
     * If parameter is false, you can try to not destroy xlet
     * by throwing an XletStateChangeException
     */
    public void destroyXlet(boolean unconditional)
        throws javax.microedition.xlet.XletStateChangeException {
        // TODO implement
    }
}
