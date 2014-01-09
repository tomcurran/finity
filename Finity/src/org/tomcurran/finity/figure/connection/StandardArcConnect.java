package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;

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
		Point middle = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
		Point perpend = new Point(end.y - start.y, -(end.x - start.x));
		double perpendLen = Math.sqrt((perpend.x * perpend.x) + (perpend.y * perpend.y));
		if (perpendLen == 0 ) {
			return middle;
		}
		return new Point(
					(int) (middle.x + ((perpend.x / perpendLen) * CONTROL_POINT_DISTANCE)),
					(int) (middle.y + ((perpend.y / perpendLen) * CONTROL_POINT_DISTANCE)));
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		QuadCurve2D q = new QuadCurve2D.Float();
		q.setCurve(connection.startPoint(), controlPoint(), connection.endPoint());
		g2.draw(q);
	}

}
