/*
 * @(#)FontSizeHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Locator;

/**
 * A Handle to change the font size by direct manipulation.
 */
public class FontSizeHandle extends LocatorHandle {

    private Font    fFont;
    private int     fSize;

    public FontSizeHandle(Figure owner, Locator l) {
        super(owner, l);
    }

    @Override
	public void draw(Graphics g) {
        Rectangle r = displayBox();

        g.setColor(Color.yellow);
        g.fillOval(r.x, r.y, r.width, r.height);

        g.setColor(Color.black);
        g.drawOval(r.x, r.y, r.width, r.height);
    }

    @Override
	public void invokeStart(int  x, int  y, DrawingView view) {
        TextFigure textOwner = (TextFigure) owner();
        fFont = textOwner.getFont();
        fSize = fFont.getSize();
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        TextFigure textOwner = (TextFigure) owner();
        int newSize = fSize + y-anchorY;
        textOwner.setFont(new Font(fFont.getName(), fFont.getStyle(), newSize) );
    }
}
