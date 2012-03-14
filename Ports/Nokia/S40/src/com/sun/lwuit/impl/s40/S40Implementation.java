/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.lwuit.impl.s40;

import com.nokia.lwuit.TextEditorProvider;
import com.sun.lwuit.impl.midp.GameCanvasImplementation;

/**
 *
 * @author tkor
 */
public class S40Implementation extends GameCanvasImplementation{
    
    
    /**
     * overriden form the baseclass. This version creates a Nokia TextEditor and sets the canvas 
     * as the parent.
     * @param maxSize
     * @param constraints
     * @param width
     * @param rows
     * @return 
     */
    public TextEditorProvider requestNewNativeTextEditor(int maxSize, int constraints, int width, int rows) {
        TextEditorProvider ret = TextEditorProvider.createTextEditor();
        if(ret != null) {
            ret.setMaxSize(maxSize);
            ret.setConstraints(constraints);
            ret.setSize(width, (ret.getFont().getHeight() + ret.getLineMarginHeight()) * rows);
            ret.setParent(this.getCanvas());
        }
        return ret;
    }

    public boolean isNativeInputSupported() {
        boolean ret = true;
        try {
            Class.forName("com.nokia.mid.ui.TextEditor");            
        }catch(ClassNotFoundException cnfe) {
            System.out.println("Native Input not supported.");
            ret = false;
        }
        return ret;
    }
}
