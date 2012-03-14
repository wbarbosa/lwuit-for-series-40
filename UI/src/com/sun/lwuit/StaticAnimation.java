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

import com.sun.lwuit.animations.Animation;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;


/**
 * An animation with pre-existing 
 *
 * @deprecated this class shouldn't be referenced directly, use the Image base class
 * for all functionality
 * @author Shai Almog
 */
public class StaticAnimation extends IndexedImage implements Animation {
    private Frame[] frames;
    
    /**
     * The current frame doesn't point directly at the frames array since the first frame
     *  is the image itself
     */
    private int currentFrame;
    
    private long animationStartTime;
    private int totalAnimationTime;
    
    private boolean loop;
    
    /**
     * Invoked with the first frame value
     */
    StaticAnimation(int width, int height, int[] palette, byte[] data) {
        super(width, height, palette, data);
    }

    /**
     * Returns the number of frames in the animation including the initial frame
     * 
     * @return number of frames in the animation
     */
    public int getFrameCount() {
        // +1 for this image
        return frames.length + 1;
    }
    
    /**
     * The time in which the given frame should appear
     * 
     * @param frame must be a number bigger than -1 and smaller than getFrameCount()
     * @return the time in milliseconds for the frame to appear
     */
    public int getFrameTime(int frame) {
        if(frame == 0) {
            return 0;
        }
        return frames[frame - 1].time;
    }

    /**
     * Returns the duration for the entire animation used to determine when to loop
     * @return total animation time in milliseconds
     */
    public int getTotalAnimationTime() {
        return totalAnimationTime;
    }
    
    /**
     * Returns the RGB for the given frame, this method is relatively slow and 
     * it is recommended that you do not use it for realtime animations.
     * 
     * @param frame must be a number bigger than -1 and smaller than getFrameCount()
     * @return ARGB pixels within the given frame
     */
    public int[] getFrameRGB(int frame) {
        if(frame == 0) {
            return getRGBCached();
        }
        Frame f = frames[frame - 1];
        int height = getHeight();
        int width = getWidth();
        int[] array = new int[width * height];
        byte[] imageDataByte = f.getKeyFrame();
        int[] palette = getPalette();
        
        for(int line = 0 ; line < height ; line++) {
            byte[] lineArray = f.getModifiedRow(line);
            if(lineArray != null) {
                for(int position = 0 ; position < width ; position++) {
                    int i = lineArray[position] & 0xff;                
                    array[position + (width * line)] = palette[i];
                }
            } else {
                int currentPos = line * width;
                for(int position = 0 ; position < width ; position++) {
                    int i = imageDataByte[position + currentPos] & 0xff;                
                    array[position + (width * line)] = palette[i];
                }
            }
        }
        return array;
    }
    
    /**
     * Creates an animation from the given stream, this method is used internally
     * by Resources normally you should not create static animations manually.
     * 
     * @param data input stream from which the animation is loaded
     * @return An instance of a static animation
     * @throws IOException when the stream throws it
     */
    public static StaticAnimation createAnimation(DataInputStream data) throws IOException {
        // read the length of the palette;
        byte pSize = data.readByte();
        int[] palette = new int[pSize & 0xff];
        for(int iter = 0 ; iter < palette.length ; iter++) {
            palette[iter] = data.readInt();
        }
        int width = data.readShort();
        int height = data.readShort();
        int numberOfFrames = data.readByte() & 0xff;
        int totalAnimationTime = Math.max(1, data.readInt());
        boolean loop = data.readBoolean();
        byte[] array = new byte[width * height];
        data.readFully(array);
        
        // create the first frame of the animation
        StaticAnimation animation = new StaticAnimation(width, height, palette, array);
        animation.frames = new Frame[numberOfFrames -1];
        animation.totalAnimationTime = totalAnimationTime;
        animation.loop = loop;
        int currentTime = 0;
        byte[] lastKeyframe = array;
        Vector rowNumbers = new Vector();
        Vector rowValues = new Vector();
        
        // read the rest of the frames in the animation
        for(int iter = 1 ; iter < numberOfFrames ; iter++) {
            currentTime += data.readInt();
            
            // if this is a keyframe then just read the data else read the specific
            // modified row offsets
            if(data.readBoolean()) {
                array = new byte[width * height];
                data.readFully(array);
                animation.frames[iter - 1] = new Frame(currentTime, array);
                lastKeyframe = array;
                rowValues.removeAllElements();
                rowNumbers.removeAllElements();
            } else {
                boolean drawPrevious = data.readBoolean();
                int nextRow = data.readShort();

                while(nextRow != -1) {
                    byte[] rowData = new byte[width];
                    data.readFully(rowData);

                    // if this row was already modified in a previous frame we can remove
                    // the old row
                    Integer rowInteger = new Integer(nextRow);
                    int index = rowNumbers.indexOf(rowInteger);
                    if(index > -1) {
                        rowNumbers.removeElementAt(index);
                        rowValues.removeElementAt(index);
                    }
                    
                    rowNumbers.addElement(rowInteger);
                    rowValues.addElement(rowData);
                    nextRow = data.readShort();
                }
                animation.frames[iter - 1] = new Frame(currentTime, lastKeyframe, rowNumbers, rowValues, drawPrevious);
            }
        }
        
        return animation;
    }
    
