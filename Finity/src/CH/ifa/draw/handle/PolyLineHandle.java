/*
 * @(#)PolyLineHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Point;

import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Locator;

/**
 * A handle for a node on the polyline.
 */
public class PolyLineHandle extends LocatorHandle {

    private int fIndex;
    @SuppressWarnings("unused")
	private Point fAnchor;

   /**
    * Constructs a poly line handle.
    * @param owner the owning polyline figure.
    * @l the locator
    * @index the index of the node associated with this handle.
    */
    public PolyLineHandle(PolyLineFigure owner, Locator l, int index) {
        super(owner, l);
        fIndex = index;
    }

    @Override
	public void invokeStart(int  x, int  y, DrawingView view) {
        fAnchor = new Point(x, y);
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        myOwner().setPointAt(new Point(x, y), fIndex);
    }

    private PolyLineFigure myOwner() {
        return (PolyLineFigure)owner();
    }
}


