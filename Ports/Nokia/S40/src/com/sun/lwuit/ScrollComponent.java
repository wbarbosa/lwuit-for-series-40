package com.sun.lwuit;

import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;

/**
 * A Component that is used 
 * @author tkor
 */
public class ScrollComponent extends Component{
    
    public ScrollComponent() {
        
    }

    public void paint(Graphics g) {
        //do nothing 
    }
    
    

    protected Dimension calcPreferredSize() {
        Dimension d = new Dimension();
        Style s = getStyle();
        d.setHeight(s.getBorder().getMinimumHeight());
        d.setWidth(s.getBorder().getMinimumWidth());
        return d;
    }
    
    
    
    
}
