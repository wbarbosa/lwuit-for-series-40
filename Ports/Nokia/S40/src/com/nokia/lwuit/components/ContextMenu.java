/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.components;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
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
    private final Image mArrow = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowLeftImage");
    
    /**
     * we need to skip the next release event since otherwise it will cause
     * the contextmenu to disappear immediately the user lifts the finger of
     * the screen.
     */
    private boolean skipRelease = false;
    
    public ContextMenu(List parentList) {
        super("", "");
        getContentPane().setUIID("ContextMenu");
        mParentList = parentList;
        setDisposeWhenPointerOutOfBounds(true);
        setTransitionInAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
        setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 500));
        mList = new List();
        mList.addActionListener(this);
        
        setLayout(new BorderLayout());
        getTitleArea().setVisible(false);
        setScrollable(false);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setShowNumbers(false);
        renderer.setUIID("TouchCommand");
        mList.setListCellRenderer(renderer);
        renderer.getStyle().setFgColor(0x000000, true);
        getContentPane().getStyle().setMargin(0, 0, 0, 0);
        
        getContentPane().getStyle().setMargin(Component.LEFT, mArrow.getWidth());
        addComponent(BorderLayout.CENTER, mList);
        skipRelease = true;
        setDisposeOnRotation(true);
        
       
    }

    public void paint(Graphics g) {
        super.paint(g);      
        int y = mList.getAbsoluteY();
        int x = mList.getAbsoluteX() - mArrow.getWidth();
        g.drawImage(mArrow, x, y);
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
        //if called from gestureevent we need to get back to edt, otherwise
        //the app will freeze to the platform thread running the gestureevent.
        Display.getInstance().callSerially(new Runnable() {

            public void run() {
                showImpl();
            }
        });
    }
    private void showImpl() {
        
        final double MaxAmountOfListItems = (Display.getInstance().isPortrait()) ? 6.0 : 3.5;
        skipRelease = true;
        this.getStyle().setPadding(0, 0, 0, 0);
        Component contentPane = super.getContentPane();
        

        int listsize = mList.getModel().getSize();
        
        double itemsToShow = (listsize < MaxAmountOfListItems) ? listsize : MaxAmountOfListItems;
        int menuHeight = (int)(itemsToShow*((Component) mParentList.getRenderer()).getPreferredH());
        
        menuHeight += getStyle().getPadding(Component.TOP) + getStyle().getPadding(Component.BOTTOM);
        menuHeight += getContentPane().getStyle().getPadding(Component.TOP) +
                        getContentPane().getStyle().getPadding(Component.BOTTOM);
        
        Border bp = getContentPane().getStyle().getBorder();
        if(bp != null) {
            menuHeight += bp.getMinimumHeight();
        }
        menuHeight += contentPane.getStyle().getPadding(Component.TOP);
        menuHeight += contentPane.getStyle().getPadding(Component.BOTTOM);
        
        //make sure the components are calculated correctly
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
            border.setImageBorderSpecialTile(t, b, l, r, this);
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

        int right = 0;
        if(availableWidth > displayHeight) {
            right = displayHeight;
        }
        show(y, bottom, x, right, true, true);
        
        /*if(restoreArrow) {
            contentPaneStyle.getBorder().clearImageBorderSpecialTile();
        }*/
        
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
