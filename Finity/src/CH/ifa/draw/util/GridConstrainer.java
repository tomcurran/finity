/*
 * @(#)GridConstrainer.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.awt.Point;
import java.io.Serializable;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.PointConstrainer;

/**
 * Constrains a point such that it falls on a grid.
 *
 * @see DrawingView
 */


public class GridConstrainer implements PointConstrainer, Serializable {

	private static final long serialVersionUID = -1412368628241031760L;
	private int fGridX;
    private int fGridY;

    public GridConstrainer(int x, int y) {
        fGridX = Math.max(1, x);
        fGridY = Math.max(1, y);
    }

    /**
     * Constrains the given point.
     * @return constrained point.
     */
    @Override
	public Point constrainPoint(Point p) {
        p.x = ((p.x+fGridX/2) / fGridX) * fGridX;
        p.y = ((p.y+fGridY/2) / fGridY) * fGridY;
        return p;
    }

    /**
     * Gets the x offset to move an object.
     */
    @Override
	public int getStepX() {
        return fGridX;
    }

    /**
     * Gets the y offset to move an object.
     */
    @Override
	public int getStepY() {
        return fGridY;
    }
}