    /**
     * @inheritDoc
     */
    public boolean animate() {
        if(animationStartTime == 0) {
            animationStartTime = System.currentTimeMillis();
            return false;
        }
        long currentTime = System.currentTimeMillis();
        int position = (int)(currentTime - animationStartTime);
        if(loop) {
            position %= totalAnimationTime;
        } else {
            if(position > totalAnimationTime) {
                return false;
            }
        }
        
        // special case for last frame
        if(currentFrame == frames.length) {
            if(position >= totalAnimationTime || position < frames[frames.length - 1].getTime()) {
                currentFrame = 0;
                return true;
            }
        } else {
            if(position >= frames[currentFrame].getTime()) {
                currentFrame++;
                if(currentFrame > frames.length + 1) {
                    currentFrame = 0;
                }
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Restarts the animation
     */
    public void restart() {
        animationStartTime = System.currentTimeMillis();
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        drawImage(g, g.getGraphics(), 0, 0);
    }

    /**
     * Indicates whether the animation will run in a loop or run only once
     * 
     * @return true for a loop animation
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Indicates whether the animation will run in a loop or run only once
     * 
     * @param loop true for looping the animation
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    /**
     * @inheritDoc
     */
    protected void drawImage(Graphics g, Object nativeGraphics, int x, int y) {
        if(currentFrame == 0) {
            super.drawImage(g, nativeGraphics, x, y);
            return;
        }
        int width = getWidth();
        
        if(lineCache == null || lineCache.length < width) {
            lineCache = new int[width];
        }
        
        // for performance we can calculate the visible drawing area so we don't have to
        // calculate the whole array
        int clipY = g.getClipY();
        int clipBottomY = g.getClipHeight() + clipY;
        int firstLine = 0;
        int lastLine = getHeight();
        if(clipY > y) {
            firstLine = clipY - y;
        } 
        if(clipBottomY < y + getHeight()) {
            lastLine = clipBottomY - y;
        }
        
        Frame f = frames[currentFrame - 1];
        byte[] imageDataByte = f.getKeyFrame();
        int[] palette = getPalette();
        
        // we draw based on the last keyframe and if a row was modified then we
        // will draw that row rather than the keyframe
        for(int line = firstLine ; line < lastLine ; line += 1) {
            byte[] lineArray = f.getModifiedRow(line);
            if(lineArray != null) {
                for(int position = 0 ; position < width ; position++) {
                    int i = lineArray[position] & 0xff;                
                    lineCache[position] = palette[i];
                }
            } else {
                //if(!f.isDrawPrevious()) {
                    int currentPos = line * width;
                    for(int position = 0 ; position < width ; position++) {
                        int i = imageDataByte[position + currentPos] & 0xff;                
                        lineCache[position] = palette[i];
                    }
                //}
            }
            g.drawRGB(lineCache, 0, x, y + line, width, 1, true);
        }
    }    
    
    /**
     * @inheritDoc
     */
    public void scale(int width, int height) {
        StaticAnimation s = (StaticAnimation)scaled(width, height);
        super.scale(width, height);
        frames = s.frames;
    }
    
    /**
     * @inheritDoc
     */
    public Image scaled(int width, int height) {
        int srcWidth = getWidth();
        int srcHeight = getHeight();

        // no need to scale
        if(srcWidth == width && srcHeight == height){
            return this;
        }

        // save the previous keyframe all the time this allows us to swap a keyframe
        // based on its pointer rather than scale it twice
        byte[] lastKeyFrame = getImageDataByte();

        // scale the first frame
        StaticAnimation result = new StaticAnimation(width, height, getPalette(), scaleArray(lastKeyFrame, width, height));
        result.loop = loop;
        result.totalAnimationTime = totalAnimationTime;
        result.animationStartTime = animationStartTime;
        result.currentFrame = currentFrame;
        result.frames = new Frame[frames.length];
        
        byte[] lastKeyFrameAfterScale = result.getImageDataByte();
        
        int yRatio = (srcHeight << 16) / height;
        int xRatio = (srcWidth << 16) / width;

        // now we need to traverse all the frames and scale them one by one
        for(int iter = 0 ; iter < frames.length ; iter++) {
            byte[] currentKeyFrame = frames[iter].getKeyFrame();
            if(currentKeyFrame != lastKeyFrame) {
                lastKeyFrame = currentKeyFrame;
                currentKeyFrame = scaleArray(currentKeyFrame, width, height);
                lastKeyFrameAfterScale = currentKeyFrame;
            }
            result.frames[iter] = new Frame(frames[iter].getTime(), lastKeyFrameAfterScale, frames[iter], xRatio, yRatio, width, height);
        }
        return result;
    }
    
    /**
     * @inheritDoc
     */
    public boolean isAnimation() {
        return true;
    }
    
    /**
     * Used by the resource editor
     */
    boolean isKeyframe(int offset) {
        if(offset == 0) {
            return true;
        }
        if(offset == 1) {
            return frames[offset - 1].getKeyFrame() != imageDataByte;
        }
        return frames[offset - 1].getKeyFrame() != frames[offset - 2].getKeyFrame();
    }
    
    /**
     * Used by the resource editor
     */
    byte[] getKeyframe(int offset) {
        return frames[offset - 1].getKeyFrame();
    }

    /**
     * Used by the resource editor
     */
    boolean isDrawPrevious(int offset) {
        return frames[offset - 1].isDrawPrevious();
    }
    
    /**
     * Used by the resource editor
     */
    byte[][] getModifiedRows(int offset) {
        return frames[offset - 1].modifiedRows;
    }
    
    /**
     * Used by the resource editor
     */
    int[] getModifiedRowOffsets(int offset) {
        return frames[offset - 1].modifiedRowOffsets;
    }
    
    Rectangle getDirtyRegion(){
        int frame = currentFrame;
        if(frame == 0){
            return null;
        }
        if(frame == frames.length){
            frame = 0;
        }
        Rectangle rect = new Rectangle(0, frames[frame].smallestChangedRow, new Dimension(getWidth(), frames[frame].highestChangedRow -frames[frame].smallestChangedRow));
        return rect;
    }
    
    /**
     * Represents a frame within the animation that is not the first frame
     */
    static class Frame {
        /**
         * Offset since the beginning of the animation
         */
        private int time;
        
        private byte[] keyFrame;
        
        /**
         * Relevant only for standard frames, this represents the rows that
         * were modified for this specific frame
         */
        byte[][] modifiedRows;
        int[] modifiedRowOffsets;
        private boolean drawPrevious;
        int smallestChangedRow;
        int highestChangedRow;
        
        
        public Frame(int time, byte[] keyFrame, Vector rowNumbers, Vector rowValues, boolean drawPrevious) {
            this.time = time;
            this.keyFrame = keyFrame;
            this.drawPrevious = drawPrevious;
            initArrays(rowNumbers, rowValues);
        }

        private void initArrays(Vector rowNumbers, Vector rowValues) {
            modifiedRowOffsets = new int[rowNumbers.size()];
            modifiedRows = new byte[modifiedRowOffsets.length][];
            for(int iter = 0 ; iter < modifiedRowOffsets.length ; iter++) {
                modifiedRowOffsets[iter] = ((Integer)rowNumbers.elementAt(iter)).intValue();
                modifiedRows[iter] = (byte[])rowValues.elementAt(iter);
            }
            smallestChangedRow = modifiedRowOffsets[0];
            highestChangedRow = modifiedRowOffsets[0];
            for(int i=1;i<modifiedRowOffsets.length;i++){
                smallestChangedRow = Math.min(smallestChangedRow, modifiedRowOffsets[i]) ;
                highestChangedRow = Math.max(highestChangedRow, modifiedRowOffsets[i]) ;
            }
        }
        
        public Frame(int time, byte[] keyFrame) {
            this.time = time;
            this.keyFrame = keyFrame;
            modifiedRowOffsets = new int[0];
        }
        

        /**
         * This constructor is used for scaling the original frame according to the given ratios
         */
        public Frame(int time, byte[] keyFrame, Frame original, int xRatio, int yRatio, int width, int height) {
            this.time = time;
            this.keyFrame = keyFrame;
            
            // if the original was a keyframe then no work...
            if(original.modifiedRowOffsets.length == 0) {
                modifiedRowOffsets = original.modifiedRowOffsets;
                return;
            }
            Vector newRows = new Vector();
            Vector newValues = new Vector();
            
            int xPos = xRatio / 2;
            int yPos = yRatio / 2;

            for(int y = 0 ; y < height ; y++) {
                int srcY = yPos >> 16;
                
                // do we have the row at srcY???
                int rowAtY = -1;
                for(int iter = 0 ; iter < original.modifiedRowOffsets.length ; iter++) {
                    if(original.modifiedRowOffsets[iter] == srcY) {
                        rowAtY = iter;
                        break;
                    }
                }
                if(rowAtY != -1) {
                    byte[] newRow = new byte[width];
                    for (int x = 0; x < width; x++) {
                        int srcX = xPos >> 16;
                        newRow[x] = original.modifiedRows[rowAtY][srcX];
                        xPos += xRatio;
                    }
                    newRows.addElement(new Integer(y));
                    newValues.addElement(newRow);
                }
                yPos += yRatio;
                xPos = xRatio / 2;
            }
            
            initArrays(newRows, newValues);
        }
        
        public boolean isDrawPrevious() {
            return drawPrevious;
        }
        
        public int getTime() {
            return time;
        }
        
        private byte[] getModifiedRow(int row) {
            for(int iter = 0 ; iter < modifiedRowOffsets.length ; iter++) {
                if(modifiedRowOffsets[iter] == row) {
                    return modifiedRows[iter];
                }
            }
            return null;
        }
        
        private byte[] getKeyFrame() {
            return keyFrame;
        }
        
        private void setKeyFrame(byte[] keyFrame) {
            this.keyFrame = keyFrame;
        }
    }
}
