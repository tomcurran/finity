/*
 * @(#)ReverseFigureEnumerator.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.util.NoSuchElementException;
import java.util.Vector;

import CH.ifa.draw.framework.Figure;

/**
 * An Enumeration that enumerates a vector of figures back (size-1) to front (0).
 */

// MIW: Added generic parameter throughout

public final class ReverseFigureEnumerator implements FigureEnumeration {
	
    ReverseVectorEnumerator<Figure> fEnumeration;

    public ReverseFigureEnumerator(Vector<Figure> v) {
    	
	    fEnumeration = new ReverseVectorEnumerator<Figure>(v);
    }

    /**
     * Returns true if the enumeration contains more elements; false
     * if its empty.
     */
    @Override
	public boolean hasMoreElements() {
	    return fEnumeration.hasMoreElements();
    }

    /**
     * Returns the next element of the enumeration. Calls to this
     * method will enumerate successive elements.
     * @exception NoSuchElementException If no more elements exist.
     */
    @Override
	public Figure nextElement() {
        return fEnumeration.nextElement();
    }

    /**
     * Returns the next element casted as a figure of the enumeration. Calls to this
     * method will enumerate successive elements.
     * @exception NoSuchElementException If no more elements exist.
     */
    @Override
	public Figure nextFigure() {
        return fEnumeration.nextElement();
    }
}
