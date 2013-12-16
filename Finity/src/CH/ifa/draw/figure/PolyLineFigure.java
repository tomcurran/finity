/*
 * @(#)PolyLineFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.connector.PolyLineConnector;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.handle.PolyLineHandle;
import CH.ifa.draw.locator.PolyLineLocator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.util.Geom;

/**
 * A poly line figure consists of a list of points. It has an optional line
 * decoration at the start and end.
 * 
 * @see LineDecoration
 */

public class PolyLineFigure extends AbstractFigure {

	public final static int ARROW_TIP_NONE = 0;
	public final static int ARROW_TIP_START = 1;
	public final static int ARROW_TIP_END = 2;
	public final static int ARROW_TIP_BOTH = 3;

	/**
	 * Creates a locator for the point with the given index.
	 */
	public static Locator locator(int pointIndex) {
		return new PolyLineLocator(pointIndex);
	}
	protected Vector<Point> fPoints;
	protected LineDecoration fStartDecoration = null;
	protected LineDecoration fEndDecoration = null;

	protected Color fFrameColor = Color.black;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -7951352179906577773L;
	// private int polyLineFigureSerializedDataVersion = 1;

	public PolyLineFigure() {
		fPoints = new Vector<Point>(4);
	}

	public PolyLineFigure(int size) {
		fPoints = new Vector<Point>(size);
	}

	public PolyLineFigure(int x, int y) {
		fPoints = new Vector<Point>();
		fPoints.addElement(new Point(x, y));
	}

	/**
	 * Adds a node to the list of points.
	 */
	public void addPoint(int x, int y) {
		fPoints.addElement(new Point(x, y));
		changed();
	}

	@Override
	public void basicDisplayBox(Point origin, Point corner) {
	}

	@Override
	protected void basicMoveBy(int dx, int dy) {
		Enumeration<Point> k = fPoints.elements();
		while (k.hasMoreElements())
			(k.nextElement()).translate(dx, dy);
	}

	@Override
	public Connector connectorAt(int x, int y) {
		return new PolyLineConnector(this);
	}

