package com.nokia.lwuit.components;


import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.layouts.GridLayout;

/**
 * ToggleButton is a {@link Container} that maintains a selection
 * of {@link ToggleButtonItem}s
 */
public class ToggleButton extends Container {
    private static int ITEM_ROW_COUNT = 1;
    private static int MAX_ITEMS = 2;
    
    private ButtonGroup group = null;
   
   /**
     * Creating an empty ToggleButton is forbidden
     */
    private ToggleButton() {
        // nada
    }

    /**
     * Constructs a ToggleButton with the given ToggleButtonItems
     * 
     * @param leftItem item of the left hand side
     * @param rightItem item of the right hand side
     * @param leftItemSelected should left item be selected initially
     */
    public ToggleButton(
            ToggleButtonItem leftItem,
            ToggleButtonItem rightItem,
            boolean leftItemSelected) {
        
        if ((leftItem == null) || (rightItem == null)) {
            throw new IllegalArgumentException("Invalid ToggleButtonItem.");
        }
        setUIID("ToggleButton");
        
        this.group = new ButtonGroup();
        leftItem.setToggle(true);
        rightItem.setToggle(true);

        leftItem.setSelected(leftItemSelected);
        rightItem.setSelected(!leftItemSelected);
        
        group.add(leftItem);
        group.add(rightItem);      
        
        this.addComponent(leftItem);
        this.addComponent(rightItem);
        
        this.setLayout(new GridLayout(ITEM_ROW_COUNT, MAX_ITEMS));    
    }
    
    /**
     * Returns button group (selection model)
     * 
     * @return button group
     */
    public ButtonGroup getButtonGroup() {
        return this.group;
    }
    
}
