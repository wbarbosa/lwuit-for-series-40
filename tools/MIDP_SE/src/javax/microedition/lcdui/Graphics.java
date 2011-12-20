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
 * Provides simple 2D geometric rendering capability.
 * Drawing primitives are provided for text, images, lines, rectangles, and arcs. Rectangles and arcs may also be filled with a solid color. Rectangles may also be specified with rounded corners.
 * A 24-bit color model is provided, with 8 bits for each of red, green, and blue components of a color. Not all devices support a full 24 bits' worth of color and thus they will map colors requested by the application into colors available on the device. Facilities are provided in the Display class for obtaining device characteristics, such as whether color is available and how many distinct gray levels are available. Applications may also use getDisplayColor() to obtain the actual color that would be displayed for a requested color. This enables applications to adapt their behavior to a device without compromising device independence.
 * For all rendering operations, source pixels are always combined with destination pixels using the Source Over Destination rule [Porter-Duff]. Other schemes for combining source pixels with destination pixels, such as raster-ops, are not provided.
 * For the text, line, rectangle, and arc drawing and filling primitives, the source pixel is a pixel representing the current color of the graphics object being used for rendering. This pixel is always considered to be fully opaque. With source pixel that is always fully opaque, the Source Over Destination rule has the effect of pixel replacement, where destination pixels are simply replaced with the source pixel from the graphics object.
 * The drawImage() and drawRegion() methods use an image as the source for rendering operations instead of the current color of the graphics object. In this context, the Source Over Destination rule has the following properties: a fully opaque pixel in the source must replace the destination pixel, a fully transparent pixel in the source must leave the destination pixel unchanged, and a semitransparent pixel in the source must be alpha blended with the destination pixel. Alpha blending of semitransparent pixels is required. If an implementation does not support alpha blending, it must remove all semitransparency from image source data at the time the image is created. See Alpha Processing for further discussion.
 * The destinations of all graphics rendering are considered to consist entirely of fully opaque pixels. A property of the Source Over Destination rule is that compositing any pixel with a fully opaque destination pixel always results in a fully opaque destination pixel. This has the effect of confining full and partial transparency to immutable images, which may only be used as the source for rendering operations.
 * Graphics may be rendered directly to the display or to an off-screen image buffer. The destination of rendered graphics depends on the provenance of the graphics object. A graphics object for rendering to the display is passed to the Canvas object's paint() method. This is the only means by which a graphics object may be obtained whose destination is the display. Furthermore, applications may draw using this graphics object only for the duration of the paint() method.
 * A graphics object for rendering to an off-screen image buffer may be obtained by calling the getGraphics() method on the desired image. A graphics object so obtained may be held indefinitely by the application, and requests may be issued on this graphics object at any time.
 * The default coordinate system's origin is at the upper left-hand corner of the destination. The X-axis direction is positive towards the right, and the Y-axis direction is positive downwards. Applications may assume that horizontal and vertical distances in the coordinate system represent equal distances on the actual device display, that is, pixels are square. A facility is provided for translating the origin of the coordinate system. All coordinates are specified as integers.
 * The coordinate system represents locations between pixels, not the pixels themselves. Therefore, the first pixel in the upper left corner of the display lies in the square bounded by coordinates (0,0) , (1,0) , (0,1) , (1,1).
 * Under this definition, the semantics for fill operations are clear. Since coordinate grid lines lie between pixels, fill operations affect pixels that lie entirely within the region bounded by the coordinates of the operation. For example, the operation
 * paints exactly six pixels. (In this example, and in all subsequent examples, the variable g is assumed to contain a reference to a Graphics object.)
 * Each character of a font contains a set of pixels that forms the shape of the character. When a character is painted, the pixels forming the character's shape are filled with the Graphics object's current color, and the pixels not part of the character's shape are left untouched. The text drawing calls drawChar(), drawChars(), drawString(), and drawSubstring() all draw text in this manner.
 * Lines, arcs, rectangles, and rounded rectangles may be drawn with either a SOLID or a DOTTED stroke style, as set by the setStrokeStyle() method. The stroke style does not affect fill, text, and image operations.
 * For the SOLID stroke style, drawing operations are performed with a one-pixel wide pen that fills the pixel immediately below and to the right of the specified coordinate. Drawn lines touch pixels at both endpoints. Thus, the operation
 * paints exactly one pixel, the first pixel in the upper left corner of the display.
 * Drawing operations under the DOTTED stroke style will touch a subset of pixels that would have been touched under the SOLID stroke style. The frequency and length of dots is implementation-dependent. The endpoints of lines and arcs are not guaranteed to be drawn, nor are the corner points of rectangles guaranteed to be drawn. Dots are drawn by painting with the current color; spaces between dots are left untouched.
 * An artifact of the coordinate system is that the area affected by a fill operation differs slightly from the area affected by a draw operation given the same coordinates. For example, consider the operations
 * Statement (1) fills a rectangle w pixels wide and h pixels high. Statement (2) draws a rectangle whose left and top edges are within the area filled by statement (1). However, the bottom and right edges lie one pixel outside the filled area. This is counterintuitive, but it preserves the invariant that
 * has an effect identical to statement (2) above.
 * The exact pixels painted by drawLine() and drawArc() are not specified. Pixels touched by a fill operation must either exactly overlap or directly abut pixels touched by the corresponding draw operation. A fill operation must never leave a gap between the filled area and the pixels touched by the corresponding draw operation, nor may the fill operation touch pixels outside the area bounded by the corresponding draw operation.
 * The clip is the set of pixels in the destination of the Graphics object that may be modified by graphics rendering operations.
 * There is a single clip per Graphics object. The only pixels modified by graphics operations are those that lie within the clip. Pixels outside the clip are not modified by any graphics operations.
 * Operations are provided for intersecting the current clip with a given rectangle and for setting the current clip outright. The application may specify the clip by supplying a clip rectangle using coordinates relative to the current coordinate system.
 * It is legal to specify a clip rectangle whose width or height is zero or negative. In this case the clip is considered to be empty, that is, no pixels are contained within it. Therefore, if any graphics operations are issued under such a clip, no pixels will be modified.
 * It is legal to specify a clip rectangle that extends beyond or resides entirely beyond the bounds of the destination. No pixels exist outside the bounds of the destination, and the area of the clip rectangle that is outside the destination is ignored. Only the pixels that lie both within the destination and within the specified clip rectangle are considered to be part of the clip.
 * Operations on the coordinate system, such as translate(), do not modify the clip. The methods getClipX(), getClipY(), getClipWidth() and getClipHeight() must return a rectangle that, if passed to setClip without an intervening change to the Graphics object's coordinate system, must result in the identical set of pixels in the clip. The rectangle returned from the getClip family of methods may differ from the clip rectangle that was requested in setClip(). This can occur if the coordinate system has been changed or if the implementation has chosen to intersect the clip rectangle with the bounds of the destination of the Graphics object.
 * If a graphics operation is affected by the clip, the pixels touched by that operation must be the same ones that would be touched as if the clip did not affect the operation. For example, consider a clip represented by the rectangle (cx, cy, cw, ch) and a point (x1, y1) that lies outside this rectangle and a point (x2, y2) that lies within this rectangle. In the following code fragment,
 * The pixels touched by statement (4) must be identical to the pixels within (cx, cy, cw, ch) touched by statement (3).
 * The drawing of text is based on anchor points. Anchor points are used to minimize the amount of computation required when placing text. For example, in order to center a piece of text, an application needs to call stringWidth() or charWidth() to get the width and then perform a combination of subtraction and division to compute the proper location. The method to draw text is defined as follows: public void drawString(String text, int x, int y, int anchor); This method draws text in the current color, using the current font with its anchor point at (x,y). The definition of the anchor point must be one of the horizontal constants (LEFT, HCENTER, RIGHT) combined with one of the vertical constants (TOP, BASELINE, BOTTOM) using the bit-wise OR operator. Zero may also be used as the value of an anchor point. Using zero for the anchor point value gives results identical to using TOP | LEFT.
 * Vertical centering of the text is not specified since it is not considered useful, it is hard to specify, and it is burdensome to implement. Thus, the VCENTER value is not allowed in the anchor point parameter of text drawing calls.
 * The actual position of the bounding box of the text relative to the (x, y) location is determined by the anchor point. These anchor points occur at named locations along the outer edge of the bounding box. Thus, if f is g's current font (as returned by g.getFont(), the following calls will all have identical results:
 * For text drawing, the inter-character and inter-line spacing (leading) specified by the font designer are included as part of the values returned in the stringWidth() and getHeight() calls of class Font. For example, given the following code:
 * Code fragments (5) and (6) behave similarly if not identically. This occurs because f.stringWidth() includes the inter-character spacing. The exact spacing of may differ between these calls if the system supports font kerning.
 * Similarly, reasonable vertical spacing may be achieved simply by adding the font height to the Y-position of subsequent lines. For example:
 * draws string1 and string2 on separate lines with an appropriate amount of inter-line spacing.
 * The stringWidth() of the string and the fontHeight() of the font in which it is drawn define the size of the bounding box of a piece of text. As described above, this box includes inter-line and inter-character spacing. The implementation is required to put this space below and to right of the pixels actually belonging to the characters drawn. Applications that wish to position graphics closely with respect to text (for example, to paint a rectangle around a string of text) may assume that there is space below and to the right of a string and that there is no space above and to the left of the string.
 * Anchor points are also used for positioning of images. Similar to text drawing, the anchor point for an image specifies the point on the bounding rectangle of the destination that is to positioned at the (x,y) location given in the graphics request. Unlike text, vertical centering of images is well-defined, and thus the VCENTER value may be used within the anchor point parameter of image drawing requests. Because images have no notion of a baseline, the BASELINE value may not be used within the anchor point parameter of image drawing requests.
 * Since: MIDP 1.0
 */
