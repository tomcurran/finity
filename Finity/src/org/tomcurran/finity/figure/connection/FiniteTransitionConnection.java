package org.tomcurran.finity.figure.connection;

import java.awt.Rectangle;

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

}
