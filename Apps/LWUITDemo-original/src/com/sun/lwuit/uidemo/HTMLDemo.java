/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */

package com.sun.lwuit.uidemo;

import com.sun.lwuit.BrowserComponent;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.Image;
import com.sun.lwuit.util.Resources;
import com.sun.lwuit.EncodedImage;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.html.DefaultHTMLCallback;
import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import com.sun.lwuit.html.HTMLComponent;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.sun.lwuit.plaf.UIManager;

/**
 * A demo showing HTMLComponent in action
 *
 * @author Ofir Leitner
 */
public class HTMLDemo extends Demo /*implements ActionListener*/ {

    Container f;
    HTMLComponent htmlC;
    String source;

    String filename = "/index.html";

    Command sourceCmd = new Command("Source") {

        public void actionPerformed(ActionEvent evt) {
            Form srcForm = new Form("View Source");
            srcForm.setLayout(new BorderLayout());
            TextArea srcTa=new TextArea(getSource());
            srcTa.setEditable(false);
            srcForm.addComponent(BorderLayout.CENTER,srcTa);
            Command c = new Command("Back") {
                 public void actionPerformed(ActionEvent evt) {
                        HTMLDemo.this.showForm();
                 }
            };
            srcForm.addCommand(c);
            srcForm.setBackCommand(c);
            srcForm.show();
        }
        
    };

    public String getName() {
        return "HTML";
    }

    public void setSource(String src) {
        source=src;
    }

    public String getSource() {
        return source;
    }



    protected void executeDemo(Container f) {
        this.f=f;
        f.setScrollable(false);
        addCommand(sourceCmd, f);
        setTitle(getName());
        f.setLayout(new BorderLayout());
        Display.getInstance().getCurrent().setCyclicFocus(false);

        if(BrowserComponent.isNativeBrowserSupported()) {
            if(Dialog.show("Native Browser", "A native browser is available for your platform, would you like to use it or the lightweight component?",
                    "Native", "Lightweight")) {
                BrowserComponent b = new BrowserComponent();
                b.setURL("jar://"+filename);
                f.addComponent(BorderLayout.CENTER, b);
                return;
            }
        }
        htmlC = new HTMLComponent(new FileRequestHandler(this));
        htmlC.setHTMLCallback(new SimpleHTMLCallback(this));
        htmlC.setPage("jar://"+filename);
        f.addComponent(BorderLayout.CENTER, htmlC);
    }

    public void showForm() {
        f.getComponentForm().show();
    }

    public void setTitle(String title) {
        //f.setTitle(title);
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("HTMLHelp", "Help description");
    }

}

/**
 * A very simple HTML callback that handles title changes and intercepts menu:// links
 * 
 * @author Ofir Leitner
 */
class SimpleHTMLCallback extends DefaultHTMLCallback {

    HTMLDemo htmlDemo;

    public SimpleHTMLCallback(HTMLDemo htmlDemo) {
        this.htmlDemo=htmlDemo;
    }

   
    /**
     * This overrides linkClicked to intercept 'menu://' links, which we use in the HTMLs.
     * menu:// is not a real protocol, but rather a convention we use specifically in this demo to allow activation of application logic from th HTML.
     * Each link click is relayed to the linkClicked method of HTMLCallback, and there the developer can decide to handle it on his own.
     * 
     */
    public boolean linkClicked(HTMLComponent htmlC, String url) {
        if (url.startsWith("menu://")) {
            String demoName=url.substring(7);
            for(int i=0;i<UIDemoMain.DEMOS.length;i++) {
                if (demoName.equals(UIDemoMain.DEMOS[i].getName())) {
                    UIDemoMain.executeDemo(i);
                    return false; // Signals the HTMLComponent to not process this link
                }
            }
            
            return false;
        }
        return true; // Signals the HTMLComponent to prcoess this link as usual (i.e. call DocumentRequestHandler.resourceRequested)
    }

    public void titleUpdated(HTMLComponent htmlC, String title) {
        htmlDemo.setTitle(title);
    }



}

/**
 * This request handler fetches files stored locally in the JAR
 * For HTTP request handling refer to the LWUITBrowser demo at the SVN in MIDP/applications
 * 
 * @author Ofir Leitner
 */
class FileRequestHandler implements DocumentRequestHandler {

