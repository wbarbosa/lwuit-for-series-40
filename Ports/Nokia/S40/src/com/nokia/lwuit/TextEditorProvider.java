package com.nokia.lwuit;

import javax.microedition.lcdui.Image;

/**
 * This class hides away the actual Nokia texteditor implementation. Making it possible to run lwuit in
 * devices that doesn't support Nokia UI API. The methods are mirrored from the actual TextEditor class.
 * @author tkor
 */
public abstract class TextEditorProvider {
    
    /**
     * Deletes characters from the TextEditorProvider
     * @param offset the beginning of the region to be deleted.
     * @param length the number of characters to be deleted.
     * @throws java.lang.IllegalArgumentException - if the resulting contents 
     * would be illegal for the current input constraints 
     * java.lang.StringIndexOutOfBoundsException - if offset and length do not 
     * specify a valid range within the content of the TextEditorProvider
     */
    public abstract void delete(int offset, int length);
    
    /**
     * Gets the background color and alpha of this TextEditorProvider
     * @return the background color.
     */
    public abstract int getBackgroundColor();
    /**
     * Gets the current position of the caret in the editor.
     * @return the position of the caret.
     */
    public abstract int getCaretPosition();
    /**
     * Gets the current input constraints of this TextEditorProvider.
     * @return the current constraints value
     */
    public abstract int getConstraints();
    /**
     * Gets the string content in the TextEditorProvider.
     * @return String containing the content.
     */
    public abstract String getContent();
    /**
     * Gets the whole content height in this TextEditorProvider in pixels.
     * @return the height of the content.
     */
    public abstract int getContentHeight();
    /**
     * Get the font used by the TextEditorProvider.
     * @return the font used.
     */
    public abstract javax.microedition.lcdui.Font getFont();
    /**
     * Gets the foreground color and alpha of this TextEditorProvider.
     * @return the foreground color.
     */
    public abstract int getForegroundColor();
    
    /**
     *  Returns the initial input mode set to the editor, or null if no initial input mode has been set.
     * @return the initial inputmode.
     */
    public abstract String getInitialInputMode();
    
    /**
     * Gets the line margin height in this TextEditor in pixels.
     * @return the height of the line margin.
     */
    public abstract int getLineMarginHeight();
    
    /**
     * Returns the maximum size (number of characters) that can be stored in this TextEditorProvider.
     * @return the maximum size.
     */
    public abstract int getMaxSize();
    
    /**
     * Gets the currently selected content in the TextEditorProvider.
     * @return the current selection.
     */
    public abstract String getSelection();
    
    /**
     * Gets the topmost pixel position of the topmost visible line in the editor.
     * @return the topmost pixel position.
     */
    public abstract int getVisibleContentPosition();
    /**
     * Returns the Z-position, or the elevation, of the item.
     * @return the Z-position.
     */
    public abstract int getZPosition();
    
    /**
     * Returns the focus state.
     * @return true if focused, false if unfocused.
     */
    public abstract boolean hasFocus();
    
    /**
     * Inserts a string into the content of the TextEditor.
     * @param text string to insert.
     * @param position position to insert to.
     */
    public abstract void insert(java.lang.String text, int position);
    
    /**
     * Returns the multiline state of the TextEditorProvider.
     * @return true if multiline, otherwise false.
     */
    public abstract boolean isMultiline();
    
    /**
     * Set the background color of the editor.
     * @param color the color.
     */
    public abstract void setBackgroundColor(int color);
    
    /**
     * Sets the index of the caret.
     * @param index caret index.
     */
    public abstract void setCaret(int index);
    
    /**
     * Sets the input constraints of this TextEditorProvider.
     * @param constraints input constraints.
     */
    public abstract void setConstraints(int constraints);
    
    /**
     * Sets the content of the TextEditor as a string.
     * @param content the nex content
     */
    public abstract void setContent(java.lang.String content);
    
    /**
     * Sets this TextEditorProvider focused or removes keyboard focus.
     * @param focused true if focus, false for unfocus.
     */
    public abstract void setFocus(boolean focused);
    /**
     * Sets the application preferred font for rendering the text content in this TextEditorProvider.
     * @param font the preferred font.
     */
    public abstract void setFont(javax.microedition.lcdui.Font font);
    
    /**
     * Sets the foreground color and alpha of this TextEditorProvider to the specified values.
     * @param color the new foreground color.
     */
    public abstract void setForegroundColor(int color);
    
    /**
     * Sets the highlight background color.
     * @param color the new highlight background color.
     */
    public abstract void setHighlightBackgroundColor(int color);
    
    /**
     * Sets the highlight foreground color.
     * @param color the new highlight foreground color.
     */
    public abstract void setHighlightForegroundColor(int color);
    
    /**
     * Sets a hint to the implementation as to the input mode that should be used when the user initiates editing of this TextEditorProvider.
     * @param characterSubset  a string naming a Unicode character subset, or null
     */
    public abstract void setInitialInputMode(java.lang.String characterSubset);
    /**
     * Sets the maximum size (number of characters) that can be contained in this TextEditor.
     * @param maxSize
     * @return 
     */
    public abstract int setMaxSize(int maxSize);
    
