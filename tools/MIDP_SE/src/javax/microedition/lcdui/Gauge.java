/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package javax.microedition.lcdui;
/**
 * Implements a graphical display, such as a bar graph, of an integer value. The Gauge contains a current value that lies between zero and the maximum value, inclusive. The application can control the current value and maximum value. The range of values specified by the application may be larger than the number of distinct visual states possible on the device, so more than one value may have the same visual representation.
 * For example, consider a Gauge object that has a range of values from zero to 99, running on a device that displays the Gauge's approximate value using a set of one to ten bars. The device might show one bar for values zero through nine, two bars for values ten through 19, three bars for values 20 through 29, and so forth.
 * A Gauge may be interactive or non-interactive. Applications may set or retrieve the Gauge's value at any time regardless of the interaction mode. The implementation may change the visual appearance of the bar graph depending on whether the object is created in interactive mode.
 * In interactive mode, the user is allowed to modify the value. The user will always have the means to change the value up or down by one and may also have the means to change the value in greater increments. The user is prohibited from moving the value outside the established range. The expected behavior is that the application sets the initial value and then allows the user to modify the value thereafter. However, the application is not prohibited from modifying the value even while the user is interacting with it.
 * In many cases the only means for the user to modify the value will be to press a button to increase or decrease the value by one unit at a time. Therefore, applications should specify a range of no more than a few dozen values.
 * In non-interactive mode, the user is prohibited from modifying the value. Non-interactive mode is used to provide feedback to the user on the state of a long-running operation. One expected use of the non-interactive mode is as a progress indicator or activity indicator to give the user some feedback during a long-running operation. The application may update the value periodically using the setValue() method.
 * A non-interactive Gauge can have a definite or indefinite range. If a Gauge has definite range, it will have an integer value between zero and the maximum value set by the application, inclusive. The implementation will provide a graphical representation of this value such as described above.
 * A non-interactive Gauge that has indefinite range will exist in one of four states: continuous-idle, incremental-idle, continuous-running, or incremental-updating. These states are intended to indicate to the user that some level of activity is occurring. With incremental-updating, progress can be indicated to the user even though there is no known endpoint to the activity. With continuous-running, there is no progress that gets reported to the user and there is no known endpoint; continuous-running is merely a busy state indicator. The implementation should use a graphical display that shows this appropriately. The implementation may use different graphics for indefinite continuous gauges and indefinite incremental gauges. Because of this, separate idle states exist for each mode. For example, the implementation might show an hourglass or spinning watch in the continuous-running state, but show an animation with different states, like a beach ball or candy-striped bar, in the incremental-updating state.
 * In the continuous-idle or incremental-idle state, the Gauge indicates that no activity is occurring. In the incremental-updating state, the Gauge indicates activity, but its graphical representation should be updated only when the application requests an update with a call to setValue(). In the continuous-running state, the Gauge indicates activity by showing an animation that runs continuously, without update requests from the application.
 * The values CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, and INCREMENTAL_UPDATING have their special meaning only when the Gauge is non-interactive and has been set to have indefinite range. They are treated as ordinary values if the Gauge is interactive or if it has been set to have a definite range.
 * An application using the Gauge as a progress indicator should typically also attach a STOP command to the container containing the Gauge to allow the user to halt the operation in progress.
 * As mentioned above, a non-interactive Gauge may be used to give user feedback during a long-running operation. If the application can observe the progress of the operation as it proceeds to an endpoint known in advance, then the application should use a non-interactive Gauge with a definite range. For example, consider an application that is downloading a file known to be 20 kilobytes in size. The application could set the Gauge's maximum value to be 20 and set its value to the number of kilobytes downloaded so far. The user will be presented with a Gauge that shows the portion of the task completed at any given time.
 * If, on the other hand, the application is downloading a file of unknown size, it should use a non-interactive Gauge with indefinite range. Ideally, the application should call setValue(INCREMENTAL_UPDATING) periodically, perhaps each time its input buffer has filled. This will give the user an indication of the rate at which progress is occurring.
 * Finally, if the application is performing an operation but has no means of detecting progress, it should set a non-interactive Gauge to have indefinite range and set its value to CONTINUOUS_RUNNING or CONTINUOUS_IDLE as appropriate. For example, if the application has issued a request to a network server and is about to block waiting for the server to respond, it should set the Gauge's state to CONTINUOUS_RUNNING before awaiting the response, and it should set the state to CONTINUOUS_IDLE after it has received the response.
 * Since: MIDP 1.0
 */
