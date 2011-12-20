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

package javax.microedition.lcdui;
/**
 * A Form is a Screen that contains an arbitrary mixture of items: images, read-only text fields, editable text fields, editable date fields, gauges, choice groups, and custom items. In general, any subclass of the Item class may be contained within a form. The implementation handles layout, traversal, and scrolling. The entire contents of the Form scrolls together.
 * The items contained within a Form may be edited using append, delete, insert, and set methods. Items within a Form are referred to by their indexes, which are consecutive integers in the range from zero to size()-1, with zero referring to the first item and size()-1 to the last item.
 * An item may be placed within at most one Form. If the application attempts to place an item into a Form, and the item is already owned by this or another Form, an IllegalStateException is thrown. The application must remove the item from its currently containing Form before inserting it into the new Form.
 * If the Form is visible on the display when changes to its contents are requested by the application, updates to the display take place as soon as it is feasible for the implementation to do so. Applications need not take any special action to refresh a Form's display after its contents have been modified.
 * Layout policy in Form is organized around rows. Rows are typically related to the width of the screen, respective of margins, scroll bars, and such. All rows in a particular Form will have the same width. Rows do not vary in width based on the Items contained within the Form, although they may all change width in certain circumstances, such as when a scroll bar needs to be added or removed. Forms generally do not scroll horizontally.
 * Forms grow vertically and scroll vertically as necessary. The height of a Form varies depending upon the number of rows and the height of each row. The height of each row is determined by the items that are positioned on that row. Rows need not all have the same height. Implementations may also vary row heights to provide proper padding or vertical alignment of Item labels.
 * An implementation may choose to lay out Items in a left-to-right or right-to-left direction depending upon the language conventions in use. The same choice of layout direction must apply to all rows within a particular Form.
 * Prior to the start of the layout algorithm, the Form is considered to have one empty row at the top. The layout algorithm considers each Item in turn, starting at Item zero and proceeding in order through each Item until the last Item in the Form has been processed. If the layout direction (as described above) is left-to-right, the beginning of the row is the left edge of the Form. If the layout direction is right-to-left, the beginning of the row is the right edge of the Form. Items are laid out at the beginning of each row, proceeding across each row in the chosen layout direction, packing as many Items onto each row as will fit, unless a condition occurs that causes the packing of a row to be terminated early. A new row is then added, and Items are packed onto it as described above. Items are packed onto rows, and new rows are added below existing rows as necessary until all Items have been processed by the layout algorithm.
 * The layout algorithm has a concept of a current alignment. It can have the value LAYOUT_LEFT, LAYOUT_CENTER, or LAYOUT_RIGHT. The value of the current alignment at the start of the layout algorithm depends upon the layout direction in effect for this Form. If the layout direction is left-to-right, the initial alignment value must be LAYOUT_LEFT. If the layout direction is right-to-left, the initial alignment value must be LAYOUT_RIGHT. The current alignment changes when the layout algorithm encounters an Item that has one of the layout directives LAYOUT_LEFT, LAYOUT_CENTER, or LAYOUT_RIGHT. If none of these directives is present on an Item, the current layout directive does not change. This rule has the effect of grouping the contents of the Form into sequences of consecutive Items sharing an alignment value. The alignment value of each Item is maintained internally to the Form and does not affect the Items' layout value as reported by the Item.getLayout method.
 * The layout algorithm generally attempts to place an item on the same row as the previous item, unless certain conditions occur that cause a row break. When there is a row break, the current item will be placed at the beginning of a new row instead of being placed after the previous item, even if there is room.
 * A row break occurs before an item if any of the following conditions occurs:
 * A row break occurs after an item if any of the following conditions occurs:
 * The presence of the LAYOUT_NEWLINE_BEFORE or LAYOUT_NEWLINE_AFTER directive does not cause an additional row break if there is one already present. For example, if a LAYOUT_NEWLINE_BEFORE directive appears on a StringItem whose contents starts with \n, there is only a single row break. A similar rule applies with a trailing \n and LAYOUT_NEWLINE_AFTER. Also, there is only a single row break if an item has the LAYOUT_NEWLINE_AFTER directive and the next item has the LAYOUT_NEWLINE_BEFORE directive. However, the presence of consecutive \n characters, either within a single StringItem or in adjacent StringItems, will cause as many row breaks as there are \n characters. This will cause empty rows to be present. The height of an empty row is determined by the prevailing font height of the StringItem within which the \n that ends the row occurs.
 * Implementations may provide additional conditions under which a row break occurs. For example, an implementation's layout policy may lay out labels specially, implicitly causing a break before every Item that has a label. Or, as another example, a particular implementation's user interface style may dictate that a DateField item always appears on a row by itself. In this case, this implementation may cause row breaks to occur both before and after every DateField item.
 * Given two items with adjacent Form indexes, if none of the specified or implementation-specific conditions for a row break between them occurs, and if space permits, these items should be placed on the same row.
 * When packing Items onto a row, the width of the item is compared with the remaining space on the row. For this purpose, the width used is the Item's preferred width, unless the Item has the LAYOUT_SHRINK directive, in which case the Item's minimum width is used. If the Item is too wide to fit in the space remaining on the row, the row is considered to be full, a new row is added beneath this one, and the Item is laid out on this new row.
 * Once the contents of a row have been determined, the space available on the row is distributed by expanding items and by adding space between items. If any items on this row have the LAYOUT_SHRINK directive (that is, they are shrinkable), space is first distributed to these items. Space is distributed to each of these items proportionally to the difference between the each Item's preferred size and its minimum size. At this stage, no shrinkable item is expanded beyond its preferred width.
 * For example, consider a row that has 30 pixels of space available and that has two shrinkable items A and B. Item A's preferred size is 15 and its minimum size is 10. Item B's preferred size is 30 and its minimum size is 20. The difference between A's preferred and minimum size is 5, and B's difference is 10. The 30 pixels are distributed to these items proportionally to these differences. Therefore, 10 pixels are distributed to item A and 20 pixels to item B.
 * If after expanding all the shrinkable items to their preferred widths, there is still space left on the row, this remaining space is distributed equally among the Items that have the LAYOUT_EXPAND directive (the stretchable Items). The presence of any stretchable items on a row will cause the Items on this row to occupy the full width of the row.
 * If there are no stretchable items on this row, and there is still space available on this row, the Items are packed as tightly as possible and are placed on the row according to the alignment value shared by the Items on this row. (Since changing the current alignment causes a row break, all Items on the same row must share the same alignment value.) If the alignment value is LAYOUT_LEFT, the Items are positioned at the left end of the row and the remaining space is placed at the right end of the row. If the alignment value is LAYOUT_RIGHT, the Items are positioned at the right end of the row and the remaining space is placed at the left end of the row. If the alignment value is LAYOUT_CENTER, the Items are positioned in the middle of the row such that the remaining space on the row is divided evenly between the left and right ends of the row.
 * Given the set of items on a particular row, the heights of these Items are inspected. For each Item, the height that is used is the preferred height, unless the Item has the LAYOUT_VSHRINK directive, in which case the Item's minimum height is used. The height of the tallest Item determines the height of the row. Items that have the LAYOUT_VSHRINK directive are expanded to their preferred height or to the height of the row, whichever is smaller. Items that are still shorter than the row height and that have the LAYOUT_VEXPAND directive will expand to the height of the row. The LAYOUT_VEXPAND directive on an item will never increase the height of a row.
 * Remaining Items shorter than the row height will be positioned vertically within the row using the LAYOUT_TOP, LAYOUT_BOTTOM, and LAYOUT_VCENTER directives. If no vertical layout directive is specified, the item must be aligned along the bottom of the row.
 * StringItems are treated specially in the above algorithm. If the contents of a StringItem (its string value, exclusive of its label) contain a newline character (\n), the string should be split at that point and the remainder laid out starting on the next row.
 * If one or both dimensions of the preferred size of a StringItem have been locked, the StringItem is wrapped to fit that width and height and is treated as a rectangle whose minimum and preferred width and height are the width and height of this rectangle. In this case, the LAYOUT_SHRINK, LAYOUT_EXPAND, and LAYOUT_VEXPAND directives are ignored.
 * If both dimensions of the preferred size of a StringItem are unlocked, the text from the StringItem may be wrapped across multiple rows. At the point in the layout algorithm where the width of the Item is compared to the remaining space on the row, as much text is taken from the beginning of the StringItem as will fit onto the current row. The contents of this row are then positioned according to the current alignment value. The remainder of the text in the StringItem is line-wrapped to the full width of as many new rows as are necessary to accommodate the text. Each full row is positioned according to the current alignment value. The last line of the text might leave space available on its row. If there is no row break following this StringItem, subsequent Items are packed into the remaining space and the contents of the row are positioned according to the current alignment value. This rule has the effect of displaying the contents of a StringItem as a paragraph of text set flush-left, flush-right, or centered, depending upon whether the current alignment value is LAYOUT_LEFT, LAYOUT_RIGHT, or LAYOUT_CENTER, respectively. The preferred width and height of a StringItem wrapped across multiple rows, as reported by the Item.getPreferredWidth and Item.getPreferredHeight methods, describe the width and height of the bounding rectangle of the wrapped text.
 * ImageItems are also treated specially by the above algorithm. The foregoing rules concerning the horizontal alignment value and the LAYOUT_LEFT, LAYOUT_RIGHT, and LAYOUT_CENTER directives, apply to ImageItems only when the LAYOUT_2 directive is also present on that item. If the LAYOUT_2 directive is not present on an ImageItem, the behavior of the LAYOUT_LEFT, LAYOUT_RIGHT, and LAYOUT_CENTER directives is implementation-specific.
 * A Form's layout is recomputed automatically as necessary. This may occur because of a change in an Item's size caused by a change in its contents or because of a request by the application to change the Item's preferred size. It may also occur if an Item's layout directives are changed by the application. The application does not need to perform any specific action to cause the Form's layout to be updated.
 * For all cases where text is wrapped, line breaks must occur at each newline character ('\n' = Unicode 'U+000A'). If space does not permit the full text to be displayed it is truncated at line breaks. If there are no suitable line breaks, it is recommended that implementations break text at word boundaries. If there are no word boundaries, it is recommended that implementations break text at character boundaries.
 * Labels that contain line breaks may be truncated at the line break and cause the rest of the label not to be shown.
 * When a Form is present on the display the user can interact with it and its Items indefinitely (for instance, traversing from Item to Item and possibly scrolling). These traversing and scrolling operations do not cause application-visible events. The system notifies the application when the user modifies the state of an interactive Item contained within the Form. This notification is accomplished by calling the itemStateChanged() method of the listener declared to the Form with the setItemStateListener() method.
 * As with other Displayable objects, a Form can declare commands and declare a command listener with the setCommandListener() method. CommandListener objects are distinct from ItemStateListener objects, and they are declared and invoked separately.
 * Since: MIDP 1.0 See Also:Item
 */