public class Graphics{
    /**
     * Constant for positioning the anchor point at the baseline of text.
     * Value 64 is assigned to BASELINE.
     * See Also:Constant Field Values
     */
    public static final int BASELINE=64;

    /**
     * Constant for positioning the anchor point of text and images below the text or image.
     * Value 32 is assigned to BOTTOM.
     * See Also:Constant Field Values
     */
    public static final int BOTTOM=32;

    /**
     * Constant for the DOTTED stroke style.
     * Value 1 is assigned to DOTTED.
     * See Also:Constant Field Values
     */
    public static final int DOTTED=1;

    /**
     * Constant for centering text and images horizontally around the anchor point
     * Value 1 is assigned to HCENTER.
     * See Also:Constant Field Values
     */
    public static final int HCENTER=1;

    /**
     * Constant for positioning the anchor point of text and images to the left of the text or image.
     * Value 4 is assigned to LEFT.
     * See Also:Constant Field Values
     */
    public static final int LEFT=4;

    /**
     * Constant for positioning the anchor point of text and images to the right of the text or image.
     * Value 8 is assigned to RIGHT.
     * See Also:Constant Field Values
     */
    public static final int RIGHT=8;

    /**
     * Constant for the SOLID stroke style.
     * Value 0 is assigned to SOLID.
     * See Also:Constant Field Values
     */
    public static final int SOLID=0;

