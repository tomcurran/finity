/*
 * @(#)PertDependency.java 5.1
 *
 */

package CH.ifa.draw.samples.pert;

import java.awt.Color;
import java.util.Vector;

import CH.ifa.draw.figure.ArrowTip;
import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.NullHandle;


public class PertDependency extends LineConnection {
    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -7959500008698525009L;
    @SuppressWarnings("unused")
	private int pertDependencySerializedDataVersion = 1;

    public PertDependency() {
        setEndDecoration(new ArrowTip());
        setStartDecoration(null);
    }

    @Override
	public boolean canConnect(Figure start, Figure end) {
        return (start instanceof PertFigure && end instanceof PertFigure);
    }

    @Override
	public void handleConnect(Figure start, Figure end) {
        PertFigure source = (PertFigure)start;
        PertFigure target = (PertFigure)end;
        if (source.hasCycle(target)) {
            setAttribute("FrameColor", Color.red);
        } else {
            target.addPreTask(source);
            source.addPostTask(target);
            source.notifyPostTasks();
        }
    }

    @Override
	public void handleDisconnect(Figure start, Figure end) {
        PertFigure source = (PertFigure)start;
        PertFigure target = (PertFigure)end;
        if (target != null) {
            target.removePreTask(source);
            target.updateDurations();
        }
        if (source != null)
            source.removePostTask(target);
   }

    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = super.handles();
        // don't allow to reconnect the starting figure
        handles.setElementAt(
            new NullHandle(this, PolyLineFigure.locator(0)), 0);
        return handles;
    }
}
