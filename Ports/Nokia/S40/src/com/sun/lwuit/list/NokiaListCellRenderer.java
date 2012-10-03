/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.list;

import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.List;

/**
 *
 * @author tkor
 */
public class NokiaListCellRenderer extends Component implements ListCellRenderer{
    
    private NokiaListCellRenderer mFocusComponent;
    
    public NokiaListCellRenderer() {
        setUIID("ListRenderer");
    }

    public void paint(Graphics g) {
        
    }

    /**
     * override to prevent unnecessary repaints when using list
     */
    public void repaint() {
    }
    
    

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        
        
        return this;
    }

    public Component getListFocusComponent(List list) {
        if(mFocusComponent == null) {
            mFocusComponent = new NokiaListCellRenderer();
            mFocusComponent.setUIID("ListRendererFocus");
            mFocusComponent.setFocus(true);
        }
    }
    
}
