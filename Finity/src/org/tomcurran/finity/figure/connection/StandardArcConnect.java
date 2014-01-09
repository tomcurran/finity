package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;

import org.tomcurran.finity.util.Geom;

public class StandardArcConnect implements ArcConnect {

	private static final int CONTROL_POINT_DISTANCE = 40;

	private FiniteTransitionConnection connection;

	public StandardArcConnect(FiniteTransitionConnection connection) {
		this.connection = connection;
	}

	@Override
	public Point controlPoint() {
		Point start = connection.startPoint();
		Point end = connection.endPoint();
		Point middle = Geom.middle(start, end);
		if (start.equals(end)) {
			return middle;
		} else {
			return Geom.perpendicular(middle, end, CONTROL_POINT_DISTANCE);
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		QuadCurve2D q = new QuadCurve2D.Float();
		q.setCurve(connection.startPoint(), controlPoint(), connection.endPoint());
		g2.draw(q);
	}

}
