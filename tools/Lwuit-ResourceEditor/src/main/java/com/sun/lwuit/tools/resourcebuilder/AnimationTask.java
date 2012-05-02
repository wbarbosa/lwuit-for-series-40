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

import com.sun.lwuit.util.EditableResources;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Allows us to define an animated gif as an animation object
 *
 * @author Shai Almog
 */
public class AnimationTask extends ResourceTask {
    private AnimationImpl impl = new AnimationImpl();
    
    public void writeResource(DataOutputStream output) throws IOException {
        impl.writeResource(output);
    }

    
    public File getFile() {
        return impl.getFile();
    }

    public void setFile(File file) {
        impl.setFile(file);
        if(getName() == null) {
            setName(file.getName());
        }
    }
    
    public boolean getLoop() {
        return impl.getLoop();
    }
    
    public void setLoop(boolean loop) {
        impl.setLoop(loop);
    }

    @Override
    public void addToResources(EditableResources e) throws IOException {
        e.setAnimation(getName(), impl.convert());
    }
}