	@Override
	public boolean containsPoint(int x, int y) {
		Rectangle bounds = displayBox();
		bounds.grow(4, 4);
		if (!bounds.contains(x, y))
			return false;

		Point p1, p2;
		for (int i = 0; i < fPoints.size() - 1; i++) {
			p1 = fPoints.elementAt(i);
			p2 = fPoints.elementAt(i + 1);
			if (Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, x, y))
				return true;
		}
		return false;
	}

	private void decorate(Graphics g) {
		if (fStartDecoration != null) {
			Point p1 = fPoints.elementAt(0);
			Point p2 = fPoints.elementAt(1);
			fStartDecoration.draw(g, p1.x, p1.y, p2.x, p2.y);
		}
		if (fEndDecoration != null) {
			Point p3 = fPoints.elementAt(fPoints.size() - 2);
			Point p4 = fPoints.elementAt(fPoints.size() - 1);
			fEndDecoration.draw(g, p4.x, p4.y, p3.x, p3.y);
		}
	}

	@Override
	public Rectangle displayBox() {
		Enumeration<Point> k = points();
		Rectangle r = new Rectangle(k.nextElement());	// MIW: danger empty Enumeration?

		while (k.hasMoreElements())
			r.add(k.nextElement());

		return r;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getFrameColor());
		Point p1, p2;
		for (int i = 0; i < fPoints.size() - 1; i++) {
			p1 = fPoints.elementAt(i);
			p2 = fPoints.elementAt(i + 1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		decorate(g);
	}

	/**
	 * Gets the segment of the polyline that is hit by the given point.
	 * 
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int findSegment(int x, int y) {
		Point p1, p2;
		for (int i = 0; i < fPoints.size() - 1; i++) {
			p1 = fPoints.elementAt(i);
			p2 = fPoints.elementAt(i + 1);
			if (Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, x, y))
				return i;
		}
		return -1;
	}

	/**
	 * Gets the attribute with the given name. PolyLineFigure maps "ArrowMode"to
	 * a line decoration.
	 */
	@Override
	public Object getAttribute(String name) {
		if (name.equals("FrameColor")) {
			return getFrameColor();
		} else if (name.equals("ArrowMode")) {
			int value = 0;
			if (fStartDecoration != null)
				value |= ARROW_TIP_START;
			if (fEndDecoration != null)
				value |= ARROW_TIP_END;
			return new Integer(value);
		}
		return super.getAttribute(name);
	}

	protected Color getFrameColor() {
		return fFrameColor;
	}

	// MIW: Looks as if just locates Handles on Points
	@Override
	public Vector<Handle> handles() {
		Vector<Handle> handles = new Vector<Handle>(fPoints.size());
		for (int i = 0; i < fPoints.size(); i++)
			handles.addElement(new PolyLineHandle(this, locator(i), i));
		return handles;
	}

	/**
	 * Insert a node at the given point.
	 */
	public void insertPointAt(Point p, int i) {
		fPoints.insertElementAt(p, i);
		changed();
	}

	@Override
	public boolean isEmpty() {
		return (size().width < 3) && (size().height < 3);
	}

	/**
	 * Joins two segments into one if the given point hits a node of the
	 * polyline.
	 * 
	 * @return true if the two segments were joined.
	 */
	public boolean joinSegments(int x, int y) {
		for (int i = 1; i < fPoints.size() - 1; i++) {
			Point p = pointAt(i);
			if (Geom.length(x, y, p.x, p.y) < 3) {
				removePointAt(i);
				return true;
			}
		}
		return false;
	}

	public Point pointAt(int i) {
		return fPoints.elementAt(i);
	}

	// MIW: drawing within model?
	
	public int pointCount() {
		return fPoints.size();
	}

	public Enumeration<Point> points() {
		return fPoints.elements();
	}

	@Override
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		int size = dr.readInt();
		fPoints = new Vector<Point>(size);
		for (int i = 0; i < size; i++) {
			int x = dr.readInt();
			int y = dr.readInt();
			fPoints.addElement(new Point(x, y));
		}
		fStartDecoration = (LineDecoration) dr.readStorable();
		fEndDecoration = (LineDecoration) dr.readStorable();
		fFrameColor = dr.readColor();
	}

	public void removePointAt(int i) {
		willChange();
		fPoints.removeElementAt(i);
		changed();
	}

	/**
	 * Sets the attribute with the given name. PolyLineFigure interprets
	 * "ArrowMode"to set the line decoration.
	 */
	@Override
	public void setAttribute(String name, Object value) {
		if (name.equals("FrameColor")) {
			setFrameColor((Color) value);
			changed();
		} else if (name.equals("ArrowMode")) {
			Integer intObj = (Integer) value;
			if (intObj != null) {
				int decoration = intObj.intValue();
				if ((decoration & ARROW_TIP_START) != 0)
					fStartDecoration = new ArrowTip();
				else
					fStartDecoration = null;
				if ((decoration & ARROW_TIP_END) != 0)
					fEndDecoration = new ArrowTip();
				else
					fEndDecoration = null;
			}
			changed();
		} else
			super.setAttribute(name, value);
	}

	/**
	 * Sets the end decoration.
	 */
	public void setEndDecoration(LineDecoration l) {
		fEndDecoration = l;
	}

	protected void setFrameColor(Color c) {
		fFrameColor = c;
	}

	/**
	 * Changes the position of a node.
	 */
	public void setPointAt(Point p, int i) {
		willChange();
		fPoints.setElementAt(p, i);
		changed();
	}

	/**
	 * Sets the start decoration.
	 */
	public void setStartDecoration(LineDecoration l) {
		fStartDecoration = l;
	}

	/**
	 * Splits the segment at the given point if a segment was hit.
	 * 
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int splitSegment(int x, int y) {
		int i = findSegment(x, y);
		if (i != -1)
			insertPointAt(new Point(x, y), i + 1);
		return i + 1;
	}

	@Override
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fPoints.size());
		Enumeration<Point> k = fPoints.elements();
		while (k.hasMoreElements()) {
			Point p = k.nextElement();
			dw.writeInt(p.x);
			dw.writeInt(p.y);
		}
		dw.writeStorable(fStartDecoration);
		dw.writeStorable(fEndDecoration);
		dw.writeColor(fFrameColor);
	}
}
