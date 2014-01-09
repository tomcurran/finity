package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;

import CH.ifa.draw.framework.Figure;

public class SelfArConnect implements ArcConnect {

	private static final int CONTROL_POINT_DISTANCE = 40;

	private FiniteTransitionConnection connection;

	public SelfArConnect(FiniteTransitionConnection connection) {
		this.connection = connection;
	}

	@Override
	public Point controlPoint() {
		Point figureCentre = figureCentre();
		Point connectionPoint = startEndPoint();
		System.out.printf("connectionPoint=%s%n", connectionPoint);

		Point v = new Point(connectionPoint.x - figureCentre.x, connectionPoint.y - figureCentre.y);
		double vLen = Math.sqrt((v.x * v.x) + (v.y * v.y));

		return new Point(
				(int) (connectionPoint.x + ((v.x / vLen) * CONTROL_POINT_DISTANCE)),
				(int) (connectionPoint.y + ((v.y / vLen) * CONTROL_POINT_DISTANCE)));
	}

	private Point figureCentre() {
		Figure figure = connection.startFigure();
		if (figure == null) {
			figure = connection.endFigure();
		}
		return new Point(figure.center());
	}

	private Point startEndPoint() {
		if (connection.start() != null) {
			return connection.startPoint();
		} else if (connection.end() != null) {
			return connection.endPoint();
		}
		return null;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		CubicCurve2D c = new CubicCurve2D.Float();
		Point control = controlPoint();
		Point control1 = control1(control);
		Point control2 = control2(control);
		c.setCurve(connection.startPoint(), control1, control2, connection.endPoint());
		g2.draw(c);
	}

	private Point control1(Point control) {
		Point control1 = new Point(control);
		control1.translate(CONTROL_POINT_DISTANCE, 0);
		return control1;
	}

	private Point control2(Point control) {
		Point control2 = new Point(control);
		control2.translate(-CONTROL_POINT_DISTANCE, 0);
		return control2;
	}

}
