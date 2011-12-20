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
 * The Image class is used to hold graphical image data. Image objects exist independently of the display device. They exist only in off-screen memory and will not be painted on the display unless an explicit command is issued by the application (such as within the paint() method of a Canvas) or when an Image object is placed within a Form screen or an Alert screen and that screen is made current.
 * Images are either mutable or immutable depending upon how they are created. Immutable images are generally created by loading image data from resource bundles, from files, or from the network. They may not be modified once created. Mutable images are created as blank images containing only white pixels. The application may render on a mutable image by calling getGraphics() on the Image to obtain a Graphics object expressly for this purpose.
 * Images may be placed within Alert, Choice, Form, or ImageItem objects. The high-level user interface implementation may need to update the display at any time, without notifying the application. In order to provide predictable behavior, the high-level user interface objects provide snapshot semantics for the image. That is, when a mutable image is placed within an Alert, Choice, Form, or ImageItem object, the effect is as if a snapshot is taken of its current contents. This snapshot is then used for all subsequent painting of the high-level user interface component. If the application modifies the contents of the image, the application must update the component containing the image (for example, by calling ImageItem.setImage) in order to make the modified contents visible.
 * An immutable image may be created from a mutable image through the use of the createImage method. It is possible to create a mutable copy of an immutable image using a technique similar to the following:
 * Every pixel within a mutable image is always fully opaque. Immutable images may contain a combination of fully opaque pixels (alpha = 2bitdepth-1), fully transparent pixels (alpha=0), and semitransparent pixels (0alpha 2bitdepth-1), where bitdepth is the number of bits per sample in the source data.
 * Implementations must support storage, processing, and rendering of fully opaque pixels and fully transparent pixels in immutable images. When creating an image from source data (whether from a PNG file or from an array of ARGB data), a fully opaque pixel in the source data must always result in a fully opaque pixel in the new image, and a fully transparent pixel in the source data must always result in a fully transparent pixel in the new image.
 * The required treatment of semitransparent pixel data depends upon whether the implementation supports alpha blending at rendering time. If the implementation supports alpha blending, a semitransparent pixel in the source data must result in a semitransparent pixel in the new image. The resulting alpha value may be modified to accommodate the number of levels of semitransparency supported by the platform. (See the Display.numAlphaLevels() method.) If an implementation does not support alpha blending, any semitransparent pixels in the source data must be replaced with fully transparent pixels in the new image.
 * Implementations are required to support images stored in the PNG format, as specified by the PNG (Portable Network Graphics) Specification, Version 1.0. All conforming MIDP implementations are also conformant to the minimum set of requirements given by the PNG Specification. MIDP implementations also must conform to additional requirements given here with respect to handling of PNG images. Note that the requirements listed here take precedence over any conflicting recommendations given in the PNG Specification.
 * All of the 'critical' chunks specified by PNG must be supported. The paragraphs below describe these critical chunks.
 * The IHDR chunk. MIDP devices must handle the following values in the IHDR chunk:
 * The PLTE chunk. Palette-based images must be supported.
 * The IDAT chunk. Image data may be encoded using any of the 5 filter types defined by filter method 0 (None, Sub, Up, Average, Paeth).
 * The IEND chunk. This chunk must be found in order for the image to be considered valid.
 * PNG defines several 'ancillary' chunks that may be present in a PNG image but are not critical for image decoding.
 * The tRNS chunk. All implementations must support the tRNS chunk. This chunk is used to implement transparency without providing alpha channel data for each pixel. For color types 0 and 2, a particular gray or RGB value is defined to be a transparent pixel. In this case, the implementation must treat pixels with this value as fully transparent. Pixel value comparison must be based on the actual pixel values using the original sample depth; that is, this comparison must be performed before the pixel values are resampled to reflect the display capabilities of the device. For color type 3 (indexed color), 8-bit alpha values are potentially provided for each entry in the color palette. In this case, the implementation must treat pixels with an alpha value of 0 as fully transparent, and it must treat pixels with an alpha value of 255 as fully opaque. If rendering with alpha blending is supported, any pixels with intermediate alpha values must be carried through to the resulting image. If alpha blending is not supported, any pixels with intermediate alpha values must be replaced with fully transparent pixels.
 * The implementation may (but is not required to) support any of the other ancillary chunks. The implementation must silently ignore any unsupported ancillary chunks that it encounters. The currently defined optional ancillary chunks are:
 * PNG (Portable Network Graphics) Specification, Version 1.0. W3C Recommendation, October 1, 1996. http://www.w3.org/TR/REC-png.html. Also available as RFC 2083, http://www.ietf.org/rfc/rfc2083.txt.
 * Since: MIDP 1.0
 */
