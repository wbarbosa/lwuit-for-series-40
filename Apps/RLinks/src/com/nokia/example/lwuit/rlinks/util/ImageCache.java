/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.nokia.example.lwuit.rlinks.util;

import com.sun.lwuit.Image;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Vector;

public class ImageCache {

    private static int maxHardReferences = 16;
    private final Hashtable cache = new Hashtable();
    private final Vector hardReferenceBuffer = new Vector();

    public Image get(Object key) {
        WeakReference weakReference = (WeakReference) cache.get(key);
        if (weakReference == null) {
            return null;
        }

        return (Image) weakReference.get();
    }

    public WeakReference put(Object key, Image value) {
        WeakReference newReference = new WeakReference(value);

        hardReferenceBuffer.addElement(value);
        if (hardReferenceBuffer.size() > maxHardReferences) {
            hardReferenceBuffer.removeElementAt(0);
        }

        WeakReference oldReference = (WeakReference) cache.put(key, newReference);

        if (oldReference == null) {
            return null;
        }

        oldReference.clear();

        return newReference;
    }

    public Image remove(Object key) {
        WeakReference oldReference = (WeakReference) cache.remove(key);

        if (oldReference == null) {
            return null;
        }

        Image reference = (Image) oldReference.get();
        oldReference.clear();

        hardReferenceBuffer.removeElement(reference);

        return reference;
    }

    public void setHardCacheSize(int size) {
        maxHardReferences = size;
    }
}
