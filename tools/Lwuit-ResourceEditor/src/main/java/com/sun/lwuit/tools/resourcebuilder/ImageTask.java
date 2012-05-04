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

package com.sun.lwuit.tools.resourcebuilder;

import com.sun.lwuit.resource.util.ImageTools;
import com.sun.lwuit.EncodedImage;
import com.sun.lwuit.Image;
import com.sun.lwuit.util.EditableResources;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents both the standard PNG image and a packed image
 *
 * @author Shai Almog
 */
public class ImageTask extends ResourceTask {
    private File file;
    private boolean pack;
    
    public File getSrc() {
        return file;
    }
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        if(getName() == null) {
            setName(file.getName());
        }
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public boolean isIndexed() {
        return pack;
    }

    public void setIndexed(boolean pack) {
        this.pack = pack;
    }

    @Override
    public void addToResources(EditableResources e) throws IOException {
        Image i;
        InputStream input = new FileInputStream(file);
        i = EncodedImage.create(input);
        if(pack) {
            i = ImageTools.forcePack(i);
        }
        input.close();
        e.setImage(getName(), i);
    }
}
