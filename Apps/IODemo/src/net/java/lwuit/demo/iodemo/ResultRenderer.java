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

package net.java.lwuit.demo.iodemo;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.CacheMap;
import com.sun.lwuit.io.NetworkEvent;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.services.GoogleRESTService;
import com.sun.lwuit.io.services.ImageDownloadService;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.UIManager;
import java.util.Vector;

/**
 * A renderer for the container class
 *
 * @author Shai Almog
 */
public class ResultRenderer extends Container implements ListCellRenderer, ActionListener {
    private DefaultListCellRenderer unselected = new DefaultListCellRenderer(false);
    private CacheMap imageCache = new CacheMap();
    private Vector fetchingUrls = new Vector();
    private Label focus = new Label();
    private Label image = new Label();
    private Label title = new Label();
    private Label secondLine = new Label();
    private Label thirdLine = new Label();
    private List parentList;

    /**
     * @inheritDoc
     */
    public ResultRenderer() {
        setCellRenderer(true);
        focus.setUIID("ListRendererFocus");
        focus.setFocus(true);
        setUIID("ListRenderer");
        setLayout(new BorderLayout());
        Container content = new Container();
        content.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        content.addComponent(title);
        content.addComponent(secondLine);
        content.addComponent(thirdLine);
        addComponent(BorderLayout.CENTER, content);
        addComponent(BorderLayout.WEST, image);
    }

    private void updateTickering(Label l) {
        if(l.getText() == null) {
            return;
        }
        if(l.shouldTickerStart()) {
            if(!l.isTickerRunning()) {
                parentList.getComponentForm().registerAnimated(this);
                l.startTicker(UIManager.getInstance().getLookAndFeel().getTickerSpeed(), true);
            }
        } else {
            if(l.isTickerRunning()) {
                l.stopTicker();
            }
        }

    }

    /**
     * @inheritDoc
     */
    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if(isSelected) {
            parentList = list;
            if(value instanceof GoogleRESTService.ResultEntry) {
                GoogleRESTService.ResultEntry g = (GoogleRESTService.ResultEntry)value;
                title.setText(g.getTitle());
                secondLine.setText(g.getContent());
                thirdLine.setText(g.getUrl());
                updateTickering(title);
                updateTickering(secondLine);
                updateTickering(thirdLine);
                if(g.getTbUrl() != null) {
                    Image i = (Image)imageCache.get(g.getTbUrl());
                    if(i != null) {
                        image.setIcon(i);
                    } else {
                        image.setIcon(null);
                        if(!fetchingUrls.contains(g.getTbUrl())) {
                            fetchingUrls.addElement(g.getTbUrl());
                            ImageDownloadService d = new ImageDownloadService(g.getTbUrl(), (ActionListener)this);
                            NetworkManager.getInstance().addToQueue(d);
                        }
                    }
                } else {
                    image.setIcon(null);
                }
                return this;
            }
            
            // takeup space for the renderer
            title.setText("AAAAAAAAAAAAAAAAAAAA");
            secondLine.setText("AAAAAAAAAAAAAAAAAAAA");
            thirdLine.setText("AAAAAAAAAAAAAAAAAAAA");

            return this;
        } else {
            if(value instanceof GoogleRESTService.ResultEntry) {
                value = ((GoogleRESTService.ResultEntry)value).getTitle();
            }

            return unselected.getListCellRendererComponent(list, value, index, isSelected);
        }

    }

    private void checkTickering(Label l) {
        if(l.isTickerRunning()) {
            if(l.animate()) {
                parentList.repaint();
            }
        }
    }

    public boolean animate() {
        if(parentList != null && parentList.getComponentForm() != null) {
            checkTickering(title);
            checkTickering(secondLine);
            checkTickering(thirdLine);
        }
        return super.animate();
    }


    /**
     * @inheritDoc
     */
    public Component getListFocusComponent(List list) {
        return focus;
    }

    /**
     * @inheritDoc
     */
    public void actionPerformed(ActionEvent evt) {
        // called back when an image arrives from the server for thumbnail preview
        NetworkEvent n = (NetworkEvent)evt;
        imageCache.put(n.getConnectionRequest().getUrl(), n.getMetaData());
        fetchingUrls.removeElement(n.getConnectionRequest().getUrl());
        parentList.repaint();
    }

}
