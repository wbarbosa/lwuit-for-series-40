/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.tree.Tree;
import com.sun.lwuit.tree.TreeModel;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Vector;
import com.sun.lwuit.plaf.UIManager;

/**
 * Simple demo showing off how to use the tree component in LWUIT
 * 
 * @author Shai Almog
 */
public class TreeDemo extends Demo {
        
    public String getName() {
        return "Tree";
    }

    protected void executeDemo(Container f) {
        try {
            f.setLayout(new BorderLayout());
            Resources imageRes = UIDemoMain.getResource("images");
            Tree.setFolderIcon(imageRes.getImage("sady.png"));
            Tree.setFolderOpenIcon(imageRes.getImage("smily.png"));
            Tree tree = new Tree(new FileTreeModel());
            f.addComponent(BorderLayout.CENTER, tree);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class FileTreeModel implements TreeModel {

        

        public Vector getChildren(Object parent) {
            Vector response = new Vector();
            if(parent == null) {
                response.addElement("One");
                response.addElement("Two");
                response.addElement("Three");
            } else {
                if(!isLeaf(parent)) {
                    response.addElement("Child One");
                    response.addElement("Child Two");
                    response.addElement("Child Three");
                }
            }
            return response;
        }

        public boolean isLeaf(Object node) {
            return ((String)node).indexOf("Child") > -1;
        }
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        return UIManager.getInstance().localize("treeHelp", "Help description");
    }
}
