package org.tomcurran.finity.util;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Geom {

	public static Point middle(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	public static Point perpendicular(Point start, Point end, int distance) {
		return perpendicular(start, end, distance, true);
	}

	public static Point perpendicular(Point start, Point end, int distance, boolean clockwise) {
		Point2D p = normalise(perpendicular(start, end, clockwise));
		return new Point(
				(int)(start.x + (p.getX() * distance)),
				(int)(start.y + (p.getY() * distance)));
	}

	public static Point2D normalise(Point2D p) {
		double norm = Math.sqrt((p.getX() * p.getX()) + (p.getY() * p.getY()));
		return new Point2D.Double(p.getX() / norm, p.getY() / norm);
	}

	public static Point extend(Point start, Point end, int distance) {
		Point2D p = normalise(new Point(end.x - start.x, end.y - start.y));
		return new Point(
				(int)(end.x + (p.getX() * distance)),
				(int)(end.y + (p.getY() * distance)));
	}

	private static Point perpendicular(Point p1, Point p2, boolean clockwise) {
		int x = p2.y - p1.y;
		int y = p2.x - p1.x;
		if (clockwise) {
			y = -y;
		} else {
			x = -x;
		}
		return new Point(x, y);
	}

}
