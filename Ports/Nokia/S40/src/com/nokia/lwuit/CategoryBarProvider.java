/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;

import com.nokia.mid.ui.IconCommand;
import com.sun.lwuit.Command;

/**
 * A wrapper class for Nokia CategoryBar API.
 * @author tkor
 */
public abstract class CategoryBarProvider {
    
    /**
     * Default image for background
     */
    public static int DEFAULT_BACKGROUND = -1;
    /**
     * Default colour for highlight
     */
    public static int DEFAULT_HIGHLIGHT_COLOUR = -1;
    /**
     * Image type representing background images.
     */
    public static int IMAGE_TYPE_BACKGROUND = 1;
    /**
     * Image type representing icon images.
     */
    public static int IMAGE_TYPE_ICON = 2;
    /**
     * Gets the most suitable Height for the given image type.
     * @param imageType type of image that the MIDlet is interested in.
     * @return the optimal height of the specified image type, in pixels.
     */
    public abstract int getBestImageHeight(int imageType);
    
    /**
     * Gets the most suitable Height for the given image type.
     * @param imageType type of image that the MIDlet is interested in.
     * @return the optimal height of the specified image type, in pixels.
     */
    public abstract int getBestImageWidth(int imageType);
    /**
     * Gets the getDefaultBoundingBox.
     * Allows MIDlets to optimise their use of image data to the device in use.
     * @return Boundry x,y,width, height.
     */
    public abstract int[] getDefaultBoundingBox();
    /**
     * Gets the maximum number of elements that can be displayed on a CategoryBar.
     * @return The maximum number of elements that can be displayed, excluding the predefined ElementListener.BACK element.
     */
    public abstract int getMaxElements();
    /**
     * Gets the index of the element that appears visually selected when the CategoryBar is displayed.
     * @return the index of the currently-selected element.
     */
    public abstract int getSelectedIndex();
    /**
     * Gets the CategoryBar's visible state.
     * @return true if visible otherwise false.
     */
    public abstract boolean getVisibility();
    /**
     * Sets category bar background colour. This method allows client to set category bar background to use solid colour instead of an image. Only applications signed to the manufacturer and operator domain are permitted to use this method.
     * By default, the CategoryBar has its own default background image. This method provides a way to replace it with a custom background colour, or to restore the default background image if a custom background image or colour has previously been set.
     * @param colour Background colour as RGB integer value. MIDlet can restore background to use default image by using constant DEFAULT_BACKGROUND.
     */
    public abstract void setBackgroundColour(int colour);
    /**
     * Sets the CategoryBar's background image.
     * Only applications signed to the manufacturer and operator domain are permitted to use this method.
     * By default, the CategoryBar has its own default background image. This method provides a way to replace it with a custom background image, or to restore the default background image if a custom background image has previously been set.

     * Note that the implementation will take a copy of any mutable image data provided.
     * @param image the background image to be used, or null to restore the default background image.
     * @throws java.lang.SecurityException - if the MIDlet does not have permission to set the background image.
     */
    public abstract void setBackgroundImage(javax.microedition.lcdui.Image image);
    /**
     * Associates an ElementListener with the CategoryBar. The listener will subsequently be notified whenever a CategoryBar element is selected. Setting a listener replaces any existing listener (i.e. only one listener can be registered to each CategoryBar instance). To remove the currently active listener, a null parameter must be given.
     * @param listener  an instance of ElementListener, or null to remove the currently-active listener.
     */
    public abstract void setElementListener(ElementListener listener);
    /**
     * Sets the icon and label properties of the specified element to match those of a given IconCommand, replacing the existing values.
     * Note that the implementation will take a copy of any mutable image data provided.
     * @param index the index of the element whose properties will be replaced.
     * @param element an IconCommand whose label and icon will be used in place of the element's existing values.
     * @param useLongLabel flag indicating whether the caller wants the long label of the IconCommand to be used as the element's label. If true, the long label will be used if it exists, otherwise the short label will be used.
     */
    public abstract void setElementProperties(int index, IconCommand element, boolean useLongLabel);
    /**
     * Sets the icon and label properties of the specified element, replacing the existing values.
     * Note that the implementation will take a copy of any mutable image data provided.
     * @param index the index of the element whose properties will be replaced.
     * @param unselectedIcon an Image providing the visual representation of the element when unselected.
     * @param selectedIcon an Image providing the visual representation of the element when selected. This parameter may be null if no selected icon is required.
     * @param label a String defining the textual representation of the element.
     */
    public abstract void setElementProperties(int index, javax.microedition.lcdui.Image unselectedIcon, javax.microedition.lcdui.Image selectedIcon, java.lang.String label);
    /**
     * Sets category bar highlight colour.
     * @param colour Highlight colour as RGB integer value. MIDlet can restore highlight to use default colour by using constant DEFAULT_HIGHLIGHT_COLOUR.
     */
    public abstract void setHighlightColour(int colour);
    /**
     * Sets the CategoryBar's selected element, which is element zero by default.
     * As a result of calling this method, the given element will be redrawn to highlight to the user that it is now selected. If the MIDlet has provided a selected version of the element's icon then it will be used for this purpose, otherwise an implementation-specific method of highlighting will be employed.
     * @param index the index of the currently-selected element.
     * @throws java.lang.IllegalArgumentException - if is less than zero or greater than or equal to the number of elements in the CategoryBar.
     */
    public abstract void setSelectedIndex(int index);
    /**
     * Sets whether transition effects are enabled on the CategoryBar, which otherwise follow the default device settings.
     * @param transitionSupport true if transition effects should be enabled otherwise false.
     */
    public abstract void setTransitionSupport(boolean transitionSupport);
    /**
     * Sets the CategoryBar's visibility, which is hidden by default. Only one CategoryBar may be visible at a time and therefore making a CategoryBar visible will automatically hide any other CategoryBar that is currently visible.
     * @param visibility the requested visible state of this CategoryBar (true for visible or false for hidden).
     */
    public abstract void setVisibility(boolean visibility);
    /**
     * Suppresses sizeChanged(int,int) events when category bar visibility changes.
     * @param suppressSizeChange If true, Category bar visibility change will not cause sizeChanged event.
     */
    public abstract void suppressSizeChanged(boolean suppressSizeChange);
    /**
     * Create the actual CategoryBar instance internally for the provider
     * @param elements an array of IconCommands whose labels and icons will be used to populate the CategoryBar.
     * @param useLongLabel  flag indicating whether the caller wants the long label of each IconCommand to be used as the label for each element in the CategoryBar. If true, the long label will be used if it exists, otherwise the short label will be used.
     */
    protected abstract void createImplementation(IconCommand[] elements, boolean useLongLabel);
    /**
     * Create the actual CategoryBar instance internally for the provider.
     * The parameters given by the MIDlet are used to populate the CategoryBar with elements. These parameters are sufficient only to define a legal CategoryBar instance. To further customise the CategoryBar (e.g. to set the CategoryBar's background image), MIDlets must call appropriate additional methods on the CategoryBar instance returned.

     * Every element is required to have an unselected version of its icon as well as a label. The selected version of an icon is optional and therefore it is legal to set the parameter or its elements to null. In such cases, the platform will provide its own highlighting when an element is selected.

     * The implementation may place a limit (N) on the maximum number of elements on a CategoryBar and therefore only the first N elements will be displayed, with the remainder ignored. The method getMaxElements() may be used to query the limit.

     * Note that the implementation will take a copy of any mutable image data provided.
     * @param unselectedIcons  an array of Images - one per element - used to define the visual representation of the element when unselected.
     * @param selectedIcons  an array of Images - one per element - used to define the visual representation of the element when selected.
     * @param labels  an array of Strings - one per element - used to define the textual representation of the element.
     */
    protected abstract void createImplementation(javax.microedition.lcdui.Image[] unselectedIcons, javax.microedition.lcdui.Image[] selectedIcons, java.lang.String[] labels);
    
