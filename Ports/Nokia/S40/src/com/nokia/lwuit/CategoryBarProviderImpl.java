/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.IconCommand;
import com.sun.lwuit.Command;
import javax.microedition.lcdui.Image;

/**
 *
 * @author tkor
 */
class CategoryBarProviderImpl extends CategoryBarProvider{
    
    private CategoryBar categorybar = null;
    
    public CategoryBarProviderImpl() {
     
    }

    public int getBestImageHeight(int imageType) {
        return categorybar.getBestImageHeight(imageType);
    }

    public int getBestImageWidth(int imageType) {
        return categorybar.getBestImageWidth(imageType);
    }

    public int[] getDefaultBoundingBox() {
        return categorybar.getDefaultBoundingBox();
    }

    public int getMaxElements() {
        return categorybar.getMaxElements();
    }

    public int getSelectedIndex() {
        return categorybar.getSelectedIndex();
    }

    public boolean getVisibility() {
        return categorybar.getVisibility();
    }

    public void setBackgroundColour(int colour) {
        categorybar.setBackgroundColour(colour);
    }

    public void setBackgroundImage(Image image) {
        categorybar.setBackgroundImage(image);
    }

    public void setElementListener(final ElementListener listener) {
        categorybar.setElementListener(new com.nokia.mid.ui.ElementListener() {

            public void notifyElementSelected(CategoryBar bar, int selectedIndex) {
                listener.notifyElementSelected(CategoryBarProviderImpl.this, selectedIndex);
            }
        });
    }

    public void setElementProperties(int index, IconCommand element, boolean useLongLabel) {
        categorybar.setElementProperties(index, element, useLongLabel);
    }

    public void setElementProperties(int index, Image unselectedIcon, Image selectedIcon, String label) {
        categorybar.setElementProperties(index, unselectedIcon, selectedIcon, label);
    }

    public void setHighlightColour(int colour) {
        categorybar.setHighlightColour(colour);
    }

    public void setSelectedIndex(int index) {
        categorybar.setSelectedIndex(index);
    }

    public void setTransitionSupport(boolean transitionSupport) {
        categorybar.setTransitionSupport(transitionSupport);
    }

    public void setVisibility(boolean visibility) {
        categorybar.setVisibility(visibility);
    }

    public void suppressSizeChanged(boolean suppressSizeChange) {
        categorybar.suppressSizeChanged(suppressSizeChange);
    }

    public void createImplementation(IconCommand[] elements, boolean useLongLabel) {
        categorybar = new CategoryBar(elements, useLongLabel);
        System.out.println("categorybar instance created");
    }

    public void createImplementation(Image[] unselectedIcons, Image[] selectedIcons, String[] labels) {
        categorybar = new CategoryBar(unselectedIcons, selectedIcons, labels);
    }
    
}
