package org.tomcurran.finity.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tomcurran.finity.test.fsm.FiniteStateMachineTest;
import org.tomcurran.finity.test.graph.DirectGraphTest;

@RunWith(Suite.class)
@SuiteClasses({ DirectGraphTest.class, FiniteStateMachineTest.class })
public class AllTests {

}
