/*
 * @(#)Filler.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

/**
 * A component that can be used to reserve white space in a layout.
 */


public  class Filler
        extends Canvas {

	private static final long serialVersionUID = -5943560414904049215L;
	private int     fWidth;
    private int     fHeight;
    private Color   fBackground;


    public Filler(int width, int height) {
        this(width, height, null);
    }

    public Filler(int width, int height, Color background) {
        fWidth = width;
        fHeight = height;
        fBackground = background;
    }

    @Override
	public Color getBackground() {
        if (fBackground != null)
            return fBackground;
        return super.getBackground();
    }

    @Override
	public Dimension getMinimumSize() {
        return new Dimension(fWidth, fHeight);
    }

    @Override
	public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}

