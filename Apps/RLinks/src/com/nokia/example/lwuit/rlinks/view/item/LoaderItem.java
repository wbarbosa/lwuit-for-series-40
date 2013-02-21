/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.view.item;

import com.nokia.example.lwuit.rlinks.Main;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.util.Resources;

/**
 * Label component containing animation for loading indicator. Assumes the
 * animation to be a single vertical sprite image with square frames as default.
 */
public class LoaderItem
        extends Label {

    private int frames = 0;
    private int frame = 0;
    private int frameWidth = 0;
    private int frameHeight = 0;
    private long interval = 100;
    private long lastUpdate = 0;
    private Image sprite;

    /**
     * Constructor for LoadingIndicator. Creates a loading animation based on
     * vertically aligned sprite image.
     * @param image Name of the animation sprite in theme file
     * @param frameWidth Width of a single animation frame
     * @param frameHeight Height of a single animation frame
     */
    public LoaderItem(String image, int frames) {
        setFocusable(false);
        Resources res = Main.getResources();
        sprite = res.getImage(image);

        if (frames > 0 && sprite.getHeight() % frames != 0) {
            throw new IllegalArgumentException("The height of " + image + " sprite is "
                    + "not divisible with the specified numer of frames " + frames);
        }

        this.frameWidth = sprite.getWidth();
        this.frameHeight = sprite.getHeight() / frames;

        updateFrameCount();
        updateFrame();
        getStyle().setAlignment(CENTER, true);
    }

    /**
     * Registers component as animated
     */
    protected void initComponent() {
        getComponentForm().registerAnimated(this);
    }

    /**
     * Animates the indicator
     * @return
     */
    public boolean animate() {
        boolean animate = super.animate();
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdate) > interval) {
            frame = ++frame % frames;
            updateFrame();
            animate = true;
            lastUpdate = currentTime;
        }
        return animate;
    }

    /**
     * Sets the interval for animation frame updates
     * @param interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Sets the height for a single frame
     * @param frameHeight
     */
    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
        updateFrameCount();
    }

    /**
     * Sets the widht for a single frame
     * @param frameWidth
     */
    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
        updateFrameCount();
    }

    /**
     * Updates the animation frame
     */
    private void updateFrame() {
        setIcon(sprite.subImage(0, frame * frameHeight, frameWidth, frameHeight,
                                true));
    }

    /**
     * Calculates the amount of frames based on frame dimensions
     */
    private void updateFrameCount() {
        frames = sprite.getHeight() / frameHeight;
    }
}
