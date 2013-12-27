package org.tomcurran.finity.test.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomcurran.finity.graph.DiGraph;
import org.tomcurran.finity.graph.DirectedGraph;

public class DirectGraphTest {

	private DirectedGraph<TestNode, TestEdge> g1;
	private TestNode n1;
	private TestNode n2;
	private TestNode n3;
	private TestEdge e1;
	private TestEdge e2;
	private TestEdge e3;

	@Before
	public void setUp() throws Exception {
		g1 = new DiGraph<TestNode, TestEdge>();
		n1 = new TestNode(1);
		n2 = new TestNode(2);
		n3 = new TestNode(3);
		e1 = new TestEdge("1");
		e2 = new TestEdge("2");
		e3 = new TestEdge("3");
	}

	@After
	public void tearDown() throws Exception {
		g1 = null;
		n1 = null;
		n2 = null;
		n3 = null;
		e1 = null;
		e2 = null;
		e3 = null;
	}

	@Test
	public void testAddNode() {
		assertTrue(g1.getNodeSet().isEmpty());
		g1.addNode(n1);
		assertEquals(1, g1.getNodeSet().size());
		g1.addNode(n2);
		assertEquals(2, g1.getNodeSet().size());
		g1.addNode(n3);
		assertEquals(3, g1.getNodeSet().size());
	}

