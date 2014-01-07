package org.tomcurran.finity.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.tomcurran.finity.graph.DiGraph;

public class FiniteStateMachine extends DiGraph<FiniteState, FiniteTransition> {

	private static final FiniteStateMachine INSTANCE = new FiniteStateMachine();

	public static FiniteStateMachine getInstance() {
		return INSTANCE;
	}

	private FiniteState startState;
	private String alphabet;

	public FiniteStateMachine() {
		this.alphabet = "";
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public FiniteState getStartState() {
		return startState;
	}

	public void setStartState(FiniteState startState) {
		this.startState = startState;
	}

	public boolean acceptInput(String input) {
		for (char c : input.toCharArray()) {
			if (!inAlphabet(c)) {
				return false;
			}
		}
		if (!isValid()) {
			return false;
		}
		FiniteState end = traverse(input, startState);
		return end != null && end.isAccepting();
	}

	private FiniteState traverse(String input, FiniteState state) {
		if (input.length() == 0 || state == null) {
			return state;
		}
		return traverse(input.substring(1), transition(input.charAt(0), state));
	}

	private FiniteState transition(char input, FiniteState state) {
		for (FiniteTransition transition : outgoingEdgesOf(state)) {
			if (transition.getLabel() == input) {
				return getEdgeTarget(transition);
			}
		}
		return null;
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
			if (transitions.size() != alphabet.length()) {
				return false;
			}
			Collection<Character> checked = new ArrayList<Character>();
			for (FiniteTransition transition : transitions) {
				char label = transition.getLabel();
				if (!inAlphabet(label) || checked.contains(label)) {
					return false;
				}
				checked.add(label);
			}
		}
		return true;
	}

}
