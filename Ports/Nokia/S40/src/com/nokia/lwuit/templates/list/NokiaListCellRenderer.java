/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.templates.list;

import com.sun.lwuit.Command;
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
 * to Label component that is used in DefaultListCellRenderer.
 * @author tkor
 */
public class NokiaListCellRenderer extends Component implements ListCellRenderer{
    
    private int cache_size = 20;
    
    private NokiaListCellRenderer mFocusComponent;
    
    private String mText = "";
    private Image mImage;
    
    private String ellipsis = "...";
    private int ellipsisWidth = 0;
    /**
     * This hashtable will contain cached values of strings that are shortened.
     * This is done so that we don't have to shorten the string every time repaint
     * occurs. If you know that you will have many items you should increase the cache
     * size. 
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
        int gap = 5; //gap between image and text
        int leftPadding = style.getPadding(rtl, Component.LEFT);
        int rightPadding = style.getPadding(rtl, Component.RIGHT);
        int topPadding = style.getPadding(false, Component.TOP);
        int bottomPadding = style.getPadding(false, Component.BOTTOM);
        
        Font font = style.getFont();
        int align = reverseAlignForBidi(style.getAlignment());
        
        int width = getWidth();
        int height = getHeight();
        int textSpace = width;
        if(mImage != null) {
            textSpace -= mImage.getWidth();
        }
        textSpace = textSpace - leftPadding - rightPadding;
        mText = shortenString(mText, textSpace, font);

        int textWidth = font.stringWidth(mText);
        
        int x = 0;
        int y = getY() + topPadding;

        switch(align) {
            case Component.LEFT:
                x = getX() + leftPadding;
                if(mImage != null) {
                    x += mImage.getWidth() + gap;
                }
                break;
            case Component.RIGHT:
                x = getX() + width - leftPadding - textWidth;
                if(mImage != null) {
                    if(rtl) {
                        x -= mImage.getWidth() - gap;
                    }
                }
                break;
            case Component.CENTER:
                x = getX() + (width / 2) - (textWidth / 2);
                if(mImage != null) {
                    if(rtl) {
                        x -= mImage.getWidth() - gap;
                    }else {
                        x += mImage.getWidth() + gap;
                    }
                }
                break;
            default:
                x = getX();
                break;
        }
        if(mImage != null) {
            if(rtl) {
                g.drawImage(mImage, getX() + getWidth() - rightPadding - mImage.getWidth(),
                                    getY() + topPadding);
            }else {
                g.drawImage(mImage, getX() + leftPadding, getY() + topPadding);
            }
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
        if (!Display.getInstance().shouldRenderSelection(list)) {
            isSelected = false;
        }
        setFocus(isSelected);        
        if (value instanceof BasicListItem) {
            BasicListItem data = (BasicListItem) value;
            mText = data.getText();
            mImage = data.getImage();

        }
        else if (value instanceof Command) {         
            Command c = (Command)value;            
            mText = c.getCommandName();
            mImage = c.getIcon();
        }
        else if (value != null) {
            mText = value.toString();
            mImage = null;
        } else {
            mText = "";
            mImage = null;
        }

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
        if(mImage != null) {
            h = Math.max(h, mImage.getHeight() + s.getPadding(TOP) + s.getPadding(BOTTOM));
            w = Math.max(w, mImage.getWidth() + f.stringWidth(mText) + s.getPadding(LEFT) + s.getPadding(RIGHT));
        }
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
        if(shorteningCache.size() > cache_size) {
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

    /**
     * Set desired String cache size. This helps increase fps when scrolling the
     * list when you have long strings. The default cache size is 20.
     * @param c desired cachesize
     */
    public void setStringCache(int c) {
        cache_size = c;
    }
    
}