public class Image{
    /**
     * Creates an immutable image which is decoded from the data stored in the specified byte array at the specified offset and length. The data must be in a self-identifying image file format supported by the implementation, such as
     * .
     * The imageoffset and imagelength parameters specify a range of data within the imageData byte array. The imageOffset parameter specifies the offset into the array of the first data byte to be used. It must therefore lie within the range [0..(imageData.length-1)]. The imageLength parameter specifies the number of data bytes to be used. It must be a positive integer and it must not cause the range to extend beyond the end of the array. That is, it must be true that imageOffset + imageLength imageData.length.
     * This method is intended for use when loading an image from a variety of sources, such as from persistent storage or from the network.
     */
    public static Image createImage(byte[] imageData, int imageOffset, int imageLength){
        return null; //TODO codavaj!!
    }

    /**
     * Creates an immutable image from a source image. If the source image is mutable, an immutable copy is created and returned. If the source image is immutable, the implementation may simply return it without creating a new image. If an immutable source image contains transparency information, this information is copied to the new image unchanged.
     * This method is useful for placing the contents of mutable images into Choice objects. The application can create an off-screen image using the createImage(w, h) method, draw into it using a Graphics object obtained with the getGraphics() method, and then create an immutable copy of it with this method. The immutable copy may then be placed into Choice objects.
     */
    public static Image createImage(Image source){
        return null; //TODO codavaj!!
    }

    /**
     * Creates an immutable image using pixel data from the specified region of a source image, transformed as specified.
     * The source image may be mutable or immutable. For immutable source images, transparency information, if any, is copied to the new image unchanged.
     * On some devices, pre-transformed images may render more quickly than images that are transformed on the fly using drawRegion. However, creating such images does consume additional heap space, so this technique should be applied only to images whose rendering speed is critical.
     * The transform function used must be one of the following, as defined in the Sprite class: Sprite.TRANS_NONE - causes the specified image region to be copied unchanged Sprite.TRANS_ROT90 - causes the specified image region to be rotated clockwise by 90 degrees. Sprite.TRANS_ROT180 - causes the specified image region to be rotated clockwise by 180 degrees. Sprite.TRANS_ROT270 - causes the specified image region to be rotated clockwise by 270 degrees. Sprite.TRANS_MIRROR - causes the specified image region to be reflected about its vertical center. Sprite.TRANS_MIRROR_ROT90 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 90 degrees. Sprite.TRANS_MIRROR_ROT180 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 180 degrees. Sprite.TRANS_MIRROR_ROT270 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 270 degrees.
     * The size of the returned image will be the size of the specified region with the transform applied. For example, if the region is 100x50 pixels and the transform is TRANS_ROT90, the returned image will be 50x100 pixels.
     * Note: If all of the following conditions are met, this method may simply return the source Image without creating a new one:
     * the source image is immutable; the region represents the entire source image; and the transform is TRANS_NONE.
     */
    public static Image createImage(Image image, int x, int y, int width, int height, int transform){
        return null; //TODO codavaj!!
    }

    /**
     * Creates an immutable image from decoded image data obtained from an InputStream. This method blocks until all image data has been read and decoded. After this method completes (whether by returning or by throwing an exception) the stream is left open and its current position is undefined.
     */
    public static Image createImage(java.io.InputStream stream) throws java.io.IOException{
        return null; //TODO codavaj!!
    }

