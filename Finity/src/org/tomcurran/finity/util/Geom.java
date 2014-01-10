package org.tomcurran.finity.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class Geom {

	public static Point middle(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	public static Point perpendicular(Point start, Point end, int distance) {
		return perpendicular(start, end, distance, true);
	}

	public static Point perpendicular(Point start, Point end, int distance, boolean clockwise) {
		return distance(start, negate(invert(diff(start, end)), clockwise), distance);
	}

	public static Point extend(Point start, Point end, int distance) {
		return distance(end, diff(start, end), distance);
	}

	public static Rectangle centreAt(Point centre, Rectangle r) {
		return new Rectangle(centre.x - (r.width / 2), centre.y - (r.height / 2), r.width, r.height);
	}

	private static Point diff(Point p1, Point p2) {
		return new Point(p2.x - p1.x, p2.y - p1.y);
	}

	private static Point invert(Point p) {
		return new Point(p.y, p.x);
	}

	private static Point negate(Point p, boolean clockwise) {
		if (clockwise) {
			return new Point(p.x, -p.y);
		} else {
			return new Point(-p.x, p.y);
		}
	}

	private static Point2D normalise(Point p) {
		double norm = Math.sqrt((p.getX() * p.getX()) + (p.getY() * p.getY()));
		return new Point2D.Double(p.getX() / norm, p.getY() / norm);
	}

	private static Point distance(Point p1, Point p2, int distance) {
		Point2D n = normalise(p2);
		return new Point(
				(int)(p1.x + (n.getX() * distance)),
				(int)(p1.y + (n.getY() * distance)));
	}

}
