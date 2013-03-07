package com.sun.lwuit;

import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.UIManager;

/**
 * SegmentedButtonItem is a {@link Button} that maintains a selection state exclusively
 * within a specific {@link ButtonGroup}
 */
public class SegmentedButtonItem extends RadioButton {

    private boolean selected = false;
    private ButtonGroup group;

    /**
     * Constructs a segmented button item with the given text
     * 
     * @param text to display next to the button
     */
    public SegmentedButtonItem(String text, int alignment) {
        this(text, null, alignment);
    }
    
    /**
     * Creating an empty segmented button item is forbidden.
     */
    private SegmentedButtonItem() {
        // nada
    }
    
    /**
     * Constructs a segmented button item with the given icon
     * 
     * @param icon icon to show next to the button
     */
    public SegmentedButtonItem(Image icon, int alignment) {
        this("", icon, alignment);
    }

    /**
     * Constructs a segmented button item with the given text and icon
     * 
     * @param text to display next to the button
     * @param icon icon to show next to the button
     */
    public SegmentedButtonItem(String text, Image icon, int alignment) {
        super(text, icon);
        
        switch(alignment) {
            case Component.CENTER:
                setUIID("SegmentedButtonItemCenter");
                break;
            case Component.LEFT:
                setUIID("SegmentedButtonItemLeft");
                break;
            case Component.RIGHT:
                setUIID("SegmentedButtonItemRight");
                break;
            default:
                throw new RuntimeException("Unsupported alignment");
        }       
    }
    
    /**
     * @inheritDoc
     */
    public String toString() {
        return "Segmented Button Item" + getText();
    }
   
    /**
     * Returns true if the segmented button item is selected
     * 
     * @return true if the segmented buttom item is selected
     */
    public boolean isSelected() {
        return selected;
    }

    void setSelectedImpl(boolean selected) {
        this.selected = selected;
        repaint();
    }
    
    /**
     * Selects the current segmented buttom item
     * 
     * @param selected value for selection
     */
    public void setSelected(boolean selected) {
        setSelectedImpl(selected);
        if(group != null && selected) {
            group.setSelected(this);
        } 
    }
    
    /**
     * @inheritDoc
     */
    public void released(int x, int y) {
        // prevent the segmented button item from being "turned off"
        if (isToggle()) {
            if (!isSelected()) {
                setSelected(true);
            }
        }     
        super.released(x, y);
    }
    
    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        UIManager.getInstance().getLookAndFeel().drawSegmentedButton(g, this);      
    }
    
    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize(){
        return UIManager.getInstance().getLookAndFeel().getSegmentedButtonPreferredSize(this);
    }
    
    /**
     * Setting a new button group
     * 
     * @param group a new button group
     */  
    public void setButtonGroup(ButtonGroup group) {
        this.group = group;
    }
    
    /**
     * @inheritDoc
     */
    void fireActionEvent() {        
        if(group != null) {
            group.setSelected(this);
        }      
        super.fireActionEvent();
    }

    /**
     * @inheritDoc
     */
    public void refreshTheme() {
        super.refreshTheme();
    }
}