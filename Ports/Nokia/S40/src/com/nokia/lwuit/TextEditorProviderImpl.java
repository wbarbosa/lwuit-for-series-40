/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit;


import com.nokia.mid.ui.TextEditor;
import com.sun.lwuit.Dialog;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

/**
 *
 * @author tkor
 */
class TextEditorProviderImpl extends TextEditorProvider implements com.nokia.mid.ui.TextEditorListener {
    
    private TextEditor editor = null;
    private TextEditorListener internalListener = null;
    
    /**
     * Default constructor.
     */
    public TextEditorProviderImpl() {
    
        //create some default values just so that we can instantiate the actual class
        editor = TextEditor.createTextEditor("", 500, 0, 100, 100);

    }
    /**
     * @inheritDoc
     */
    public void delete(int offset, int length) {
        editor.delete(offset, length);
    }
    /**
     * @inheritDoc
     */
    public int getBackgroundColor() {
        return editor.getBackgroundColor();
        
    }
    /**
     * @inheritDoc
     */
    public int getCaretPosition() {
        return editor.getCaretPosition();
    }
    /**
     * @inheritDoc
     */
    public int getConstraints() {
        return editor.getConstraints();
    }
    /**
     * @inheritDoc
     */
    public String getContent() {
        return editor.getContent();
    }
    /**
     * @inheritDoc
     */
    public int getContentHeight() {
        return editor.getContentHeight();
    }
    /**
     * @inheritDoc
     */
    public Font getFont() {
        return editor.getFont();
    }
    /**
     * @inheritDoc
     */
    public int getForegroundColor() {
        return editor.getForegroundColor();
    }
    /**
     * @inheritDoc
     */
    public String getInitialInputMode() {
        return editor.getInitialInputMode();
    }
    /**
     * @inheritDoc
     */
    public int getLineMarginHeight() {
        return editor.getLineMarginHeight();
    }
    /**
     * @inheritDoc
     */
    public int getMaxSize() {
        return editor.getMaxSize();
    }
    /**
     * @inheritDoc
     */
    public String getSelection() {
        return editor.getSelection();
    }
    /**
     * @inheritDoc
     */
    public int getVisibleContentPosition() {
        return editor.getVisibleContentPosition();
    }
    /**
     * @inheritDoc
     */
    public int getZPosition() {
        return editor.getZPosition();
    }
    /**
     * @inheritDoc
     */
    public boolean hasFocus() {
        return editor.hasFocus();
    }
    /**
     * @inheritDoc
     */
    public void insert(String text, int position) {
        editor.insert(text, position);
    }
    /**
     * @inheritDoc
     */
    public boolean isMultiline() {
        return editor.isMultiline();
    }
    /**
     * @inheritDoc
     */
    public void setBackgroundColor(int color) {
        editor.setBackgroundColor(color);
    }
    /**
     * @inheritDoc
     */
    public void setCaret(int index) {
        editor.setCaret(index);
    }
    /**
     * @inheritDoc
     */
    public void setConstraints(int constraints) {
        editor.setConstraints(constraints);
    }
    /**
     * @inheritDoc
     */
    public void setContent(String content) {
        editor.setContent(content);
    }
    /**
     * @inheritDoc
     */
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }
    /**
     * @inheritDoc
     */
    public void setFont(Font font) {
        editor.setFont(font);
    }
    /**
     * @inheritDoc
     */
    public void setForegroundColor(int color) {
        editor.setForegroundColor(color);
    }
    /**
     * @inheritDoc
     */
    public void setHighlightBackgroundColor(int color) {
        editor.setHighlightBackgroundColor(color);
    }
    /**
     * @inheritDoc
     */
    public void setHighlightForegroundColor(int color) {
        editor.setHighlightForegroundColor(color);
    }
    /**
     * @inheritDoc
     */
    public void setInitialInputMode(String characterSubset) {
        editor.setInitialInputMode(characterSubset);
    }
    /**
     * @inheritDoc
     */
    public int setMaxSize(int maxSize) {
        editor.setMaxSize(maxSize);
        return editor.getMaxSize();
    }
    /**
     * @inheritDoc
     */
    public void setMultiline(boolean aMultiline) {
        editor.setMultiline(aMultiline);
    }
    /**
     * @inheritDoc
     */
    public void setParent(Object parent) {
        editor.setParent(parent);
    }
    /**
     * @inheritDoc
     */
    public void setPosition(int x, int y) {
        editor.setPosition(x, y);
    }
    /**
     * @inheritDoc
     */
    public void setSelection(int index, int length) {
        editor.setSelection(index, length);
    }
    /**
     * @inheritDoc
     */
    public void setSize(int width, int height) {
        editor.setSize(width < 1 ? 1 : width, height < 1 ? 1 : height);
    }
    /**
     * @inheritDoc
     */
    public void setTextEditorListener(TextEditorListener listener) {
        internalListener = listener;
        editor.setTextEditorListener(this);
    }
    /**
     * @inheritDoc
     */
    public void setVisible(boolean visible) {
        editor.setVisible(visible);
    }
    /**
     * @inheritDoc
     */
    public void setZPosition(int z) {
        editor.setZPosition(z);
    }
    /**
     * @inheritDoc
     */
    public int size() {
        return editor.size();
    }
    /**
     * @inheritDoc
     */
    public boolean isVisible() {
        return editor.isVisible();
    }
    /**
     * @inheritDoc
     */
    public int getHeight() {
        return editor.getHeight();
    }
    /**
     * @inheritDoc
     */
    public int getPositionX() {
        return editor.getPositionX();
    }
    /**
     * @inheritDoc
     */
    public int getPositionY() {
        return editor.getPositionY();
    }
    /**
     * @inheritDoc
     */
    public int getWidth() {
        return editor.getWidth();
    }   
    /**
     * @inheritDoc
     */
    public void inputAction(TextEditor textEditor, int actions) {
        if(internalListener != null) {
            internalListener.inputAction(this, actions);
        }
    }

    public void cleanup() {
        editor.setParent(null);
        editor = null;
    }
    
    
}