    /**
     * Creates a new, mutable image for off-screen drawing. Every pixel within the newly created image is white. The width and height of the image must both be greater than zero.
     */
    public static Image createImage(int width, int height){
        return null; //TODO codavaj!!
    }

    /**
     * Creates an immutable image from decoded image data obtained from the named resource. The name parameter is a resource name as defined by
     * . The rules for resolving resource names are defined in the
     * section of the java.lang package documentation.
     */
    public static Image createImage(java.lang.String name) throws java.io.IOException{
        return null; //TODO codavaj!!
    }

    /**
     * Creates an immutable image from a sequence of ARGB values, specified as 0xAARRGGBB. The ARGB data within the rgb array is arranged horizontally from left to right within each row, row by row from top to bottom. If processAlpha is true, the high-order byte specifies opacity; that is, 0x00RRGGBB specifies a fully transparent pixel and 0xFFRRGGBB specifies a fully opaque pixel. Intermediate alpha values specify semitransparency. If the implementation does not support alpha blending for image rendering operations, it must replace any semitransparent pixels with fully transparent pixels. (See
     * for further discussion.) If processAlpha is false, the alpha values are ignored and all pixels must be treated as fully opaque.
     * Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, where rows and columns are numbered downward from the top starting at zero, and columns are numbered rightward from the left starting at zero. This operation can then be defined as:
     * for
     */
    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha){
        return null; //TODO codavaj!!
    }

    /**
     * Creates a new Graphics object that renders to this image. This image must be mutable; it is illegal to call this method on an immutable image. The mutability of an image may be tested with the isMutable() method.
     * The newly created Graphics object has the following properties:
     * the destination is this Image object; the clip region encompasses the entire Image; the current color is black; the font is the same as the font returned by
     * ; the stroke style is
     * ; and the origin of the coordinate system is located at the upper-left corner of the Image.
     * The lifetime of Graphics objects created using this method is indefinite. They may be used at any time, by any thread.
     */
    public Graphics getGraphics(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the height of the image in pixels. The value returned must reflect the actual height of the image when rendered.
     */
    public int getHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Obtains ARGB pixel data from the specified region of this image and stores it in the provided array of integers. Each pixel value is stored in 0xAARRGGBB format, where the high-order byte contains the alpha channel and the remaining bytes contain color components for red, green and blue, respectively. The alpha channel specifies the opacity of the pixel, where a value of 0x00 represents a pixel that is fully transparent and a value of 0xFF represents a fully opaque pixel.
     * The returned values are not guaranteed to be identical to values from the original source, such as from createRGBImage or from a PNG image. Color values may be resampled to reflect the display capabilities of the device (for example, red, green or blue pixels may all be represented by the same gray value on a grayscale device). On devices that do not support alpha blending, the alpha value will be 0xFF for opaque pixels and 0x00 for all other pixels (see Alpha Processing for further discussion.) On devices that support alpha blending, alpha channel values may be resampled to reflect the number of levels of semitransparency supported.
     * The scanlength specifies the relative offset within the array between the corresponding pixels of consecutive rows. In order to prevent rows of stored pixels from overlapping, the absolute value of scanlength must be greater than or equal to width. Negative values of scanlength are allowed. In all cases, this must result in every reference being within the bounds of the rgbData array.
     * Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, where rows and columns are numbered downward from the top starting at zero, and columns are numbered rightward from the left starting at zero. This operation can then be defined as:
     * for
     * The source rectangle is required to not exceed the bounds of the image. This means:
     * If any of these conditions is not met an IllegalArgumentException is thrown. Otherwise, in cases where width = 0 or height = 0, no exception is thrown, and no pixel data is copied to rgbData.
     */
    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height){
        return; //TODO codavaj!!
    }

    /**
     * Gets the width of the image in pixels. The value returned must reflect the actual width of the image when rendered.
     */
    public int getWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Check if this image is mutable. Mutable images can be modified by rendering to them through a Graphics object obtained from the getGraphics() method of this object.
     */
    public boolean isMutable(){
        return false; //TODO codavaj!!
    }

}
