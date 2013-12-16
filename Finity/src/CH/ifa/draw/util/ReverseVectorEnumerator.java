/*
 * @(#)ReverseVectorEnumerator.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * An Enumeration that enumerates a vector back (size-1) to front (0).
 */

// MIW: Added generic parameter E throughout - See Bracha: Generics in Java

public class ReverseVectorEnumerator<E> implements Enumeration<E> {

    Vector<E> vector;
    int count;

    public ReverseVectorEnumerator(Vector<E> v) {
	    vector = v;
	    count = vector.size() - 1;
    }

    @Override
	public boolean hasMoreElements() {
	    return count >= 0;
    }

    @Override
	public E nextElement() {
	    if (count >= 0) {
		    return vector.elementAt(count--);
	    }
	    throw new NoSuchElementException("ReverseVectorEnumerator");
    }

}
