package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.CreationTool;

public class FiniteStateCreationTool extends CreationTool {

	private FiniteStateMachine fsm;

	public FiniteStateCreationTool(DrawingView view, FiniteStateMachine fsm) {
		super(view);
		this.fsm = fsm;
	}

	@Override
	protected Figure createFigure() {
		FiniteStateFigure fsFigure = new FiniteStateFigure();
		fsm.addNode(fsFigure);
		fsFigure.addFigureChangeListener(fsm);
		return fsFigure;
	}

}
