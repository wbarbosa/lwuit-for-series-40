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

package com.sun.lwuit.resources.editor.editors;

import com.sun.lwuit.resource.util.ImageTools;
import com.sun.lwuit.resources.editor.ResourceEditorView;
import com.sun.lwuit.util.EditableResources;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Allows editing an indexed image which is now deprecated
 *
 * @author Shai Almog
 */
public class ImageIndexedEditor extends ImageRGBEditor {
    public ImageIndexedEditor(EditableResources res, String name, ResourceEditorView view) {
        super(res, name, view);
    }

    protected com.sun.lwuit.Image createImage(byte[] data) throws IOException {
        com.sun.lwuit.Image i = com.sun.lwuit.IndexedImage.pack(com.sun.lwuit.Image.createImage(data, 0, data.length));
        if(i instanceof com.sun.lwuit.IndexedImage) {
            return i;
        } else {
            // OK we need to manually compress the image or reverse the packing...
            JOptionPane.showMessageDialog(this, "The image contains more than 256 colors. The resource editor can reduce the\n" +
                "number of colors but it might cause a loss of quality.", "Too Many Colors", JOptionPane.PLAIN_MESSAGE);
            return ImageTools.forcePack(i);
        }
    }

    protected String getType() {
        return "Indexed";
    }
}
