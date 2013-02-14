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
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * ContextMenu is a component similar to the Full Touch ContextMenu. Developer is 
 * free to show the menu the most suitable way for the application. The menu extends
 * from dialog so the API is very similar. To have the same behavior as the platform
 * menu, the developer should use the LONG_TAP gesture to show the menu. ContextMenu 
 * has a "tail" which is an arrow that points to the parent component similary to the
 * platform menu.
 * 
 * The easiest way to use the menu is to call the static show-method which will return
 * the selected command.
 * @author tkor
 */
public class ContextMenu extends Dialog implements ActionListener{
    
    private List mList;
    private Component mParent;
    private final Image mArrow = UIManager.getInstance().getThemeImageConstant("ContextMenuArrowLeftImage");
    
    /**
     * we need to skip the next release event since otherwise it will cause
     * the contextmenu to disappear immediately the user lifts the finger of
     * the screen.
     */
    private boolean skipRelease = false;
    
    /**
     * Create a new ContextMenu object.
     * @param parent the parent component that will be used to calculate menu position.
     */
    public ContextMenu(Component parent) {
        super("", "");
        getContentPane().setUIID("ContextMenu");
        getContentPane().getParent().getStyle().setBgTransparency(0);
        mParent = parent;
        setDisposeWhenPointerOutOfBounds(true);
        setTransitionInAnimator(CommonTransitions.createEmpty());
        setTransitionOutAnimator(CommonTransitions.createEmpty());
        mList = new List();
        mList.addActionListener(this);
        mList.getStyle().setPadding(0, 0, 0, 0);
        setLayout(new BorderLayout());
        getTitleArea().setVisible(false);
        setScrollable(false);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setShowNumbers(false);
        renderer.setUIID("TouchCommand");
        mList.setListCellRenderer(renderer);
        renderer.getStyle().setFgColor(0x000000, true);
        getContentPane().getStyle().setMargin(0, 0, 0, 0);
        if(mArrow != null) {
            getContentPane().getStyle().setMargin(Component.LEFT, mArrow.getWidth());
        }
        addComponent(BorderLayout.CENTER, mList);
        skipRelease = true;
        setDisposeOnRotation(true);
        
       
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (Display.getInstance().getDeviceType() == Display.FULL_TOUCH_DEVICE) {
            paintArrow(g);
        }
    }
    
    private void paintArrow(Graphics g) {
        Rectangle rect = mParent.getSelectedRect();
        int y = rect.getY();
        if (y < mList.getAbsoluteY()) {
            y = mList.getAbsoluteY();
        } else if (y > (mList.getAbsoluteY() + mList.getHeight() - mArrow.getHeight())) {
            y = mList.getAbsoluteY() + mList.getHeight() - mArrow.getHeight();
        }
        int x = mList.getAbsoluteX() - mArrow.getWidth() - 1;
        g.drawImage(mArrow, x, y);
    }
    
    /**
     * Set menu commands
     * @param model ListModel containing the menu commands
     */
    public void setMenuItems(ListModel model) {
        mList.setModel(model);
    }

    /**
     * @inheritDoc
     */
    public void actionPerformed(ActionEvent ae) {
        ae.consume();
        lastCommandPressed = (Command) mList.getSelectedItem();
        dispose();
    }
    /**
     * show the ContextMenu. Creates a new ContextMenu object and adds the given
     * Commands as menuitems and uses the parent to calculate the position of the
     * new ContextMenu component.
     * @param cmds Commands to be added to the menu
     * @param parent component used to calculate ContextMenu position on the screen.
     * @return selected Command or null if none was selected.
     */
    public static Command show(Command [] cmds, Component parent) {
        ContextMenu ctx = new ContextMenu(parent);
        ctx.setMenuItems(new DefaultListModel(cmds));
        return ctx.showImpl();
    }
    private Command showImpl() {
        
        final double t_and_tLimit = 4.0;
        double MaxAmountOfListItems = (Display.getInstance().isPortrait()) ? 6.0 : 3.5;
        if(Display.getInstance().getDeviceType() == Display.TOUCH_AND_TYPE_DEVICE) {
            MaxAmountOfListItems = t_and_tLimit;
        }
        skipRelease = true;
        this.getStyle().setPadding(0, 0, 0, 0);
        Component contentPane = super.getContentPane();

        int listsize = mList.getModel().getSize();
        double itemsToShow = (listsize < MaxAmountOfListItems) ? listsize : MaxAmountOfListItems;
        int menuHeight = (int)(itemsToShow*((Component) mList.getRenderer()).getPreferredH());
        if(listsize < MaxAmountOfListItems && menuHeight < mList.getScrollDimension().getHeight()) {
            menuHeight = mList.getScrollDimension().getHeight();
        }
        menuHeight += getStyle().getPadding(Component.TOP) + getStyle().getPadding(Component.BOTTOM);
        menuHeight += getContentPane().getStyle().getPadding(Component.TOP) +
                        getContentPane().getStyle().getPadding(Component.BOTTOM);
        
        Border bp = getContentPane().getStyle().getBorder();
        if(bp != null) {
            menuHeight += bp.getMinimumHeight();
        }
        menuHeight += contentPane.getStyle().getPadding(Component.TOP);
        menuHeight += contentPane.getStyle().getPadding(Component.BOTTOM);
        menuHeight += mList.getStyle().getPadding(Component.TOP);
        menuHeight += mList.getStyle().getPadding(Component.BOTTOM);
        menuHeight += mList.getStyle().getMargin(Component.TOP);
        menuHeight += mList.getStyle().getMargin(Component.BOTTOM);
        //make sure the components are calculated correctly
        revalidate();

        Style contentPaneStyle = getContentPane().getStyle();
        
        int prefHeight = contentPane.getPreferredH();
        int prefWidth = contentPane.getPreferredW();
        if(contentPaneStyle.getBorder() != null) {
            prefWidth = Math.max(contentPaneStyle.getBorder().getMinimumWidth(), prefWidth);
            prefHeight = Math.max(contentPaneStyle.getBorder().getMinimumHeight(), prefHeight);
        }

        Rectangle componentPos = mParent.getSelectedRect();
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
        return show(y, bottom, x, right, true, true);
    }
    
    /**
     * @inheritDoc
     */
    public void pointerReleased(int x, int y) {
        if(!skipRelease) {
            super.pointerReleased(x, y);
        }
    }
    /**
     * @inheritDoc
     */
    public void pointerDragged(int x, int y) {
        super.pointerDragged(x, y);
        skipRelease = false;
    }
    /**
     * @inheritDoc
     */
    public void pointerPressed(int x, int y) {
        super.pointerPressed(x, y);
        skipRelease = false;
    }
}
