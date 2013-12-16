/*
 * @(#)FigureChangeEvent.java 5.1
 *
 */

package CH.ifa.draw.framework;

import java.awt.Rectangle;
import java.util.EventObject;

/**
 * FigureChange event passed to FigureChangeListeners.
 *
 */
public class FigureChangeEvent extends EventObject {

    /**
	 * MIW: added generated serial version ID
	 */
	private static final long serialVersionUID = 3789585381323569721L;
	private Rectangle fRectangle;
    private static final Rectangle  fgEmptyRectangle = new Rectangle(0, 0, 0, 0);

   public FigureChangeEvent(Figure source) {
    super(source);
    fRectangle = fgEmptyRectangle;
}

    /**
	    * Constructs an event for the given source Figure. The rectangle is the
	    * area to be invalidated.
	    */
	    public FigureChangeEvent(Figure source, Rectangle r) {
	        super(source);
	        fRectangle = r;
	    }

    /**
     *  Gets the changed figure
     */
    public Figure getFigure() {
        return (Figure)getSource();
    }

    /**
     *  Gets the changed rectangle
     */
    public Rectangle getInvalidatedRectangle() {
        return fRectangle;
    }
}
