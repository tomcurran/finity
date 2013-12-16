/*
 * @(#)SelectAreaTracker.java 5.1
 *
 */

package CH.ifa.draw.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * SelectAreaTracker implements a rubberband selection of an area.
 */
public class SelectAreaTracker extends AbstractTool {

    private Rectangle fSelectGroup;

    public SelectAreaTracker(DrawingView view) {
        super(view);
    }

    private void drawXORRect(Rectangle r)
    {
        Graphics g = view().getGraphics();
        g.setXORMode(view().getBackground());
        g.setColor(Color.black);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    private void eraseRubberBand()
    {
        drawXORRect(fSelectGroup);
    }

    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
        // use event coordinates to supress any kind of
        // transformations like constraining points to a grid
        super.mouseDown(e, e.getX(), e.getY());
        rubberBand(fAnchorX, fAnchorY, fAnchorX, fAnchorY);
    }

    @Override
	public void mouseDrag(MouseEvent e, int x, int y) {
        super.mouseDrag(e, x, y);
        eraseRubberBand();
        rubberBand(fAnchorX, fAnchorY, x, y);
    }

    @Override
	public void mouseUp(MouseEvent e, int x, int y) {
        super.mouseUp(e, x, y);
        eraseRubberBand();
        selectGroup(e.isShiftDown());
    }

    private void rubberBand(int x1, int y1, int x2, int y2)
    {
        fSelectGroup = new Rectangle(new Point(x1, y1));
        fSelectGroup.add(new Point(x2, y2));
        drawXORRect(fSelectGroup);
    }

    // MIW: Removed FigureEnumeration
    private void selectGroup(boolean toggle)
    {
        Enumeration<Figure> k = drawing().figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            Rectangle r2 = figure.displayBox();
            if (fSelectGroup.contains(r2.x, r2.y) && fSelectGroup.contains(r2.x+r2.width, r2.y+r2.height)) {
                if (toggle)
                    view().toggleSelection(figure);
                else
                    view().addToSelection(figure);
            }
        }
    }
}
