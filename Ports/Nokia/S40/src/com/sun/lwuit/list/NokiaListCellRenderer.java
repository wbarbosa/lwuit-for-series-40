/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.list;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * A faster version of the Renderer that list component can use. This renderer
 * only supports text and image but has far more simpler way of drawing them compared
 * to Label component that is used in DefaultListCellRenderer
 * @author tkor
 */
public class NokiaListCellRenderer extends Component implements ListCellRenderer{
    
    private NokiaListCellRenderer mFocusComponent;
    
    private String mText = "";
    private Image mImage;
    
    public NokiaListCellRenderer() {
        setUIID("ListRenderer");
        setCellRenderer(true);
    }

    public void paint(Graphics g) {
        UIManager.getInstance().getLookAndFeel().setFG(g, this);
        Style style = getStyle();
        boolean rtl = this.isRTL();
        int leftPadding = style.getPadding(rtl, Component.LEFT);
        int rightPadding = style.getPadding(rtl, Component.RIGHT);
        int topPadding = style.getPadding(false, Component.TOP);
        int bottomPadding = style.getPadding(false, Component.BOTTOM);
        
        Font font = style.getFont();
        int align = style.getAlignment();
        int textWidth = font.stringWidth(mText);
        int width = getWidth();
        int height = getHeight();
        int x;
        int y = getY() + topPadding;

        switch(align) {
            case Component.LEFT:
                x = getX() + leftPadding;
                break;
            case Component.RIGHT:
                x = getX() + width - leftPadding - textWidth;
                break;
            case Component.CENTER:
                x = getX() + (width / 2) - (textWidth / 2);
                break;
            default:
                x = getX();
                break;
        }
       
        g.drawString(mText, x, y, style.getTextDecoration());
        
    }

    /**
     * override to prevent unnecessary repaints when using list
     */
    public void repaint() {
    }
    
    

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if(!Display.getInstance().shouldRenderSelection(list)) {
            isSelected = false;
        }
        setFocus(isSelected);
        mText = value.toString();
        
        return this;
    }

    public Component getListFocusComponent(List list) {
        if(mFocusComponent == null) {
            mFocusComponent = new NokiaListCellRenderer();
            mFocusComponent.setUIID("ListRendererFocus");
            mFocusComponent.setFocus(true);
            mFocusComponent.setCellRenderer(true);
        }
        return mFocusComponent;
    }

    protected Dimension calcPreferredSize() {
        Style s = getStyle();
        Font f = s.getFont();
        int w = 0;
        int h = 0;
        w += s.getPadding(LEFT) + s.getPadding(RIGHT) + f.stringWidth(mText);
        h += s.getPadding(TOP) + s.getPadding(BOTTOM) + f.getHeight();
        
        return new Dimension(w, h);
        
        
    }
    
    
    
}
