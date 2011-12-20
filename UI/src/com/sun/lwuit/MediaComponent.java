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

import com.sun.lwuit.geom.*;
import com.sun.lwuit.impl.LWUITImplementation;

/**
 * A component allowing us to embed and control rich media content
 * 
 * @author Nir Shabi
 * @deprecated replaced by VideoComponent, this class will be removed in the next
 * version of LWUIT
 */
public class MediaComponent extends Component {

    private Object player;
    private Object vidc;
    private boolean fullscreen = false;

    /** 
     * Creates a new instance of MediaComponent 
     * 
     * @param player the media player
     */
    public MediaComponent(Object player) {
        this.player = player;
        setFocusable(false);
        vidc = Display.getInstance().getImplementation().createVideoComponent(player);

    }

    /**
     * @inheritDoc
     */
    protected void initComponent() {
        getComponentForm().registerMediaComponent(this);
    }

    /**
     * @inheritDoc
     */
    protected void deinitialize() {
        getComponentForm().deregisterMediaComponent(this);
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) { 
        if(isVisible()){
            Display.getInstance().getImplementation().paintVideo(this, fullscreen, g.getGraphics(), vidc, player);
        }
    }

    protected void paintBackground(Graphics g) {
    }

    public void paintBackgrounds(Graphics g) {
    }

    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize() {
        LWUITImplementation impl = Display.getInstance().getImplementation();
        return new Dimension(impl.getVideoWidth(vidc), impl.getVideoHeight(vidc));
    }

    /**
     * Display the embedded media component
     * 
     * @param visible true to display, false to hide
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (vidc != null) {
            Display.getInstance().getImplementation().setVideoVisible(vidc, visible);
        }
    }

    /**
     * Start media playback implicitly setting the component to visible
     */
    public void start(){
        Display.getInstance().getImplementation().startVideo(player, vidc);
    }

    /**
     * Stope media playback 
     */
    public void stop(){
        Display.getInstance().getImplementation().stopVideo(player, vidc);
    }

    /**
     * Set the number of times the media should loop
     * 
     * @param count the number of times the media should loop
     */
    public void setLoopCount(int count) {
        Display.getInstance().getImplementation().setVideoLoopCount(player, count);
    }

    /**
     * Return the duration of the media
     * 
     * @return the duration of the media
     */
    public long getMediaTime() {
        return Display.getInstance().getImplementation().getMediaTime(player);
    }

    /**
     * "Jump" to a point in time within the media
     * 
     * @param now the point in time to "Jump" to
     * @return the media time in microseconds
     */
    public long setMediaTime(long now) {
        return Display.getInstance().getImplementation().setMediaTime(player, now);
    }

    /**
     * Toggles the fullscreen mode
     * 
     * @param fullscreen true for fullscreen mode
     */
    public void setFullScreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        repaint();
    }

    /**
     * Indicates the fullscreen mode
     *
     * @return true for fullscreen mode
     */
    public boolean isFullScreen() {
        return fullscreen;
    }

    /**
     * Returns the native video control if such a control exists, this is required
     * for some platforms.
     *
     * @return the underlying native video control or null
     */
    public Object getVideoControl() {
        return vidc;
    }
}