public class Gauge extends Item{
    /**
     * The value representing the continuous-idle state of a non-interactive Gauge with indefinite range. In the continuous-idle state, the gauge shows a graphic indicating that no work is in progress.
     * This value has special meaning only for non-interactive gauges with indefinite range. It is treated as an ordinary value for interactive gauges and for non-interactive gauges with definite range.
     * The value of CONTINUOUS_IDLE is 0.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int CONTINUOUS_IDLE=0;

    /**
     * The value representing the continuous-running state of a non-interactive Gauge with indefinite range. In the continuous-running state, the gauge shows a continually-updating animation sequence that indicates that work is in progress. Once the application sets a gauge into the continuous-running state, the animation should proceed without further requests from the application.
     * This value has special meaning only for non-interactive gauges with indefinite range. It is treated as an ordinary value for interactive gauges and for non-interactive gauges with definite range.
     * The value of CONTINUOUS_RUNNING is 2.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int CONTINUOUS_RUNNING=2;

    /**
     * The value representing the incremental-idle state of a non-interactive Gauge with indefinite range. In the incremental-idle state, the gauge shows a graphic indicating that no work is in progress.
     * This value has special meaning only for non-interactive gauges with indefinite range. It is treated as an ordinary value for interactive gauges and for non-interactive gauges with definite range.
     * The value of INCREMENTAL_IDLE is 1.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int INCREMENTAL_IDLE=1;

    /**
     * The value representing the incremental-updating state of a non-interactive Gauge with indefinite range. In the incremental-updating state, the gauge shows a graphic indicating that work is in progress, typically one frame of an animation sequence. The graphic should be updated to the next frame in the sequence only when the application calls setValue(INCREMENTAL_UPDATING).
     * This value has special meaning only for non-interactive gauges with indefinite range. It is treated as an ordinary value for interactive gauges and for non-interactive gauges with definite range.
     * The value of INCREMENTAL_UPDATING is 3.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int INCREMENTAL_UPDATING=3;

    /**
     * A special value used for the maximum value in order to indicate that the Gauge has indefinite range. This value may be used as the maxValue parameter to the constructor, the parameter passed to setMaxValue(), and as the return value of getMaxValue().
     * The value of INDEFINITE is -1.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int INDEFINITE=-1;

    /**
     * Creates a new Gauge object with the given label, in interactive or non-interactive mode, with the given maximum and initial values. In interactive mode (where interactive is true) the maximum value must be greater than zero, otherwise an exception is thrown. In non-interactive mode (where interactive is false) the maximum value must be greater than zero or equal to the special value INDEFINITE, otherwise an exception is thrown.
     * If the maximum value is greater than zero, the gauge has definite range. In this case the initial value must be within the range zero to maxValue, inclusive. If the initial value is less than zero, the value is set to zero. If the initial value is greater than maxValue, it is set to maxValue.
     * If interactive is false and the maximum value is INDEFINITE, this creates a non-interactive gauge with indefinite range. The initial value must be one of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING.
     * label - the Gauge's labelinteractive - tells whether the user can change the valuemaxValue - the maximum value, or INDEFINITEinitialValue - the initial value in the range [0..maxValue], or one of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING if maxValue is INDEFINITE.
     * - if maxValue is not positive for interactive gauges
     * - if maxValue is neither positive nor INDEFINITE for non-interactive gauges
     * - if initialValue is not one of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING for a non-interactive gauge with indefinite range
     * ,
     * ,
     * ,
     * ,
     */
    public Gauge(java.lang.String label, boolean interactive, int maxValue, int initialValue){
         //TODO codavaj!!
    }