public class Form extends Screen{
    /**
     * Creates a new, empty Form.
     * title - the Form's title, or null for no title
     */
    public Form(java.lang.String title){
         //TODO codavaj!!
    }

    /**
     * Creates a new Form with the specified contents. This is identical to creating an empty Form and then using a set of append methods. The items array may be null, in which case the Form is created empty. If the items array is non-null, each element must be a valid Item not already contained within another Form.
     * title - the Form's title stringitems - the array of items to be placed in the Form, or null if there are no items
     * - if one of the items is already owned by another container
     * - if an element of the items array is null
     */
    public Form(java.lang.String title, Item[] items){
         //TODO codavaj!!
    }

    /**
     * Adds an item consisting of one Image to the Form. The effect of this method is identical to
     * append(new ImageItem(null, img, ImageItem.LAYOUT_DEFAULT, null))
     */
    public int append(Image img){
        return 0; //TODO codavaj!!
    }

    /**
     * Adds an Item into the Form. The newly added Item becomes the last Item in the Form, and the size of the Form grows by one.
     */
    public int append(Item item){
        return 0; //TODO codavaj!!
    }

    /**
     * Adds an item consisting of one String to the Form. The effect of this method is identical to
     * append(new StringItem(null, str))
     */
    public int append(java.lang.String str){
        return 0; //TODO codavaj!!
    }

