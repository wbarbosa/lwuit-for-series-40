package com.nokia.lwuit.components;

import com.sun.lwuit.*;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.UIManager;

/**
 * DualButtonItem is a {@link Button} that does not maintain a selection state
 */
public class DualButtonItem extends Button {

    /**
     * Constructs a DualButtonItem with the given text
     * 
     * @param text to display next to the button
     * @param alignment left or right
     */
    public DualButtonItem(String text, int alignment) {
        this(text, null, alignment);
    }
    
    /**
     * Creating an empty DualButtonItem is forbidden
     */
    private DualButtonItem() {
        // nada
    }
    
    /**
     * Constructs a DualButtonItem with the given icon
     * 
     * @param icon icon to show next to the button
     * @param alignment left or right
     */
    public DualButtonItem(Image icon, int alignment) {
        this("", icon, alignment);
    }

    /**
     * Constructs a DualButtonItem with the given text and icon
     * 
     * @param text to display next to the button
     * @param icon icon to show next to the button
     * @param alignment left or right
     */
    public DualButtonItem(String text, Image icon, int alignment) {
        super(text, icon);
        
        switch(alignment) {
            case Component.LEFT:
                setUIID("DualButtonItemLeft");
                break;
            case Component.RIGHT:
                setUIID("DualButtonItemRight");
                break;
            default:
                throw new RuntimeException("Unsupported alignment");
        }       
    }
    
    /**
     * @inheritDoc
     */
    public String toString() {
        return "Dual Button Item" + getText();
    }
    
    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        UIManager.getInstance().getLookAndFeel().drawDualButton(g, this); 
    }
    
    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize(){
        return UIManager.getInstance().getLookAndFeel().getDualButtonPreferredSize(this);
    }   

    /**
     * @inheritDoc
     */
    public void refreshTheme() {
        super.refreshTheme();
    }
}