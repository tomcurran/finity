/*
 * @(#)AbstractTool.java 5.1
 *
 */

package CH.ifa.draw.tool;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Tool;

/**
 * Default implementation support for Tools.
 *
 * @see DrawingView
 * @see Tool
 */

public class AbstractTool implements Tool {

    protected DrawingView  fView;

    /**
     * The position of the initial mouse down.
     */
    protected int     fAnchorX, fAnchorY;

    /**
     * Constructs a tool for the given view.
     */
    public AbstractTool(DrawingView itsView) {
        fView = itsView;
    }

    /**
     * Activates the tool for the given view. This method is called
     * whenever the user switches to this tool. Use this method to
     * reinitialize a tool.
     */
    @Override
	public void activate() {
        fView.clearSelection();
    }

    /**
     * Deactivates the tool. This method is called whenever the user
     * switches to another tool. Use this method to do some clean-up
     * when the tool is switched. Subclassers should always call
     * super.deactivate.
     */
    @Override
	public void deactivate() {
        fView.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Gets the tool's drawing.
     */
    public Drawing drawing() {
        return fView.drawing();
    }

    /**
     * Gets the tool's editor.
     */
    public DrawingEditor editor() {
        return fView.editor();
    }

    /**
     * Handles key down events in the drawing view.
     */
    @Override
	public void keyDown(KeyEvent evt, int key) {
    }

    /**
     * Handles mouse down events in the drawing view.
     */
    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
        fAnchorX = x;
        fAnchorY = y;
    }

    /**
     * Handles mouse drag events in the drawing view.
     */
    @Override
	public void mouseDrag(MouseEvent e, int x, int y) {
    }

    /**
     * Handles mouse moves (if the mouse button is up).
     */
    @Override
	public void mouseMove(MouseEvent evt, int x, int y) {
    }

    /**
     * Handles mouse up in the drawing view.
     */
    @Override
	public void mouseUp(MouseEvent e, int x, int y) {
    }

    /**
     * Gets the tool's view.
     */
    public DrawingView view() {
        return fView;
    }
}
