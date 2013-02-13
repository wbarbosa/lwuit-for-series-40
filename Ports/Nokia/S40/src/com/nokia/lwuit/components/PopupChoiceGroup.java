/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.components;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.Layout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * NOT USABLE YET
 * @author tkor
 */
public class PopupChoiceGroup extends Container{
    
    private ButtonGroup mGroup;
    private RadioButton[] mButtons;
    private GroupContainer mGroupContainer;
    
    private Label mSelection;
    
    private final int OPEN = 0;
    private final int CLOSED = 1;
    
    private int mCurrentState = CLOSED;
    
    private Image mArrowOpenImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupOpenArrowImage");
    private Image mArrowClosedImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupClosedArrowImage");
    private Image mArrowClosedPressedImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupPressedArrowImage");
    
    public PopupChoiceGroup(String title, RadioButton[] choices) {
        super();
        setUIID("PopupChoiceGroup");
        mButtons = choices;
        mGroup = new ButtonGroup();
        mGroupContainer = new GroupContainer(mButtons);
        mGroupContainer.getStyle().setBgColor(0x000000);
        mGroupContainer.getStyle().setBgTransparency(255);
        mSelection = new Label();
        mSelection.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        //add choicelistener
        ActionListener choicelistener = new ActionListener() { 

            public void actionPerformed(ActionEvent evt) {
                RadioButton rb = (RadioButton)evt.getComponent();
                mSelection.setText(rb.getText());
                if(mCurrentState == OPEN) {
                    removeChoices();
                    
                }
            }
            
        };
        //add choices to group
        int l = choices.length;
        for(int i = 0; i < l; i++) {
            choices[i].addActionListener(choicelistener);
            mGroup.add(choices[i]);
        }
        //create the toparea that will open the selections
        Button b = new Button(title) {
            private Image mCurrentArrow = mArrowClosedImage;
            public void paint(Graphics g) {
                System.out.println("button state:" + getState());
                if(getState() == Button.STATE_PRESSED) {
                    System.out.println("setting pressed arrow");
                    mCurrentArrow = mArrowClosedPressedImage;
                }else {
                    mCurrentArrow = mArrowClosedImage;
                }
                super.paint(g);
                int x = getX();
                int y = getY();
                int rightpadding = getStyle().getPadding(Component.RIGHT);
                x = x + getWidth() - rightpadding - mCurrentArrow.getWidth();
                y += (getHeight() / 2) - (mCurrentArrow.getHeight() / 2);
                g.drawImage(mCurrentArrow, x, y);
                
            }
        };
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if(mCurrentState == CLOSED) {
                    addChoices();
                }else {
                    removeChoices();
                }
                
            }
        });
        b.setUIID("Label");
        b.getStyle().setBgTransparency(0);
        b.getPressedStyle().setBgTransparency(0);
        b.getSelectedStyle().setBgTransparency(0);
        setScrollable(false);
        //add everything to container
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(b);
        addComponent(mSelection);
    }
    
    private void removeChoices() {
        int l = mButtons.length;
        for (int i = 0; i < l; i++) {
            removeComponent(mGroupContainer);
        }
        mCurrentState = CLOSED;
        revalidate();
    }

    private void addChoices() {
        int l = mButtons.length;
        addComponent(mGroupContainer);
        
        mCurrentState = OPEN;
        revalidate();
    }
    
    private static class GroupContainer extends Container {
        
        private RadioButton[] mButtons;
        private int height = 0;
        
        public GroupContainer(RadioButton[] buttons) {
            super();
            mButtons = buttons;
            height = 0;
            if(mButtons != null) {
                if(mButtons.length >= 3) {
                    height = mButtons[0].getPreferredH() * 3;
                }else {
                    height = mButtons[0].getPreferredH() * mButtons.length;
                }
            }
            setPreferredH(height);
            System.out.println("buttons:" + mButtons.length);
            
            setLayout(new SimpleLayout());
            for(int i = 0; i < buttons.length; i++) {
                addComponent(buttons[i]);
            }
            
            setScrollableY(true);
            

        }

        protected Dimension calcScrollSize() {
            return super.calcPreferredSize();
        }
        
        /**
         * Basically quite the same as boxlayout is, but there some problems with
         * how boxlayout determines the size and for backwards compatability we don't
         * want to modify the boxlayout so instead we create a new one with just the
         * modifications we need for this component.
         */
        private static class SimpleLayout extends Layout {

            public void layoutContainer(Container parent) {
                int width = parent.getLayoutWidth() - parent.getSideGap() - parent.getStyle().getPadding(false, Component.RIGHT) - parent.getStyle().getPadding(false, Component.LEFT);
                int height = parent.getLayoutHeight() - parent.getBottomGap() - parent.getStyle().getPadding(false, Component.BOTTOM) - parent.getStyle().getPadding(false, Component.TOP);
                int x = parent.getStyle().getPadding(parent.isRTL(), Component.LEFT);
                int y = parent.getStyle().getPadding(false, Component.TOP);
                int numOfcomponents = parent.getComponentCount();

                boolean rtl = parent.isRTL();
                if (rtl) {
                    x += parent.getSideGap();
                }

                for (int i = 0; i < numOfcomponents; i++) {
                    Component cmp = parent.getComponentAt(i);
                    Style stl = cmp.getStyle();


                    int cmpBottom = height;
                    int cmpH = cmp.getPreferredH();

                    y += stl.getMargin(false, Component.TOP);

                    cmp.setWidth(width - stl.getMargin(parent.isRTL(), Component.LEFT) - stl.getMargin(parent.isRTL(), Component.RIGHT));
                    cmp.setHeight(cmpH);
                    cmp.setX(x + stl.getMargin(parent.isRTL(), Component.LEFT));
                    cmp.setY(y);
                    y += cmp.getHeight() + stl.getMargin(false, Component.BOTTOM);

                }
            }

            public Dimension getPreferredSize(Container parent) {
                int height = 0;
                int width = 0;

                int numOfcomponents = parent.getComponentCount();
                for (int i = 0; i < numOfcomponents; i++) {
                    Component cmp = parent.getComponentAt(i);
                    Style stl = cmp.getStyle();
                    int cmpH = cmp.getPreferredH() + stl.getMargin(false, Component.TOP) + stl.getMargin(false, Component.BOTTOM);
                    height += cmpH;
                    width = Math.max(width, cmp.getPreferredW() + stl.getMargin(false, Component.LEFT) + stl.getMargin(false, Component.RIGHT));

                }
                Dimension d = new Dimension(width + parent.getStyle().getPadding(false, Component.LEFT) + parent.getStyle().getPadding(false, Component.RIGHT),
                        height + parent.getStyle().getPadding(false, Component.TOP) + parent.getStyle().getPadding(false, Component.BOTTOM));
                return d;
            }
        }
        
        
    }
    
}