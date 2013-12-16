/*
 * @(#)ActionTool.java 5.1
 *
 */

package CH.ifa.draw.tool;

import java.awt.event.MouseEvent;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * A tool that performs an action when it is active and
 * the mouse is clicked.
 */
public abstract class ActionTool extends AbstractTool {

    public ActionTool(DrawingView itsView) {
        super(itsView);
    }

    /**
     * Performs an action with the touched figure.
     */
    public abstract void action(Figure figure);

    /**
     * Add the touched figure to the selection an invoke action
     * @see #action()
     */
    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
        Figure target = drawing().findFigure(x, y);
        if (target != null) {
            view().addToSelection(target);
            action(target);
        }
    }

    @Override
	public void mouseUp(MouseEvent e, int x, int y) {
    	editor().toolDone();
    }
}
