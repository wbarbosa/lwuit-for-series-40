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

import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.EventDispatcher;

/**
 * The slider component serves both as a slider widget to allow users to select
 * a value on a scale via touch/arrows and also to indicate progress. The slider
 * defaults to percentage display but can represent any positive set of values.
 *
 * @author Shai Almog
 */
public class Slider extends Label {

    private int value;
    private int maxValue = 100;
    private int minValue = 0;
    private boolean vertical;
    private boolean editable;
    private EventDispatcher listeners = new EventDispatcher();
    private int increments = 4;
    private int previousX = -1, previousY = -1;
    private Style sliderFull;
    private Style sliderFullSelected;
    private boolean paintingFull;
    private boolean renderPercentageOnTop;
    private boolean renderValueOnTop;
    private boolean infinite = false;
    private float infiniteDirection = 0.03f;
    private Image thumbImage;
    private String text;

    /**
     * The default constructor uses internal rendering to draw its state
     */
    public Slider() {
        this("Slider", "SliderFull");
    }

    private Slider(String uiid, String fullUIID) {
        setFocusable(false);
        setUIID(uiid);
        sliderFull = UIManager.getInstance().getComponentStyle(fullUIID);
        sliderFullSelected = UIManager.getInstance().getComponentSelectedStyle(fullUIID);
        initCustomStyle(sliderFull);
        initCustomStyle(sliderFullSelected);
        setAlignment(CENTER);
    }

    /**
     * @inheritDoc
     */
    public void setUIID(String id) {
        super.setUIID(id);
        sliderFull = UIManager.getInstance().getComponentStyle(id + "Full");
        sliderFullSelected = UIManager.getInstance().getComponentSelectedStyle(id + "Full");
        initCustomStyle(sliderFull);
        initCustomStyle(sliderFullSelected);
    }

    /**
     * @inheritDoc
     */
    public void initComponent() {
        if (infinite) {
            getComponentForm().registerAnimated(this);
        }
    }

    /**
     * @inheritDoc
     */
    public void deinitialize() {
        if (infinite) {
            Form f = getComponentForm();
            if (f != null) {
                f.deregisterAnimated(this);
            }
        }
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        if (infinite) {

            super.animate();
            float f = infiniteDirection * (float)getRange();
            if (((int) f) == 0) {
                if (f < 0) {
                    f = -1;
                } else {
                    f = 1;
                }
            }
            value += ((int) f);
            if (value >= maxValue) {
                value = maxValue;
                infiniteDirection *= -1;
            }
            if (value <= 0) {
                value = 0;
                infiniteDirection *= -1;
            }
            return true;
        }
        return super.animate();
    }

    /**
     * The infinite slider functionality is used to animate progress for which
     * there is no defined value.
     *
     * @return true for infinite progress
     */
    public boolean isInfinite() {
        return infinite;
    }

    /**
     * Activates/disables the infinite slider functionality used to animate
     * progress for which there is no defined value.
     *
     * @param i true for infinite progress
     */
    public void setInfinite(boolean i) {
        if (infinite != i) {
            infinite = i;
            if (isInitialized()) {
                if (i) {
                    getComponentForm().registerAnimated(this);
                } else {
                    getComponentForm().deregisterAnimated(this);
                }
            }
        }
        if (i) {
            minValue = 0;
            maxValue = 100;
            setRenderPercentageOnTop(false);
            setRenderValueOnTop(false);
        }
    }

    /**
     * Creates an infinite progress slider
     *
     * @return a slider instance that has no end value
     */
    public static Slider createInfinite() {
        Slider s = new Slider();
        s.infinite = true;
        return s;
    }

    /**
     * @inheritDoc
     */
    public void refreshTheme() {
        super.refreshTheme();
        deinitializeCustomStyle(sliderFull);
        deinitializeCustomStyle(sliderFullSelected);
        sliderFull = UIManager.getInstance().getComponentStyle("SliderFull");
        sliderFullSelected = UIManager.getInstance().getComponentSelectedStyle("SliderFull");
    }

    /**
     * Indicates the value of slider according to the scale set by minimum and
     * maximum values set.
     *
     * @return the value on the slider
     */
    public int getSliderValue() {
        return value;
    }

