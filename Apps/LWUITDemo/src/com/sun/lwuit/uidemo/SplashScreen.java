package com.sun.lwuit.uidemo;

import com.sun.lwuit.*;

public class SplashScreen extends Form {
    
    private Image image;
    
    public SplashScreen(Image image) {
        this.image = image;
    }
    
    public void paint(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());
        int x = getWidth() / 2 - image.getWidth() / 2;
        int y = getHeight() / 2 - image.getHeight() / 2;
        g.drawImage(image, x, y);
    }
}
