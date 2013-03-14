package com.nokia.lwuit.components;

import com.sun.lwuit.*;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.UIManager;

/**
 * ToggleButtonItem is a {@link RadioButton} that maintains a selection state
 * using a {@link ButtonGroup}
 */
public class ToggleButtonItem extends RadioButton {

    /**
     * Constructs a ToggleButtonItem with the given text
     * 
     * @param text to display next to the button
     * @param alignment left or right
     */
    public ToggleButtonItem(String text, int alignment) {
        this(text, null, alignment);
    }
    
    /**
     * Creating an empty ToggleButtonItem is forbidden
     */
    private ToggleButtonItem() {
        // nada
    }
    
    /**
     * Constructs a ToggleButtonItem with the given icon
     * 
     * @param icon icon to show next to the button
     * @param alignment left or right
     */
    public ToggleButtonItem(Image icon, int alignment) {
        this("", icon, alignment);
    }

    /**
     * Constructs a ToggleButtonItem with the given text and icon
     * 
     * @param text to display next to the button
     * @param icon icon to show next to the button
     * @param alignment left or right
     */
    public ToggleButtonItem(String text, Image icon, int alignment) {
        super(text, icon);
        
        switch(alignment) {
            case Component.LEFT:
                setUIID("ToggleButtonItemLeft");
                break;
            case Component.RIGHT:
                setUIID("ToggleButtonItemRight");
                break;
            default:
                throw new RuntimeException("Unsupported alignment");
        }       
    }
    
    /**
     * @inheritDoc
     */
    public String toString() {
        return "Toggle Button Item" + getText();
    }
    
    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        UIManager.getInstance().getLookAndFeel().drawToggleButton(g, this); 
    }
    
    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize(){
        return UIManager.getInstance().getLookAndFeel().getToggleButtonPreferredSize(this);
    }   

    /**
     * @inheritDoc
     */
    public void refreshTheme() {
        super.refreshTheme();
    }
}