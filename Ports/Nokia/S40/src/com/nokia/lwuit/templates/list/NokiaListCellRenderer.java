/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.templates.list;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A faster version of the Renderer that list component can use. This renderer
 * only supports text and image but has far more simpler way of drawing them compared
 * to Label component that is used in DefaultListCellRenderer. <b>Note:</b> you
 * should not reuse the same renderer but instead create a new renderer every time.
 * @author tkor
 */
public class NokiaListCellRenderer extends Component implements ListCellRenderer{
    
    private NokiaListCellRenderer mFocusComponent;
    
    private String mText = "";
    private Image mImage;
    
    private String ellipsis = "...";
    private int ellipsisWidth = 0;
    /**
     * This hashtable will contain cached values of strings that are shortened.
     * This is done so that we don't have to shorten the string every time repaint
     * occurs.
     */
    private static Hashtable shorteningCache = new Hashtable(20);
    
    public NokiaListCellRenderer() {
        setUIID("ListRenderer");
        setCellRenderer(true);
        ellipsisWidth = getStyle().getFont().stringWidth(ellipsis);
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
        int align = reverseAlignForBidi(style.getAlignment());
        
        int width = getWidth();
        int height = getHeight();
        mText = shortenString(mText, width, font);

        int textWidth = font.stringWidth(mText);
        
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
        g.drawString(mText, x, y, this.getStyle().getTextDecoration());
        
    }

    /**
     * override to prevent unnecessary repaints when using list
     */
    public void repaint() {
    }

    public void repaint(int x, int y, int w, int h) {
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
    
    private int reverseAlignForBidi(int align) {
        if(isRTL()) {
            switch(align) {
                case Component.RIGHT:
                    return Component.LEFT;
                case Component.LEFT:
                    return Component.RIGHT;
            }
        }
        return align;
    }

    /**
     * Shortens the string to fit inside given width and adds ... to the end of the string
     * @param original the string to shorten
     * @param width the space that the shortened string should fit
     * @param font the font that is used to draw the text
     * @return shortened string with ellipsis
     */    
    private String shortenString(String original, final int width, final Font font) {
        if(font.stringWidth(original) <= width) {
            return original;
        }
        if(shorteningCache.containsKey(original)) {
            return (String) shorteningCache.get(original);
        }
        
        int widestCharWidth = font.charWidth('W');

        int index = original.length() - 1;
        int targetWidth = width - ellipsisWidth;
        while(!doesStringFit(original, index, widestCharWidth, targetWidth, font)) {
            index--;
        }
        String ret = original.substring(0, index) + ellipsis;
        //make sure the cache is cleared so that we don't get OOM
        if(shorteningCache.size() > 20) {
            shorteningCache.clear();
        }
        shorteningCache.put(original, ret);
        return ret;
        
    }
    private boolean doesStringFit(String s, int length, int widestCharWidth, int width, Font f) {
        if(length * widestCharWidth < width) {
            return true;
        }
        return f.stringWidth(s.substring(0, length)) < width;
    }

    protected void deinitialize() {
        super.deinitialize();
        System.out.println("deinitialize");
    }
    
}
