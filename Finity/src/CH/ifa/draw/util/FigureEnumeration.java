/*
 * @(#)FigureEnumeration.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import CH.ifa.draw.framework.Figure;

/**
 * Interface for Enumerations that access Figures.
 * It provides a method nextFigure, that hides the down casting
 * from client code.
 */
// MIM: Added Figure to Enumeration - suspect interface no longer required.
public interface FigureEnumeration extends Enumeration<Figure> {
    /**
     * Returns the next element of the enumeration. Calls to this
     * method will enumerate successive elements.
     * @exception NoSuchElementException If no more elements exist.
     */
    public Figure nextFigure();
}
