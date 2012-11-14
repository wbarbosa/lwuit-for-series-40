/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package net.java.dev.lwuit.speed;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.util.Resources;
import java.io.IOException;

/**
 * Tests how fast blitting is performed on the device with LWUIT
 *
 * @author Shai Almog
 */
public class ListTest extends Form {
    private static int averageFramerate;
    private String[][] CONTACTS_INFO = {
    {"Nir V.","email removed"},
    {"Tidhar G.","email removed"},
    {"Iddo A.","email removed"},
    {"Ari S.","email removed"},
    {"Chen F.","email removed"},
    {"Yoav B.","email removed"},
    {"Moshe S.","email removed"},
    {"Keren S.","email removed"},
    {"Amit H.","email removed"},
    {"Arkady N.","email removed"},
    {"Shai A.","email removed"},
    {"Elina K.","email removed"},
    {"Yaniv V.","email removed"},
    {"Nadav B.","email removed"},
    {"Martin L.","email removed"},
    {"Tamir S.","email removed"},
    {"Nir S.","email removed"},
    {"Eran K.","email removed"}
    };
        
    private static final int TEST_DURATION = 10000;
    private static final int RAMP_UP_DURATION = 1000;
    private long startTime = System.currentTimeMillis();
    private static int paintCalls;
    
    public static int getFramecount() {
        return paintCalls;
    }
    public ListTest() {
        setLayout(new BorderLayout());
        
        // disable the scroll on the Form.
        setScrollable(false);
        Image contacts = null;
        Image  persons[] = null;
       
        //some constants to help us parse the people image
        int contactWidth= 36;
        int contactHeight= 48;
        int cols = 4;
 
        try {
            Resources images = Resources.open("/images.res");
            contacts = images.getImage("people.jpg");
            images = null;
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
        addComponent(BorderLayout.CENTER, createList(contactArray, List.VERTICAL, new ContactsRenderer()));
        
        final int downKey = Display.getInstance().getKeyCode(Display.GAME_DOWN);
        new Thread() {
            public void run() {
                while(System.currentTimeMillis() - startTime < TEST_DURATION) {
                    Display.getInstance().callSeriallyAndWait(new Runnable() {
                        public void run() {
                            ListTest.this.keyPressed(downKey);
                            ListTest.this.keyReleased(downKey);
                        }
                    });
                }
                averageFramerate = paintCalls / ((TEST_DURATION - RAMP_UP_DURATION) / 1000);
                new ComponentTest();
            }
        }.start();
        show();
    }
    
    public static int getAverageFramerate() {
        return averageFramerate;
    }
    
    private List createList(Contact[] contacts, int orientation, ListCellRenderer renderer) {
        List list = new List(contacts) {
            public void paint(Graphics g) {
                if(System.currentTimeMillis() - startTime > RAMP_UP_DURATION) {
                    paintCalls++;
                }
                super.paint(g);
            }
        };
        list.setSmoothScrolling(false);
        list.getStyle().setBgTransparency(0);
        list.setListCellRenderer(renderer);
        list.setOrientation(orientation);
        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        return list;
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
            name.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            email.getStyle().setBgTransparency(0);
            cnt.addComponent(name);
            cnt.addComponent(email);
            addComponent(BorderLayout.CENTER, cnt);
            setCellRenderer(true);
            name.setCellRenderer(true);
            email.setCellRenderer(true);
            pic.setCellRenderer(true);
            focus.setCellRenderer(true);
            focus.getStyle().setBgTransparency(100);
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
