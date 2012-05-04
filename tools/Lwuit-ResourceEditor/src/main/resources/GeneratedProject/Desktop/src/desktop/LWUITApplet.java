/*
 * Feel free to update this class
 */

package desktop;

import com.sun.lwuit.Display;
import java.applet.Applet;
import userclasses.StateMachine;

/**
 *
 * @author Your name here
 */
public class LWUITApplet extends Applet implements Runnable {

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init() {
        setLayout(new java.awt.BorderLayout());
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/res_file.res");
    }
}
