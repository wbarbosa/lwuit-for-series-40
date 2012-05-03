/*
 * Feel free to update this class
 */

package userclasses;

import com.sun.lwuit.Display;
import javax.microedition.midlet.*;

/**
 * @author Your Name Here
 */
public class MainMIDlet extends MIDlet implements Runnable {
    public void startApp() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/res_file.res");
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
