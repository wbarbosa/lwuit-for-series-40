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

package javax.microedition.media;
/**
 * Player controls the rendering of time based media data. It provides the methods to manage the Player's life cycle, controls the playback progress, obtains the presentation components, controls and provides the means to synchronize with other Players.
 * Simple playback example illustrates this.
 * The purpose of these life-cycle states is to provide programmatic control over potentially time-consuming operations. For example, when a Player is first constructed, it's in the UNREALIZED state. Transitioned from UNREALIZED to REALIZED, the Player performs the communication necessary to locate all of the resources it needs to function (such as communicating with a server or a file system). The realize method allows an application to initiate this potentially time-consuming process at an appropriate time.
 * Typically, a Player moves from the UNREALIZED state to the REALIZED state, then to the PREFETCHED state, and finally on to the STARTED state.
 * A Player stops when it reaches the end of media; when its stop time is reached; or when the stop method is invoked. When that happens, the Player moves from the STARTED state back to the PREFETCHED state. It is then ready to repeat the cycle.
 * To use a Player, you must set up parameters to manage its movement through these life-cycle states and then move it through the states using the Player's state transition methods.
 * The following methods must not be used when the Player is in the UNREALIZED state. getContentType setTimeBase getTimeBase setMediaTime getControls getControl An IllegalStateException will be thrown.
 * The realize method transitions the Player from the UNREALIZED state to the REALIZED state.
 * Although a realized Player does not have to acquire any resources, it is likely to have acquired all of the resources it needs except those that imply exclusive use of a scarce system resource, such as an audio device.
 * Normally, a Player moves from the UNREALIZED state to the REALIZED state. After realize has been invoked on a Player, the only way it can return to the UNREALIZED state is if deallocate is invoked before realize is completed. Once a Player reaches the REALIZED state, it never returns to the UNREALIZED state. It remains in one of four states: REALIZED, PREFETCHED, STARTED or CLOSED.
 * Once a Player is in the PREFETCHED state, it may be started. Prefetching reduces the startup latency of a Player to the minimum possible value.
 * When a started Player stops, it returns to the PREFETCHED state.
 * When the Player moves from the PREFETCHED to the STARTED state, it posts a STARTED event. When it moves from the STARTED state to the PREFETCHED state, it posts a STOPPED, END_OF_MEDIA or STOPPED_AT_TIME event depending on the reason it stopped.
 * The following methods must not be used when the Player is in the STARTED state: setTimeBase setLoopCount An IllegalStateException will be thrown.
 * Player Events
 * To receive events, an object must implement the PlayerListener interface and use the addPlayerListener method to register its interest in a Player's events. All Player events are posted to each registered listener.
 * The events are guaranteed to be delivered in the order that the actions representing the events occur. For example, if a Player stops shortly after it starts because it is playing back a very short media file, the STARTED event must always preceed the END_OF_MEDIA event.
 * An ERROR event may be sent any time an irrecoverable error has occured. When that happens, the Player is in the CLOSED state.
 * The Player event mechanism is extensible and some Players define events other than the ones described here. For a list of pre-defined player events, check the PlayerListener interface.
 * For example, in an implementation with an exclusive audio device, to alternate the audio playback of multiple Players, an application can selectively deallocate and prefetch individual Players.
 * Setting a different TimeBase on a Player instructs the Player to synchronize its playback rate according to the given TimeBase.
 * Two Players can be synchronized by getting the TimeBase from one Player and setting that on the second Player.
 * However, not all Players support using a different TimeBase other than its own. In such cases, a MediaException will be thrown when setTimeBase is called.
 */
public interface Player extends javax.microedition.media.Controllable{
    /**
     * The state of the Player indicating that the Player is closed.
     * Value 0 is assigned to CLOSED.
     * See Also:Constant Field Values
     */
    static final int CLOSED=0;

    /**
     * The state of the Player indicating that it has acquired all the resources to begin playing.
     * Value 300 is assigned to PREFETCHED.
     * See Also:Constant Field Values
     */
    static final int PREFETCHED=300;

    /**
     * The state of the Player indicating that it has acquired the required information but not the resources to function.
     * Value 200 is assigned to REALIZED.
     * See Also:Constant Field Values
     */
    static final int REALIZED=200;

    /**
     * The state of the Player indicating that the Player has already started.
     * Value 400 is assigned to STARTED.
     * See Also:Constant Field Values
     */
    static final int STARTED=400;

    /**
     * The returned value indicating that the requested time is unknown.
     * Value -1 is assigned to TIME_UNKNOWN.
     * See Also:Constant Field Values
     */
    static final long TIME_UNKNOWN=-1l;

    /**
     * The state of the Player indicating that it has not acquired the required information and resources to function.
     * Value 100 is assigned to UNREALIZED.
     * See Also:Constant Field Values
     */
    static final int UNREALIZED=100;

    /**
     * Add a player listener for this player.
     */
    abstract void addPlayerListener(javax.microedition.media.PlayerListener playerListener);

    /**
     * Close the Player and release its resources.
     * When the method returns, the Player is in the CLOSED state and can no longer be used. A CLOSED event will be delivered to the registered PlayerListeners.
     * If close is called on a closed Player the request is ignored.
     */
    abstract void close();

