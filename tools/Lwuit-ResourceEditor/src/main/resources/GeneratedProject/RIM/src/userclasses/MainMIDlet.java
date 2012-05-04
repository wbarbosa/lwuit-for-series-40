/*
 * Feel free to update this class
 */

package userclasses;

import com.sun.lwuit.Display;
import net.rim.device.api.ui.UiApplication;

/**
 * @author Your Name Here
 */
public class MainMIDlet extends UiApplication implements Runnable {
    public MainMIDlet() {
        Display.init(this);
        Display.getInstance().callSerially(this);
    }

    public void run() {
        new StateMachine("/res_file.res");
    }
    
    public static void main(String[] arg) {
        new MainMIDlet();
    }
}
