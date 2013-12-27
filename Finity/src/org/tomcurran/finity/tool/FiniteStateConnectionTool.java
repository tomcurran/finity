package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.connection.FiniteTransitionConnection;
import org.tomcurran.finity.fsm.FiniteStateMachine;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.tool.ConnectionTool;

public class FiniteStateConnectionTool extends ConnectionTool {

	private FiniteStateMachine fsm;

	public FiniteStateConnectionTool(DrawingView view, FiniteStateMachine fsm) {
		super(view, null);
		this.fsm = fsm;
	}

	@Override
	protected ConnectionFigure createConnection() {
		return new FiniteTransitionConnection(fsm);
	}

}
