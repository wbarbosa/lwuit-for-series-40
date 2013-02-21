package com.nokia.example.listdemo;

import com.nokia.lwuit.templates.list.NokiaListCellRenderer;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

public class TwoRowListCellRenderer extends Component implements ListCellRenderer {

    private static TwoRowListCellRenderer instance;
    private String mainText;
    private String subText;
    private Font mainFont = Font.createSystemFont(
            Font.FACE_SYSTEM,
            Font.STYLE_PLAIN,
            Font.SIZE_MEDIUM);
    private Font subFont = Font.createSystemFont(
            Font.FACE_SYSTEM,
            Font.STYLE_PLAIN,
            Font.SIZE_SMALL);
    private Image image;

    static {
        instance = new TwoRowListCellRenderer();
        instance.setUIID("ListItem");
    }

    private void setMainText(String text) {
        mainText = text;
    }

    private void setSubText(String text) {
        subText = text;
    }

    private void setIcon(Image image) {
        this.image = image;
    }

    public Component getListCellRendererComponent(List list, Object o, int index, boolean selected) {
        if (o instanceof TwoRowListItem) {
            TwoRowListItem item = (TwoRowListItem) o;
            instance.setMainText(item.getTitle());
            instance.setSubText(item.getText());
            instance.setIcon(item.getIcon());
            instance.setFocus(selected);
        }
        return instance;
    }

    public Component getListFocusComponent(List list) {
        return instance;
    }

    public void paint(Graphics g) {
        super.paint(g);
        UIManager.getInstance().getLookAndFeel().setFG(g, this);
        boolean rtl = this.isRTL();
        Style style = getStyle();
        int leftPadding = style.getPadding(rtl, Component.LEFT);
        int topPadding = style.getPadding(rtl, Component.TOP);

        int imgY = getY() + (getHeight() - image.getHeight()) / 2;
        g.drawImage(image, getX() + leftPadding, imgY);

        leftPadding += leftPadding + image.getWidth();

        g.setFont(mainFont);
        g.drawString(mainText, getX() + leftPadding, getY() + topPadding);

        int yOffset = mainFont.getHeight();
        g.setFont(subFont);
        g.drawString(subText, getX() + leftPadding, getY() + topPadding + yOffset);
    }

    public Dimension calcPreferredSize() {

        int textHeight = mainFont.getHeight() + subFont.getHeight();
        int height = Math.max(textHeight, image.getHeight());
        return new Dimension(50, height
                + getStyle().getPadding(false, Component.TOP)
                + getStyle().getPadding(false, Component.BOTTOM));
    }
}