    HTMLDemo htmlDemo;
    static final String DEFAULT_RES = "images";

    public FileRequestHandler(HTMLDemo htmlDemo) {
        this.htmlDemo=htmlDemo;
    }

    public InputStream resourceRequested(DocumentInfo docInfo) {
        String url=docInfo.getUrl();

        /**
         * Links that start with res:// (which is not a real protocol, but rather a convention we use in this app) are references to images in the resource files
         * The format is res://resource-file-name/resource-name
         */
        if (url.startsWith("res://")) {
            url=url.substring(6); //remove the res://
            String resName=DEFAULT_RES;
            int i=url.indexOf('/');
            if (i!=-1) {
                resName=url.substring(0, i);
                url=url.substring(i+1);
            }
            Resources res=null;
            try {
                res=UIDemoMain.getResource(resName);
            } catch (Exception e) {
                System.out.println("Error while opening resource file "+resName+" while attempting to get "+docInfo.getUrl()+", Exception="+e.getMessage());
            }
            if (res==null) {
                return null;
            }
            Image img=res.getImage(url);
            if (img!=null) {
                try {
                    EncodedImage eImg=(EncodedImage)img;
                    byte[] buf = eImg.getImageData();
                    if (buf!=null) {
                        return  new ByteArrayInputStream(buf);
                    }
                } catch (Exception e) {
                    System.out.println("Error: "+e.getMessage());
                    return null;
                }
            } else {
                return null;
            }
        }

        // If a from was submitted on a local file, just display the parameters
        if ((docInfo.getParams()!=null) && (!docInfo.getParams().equals(""))) {
            String method="GET";
            if (docInfo.isPostRequest()) {
                method="POST";
            }
            String params=docInfo.getParams();
            String newParams="";
            if (params!=null) {
                for(int i=0;i<params.length();i++) {
                    char c=params.charAt(i);
                    if (c=='&') {
                        newParams+=", ";
                    } else {
                        newParams+=c;
                    }
                }
            }
            return getStream("<h2>Form submitted locally.</h2><b>Method:</b> "+method+"<br><br><b>Parameters:</b><br>"+newParams+"<hr><a href=\""+docInfo.getUrl()+"\">Continue to local URL</a>","Form Results");
        }


        url=url.substring(6); // Cut the jar://

        int hash=url.indexOf('#'); //trim anchors
        if (hash!=-1) {
           url=url.substring(0,hash);
        }

        int param=url.indexOf('?'); //trim parameters, not relvant for files
        if (param!=-1) {
            url=url.substring(0, param);
        }

        byte[] buf;
        try {
            buf = getBuffer(Display.getInstance().getResourceAsStream(getClass(), url));
            if (url.endsWith(".html")) { //only set source to HTML files (not images)
                htmlDemo.setSource(new String(getBuffer(new ByteArrayInputStream(buf))));
            }
            return new ByteArrayInputStream(buf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;


    }

    /**
     * Returns an Inputstream of the specified HTML text
     *
     * @param htmlText The text to get the stream from
     * @param title The page's title
     * @return an Inputstream of the specified HTML text
     */
    private InputStream getStream(String htmlText,String title) {
        String titleStr="";
        if (title!=null) {
            titleStr="<head><title>"+title+"</title></head>";
        }
        htmlText="<html>"+titleStr+"<body>"+htmlText+"</body></html>";
        ByteArrayInputStream bais = new ByteArrayInputStream(htmlText.getBytes());
        return bais;

    }


    /**
     * Reads an inputstream completely and places it into a buffer
     *
     * @param is The InputStream to read
     * @return A buffer containing the stream's contents
     * @throws IOException
     */
    static byte[] getBuffer(InputStream is) throws IOException {
            int chunk = 5000;
            byte[] buf = new byte[chunk];
            int i=0;
            int b = is.read();
            while (b!=-1) {
                if (i>=buf.length) {
                    byte[] tempbuf=new byte[buf.length+chunk];
                    for (int j=0;j<buf.length;j++) {
                        tempbuf[j]=buf[j];
                    }
                    buf=tempbuf;
                }
                buf[i]=(byte)b;
                i++;
                b = is.read();
            }
            byte[] tempbuf=new byte[i];
            for (int j=0;j<tempbuf.length;j++) {
                tempbuf[j]=buf[j];
            }

            buf=tempbuf;
            return buf;
    }



}