    /**
     * Constant for positioning the anchor point of text and images above the text or image.
     * Value 16 is assigned to TOP.
     * See Also:Constant Field Values
     */
    public static final int TOP=16;

    /**
     * Constant for centering images vertically around the anchor point.
     * Value 2 is assigned to VCENTER.
     * See Also:Constant Field Values
     */
    public static final int VCENTER=2;

    /**
     * Intersects the current clip with the specified rectangle. The resulting clipping area is the intersection of the current clipping area and the specified rectangle. This method can only be used to make the current clip smaller. To set the current clip larger, use the setClip method. Rendering operations have no effect outside of the clipping area.
     */
    public void clipRect(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Copies the contents of a rectangular area (x_src, y_src, width, height) to a destination area, whose anchor point identified by anchor is located at (x_dest, y_dest). The effect must be that the destination area contains an exact copy of the contents of the source area immediately prior to the invocation of this method. This result must occur even if the source and destination areas overlap.
     * The points (x_src, y_src) and (x_dest, y_dest) are both specified relative to the coordinate system of the Graphics object. It is illegal for the source region to extend beyond the bounds of the graphic object. This requires that:
     * where tx and ty represent the X and Y coordinates of the translated origin of this graphics object, as returned by getTranslateX() and getTranslateY(), respectively.
     * However, it is legal for the destination area to extend beyond the bounds of the Graphics object. Pixels outside of the bounds of the Graphics object will not be drawn.
     * The copyArea method is allowed on all Graphics objects except those whose destination is the actual display device. This restriction is necessary because allowing a copyArea method on the display would adversely impact certain techniques for implementing double-buffering.
     * Like other graphics operations, the copyArea method uses the Source Over Destination rule for combining pixels. However, since it is defined only for mutable images, which can contain only fully opaque pixels, this is effectively the same as pixel replacement.
     */
    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Draws the outline of a circular or elliptical arc covering the specified rectangle, using the current color and stroke style.
     * The resulting arc begins at startAngle and extends for arcAngle degrees, using the current color. Angles are interpreted such that 0degrees is at the 3o'clock position. A positive value indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is (x,y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width+1 pixels wide by height+1 pixels tall. If either width or height is less than zero, nothing is drawn.
     * The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
     */
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle){
        return; //TODO codavaj!!
    }

