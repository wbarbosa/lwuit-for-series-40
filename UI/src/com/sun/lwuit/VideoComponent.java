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
package com.sun.lwuit;

import java.io.IOException;
import java.io.InputStream;

/**
 * Allows embedding a video into a LWUIT component, video creation/management is
 * very platform specific and this component tries to abstract that logic as much
 * as possible while maintaining portability.
 * <p>Notice that unlike the previous implementation these implementations completely
 * hide the underlying MMAPI (or other) interface. To access it directly you can extract
 * the Player/VideoControl instance (if applicable in this implementation) by invoking
 * getClientProperty("Player")/getClientProperty("VideoController"). This behavior is
 * implementation specific and we encorage implementations to expose such features via
 * the client property facility.
 *
 * @author Shai Almog
 */
public abstract class VideoComponent extends PeerComponent {
    /**
     * This constructor is used internally by the LWUIT implementation class
     * 
     * @param nativePeer the native peer
     */
    protected VideoComponent(Object nativePeer) {
        super(nativePeer);
        setFocusable(false);
    }

    /**
     * Some devices might not allow extracting the native peer before the first initialization
     * 
     * @param nativePeer the native peer
     */
    protected void setNativePeer(Object nativePeer) {
        super.setNativePeer(nativePeer);
    }

    /**
     * Create a video component
     *
     * @param url the platform specific location for the sound
     * @return a VideoComponent that can be used to control the playback of the
     * video
     * @throws java.io.IOException if the allocation fails
     */
    public static VideoComponent createVideoPeer(String url) throws IOException {
        return Display.getInstance().getImplementation().createVideoPeer(url);
    }

    /**
     * Create a video component
     *
     * @param stream the stream containing the media data
     * @param mimeType the type of the data in the stream
     * @return a VideoComponent that can be used to control the playback of the
     * video
     * @throws java.io.IOException if the allocation fails
     */
    public static VideoComponent createVideoPeer(InputStream stream, String mimeType) throws IOException {
        return Display.getInstance().getImplementation().createVideoPeer(stream, mimeType);
    }


    /**
     * Start media playback implicitly setting the component to visible
     */
    public abstract void start();

    /**
     * Stop media playback
     */
    public abstract void stop();

    /**
     * Set the number of times the media should loop
     *
     * @param count the number of times the media should loop
     */
    public abstract void setLoopCount(int count);

    /**
     * Return the duration of the media
     *
     * @return the duration of the media
     */
    public int getMediaTimeSeconds() {
        return getMediaTimeMS() / 1000;
    }

    /**
     * "Jump" to a point in time within the media
     *
     * @param now the point in time to "Jump" to
     * @return the media time in seconds
     */
    public int setMediaTimeSeconds(int now) {
        return setMediaTimeMS(now * 1000) / 1000;
    }

    /**
     * Return the duration of the media in milliseconds
     *
     * @return the duration of the media
     */
    public abstract int getMediaTimeMS();

    /**
     * "Jump" to a point in time within the media in milliseconds
     *
     * @param now the point in time to "Jump" to
     * @return the media time in milliseconds
     */
    public abstract int setMediaTimeMS(int now);

    /**
     * Returns the duration in milliseconds
     * 
     * @return the media duration
     */
    public abstract int getMediaDuration();

    /**
     * Indicates if the media is currently in the playing state
     * @return true if the media is playing
     */
    public abstract boolean isPlaying();

    /**
     * Toggles the fullscreen mode
     *
     * @param fullscreen true for fullscreen mode
     */
    public abstract void setFullScreen(boolean fullscreen);

    /**
     * Indicates the fullscreen mode
     *
     * @return true for fullscreen mode
     */
    public abstract boolean isFullScreen();

    /**
     * Some platforms might allow invoking the native video player for this file type
     *
     * @return true if the native player can be used for this file
     */
    public boolean hasNativeFullscreenPlayer() {
        return false;
    }

    /**
     * This method will only work if hasNativeFullscreenPlayer returns true
     */
    public void playInNativePlayer() {
    }
}
