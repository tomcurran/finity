/*
 * @(#)BoxHandleKit.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.locator.RelativeLocator;

/**
 * A set of utility methods to create Handles for the common
 * locations on a figure's display box.
 * @see Handle
 */

 // TBD: use anonymous inner classes (had some problems with JDK 1.1)
 // MIW : Changed to anonymous inner classes

public class BoxHandleKit {

    /**
     * Fills the given Vector with handles at each corner of a
     * figure.
     */
    static public void addCornerHandles(Figure f, Vector<Handle> handles) {
        handles.addElement(new SouthEastHandle(f));
        handles.addElement(new SouthWestHandle(f));
        handles.addElement(new NorthEastHandle(f));
        handles.addElement(new NorthWestHandle(f));
    }

    /**
     * Fills the given Vector with handles at each corner
     * and the north, south, east, and west of the figure.
     */
    static public void addHandles(Figure f, Vector<Handle> handles) {
        addCornerHandles(f, handles);
        handles.addElement(new SouthHandle(f));
        handles.addElement(new NorthHandle(f));
        handles.addElement(new EastHandle(f));
        handles.addElement(new WestHandle(f));
    }

 	static public Handle south(Figure owner) {
        return new SouthHandle(owner);
    }

    static public Handle southEast(Figure owner) {
        return new SouthEastHandle(owner);
    }

    static public Handle southWest(Figure owner) {
        return new SouthWestHandle(owner);
    }

    static public Handle north(Figure owner) {
        return new NorthHandle(owner);
    }

    static public Handle northEast(Figure owner) {
        return new NorthEastHandle(owner);
    }

    static public Handle northWest(Figure owner) {
        return new NorthWestHandle(owner);
    }

    static public Handle east(Figure owner) {
        return new EastHandle(owner);
    }
    static public Handle west(Figure owner) {
        return new WestHandle(owner);
    }
}

class EastHandle extends LocatorHandle {
    EastHandle(Figure owner) {
        super(owner, RelativeLocator.east());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(r.x, r.y), new Point(Math.max(r.x, x), r.y + r.height)
        );
    }
}

class NorthEastHandle extends LocatorHandle {
    NorthEastHandle(Figure owner) {
        super(owner, RelativeLocator.northEast());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(r.x, Math.min(r.y + r.height, y)),
            new Point(Math.max(r.x, x), r.y + r.height)
        );
    }
}

class NorthHandle extends LocatorHandle {
    NorthHandle(Figure owner) {
        super(owner, RelativeLocator.north());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(r.x, Math.min(r.y + r.height, y)),
            new Point(r.x + r.width, r.y + r.height)
        );
    }
}

class NorthWestHandle extends LocatorHandle {
    NorthWestHandle(Figure owner) {
        super(owner, RelativeLocator.northWest());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(Math.min(r.x + r.width, x), Math.min(r.y + r.height, y)),
            new Point(r.x + r.width, r.y + r.height)
        );
    }
}

class SouthEastHandle extends LocatorHandle {
    SouthEastHandle(Figure owner) {
        super(owner, RelativeLocator.southEast());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(r.x, r.y),
            new Point(Math.max(r.x, x), Math.max(r.y, y))
        );
    }
}

class SouthHandle extends LocatorHandle {
    SouthHandle(Figure owner) {
        super(owner, RelativeLocator.south());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(r.x, r.y),
            new Point(r.x + r.width, Math.max(r.y, y))
        );
    }
}

class SouthWestHandle extends LocatorHandle {
    SouthWestHandle(Figure owner) {
        super(owner, RelativeLocator.southWest());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(Math.min(r.x + r.width, x), r.y),
            new Point(r.x + r.width, Math.max(r.y, y))
        );
    }
}

class WestHandle extends LocatorHandle {
    WestHandle(Figure owner) {
        super(owner, RelativeLocator.west());
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Rectangle r = owner().displayBox();
        owner().displayBox(
            new Point(Math.min(r.x + r.width, x), r.y),
            new Point(r.x + r.width, r.y + r.height)
        );
    }
}
