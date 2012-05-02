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

package com.sun.lwuit.resource.util;

import com.sun.lwuit.IndexedImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Tools allowing us to support the indexed image API in the resource editor
 *
 * @author Shai Almog
 */
public class ImageTools {

    static List<Integer> buildPalette(int[] array) {
        List<Integer> colors = new ArrayList<Integer>();
        for(int val : array) {
            Integer i = new Integer(val);
            if(!colors.contains(i)) {
                colors.add(i);
            }
        }
        return colors;
    }
    
    private static void replace(int[] array, int oldColor, int newColor) {
        for(int iter = 0 ; iter < array.length ; iter++) {
            if(array[iter] == oldColor) {
                array[iter] = newColor;
            }
        }
    }

    /**
     * Converts a standard image into a packed image while potentially reducing colors
     * and alpha channel
     */
    public static IndexedImage forcePack(com.sun.lwuit.Image unpacked) {
        int w = unpacked.getWidth();
        int h = unpacked.getHeight();
        int[] array = unpacked.getRGB();

        // remove transparent colors:
        for(int iter = 0 ; iter < array.length ; iter++) {
            if((array[iter] & 0xff000000) == 0) {
                array[iter] = 0;
            }
        }

        List<Integer> colors = buildPalette(array);
        int originalColorCount = colors.size();
        if(originalColorCount > 256) {
            // first reduce the number of alpha channels to 2
            for(Integer val : colors) {
                int current = val;
                int alpha = (current >> 24) & 0xff;
                if(alpha == 0xff || alpha == 0) {
                    continue;
                }
                if(alpha >= 0xf0) {
                    alpha = 0xff;
                } else {
                    if(alpha <= 9) {
                        alpha = 0;
                    } else {
                        alpha = 0x7f;
                    }
                }
                int newColor = (current & 0xffffff) | ((alpha << 24) & 0xff000000);
                replace(array, current, newColor);
            }
            colors = buildPalette(array);

            // just start removing similar colors by finding the closest matching colors
            if(colors.size() > 256) {
                BufferedImage indexed = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED);
                indexed.setRGB(0, 0, w, h, array, 0, w);
                array = indexed.getRGB(0, 0, w, h, array, 0, w);
            }
        }
        return (IndexedImage)IndexedImage.pack(com.sun.lwuit.Image.createImage(array, w, h));
    }    
}
