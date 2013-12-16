/*
 * @(#)GroupFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.GroupHandle;
import CH.ifa.draw.locator.RelativeLocator;

/**
 * A Figure that groups a collection of figures.
 */
public  class GroupFigure extends CompositeFigure {

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 8311226373023297933L;
    
    // MIW: Not used:
    //   private int groupFigureSerializedDataVersion = 1;

   @Override
public void basicDisplayBox(Point origin, Point corner) {
    // do nothing
    // we could transform all components proportionally
}

   /**
    * GroupFigures cannot be connected
    */
    @Override
	public boolean canConnect() {
        return false;
    }

    // MIW: FigureEnumeration removed
    @Override
	public Enumeration<Figure> decompose() {
        return fFigures.elements();
    }

    /**
	    * Gets the display box. The display box is defined as the union
	    * of the contained figures.
	    */
	    
	    // MIW: FigureEnumeration removed
	    @Override
		public Rectangle displayBox() {
	        Enumeration<Figure> k = figures();
	        Rectangle r = k.nextElement().displayBox();
	
	        while (k.hasMoreElements())
	            r.add(k.nextElement().displayBox());
	        return r;
	    }

   /**
    * Gets the handles for the GroupFigure.
    */
    // MIW: Added generic parameter.
    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = new Vector<Handle>();
        handles.addElement(new GroupHandle(this, RelativeLocator.northWest()));
        handles.addElement(new GroupHandle(this, RelativeLocator.northEast()));
        handles.addElement(new GroupHandle(this, RelativeLocator.southWest()));
        handles.addElement(new GroupHandle(this, RelativeLocator.southEast()));
        return handles;
    }

   /**
    * Sets the attribute of all the contained figures.
    */
    @Override
	public void setAttribute(String name, Object value) {
        super.setAttribute(name, value);
        Enumeration<Figure> k = figures();
        while (k.hasMoreElements())
            k.nextElement().setAttribute(name, value);
    }
}
