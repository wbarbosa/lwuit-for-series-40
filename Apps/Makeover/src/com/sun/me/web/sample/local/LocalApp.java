/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.me.web.sample.local;

import com.sun.lwuit.Button;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import java.io.IOException;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.ConnectionRequest;
import com.sun.lwuit.io.NetworkManager;
import com.sun.lwuit.io.services.ImageDownloadService;
import com.sun.lwuit.io.ui.SliderBridge;
import com.sun.lwuit.io.util.JSONParser;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Implements the local business search UI of the makeover demo
 *
 * @author  Shai Almog
 */
public class LocalApp {
    /* Demo mode is activiated if network connection fails */
    private boolean demoMode = false;

    private static final int DEFAULT_MAP_ZOOM = 6;
    private int zoom = DEFAULT_MAP_ZOOM;

    /* TODO: Get own APP ID for Yahoo Local and Maps APIs */
    private static final String APPID = "VS2gtQrV34ElS4obpTabGJ0lxYxDjwPzrjgaj_xTo.VbdnpA24586Jul4oDCXpO3UVN7";

    private static final String LOCAL_BASE = "http://local.yahooapis.com/LocalSearchService/V2/localSearch";
    
    private static final String MAP_BASE = "http://api.local.yahoo.com/MapsService/V1/mapImage";

    static final Object LOADING_MARKER = new Object();
    
    private boolean started;
    private Command exitCommand = new Command("Exit") {
        public void actionPerformed(ActionEvent ev) {
            Display.getInstance().exitApplication();
        }
    };
    
    private Command defaultThemeCommand = new Command("Default Theme") {
        public void actionPerformed(ActionEvent ev) {
            setTheme("/theme.res");
        }
    };
    
    private Command javaThemeCommand = new Command("LWUIT Theme") {
        public void actionPerformed(ActionEvent ev) {
            setTheme("/LWUITtheme.res");
        }
    };

    private void setTheme(String name) {
        try {
            com.sun.lwuit.util.Resources res = com.sun.lwuit.util.Resources.open(name);
            com.sun.lwuit.plaf.UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            Display.getInstance().getCurrent().refreshTheme();
        } catch(java.io.IOException err) {
             err.printStackTrace();
        }
    }
    
    private static Image getImage(String name) {
        try {
            return Image.createImage(name);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void startApp() {
        // distinguish between start and resume from pause
        if(!started) {
            started = true;
                        
            // show your LWUIT form here e.g.: new MyForm().show();
            // this is a good place to set your default theme using
            // the UIManager class e.g.:
            try {
                com.sun.lwuit.util.Resources res = com.sun.lwuit.util.Resources.open("/theme.res");
                com.sun.lwuit.plaf.UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            } catch(java.io.IOException err) {
                 err.printStackTrace();
            }
            showMainForm();
        }
    }
    
    private void showMainForm() {
        Form mainForm = createForm("Local Search");
        mainForm.setTransitionInAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 400));
        mainForm.setTransitionOutAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
        mainForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        mainForm.addComponent(new Label("search for:"));
        final TextField searchFor = new TextField("coffee", 50);
        mainForm.addComponent(searchFor);
        mainForm.addComponent(new Label("location:"));
        final TextField location = new TextField("95054", 50);
        mainForm.addComponent(location);
        mainForm.addComponent(new Label("street:"));
        final TextField street = new TextField(50);
        mainForm.addComponent(street);
        mainForm.addComponent(new Label("sort results by:"));
        final ComboBox sortResults = new ComboBox(new String[] {"Distance", "Title", "Rating", "Relevance"});
        mainForm.addComponent(sortResults);
        mainForm.addCommand(exitCommand);
        mainForm.addCommand(defaultThemeCommand);
        mainForm.addCommand(javaThemeCommand);
        Command searchCommand = new Command("Search") {
            public void actionPerformed(ActionEvent ev) {
                showSearchResultForm(searchFor.getText(), location.getText(), street.getText(), (String) sortResults.getSelectedItem());
            }
        };
        Button searchButton = new Button(searchCommand);
        Container searchContainer = new Container(new BorderLayout());
        searchContainer.addComponent(BorderLayout.EAST, searchButton);
        mainForm.addComponent(searchContainer);
        mainForm.addCommand(searchCommand);
        mainForm.show();
    }
    
    private void exception(Exception ex) {
        ex.printStackTrace();
        Dialog.show("Error", "Error connecting to search service - Turning on DEMO MODE", "OK", null);
        demoMode = true;
        showMainForm();
    }
    
