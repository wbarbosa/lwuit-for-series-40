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

package com.sun.lwuit.resources.editor.editors;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Base class for forms containing common functionality for all forms
 *
 * @author Shai Almog
 */
public class BaseForm extends JPanel {
    private static final TableCellRenderer headerInstance = new HeaderRenderer();
    private static final Icon DOWN_ICON = new ImageIcon(BaseForm.class.getResource("/downarrow.gif"));
    private static final Icon UP_ICON = new ImageIcon(BaseForm.class.getResource("/uparrow.gif"));

    /**
     * Returns the selection in the JXTable mapped to the model since the table
     * might be sorted or filtered
     */
    protected int getModelSelection(JTable t) {
        EditorTable table = (EditorTable)t;
        int r = table.getSelectedRow();
        if(r != -1) {
            r = table.convertRow(r);
        }
        return r;
    }
    
    /**
     * Create an empty sortable JXTable with sorting etc. enabled for a common look
     */
    protected JTable createTable() {
        EditorTable table = new EditorTable();
        try {
            table.getAccessibleContext().setAccessibleName("Table");
            table.getAccessibleContext().setAccessibleDescription("Table");
            table.setFillsViewportHeight(true);
        } catch(Throwable err) {
            // doesn't exist in Java 5
        }
        table.setShowGrid(false);
        return table;
    }

    /**
     * Binds search filtering to a JTable
     */
    protected void bindSearch(final JTextField search, JTable table) {
        final EditorTable jx = (EditorTable) table;
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                updateSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                updateSearch();
            }

