package org.tomcurran.finity.tool;

import java.awt.Color;

import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.ActionTool;

public class StartStateTool extends ActionTool {

	private FiniteStateMachine fsm;

	public StartStateTool(DrawingView view, FiniteStateMachine fsm) {
		super(view);
		this.fsm = fsm;
	}

	@Override
	public void action(Figure figure) {
		if (figure instanceof FiniteStateFigure) {
			FiniteStateFigure state = (FiniteStateFigure) figure;
			FiniteStateFigure oldState = (FiniteStateFigure) fsm.getStartState();
			if (oldState != null) {
				oldState.setAttribute("FillColor", Color.WHITE);
			}
			state.setAttribute("FillColor", new Color(0x3366FF));
			fsm.setStartState(state);
		}
	}

}
