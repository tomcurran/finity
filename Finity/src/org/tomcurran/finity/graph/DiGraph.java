package org.tomcurran.finity.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DiGraph<N, E> extends AbstractGraph<N, E> implements DirectedGraph<N, E> {

	private Map<E, DiGraphEdge> edgeMap;

	private class DiGraphEdge {
		N source;
		N target;
	}

	public DiGraph() {
		super();
		edgeMap = new LinkedHashMap<E, DiGraphEdge>();
	}

	@Override
	public boolean addEdge(N n1, N n2, E e) {
		addNode(n1);
		addNode(n2);
		DiGraphEdge edge = new DiGraphEdge();
		edge.source = n1;
		edge.target = n2;
		edgeMap.put(e, edge);
		// TODO give sensible return value
		return false;
	}

	@Override
	public boolean containsEdge(E e) {
		return getEdgeSet().contains(e);
	}

	@Override
	public boolean containsEdge(N n1, N n2) {
		for (DiGraphEdge edge : edgeMap.values()) {
			if (edge.source == n1 && edge.target == n2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeEdge(E e) {
		return getEdgeSet().remove(e);
	}

	@Override
	public Set<E> getEdgeSet() {
		return edgeMap.keySet();
	}

	@Override
	public Set<E> getEdgesOf(N node) {
		Set<E> edges = new HashSet<E>();
		edges.addAll(incomingEdgesOf(node));
		edges.addAll(outgoingEdgesOf(node));
		return edges;
	}

	@Override
	public int inDegreeOf(N node) {
		return incomingEdgesOf(node).size();
	}

	@Override
	public Set<E> incomingEdgesOf(N node) {
		Set<E> edges = new HashSet<E>();
		for (Entry<E, DiGraphEdge> entry : edgeMap.entrySet()) {
			DiGraphEdge digEdge = entry.getValue();
			E edge = entry.getKey();
			if (digEdge.target == node) {
				edges.add(edge);
			}
		}
		return edges;
	}

	@Override
	public int outDegreeOf(N node) {
		return outgoingEdgesOf(node).size();
	}

	@Override
	public Set<E> outgoingEdgesOf(N node) {
		Set<E> edges = new HashSet<E>();
		for (Entry<E, DiGraphEdge> entry : edgeMap.entrySet()) {
			DiGraphEdge digEdge = entry.getValue();
			E edge = entry.getKey();
			if (digEdge.source == node) {
				edges.add(edge);
			}
		}
		return edges;
	}

	@Override
	public N getEdgeSource(E e) {
		N node = null;
		if (edgeMap.containsKey(e)) {
			node = edgeMap.get(e).source;
		}
		return node;
	}

	@Override
	public N getEdgeTarget(E e) {
		N node = null;
		if (edgeMap.containsKey(e)) {
			node = edgeMap.get(e).target;
		}
		return node;
	}

}
