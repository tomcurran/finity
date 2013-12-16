/*
 * @(#)PolyLineLocator.java 5.1
 *
 */

package CH.ifa.draw.locator;

import java.awt.Point;

import CH.ifa.draw.figure.LineDecoration;
import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.framework.Figure;

/**
 * A poly line figure consists of a list of points.
 * It has an optional line decoration at the start and end.
 *
 * @see LineDecoration
 */
public class PolyLineLocator extends AbstractLocator {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5999572414283034006L;
	
	int fIndex;

    public PolyLineLocator(int index) {
        fIndex = index;
    }

    @Override
	public Point locate(Figure owner) {
        PolyLineFigure plf = (PolyLineFigure)owner;
        // guard against changing PolyLineFigures -> temporary hack
        if (fIndex < plf.pointCount())
            return ((PolyLineFigure)owner).pointAt(fIndex);
        return new Point(0, 0);
    }
}
