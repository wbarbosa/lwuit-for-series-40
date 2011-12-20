/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package net.java.lwuit.demo.iodemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.PeerComponent;
import com.sun.lwuit.Slider;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.VideoComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.io.NetworkEvent;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.services.GoogleRESTService;
import com.sun.lwuit.io.services.TwitterRESTService;
import com.sun.lwuit.io.ui.FileTreeModel;
import com.sun.lwuit.io.ui.SliderBridge;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.table.TableLayout;
import com.sun.lwuit.tree.Tree;
import com.sun.lwuit.tree.TreeModel;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
//#ifdef RIM
//# import net.rim.device.api.ui.component.ButtonField;
//# import net.rim.device.api.ui.component.LabelField;
//#endif

/**
 * Main class for the application generic across platforms
 *
 * @author Shai Almog
 */
public class Main implements Runnable {
    private Command exitCommand;
    private Command backToMainCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                showMainForm();
            }
        };
    private ActionListener platformRequest;
    public Main(Command exitCommand, ActionListener platformRequest) {
        this.exitCommand = exitCommand;
        this.platformRequest = platformRequest;
        NetworkManager.getInstance().start();
    }

    /**
     * Initilize the UI
     */
    public void run() {
        try {
            Resources res = Resources.open("/LWUITtheme.res");
            UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            showMainForm();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showMainForm() {
        Form main = new Form("Main");
        main.addCommand(exitCommand);
        main.setLayout(new BorderLayout());
        Container status = new Container(new BorderLayout());
        final Label statusLabel = new Label("Network Status N/A");
        Slider statusSlider = new SliderBridge();
        status.addComponent(BorderLayout.NORTH, statusLabel);
        status.addComponent(BorderLayout.SOUTH, statusSlider);
        main.addComponent(BorderLayout.SOUTH, status);

        TableLayout tl = new TableLayout(7, 3);
        Container content = new Container(tl);
        content.addComponent(new Label("Search Google"));
        final TextField googleSearch = new TextField("LWUIT", 7);
        googleSearch.setLeftAndRightEditingTrigger(false);
        content.addComponent(googleSearch);
        Button googleSearchButton = new Button("Go");
        content.addComponent(googleSearchButton);
        content.addComponent(new Label("Type"));
        final ComboBox type = new ComboBox(new Object[] {
            GoogleRESTService.WEB_SEARCH,
            GoogleRESTService.IMAGE_SEARCH,
            GoogleRESTService.BLOG_SEARCH,
            GoogleRESTService.BOOK_SEARCH,
            GoogleRESTService.LOCAL_SEARCH,
            GoogleRESTService.NEWS_SEARCH,
            GoogleRESTService.PATENT_SEARCH,
            GoogleRESTService.VIDEO_SEARCH
        });
        TableLayout.Constraint con = tl.createConstraint();
        con.setHorizontalSpan(2);
        content.addComponent(con, type);
        main.addComponent(BorderLayout.CENTER, content);
        googleSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(evt.getSource() instanceof GoogleRESTService) {
                    statusLabel.setText("Received Google Response");
                    Vector v = (Vector)((NetworkEvent)evt).getMetaData();
                    if(v.size() > 0) {
                        GoogleRESTService.ResultEntry e = (GoogleRESTService.ResultEntry)v.elementAt(v.size() - 1);
                        if(e.getTitle() == null && e.getEstimatedResultCount() != null) {
                            // this is a result count entry which isn't interesting for this particular demo
                            v.removeElementAt(v.size() - 1);
                        }
                    }
                    List results = new List(v);
                    results.setListCellRenderer(new ResultRenderer());
                    results.setRenderingPrototype("X");
                    Form resultsForm = new Form("Results");
                    resultsForm.setLayout(new BorderLayout());
                    resultsForm.addComponent(BorderLayout.CENTER, results);
                    results.addActionListener(platformRequest);
                    resultsForm.addCommand(backToMainCommand);
                    resultsForm.setBackCommand(backToMainCommand);
                    resultsForm.show();
                } else {
                    statusLabel.setText("Sending Google Request");
                    GoogleRESTService s = new GoogleRESTService(googleSearch.getText(), (String)type.getSelectedItem());
                    s.setResultSize(5);
                    s.addResponseListener(this);
                    NetworkManager.getInstance().addToQueue(s);
                }
            }
        });

        content.addComponent(new Label("Twitter"));
        final Button publicTimeline = new Button("Public TL");
        content.addComponent(publicTimeline);
        final Button usersShow = new Button("Users Show");
        content.addComponent(usersShow);
        ActionListener twitterListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(evt.getSource() instanceof TwitterRESTService) {
                    Hashtable h = (Hashtable)((NetworkEvent)evt).getMetaData();
                    showResponseTree(h);
                    return;
                }
                statusLabel.setText("Sending Twitter Request");
                TwitterRESTService t;
                if(publicTimeline == evt.getSource()) {
                    t = new TwitterRESTService("statuses/public_timeline");
                } else {
                    t = new TwitterRESTService("users/show");
                    TextField screenName = new TextField();
                    Command ok = new Command("OK");
                    Command cancel = new Command("Cancel");
                    if(Dialog.show("Screen Name", screenName, new Command[]{ok, cancel}) == cancel) {
                        return;
                    }
                    t.addArgument("screen_name", screenName.getText());
                }

                t.addResponseListener(this);
                NetworkManager.getInstance().addToQueue(t);
            }
        };
        publicTimeline.addActionListener(twitterListener);
        usersShow.addActionListener(twitterListener);


        //#ifdef RIM