    /**
     * Deletes the Item referenced by itemNum. The size of the Form shrinks by one. It is legal to delete all items from a Form. The itemNum parameter must be within the range [0..size()-1], inclusive.
     */
    public void delete(int itemNum){
        return; //TODO codavaj!!
    }

    /**
     * Deletes all the items from this Form, leaving it with zero items. This method does nothing if the Form is already empty.
     */
    public void deleteAll(){
        return; //TODO codavaj!!
    }

    /**
     * Gets the item at given position. The contents of the Form are left unchanged. The itemNum parameter must be within the range [0..size()-1], inclusive.
     */
    public Item get(int itemNum){
        return null; //TODO codavaj!!
    }

    /**
     * Returns the height in pixels of the displayable area available for items. This value is the height of the form that can be displayed without scrolling. The value may depend on how the device uses the screen and may be affected by the presence or absence of the ticker, title, or commands.
     */
    public int getHeight(){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the width in pixels of the displayable area available for items. The value may depend on how the device uses the screen and may be affected by the presence or absence of the ticker, title, or commands. The Items of the Form are laid out to fit within this width.
     */
    public int getWidth(){
        return 0; //TODO codavaj!!
    }

    /**
     * Inserts an item into the Form just prior to the item specified. The size of the Form grows by one. The itemNum parameter must be within the range [0..size()], inclusive. The index of the last item is size()-1, and so there is actually no item whose index is size(). If this value is used for itemNum, the new item is inserted immediately after the last item. In this case, the effect is identical to
     * .
     * The semantics are otherwise identical to append(Item).
     */
    public void insert(int itemNum, Item item){
        return; //TODO codavaj!!
    }

    /**
     * Sets the item referenced by itemNum to the specified item, replacing the previous item. The previous item is removed from this Form. The itemNum parameter must be within the range [0..size()-1], inclusive.
     * The end result is equal to insert(n, item); delete(n+1); although the implementation may optimize the repainting and usage of the array that stores the items.
     */
    public void set(int itemNum, Item item){
        return; //TODO codavaj!!
    }

    /**
     * Sets the ItemStateListener for the Form, replacing any previous ItemStateListener. If iListener is null, simply removes the previous ItemStateListener.
     */
    public void setItemStateListener(ItemStateListener iListener){
        return; //TODO codavaj!!
    }

    /**
     * Gets the number of items in the Form.
     */
    public int size(){
        return 0; //TODO codavaj!!
    }

}
