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

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * A Sprite is a basic visual element that can be rendered with one of several frames stored in an Image; different frames can be shown to animate the Sprite. Several transforms such as flipping and rotation can also be applied to a Sprite to further vary its appearance. As with all Layer subclasses, a Sprite's location can be changed and it can also be made visible or invisible.
 * Each frame is assigned a unique index number. The frame located in the upper-left corner of the Image is assigned an index of 0. The remaining frames are then numbered consecutively in row-major order (indices are assigned across the first row, then the second row, and so on). The method getRawFrameCount() returns the total number of raw frames.
 * The developer must manually switch the current frame in the frame sequence. This may be accomplished by calling setFrame(int), prevFrame(), or nextFrame(). Note that these methods always operate on the sequence index, they do not operate on frame indices; however, if the default frame sequence is used, then the sequence indices and the frame indices are interchangeable.
 * If desired, an arbitrary frame sequence may be defined for a Sprite. The frame sequence must contain at least one element, and each element must reference a valid frame index. By defining a new frame sequence, the developer can conveniently display the Sprite's frames in any order desired; frames may be repeated, omitted, shown in reverse order, etc.
 * For example, the diagram below shows how a special frame sequence might be used to animate a mosquito. The frame sequence is designed so that the mosquito flaps its wings three times and then pauses for a moment before the cycle is repeated. By calling nextFrame() each time the display is updated, the resulting animation would like this:
 * Therefore, Sprite includes the concept of a reference pixel. The reference pixel is defined by specifying its location in the Sprite's untransformed frame using defineReferencePixel(x,y). By default, the reference pixel is defined to be the pixel at (0,0) in the frame. If desired, the reference pixel may be defined outside of the frame's bounds.
 * In this example, the reference pixel is defined to be the pixel that the monkey appears to be hanging from:
 * getRefPixelX() and getRefPixelY() can be used to query the location of the reference pixel in the painter's coordinate system. The developer can also use setRefPixelPosition(x,y) to position the Sprite so that reference pixel appears at a specific location in the painter's coordinate system. These methods automatically account for any transforms applied to the Sprite.
 * In this example, the reference pixel's position is set to a point at the end of a tree branch; the Sprite's location changes so that the reference pixel appears at this point and the monkey appears to be hanging from the branch:
 * When a transform is applied, the Sprite is automatically repositioned such that the reference pixel appears stationary in the painter's coordinate system. Thus, the reference pixel effectively becomes the center of the transform operation. Since the reference pixel does not move, the values returned by getRefPixelX() and getRefPixelY() remain the same; however, the values returned by getX() and getY() may change to reflect the movement of the Sprite's upper-left corner.
 * Referring to the monkey example once again, the position of the reference pixel remains at (48, 22) when a 90 degree rotation is applied, thereby making it appear as if the monkey is swinging from the branch:
 * Sprites can be implemented using whatever techniques a manufacturers wishes to use (e.g hardware acceleration may be used for all Sprites, for certain sizes of Sprites, or not at all).
 * For some platforms, certain Sprite sizes may be more efficient than others; manufacturers may choose to provide developers with information about device-specific characteristics such as these.
 * Since: MIDP 2.0
 */
public class Sprite extends Layer{
    /**
     * Causes the Sprite to appear reflected about its vertical center. This constant has a value of 2.
     * See Also:Constant Field Values
     */
    public static final int TRANS_MIRROR=2;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 180 degrees. This constant has a value of 1.
     * See Also:Constant Field Values
     */
    public static final int TRANS_MIRROR_ROT180=1;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 270 degrees. This constant has a value of 4.
     * See Also:Constant Field Values
     */
    public static final int TRANS_MIRROR_ROT270=4;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 90 degrees. This constant has a value of 7.
     * See Also:Constant Field Values
     */
    public static final int TRANS_MIRROR_ROT90=7;