//#         LabelField lf = new LabelField("Native Test");
//#         content.addComponent(PeerComponent.create(lf));
//#         //content.addComponent(new Label("Native"));
//# 
//#         net.rim.device.api.ui.component.TextField rt = new net.rim.device.api.ui.component.TextField();
//#         rt.setText("Hi World");
//#         PeerComponent c = PeerComponent.create(rt);
//#         content.addComponent(c);
//# 
//#         //content.addComponent(new Label("X"));
//# 
//#         ButtonField bf = new ButtonField("Native Test");
//#         content.addComponent(PeerComponent.create(bf));
//# 
        //#endif


        /*Button showVideo = new Button("Show Video");
        content.addComponent(showVideo);
        showVideo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Tree fileTree = new Tree(new FileTreeModel(true)) {
                    protected String childToDisplayLabel(Object child) {
                        if (((String) child).endsWith("/")) {
                            return ((String) child).substring(((String) child).lastIndexOf('/', ((String) child).length() - 2));
                        }
                        return ((String) child).substring(((String) child).lastIndexOf('/'));
                    }
                };
                Command ok = new Command("OK");
                Command cancel = new Command("Cancel");
                Command[] cmds = new Command[]{ok, cancel};
                if(ok == Dialog.show(null, fileTree, cmds)) {
                    try {
                        Form f = new Form();
                        VideoComponent vc = VideoComponent.create((String) fileTree.getSelectedItem(), null);
                        f.addComponent(vc);
                        vc.start();
                        f.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });*/

        /*con = tl.createConstraint();
        con.setHorizontalSpan(3);
        //content.addComponent(con, new TextArea("LWUIT Editor"));
        VideoComponent vc;
        try {
            //vc = VideoComponent.create("capture://video", null);
            vc = VideoComponent.create("jar:///2.3gp", null);
            //vc = VideoComponent.create(getClass().getResourceAsStream("/a.mp4"), "video/mp4");
            content.addComponent(con, vc);
            vc.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            content.addComponent(new TextArea(ex.toString()));
        }*/
        content.addComponent(new TextArea("Lots of text"));
        content.addComponent(new TextField("Field"));

        main.show();
    }

    /**
     * Shows the response of the webservice as a LWUIT tree
     *
     * @param h the hashtable containing the response
     */
    private void showResponseTree(Hashtable h) {
        Form f = new Form("Response");
        f.setLayout(new BorderLayout());
        Tree t = new Tree(new JSONTreeModel(h)) {
            protected String childToDisplayLabel(Object child) {
                if(child instanceof Object[]) {
                    if(((Object[])child)[1] instanceof String || ((Object[])child)[1] instanceof Double) {
                        return ((Object[])child)[0] + ": " + ((Object[])child)[1];
                    }
                    return ((Object[])child)[0].toString();
                }
                if(child instanceof Vector) {
                    return "[Array]";
                }
                return "[Unnamed]";
            }
        };
        f.addComponent(BorderLayout.CENTER, t);
        f.addCommand(backToMainCommand);
        f.show();
    }

    class JSONTreeModel implements TreeModel {
        private Hashtable root;
        public JSONTreeModel(Hashtable root) {
            this.root = root;
        }

        public Vector getChildren(Object parent) {
            if(parent == null) {
                return hashToVector(root);
            }
            if(parent instanceof Vector) {
                return (Vector)parent;
            }
            if(parent instanceof Hashtable) {
                return hashToVector((Hashtable)parent);
            }
            Object[] keyValue = (Object[]) parent;
            if(keyValue[1] instanceof Hashtable) {
                return hashToVector((Hashtable)keyValue[1]);
            }
            if(keyValue[1] instanceof Vector) {
                return (Vector)keyValue[1];
            }
            Vector v = new Vector();
            v.addElement(keyValue[1]);
            return v;
        }

        private Vector hashToVector(Hashtable h) {
            Enumeration e = h.keys();
            Vector v = new Vector();
            while(e.hasMoreElements()) {
                Object key = e.nextElement();
                Object value = h.get(key);
                v.addElement(new Object[] {key, value});
            }
            return v;
        }

        public boolean isLeaf(Object node) {
            if(node instanceof Object[]) {
                node = ((Object[])node)[1];
            }
            return !(node instanceof Vector || node instanceof Hashtable);
        }
        
    }
}
