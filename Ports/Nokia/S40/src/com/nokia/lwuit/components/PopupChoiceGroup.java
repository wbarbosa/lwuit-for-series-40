/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.components;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author tkor
 */
public class PopupChoiceGroup extends Container{
    
    private ButtonGroup mGroup;
    private RadioButton[] mButtons;
    private GroupContainer mGroupContainer;
    
    private Label mSelection;
    
    private final int OPEN = 0;
    private final int CLOSED = 1;
    
    private int currentState = CLOSED;
    
    public PopupChoiceGroup(String title, RadioButton[] choices) {
        super();
        mButtons = choices;
        mGroup = new ButtonGroup();
        mGroupContainer = new GroupContainer(mButtons);
        mSelection = new Label();
        mSelection.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        //add choicelistener
        ActionListener choicelistener = new ActionListener() { 

            public void actionPerformed(ActionEvent evt) {
                RadioButton rb = (RadioButton)evt.getComponent();
                mSelection.setText(rb.getText());
                if(currentState == OPEN) {
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
        Button b = new Button(title);
        
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if(currentState == CLOSED) {
                    addChoices();
                }else {
                    removeChoices();
                }
                
            }
        });
        b.setUIID("Label");
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
        currentState = CLOSED;
        revalidate();
    }

    private void addChoices() {
        int l = mButtons.length;
        addComponent(mGroupContainer);
        
        currentState = OPEN;
        revalidate();
    }
    
    private static class GroupContainer extends Container {
        
        private RadioButton[] mButtons;
        private int height = 0;
        
        public GroupContainer(RadioButton[] buttons) {
            super();
            mButtons = buttons;
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            for(int i = 0; i < buttons.length; i++) {
                addComponent(buttons[i]);
            }
            
            setScrollable(true);
            height = 0;
            if(mButtons != null) {
                if(mButtons.length >= 3) {
                    height = mButtons[0].getPreferredH() * 3;
                }else {
                    height = mButtons[0].getPreferredH() * mButtons.length;
                }
            }
            System.out.println("buttons:" + mButtons.length);
            setPreferredH(height);
        }
        
        /*public int getHeight() {
            return height;
        }*/

        protected Dimension calcScrollSize() {
            return super.calcPreferredSize();
        }
        
        
    }
    
}