    /**
     * No transform is applied to the Sprite. This constant has a value of 0.
     * See Also:Constant Field Values
     */
    public static final int TRANS_NONE=0;

    /**
     * Causes the Sprite to appear rotated clockwise by 180 degrees. This constant has a value of 3.
     * See Also:Constant Field Values
     */
    public static final int TRANS_ROT180=3;

    /**
     * Causes the Sprite to appear rotated clockwise by 270 degrees. This constant has a value of 6.
     * See Also:Constant Field Values
     */
    public static final int TRANS_ROT270=6;

    /**
     * Causes the Sprite to appear rotated clockwise by 90 degrees. This constant has a value of 5.
     * See Also:Constant Field Values
     */
    public static final int TRANS_ROT90=5;

    /**
     * Creates a new non-animated Sprite using the provided Image. This constructor is functionally equivalent to calling new Sprite(image, image.getWidth(), image.getHeight())
     * By default, the Sprite is visible and its upper-left corner is positioned at (0,0) in the painter's coordinate system.
     * image - the Image to use as the single frame for the Sprite
     * - if img is null
     */
    public Sprite(Image image){
         //TODO codavaj!!
    }

    /**
     * Creates a new animated Sprite using frames contained in the provided Image. The frames must be equally sized, with the dimensions specified by frameWidth and frameHeight. They may be laid out in the image horizontally, vertically, or as a grid. The width of the source image must be an integer multiple of the frame width, and the height of the source image must be an integer multiple of the frame height. The values returned by
     * and
     * will reflect the frame width and frame height subject to the Sprite's current transform.
     * Sprites have a default frame sequence corresponding to the raw frame numbers, starting with frame 0. The frame sequence may be modified with setFrameSequence(int[]).
     * By default, the Sprite is visible and its upper-left corner is positioned at (0,0) in the painter's coordinate system.
     * image - the Image to use for SpriteframeWidth - the width, in pixels, of the individual raw framesframeHeight - the height, in pixels, of the individual raw frames
     * - if img is null
     * - if frameHeight or frameWidth is less than 1
     * - if the image width is not an integer multiple of the frameWidth
     * - if the image height is not an integer multiple of the frameHeight
     */
    public Sprite(Image image, int frameWidth, int frameHeight){
         //TODO codavaj!!
    }

    /**
     * Creates a new Sprite from another Sprite.
     * All instance attributes (raw frames, position, frame sequence, current frame, reference point, collision rectangle, transform, and visibility) of the source Sprite are duplicated in the new Sprite.
     * s - the Sprite to create a copy of
     * - if s is null
     */
    public Sprite(Sprite s){
         //TODO codavaj!!
    }

    /**
     * Checks for a collision between this Sprite and the specified Image with its upper left corner at the specified location. If pixel-level detection is used, a collision is detected only if opaque pixels collide. That is, an opaque pixel in the Sprite would have to collide with an opaque pixel in Image for a collision to be detected. Only those pixels within the Sprite's collision rectangle are checked.
     * If pixel-level detection is not used, this method simply checks if the Sprite's collision rectangle intersects with the Image's bounds.
     * Any transform applied to the Sprite is automatically accounted for.
     * The Sprite must be visible in order for a collision to be detected.
     */
    public final boolean collidesWith(Image image, int x, int y, boolean pixelLevel){
        return false; //TODO codavaj!!
    }

    /**
     * Checks for a collision between this Sprite and the specified Sprite.
     * If pixel-level detection is used, a collision is detected only if opaque pixels collide. That is, an opaque pixel in the first Sprite would have to collide with an opaque pixel in the second Sprite for a collision to be detected. Only those pixels within the Sprites' respective collision rectangles are checked.
     * If pixel-level detection is not used, this method simply checks if the Sprites' collision rectangles intersect.
     * Any transforms applied to the Sprites are automatically accounted for.
     * Both Sprites must be visible in order for a collision to be detected.
     */
    public final boolean collidesWith(Sprite s, boolean pixelLevel){
        return false; //TODO codavaj!!
    }

