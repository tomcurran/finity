package org.tomcurran.finity.graph;

import java.util.Set;

public interface DirectedGraph<N, E> extends Graph<N, E> {

    public int inDegreeOf(N node);
    public Set<E> incomingEdgesOf(N node);
    public int outDegreeOf(N node);
    public Set<E> outgoingEdgesOf(N node);
    public N getEdgeSource(E e);
    public N getEdgeTarget(E e);

}
