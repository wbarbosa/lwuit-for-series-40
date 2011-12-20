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

package preview;

import javax.microedition.midlet.*;
import com.sun.lwuit.*;
import com.sun.lwuit.util.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.plaf.*;

/**
 * MIDlet/RIM app used to preview LWUIT on devices within the resource editor
 *
 * @author Shai Almog
 */
public class PreviewMIDlet
//#ifdef Blackberry
//#         extends net.rim.device.api.ui.UiApplication
//#else
        extends MIDlet
//#endif
        implements Runnable
{
    public static void main(String[] argv) {
        new PreviewMIDlet().startApp();
    }

    public void run() {
        try {
            final Resources res = Resources.open("/r.res");
            UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            if(res.getUIResourceNames().length > 1) {
                Form pick = new Form("Pick Form");
                final ComboBox cb = new ComboBox(res.getUIResourceNames());
                pick.addComponent(cb);
                cb.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        UIBuilder b = new UIBuilder();
                        b.setResourceFilePath("/r.res");
                        Form f = (Form)b.createContainer(res, (String)cb.getSelectedItem());
                        f.show();
                    }
                });
                pick.show();
            } else {
                UIBuilder b = new UIBuilder();
                b.setResourceFilePath("/r.res");
                Form f = (Form)b.createContainer(res, res.getUIResourceNames()[0]);
                f.show();
            }
        } catch(java.io.IOException err) {
            err.printStackTrace();
        }
    }

    public void startApp() {
        Display.init(this);
        NetworkManager.getInstance().start();
        UIBuilder.registerCustomComponent("HTMLComponent", com.sun.lwuit.html.HTMLComponent.class);
        UIBuilder.registerCustomComponent("Tree", com.sun.lwuit.tree.Tree.class);
        UIBuilder.registerCustomComponent("Table", com.sun.lwuit.table.Table.class);
        UIBuilder.registerCustomComponent("ContainerList", com.sun.lwuit.list.ContainerList.class);
        UIBuilder.registerCustomComponent("RSSReader", com.sun.lwuit.io.ui.RSSReader.class);
        UIBuilder.registerCustomComponent("FileTree", com.sun.lwuit.io.ui.FileTree.class);
        UIBuilder.registerCustomComponent("WebBrowser", com.sun.lwuit.io.ui.WebBrowser.class);
        Display.getInstance().callSerially(this);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