    /**
     * Checks for a collision between this Sprite and the specified TiledLayer. If pixel-level detection is used, a collision is detected only if opaque pixels collide. That is, an opaque pixel in the Sprite would have to collide with an opaque pixel in TiledLayer for a collision to be detected. Only those pixels within the Sprite's collision rectangle are checked.
     * If pixel-level detection is not used, this method simply checks if the Sprite's collision rectangle intersects with a non-empty cell in the TiledLayer.
     * Any transform applied to the Sprite is automatically accounted for.
     * The Sprite and the TiledLayer must both be visible in order for a collision to be detected.
     */
    public final boolean collidesWith(TiledLayer t, boolean pixelLevel){
        return false; //TODO codavaj!!
    }

    /**
     * Defines the Sprite's bounding rectangle that is used for collision detection purposes. This rectangle is specified relative to the un-transformed Sprite's upper-left corner and defines the area that is checked for collision detection. For pixel-level detection, only those pixels within the collision rectangle are checked. By default, a Sprite's collision rectangle is located at 0,0 as has the same dimensions as the Sprite. The collision rectangle may be specified to be larger or smaller than the default rectangle; if made larger, the pixels outside the bounds of the Sprite are considered to be transparent for pixel-level collision detection.
     */
    public void defineCollisionRectangle(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Defines the reference pixel for this Sprite. The pixel is defined by its location relative to the upper-left corner of the Sprite's un-transformed frame, and it may lay outside of the frame's bounds.
     * When a transformation is applied, the reference pixel is defined relative to the Sprite's initial upper-left corner before transformation. This corner may no longer appear as the upper-left corner in the painter's coordinate system under current transformation.
     * By default, a Sprite's reference pixel is located at (0,0); that is, the pixel in the upper-left corner of the raw frame.
     * Changing the reference pixel does not change the Sprite's physical position in the painter's coordinate system; that is, the values returned by getX() and getY() will not change as a result of defining the reference pixel. However, subsequent calls to methods that involve the reference pixel will be impacted by its new definition.
     */
    public void defineReferencePixel(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Gets the current index in the frame sequence.
     * The index returned refers to the current entry in the frame sequence, not the index of the actual frame that is displayed.
     */
    public final int getFrame(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the number of elements in the frame sequence. The value returned reflects the length of the Sprite's frame sequence; it does not reflect the number of raw frames. However, these two values will be the same if the default frame sequence is used.
     */
    public int getFrameSequenceLength(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the number of raw frames for this Sprite. The value returned reflects the number of frames; it does not reflect the length of the Sprite's frame sequence. However, these two values will be the same if the default frame sequence is used.
     */
    public int getRawFrameCount(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the horizontal position of this Sprite's reference pixel in the painter's coordinate system.
     */
    public int getRefPixelX(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the vertical position of this Sprite's reference pixel in the painter's coordinate system.
     */
    public int getRefPixelY(){
        return 0; //TODO codavaj!!
    }

    /**
     * Selects the next frame in the frame sequence.
     * The frame sequence is considered to be circular, i.e. if nextFrame() is called when at the end of the sequence, this method will advance to the first entry in the sequence.
     */
    public void nextFrame(){
        return; //TODO codavaj!!
    }

    /**
     * Draws the Sprite.
     * Draws current frame of Sprite using the provided Graphics object. The Sprite's upper left corner is rendered at the Sprite's current position relative to the origin of the Graphics object. The current position of the Sprite's upper-left corner can be retrieved by calling Layer.getX() and Layer.getY().
     * Rendering is subject to the clip region of the Graphics object. The Sprite will be drawn only if it is visible.
     * If the Sprite's Image is mutable, the Sprite is rendered using the current contents of the Image.
     */
    public final void paint(Graphics g){
        return; //TODO codavaj!!
    }

    /**
     * Selects the previous frame in the frame sequence.
     * The frame sequence is considered to be circular, i.e. if prevFrame() is called when at the start of the sequence, this method will advance to the last entry in the sequence.
     */
    public void prevFrame(){
        return; //TODO codavaj!!
    }

    /**
     * Selects the current frame in the frame sequence.
     * The current frame is rendered when paint(Graphics) is called.
     * The index provided refers to the desired entry in the frame sequence, not the index of the actual frame itself.
     */
    public void setFrame(int sequenceIndex){
        return; //TODO codavaj!!
    }

    /**
     * Set the frame sequence for this Sprite.
     * All Sprites have a default sequence that displays the Sprites frames in order. This method allows for the creation of an arbitrary sequence using the available frames. The current index in the frame sequence is reset to zero as a result of calling this method.
     * The contents of the sequence array are copied when this method is called; thus, any changes made to the array after this method returns have no effect on the Sprite's frame sequence.
     * Passing in null causes the Sprite to revert to the default frame sequence.
     */
    public void setFrameSequence(int[] sequence){
        return; //TODO codavaj!!
    }

    /**
     * Changes the Image containing the Sprite's frames.
     * Replaces the current raw frames of the Sprite with a new set of raw frames. See the constructor Sprite(Image, int, int) for information on how the frames are created from the image. The values returned by Layer.getWidth() and Layer.getHeight() will reflect the new frame width and frame height subject to the Sprite's current transform.
     * Changing the image for the Sprite could change the number of raw frames. If the new frame set has as many or more raw frames than the previous frame set, then: The current frame will be unchanged If a custom frame sequence has been defined (using setFrameSequence(int[])), it will remain unchanged. If no custom frame sequence is defined (i.e. the default frame sequence is in use), the default frame sequence will be updated to be the default frame sequence for the new frame set. In other words, the new default frame sequence will include all of the frames from the new raw frame set, as if this new image had been used in the constructor.
     * If the new frame set has fewer frames than the previous frame set, then: The current frame will be reset to entry 0 Any custom frame sequence will be discarded and the frame sequence will revert to the default frame sequence for the new frame set.
     * The reference point location is unchanged as a result of calling this method, both in terms of its defined location within the Sprite and its position in the painter's coordinate system. However, if the frame size is changed and the Sprite has been transformed, the position of the Sprite's upper-left corner may change such that the reference point remains stationary.
     * If the Sprite's frame size is changed by this method, the collision rectangle is reset to its default value (i.e. it is set to the new bounds of the untransformed Sprite).
     */
    public void setImage(Image img, int frameWidth, int frameHeight){
        return; //TODO codavaj!!
    }

    /**
     * Sets this Sprite's position such that its reference pixel is located at (x,y) in the painter's coordinate system.
     */
    public void setRefPixelPosition(int x, int y){
        return; //TODO codavaj!!
    }

    /**
     * Sets the transform for this Sprite. Transforms can be applied to a Sprite to change its rendered appearance. Transforms are applied to the original Sprite image; they are not cumulative, nor can they be combined. By default, a Sprite's transform is
     * .
     * Since some transforms involve rotations of 90 or 270 degrees, their use may result in the overall width and height of the Sprite being swapped. As a result, the values returned by Layer.getWidth() and Layer.getHeight() may change.
     * The collision rectangle is also modified by the transform so that it remains static relative to the pixel data of the Sprite. Similarly, the defined reference pixel is unchanged by this method, but its visual location within the Sprite may change as a result.
     * This method repositions the Sprite so that the location of the reference pixel in the painter's coordinate system does not change as a result of changing the transform. Thus, the reference pixel effectively becomes the centerpoint for the transform. Consequently, the values returned by getRefPixelX() and getRefPixelY() will be the same both before and after the transform is applied, but the values returned by getX() and getY() may change.
     */
    public void setTransform(int transform){
        return; //TODO codavaj!!
    }

}
