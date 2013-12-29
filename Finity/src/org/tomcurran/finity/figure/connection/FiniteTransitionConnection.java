package org.tomcurran.finity.figure.connection;

import org.tomcurran.finity.figure.AcceptStateDecorator;
import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection implements FiniteTransition {

	private static final long serialVersionUID = -3487562166427672499L;

	private FiniteStateMachine fsm;
	private char label;

	public FiniteTransitionConnection(FiniteStateMachine fsm) {
		this(fsm, '1');
	}

	public FiniteTransitionConnection(FiniteStateMachine fsm, char label) {
		this.fsm = fsm;
		setLabel(label);
		setAttribute("ArrowMode", ARROW_TIP_END);
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		return (start instanceof FiniteStateFigure || start instanceof AcceptStateDecorator)
				&& (end instanceof FiniteStateFigure || end instanceof AcceptStateDecorator);
	}

	@Override
	protected void handleConnect(Figure start, Figure end) {
		fsm.addEdge((FiniteStateFigure)start, (FiniteStateFigure)end, this);
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		fsm.removeEdge(this);
	}

	@Override
	public char getLabel() {
		return label;
	}

	@Override
	public void setLabel(char label) {
		this.label = label;
	}

}
