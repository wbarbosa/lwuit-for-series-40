/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.sun.lwuit.uidemo;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.layouts.GroupLayout;
import com.sun.lwuit.layouts.LayoutStyle;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.table.TableLayout;
import java.util.Random;

/**
 * Demonstrates the various layout managers that are a part of LWUIT
 *
 * @author Chen Fishbein
 */
public class LayoutDemo extends Demo {

    private Button group;
    private Button coordinate;
    private Button border;
    
    private Button boxY;
    
    private Button boxX;
    
    private Button flow;
    
    private Button grid;

    private Button table;
    
     public void cleanup() {
         border = null;
         boxY = null;
         flow = null;
         grid = null;
         boxX = null;
         table = null;
     }

    public String getName() {
        return "Layouts";
    }

    protected String getHelp() {
        return UIManager.getInstance().localize("layoutHelp", "Help description");
    }

    protected void executeDemo(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addCommand(new Command("Shuffle") {
            private Random r = new Random();
            public void actionPerformed(ActionEvent ev) {
                int w = f.getWidth();
                int h = f.getHeight();
                int x = w * 2;
                int y = h * 2;
                group.setX(r.nextInt(x) - w);
                group.setY(r.nextInt(y) - h);
                coordinate.setX(r.nextInt(x) - w);
                coordinate.setY(r.nextInt(y) - h);
                border.setX(r.nextInt(x) - w);
                border.setY(r.nextInt(y) - h);
                boxY.setX(r.nextInt(x) - w);
                boxY.setY(r.nextInt(y) - h);
                boxX.setX(r.nextInt(x) - w);
                boxX.setY(r.nextInt(y) - h);
                flow.setX(r.nextInt(x) - w);
                flow.setY(r.nextInt(y) - h);
                grid.setX(r.nextInt(x) - w);
                grid.setY(r.nextInt(y) - h);
                table.setX(r.nextInt(x) - w);
                table.setY(r.nextInt(y) - h);
                f.setShouldCalcPreferredSize(true);
                f.animateLayout(800);
            }
        }, f);
        border = new Button("BorderLayout");
        
        border.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new BorderLayout());
                f.removeAll();
                f.setScrollable(false);
                f.addComponent(BorderLayout.NORTH, border);
                f.addComponent(BorderLayout.EAST, boxY);
                f.addComponent(BorderLayout.CENTER, grid);
                f.addComponent(BorderLayout.WEST, flow);
                f.addComponent(BorderLayout.SOUTH, boxX);
                f.animateLayout(500);
            }
        });
        boxY = new Button("BoxLayout-Y");
        boxY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                f.setScrollable(true);
                addComponents(f);
                f.animateLayout(500);
            }
        });
        flow = new Button("FlowLayout");
        flow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new FlowLayout());
                f.setScrollable(false);
                addComponents(f);
                f.animateLayout(500);
            }
        });
        
        grid = new Button("GridLayout");
        grid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new GridLayout(3, 2));
                f.setScrollable(false);
                addComponents(f);
                f.animateLayout(500);
            }
        });
        
        boxX = new Button("BoxLayout-X");
        boxX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new BoxLayout(BoxLayout.X_AXIS));
                f.setScrollable(true);
                addComponents(f);
                f.animateLayout(500);
            }
        });
        
        coordinate = new Button("Coordinate");
        coordinate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new CoordinateLayout(100, 100));
                f.setScrollable(false);
                addComponents(f);
                boxY.setX(0);
                boxY.setY(0);
                boxX.setX(10);
                boxX.setY(12);
                border.setX(20);
                border.setY(24);
                flow.setX(30);
                flow.setY(36);
                grid.setX(40);
                grid.setY(50);
                group.setX(50);
                group.setY(62);
                coordinate.setX(60);
                coordinate.setY(75);
                table.setX(70);
                table.setY(88);

                f.animateLayout(500);
            }
        });


        table = new Button("Table");
        table.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                f.setLayout(new TableLayout(3, 3));
                f.setScrollable(false);
                addComponents(f);
                f.animateLayout(500);
            }
        });

        group = new Button("Group");
        group.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GroupLayout layout = new GroupLayout(f);
                f.setLayout(layout);
                f.setScrollableX(true);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(group)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(coordinate)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(grid))
                            .add(layout.createSequentialGroup()
                                .add(border)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(boxY, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(boxX, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.UNRELATED)
                                .add(flow, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.UNRELATED)
                                .add(table)))
                        .addContainerGap())
                );

                layout.linkSize(new Component[] {border, coordinate, flow, group}, GroupLayout.HORIZONTAL);

                layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                            .add(group)
                            .add(coordinate)
                            .add(grid))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                            .add(border)
                            .add(boxY))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                            .add(boxX, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(flow, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(table, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                f.animateLayout(800);
            }
        });



        addComponents(f);
    }
    
    private void addComponents(final Container f){
        f.removeAll();
        f.addComponent(boxY);
        f.addComponent(boxX);
        f.addComponent(border);
        f.addComponent(flow);
        f.addComponent(grid);
        f.addComponent(group);
        f.addComponent(coordinate);
        f.addComponent(table);
    }
}