    /**
     * Sets the value of the slider
     *
     * @param value the new value
     */
    public void setSliderValue(int value) {
        if (value < minValue) {
            throw new IllegalArgumentException("Slider value must be greater or equal than the minimum value.");
        }
        if (value > maxValue) {
            throw new IllegalArgumentException("Slider value must be less or equal than the maximum value.");
        }
        this.value = value;
        refreshText();
    }

    private void refreshText() {
        if (renderValueOnTop) {
            super.setText("" + getSliderValue());
        } else {
            if (renderPercentageOnTop) {
                super.setText(getProgress() + "%");
            } else {
                if (text != null) {
                    super.setText(text);
                }
                else {
                    super.setText("");
                }
                repaint();
            }
        }
    }

    /**
     * Indicates the value of progress made in percentage
     *
     * @return the progress on the slider
     */
    public int getProgress() {
        return (int) (getRelativeValue() * 100F);
    }

    /**
     * Calculates the slider range
     *
     * @return the slider range
     */
    private int getRange() {
        return maxValue - minValue;
    }

    /**
     * Returns the relative value of the slider in 0..1 range
     *
     * @return the relative value
     */
    private float getRelativeValue() {
        return ((float) (value - minValue) / (float) getRange());
    }
    
    /**
     * Sets the slider value based on relative value in 0..1 range
     *
     * @param relativeValue relative value in 0..1 range
     */
    private void setRelativeValue(float relativeValue) {
        float rounder = relativeValue > 0 ? 0.5f : -0.5f;
        setSliderValue((int) ((float) getRange() * relativeValue + minValue + rounder));
        refreshText();
    }

    /**
     * Sets the progress indicator level as percentage value
     *
     * @param value new value for progress
     */
    public void setProgress(int value) {
        setRelativeValue((float) value / 100F);
    }

    /**
     * @inheritDoc
     */
    public Style getStyle() {
        if (paintingFull) {
            if (hasFocus()) {
                return sliderFullSelected;
            }
            return sliderFull;
        }
        return super.getStyle();
    }

    /**
     * Return the size we would generally like for the component
     */
    protected Dimension calcPreferredSize() {
        Style style = getStyle();
        int prefW = 0, prefH = 0;
        if (style.getBorder() != null) {
            prefW = Math.max(style.getBorder().getMinimumWidth(), prefW);
            prefH = Math.max(style.getBorder().getMinimumHeight(), prefH);
        }
        // we don't really need to be in the font height but this provides
        // a generally good indication for size expectations
        if (Display.getInstance().isTouchScreenDevice() && isEditable()) {
            if (vertical) {
                return new Dimension(Math.max(prefW, Font.getDefaultFont().charWidth('X') * 2),
                        Math.max(prefH, Display.getInstance().getDisplayHeight() / 2));
            } else {
                return new Dimension(Math.max(prefW, Display.getInstance().getDisplayWidth() / 2),
                        Math.max(prefH, Font.getDefaultFont().getHeight() * 2));
            }
        } else {
            if (vertical) {
                return new Dimension(Math.max(prefW, Font.getDefaultFont().charWidth('X')),
                        Math.max(prefH, Display.getInstance().getDisplayHeight() / 2));
            } else {
                return new Dimension(Math.max(prefW, Display.getInstance().getDisplayWidth() / 2),
                        Math.max(prefH, Font.getDefaultFont().getHeight()));
            }
        }
    }

    /**
     * Paint the progress indicator
     */
    public void paintBackground(Graphics g) {
        super.paintBackground(g);
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight();
        int width = getWidth();
        int height = getHeight();

        int y = getY();
        if (infinite) {
            int blockSize = getWidth() / 5;
            int x = getX() + (int) (getRelativeValue() * (getWidth() - blockSize));
            g.clipRect(x, y, blockSize, height - 1);
        } else {
            if (vertical) {
                float pixelValue = getRelativeValue() * (float) getHeight();
                if (pixelValue < 1F) {
                    y += height - (int) Math.floor(pixelValue);
                } else {
                    y += height - (int) Math.ceil(pixelValue);
                }
            } else {
                float pixelValue = getRelativeValue() * (float) getWidth();
                if (pixelValue < 1F) {
                    width = (int) Math.floor(pixelValue);
                } else {
                    width = (int) Math.ceil(pixelValue);
                }
            }
            g.clipRect(getX(), y, width, height);
        }

        // paint the selected style
        paintingFull = true;
        super.paintBackground(g);
        paintingFull = false;

        g.setClip(clipX, clipY, clipW, clipH);
        if (thumbImage != null && !infinite) {
            if (!vertical) {
                int xPos = getX() + width - thumbImage.getWidth() / 2;
                xPos = Math.max(getX(), xPos);
                xPos = Math.min(getX() + getWidth() - thumbImage.getWidth(), xPos);
                g.drawImage(thumbImage, xPos, y + height / 2 - thumbImage.getHeight() / 2);
            } else {
                int yPos = y + height - thumbImage.getHeight() / 2;
                yPos = Math.max(getY(), yPos);
                yPos = Math.min(getY() + getHeight() - thumbImage.getHeight(), yPos);
                g.drawImage(thumbImage, getX() + width / 2 - thumbImage.getWidth() / 2, yPos);
            }
        }
    }

