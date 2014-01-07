package org.tomcurran.finity.test.fsm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.figure.FiniteTransitionFigure;
import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

public class FiniteStateMachineTest {

	private final String ALPHABET = "01";

	private FiniteStateMachine fsm;
	private FiniteState fs1;
	private FiniteState fs2;
	private FiniteState fs3;
	private FiniteTransition ft1;
	private FiniteTransition ft2;
	private FiniteTransition ft3;
	private FiniteTransition ft4;
	private FiniteTransition ft5;
	private FiniteTransition ft6;

	@Before
	public void setUp() throws Exception {
		fsm = FiniteStateMachine.getInstance();
		fsm.setAlphabet(ALPHABET);
		fs1 = new FiniteStateFigure("S1", false);
		fs2 = new FiniteStateFigure("S2", false);
		fs3 = new FiniteStateFigure("S3", true);
		ft1 = new FiniteTransitionFigure();
		ft2 = new FiniteTransitionFigure();
		ft3 = new FiniteTransitionFigure();
		ft4 = new FiniteTransitionFigure();
		ft5 = new FiniteTransitionFigure();
		ft6 = new FiniteTransitionFigure();
		ft1.setLabel('0');
		ft2.setLabel('1');
		ft3.setLabel('0');
		ft4.setLabel('1');
		ft5.setLabel('0');
		ft6.setLabel('1');
		fsm.addTransition(fs1, fs1, ft1);
		fsm.addTransition(fs1, fs2, ft2);
		fsm.addTransition(fs2, fs3, ft3);
		fsm.addTransition(fs2, fs2, ft4);
		fsm.addTransition(fs3, fs1, ft5);
		fsm.addTransition(fs3, fs2, ft6);
		fsm.setStartState(fs1);
	}

	@Test
	public void testSetGetStartState() {
		fsm.addState(fs1);
		fsm.addState(fs2);
		fsm.addState(fs3);
		fsm.setStartState(fs1);
		assertSame(fs1, fsm.getStartState());
		fsm.setStartState(fs2);
		assertSame(fs2, fsm.getStartState());
		fsm.setStartState(fs3);
		assertSame(fs3, fsm.getStartState());
	}

	@Test
	public void testAcceptInput() {
		// this fsm accepts input ending 10
		assertTrue(fsm.acceptInput("10"));
		assertTrue(fsm.acceptInput("1010010110"));
		assertTrue(fsm.acceptInput("00110100110"));
		assertFalse(fsm.acceptInput("0"));
		assertFalse(fsm.acceptInput("1"));
		assertFalse(fsm.acceptInput("01"));
		assertFalse(fsm.acceptInput("101011101001"));
		assertFalse(fsm.acceptInput("01011010010101"));
		assertFalse(fsm.acceptInput("10A10"));
		assertFalse(fsm.acceptInput("01A01"));
	}

	@Test
	public void testInAlphabet() {
		assertTrue(fsm.inAlphabet('0'));
		assertTrue(fsm.inAlphabet('1'));
		assertFalse(fsm.inAlphabet('2'));
		assertFalse(fsm.inAlphabet('3'));
		assertFalse(fsm.inAlphabet('A'));
		assertFalse(fsm.inAlphabet('b'));
	}

	@Test
	public void testIsValid() {
		assertTrue(fsm.isValid());
		fsm.addTransition(fs1, fs2, new FiniteTransitionFigure());
		assertFalse(fsm.isValid());
	}

}
