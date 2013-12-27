package org.tomcurran.finity.graph;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGraph<N, E> implements Graph<N, E> {

	protected Set<N> nodeSet;

	public AbstractGraph() {
		nodeSet = new HashSet<N>();
	}

	@Override
	public boolean addNode(N n) {
		return nodeSet.add(n);
	}

	@Override
	public boolean containsNode(N n) {
		return nodeSet.contains(n);
	}

	@Override
	public Set<N> getNodeSet() {
		return nodeSet;
	}

	@Override
	public boolean removeNode(N n) {
		return nodeSet.remove(n);
	}

}
