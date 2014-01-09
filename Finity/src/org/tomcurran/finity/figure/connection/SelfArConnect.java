package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;

import org.tomcurran.finity.util.Geom;

import CH.ifa.draw.framework.Figure;

public class SelfArConnect implements ArcConnect {

	private static final int CONTROL_POINT_DISTANCE = 40;

	private FiniteTransitionConnection connection;

	public SelfArConnect(FiniteTransitionConnection connection) {
		this.connection = connection;
	}

	@Override
	public Point controlPoint() {
		Figure figure = connection.startFigure();
		if (figure == null) {
			figure = connection.endFigure();
		}
		Point figureCentre = figure.center();
		Point connectionPoint = connectionPoint();
		return Geom.extend(figureCentre, connectionPoint, CONTROL_POINT_DISTANCE);
	}

	private Point connectionPoint() {
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
		CubicCurve2D c  = new CubicCurve2D.Float();
		Point control = controlPoint();
		Point connectionPoint = connectionPoint();
		Point control1 = Geom.perpendicular(control, connectionPoint, CONTROL_POINT_DISTANCE);
		Point control2 = Geom.perpendicular(control, connectionPoint, CONTROL_POINT_DISTANCE, false);
		c.setCurve(connection.startPoint(), control1, control2, connection.endPoint());
		g2.draw(c);
	}

}