    /**
     * Release the scarce or exclusive resources like the audio device acquired by the Player.
     * When deallocate returns, the Player is in the UNREALIZED or REALIZED state.
     * If the Player is blocked at the realize call while realizing, calling deallocate unblocks the realize call and returns the Player to the UNREALIZED state. Otherwise, calling deallocate returns the Player to the REALIZED state.
     * If deallocate is called when the Player is in the UNREALIZED or REALIZED state, the request is ignored.
     * If the Player is STARTED when deallocate is called, deallocate will implicitly call stop on the Player.
     */
    abstract void deallocate();

    /**
     * Get the content type of the media that's being played back by this Player.
     * See content type for the syntax of the content type returned.
     */
    abstract java.lang.String getContentType();

    /**
     * Get the duration of the media. The value returned is the media's duration when played at the default rate. If the duration cannot be determined (for example, the Player is presenting live media) getDuration returns TIME_UNKNOWN.
     */
    abstract long getDuration();

    /**
     * Gets this Player's current
     * .
     * getMediaTime may return TIME_UNKNOWN to indicate that the media time cannot be determined. However, once getMediaTime returns a known time (time not equals to TIME_UNKNOWN), subsequent calls to getMediaTime must not return TIME_UNKNOWN.
     */
    abstract long getMediaTime();

    /**
     * Gets the current state of this Player. The possible states are:
     * ,
     * ,
     * ,
     * ,
     * .
     */
    abstract int getState();

    /**
     * Gets the TimeBase that this Player is using.
     */
    abstract javax.microedition.media.TimeBase getTimeBase();

    /**
     * Acquires the scarce and exclusive resources and processes as much data as necessary to reduce the start latency.
     * When prefetch completes successfully, the Player is in the PREFETCHED state.
     * If prefetch is called when the Player is in the UNREALIZED state, it will implicitly call realize.
     * If prefetch is called when the Player is already in the PREFETCHED state, the Player may still process data necessary to reduce the start latency. This is to guarantee that start latency can be maintained at a minimum.
     * If prefetch is called when the Player is in the STARTED state, the request will be ignored.
     * If the Player cannot obtain all of the resources it needs, it throws a MediaException. When that happens, the Player will not be able to start. However, prefetch may be called again when the needed resource is later released perhaps by another Player or application.
     */
    abstract void prefetch() throws javax.microedition.media.MediaException;

    /**
     * Constructs portions of the Player without acquiring the scarce and exclusive resources. This may include examining media data and may take some time to complete.
     * When realize completes successfully, the Player is in the REALIZED state.
     * If realize is called when the Player is in the REALIZED, PREFETCHTED or STARTED state, the request will be ignored.
     */
    abstract void realize() throws javax.microedition.media.MediaException;

    /**
     * Remove a player listener for this player.
     */
    abstract void removePlayerListener(javax.microedition.media.PlayerListener playerListener);

    /**
     * Set the number of times the Player will loop and play the content.
     * By default, the loop count is one. That is, once started, the Player will start playing from the current media time to the end of media once.
     * If the loop count is set to N where N is bigger than one, starting the Player will start playing the content from the current media time to the end of media. It will then loop back to the beginning of the content (media time zero) and play till the end of the media. The number of times it will loop to the beginning and play to the end of media will be N-1.
     * Setting the loop count to 0 is invalid. An IllegalArgumentException will be thrown.
     * Setting the loop count to -1 will loop and play the content indefinitely.
     * If the Player is stopped before the preset loop count is reached either because stop is called or a preset stop time (set with the StopTimeControl) is reached, calling start again will resume the looping playback from where it was stopped until it fully reaches the preset loop count.
     * An END_OF_MEDIA event will be posted every time the Player reaches the end of media. If the Player loops back to the beginning and starts playing again because it has not completed the loop count, a STARTED event will be posted.
     */
    abstract void setLoopCount(int count);

    /**
     * Sets the Player's
     * .
     * For some media types, setting the media time may not be very accurate. The returned value will indicate the actual media time set.
     * Setting the media time to negative values will effectively set the media time to zero. Setting the media time to beyond the duration of the media will set the time to the end of media.
     * There are some media types that cannot support the setting of media time. Calling setMediaTime will throw a MediaException in those cases.
     */
    abstract long setMediaTime(long now) throws javax.microedition.media.MediaException;

    /**
     * Sets the TimeBase for this Player.
     * A Player has a default TimeBase that is determined by the implementation. To reset a Player to its default TimeBase, call setTimeBase(null).
     */
    abstract void setTimeBase(javax.microedition.media.TimeBase master) throws javax.microedition.media.MediaException;

    /**
     * Starts the Player as soon as possible. If the Player was previously stopped by calling stop or reaching a preset stop time, it will resume playback from where it was previously stopped. If the Player has reached the end of media, calling start will automatically start the playback from the start of the media.
     * When start returns successfully, the Player must have been started and a STARTED event will be delivered to the registered PlayerListeners. However, the Player is not guaranteed to be in the STARTED state. The Player may have already stopped (in the PREFETCHED state) because the media has 0 or a very short duration.
     * If start is called when the Player is in the UNREALIZED or REALIZED state, it will implicitly call prefetch.
     * If start is called when the Player is in the STARTED state, the request will be ignored.
     */
    abstract void start() throws javax.microedition.media.MediaException;

    /**
     * Stops the Player. It will pause the playback at the current media time.
     * When stop returns, the Player is in the PREFETCHED state. A STOPPED event will be delivered to the registered PlayerListeners.
     * If stop is called on a stopped Player, the request is ignored.
     */
    abstract void stop() throws javax.microedition.media.MediaException;

}
