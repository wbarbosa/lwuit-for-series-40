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
    
    public abstract void createImplementation(IconCommand[] elements, boolean useLongLabel);
    public abstract void createImplementation(javax.microedition.lcdui.Image[] unselectedIcons, javax.microedition.lcdui.Image[] selectedIcons, java.lang.String[] labels);
    
    public static CategoryBarProvider getCategoryBarProvider(IconCommand[] elements, boolean useLongLabel) {
        CategoryBarProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class c = Class.forName("com.nokia.lwuit.CategoryBarProviderImpl");
            provider = (CategoryBarProvider) c.newInstance();
            provider.createImplementation(elements, useLongLabel);
        }catch(Exception cnfe) {
            System.out.println("CategoryBar API not available.");
        }
        
        return provider;
        
    }
    public static CategoryBarProvider getCategoryBarProvider(javax.microedition.lcdui.Image[] unselectedIcons, javax.microedition.lcdui.Image[] selectedIcons, java.lang.String[] labels) {
        CategoryBarProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class c = Class.forName("com.nokia.lwuit.CategoryBarProviderImpl");
            provider = (CategoryBarProvider) c.newInstance();
            provider.createImplementation(unselectedIcons, selectedIcons, labels);
        }catch(Exception cnfe) {
            System.out.println("CategoryBar API not available.");
        }
        
        return provider;
    }
    
    public static interface ElementListener {
        public static int BACK = -1;
        
        public void notifyElementSelected(CategoryBarProvider bar, int selectedIndex);
    }
}