    /**
     * Indicates the slider is vertical
     *
     * @return true if the slider is vertical
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Indicates the slider is vertical
     *
     * @param vertical true if the slider is vertical
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * Indicates the slider is modifyable
     *
     * @return true if the slider is editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Indicates the slider is modifyable
     *
     * @param editable true if the slider is editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        setFocusable(editable);
    }

    public void pointerPressed(int x, int y) {
        if (!editable) {
            return;
        }

        if (vertical) {
            // turn the coordinate to a local coordinate and invert it
            y = y - getAbsoluteY();
            setRelativeValue(1F - (float) y / ((float) getHeight() - 1F));
        } else {
            x = Math.abs(x - getAbsoluteX());
            setRelativeValue((float) x / ((float) getWidth() - 1F));
        }

        if (vertical) {
            if (previousY < y) {
                fireDataChanged(DataChangedListener.ADDED, value);
            } else {
                fireDataChanged(DataChangedListener.REMOVED, value);
            }
            previousY = y;
        } else {
            if (previousX < x) {
                fireDataChanged(DataChangedListener.ADDED, value);
            } else {
                fireDataChanged(DataChangedListener.REMOVED, value);
            }
            previousX = x;

        }
    }

    /**
     * @inheritDoc
     */
    public void pointerDragged(int x, int y) {
        if (!editable) {
            return;
        }
        if (vertical && previousY == -1) {
            previousY = y;
            return;
        }
        if (!vertical && previousX == -1) {
            previousX = x;
            return;
        }

        int prevValue = getSliderValue();
        if (vertical) {
            y = y - getAbsoluteY();
            setRelativeValue(1F - (float) y / ((float) getHeight() - 1F));
        } else {
            x = x - getAbsoluteX();
            setRelativeValue((float) x / ((float) getWidth() - 1F));
        }

        if (prevValue != getSliderValue()) {

            if (vertical) {
                if (previousY < y) {
                    fireDataChanged(DataChangedListener.ADDED, value);
                } else {
                    fireDataChanged(DataChangedListener.REMOVED, value);
                }
                previousY = y;
            } else {
                if (previousX < x) {
                    fireDataChanged(DataChangedListener.ADDED, value);
                } else {
                    fireDataChanged(DataChangedListener.REMOVED, value);
                }
                previousX = x;
            }
        }
    }

    /**
     * @inheritDoc
     */
    protected void fireClicked() {
        setHandlesInput(!handlesInput());
    }

    /**
     * @inheritDoc
     */
    protected boolean isSelectableInteraction() {
        return editable;
    }

    /**
     * @inheritDoc
     */
    public void pointerReleased(int x, int y) {
        if (!editable) {
            return;
        }
        previousX = -1;
        previousY = -1;
    }

