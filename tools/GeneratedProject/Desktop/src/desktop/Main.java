/*
 * Feel free to update this class
 */

package desktop;

import com.sun.lwuit.Display;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import userclasses.StateMachine;

/**
 *
 * @author Your Name Here
 */
public class Main implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // for a full screen application use this code
        // Display.init(null);

        // for a windowed application use this code
        Frame f = new Frame("LWUIT App");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Display.getInstance().exitApplication();
            }
        });
        f.setLayout(new java.awt.BorderLayout());
        Display.init(f);
        f.setSize(320, 480);
        f.validate();
        f.setLocationByPlatform(true);
        f.setVisible(true);

        // give the awt frame time to appear so LWUIT doesn't start rendering before the AWT Frame finished initializing
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Display.getInstance().callSerially(new Main());
            }
        }
    }

    public void run() {
        new StateMachine("/res_file.res");
    }
}
