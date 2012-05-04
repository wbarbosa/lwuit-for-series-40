/**
 * Your application code goes here
 */

package userclasses;

import generated.StateMachineBase;
import com.sun.lwuit.*;
import com.sun.lwuit.util.*;
import com.sun.lwuit.events.*;

/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars() {
    }
}