    /**
     * Adds a context sensitive Command to the item. The semantic type of Command should be ITEM. The implementation will present the command only when the the item is active, for example, highlighted.
     * If the added command is already in the item (tested by comparing the object references), the method has no effect. If the item is actually visible on the display, and this call affects the set of visible commands, the implementation should update the display as soon as it is feasible to do so.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void addCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Gets the maximum value of this Gauge object.
     * If this gauge is interactive, the maximum value will be a positive integer. If this gauge is non-interactive, the maximum value will be a positive integer (indicating that the gauge has definite range) or the special value INDEFINITE (indicating that the gauge has indefinite range).
     */
    public int getMaxValue(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current value of this Gauge object.
     * If this Gauge object is a non-interactive gauge with indefinite range, the value returned will be one of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING. Otherwise, it will be an integer between zero and the gauge's maximum value, inclusive.
     */
    public int getValue(){
        return 0; //TODO codavaj!!
    }

    /**
     * Tells whether the user is allowed to change the value of the Gauge.
     */
    public boolean isInteractive(){
        return false; //TODO codavaj!!
    }

    /**
     * Sets default Command for this Item. If the Item previously had a default Command, that Command is no longer the default, but it remains present on the Item.
     * If not null, the Command object passed becomes the default Command for this Item. If the Command object passed is not currently present on this Item, it is added as if addCommand(javax.microedition.lcdui.Command) had been called before it is made the default Command.
     * If null is passed, the Item is set to have no default Command. The previous default Command, if any, remains present on the Item.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setDefaultCommand(Command cmd){
        return; //TODO codavaj!!
    }

    /**
     * Sets a listener for Commands to this Item, replacing any previous ItemCommandListener. A null reference is allowed and has the effect of removing any existing listener.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setItemCommandListener(ItemCommandListener l){
        return; //TODO codavaj!!
    }

    /**
     * Sets the label of the Item. If label is null, specifies that this item has no label.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setLabel(java.lang.String label){
        return; //TODO codavaj!!
    }

    /**
     * Sets the layout directives for this item.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setLayout(int layout){
        return; //TODO codavaj!!
    }

    /**
     * Sets the maximum value of this Gauge object.
     * For interactive gauges, the new maximum value must be greater than zero, otherwise an exception is thrown. For non-interactive gauges, the new maximum value must be greater than zero or equal to the special value INDEFINITE, otherwise an exception is thrown.
     * If the new maximum value is greater than zero, this provides the gauge with a definite range. If the gauge previously had a definite range, and if the current value is greater than new maximum value, the current value is set to be equal to the new maximum value. If the gauge previously had a definite range, and if the current value is less than or equal to the new maximum value, the current value is left unchanged.
     * If the new maximum value is greater than zero, and if the gauge had previously had indefinite range, this new maximum value provides it with a definite range. Its graphical representation must change accordingly, the previous state of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING is ignored, and the current value is set to zero.
     * If this gauge is non-interactive and the new maximum value is INDEFINITE, this gives the gauge indefinite range. If the gauge previously had a definite range, its graphical representation must change accordingly, the previous value is ignored, and the current state is set to CONTINUOUS_IDLE. If the gauge previously had an indefinite range, setting the maximum value to INDEFINITE will have no effect.
     */
    public void setMaxValue(int maxValue){
        return; //TODO codavaj!!
    }

    /**
     * Sets the preferred width and height for this Item. Values for width and height less than -1 are illegal. If the width is between zero and the minimum width, inclusive, the minimum width is used instead. If the height is between zero and the minimum height, inclusive, the minimum height is used instead.
     * Supplying a width or height value greater than the minimum width or height locks that dimension to the supplied value. The implementation may silently enforce a maximum dimension for an Item based on factors such as the screen size. Supplying a value of -1 for the width or height unlocks that dimension. See Item Sizes for a complete discussion.
     * It is illegal to call this method if this Item is contained within an Alert.
     */
    public void setPreferredSize(int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Sets the current value of this Gauge object.
     * If the gauge is interactive, or if it is non-interactive with definite range, the following rules apply. If the value is less than zero, zero is used. If the current value is greater than the maximum value, the current value is set to be equal to the maximum value.
     * If this Gauge object is a non-interactive gauge with indefinite range, then value must be one of CONTINUOUS_IDLE, INCREMENTAL_IDLE, CONTINUOUS_RUNNING, or INCREMENTAL_UPDATING. Other values will cause an exception to be thrown.
     */
    public void setValue(int value){
        return; //TODO codavaj!!
    }

}