            private void updateSearch() {
                String t = search.getText();
                jx.filter(t);
            }
        });
    }

    /**
     * Implementation of the JTable providing sorting, highlighting and filtering 
     */
    public static class EditorTable extends JTable {
        private List<Integer> rows = null;
        private int sortColumn = -1;
        private String filter;
        private boolean ascending;
        private TableModel internalModel;
        
        // initialize a table model that implements sorting and filtering
        public void setModel(TableModel model) {
            internalModel = model;
            super.setModel(new Wrapper());
        }

        public TableModel getInternalModel() {
            return internalModel;
        }

        protected JTableHeader createDefaultTableHeader() {
            return new EditorTableHeader(columnModel);
        }
        
        public void sort(int column) {
            if(sortColumn == column) {
                ascending = !ascending;
            } else {
                ascending = true;
            }
            sortColumn = column;
            if(sortColumn == -1) {
                if(filter == null || filter.length() == 0) {
                    rows = null;
                } else {
                    rows = new ArrayList<Integer>();
                    for(int iter = 0 ; iter < internalModel.getRowCount() ; iter++) {
                        if(checkFilter(iter)) {
                            rows.add(iter);
                        }
                    }
                }
            } else {
                TreeMap<Object, List<Integer>> sorter = new TreeMap<Object, List<Integer>>(new Comparator<Object>() {
                    public int compare(Object o1, Object o2) {
                        if(o1 == null) {
                            return -1;
                        }
                        if(o2 == null) {
                            return 1;
                        }
                        if(o1.getClass() == o2.getClass()) {
                            if(o1 instanceof Comparable) {
                                if(o1 instanceof String && o2 instanceof String) {
                                    return String.CASE_INSENSITIVE_ORDER.compare((String)o1, (String)o2);
                                }

                                return ((Comparable)o1).compareTo(o2);
                            }
                        }
                        return -1;
                    }
                });
                for(int iter = 0 ; iter < internalModel.getRowCount() ; iter++) {
                    if(checkFilter(iter)) {
                        Object o = internalModel.getValueAt(iter, column);
                        if(sorter.containsKey(o)) {
                            sorter.get(o).add(iter);
                        } else {
                            List<Integer> l = new ArrayList<Integer>();
                            l.add(iter);
                            sorter.put(o, l);
                        }
                    }
                }
                rows = new ArrayList<Integer>();
                for(List<Integer> entries : sorter.values()) {
                    rows.addAll(entries);
                }
                if(!ascending) {
                    Collections.reverse(rows);
                }
            }   
            ((Wrapper)getModel()).tableChanged(new TableModelEvent(getModel(), -1, -1, -1, TableModelEvent.ALL_COLUMNS));
        }
        
        public boolean isAscending() {
            return ascending;
        }
        
        private boolean checkFilter(int row) {
            if(filter != null && filter.length() > 0) {
                for(int iter = 0 ; iter < internalModel.getColumnCount() ; iter++) {
                    Object o = internalModel.getValueAt(row, iter);
                    if(o instanceof String) {
                        if(((String)o).toUpperCase().indexOf(filter) > -1) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }
        
        public void filter(String text) {
            filter = text.toUpperCase();
            ascending = !ascending;
            sort(sortColumn);
        }
        
        public int getSortedColumn() {
            return sortColumn;
        }
        
        public int convertRow(int i) {
            return ((Wrapper)getModel()).mapRow(i);
        }
        
        class Wrapper implements TableModel, TableModelListener {
            private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

            public Wrapper() {
                internalModel.addTableModelListener(this);
            }

            public int getRowCount() {
                if(rows == null) {
                    return internalModel.getRowCount();
                }
                return rows.size();
            }

            public int getColumnCount() {
                return internalModel.getColumnCount();
            }

            public String getColumnName(int columnIndex) {
                return internalModel.getColumnName(columnIndex);
            }

            public Class<?> getColumnClass(int columnIndex) {
                return internalModel.getColumnClass(columnIndex);
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if(rows == null) {
                    return internalModel.isCellEditable(rowIndex, columnIndex);
                } else {
                    return internalModel.isCellEditable(rows.get(rowIndex), columnIndex);
                }
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if(rows == null) {
                    return internalModel.getValueAt(rowIndex, columnIndex);
                } else {
                    return internalModel.getValueAt(rows.get(rowIndex), columnIndex);
                }
            }

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if(rows == null) {
                    internalModel.setValueAt(aValue, rowIndex, columnIndex);
                } else {
                    internalModel.setValueAt(aValue, rows.get(rowIndex), columnIndex);
                }
            }

            public void addTableModelListener(TableModelListener l) {
                if(!listeners.contains(l)) {
                    listeners.add(l);
                }
            }

            public void removeTableModelListener(TableModelListener l) {
                listeners.remove(l);
            }

            private int mapRow(int row) {
                if(row < 0 || rows == null) {
                    return row;
                }
                return rows.get(row);
            }
            
            private int reverseMapRow(int row) {
                for(int iter = 0 ; iter < rows.size() ; iter++) {
                    if(rows.get(iter).intValue() == row) {
                        return iter;
                    }
                }
                return -1;
            }
            
            public void tableChanged(TableModelEvent e) {
                if(rows != null) {
                    e = new TableModelEvent(this, reverseMapRow(e.getFirstRow()), reverseMapRow(e.getLastRow()), e.getColumn(), e.getType());
                }
                for(TableModelListener l : listeners) {
                    l.tableChanged(e);
                }
            }
        }   
    }
    
    private static class EditorTableHeader extends JTableHeader implements MouseInputListener{
        private TableColumn cachedResizingColumn;

        public EditorTableHeader(TableColumnModel columnModel) {
            super(columnModel);
        }

        public void setTable(JTable table) {
            super.setTable(table);
            installHeaderListener();
        }

        private int getViewIndexForColumn(TableColumn col) {
            if (col == null) {
                return -1;
            }
            TableColumnModel cm = getColumnModel();
            for (int column = 0; column < cm.getColumnCount(); column++) {
                if (cm.getColumn(column) == col) {
                    return column;
                }
            }
            return -1;
        }

        protected TableCellRenderer createDefaultRenderer() {
            return headerInstance;
        }

        protected void installHeaderListener() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        protected void uninstallHeaderListener() {
            removeMouseListener(this);
            removeMouseMotionListener(this);
        }

        public void mouseClicked(MouseEvent e) {
            if (shouldIgnore(e)) {
                return;
            }
            if (isInResizeRegion(e)) {
                doResize(e);
            } else {
                doSort(e);
            }
        }

        private boolean shouldIgnore(MouseEvent e) {
            return !SwingUtilities.isLeftMouseButton(e)
              || !table.isEnabled();
        }

        private void doSort(MouseEvent e) {
            EditorTable table = (EditorTable)getTable();
            if (e.getClickCount() != 1) {
                return;
            }
            
            if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK) {
                table.sort(-1);
            } else {
                int column = columnAtPoint(e.getPoint());
                if (column >= 0) {
                    table.sort(column);
                }
                uncacheResizingColumn();
            }
            repaint();

        }

        private void doResize(MouseEvent e) {
            if (e.getClickCount() != 2)
                return;
            int column = getViewIndexForColumn(cachedResizingColumn);
            uncacheResizingColumn();

        }

        public void mouseReleased(MouseEvent e) {
            cacheResizingColumn(e);
        }

        public void mousePressed(MouseEvent e) {
            cacheResizingColumn(e);
        }

        private void cacheResizingColumn(MouseEvent e) {
            if (e.getClickCount() != 1)
                return;
            TableColumn column = getResizingColumn();
            if (column != null) {
                cachedResizingColumn = column;
            }
        }

        private void uncacheResizingColumn() {
            cachedResizingColumn = null;
        }

        private boolean isInResizeRegion(MouseEvent e) {
            return cachedResizingColumn != null; // inResize;
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
            uncacheResizingColumn();
        }

        public void mouseDragged(MouseEvent e) {
            uncacheResizingColumn();
        }

        public void mouseMoved(MouseEvent e) {
        }

    }
    
    private static class HeaderRenderer extends JComponent implements TableCellRenderer {
        private TableCellRenderer internal;

        public HeaderRenderer() {
            JTableHeader header = new JTableHeader();
            internal = header.getDefaultRenderer();
        }

        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
            java.awt.Component cmp = internal.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
            
            int col = ((EditorTable) table).getSortedColumn();

            if (col == columnIndex) {
                ((JLabel)cmp).setIcon(((EditorTable) table).isAscending() ? UP_ICON : DOWN_ICON);
            } else {
                ((JLabel)cmp).setIcon(null);
            }
            return cmp;
        }
    }
}
