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
package com.sun.lwuit;

import com.sun.lwuit.animations.Transition;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A component that lets the user switch between a group of components by
 * clicking on a tab with a given title and/or icon.
 * 
 * <p>
 * Tabs/components are added to a <code>TabbedPane</code> object by using the
 * <code>addTab</code> and <code>insertTab</code> methods.
 * A tab is represented by an index corresponding
 * to the position it was added in, where the first tab has an index equal to 0
 * and the last tab has an index equal to the tab count minus 1.
 * <p>
 * The <code>TabbedPane</code> uses a <code>SingleSelectionModel</code>
 * to represent the set of tab indices and the currently selected index.  
 * If the tab count is greater than 0, then there will always be a selected 
 * index, which by default will be initialized to the first tab.  
 * If the tab count is 0, then the selected index will be -1.
 * <p>
 * @deprecated see Tabs.java
 * @author Tamir Shabat
 *
 */
public class TabbedPane extends Container {
    private Transition transitionRight;
    private Transition transitionLeft;
    private Container contentPane = new Container(new BorderLayout());
    private List tabsList = new List();
    private Hashtable tabsTable = new Hashtable();
    /** 
     * Where the tabs are placed.
     */
    private int tabPlacement = -1;
    /**
     * The TabbedPane surrounded border width (contentPane and tabs border)
     */
    private int tPBorder = 1;

    /**
     * Allows tracking list changes to allow refresh of the tabbed pane
     */
    private int lastListX;

    /**
     * Allows tracking list changes to allow refresh of the tabbed pane
     */
    private int lastListY;

    /**
     * Creates an empty <code>TabbedPane</code> with a default
     * tab placement of <code>Component.TOP</code>.
     */
    public TabbedPane() {
        this(TOP);
    }

