package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;

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
		Point figureCentre = figure.center();
		int h = figure.displayBox().height;
		return new Point(figureCentre.x, figureCentre.y + (h / 2) + CONTROL_POINT_DISTANCE);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		QuadCurve2D q = new QuadCurve2D.Float();
		q.setCurve(connection.startPoint(), controlPoint(), connection.endPoint());
		g2.draw(q);
	}

}
