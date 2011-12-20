/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.lwuit;


/**
 * Support for binding the 3D graphics M3G API (JSR 184), this allows us to integrate
 * 2D UI's with 3D special effects and transitions while keeping the rendering
 * pipelines in sync.
 * <p>This class is a singleton that includes a callback interface which abstracts
 * the separation between the 2D and 3D pipelines. For more on the difference between
 * 2D and 3D rendering pipelines please consult the M3G documentation.
 * 
 * @author Shai Almog
 * @deprecated this class isn't portable, its functionality will move to an external package in the future
 */
public class M3G {

    private static final M3G INSTANCE = new M3G();
    private static final boolean IS_SUPPORTED = false;

    private M3G() {
    }

    /**
     * Returns the singleton instance of this class
     * 
     * @return the singleton instance of this class
     */
    public static M3G getInstance() {
        return INSTANCE;
    }

    /**
     * Returns true if the M3G (JSR 184) API is supported on this device
     * 
     * @return True if this device supports M3G
     */
    public static boolean isM3GSupported() {
        return IS_SUPPORTED;
    }

    /**
     * Returns the maximum size for a texture according to the underlying graphics engine
     * 
     * @return the size of the largest possible texture on the device
     */
    public int getMaxTextureDimension() {
        return 0;
    }
    
    /**
     * Helper method returns the closest power of two smaller than X
     * 
     * @param x number
     * @return a power of 2 smaller than X
     */
    public static int closestLowerPowerOf2(int x) {        
        return closestHigherPowerOf2(x) >> 1;
    }

    /**
     * Helper method returns the closest power of two larger than X
     * 
     * @param x number
     * @return a power of 2 smaller than X
     */
    public static int closestHigherPowerOf2(int x) {
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        x++;
        return x;
    }

    /**
     * Binds the Graphics3D object to the current graphics context and invokes
     * callback to perform the rendering. This method is responsible for flushing
     * the graphics3D object and buffer to avoid problems related to 2D/3D rendering
     * pipeline collision.
     * 
     * @param g Graphics context to draw on, notice that translate and clipping might be ignored
     * @param depth should depth buffering be used in the graphics context bind method
     * @param arguments arguments to the Graphics3D bind method
     * @param c the callback invoked to perform the actual 3D rendering
     */
    public void renderM3G(Graphics g, boolean depth, int arguments, Callback c) {
    }

    /**
     * Callback interface that allows rendering of 3D graphics on top/bellow the 
     * current form. This interface is invoked as a result of a renderM3G call
     * and should not make any calls to the 2D pipeline. It is the responsibility
     * of the developer not to open additional threads or try to "abuse" the 2D/3D
     * pipeline separation in any way.
     */
    public static interface Callback {
    }
}
