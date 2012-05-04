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

/**
 * Utility to access package protected lwuit classes and attributes
 *
 * @author Shai Almog
 */
public class LWUITAccessor {
    public static com.sun.lwuit.Image getImage(com.sun.lwuit.Font f) {
        return ((com.sun.lwuit.CustomFont)f).cache;
    }

    public static int[] getOffsets(com.sun.lwuit.Font f) {
        return ((com.sun.lwuit.CustomFont)f).cutOffsets;
    }

    public static int[] getWidths(com.sun.lwuit.Font f) {
        return ((com.sun.lwuit.CustomFont)f).charWidth;
    }
    
    public static int[] getPalette(com.sun.lwuit.IndexedImage p) {
        return p.palette;
    }

    public static byte[] getImageData(com.sun.lwuit.IndexedImage p) {
        return p.imageDataByte;
    }
    
    public static boolean isKeyframe(StaticAnimation anim, int offset) {
        return anim.isKeyframe(offset);
    }

    public static byte[] getKeyframe(StaticAnimation anim, int offset) {
        return anim.getKeyframe(offset);
    }

    public static boolean isDrawPrevious(StaticAnimation anim, int offset) {
        return anim.isDrawPrevious(offset);
    }

    public static byte[][] getModifiedRows(StaticAnimation anim, int offset) {
        return anim.getModifiedRows(offset);
    }

    public static int[] getModifiedRowOffsets(StaticAnimation anim, int offset) {
        return anim.getModifiedRowOffsets(offset);
    }

    public static boolean isScrollableX(com.sun.lwuit.Container c) {
        if(c instanceof com.sun.lwuit.Form) {
            c = ((com.sun.lwuit.Form)c).getContentPane();
        }
        return c.scrollableX;
    }

    public static void setScrollableX(com.sun.lwuit.Container c, boolean scrollableX) {
        if(c instanceof com.sun.lwuit.Form) {
            c = ((com.sun.lwuit.Form)c).getContentPane();
        }
        c.scrollableX = scrollableX;
    }

    public static boolean isScrollableY(com.sun.lwuit.Container c) {
        if(c instanceof com.sun.lwuit.Form) {
            c = ((com.sun.lwuit.Form)c).getContentPane();
        }
        return c.scrollableY;
    }

    public static void setScrollableY(com.sun.lwuit.Container c, boolean scrollableY) {
        if(c instanceof com.sun.lwuit.Form) {
            c = ((com.sun.lwuit.Form)c).getContentPane();
        }
        c.scrollableY = scrollableY;
    }
}
