package org.tomcurran.finity.test.fsm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
		fsm = new FiniteStateMachine(ALPHABET);
		fs1 = new FiniteState("S1", false);
		fs2 = new FiniteState("S2", false);
		fs3 = new FiniteState("S3", true);
		ft1 = new FiniteTransition('0');
		ft2 = new FiniteTransition('1');
		ft3 = new FiniteTransition('0');
		ft4 = new FiniteTransition('1');
		ft5 = new FiniteTransition('0');
		ft6 = new FiniteTransition('1');
	}

	@After
	public void tearDown() throws Exception {
	}

	private void setupFSM() {
		fsm.addEdge(fs1, fs1, ft1);
		fsm.addEdge(fs1, fs2, ft2);
		fsm.addEdge(fs2, fs3, ft3);
		fsm.addEdge(fs2, fs2, ft4);
		fsm.addEdge(fs3, fs1, ft5);
		fsm.addEdge(fs3, fs2, ft6);
		fsm.setStartState(fs1);
	}

	@Test
	public void testSetGetStartState() {
		fsm.addNode(fs1);
		fsm.addNode(fs2);
		fsm.addNode(fs3);
		fsm.setStartState(fs1);
		assertSame(fs1, fsm.getStartState());
		fsm.setStartState(fs2);
		assertSame(fs2, fsm.getStartState());
		fsm.setStartState(fs3);
		assertSame(fs3, fsm.getStartState());
	}

	@Test
	public void testAcceptInput() {
		fail("Not yet implemented");
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
		setupFSM();
		assertTrue(fsm.isValid());
		fsm.addEdge(fs1, fs2, new FiniteTransition('1'));
		assertFalse(fsm.isValid());
	}

}
