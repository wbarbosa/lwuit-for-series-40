/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.table.DefaultTableModel;
import com.sun.lwuit.table.Table;
import com.sun.lwuit.table.TableModel;
import com.sun.lwuit.plaf.UIManager;

/**
 * Simple demo showing off how to use the table component in LWUIT
 * 
 * @author Shai Almog
 */
public class TableDemo extends Demo {

    public String getName() {
        return "Table";
    }

    protected void executeDemo(Container f) {
        f.setLayout(new BorderLayout());
        TableModel model = new DefaultTableModel(new String[] {"Uneditable", "Editable", "CheckBox", "Multiline"}, new Object[][] {
          {"Row 1", "", new Boolean(false), "Multi-line text\nright here"},
          {"Row 2", "", new Boolean(true), "Further text that\nspans lines"},
          {"Row 3", "", new Boolean(true), "No span"},
          {"Row 4", "", new Boolean(false), "Spanning\nFor\nEvery\nWord"},
        }) {
          public boolean isCellEditable(int row, int col) {
              return col != 0 && col != 3;
          }
        };
        Table table = new Table(model) {
            private CellListener listener;
            protected Component createCell(Object value, final int row, final int column, boolean editable) {
                if(row != -1) {
                    if(listener == null) {
                        listener = new CellListener(this);
                    }
                    switch(column) {
                        case 2:
                            // checkbox column
                            final CheckBox c = new CheckBox();
                            c.setSelected(((Boolean)value).booleanValue());
                            c.addActionListener(listener);
                            return c;
                        case 3:
                            TextArea t = new TextArea(2, 10);
                            t.setGrowByContent(true);
                            t.setText((String)value);
                            return t;
                    }
                }
                return super.createCell(value, row, column, editable);
            }
        };
        table.setScrollableX(true);
        table.setScrollableY(true);
        f.setScrollable(false);
        f.addComponent(BorderLayout.CENTER, table);
    }

    class CellListener implements ActionListener {
        private Table t;
        public CellListener(Table t) {
            this.t = t;
        }

        public void actionPerformed(ActionEvent evt) {
            Component source = (Component)evt.getSource();
            if(source instanceof CheckBox) {
                t.getModel().setValueAt(t.getCellRow(source), t.getCellColumn(source),
                        new Boolean(((CheckBox)source).isSelected()));
            }
        }
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        return UIManager.getInstance().localize("tableHelp", "Help description");
    }
}