	@Test
	public void testAddEdge() {
		assertTrue(g1.getEdgeSet().isEmpty());
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.getEdgeSet().size());
		g1.addEdge(n2, n3, e2);
		assertEquals(2, g1.getEdgeSet().size());
		g1.addEdge(n3, n1, e3);
		assertEquals(3, g1.getEdgeSet().size());
	}

	@Test
	public void testContainsEdgeE() {
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e1));
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e1));
		assertFalse(g1.containsEdge(e2));
		g1.addEdge(n2, n3, e2);
		assertEquals(2, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(e3));
		g1.addEdge(n3, n1, e3);
		assertEquals(3, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e3));
	}

	@Test
	public void testContainsEdgeNN() {
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e1));
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(n1, n2));
		assertFalse(g1.containsEdge(e2));
		g1.addEdge(n2, n3, e2);
		assertEquals(2, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(n2, n3));
		assertFalse(g1.containsEdge(e3));
		g1.addEdge(n3, n1, e3);
		assertEquals(3, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(n3, n1));
	}

	@Test
	public void testContainsNode() {
		assertTrue(g1.getNodeSet().isEmpty());
		assertFalse(g1.containsNode(n1));
		g1.addNode(n1);
		assertEquals(1, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n1));
		assertFalse(g1.containsNode(n2));
		g1.addNode(n2);
		assertEquals(2, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));
		g1.addNode(n3);
		assertEquals(3, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n3));
	}

	@Test
	public void testRemoveNode() {
		assertTrue(g1.getNodeSet().isEmpty());
		assertFalse(g1.containsNode(n1));
		g1.addNode(n1);
		assertEquals(1, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n1));
		g1.removeNode(n1);
		assertTrue(g1.getNodeSet().isEmpty());
		assertFalse(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));
		g1.addNode(n2);
		g1.addNode(n3);
		assertEquals(2, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n2));
		assertTrue(g1.containsNode(n3));
		g1.removeNode(n3);
		assertEquals(1, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));
		g1.removeNode(n2);
		assertTrue(g1.getNodeSet().isEmpty());
		assertFalse(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));
	}

	@Test
	public void testRemoveEdge() {
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e1));
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e1));
		assertTrue(g1.containsEdge(n1, n2));
		g1.removeEdge(e1);
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(e3));
		g1.addEdge(n2, n3, e2);
		g1.addEdge(n3, n1, e3);
		assertEquals(2, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e2));
		assertTrue(g1.containsEdge(n2, n3));
		assertTrue(g1.containsEdge(e3));
		assertTrue(g1.containsEdge(n3, n1));
		g1.removeEdge(e3);
		assertEquals(1, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e2));
		assertTrue(g1.containsEdge(n2, n3));
		assertFalse(g1.containsEdge(e3));
		assertFalse(g1.containsEdge(n3, n1));
		g1.removeEdge(e2);
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(n2, n3));
		assertFalse(g1.containsEdge(e3));
		assertFalse(g1.containsEdge(n3, n1));
	}

	@Test
	public void testGetNodeSet() {
		assertTrue(g1.getNodeSet().isEmpty());
		assertFalse(g1.containsNode(n1));
		assertFalse(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));

		g1.addNode(n1);
		assertEquals(1, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n1));
		assertFalse(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));

		g1.addNode(n2);
		assertEquals(2, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n1));
		assertTrue(g1.containsNode(n2));
		assertFalse(g1.containsNode(n3));

		g1.addNode(n3);
		assertEquals(3, g1.getNodeSet().size());
		assertTrue(g1.containsNode(n1));
		assertTrue(g1.containsNode(n2));
		assertTrue(g1.containsNode(n3));
	}

	@Test
	public void testGetEdgeSet() {
		assertTrue(g1.getEdgeSet().isEmpty());
		assertFalse(g1.containsEdge(e1));
		assertFalse(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(e3));

		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e1));
		assertFalse(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(e3));

		g1.addEdge(n2, n3, e2);
		assertEquals(2, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e1));
		assertTrue(g1.containsEdge(e2));
		assertFalse(g1.containsEdge(e3));

		g1.addEdge(n3, n1, e3);
		assertEquals(3, g1.getEdgeSet().size());
		assertTrue(g1.containsEdge(e1));
		assertTrue(g1.containsEdge(e2));
		assertTrue(g1.containsEdge(e3));
	}

	@Test
	public void testGetEdgesOf() {
		Set<TestEdge> es;

		es = g1.getEdgesOf(n1);
		assertEquals(0, es.size());

		g1.addEdge(n1, n2, e1);
		es = g1.getEdgesOf(n1);
		assertEquals(1, es.size());

		g1.addEdge(n1, n3, e2);
		es = g1.getEdgesOf(n1);
		assertEquals(2, es.size());
	}

	@Test
	public void testInDegreeOf() {
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.inDegreeOf(n2));

		g1.addEdge(n3, n2, e2);
		assertEquals(2, g1.inDegreeOf(n2));

		g1.addEdge(n1, n3, e3);
		assertEquals(1, g1.inDegreeOf(n3));
	}

	@Test
	public void testIncomingEdgesOf() {
		Set<TestEdge> es;

		g1.addEdge(n1, n2, e1);
		es = g1.incomingEdgesOf(n2);
		assertEquals(1, es.size());
		assertTrue(es.contains(e1));

		g1.addEdge(n3, n2, e2);
		es = g1.incomingEdgesOf(n2);
		assertEquals(2, es.size());
		assertTrue(es.contains(e1));
		assertTrue(es.contains(e2));

		g1.addEdge(n1, n3, e3);
		es = g1.incomingEdgesOf(n3);
		assertEquals(1, es.size());
		assertTrue(es.contains(e3));
	}

	@Test
	public void testOutDegreeOf() {
		g1.addEdge(n1, n2, e1);
		assertEquals(1, g1.outDegreeOf(n1));

		g1.addEdge(n1, n3, e2);
		assertEquals(2, g1.outDegreeOf(n1));

		g1.addEdge(n2, n3, e3);
		assertEquals(1, g1.outDegreeOf(n2));
	}

	@Test
	public void testOutgoingEdgesOf() {
		Set<TestEdge> es;

		g1.addEdge(n1, n2, e1);
		es = g1.outgoingEdgesOf(n1);
		assertEquals(1, es.size());
		assertTrue(es.contains(e1));

		g1.addEdge(n1, n3, e2);
		es = g1.outgoingEdgesOf(n1);
		assertEquals(2, es.size());
		assertTrue(es.contains(e1));
		assertTrue(es.contains(e2));

		g1.addEdge(n2, n3, e3);
		es = g1.outgoingEdgesOf(n2);
		assertEquals(1, es.size());
		assertTrue(es.contains(e3));
	}

	@Test
	public void testGetEdgeSource() {
		g1.addEdge(n1, n2, e1);
		assertSame(n1, g1.getEdgeSource(e1));

		g1.addEdge(n2, n3, e2);
		assertSame(n2, g1.getEdgeSource(e2));

		g1.addEdge(n3, n1, e3);
		assertSame(n3, g1.getEdgeSource(e3));
	}

	@Test
	public void testGetEdgeTarget() {
		g1.addEdge(n1, n2, e1);
		assertSame(n2, g1.getEdgeTarget(e1));

		g1.addEdge(n2, n3, e2);
		assertSame(n3, g1.getEdgeTarget(e2));

		g1.addEdge(n3, n1, e3);
		assertSame(n1, g1.getEdgeTarget(e3));
	}

}
