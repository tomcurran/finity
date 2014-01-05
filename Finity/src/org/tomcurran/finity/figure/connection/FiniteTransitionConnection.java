package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.QuadCurve2D;

import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection {

	private static final long serialVersionUID = -3487562166427672499L;

	private FiniteStateMachine fsm;
	private FiniteTransition ft;

	public FiniteTransitionConnection(FiniteStateMachine fsm, FiniteTransition ft) {
		this.fsm = fsm;
		this.ft = ft;
		setAttribute("ArrowMode", ARROW_TIP_END);
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		if (start instanceof DecoratorFigure) {
			start = ((DecoratorFigure) start).peelDecoration();
		}
		if (end instanceof DecoratorFigure) {
			end = ((DecoratorFigure) end).peelDecoration();
		}
		return (start instanceof FiniteState) && (end instanceof FiniteState);
	}

	@Override
	protected void handleConnect(Figure start, Figure end) {
		fsm.addEdge((FiniteState)start, (FiniteState)end, ft);
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		fsm.removeEdge(ft);
	}

	@Override
	public int findSegment(int x, int y) {
		// prevents line becoming polyline
		return -1;
	}

	@Override
	public Rectangle displayBox() {
		// prevent Enumeration error when adding connection to group
		if (!points().hasMoreElements()) {
			return new Rectangle();
		}
		return super.displayBox();
	}

	public Point controlPoint() {
		Point start = startPoint();
		Point end = endPoint();
		Point middle = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
		Point control = new Point(middle);
		control.translate(10, 10);
		return control;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getFrameColor());
		Graphics2D g2 = (Graphics2D) g;
		QuadCurve2D q = new QuadCurve2D.Float();
		Point start = startPoint();
		Point end = endPoint();
		Point control = controlPoint();
		q.setCurve(start.x, start.y, control.x, control.y, end.x, end.y);
		g2.draw(q);
		decorate(g);
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

}
