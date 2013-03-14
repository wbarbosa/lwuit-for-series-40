package com.nokia.lwuit.components;

import com.sun.lwuit.Container;
import com.sun.lwuit.layouts.GridLayout;

/**
 * DualButton is a {@link Container} that maintains a selection
 * of {@link DualButtonItem}s
 */
public class DualButton extends Container {
    private static int ITEM_ROW_COUNT = 1;
    private static int MAX_ITEMS = 2;
   
   /**
     * Creating an empty DualButton is forbidden
     */
    private DualButton() {
        // nada
    }

    /**
     * Constructs a DualButton with the given items
     * 
     * @param leftItem item of the left hand side
     * @param rightItem item of the right hand side
     */
    public DualButton(DualButtonItem leftItem, DualButtonItem rightItem) {
        
        if ((leftItem == null) || (rightItem == null)) {
            throw new RuntimeException("Invalid DualButtonItem.");
        }
        
        leftItem.setToggle(false);
        rightItem.setToggle(false);
        this.addComponent(leftItem);
        this.addComponent(rightItem);
        
        this.setLayout(new GridLayout(ITEM_ROW_COUNT, MAX_ITEMS));
    }     
}
