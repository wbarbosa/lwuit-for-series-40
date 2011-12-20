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
 * The Font class represents fonts and font metrics. Fonts cannot be created by applications. Instead, applications query for fonts based on font attributes and the system will attempt to provide a font that matches the requested attributes as closely as possible.
 * A Font's attributes are style, size, and face. Values for attributes must be specified in terms of symbolic constants. Values for the style attribute may be combined using the bit-wise OR operator, whereas values for the other attributes may not be combined. For example, the value
 * STYLE_BOLD | STYLE_ITALIC
 * may be used to specify a bold-italic font; however
 * SIZE_LARGE | SIZE_SMALL
 * is illegal.
 * The values of these constants are arranged so that zero is valid for each attribute and can be used to specify a reasonable default font for the system. For clarity of programming, the following symbolic constants are provided and are defined to have values of zero:
 * STYLE_PLAIN SIZE_MEDIUM FACE_SYSTEM
 * Values for other attributes are arranged to have disjoint bit patterns in order to raise errors if they are inadvertently misused (for example, using FACE_PROPORTIONAL where a style is required). However, the values for the different attributes are not intended to be combined with each other.
 * Since: MIDP 1.0
 */
public final class Font{
    /**
     * The
     * monospace
     * font face.
     * Value 32 is assigned to FACE_MONOSPACE.
     * See Also:Constant Field Values
     */
    public static final int FACE_MONOSPACE=32;

    /**
     * The
     * proportional
     * font face.
     * Value 64 is assigned to FACE_PROPORTIONAL.
     * See Also:Constant Field Values
     */
    public static final int FACE_PROPORTIONAL=64;

    /**
     * The
     * system
     * font face.
     * Value 0 is assigned to FACE_SYSTEM.
     * See Also:Constant Field Values
     */
    public static final int FACE_SYSTEM=0;

    /**
     * Font specifier used by the implementation to draw text input by a user. FONT_INPUT_TEXT has the value 1.
     * Since: MIDP 2.0 See Also:getFont(int fontSpecifier), Constant Field Values
     */
    public static final int FONT_INPUT_TEXT=1;

    /**
     * Default font specifier used to draw Item and Screen contents. FONT_STATIC_TEXT has the value 0.
     * Since: MIDP 2.0 See Also:getFont(int fontSpecifier), Constant Field Values
     */
    public static final int FONT_STATIC_TEXT=0;

    /**
     * The
     * large
     * system-dependent font size.
     * Value 16 is assigned to SIZE_LARGE.
     * See Also:Constant Field Values
     */
    public static final int SIZE_LARGE=16;

    /**
     * The
     * medium
     * system-dependent font size.
     * Value 0 is assigned to STYLE_MEDIUM.
     * See Also:Constant Field Values
     */
    public static final int SIZE_MEDIUM=0;

    /**
     * The
     * small
     * system-dependent font size.
     * Value 8 is assigned to STYLE_SMALL.
     * See Also:Constant Field Values
     */
    public static final int SIZE_SMALL=8;

    /**
     * The bold style constant. This may be combined with the other style constants for mixed styles.
     * Value 1 is assigned to STYLE_BOLD.
     * See Also:Constant Field Values
     */
    public static final int STYLE_BOLD=1;

    /**
     * The italicized style constant. This may be combined with the other style constants for mixed styles.
     * Value 2 is assigned to STYLE_ITALIC.
     * See Also:Constant Field Values
     */
    public static final int STYLE_ITALIC=2;

    /**
     * The plain style constant. This may be combined with the other style constants for mixed styles.
     * Value 0 is assigned to STYLE_PLAIN.
     * See Also:Constant Field Values
     */
    public static final int STYLE_PLAIN=0;

    /**
     * The underlined style constant. This may be combined with the other style constants for mixed styles.
     * Value 4 is assigned to STYLE_UNDERLINED.
     * See Also:Constant Field Values
     */
    public static final int STYLE_UNDERLINED=4;

    /**
     * Returns the advance width of the characters in ch, starting at the specified offset and for the specified number of characters (length). The advance width is the horizontal distance that would be occupied if the characters were to be drawn using this Font, including inter-character spacing following the characters necessary for proper positioning of subsequent text.
     * The offset and length parameters must specify a valid range of characters within the character array ch. The offset parameter must be within the range [0..(ch.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = ch.length.
     */
    public int charsWidth(char[] ch, int offset, int length){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the advance width of the specified character in this Font. The advance width is the horizontal distance that would be occupied if ch were to be drawn using this Font, including inter-character spacing following ch necessary for proper positioning of subsequent text.
     */
    public int charWidth(char ch){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the distance in pixels from the top of the text to the text's baseline.
     */
    public int getBaselinePosition(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the default font of the system.
     */
    public static Font getDefaultFont(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the face of the font.
     */
    public int getFace(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the Font used by the high level user interface for the fontSpecifier passed in. It should be used by subclasses of CustomItem and Canvas to match user interface on the device.
     */
    public static Font getFont(int fontSpecifier){
        return null; //TODO codavaj!!
    }

    /**
     * Obtains an object representing a font having the specified face, style, and size. If a matching font does not exist, the system will attempt to provide the closest match. This method always returns a valid font object, even if it is not a close match to the request.
     */
    public static Font getFont(int face, int style, int size){
        return null; //TODO codavaj!!
    }

    /**
     * Gets the standard height of a line of text in this font. This value includes sufficient spacing to ensure that lines of text painted this distance from anchor point to anchor point are spaced as intended by the font designer and the device. This extra space (leading) occurs below the text.
     */
    public int getHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the size of the font.
     */
    public int getSize(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the style of the font. The value is an OR'ed combination of STYLE_BOLD, STYLE_ITALIC, and STYLE_UNDERLINED; or the value is zero (STYLE_PLAIN).
     */
    public int getStyle(){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns true if the font is bold.
     */
    public boolean isBold(){
        return false; //TODO codavaj!!
    }

    /**
     * Returns true if the font is italic.
     */
    public boolean isItalic(){
        return false; //TODO codavaj!!
    }

    /**
     * Returns true if the font is plain.
     */
    public boolean isPlain(){
        return false; //TODO codavaj!!
    }

    /**
     * Returns true if the font is underlined.
     */
    public boolean isUnderlined(){
        return false; //TODO codavaj!!
    }

    /**
     * Gets the total advance width for showing the specified String in this Font. The advance width is the horizontal distance that would be occupied if str were to be drawn using this Font, including inter-character spacing following str necessary for proper positioning of subsequent text.
     */
    public int stringWidth(java.lang.String str){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the total advance width for showing the specified substring in this Font. The advance width is the horizontal distance that would be occupied if the substring were to be drawn using this Font, including inter-character spacing following the substring necessary for proper positioning of subsequent text.
     * The offset and len parameters must specify a valid range of characters within str. The offset parameter must be within the range [0..(str.length())], inclusive. The len parameter must be a non-negative integer such that (offset + len) = str.length().
     */
    public int substringWidth(java.lang.String str, int offset, int len){
        return 0; //TODO codavaj!!
    }

}
