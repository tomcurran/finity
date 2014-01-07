package org.tomcurran.finity.graph;

import java.util.Set;

public interface Graph<N, E> {

    public boolean addEdge(N n1, N n2, E e);
    public boolean addNode(N n);

    public boolean containsEdge(E e);
    public boolean containsNode(N n);
    public boolean containsEdge(N n1, N n2);

    public Set<E> getEdgeSet();
    public Set<E> getEdgesOf(N node);
    public Set<N> getNodeSet();

    public boolean removeEdge(E e);
    public boolean removeNode(N n);

}
