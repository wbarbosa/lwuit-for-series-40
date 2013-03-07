package com.sun.lwuit;

import com.sun.lwuit.layouts.GridLayout;

/**
 * SegmentedButton is a {@link Container} that maintains a selection
 * of {@link SegmentedButtonItem}s
 */
public class SegmentedButton extends Container {
    
    private ButtonGroup group = null;
   
   /**
     * Creating an empty segmented button is forbidden.
     */
    private SegmentedButton() {
        // nada
    }

    /**
     * Constructs a segmented button with the given items in the given
     * toggle mode
     * 
     * @param items to be incorporated
     * @param toggleMode toggle/selection mode
     */
    public SegmentedButton(SegmentedButtonItem[] items, boolean toggleMode) {
        int itemsCount = items.length;
        
        if ((items == null) || (itemsCount == 0)) {
            throw new RuntimeException("No SegmentedButtonItems.");
        }
        
        this.group = new ButtonGroup();
        
        for (int i=0; i<itemsCount;i++){
            SegmentedButtonItem sb = items[i];
            this.addComponent(sb);
            group.add(sb);
            sb.setTickerEnabled(false);
            sb.setButtonGroup(group);
            sb.setToggle(toggleMode);
        }
        
        this.setLayout(new GridLayout(1, itemsCount));
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
