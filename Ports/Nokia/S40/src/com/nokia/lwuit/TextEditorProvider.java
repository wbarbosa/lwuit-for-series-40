package com.nokia.lwuit;

import javax.microedition.lcdui.Image;

/**
 * This class hides away the actual Nokia texteditor implementation. Making it possible to run lwuit in
 * devices that doesn't support Nokia UI API. The methods are mirrored from the actual TextEditor class so
 * check that for definition of each method.
 * @author tkor
 */
public abstract class TextEditorProvider {
    
    public abstract void delete(int offset, int length);
    public abstract int getBackgroundColor();
    public abstract int getCaretPosition();
    public abstract int getConstraints();
    public abstract String getContent();
    public abstract int getContentHeight();
    public abstract javax.microedition.lcdui.Font getFont();
    public abstract int getForegroundColor();
    public abstract String getInitialInputMode();
    public abstract int getLineMarginHeight();
    public abstract int getMaxSize();
    public abstract String getSelection();
    public abstract int getVisibleContentPosition();
    public abstract int getZPosition();
    public abstract boolean hasFocus();
    public abstract void insert(java.lang.String text, int position);
    public abstract boolean isMultiline();
    public abstract void setBackgroundColor(int color);
    public abstract void setCaret(int index);
    public abstract void setConstraints(int constraints);
    public abstract void setContent(java.lang.String content);
    public abstract void setFocus(boolean focused);
    public abstract void setFont(javax.microedition.lcdui.Font font);
    public abstract void setForegroundColor(int color);
    public abstract void setHighlightBackgroundColor(int color);
    public abstract void setHighlightForegroundColor(int color);
    public abstract void setInitialInputMode(java.lang.String characterSubset);
    public abstract int setMaxSize(int maxSize);
    public abstract void setMultiline(boolean aMultiline);
    public abstract void setParent(java.lang.Object parent);
    public abstract void setPosition(int x, int y);
    public abstract void setSelection(int index, int length);
    public abstract void setSize(int width, int height);
    public abstract void setTextEditorListener(TextEditorListener listener);
    public abstract void setVisible(boolean visible);
    public abstract void setZPosition(int z);
    public abstract int size();
    public abstract boolean isVisible();
    public abstract int getHeight();
    public abstract int getWidth();
    public abstract int getPositionX();
    public abstract int getPositionY();
    public abstract Image[] getInputIndicators();
    public abstract int getInputMode();
    
    
    public static TextEditorProvider createTextEditor() {
        TextEditorProvider provider = null;
        try {
            Class.forName("com.nokia.mid.ui.TextEditor");
            Class c = Class.forName("com.nokia.lwuit.TextEditorProviderImpl");
            provider = (TextEditorProvider)c.newInstance();
        }catch(Exception cnfe) {
            System.out.println("No native TextEditor supported.");
        }
        return provider;
    }
    
    public static interface TextEditorListener {
        public static final int ACTION_CARET_MOVE =                 4;
        public static final int ACTION_CONTENT_CHANGE =             1;
        public static final int ACTION_DIRECTION_CHANGE =           64;
        public static final int ACTION_INPUT_MODE_CHANGE =          128;
        public static final int ACTION_LANGUAGE_CHANGE =            256;
        public static final int ACTION_OPTIONS_CHANGE =             2;
        public static final int ACTION_PAINT_REQUEST =              32;
        public static final int ACTION_SCROLLBAR_CHANGED =          2048;
        public static final int ACTION_TRAVERSE_NEXT =              16;
        public static final int ACTION_TRAVERSE_OUT_SCROLL_DOWN =   1024;
        public static final int ACTION_TRAVERSE_OUT_SCROLL_UP =     512;
        public static final int ACTION_TRAVERSE_PREVIOUS =          8;
                
        public void inputAction(TextEditorProvider textEditor, int actions);
    }
    
}
