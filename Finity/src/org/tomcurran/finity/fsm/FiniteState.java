package org.tomcurran.finity.fsm;

public interface FiniteState {

	public String getLabel();
	public void setLabel(String label);
	public boolean isAccepting();
	public void setAccepting(boolean accepting);

}
