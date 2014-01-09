package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;

import org.tomcurran.finity.util.Geom;

public class SelfArConnect implements ArcConnect {

	private static final int CONTROL_POINT_DISTANCE = 50;

	private FiniteTransitionConnection connection;

	public SelfArConnect(FiniteTransitionConnection connection) {
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
			return Geom.perpendicular(middle, end, CONTROL_POINT_DISTANCE, false);
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		CubicCurve2D c  = new CubicCurve2D.Float();
		Point control = controlPoint();
		Point middle = Geom.middle(connection.startPoint(), connection.endPoint());
		Point control1 = Geom.perpendicular(control, middle, CONTROL_POINT_DISTANCE);
		Point control2 = Geom.perpendicular(control, middle, CONTROL_POINT_DISTANCE, false);
		c.setCurve(connection.startPoint(), control1, control2, connection.endPoint());
		g2.draw(c);
	}

}
