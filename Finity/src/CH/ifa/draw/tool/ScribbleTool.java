/*
 * @(#)ScribbleTool.java 5.1
 *
 */

package CH.ifa.draw.tool;

import java.awt.event.MouseEvent;

import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.framework.DrawingView;

/**
 * Tool to scribble a PolyLineFigure
 * @see PolyLineFigure
 */
public class ScribbleTool extends AbstractTool {

    private PolyLineFigure  fScribble;
    private int             fLastX, fLastY;

    public ScribbleTool(DrawingView view) {
        super(view);
    }

    @Override
	public void activate() {
        super.activate();
        fScribble = null;
    }

    @Override
	public void deactivate() {
        super.deactivate();
        if (fScribble != null) {
            if (fScribble.size().width < 4 || fScribble.size().height < 4)
                drawing().remove(fScribble);
        }
    }

    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
        if (e.getClickCount() >= 2) {
            fScribble = null;
            editor().toolDone();
        }
        else {
            // use original event coordinates to avoid
            // suppress that the scribble is constrained to
            // the grid
            point(e.getX(), e.getY());
        }
    }

    @Override
	public void mouseDrag(MouseEvent e, int x, int y) {
        if (fScribble != null)
            point(e.getX(), e.getY());
    }

    private void point(int x, int y) {
        if (fScribble == null) {
            fScribble = new PolyLineFigure(x, y);
            view().add(fScribble);
        } else if (fLastX != x || fLastY != y)
            fScribble.addPoint(x, y);

        fLastX = x;
        fLastY = y;
    }
}
