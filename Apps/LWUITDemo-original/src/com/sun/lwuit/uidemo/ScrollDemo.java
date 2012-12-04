/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.list.CellRenderer;
import com.sun.lwuit.list.ContainerList;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author cf130546
 */
public class ScrollDemo extends Demo {
    private int orientation;
    
    private String[][] CONTACTS_INFO = {
    {"Nir V.","Nir.Vazana@Sun.COM"},
    {"Tidhar G.","Tidhar.Gilor@Sun.COM"},
    {"Iddo A.","Iddo.Arie@Sun.COM"},
    {"Ari S.","Ari.Shapiro@Sun.COM"},
    {"Chen F.","Chen.Fishbein@Sun.COM"},
    {"Yoav B.","Yoav.Barel@Sun.COM"},
    {"Moshe S.","Moshe.Sambol@Sun.COM"},
    {"Keren S.","Keren.Strul@Sun.COM"},
    {"Amit H.","Amit.Harel@Sun.COM"},
    {"Arkady N.","Arcadi.Novosiolok@Sun.COM"},
    {"Shai A.","Shai.Almog@Sun.COM"},
    {"Elina K.","Elina.Kleyman@Sun.COM"},
    {"Yaniv V.","Yaniv.Vakrat@Sun.COM"},
    {"Ofir L.","Ofir.Leitner@Sun.COM"},
    {"Martin L.","Martin.Lichtbrun@Sun.COM"},
    {"Tamir S.","Tamir.Shabat@Sun.COM"},
    {"Nir S.","Nir.Shabi@Sun.COM"},
    {"Eran K.","Eran.Katz@Sun.COM"},
    {"Meirav N..","Meirav.Nachmanovich@Sun.COM"}
    };
    
    public String getName() {
        return "Scrolling";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("scrollHelp", "Help description");
    }

    protected void executeDemo(final Container f) {
        f.setLayout(new BorderLayout());
        //disable the scroll on the Form.
        f.setScrollable(false);
        Image contacts = null;
        Image  persons[] = null;
       
        int cols = 4;
        int rows = 5;
        try {
            Resources images = UIDemoMain.getResource("images");
            contacts = images.getImage("people.jpg");
            int contactWidth= contacts.getWidth() / cols;
            int contactHeight= contacts.getHeight() / rows;
            persons = new Image[CONTACTS_INFO.length];
            for(int i = 0; i < persons.length ; i++){
                    persons[i] = contacts.subImage((i%cols)*contactWidth, (i/cols)*contactHeight, contactWidth, contactHeight, true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        final Contact[] contactArray = new Contact[5 * persons.length];
        for (int i = 0; i < contactArray.length; i++) {
            int pos = i % CONTACTS_INFO.length;
            contactArray[i] = new Contact(CONTACTS_INFO[pos][0], CONTACTS_INFO[pos][1], persons[pos]);
        }
        f.addComponent(BorderLayout.CENTER, createList(contactArray, List.VERTICAL, new ContactsRenderer()));
        addCommand(new Command("Horizontal") {
            public void actionPerformed(ActionEvent ev) {
                replaceContent(f, createList(contactArray, List.HORIZONTAL, new ImageRenderer()), BorderLayout.NORTH);
            }
        }, f);
        addCommand(new Command("Grid") {
            public void actionPerformed(ActionEvent ev) {
                replaceContent(f, createGridList(contactArray, new ImageRenderer()), BorderLayout.CENTER);
            }
        }, f);
        addCommand(new Command("Vertical") {
            public void actionPerformed(ActionEvent ev) {
                replaceContent(f, createList(contactArray, List.VERTICAL, new ContactsRenderer()), BorderLayout.CENTER);
            }
        }, f);
    }

    private void replaceContent(final Container f, final Component newComponent, final String constraint) {
        final Component cmp = f.getComponentAt(0);
        Object currentPos = f.getLayout().getComponentConstraint(cmp);

        // if we are switching from/to a component in the NORTH (the horizontal list) then we should also shrink the component into the
        // new position. Notice that we don't use replaceAndWait since when we have a menu dialog the wait might block the folding
        // of the dialog.
        if(currentPos != constraint) {
            f.replace(cmp, newComponent, CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 800), new Runnable() {
                public void run() {
                    // setting the removed component to visible false blocks any potential paint operations that might be
                    // already in progress from proceeding
                    cmp.setVisible(false);
                    f.removeAll();
                    f.addComponent(constraint, newComponent);
                    f.animateLayout(400);
                }
            }, -1);
        } else {
            f.replace(cmp, newComponent, CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 800));
        }
    }

    private ContainerList createGridList(Contact[] contacts, CellRenderer renderer) {
        ContainerList list = new ContainerList(new DefaultListModel(contacts));
        list.getStyle().setBgTransparency(0);
        list.setRenderer(renderer);
        GridLayout grid = new GridLayout(5, 5);
        grid.setAutoFit(true);
        list.setLayout(grid);
        return list;
    }

    private List createList(Contact[] contacts, int orientation, ListCellRenderer renderer) {
        List list = new List(contacts);
        this.orientation = orientation;
        list.getStyle().setBgTransparency(0);
        list.setListCellRenderer(renderer);
        list.setOrientation(orientation);
        list.setPaintFocusBehindList(true);
        return list;
    }
    
    class ImageRenderer extends DefaultListCellRenderer {
        private Label focus = new Label("");
        public ImageRenderer() {
            super(false);
            //focus.getStyle().setBgTransparency(100);
            setUIID("ListRenderer");
            focus.setUIID("ListRendererFocus");
            focus.setFocus(true);
        }
        public void refreshTheme() {
            super.refreshTheme();
            focus.refreshTheme();
        }
        public Component getCellRendererComponent(Component list, Object model, Object value, int index, boolean isSelected) {
            super.getCellRendererComponent(list, model, "", index, isSelected);
            setIcon(((Contact) value).getPic());
            return this;
        }
        public Component getListFocusComponent(List list) {
            return focus;
        }
    }

    class ContactsRenderer extends Container implements ListCellRenderer {

        private Label name = new Label("");
        private Label email = new Label("");
        private Label pic = new Label("");

        private Label focus = new Label("");
        
        public ContactsRenderer() {
            setLayout(new BorderLayout());
            addComponent(BorderLayout.WEST, pic);
            Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            name.getStyle().setBgTransparency(0);
            email.getStyle().setBgTransparency(0);
            pic.getStyle().setBgTransparency(0);
            cnt.addComponent(name);
            cnt.addComponent(email);
            addComponent(BorderLayout.CENTER, cnt);
            setUIID("ListRenderer");
            focus.setUIID("ListRendererFocus");
            focus.setFocus(true);
        }

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {

            Contact person = (Contact) value;
            name.setText(person.getName());
            email.setText(person.getEmail());
            pic.setIcon(person.getPic());
            return this;
        }

        public Component getListFocusComponent(List list) {
            return focus;
        }
    }

    class Contact {

        private String name;
        private String email;
        private Image pic;

        public Contact(String name, String email, Image pic) {
            this.name = name;
            this.email = email;
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public Image getPic() {
            return pic;
        }
    }
}
