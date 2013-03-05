package com.nokia.lwuit.components;

import com.sun.lwuit.*;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * A component that visually displays the progress of an operation. The
 * component can be used in indeterministic or deterministic mode.
 * 
 * In indeterminate mode the progress is indicated by spinning animation. It
 * is meant for cases where the progress cannot be reliably measured.
 * 
 * In deterministic mode the progress is represented by a progress bar. This
 * mode simply delegates the functionality to Slider component.
 * 
 */
public class ProgressIndicator extends Component {

    private boolean indeterminate;        
    private Image image;
    private Image rotatedImage;
    private int angle;
    private Style indeterminateStyle;
    private Slider progressBar;
    private long timeStamp;    
    private static final int MILLISECONDS_BETWEEN_UPDATES = 66;
    
    /** 
     * Constructs a new indeterminate ProgressIndicator.
     * 
     */
    public ProgressIndicator() {
        this(true);
    }
    
    /** 
     * Constructs a new ProgressIndicator.
     * 
     * @param indeterminate if true the progress indicator will be indeterminate.
     */
    public ProgressIndicator(boolean indeterminate) {
        this.indeterminate = indeterminate;
        indeterminateStyle = UIManager.getInstance().getComponentStyle("ProgressIndicator");
        image = indeterminateStyle.getBgImage();
        rotatedImage = image;

        progressBar = new Slider();
        progressBar.setInfinite(false);
        progressBar.setEditable(false);

        this.setFocusable(false);
    }

    /**
     * @inheritDoc
     */
    public void initComponent() {
        if (indeterminate) {
            getComponentForm().registerAnimated(this);
        }
    }

    /**
     * @inheritDoc
     */
    public void deinitialize() {
        if (indeterminate) {
            Form f = getComponentForm();
            if (f != null) {
                f.deregisterAnimated(this);
            }
        }
    }

    /**
     * Sets the value of the determinate progress bar. The value must be in 0 - 100 range.
     *
     * @param value percentage value
     */
    public void setProgress(int progress) {
        progressBar.setSliderValue(progress);
        repaint();
    }

    /**
     * Indicates the value of the determinate progress bar.
     *
     * @return the value of the progress bar.
     */    
    public int getProgress() {
        return progressBar.getSliderValue();
    }

    /**
     * Tells whether the progress indicator is in indeterminate or determinate mode.
     *
     * @return true if the progress indicator is in indeterminate mode.
     */ 
    public boolean isIndeterminate() {
        return indeterminate;
    }

    /**
     * Sets the mode of the progress indicator.
     *
     * @param indeterminate if true the progress indicator will be set to indeterminate mode.
     */ 
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    /**
     * @inheritDoc
     */
    public void setX(int x) {
        progressBar.setX(x);
        super.setX(x);
    }

    /**
     * @inheritDoc
     */
    public void setY(int y) {
        progressBar.setY(y);
        super.setY(y);
    }

    /**
     * @inheritDoc
     */
    public void setWidth(int w) {
        progressBar.setWidth(w);
        super.setWidth(w);
    }

    /**
     * @inheritDoc
     */
    public void setHeight(int h) {
        progressBar.setHeight(h);
        super.setHeight(h);
    }

    /**
     * @inheritDoc
     */
    public void setSize(Dimension dim) {
        progressBar.setSize(dim);
        super.setSize(dim);
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        long curTime = System.currentTimeMillis();
        long delta = curTime - timeStamp;

        if (image != null && delta > MILLISECONDS_BETWEEN_UPDATES) {
            angle += 45;
            if (angle >= 360) {
                angle -= 360;
            }
            rotatedImage = image.rotate((int) angle);
            timeStamp = curTime;
            return true; // repaint is needed
        }
        return false; // no repaint needed
    }

    /**
     * @inheritDoc
     */
    public void paintBackground(Graphics g) {
        if (!indeterminate) {
            progressBar.paintBackground(g);
        }
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        if (indeterminate && rotatedImage != null) {
            g.drawImage(rotatedImage, this.getX(), this.getY());
        }
    }

    /**
     * @inheritDoc
     */
    public Dimension calcPreferredSize() {

        if (indeterminate && image != null) {
            int prefW = 0, prefH = 0;
            if (indeterminateStyle.getBorder() != null) {
                prefW = Math.max(indeterminateStyle.getBorder().getMinimumWidth(), prefW);
                prefH = Math.max(indeterminateStyle.getBorder().getMinimumHeight(), prefH);
            }
            prefW += image.getWidth();
            prefH += image.getHeight();
            return new Dimension(prefW, prefH);
        } else {
            if (progressBar != null) {
                return progressBar.getPreferredSize();
            } else {
                return super.calcPreferredSize();
            }
        }
    }
}
