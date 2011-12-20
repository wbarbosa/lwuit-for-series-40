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
package com.sun.lwuit.io.impl;

import com.nokia.mid.iapinfo.IAPInfoException;
import com.sun.lwuit.io.NetworkManager;
import com.nokia.mid.iapinfo.AccessPoint;
import com.nokia.mid.iapinfo.IAPInfo;
import java.io.IOException;

/**
 * Supports access to the Nokia IAP lookup API
 *
 * @author Shai Almog
 */
public class NokiaAPImplementation extends MIDPImpl {
    private String currentAccessPoint;

    /**
     * @inheritDoc
     */
    public boolean isAPSupported() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public String[] getAPIds() {
        try {
            IAPInfo i = IAPInfo.getIAPInfo();
            AccessPoint[] pts = i.getAccessPoints();
            String[] s = new String[pts.length];
            for (int iter = 0; iter < pts.length; iter++) {
                s[iter] = "" + pts[iter].getID();
            }
            return s;
        } catch (IAPInfoException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public int getAPType(String id) {
        return NetworkManager.ACCESS_POINT_TYPE_UNKNOWN;
    }

    /**
     * @inheritDoc
     */
    public String getAPName(String id) {
        try {
            IAPInfo i = IAPInfo.getIAPInfo();
            AccessPoint[] pts = i.getAccessPoints();
            for (int iter = 0; iter < pts.length; iter++) {
                String s = "" + pts[iter].getID();
                if (s.equals(id)) {
                    return pts[iter].getName();
                }
            }
        } catch (IAPInfoException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public String getCurrentAccessPoint() {
        if(currentAccessPoint != null) {
            return currentAccessPoint;
        }
        try {
            AccessPoint a = IAPInfo.getIAPInfo().getLastUsedAccessPoint();
            if (a != null) {
                return "" + a.getID();
            }
        } catch (IAPInfoException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public void setCurrentAccessPoint(String id) {
        currentAccessPoint = id;
    }

    /**
     * @inheritDoc
     */
    public Object connect(String url, boolean read, boolean write) throws IOException {
        if(currentAccessPoint != null) {
            url += ";nokia_apnid=" + currentAccessPoint;
        }
        return super.connect(url, read, write);
    }
}