    /**
     * Sets the editor to be either multi-line (true) or single-line (false).
     * @param aMultiline true for multi-line, false for single-line.
     */
    public abstract void setMultiline(boolean aMultiline);
    
    /**
     * Set the parent object of this TextEditor.
     * @param parent the parent object.
     */
    public abstract void setParent(java.lang.Object parent);
    
    /**
     * Sets the rendering position of this TextEditorProvider.
     * @param x
     * @param y 
     */
    public abstract void setPosition(int x, int y);
    
    /**
     * Sets a selection on a range of text in the TextEditor content.
     * @param index start of selection
     * @param length length of the selection
     */
    public abstract void setSelection(int index, int length);
    
    /**
     * Sets the size of this TextEditor in pixels.
     * @param width the width
     * @param height the height
     */
    public abstract void setSize(int width, int height);
    
    /**
     * Sets a listener for content changes in this TextEditorProvider, replacing any previous TextEditorListener.
     * @param listener the new listener
     */
    public abstract void setTextEditorListener(TextEditorListener listener);
    
    /**
     * Sets the visibility value of TextEditorProvider.
     * @param visible true for visible, false for invisible.
     */
    public abstract void setVisible(boolean visible);
    /**
     * Sets the Z-position, or the elevation, of the item.
     * @param z the new z-position.
     */
    public abstract void setZPosition(int z);
    
    /**
     * Gets the number of characters that are currently stored in this TextEditorProvider.
     * @return number of characters.
     */
    public abstract int size();
    
    /**
     * True for visible, false for invisible.
     * @return true if visible, false if invisible.
     */
    public abstract boolean isVisible();
    
    /**
     * Get the height of the editor.
     * @return the height in pixels.
     */
    public abstract int getHeight();
    
    /**
     * Get the width of the editor.
     * @return the width in pixels.
     */
    public abstract int getWidth();
    
    /**
     * Get the x-position of the editor.
     * @return the x-position.
     */
    public abstract int getPositionX();
    
    /**
     * Get the y-position of the editor.
     * @return the y-position.
     */
    public abstract int getPositionY();
    
    public abstract void cleanup(); 
    
    /**
     * Create an new instance of the TextEditorProvider
     * @return new instance of the TextEditorProvider class or null if required
     * API is not available.
     */
    public static TextEditorProvider createTextEditor() {
        TextEditorProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.TextEditor");
            Class c = Class.forName("com.nokia.lwuit.TextEditorProviderImpl");
            provider = (TextEditorProvider)c.newInstance();
        } catch(Exception cnfe) {
            // No native TextEditor supported.
        }
        return provider;
    }
    /**
     * A listener for receiving notification of content changes and other editor events from TextEditor objects. 
     * The events are generated on content changes that are either a result of a programmatical change or due to an user input.
     */
    public static interface TextEditorListener {
        /**
         * Indicates that the caret in this TextEditor has moved.
         */
        public static final int ACTION_CARET_MOVE =                 4;
        
        /**
         * Indicates that the content of this TextEditor has changed.
         */
        public static final int ACTION_CONTENT_CHANGE =             1;
        
        /**
         * Indicates that the direction of the writing-language has changed.
         */
        public static final int ACTION_DIRECTION_CHANGE =           64;
        
        /**
         * Indicates that the current input-mode has changed.
         */
        public static final int ACTION_INPUT_MODE_CHANGE =          128;
        
        /**
         * Indicates that the current input-language has changed.
         */
        public static final int ACTION_LANGUAGE_CHANGE =            256;
        
        /**
         * Indicates that the options of this TextEditor have changed.
         */
        public static final int ACTION_OPTIONS_CHANGE =             2;
        
        /**
         * Indicates that this TextEditor has to be repainted.
         */
        public static final int ACTION_PAINT_REQUEST =              32;
        
        /**
         * Indicates that scrollbar should be updated.
         */
        public static final int ACTION_SCROLLBAR_CHANGED =          2048;
        
        /**
         * Indicates that the user tries to exit this TextEditor downwards.
         */
        public static final int ACTION_TRAVERSE_NEXT =              16;
        
        /**
         * Indicates that the editor cannot scroll down anymore.
         */
        public static final int ACTION_TRAVERSE_OUT_SCROLL_DOWN =   1024;
        
        /**
         * Indicates that the editor cannot scroll up anymore.
         */
        public static final int ACTION_TRAVERSE_OUT_SCROLL_UP =     512;
        
        /**
         * Indicates that the user tries to exit this TextEditor upwards.
         */
        public static final int ACTION_TRAVERSE_PREVIOUS =          8;
              
        /**
        * Implement to handle textEditor events
        * @param textEditor the editor that sent the event
        * @param actions actions that happened in the editor
        */
        public void inputAction(TextEditorProvider textEditor, int actions);
    }
    
}
