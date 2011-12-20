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

package com.sun.lwuit.impl.midp;

import com.sun.lwuit.impl.ImplementationFactory;
import com.sun.lwuit.impl.LWUITImplementation;

/**
 * Implementation factory that initializes the SVGImplementation if SVG is supported
 * by the underlying platform.
 *
 * @author Shai Almog
 */

public class SVGImplementationFactory extends ImplementationFactory {
    private static final boolean IS_SUPPORTED;

    static {
        boolean supported = false;
        try {
            Class.forName("javax.microedition.m2g.SVGImage");
            supported = true;
        } catch (Throwable t) {
        }
        IS_SUPPORTED = supported;
    }


    /**
     * Installs the SVG Implementation, this method must be invoked <strong>before</strong>
     * the first call to Display.init().
     */
    public static void init() {
        ImplementationFactory.setInstance(new SVGImplementationFactory());
    }

    /**
     * @inheritDoc
     */
    public LWUITImplementation createImplementation() {
        if (IS_SUPPORTED) {
            return new SVGImplementation();
        }
        return new GameCanvasImplementation();
    }
}
