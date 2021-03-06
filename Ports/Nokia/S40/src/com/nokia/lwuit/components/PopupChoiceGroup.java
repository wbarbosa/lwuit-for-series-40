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
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * A PopupChoiceGroup is similar component to the full touch platform PopupChoiceGroup
 * component. It can be used to provide more platform look and feel to application
 * as a replacement of the ComboBox component. The component opens up to show selectable
 * radiobuttons when the user presses the component. The component closes when another press
 * is done over the top area of the component.
 * @author tkor
 */
public class PopupChoiceGroup extends Container{
    
    private ButtonGroup mGroup;
    private RadioButton[] mButtons;
    private GroupContainer mGroupContainer;
    
    private Button mOpenButton;
    
    private Label mSelection;
    
    private final int OPEN = 0;
    private final int CLOSED = 1;
    
    private int mCurrentState = CLOSED;
    
    private Border mClosedBorder;
    private Border mPressedBorder;
    private Border mOpenBorder;
    
    private Image mArrowOpenImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupOpenArrowImage");
    private Image mArrowClosedImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupClosedArrowImage");
    private Image mArrowClosedPressedImage = UIManager.getInstance().getThemeImageConstant("PopupChoiceGroupPressedArrowImage");
    
    /**
     * Creates a new PopupChoiceGroup with the given title and choices.
     * @param title The title of the group
     * @param choices choices a RadioButton array of the choices
     */
    public PopupChoiceGroup(String title, RadioButton[] choices) {
        super();
        if(choices == null || choices.length == 0) {
            throw new IllegalArgumentException("choices can't be null or empty");
        }
        setUIID("PopupChoiceGroup");
        
        mButtons = choices;
        mGroup = new ButtonGroup();
        mGroupContainer = new GroupContainer(mButtons);
        mGroupContainer.getStyle().setBgColor(0x000000);
        mGroupContainer.getStyle().setBgTransparency(255);
        mGroupContainer.getStyle().setMargin(Component.LEFT, 0);
        mGroupContainer.getStyle().setMargin(Component.RIGHT, 0);
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
        mGroup.getRadioButton(0).setSelected(true);
        mSelection.setText(mGroup.getRadioButton(0).getText());
        
        //create the toparea that will open the selections
        mOpenButton = new Button(title) {
            
            private Image mCurrentArrow = mArrowClosedImage;
            
            public void paint(Graphics g) {
                if(getState() == Button.STATE_PRESSED) {
                    mCurrentArrow = mArrowClosedPressedImage;
                    PopupChoiceGroup.this.repaint();
                }else {
                    mCurrentArrow = mArrowClosedImage;
                }
                
                if(mCurrentState == OPEN) {
                    mCurrentArrow = mArrowOpenImage;
                }
                super.paint(g);
                int x = getX();
                int y = getY();
                int rightpadding = getStyle().getPadding(Component.RIGHT);
                x = x + getWidth() - rightpadding - mCurrentArrow.getWidth();
                y += (getHeight() / 2) - (mCurrentArrow.getHeight() / 2);
                g.drawImage(mCurrentArrow, x, y);
                
            }

            public void pressed() {
                super.pressed();
                //this is done so that the pressed style is repainted immediately for the parent container
                PopupChoiceGroup.this.repaint();
            }
            
            
        };
        mOpenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if(mCurrentState == CLOSED) {
                    addChoices();
                }else {
                    removeChoices();
                }
                
            }
        });
        mOpenButton.setUIID("Label");
        mOpenButton.getStyle().setBgTransparency(0);
        mOpenButton.getPressedStyle().setBgTransparency(0);
        mOpenButton.getSelectedStyle().setBgTransparency(0);
        setScrollable(false);
        //add everything to container
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addComponent(mOpenButton);
        addComponent(mSelection);
        
        Style openStyle = UIManager.getInstance().getComponentStyle("PopupChoiceGroupOpen");
        mOpenBorder = openStyle.getBorder();
        mClosedBorder = getUnselectedStyle().getBorder();
        mPressedBorder = getPressedStyle().getBorder();
    }
    
    private void removeChoices() {
        getStyle().setPadding(Component.BOTTOM, 2);
        int l = mButtons.length;
        for (int i = 0; i < l; i++) {
            removeComponent(mGroupContainer);
        }
        mCurrentState = CLOSED;
        getStyle().setBorder(mClosedBorder);
        revalidate();
    }

    private void addChoices() {
        getStyle().setPadding(Component.BOTTOM, 5);
        getStyle().setBorder(mOpenBorder);
        int l = mButtons.length;
        addComponent(mGroupContainer);
        
        mCurrentState = OPEN;
        
        revalidate();
    }

    /**
     * @inheritDoc
     */
    public Style getStyle() {
        if(mOpenButton.getState() == Button.STATE_PRESSED) {
            
            return getPressedStyle();
        }
        return super.getStyle();
    }
    
    /**
     * get currently selected radiobutton index.
     * @return the selected index integer from 0 to radiobutton array length-1
     */
    public int getSelectedIndex() {
        return mGroup.getSelectedIndex();
    }
    /**
     * Get the selected RadioButton component.
     * @return selected RadioButton object.
     */
    public RadioButton getSelected() {
        return mGroup.getRadioButton(mGroup.getSelectedIndex());
    }
    /**
     * Set selected radiobutton.
     * @param index index of the radiobutton in zero based array (0...array.length-1)
     */
    public void setSelected(int index) {
        if(index >= mGroup.getButtonCount()) {
            throw new IllegalArgumentException("index must be less than length of radiobuttons");
        }
        mGroup.getRadioButton(index).setSelected(true);
        mSelection.setText(mGroup.getRadioButton(index).getText());
    }
    
    /**
     * Basically the same as Container with the exception of height being only maximum
     * of 3 * radiobutton height or less if under 3 components in container.
     */
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
            
            setLayout(new SimpleLayout());
            for(int i = 0; i < buttons.length; i++) {
                addComponent(buttons[i]);
            }
            
            setScrollableY(true);
            

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