    private void showSearchResultForm(String searchFor, String location, String street, String sortOrder) {
        final Form resultForm = createForm("result list");
        resultForm.setScrollable(false);
        resultForm.setLayout(new BorderLayout());
        InfiniteProgressIndicator tempIndicator = null;
        try {
            tempIndicator = new InfiniteProgressIndicator(Image.createImage("/wait-circle.png"));
        } catch (IOException ex) {
            tempIndicator = null;
            ex.printStackTrace();
        }
        final InfiniteProgressIndicator indicator = tempIndicator;
        final List resultList = new List(new LocalResultModel(searchFor, location, sortOrder, street)) {
            public boolean animate() {
                boolean val = super.animate();
                
                // return true of animate only if there is data loading, this saves battery and CPU
                if(indicator.animate()) {
                    int index = getSelectedIndex();
                    index = Math.max(0, index - 4);
                    ListModel model = getModel();
                    int dest = Math.min(index + 4, model.getSize());
                    for(int iter = index ; iter < dest ; iter++) {
                        if(model.getItemAt(index) == LOADING_MARKER) {
                            return true;
                        }
                    }
                }
                return val;
            }
        };
        Links pro = new Links();
        pro.title = "prototype";
        pro.tel = "9999999999";
        pro.distance = "9999999";
        pro.address = "Long address string";
        pro.rating = "5";
        resultList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showMap(resultForm, resultList.getSelectedItem());
            }
        });
        resultList.setRenderingPrototype(pro);
        resultList.setFixedSelection(List.FIXED_NONE_CYCLIC);
        resultList.getStyle().setBorder(null);

        resultList.setRenderer(new DefaultListCellRenderer(false) {
            private Label focus;
            private Container selected;
            private Label firstLine;
            private Label secondLine;
            private boolean loading;
            
            {
                selected = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                firstLine = new Label("First Line");
                secondLine = new Label("Second Line");
                int iconWidth = 20;
                firstLine.getStyle().setMargin(LEFT, iconWidth);
                secondLine.getStyle().setMargin(LEFT, iconWidth);
                selected.addComponent(firstLine);
                selected.addComponent(secondLine);
            }
            
            public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
                if(value == null || value == LOADING_MARKER) {
                    loading = true;
                    if(isSelected) {
                        firstLine.setText("Loading...");
                        secondLine.setText("Loading...");
                        return selected;
                    }
                    return indicator;
                }
                loading = false;
                if(isSelected) {
                    int listSelectionColor = list.getStyle().getFgColor();
                    firstLine.getStyle().setFgColor(listSelectionColor);
                    secondLine.getStyle().setFgColor(listSelectionColor);
                    firstLine.getStyle().setBgTransparency(0);
                    secondLine.getStyle().setBgTransparency(0);
                    Links l = (Links)value;
                    firstLine.setText(l.address + " " + l.tel);
                    secondLine.setText(l.distance + " miles " + ("".equals(l.rating) ? "" : ", " + l.rating + "*"));
                    return selected;
                }
                super.getListCellRendererComponent(list, ((Links)value).title, index, isSelected);
                return this;
            }
            
            public void paint(Graphics g) {
                if(loading) {
                    indicator.setX(getX());
                    indicator.setY(getY());
                    indicator.setWidth(getWidth());
                    indicator.setHeight(getHeight());
                    indicator.paint(g);
                } else {
                    super.paint(g);
                }
            }
            
            public Component getListFocusComponent(List list) {
                if(focus == null) {
                    try {
                        focus = new Label(Image.createImage("/svgSelectionMarker.png"));
                        focus.getStyle().setBgTransparency(0);
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }
                }
                return focus;
            }
        });
        resultForm.addComponent(BorderLayout.CENTER, resultList);
        resultForm.addCommand(new Command("Map") {
            public void actionPerformed(ActionEvent ev) {
                showMap(resultForm, resultList.getSelectedItem());
            }
        });
        resultForm.addCommand(new Command("Details") {
            public void actionPerformed(ActionEvent ev) {
                showDetails(resultForm, resultList.getSelectedItem());
            }
        });
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                showMainForm();
            }
        };
        resultForm.setBackCommand(backCommand);
        resultForm.addCommand(backCommand);
        resultForm.addCommand(exitCommand);
        resultForm.show();
    }
    
    private Label createSubLabel(String text, int fgColor, Font f) {
        Label l = new Label(text);
        Style s = l.getStyle();
        s.setFgColor(fgColor);
        s.setFont(f);
        return l;
    }
    
    private void showDetails(final Form resultForm, final Object selectedItem) {
        if(selectedItem != null && selectedItem instanceof Links) {
            Links l = (Links)selectedItem;
            int fgColor = resultForm.getStyle().getFgColor();
            Font standardFont = resultForm.getStyle().getFont();
            Form details = createForm(l.title);
            details.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            details.addComponent(createSubLabel("address", fgColor, standardFont));
            details.addComponent(new Label(l.address));
            details.addComponent(createSubLabel("distance", fgColor, standardFont));
            details.addComponent(new Label(l.distance + " mi."));
            details.addComponent(createSubLabel("average rating", fgColor, standardFont));
            details.addComponent(new Label(l.rating + "*"));
            details.addComponent(createSubLabel("telephone", fgColor, standardFont));
            details.addComponent(new Label(l.tel));
            details.addCommand(exitCommand);
            Command backCommand = new Command("Back") {
                public void actionPerformed(ActionEvent ev) {
                    resultForm.show();
                }
            };
            details.setBackCommand(backCommand);
            details.addCommand(backCommand);
            details.setTransitionInAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 400));
            details.setTransitionOutAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
            details.show();
        }
    }
    
    private void showMap(final Form resultForm, final Object selectedItem) {
        // might still be downloading the entry...
        if(selectedItem instanceof Links) {
            final Links link = (Links)selectedItem;
            if (link == null) {
                return;
            }

            final Label mapLabel = new Label();
            final ImageDownloadService img = new ImageDownloadService("", mapLabel);
            final Dialog progress = new Dialog();
            ConnectionRequest con = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException  {
                    JSONParser p = new JSONParser();
                    Hashtable results = (Hashtable)p.parse(new InputStreamReader(input)).get("ResultSet");
                    String url = (String)results.get("Result");
                    img.setUrl(url);
                    img.addRequestHeader("Accept", "image/png");
                    img.setDisposeOnCompletion(progress);
                    NetworkManager.getInstance().addToQueue(img);
                }
            };
            con.setUrl(MAP_BASE);
            con.setPriority(ConnectionRequest.PRIORITY_HIGH);
            con.setPost(false);
            con.addArgument("output", "json");
            con.addArgument("appid", APPID);
            con.addArgument("latitude", link.latitude);
            con.addArgument("longitude", link.longitude);
            con.addArgument("image_height", Integer.toString((int)(Display.getInstance().getDisplayHeight() * 1.5)));
            con.addArgument("image_width", Integer.toString((int)(Display.getInstance().getDisplayWidth() * 1.5)));
            con.addArgument("zoom", Integer.toString(zoom));

            progress.getDialogStyle().setBorder(Border.createRoundBorder(6, 6, 0xe3ef5a));
            progress.setTransitionInAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 400));
            progress.setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 400));
            ((FlowLayout)progress.getLayout()).setValign(Component.CENTER);
            progress.addComponent(new Label("Please Wait"));
            SliderBridge b = new SliderBridge(new ConnectionRequest[]{ con, img });
            progress.addComponent(b);
            NetworkManager.getInstance().addToQueue(con);
            progress.showPacked(BorderLayout.CENTER, true);
            final Form map = createForm("Map");
            map.setScrollableX(true);
            map.setScrollableY(true);
            map.setTransitionInAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 400));
            map.setTransitionOutAnimator(CommonTransitions.createFastSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
            map.setLayout(new BorderLayout());
            map.addComponent(BorderLayout.CENTER, mapLabel);
            if(zoom < 9) {
                map.addCommand(new Command("Zoom Out") {
                    public void actionPerformed(ActionEvent ev) {
                        zoom++;
                        map.setTransitionOutAnimator(null);
                        showMap(resultForm, selectedItem);
                    }
                });
            }
            if(zoom > 1) {
                map.addCommand(new Command("Zoom In") {
                    public void actionPerformed(ActionEvent ev) {
                        zoom--;
                        map.setTransitionOutAnimator(null);
                        showMap(resultForm, selectedItem);
                    }
                });
            }
            Command backCommand = new Command("Back") {
                public void actionPerformed(ActionEvent ev) {
                    resultForm.show();
                }
            };
            map.addCommand(backCommand);
            map.setBackCommand(backCommand);
            map.addCommand(exitCommand);
            map.show();
        } else {
            Dialog.show("Please Wait", "Please wait for download to complete", "OK", null);
        }
    }
    
    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) {
    }

    private static class Links {
        String title;
        String business;
        String listing;
        String map;
        String tel;
        String latitude;
        String longitude;
        String rating;
        String address;
        String distance;
    };

    private Form createForm(String title) {
        Form f = new Form(title);
        f.getTitleComponent().setAlignment(Component.LEFT);
        f.setMenuCellRenderer(new DefaultListCellRenderer(false));
        return f;
    }
    
    /**
     * A list model that lazily fetches a result over the web if its unavailable
     */
    class LocalResultModel implements ListModel {
        private Vector cache;
        private boolean fetching;
        private Vector fetchQueue = new Vector();
        private Vector dataListeners = new Vector();

        private Vector selectionListeners = new Vector();

        private int selectedIndex = 0;
        private boolean firstTime = true;
        private String searchFor;
        private String location;
        private String sortOrder;
        private String street;
        public LocalResultModel(String searchFor, String location, String sortOrder, String street) {
            cache = new Vector();
            cache.setSize(1);
            this.searchFor = searchFor;
            this.location = location;
            this.sortOrder = sortOrder.toLowerCase();
            this.street = street;
        }
        
        private void fetch(final int startOffset) {
            int count = Math.min(cache.size(), startOffset + 9);
            for(int iter = startOffset - 1 ; iter < count ; iter++) {
                if(cache.elementAt(iter) == null) {
                    cache.setElementAt(LOADING_MARKER, iter);
                }
            }
            NetworkManager.getInstance().addToQueue(fetchEntry(startOffset));
        }
        
        private ConnectionRequest fetchEntry(final int startOffset) {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException  {
                    JSONParser p = new JSONParser();
                    Hashtable result = p.parse(new InputStreamReader(input));
                    result = (Hashtable)result.get("ResultSet");
                    if(startOffset == 1) {
                        // Yahoo now blocks anything over 250 results!
                        cache.setSize(Math.min(250, ((Double)result.get("totalResultsAvailable")).intValue()));
                    }
                    Vector entries = (Vector)result.get("Result");
                    if(cache.size() < entries.size() + startOffset) {
                        cache.setSize(entries.size() + startOffset);
                    }
                    for(int iter = 0 ; iter < entries.size() ; iter++) {
                        result = (Hashtable)entries.elementAt(iter);
                        Links currentLink = new Links();
                        currentLink.title = (String)result.get("Title");
                        currentLink.address = (String)result.get("Address");
                        currentLink.map = (String)result.get("MapUrl");
                        currentLink.listing = (String)result.get("ClickUrl");
                        currentLink.business = (String)result.get("BusinessClickUrl");
                        currentLink.tel = (String)result.get("Phone");
                        currentLink.latitude = (String)result.get("Latitude");
                        currentLink.longitude = (String)result.get("Longitude");
                        currentLink.rating = (String)result.get("AverageRating");
                        currentLink.distance = (String)result.get("Distance");
                        cache.setElementAt(currentLink, startOffset + iter - 1);
                        fireDataChangedEvent(DataChangedListener.CHANGED, startOffset + iter - 1);
                    }
                }
            };
            req.setPost(false);
            req.setUrl(LOCAL_BASE);
            req.addArgument("output", "json");
            req.addArgument("appid", APPID);
            req.addArgument("query", searchFor);
            req.addArgument("location", location);
            req.addArgument("sort", sortOrder.toLowerCase());
            if(street.length() > 0) {
                req.addArgument("street", street);
            }
            req.addArgument("start", Integer.toString(startOffset));
            return req;
        }
        
        public Object getItemAt(int index) {
            Object val = cache.elementAt(index);
            if(val == null) {
                fetch(index + 1);
                return LOADING_MARKER;
            }
            return val;
        }

        public int getSize() {
            return cache.size();
        }

        public void setSelectedIndex(int index) {
            int oldIndex = selectedIndex;
            this.selectedIndex = index;
            fireSelectionEvent(oldIndex, selectedIndex);
        }

        public void addDataChangedListener(DataChangedListener l) {
            dataListeners.addElement(l);
        }

        public void removeDataChangedListener(DataChangedListener l) {
            dataListeners.removeElement(l);
        }

        private void fireDataChangedEvent(final int status, final int index){
            if(!Display.getInstance().isEdt()) {
                Display.getInstance().callSeriallyAndWait(new Runnable() {
                    public void run() {
                        fireDataChangedEvent(status, index);
                    }
                });
                return;
            }
            // we query size with every iteration and avoid an Enumeration since a data 
            // changed event can remove a listener instance thus break the enum...
            for(int iter = 0 ; iter < dataListeners.size() ; iter++) {
                DataChangedListener l = (DataChangedListener)dataListeners.elementAt(iter);
                l.dataChanged(status, index);
            }
        }

        public void addSelectionListener(SelectionListener l) {
            selectionListeners.addElement(l);
        }

        public void removeSelectionListener(SelectionListener l) {
            selectionListeners.removeElement(l);
        }

        private void fireSelectionEvent(int oldIndex, int newIndex){
            Enumeration listenersEnum = selectionListeners.elements();
            while(listenersEnum.hasMoreElements()){
                SelectionListener l = (SelectionListener)listenersEnum.nextElement();
                l.selectionChanged(oldIndex, newIndex);
            }
        }

        public void addItem(Object item) {
        }

        public void removeItem(int index) {
        }
        
        public int getSelectedIndex() {
            return selectedIndex;
        }
    }
}