    /**
     * @inheritDoc
     */
    public void keyPressed(int code) {
        if (editable && handlesInput()) {
            int game = Display.getInstance().getGameAction(code);
            switch (game) {
                case Display.GAME_UP:
                    if (vertical) {
                        setProgress((byte) (Math.min(maxValue, value + increments)));
                        fireDataChanged(DataChangedListener.ADDED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_DOWN:
                    if (vertical) {
                        setProgress((byte) (Math.max(minValue, value - increments)));
                        fireDataChanged(DataChangedListener.REMOVED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_LEFT:
                    if (!vertical) {
                        setProgress((byte) (Math.max(minValue, value - increments)));
                        fireDataChanged(DataChangedListener.REMOVED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_RIGHT:
                    if (!vertical) {
                        setProgress((byte) (Math.min(maxValue, value + increments)));
                        fireDataChanged(DataChangedListener.ADDED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_FIRE:
                    if (!Display.getInstance().isThirdSoftButton()) {
                        fireClicked();
                    }
                    break;
            }
        } else {
            if (!Display.getInstance().isThirdSoftButton()
                    && Display.getInstance().getGameAction(code) == Display.GAME_FIRE) {
                fireClicked();
            }
        }
        super.keyPressed(code);
    }

    /**
     * The increments when the user presses a key to the left/right/up/down etc.
     *
     * @return increment value
     */
    public int getIncrements() {
        return increments;
    }

    /**
     * The increments when the user presses a key to the left/right/up/down etc.
     *
     * @param increments increment value
     */
    public void setIncrements(int increments) {
        this.increments = increments;
    }

    private void fireDataChanged(int event, int val) {
        listeners.fireDataChangeEvent(val, event);
    }

    /**
     * Adds a listener to data changed events
     *
     * @param l new listener
     */
    public void addDataChangedListener(DataChangedListener l) {
        listeners.addListener(l);
    }

    /**
     * Removes a listener from data changed events
     *
     * @param l listener to remove
     */
    public void removeDataChangedListener(DataChangedListener l) {
        listeners.removeListener(l);
    }

    /**
     * Indicates that the value of the slider should be rendered with a
     * percentage sign on top of the slider.
     *
     * @return true if so
     */
    public boolean isRenderPercentageOnTop() {
        return renderPercentageOnTop;
    }

    /**
     * Indicates that the percentage value of the slider should be rendered with
     * a percentage sign on top of the slider. For infinite slider this method
     * will have no effect.
     *
     * @param renderPercentageOnTop true to render percentages
     */
    public void setRenderPercentageOnTop(boolean renderPercentageOnTop) {
        if (infinite) {
            this.renderPercentageOnTop = false;
        } else {
            this.renderPercentageOnTop = renderPercentageOnTop;
        }
        refreshText();
        repaint();
    }
    
    /**
     * @inheritDoc
     */
    public void setText(String text) {
        this.text = text;
        super.setText(text);
    }

    /**
     * @return the renderValueOnTop
     */
    public boolean isRenderValueOnTop() {
        return renderValueOnTop;
    }

    /**
     * Indicates that the value of the slider should be rendered with a on top
     * of the slider. For infinite slider this method will have no effect.
     *
     * @param renderValueOnTop true to render value
     */
    public void setRenderValueOnTop(boolean renderValueOnTop) {
        if (infinite) {
            this.renderValueOnTop = false;
        } else {
            this.renderValueOnTop = renderValueOnTop;
        }
        refreshText();
        repaint();
    }

    /**
     * Returns the maximum value set for the slider Default value is 100
     *
     * @return the maxValue
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Indicates the maximum value set for the slider. This method will be
     * ignored for infinite slider.
     *
     * @param maxValue the maxValue to set
     * @throws IllegalArgumentException if maxValue is not in valid integer
     * range
     */
    public void setMaxValue(int maxValue) {
        if (!infinite) {
            if (maxValue <= minValue) {
                throw new IllegalArgumentException("Slider maximum value must be greater than the minimum value.");
            }
            this.maxValue = maxValue;
        }
    }

    /**
     * Returns the maximum value set for the slider Default value is 0
     *
     * @return the minValue
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Indicates the minimum value set for the slider. This method will be
     * ignored for infinite slider.
     *
     * @param minValue the minValue to set
     * @throws IllegalArgumentException if minValue is not in valid integer
     * range
     */
    public void setMinValue(int minValue) {
        if (!infinite) {
            if (minValue >= maxValue) {
                throw new IllegalArgumentException("Slider minimum value must be less than maximum value");
            }
            this.minValue = minValue;
        }
    }

    /**
     * The thumb image is drawn on top of the current progress
     *
     * @return the thumbImage
     */
    public Image getThumbImage() {
        return thumbImage;
    }

    /**
     * The thumb image is drawn on top of the current progress
     *
     * @param thumbImage the thumbImage to set
     */
    public void setThumbImage(Image thumbImage) {
        this.thumbImage = thumbImage;
    }

    /**
     * @inheritDoc
     */
    boolean shouldBlockSideSwipe() {
        return !vertical;
    }
}