    /**
     * Creates an empty <code>TabbedPane</code> with the specified tab placement
     * of either: <code>Component.TOP</code>, <code>Component.BOTTOM</code>,
     * <code>Component.LEFT</code>, or <code>Component.RIGHT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     */
    public TabbedPane(int tabPlacement) {
        super(new BorderLayout());
        contentPane.setUIID("TabbedPane");
        contentPane.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                UIManager.getInstance().getLookAndFeel().
                        drawTabbedPaneContentPane(TabbedPane.this, g, rect,
                        tabsList.getPreferredSize(), tabsList.size(),
                        tabsList.getSelectedIndex(), tabsList.getElementSize(true, true),
                        tabsList.getScrollX(), tabsList.getScrollY());
            }
        });
        super.addComponent(BorderLayout.CENTER, contentPane);
        
        setTabPlacement(tabPlacement);

        tabsList.getUnselectedStyle().setPadding(0, 0, 0, 0);
        tabsList.getUnselectedStyle().setMargin(0, 0, 0, 0);
        tabsList.getUnselectedStyle().setBorder(null);
        tabsList.getSelectedStyle().setPadding(0, 0, 0, 0);
        tabsList.getSelectedStyle().setMargin(0, 0, 0, 0);
        tabsList.getSelectedStyle().setBorder(null);
        
        tabsList.setListCellRenderer(new TabsRenderer());
        tabsList.setItemGap(0);
        tabsList.setIsScrollVisible(false);
        tabsList.addSelectionListener(new SelectionListener() {
            public void selectionChanged(int oldSelected, int newSelected) {
                if(oldSelected == newSelected) {
                    return;
                }
                Component c = (Component) tabsList.getModel().getItemAt(newSelected);
                Transition t = transitionLeft;
                if(oldSelected < newSelected) {
                    t = transitionRight;
                }
                if(c != null) {
                    if(t == null || contentPane.getComponentCount() == 0) {
                        contentPane.removeAll();
                        contentPane.addComponent(BorderLayout.CENTER, (Component) tabsTable.get(c));
                        if(isInitialized()) {
                            revalidate();
                        } else {
                            setShouldCalcPreferredSize(true);
                        }
                    } else {
                        contentPane.flushReplace();
                        contentPane.replace(contentPane.getComponentAt(0), (Component) tabsTable.get(c), t);
                    }
                }
            }
        });        
        
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        // we track changes to the list and repaint when the list is scrolled so the border paints correctly
        boolean b = super.animate();
        int x = tabsList.getScrollX();
        int y = tabsList.getScrollY();
        if(lastListY != y || lastListX != x) {
            b = true;
            lastListX = x;
            lastListY = y;
        }
        return b;
    }

    /**
     * @inheritDoc
     */
    void initComponentImpl() {
        super.initComponentImpl();
        getComponentForm().registerAnimatedInternal(this);
    }

    /**
     * Indicates the transition to use when switching between tabs from right to left. 
     * 
     * @param transitionLeft transition to use when switching tabs or null to avoid transition
     */
    public void setTransitionLeft(Transition transitionLeft) {
        this.transitionLeft = transitionLeft;
    }

    /**
     * Indicates the transition to use when switching between tabs from right to left. 
     * 
     * @return the transition towards the left direction
     */
    public Transition getTransitionLeft() {
        return transitionLeft;
    }
    
    /**
     * Indicates the transition to use when switching between tabs from left to right. 
     * 
     * @param transitionRight transition to use when switching tabs or null to avoid transition
     */
    public void setTransitionRight(Transition transitionRight) {
        this.transitionRight = transitionRight;
    }

    /**
     * Indicates the transition to use when switching between tabs from left to right. 
     * 
     * @return the transition towards the right direction
     */
    public Transition getTransitionRight() {
        return transitionRight;
    }
    
    /**
     * @inheritDoc
     */
    public void setFocusable(boolean b) {
        if(tabsList != null) {
            tabsList.setFocusable(b);
        }
        super.setFocusable(b);
    }

    /**
     * @inheritDoc
     */
    public void setFocus(boolean b) {
        if(tabsList != null) {
            tabsList.setFocus(b);
        }
    }
    

    /**
     * @inheritDoc
     */
    public boolean hasFocus() {
        if(tabsList != null) {
            return tabsList.hasFocus();
        }
        return super.hasFocus();
    }
    /**
     * This method adds a listener to the tabs.
     * 
     * @param listener a selection listener to gets the selection
     * events
     */
    public void addTabsListener(SelectionListener listener){
        tabsList.addSelectionListener(listener);
    }
    
    /**
     * @inheritDoc
     */
    public void requestFocus() {
        tabsList.requestFocus();
    }
    
    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize() {
        int maxContentW = 0;
        int maxContentH = 0;
        int maxW = 0;
        int maxH = 0;

        for (int i = 0; i < tabsList.size(); i++) {
            Component tabsComp = (Component) tabsList.getModel().getItemAt(i);
            Component contentComp = (Component) tabsTable.get(tabsComp);

            if (contentComp.getPreferredW() > maxContentW) {
                maxContentW = contentComp.getPreferredW();
            }
            if (contentComp.getPreferredH() > maxContentH) {
                maxContentH = contentComp.getPreferredH();
            }
        }
        if (tabPlacement == TOP || tabPlacement == BOTTOM) {
            maxW = maxContentW;
            maxH = tabsList.getPreferredH() + maxContentH;
        } else {
            maxW = tabsList.getPreferredW() + maxContentW;
            maxH = maxContentH;
        }
        return new Dimension(maxW, maxH);
    }

    /**
     * Sets the tab placement for this tabbedpane.
     * Possible values are:<ul>
     * <li><code>Component.TOP</code>
     * <li><code>Component.BOTTOM</code>
     * <li><code>Component.LEFT</code>
     * <li><code>Component.RIGHT</code>
     * </ul>
     * The default value, if not set, is <code>Component.TOP</code>.
     * 
     * @param tabPlacement the placement for the tabs relative to the content
     */
    public void setTabPlacement(int tabPlacement) {
        if (tabPlacement != TOP && tabPlacement != LEFT &&
                tabPlacement != BOTTOM && tabPlacement != RIGHT) {
            throw new IllegalArgumentException("illegal tab placement: must be TOP, BOTTOM, LEFT, or RIGHT");
        }
        if (this.tabPlacement == tabPlacement) {
            return;
        }
        this.tabPlacement = tabPlacement;
        removeComponent(tabsList);
        
        if (tabPlacement == TOP || tabPlacement == BOTTOM) {
            tabsList.setOrientation(List.HORIZONTAL);
            if (tabPlacement == TOP) {
                super.addComponent(BorderLayout.NORTH, tabsList);
            } else if (tabPlacement == BOTTOM) {
                super.addComponent(BorderLayout.SOUTH, tabsList);
            }
        } else {// LEFT Or RIGHT
            tabsList.setOrientation(List.VERTICAL);
            if (tabPlacement == LEFT) {
                super.addComponent(BorderLayout.WEST, tabsList);
            } else {// RIGHT
                super.addComponent(BorderLayout.EAST, tabsList);
            }
        }
        tabsList.setShouldCalcPreferredSize(true);
        contentPane.setShouldCalcPreferredSize(true);

        revalidate();
    }

    /**
     * Adds a <code>component</code> 
     * represented by a <code>title</code> and/or <code>icon</code>,
     * either of which can be <code>null</code>.
     * Cover method for <code>insertTab</code>.
     * 
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     * 
     * @see #insertTab
     * @see #removeTabAt
     */
    public void addTab(String title, Image icon, Component component) {
        insertTab(title, icon, component, tabsList.size());
    }

    /**
     * Adds a <code>component</code> 
     * represented by a <code>title</code> and no <code>icon</code>.
     * Cover method for <code>insertTab</code>.
     * 
     * @param title the title to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     * 
     * @see #insertTab
     * @see #removeTabAt
     */
    public void addTab(String title, Component component) {
        insertTab(title, null, component, tabsList.size());
    }

    /**
     * Inserts a <code>component</code>, at <code>index</code>,
     * represented by a <code>title</code> and/or <code>icon</code>,
     * either of which may be <code>null</code>.
     * Uses java.util.Vector internally, see <code>insertElementAt</code>
     * for details of insertion conventions. 
     *
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component The component to be displayed when this tab is clicked.
     * @param index the position to insert this new tab 
     *
     * @see #addTab
     * @see #removeTabAt
     */
    public void insertTab(String title, Image icon, Component component,
            int index) {
        checkIndex(index);
        if (component == null) {
            return;
        }
        Button b = new Button(title != null ? title : "", icon);
        ((DefaultListModel) tabsList.getModel()).addItemAtIndex(b, index);
        tabsTable.put(b, component);

        if (tabsList.size() == 1) {
            contentPane.addComponent(BorderLayout.CENTER, component);
        }

    }
    
    /**
     * Updates the information about the tab details
     * 
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param index the position to insert this new tab 
     */
    public void setTabTitle(String title, Image icon, int index) {
        checkIndex(index);
        Button b = (Button)tabsList.getModel().getItemAt(index);
        b.setText(title);
        b.setIcon(icon);
        ((DefaultListModel) tabsList.getModel()).setItem(index, b);
    }
    
    /**
     * Removes the tab at <code>index</code>.
     * After the component associated with <code>index</code> is removed,
     * its visibility is reset to true to ensure it will be visible
     * if added to other containers.
     * @param index the index of the tab to be removed
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     *
     * @see #addTab
     * @see #insertTab  
     */
    public void removeTabAt(int index) {
        checkIndex(index);

        Object key = tabsList.getModel().getItemAt(index);
        ((DefaultListModel) tabsList.getModel()).removeItem(index);
        tabsTable.remove(key);
    }

    /**
     * Returns the tab at <code>index</code>.
     * 
     * @param index the index of the tab to be removed
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     * @return the component at the given tab location
     * @see #addTab
     * @see #insertTab  
     */
    public Component getTabComponentAt(int index) {
        checkIndex(index);

        return (Component) tabsTable.get(((Component) tabsList.getModel().getItemAt(index)));
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index > tabsList.size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * Returns the index of the tab for the specified component.
     * Returns -1 if there is no tab for this component.
     *
     * @param component the component for the tab
     * @return the first tab which matches this component, or -1
     *		if there is no tab for this component
     */
    public int indexOfComponent(Component component) {
        for (int i = 0; i < getTabCount(); i++) {
            Component c = (Component) tabsList.getModel().getItemAt(i);
            Component content = (Component) tabsTable.get(c);
                
            if(component.equals(content)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the number of tabs in this <code>tabbedpane</code>.
     *
     * @return an integer specifying the number of tabbed pages
     */
    public int getTabCount() {
        return tabsList.size();
    }

    /**
     * Returns the currently selected index for this tabbedpane.
     * Returns -1 if there is no currently selected tab.
     *
     * @return the index of the selected tab
     */
    public int getSelectedIndex() {
        return tabsList.getSelectedIndex();
    }

    /**
     * The prototype is optionally used in calculating the size of an individual
     * tab and is recommended for performance reasons. You should invoke it with a String
     * representing the width/height which will be used to calculate
     * the size required for each element in the list.\
     * <p>This operation is not essential and if it isn't invoked the size of the first
     * few tabs is used to determine the overall size of a tab.
     * <p>This allows the size calculations to work across look and feels and allows
     * developers to predetermin size for the tabs. 
     * <p>e.g. For tabs which you would like to always be 5 characters wide
     * you can use a prototype "XXXXX" which would use the preferred size of the XXXXX 
     * String to determine the size of the tabs..
     * 
     * @param title a string to determine the size.
     */
    public void setTabTitlePrototype(String title) {
        tabsList.setRenderingPrototype(title);
    }
    
    /**
     * @inheritDoc
     */
    public String toString() {
        String className = getClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return className + "[x=" + getX() + " y=" + getY() + " width=" +
                getWidth() + " height=" + getHeight() + ", tab placement = " +
                tabPlacement + ", tab count = " + getTabCount() +
                ", selected index = " + getSelectedIndex() + "]";
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        super.paint(g);
        UIManager.getInstance().getLookAndFeel().drawTabbedPane(g, this);
    }

    /**
     * Sets the tabs selected style
     * @param style
     */
    public void setTabsSelectedStyle(Style style) {
        tabsList.setSelectedStyle(style);
    }

    /**
     * Sets the tabs un selected style
     * @param unselectedStyle the style
     */
    public void setTabsUnselectedStyle(Style unselectedStyle) {
        tabsList.setUnselectedStyle(unselectedStyle);
    }
    
    /**
     * Sets the content pane selected style
     * @param style
     */
    public void setContentPaneSelectedStyle(Style style) {
        contentPane.setSelectedStyle(style);
    }

    /**
     * Sets the content pane un selected style
     * @param unselectedStyle the style
     */
    public void setContentPaneUnselectedStyle(Style unselectedStyle) {
        contentPane.setUnselectedStyle(unselectedStyle);
    }
    
    /**
     * @inheritDoc
     */
    public void refreshTheme() {
        Painter p = contentPane.getStyle().getBgPainter();
        super.refreshTheme();
        contentPane.getStyle().setBgPainter(p);
        Enumeration e = tabsTable.elements();
        while(e.hasMoreElements()) {
            Component c = (Component)e.nextElement();
            c.refreshTheme();
        }
    }
    
    /**
     * Returns the placement of the tabs for this tabbedpane.
     * 
     * @return the tab placement value
     * @see #setTabPlacement
     */
    public int getTabPlacement() {
        return tabPlacement;
    }

    /**
     * The TabbedPane surrounded border width
     * 
     * @return The TabbedPane surrounded border width
     */
    public int getTabbedPaneBorderWidth() {
        return tPBorder;
    }

    /**
     * Setting the TabbedPane surrounded border width
     * 
     * @param tabbedPaneBorderWidth TabbedPane surrounded border width
     */
    public void setTabbedPaneBorderWidth(int tabbedPaneBorderWidth) {
        this.tPBorder = tabbedPaneBorderWidth;
    }

    /**
     * @inheritDoc
     */
    public void setPadding(int top, int bottom, int left, int right) {
        if (contentPane != null) {
            contentPane.getStyle().setPadding(top, bottom, left, right);
        }
    }
    
    /**
     * @inheritDoc
     */
    public void setPadding(int orientation, int gap) {
        if (contentPane != null) {
            contentPane.getStyle().setPadding(orientation, gap);
        }
    }
    
    /**
     * Sets the selected index for this tabbedpane. The index must be a valid 
     * tab index.
     * @param index the index to be selected
     * @throws IndexOutOfBoundsException if index is out of range 
     * (index < 0 || index >= tab count)
     */
    public void setSelectedIndex(int index) {
        if (index < 0 || index >= tabsList.size()) {
            throw new IndexOutOfBoundsException("Index: "+index+", Tab count: "+tabsList.size());
        }
        tabsList.setSelectedIndex(index);
    }
    
    class TabsRenderer implements ListCellRenderer {

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {            
            // prototype value can cause this
            if(value == null || (!(value instanceof Button))) {
                value = new Button("" + value);
            }
            return UIManager.getInstance().getLookAndFeel().getTabbedPaneCell(
                    TabbedPane.this, ((Button) value).getText(),
                    ((Button) value).getIcon(), isSelected,
                    list.hasFocus(), list.getStyle(), list.getSelectedStyle(), TabbedPane.this.getStyle(),
                    list.getScrollX(), list.getScrollY(),
                    list.getPreferredSize(), contentPane.getBounds().getSize());
        }

        public Component getListFocusComponent(List list) {
            return null;
        }
    }
}
