package org.tomcurran.finity.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.graph.DiGraph;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;

public class FiniteStateMachine extends DiGraph<FiniteState, FiniteTransition> implements FigureChangeListener {

	private FiniteState startState;
	private String alphabet;

	public FiniteStateMachine(String alphabet, FiniteState startState) {
		super();
		this.alphabet = alphabet;
		this.startState = startState;
	}

	public FiniteStateMachine(String alphabet) {
		this(alphabet, null);
	}

	public FiniteStateMachine() {
		this("", null);
	}

	public FiniteState getStartState() {
		return startState;
	}

	public void setStartState(FiniteState startState) {
		this.startState = startState;
	}

	public boolean acceptInput(String input) {
		return false;
	}

	public boolean inAlphabet(char c) {
		return alphabet.indexOf(c) > -1;
	}

	public boolean isValid() {
		if (startState == null) {
			return false;
		}
		for (FiniteState state : getNodeSet()) {
			Set<FiniteTransition> transitions = outgoingEdgesOf(state);
			Collection<FiniteTransition> checkTransitions = new ArrayList<FiniteTransition>();
			for (FiniteTransition transition : transitions) {
				if (!inAlphabet(transition.getLabel()) || checkTransitions.contains(transition)) {
					return false;
				}
				for (FiniteTransition ct : checkTransitions) {
					if (transition.getLabel() == ct.getLabel()) {
						return false;
					}
				}
				checkTransitions.add(transition);
			}
		}
		return true;
	}

	@Override
	public void figureRemoved(FigureChangeEvent e) {
		Figure figure = e.getFigure();
		if (figure instanceof FiniteStateFigure) {
			removeNode(((FiniteStateFigure)figure).getModel());
		}
	}

	@Override
	public void figureChanged(FigureChangeEvent e) {}

	@Override
	public void figureInvalidated(FigureChangeEvent e) {}

	@Override
	public void figureRequestRemove(FigureChangeEvent e) {}

	@Override
	public void figureRequestUpdate(FigureChangeEvent e) {}

}
