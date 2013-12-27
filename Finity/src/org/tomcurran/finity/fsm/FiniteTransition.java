package org.tomcurran.finity.fsm;

import java.util.Observable;

public class FiniteTransition extends Observable {

	private char label;

	public FiniteTransition(char label) {
		setLabel(label);
	}

	public FiniteTransition() {
		this(' ');
	}

	public char getLabel() {
		return label;
	}

	public void setLabel(char label) {
		this.label = label;
		setChanged();
		notifyObservers();
	}

}
