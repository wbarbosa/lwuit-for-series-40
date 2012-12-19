/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.IconCommand;
import com.sun.lwuit.Command;

/**
 *
 * @author tkor
 */
public abstract class CategoryBarProvider {
    
    public static int DEFAULT_BACKGROUND = -1;
    public static int DEFAULT_HIGHLIGHT_COLOUR = -1;
    public static int IMAGE_TYPE_BACKGROUND = 1;
    public static int IMAGE_TYPE_ICON = 2;
    
    public abstract int getBestImageHeight(int imageType);
    
    public abstract int getBestImageWidth(int imageType);
    
    public abstract int[] getDefaultBoundingBox();
    
    public abstract int getMaxElements();
    
    public abstract int getSelectedIndex();
    
    public abstract boolean getVisibility();
    
    public abstract void setBackgroundColour(int colour);
    
    public abstract void setBackgroundImage(javax.microedition.lcdui.Image image);
    
    public abstract void setElementListener(ElementListener listener);
    
    public abstract void setElementProperties(int index, IconCommand element, boolean useLongLabel);
    
    public abstract void setElementProperties(int index, javax.microedition.lcdui.Image unselectedIcon, javax.microedition.lcdui.Image selectedIcon, java.lang.String label);
    
    public abstract void setHighlightColour(int colour);
    
    public abstract void setSelectedIndex(int index);
    
    public abstract void setTransitionSupport(boolean transitionSupport);
    
    public abstract void setVisibility(boolean visibility);
    
    public abstract void suppressSizeChanged(boolean suppressSizeChange);
    
    public static CategoryBarProvider getCategoryBarProvider(Command[] elements, boolean useLongLabel) {
        return null;
    }
    public static CategoryBarProvider getCategoryBarProvider(javax.microedition.lcdui.Image[] unselectedIcons, javax.microedition.lcdui.Image[] selectedIcons, java.lang.String[] labels) {
        return null;
    }
    
    public static interface ElementListener {
        public static int BACK = -1;
        
        public void notifyElementSelected(CategoryBarProvider bar, int selectedIndex);
    }
}
