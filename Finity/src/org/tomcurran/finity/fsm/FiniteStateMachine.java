package org.tomcurran.finity.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;

import org.tomcurran.finity.graph.DiGraph;
import org.tomcurran.finity.graph.DirectedGraph;

public class FiniteStateMachine extends Observable {

	private static final FiniteStateMachine INSTANCE = new FiniteStateMachine();

	public static FiniteStateMachine getInstance() {
		return INSTANCE;
	}

	private DirectedGraph<FiniteState, FiniteTransition> graph;
	private FiniteState startState;
	private FiniteState currentState;
	private String alphabet;

	private FiniteStateMachine() {
		this.alphabet = "";
		this.graph = new DiGraph<FiniteState, FiniteTransition>();
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

	public FiniteState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(FiniteState state) {
		currentState = state;
		setChanged();
		notifyObservers();
	}

	public void transition(char label) throws FiniteTransitionException {
		FiniteState nextState = transition(label, currentState);
		if (nextState == null) {
			throw new FiniteTransitionException();
		}
		setCurrentState(nextState);
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

	private FiniteState transition(char label, FiniteState state) {
		for (FiniteTransition transition : graph.outgoingEdgesOf(state)) {
			if (transition.getLabel() == label) {
				return graph.getEdgeTarget(transition);
			}
		}
		return null;
	}

	public boolean inAlphabet(String inputString) {
		for (char inputChar : inputString.toCharArray()) {
			if (!inAlphabet(inputChar)) {
				return false;
			}
		}
		return true;
	}

	public boolean inAlphabet(char inputChar) {
		return alphabet.indexOf(inputChar) > -1;
	}

	public boolean isValid() {
		if (startState == null) {
			return false;
		}
		for (FiniteState state : graph.getNodeSet()) {
			Set<FiniteTransition> transitions = graph.outgoingEdgesOf(state);
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

	// adapt graph

	public void addTransition(FiniteState startState, FiniteState endState, FiniteTransition transition) {
		graph.addEdge(startState, endState, transition);
	}

	public void removeTransition(FiniteTransition transition) {
		graph.removeEdge(transition);
	}

	public void addState(FiniteState state) {
		graph.addNode(state);
	}

	public void removeState(FiniteState state) {
		graph.removeNode(state);
	}

	public int getStateCount() {
		return graph.getNodeSet().size();
	}

	public int getTransitionCount() {
		return graph.getEdgeSet().size();
	}

}
