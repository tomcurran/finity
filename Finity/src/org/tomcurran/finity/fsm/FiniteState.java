package org.tomcurran.finity.fsm;

import java.util.Observable;

public class FiniteState extends Observable {

	private static int counter = 1;

	private boolean accepting;
	private String label;

	public FiniteState(String label, boolean accepting) {
		setLabel(label);
		setAccepting(accepting);
	}

	public FiniteState(String label) {
		this(label, false);
	}

	public FiniteState() {
		this(String.format("S%s", counter++), false);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		setChanged();
		notifyObservers();
	}

	public boolean isAccepting() {
		return accepting;
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
		setChanged();
		notifyObservers();
	}

}
