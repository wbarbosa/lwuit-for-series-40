/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.components;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.DefaultLookAndFeel;
import com.sun.lwuit.plaf.LookAndFeel;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author tkor
 */
public class ContextMenu extends Dialog implements ActionListener{
    
    private List mList;
    private ContextMenuListener mListener;
    private List mParentList;
    
    
    private boolean skipRelease = false;
    
    public ContextMenu(List parentList) {
        super("", "");
        getContentPane().setUIID("ContextMenu");
        mParentList = parentList;
        setDisposeWhenPointerOutOfBounds(true);
        setTransitionInAnimator(CommonTransitions.createEmpty());
        setTransitionOutAnimator(CommonTransitions.createEmpty());
        mList = new List();
        mList.addActionListener(this);
        
        setLayout(new BorderLayout());
        getTitleArea().setVisible(false);
        setScrollable(false);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setUIID("TouchCommand");
        mList.setListCellRenderer(renderer);
        renderer.getStyle().setFgColor(0x000000, true);
        getContentPane().getStyle().setMargin(0, 0, 0, 0);
        addComponent(BorderLayout.CENTER, mList);
        skipRelease = true;
        setDisposeOnRotation(true);
       
    }
    
    public void setMenuItems(ListModel model) {
        mList.setModel(model);
    }

    public void actionPerformed(ActionEvent ae) {
        ae.consume();
        
        this.dispose();
        if(mListener != null) {
            mListener.menuItemSelected(mList.getSelectedIndex());
        }
    }
    public void show() {
        Display.getInstance().callSerially(new Runnable() {

            public void run() {
                showImpl();
            }
        });
    }
    private void showImpl() {
        final int MaxAmountOfListItems = (Display.getInstance().isPortrait()) ? 6 : 3;
        skipRelease = true;
        this.getStyle().setPadding(0, 0, 0, 0);
        Component contentPane = super.getContentPane();
        

        int listsize = mList.getModel().getSize();
        
        int itemsToShow = (listsize < MaxAmountOfListItems) ? listsize : MaxAmountOfListItems;
        int menuHeight = itemsToShow*((Component) mParentList.getRenderer()).getPreferredH();
        
        menuHeight += getStyle().getPadding(Component.TOP) + getStyle().getPadding(Component.BOTTOM);
        menuHeight += getContentPane().getStyle().getPadding(Component.TOP) +
                        getContentPane().getStyle().getPadding(Component.BOTTOM);
        
        Border bp = getContentPane().getStyle().getBorder();
        if(bp != null) {
            menuHeight += bp.getMinimumHeight();
        }
        menuHeight += contentPane.getStyle().getPadding(Component.TOP);
        menuHeight += contentPane.getStyle().getPadding(Component.BOTTOM);
        //hide title
        getTitleComponent().setVisible(false);
        
        // allows a text area to recalculate its preferred size if embedded within a dialog
        revalidate();

        Style contentPaneStyle = getContentPane().getStyle();

        boolean restoreArrow = false;
        //set the arrow
        Image t = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowTopImage");
        Image b = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowBottomImage");
        Image l = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowLeftImage");
        Image r = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowRightImage");
        Border border = contentPaneStyle.getBorder();
        if (border != null) {
            System.out.println("setting arrows");
            border.setImageBorderSpecialTile(t, b, l, r, mParentList);
            restoreArrow = true;
        }
        
        int prefHeight = contentPane.getPreferredH();
        int prefWidth = contentPane.getPreferredW();
        if(contentPaneStyle.getBorder() != null) {
            prefWidth = Math.max(contentPaneStyle.getBorder().getMinimumWidth(), prefWidth);
            prefHeight = Math.max(contentPaneStyle.getBorder().getMinimumHeight(), prefHeight);
        }

        Rectangle componentPos = mParentList.getSelectedRect();
        int displayHeight = Display.getInstance().getDisplayHeight();
        int availableWidth = Display.getInstance().getDisplayWidth();
        int x = 0;
        int y = 0;

        x = componentPos.getX();
        y = componentPos.getY();
        if(y + menuHeight > displayHeight) {
            y -= ((y + menuHeight) - displayHeight);
        }
        
        int bottom = displayHeight - (y + menuHeight);
        bottom = (bottom < 0) ? 0 : bottom;

        int left = 0;
        if(availableWidth > displayHeight) {
            left = displayHeight;
        }
        show(y, bottom, x, left, true, true);
        
        if(restoreArrow) {
            contentPaneStyle.getBorder().clearImageBorderSpecialTile();
        }
        
    }
    
    
    
    public void setListener(ContextMenuListener listener) {
        mListener = listener;
    }
    public void pointerReleased(int x, int y) {
        if(!skipRelease) {
            super.pointerReleased(x, y);
        }
    }
    public void pointerDragged(int x, int y) {
        super.pointerDragged(x, y);
        skipRelease = false;
    }
    public void pointerPressed(int x, int y) {
        super.pointerPressed(x, y);
        skipRelease = false;
    }
    
    public static interface ContextMenuListener {
        public void menuItemSelected(int index);
    }
}
