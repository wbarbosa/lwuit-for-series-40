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

package com.sun.lwuit.resources.editor;

import com.sun.lwuit.resource.util.WrappingLayout;
import com.sun.lwuit.util.EditableResources;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Simple abstraction for elements within the resource editor as a horizontal list
 * with the appropriate preview
 *
 * @author Shai Almog
 */
public class HorizontalList extends JPanel {
    private static boolean blockRefeshWhileLoading;

    /**
     * @return the blockRefeshWhileLoading
     */
    public static boolean isBlockRefeshWhileLoading() {
        return blockRefeshWhileLoading;
    }

    /**
     * @param aBlockRefeshWhileLoading the blockRefeshWhileLoading to set
     */
    public static void setBlockRefeshWhileLoading(boolean aBlockRefeshWhileLoading) {
        blockRefeshWhileLoading = aBlockRefeshWhileLoading;
    }
    private EditableResources res;
    private static ButtonGroup group = new ButtonGroup();
    private ResourceEditorView view;
    private static int iconWidth;
    private static int iconHeight;
    
    static {
        iconWidth = Preferences.userNodeForPackage(HorizontalList.class).getInt("previewIconWidth", 24);
        iconHeight = Preferences.userNodeForPackage(HorizontalList.class).getInt("previewIconHeight", 24);
    }
    
    private ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String text = ((JToggleButton)e.getSource()).getText();
            
            // special case for font
            if(text.equals("")) {
                text = ((JToggleButton)e.getSource()).getToolTipText();
            }
            view.setSelectedResource(text);
        }
    };

    public HorizontalList(EditableResources res, ResourceEditorView view) {
        this(res, view, -1);
    }
    
    public HorizontalList(EditableResources res, ResourceEditorView view, int maxButtonWidth) {
        this.res = res;
        this.view = view;
        setOpaque(false);
        setLayout(new WrappingLayout(maxButtonWidth));
        res.addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent e) {
                refresh();
            }

            public void treeNodesInserted(TreeModelEvent e) {
                refresh();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                refresh();
            }

            public void treeStructureChanged(TreeModelEvent e) {
                refresh();
            }
        });
    }
    
    public void refresh() {
        if(blockRefeshWhileLoading) {
            return;
        }
        for(java.awt.Component cmp : getComponents()) {
            remove(cmp);
            group.remove((JToggleButton)cmp);
        }
        String[] entries = getEntries();
        Arrays.sort(entries, String.CASE_INSENSITIVE_ORDER);
        initLayout(entries.length);
        for(String current : entries) {
            JToggleButton button = createButton(current);
            add(button);
            String selection = view.getSelectedResource();
            if(selection != null && selection.equals(current)) {
                button.setSelected(true);
            }
            button.addActionListener(listener);
            group.add(button);
        }
        revalidate();
        repaint();
    }

    protected void initLayout(int count) {
    }
    
    protected EditableResources getRes() {
        return res;
    }
    
    protected JToggleButton createButton(String label) {
        JToggleButton button = new JToggleButton(label, getIconImage(label));
        button.setToolTipText(label);
        button.setRolloverEnabled(true);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        return button;
    }
    
    public Icon getIconImage(final String current) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                com.sun.lwuit.Image bgImage = (com.sun.lwuit.Image)res.getTheme(current).get("Form.bgImage");
                if(bgImage != null) {
                    int[] rgb = bgImage.scaled(getIconWidth(), getIconHeight()).getRGB();
                    BufferedImage i = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    i.setRGB(0, 0, getIconWidth(), getIconHeight(), rgb, 0, getIconWidth());
                    g.drawImage(i, x, y, null);
                } else {
                    final String bgColor = (String)res.getTheme(current).get("bgColor");
                    if(bgColor != null) {
                        Color col = new Color(Integer.decode("0x" + bgColor));
                        g.setColor(col);
                        g.fillRect(x, y, getIconWidth(), getIconHeight());
                    }
                }
            }

            public int getIconWidth() {
                return getSettingsIconWidth();
            }

            public int getIconHeight() {
                return getSettingsIconHeight();
            }
        };
    }
    
    public String[] getEntries() {
        return res.getThemeResourceNames();
    }
    
    public static int getSettingsIconWidth() {
        return iconWidth;
    }

    public static int getSettingsIconHeight() {
        return iconHeight;
    }

    public static void setSettingsIconWidth(int v) {
        iconWidth = v;
        Preferences.userNodeForPackage(HorizontalList.class).putInt("previewIconWidth", iconWidth);
    }

    public static void setSettingsIconHeight(int v) {
        iconHeight = v;
        Preferences.userNodeForPackage(HorizontalList.class).putInt("previewIconHeight", iconHeight);
    }
}
