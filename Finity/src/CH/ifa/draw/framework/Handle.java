/*
 * @(#)Handle.java 5.1
 *
 */

package CH.ifa.draw.framework;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Handles are used to change a figure by direct manipulation.
 * Handles know their owning figure and they provide methods to
 * locate the handle on the figure and to track changes.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld004.htm>Adapter</a></b><br>
 * Handles adapt the operations to manipulate a figure to a common interface.
 *
 * @see Figure
 */
public interface Handle {

    public static final int HANDLESIZE = 8;

    /**
     * Tests if a point is contained in the handle.
     */
    public boolean containsPoint(int x, int y);

    /**
     * Gets the display box of the handle.
     */
    public Rectangle displayBox();

    /**
     * Draws this handle.
     */
    public void draw(Graphics g);

    /**
     * @deprecated As of version 4.1,
     * use invokeEnd(x, y, anchorX, anchorY, drawingView).
     *
     * Tracks the end of the interaction.
     */
    @Deprecated
	public void invokeEnd  (int dx, int dy, Drawing drawing);

    /**
     * Tracks the end of the interaction.
     * @param x the current x position
     * @param y the current y position
     * @param anchorX the x position where the interaction started
     * @param anchorY the y position where the interaction started
     */
    public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view);

    /**
     * @deprecated As of version 4.1,
     * use invokeStart(x, y, drawingView)
     * Tracks the start of the interaction. The default implementation
     * does nothing.
     * @param x the x position where the interaction started
     * @param y the y position where the interaction started
     */
    @Deprecated
	public void invokeStart(int  x, int  y, Drawing drawing);

    /**
     * 
     * use invokeStart(x, y, drawingView)
     * Tracks the start of the interaction. The default implementation
     * does nothing.
     * @param x the x position where the interaction started
     * @param y the y position where the interaction started
     * @param view the handles container
     */
    // MIW: don't think this is deprecated - it is using drawingView
	public void invokeStart(int  x, int  y, DrawingView view);

    /**
     * @deprecated As of version 4.1,
     * use invokeStep(x, y, anchorX, anchorY, drawingView)
     *
     * Tracks a step of the interaction.
     * @param dx x delta of this step
     * @param dy y delta of this step
     */
    @Deprecated
	public void invokeStep (int dx, int dy, Drawing drawing);

    /**
     * Tracks a step of the interaction.
     * @param x the current x position
     * @param y the current y position
     * @param anchorX the x position where the interaction started
     * @param anchorY the y position where the interaction started
     */
    public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view);

    /**
     * Locates the handle on the figure. The handle is drawn
     * centered around the returned point.
     */
    public abstract Point locate();

    /**
     * Gets the handle's owner.
     */
    public Figure owner();
}


