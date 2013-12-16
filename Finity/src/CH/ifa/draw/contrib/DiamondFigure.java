/*
 * Hacked together by Doug lea
 * Tue Feb 25 17:39:44 1997  Doug Lea  (dl at gee)
 *
 */

package CH.ifa.draw.contrib;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import CH.ifa.draw.connector.ShortestDistanceConnector;
import CH.ifa.draw.figure.RectangleFigure;
import CH.ifa.draw.framework.Connector;

/**
 * A diamond with vertices at the midpoints of its enclosing rectangle
 */
public class DiamondFigure extends RectangleFigure {

	private static final long serialVersionUID = 138784184926403279L;

	public DiamondFigure() {
		super(new Point(0, 0), new Point(0, 0));
	}

	public DiamondFigure(Point origin, Point corner) {
		super(origin, corner);
	}

	@Override
	public Insets connectionInsets() {
		Rectangle r = displayBox();
		return new Insets(r.height / 2, r.width / 2, r.height / 2, r.width / 2);
	}

	// MIW added so that connects to figure rather than displayBox
    @Override
	public Connector connectorAt(int x, int y) {
        return new ShortestDistanceConnector(this); // just for demo purposes
    }

	@Override
	public boolean containsPoint(int x, int y) {
		return polygon().contains(x, y);
	}

	@Override
	public void draw(Graphics g) {
		Polygon p = polygon();
		g.setColor(getFillColor());
		g.fillPolygon(p);
		g.setColor(getFrameColor());
		g.drawPolygon(p);
	}

	/*
	 * public Point chop(Point p) { return PolygonFigure.chop(polygon(), p); }
	 */

	/** Return the polygon describing the diamond **/
	protected Polygon polygon() {
		Rectangle r = displayBox();
		Polygon p = new Polygon();
		p.addPoint(r.x, r.y + r.height / 2);
		p.addPoint(r.x + r.width / 2, r.y);
		p.addPoint(r.x + r.width, r.y + r.height / 2);
		p.addPoint(r.x + r.width / 2, r.y + r.height);
		return p;
	}
}