    /**
     * Create a new CategoryBarProvider. This uses internal class to create the actual
     * CategoryBar object that can be used through the CategoryBarProvider. 
     * @param elements lwuit commands that you want to set in CategoryBar.
     * @param useLongLabel  flag indicating whether the caller wants the long label of each IconCommand to be used as the label for each element in the CategoryBar. If true, the long label will be used if it exists, otherwise the short label will be used.
     * @return new CategoryBarProvider or null if the CategoryBar API is not available.
     */
    public static CategoryBarProvider getCategoryBarProvider(Command[] elements, boolean useLongLabel) {
        if(elements == null || elements.length == 0) {
            throw new IllegalArgumentException("elements must not be null or zero length.");
        }
        CategoryBarProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class c = Class.forName("com.nokia.lwuit.CategoryBarProviderImpl");
            provider = (com.nokia.lwuit.CategoryBarProvider)c.newInstance();
            provider.createImplementation(wrapCommandToIconCommand(elements), useLongLabel);
        }catch(Exception cnfe) {
            System.out.println("CategoryBar API not available.");
        }
        
        return provider;
        
    }
    /**
     * Create a new CategoryBarProvider. This uses internal class to create the actual
     * CategoryBar object that can be used through the CategoryBarProvider. 
     * @param unselectedIcons
     * @param selectedIcons
     * @param labels
     * @return new CategoryBarProvider or null if CategoryBar API is not available.
     */
    public static CategoryBarProvider getCategoryBarProvider(javax.microedition.lcdui.Image[] unselectedIcons, javax.microedition.lcdui.Image[] selectedIcons, java.lang.String[] labels) {
        CategoryBarProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class c = Class.forName("com.nokia.lwuit.CategoryBarProviderImpl");
            provider = (com.nokia.lwuit.CategoryBarProvider) c.newInstance();
            provider.createImplementation(unselectedIcons, selectedIcons, labels);
        }catch(Exception cnfe) {
            System.out.println("CategoryBar API not available.");
        }
        
        return provider;
    }
    
    private static IconCommand[] wrapCommandToIconCommand(Command[] cmds) {
        IconCommand[] ret = new IconCommand[cmds.length];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = (IconCommand)MIDPCommandWrapper.createInstance(cmds[i]).getCommand();
        }
        return ret;
    }
    
    /**
     * ElementListener enables an application to discover when the user has selected an element from a CategoryBar.
     */
    public static interface ElementListener {
        /**
         * Constant indicating that the predefined BACK element has been selected.
         */
        public static int BACK = -1;
        /**
         * Notifies the listener that an element belonging to the given CategoryBar was selected.
         * Every CategoryBar features a predefined element that cannot be removed. 
         * If this element is selected then the special value BACK is supplied in the parameter.
         * @param bar the instance of CategoryBar whose element was selected.
         * @param selectedIndex the index of the selected element, or BACK in the case that the predefined BACK element was selected.
         */
        public void notifyElementSelected(CategoryBarProvider bar, int selectedIndex);
    }
}