    /**
     * Draws the specified character using the current font and color.
     */
    public void drawChar(char character, int x, int y, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Draws the specified characters using the current font and color.
     * The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = data.length.
     */
    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Draws the specified image by using the anchor point. The image can be drawn in different positions relative to the anchor point by passing the appropriate position constants. See
     * .
     * If the source image contains transparent pixels, the corresponding pixels in the destination image must be left untouched. If the source image contains partially transparent pixels, a compositing operation must be performed with the destination pixels, leaving all pixels of the destination image fully opaque.
     * If img is the same as the destination of this Graphics object, the result is undefined. For copying areas within an Image, copyArea should be used instead.
     */
    public void drawImage(Image img, int x, int y, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Draws a line between the coordinates (x1,y1) and (x2,y2) using the current color and stroke style.
     */
    public void drawLine(int x1, int y1, int x2, int y2){
        return; //TODO codavaj!!
    }

    /**
     * Draws the outline of the specified rectangle using the current color and stroke style. The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. If either width or height is less than zero, nothing is drawn.
     */
    public void drawRect(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Copies a region of the specified source image to a location within the destination, possibly transforming (rotating and reflecting) the image data using the chosen transform function.
     * The destination, if it is an image, must not be the same image as the source image. If it is, an exception is thrown. This restriction is present in order to avoid ill-defined behaviors that might occur if overlapped, transformed copies were permitted.
     * The transform function used must be one of the following, as defined in the Sprite class: Sprite.TRANS_NONE - causes the specified image region to be copied unchanged Sprite.TRANS_ROT90 - causes the specified image region to be rotated clockwise by 90 degrees. Sprite.TRANS_ROT180 - causes the specified image region to be rotated clockwise by 180 degrees. Sprite.TRANS_ROT270 - causes the specified image region to be rotated clockwise by 270 degrees. Sprite.TRANS_MIRROR - causes the specified image region to be reflected about its vertical center. Sprite.TRANS_MIRROR_ROT90 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 90 degrees. Sprite.TRANS_MIRROR_ROT180 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 180 degrees. Sprite.TRANS_MIRROR_ROT270 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 270 degrees.
     * If the source region contains transparent pixels, the corresponding pixels in the destination region must be left untouched. If the source region contains partially transparent pixels, a compositing operation must be performed with the destination pixels, leaving all pixels of the destination region fully opaque.
     * The (x_src, y_src) coordinates are relative to the upper left corner of the source image. The x_src, y_src, width, and height parameters specify a rectangular region of the source image. It is illegal for this region to extend beyond the bounds of the source image. This requires that:
     * The (x_dest, y_dest) coordinates are relative to the coordinate system of this Graphics object. It is legal for the destination area to extend beyond the bounds of the Graphics object. Pixels outside of the bounds of the Graphics object will not be drawn.
     * The transform is applied to the image data from the region of the source image, and the result is rendered with its anchor point positioned at location (x_dest, y_dest) in the destination.
     */
    public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Renders a series of device-independent RGB+transparency values in a specified region. The values are stored in rgbData in a format with 24 bits of RGB and an eight-bit alpha value (0xAARRGGBB), with the first value stored at the specified offset. The scanlength specifies the relative offset within the array between the corresponding pixels of consecutive rows. Any value for scanlength is acceptable (even negative values) provided that all resulting references are within the bounds of the rgbData array. The ARGB data is rasterized horizontally from left to right within each row. The ARGB values are rendered in the region specified by x, y, width and height, and the operation is subject to the current clip region and translation for this Graphics object.
     * Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, where rows and columns are numbered downward from the top starting at zero, and columns are numbered rightward from the left starting at zero. This operation can then be defined as:
     * for
     * This capability is provided in the Graphics class so that it can be used to render both to the screen and to offscreen Image objects. The ability to retrieve ARGB values is provided by the Image.getRGB(int[], int, int, int, int, int, int) method.
     * If processAlpha is true, the high-order byte of the ARGB format specifies opacity; that is, 0x00RRGGBB specifies a fully transparent pixel and 0xFFRRGGBB specifies a fully opaque pixel. Intermediate alpha values specify semitransparency. If the implementation does not support alpha blending for image rendering operations, it must remove any semitransparency from the source data prior to performing any rendering. (See Alpha Processing for further discussion.) If processAlpha is false, the alpha values are ignored and all pixels must be treated as completely opaque.
     * The mapping from ARGB values to the device-dependent pixels is platform-specific and may require significant computation.
     */
    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha){
        return; //TODO codavaj!!
    }

    /**
     * Draws the outline of the specified rounded corner rectangle using the current color and stroke style. The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. If either width or height is less than zero, nothing is drawn.
     */
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight){
        return; //TODO codavaj!!
    }

    /**
     * Draws the specified String using the current font and color. The x,y position is the position of the anchor point. See
     * .
     */
    public void drawString(java.lang.String str, int x, int y, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Draws the specified String using the current font and color. The x,y position is the position of the anchor point. See
     * .
     * The offset and len parameters must specify a valid range of characters within the string str. The offset parameter must be within the range [0..(str.length())], inclusive. The len parameter must be a non-negative integer such that (offset + len) = str.length().
     */
    public void drawSubstring(java.lang.String str, int offset, int len, int x, int y, int anchor){
        return; //TODO codavaj!!
    }

    /**
     * Fills a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees. Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is (x,y) and whose size is specified by the width and height arguments.
     * If either width or height is zero or less, nothing is drawn.
     * The filled region consists of the pie wedge region bounded by the arc segment as if drawn by drawArc(), the radius extending from the center to this arc at startAngle degrees, and radius extending from the center to this arc at startAngle + arcAngle degrees.
     * The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
     */
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle){
        return; //TODO codavaj!!
    }

    /**
     * Fills the specified rectangle with the current color. If either width or height is zero or less, nothing is drawn.
     */
    public void fillRect(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Fills the specified rounded corner rectangle with the current color. If either width or height is zero or less, nothing is drawn.
     */
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight){
        return; //TODO codavaj!!
    }

    /**
     * Fills the specified triangle will the current color. The lines connecting each pair of points are included in the filled triangle.
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3){
        return; //TODO codavaj!!
    }

    /**
     * Gets the blue component of the current color.
     */
    public int getBlueComponent(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the height of the current clipping area.
     */
    public int getClipHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the width of the current clipping area.
     */
    public int getClipWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the X offset of the current clipping area, relative to the coordinate system origin of this graphics context. Separating the getClip operation into two methods returning integers is more performance and memory efficient than one getClip() call returning an object.
     */
    public int getClipX(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the Y offset of the current clipping area, relative to the coordinate system origin of this graphics context. Separating the getClip operation into two methods returning integers is more performance and memory efficient than one getClip() call returning an object.
     */
    public int getClipY(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current color.
     */
    public int getColor(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the color that will be displayed if the specified color is requested. This method enables the developer to check the manner in which RGB values are mapped to the set of distinct colors that the device can actually display. For example, with a monochrome device, this method will return either 0xFFFFFF (white) or 0x000000 (black) depending on the brightness of the specified color.
     */
    public int getDisplayColor(int color){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current font.
     */
    public Font getFont(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the current grayscale value of the color being used for rendering operations. If the color was set by setGrayScale(), that value is simply returned. If the color was set by one of the methods that allows setting of the red, green, and blue components, the value returned is computed from the RGB color components (possibly in a device-specific fashion) that best approximates the brightness of that color.
     */
    public int getGrayScale(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the green component of the current color.
     */
    public int getGreenComponent(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the red component of the current color.
     */
    public int getRedComponent(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the stroke style used for drawing operations.
     */
    public int getStrokeStyle(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the X coordinate of the translated origin of this graphics context.
     */
    public int getTranslateX(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the Y coordinate of the translated origin of this graphics context.
     */
    public int getTranslateY(){
        return 0; //TODO codavaj!!
    }

    /**
     * Sets the current clip to the rectangle specified by the given coordinates. Rendering operations have no effect outside of the clipping area.
     */
    public void setClip(int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Sets the current color to the specified RGB values. All subsequent rendering operations will use this specified color. The RGB value passed in is interpreted with the least significant eight bits giving the blue component, the next eight more significant bits giving the green component, and the next eight more significant bits giving the red component. That is to say, the color component is specified in the form of 0x00RRGGBB. The high order byte of this value is ignored.
     */
    public void setColor(int RGB){
        return; //TODO codavaj!!
    }

    /**
     * Sets the current color to the specified RGB values. All subsequent rendering operations will use this specified color.
     */
    public void setColor(int red, int green, int blue){
        return; //TODO codavaj!!
    }

    /**
     * Sets the font for all subsequent text rendering operations. If font is null, it is equivalent to setFont(Font.getDefaultFont()).
     */
    public void setFont(Font font){
        return; //TODO codavaj!!
    }

    /**
     * Sets the current grayscale to be used for all subsequent rendering operations. For monochrome displays, the behavior is clear. For color displays, this sets the color for all subsequent drawing operations to be a gray color equivalent to the value passed in. The value must be in the range 0-255.
     */
    public void setGrayScale(int value){
        return; //TODO codavaj!!
    }

    /**
     * Sets the stroke style used for drawing lines, arcs, rectangles, and rounded rectangles. This does not affect fill, text, and image operations.
     */
    public void setStrokeStyle(int style){
        return; //TODO codavaj!!
    }

    /**
     * Translates the origin of the graphics context to the point (x, y) in the current coordinate system. All coordinates used in subsequent rendering operations on this graphics context will be relative to this new origin.
     * The effect of calls to translate() are cumulative. For example, calling translate(1, 2) and then translate(3, 4) results in a translation of (4, 6).
     * The application can set an absolute origin (ax, ay) using the following technique:
     * g.translate(ax - g.getTranslateX(), ay - g.getTranslateY())
     */
    public void translate(int x, int y){
        return; //TODO codavaj!!
    }

}
