/*
 * @(#)LineConnection.java 5.1
 *
 */

package CH.ifa.draw.figure.connection;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

import CH.ifa.draw.figure.ArrowTip;
import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.ChangeConnectionEndHandle;
import CH.ifa.draw.handle.ChangeConnectionStartHandle;
import CH.ifa.draw.handle.PolyLineHandle;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * A LineConnection is a standard implementation of the ConnectionFigure
 * interface. The interface is implemented with PolyLineFigure.
 * 
 * @see ConnectionFigure
 */

// MIW: Modernised

public class LineConnection extends PolyLineFigure implements ConnectionFigure {

	protected Connector fStart = null;
	protected Connector fEnd = null;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 6883731614578414801L;

	// private int lineConnectionSerializedDataVersion = 1;

	/**
	 * Constructs a LineConnection. A connection figure has an arrow decoration
	 * at the start and end.
	 */
	public LineConnection() {
		super(4);
		setStartDecoration(new ArrowTip());
		setEndDecoration(new ArrowTip());
	}

	/**
	 * Ensures that a connection is updated if the connection was moved.
	 */
	@Override
	protected void basicMoveBy(int dx, int dy) {
		// don't move the start and end point since they are connected
		for (int i = 1; i < fPoints.size() - 1; i++)
			fPoints.elementAt(i).translate(dx, dy);

		updateConnection(); // make sure that we are still connected
	}

	/**
	 * Tests whether a figure can be a connection target. ConnectionFigures
	 * cannot be connected and return false.
	 */
	@Override
	public boolean canConnect() {
		return false;
	}

	/**
	 * Tests whether two figures can be connected.
	 */
	@Override
	public boolean canConnect(Figure start, Figure end) {
		return true;
	}

	/**
	 * Sets the end figure of the connection.
	 */
	@Override
	public void connectEnd(Connector end) {
		fEnd = end;
		endFigure().addFigureChangeListener(this);
		handleConnect(startFigure(), endFigure());
	}

	/**
	 * Tests whether a connection connects the same figures as another
	 * ConnectionFigure.
	 */
	@Override
	public boolean connectsSame(ConnectionFigure other) {
		return other.start() == start() && other.end() == end();
	}

	/**
	 * Sets the start figure of the connection.
	 */
	@Override
	// MIW: Bit of indirection here - adding fStart.owner() as listener - the
	// Figure associated with Connector start
	public void connectStart(Connector start) {
		fStart = start;
		startFigure().addFigureChangeListener(this);
	}

	/**
	 * Disconnects the end figure.
	 */
	@Override
	public void disconnectEnd() {
		handleDisconnect(startFigure(), endFigure());
		endFigure().removeFigureChangeListener(this);
		fEnd = null;
	}

	/**
	 * Disconnects the start figure.
	 */
	@Override
	public void disconnectStart() {
		startFigure().removeFigureChangeListener(this);
		fStart = null;
	}

	/**
	 * Gets the end figure of the connection.
	 */
	@Override
	public Connector end() {
		return fEnd;
	}

	/**
	 * Gets the end figure of the connection.
	 */
	public Figure endFigure() {
		if (end() != null)
			return end().owner();
		return null;
	}

	/**
	 * Gets the end point.
	 */
	@Override
	public Point endPoint() {
		Point p = fPoints.lastElement();
		return new Point(p.x, p.y);
	}

	/**
	 * Sets the end point.
	 */
	@Override
	public void endPoint(int x, int y) {
		willChange();
		if (fPoints.size() < 2)
			fPoints.addElement(new Point(x, y));
		else
			fPoints.setElementAt(new Point(x, y), fPoints.size() - 1);
		changed();
	}

	@Override
	public void figureChanged(FigureChangeEvent e) {
		updateConnection();
	}

	@Override
	public void figureInvalidated(FigureChangeEvent e) {
	}

	@Override
	public void figureRemoved(FigureChangeEvent e) {
		if (listener() != null)
			listener().figureRequestRemove(new FigureChangeEvent(this));
	}

	@Override
	public void figureRequestRemove(FigureChangeEvent e) {
	}

	@Override
	public void figureRequestUpdate(FigureChangeEvent e) {
	}

	/**
	 * Handles the connection of a connection. Override this method to handle
	 * this event.
	 */
	protected void handleConnect(Figure start, Figure end) {
	}

	/**
	 * Handles the disconnection of a connection. Override this method to handle
	 * this event.
	 */
	protected void handleDisconnect(Figure start, Figure end) {
	}

	/**
	 * Gets the handles of the figure. It returns the normal PolyLineHandles but
	 * adds ChangeConnectionHandles at the start and end.
	 */
	@Override
	public Vector<Handle> handles() {
		Vector<Handle> handles = new Vector<Handle>(fPoints.size());
		handles.addElement(new ChangeConnectionStartHandle(this));
		for (int i = 1; i < fPoints.size() - 1; i++)
			handles.addElement(new PolyLineHandle(this, locator(i), i));
		handles.addElement(new ChangeConnectionEndHandle(this));
		return handles;
	}

	/**
	 * Inserts the point and updates the connection.
	 */
	@Override
	public void insertPointAt(Point p, int i) {
		super.insertPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Lays out the connection. This is called when the connection itself
	 * changes. By default the connection is recalculated
	 */
	public void layoutConnection() {
		updateConnection();
	}

	@Override
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		Connector start = (Connector) dr.readStorable();
		if (start != null)
			connectStart(start);
		Connector end = (Connector) dr.readStorable();
		if (end != null)
			connectEnd(end);
		if (start != null && end != null)
			updateConnection();
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException {

		s.defaultReadObject();

		if (fStart != null)
			connectStart(fStart);
		if (fEnd != null)
			connectEnd(fEnd);
	}

	@Override
	public void release() {
		super.release();
		handleDisconnect(startFigure(), endFigure());
		if (fStart != null)
			startFigure().removeFigureChangeListener(this);
		if (fEnd != null)
			endFigure().removeFigureChangeListener(this);
	}

	/**
	 * Removes the point and updates the connection.
	 */
	@Override
	public void removePointAt(int i) {
		super.removePointAt(i);
		layoutConnection();
	}

	/**
	 * Sets the point and updates the connection.
	 */
	@Override
	public void setPointAt(Point p, int i) {
		super.setPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Gets the start figure of the connection.
	 */
	@Override
	public Connector start() {
		return fStart;
	}

	/**
	 * Gets the start figure of the connection.
	 */
	public Figure startFigure() {
		if (start() != null)
			return start().owner();
		return null;
	}

	/**
	 * Gets the start point.
	 */
	@Override
	public Point startPoint() {
		Point p = fPoints.firstElement();
		return new Point(p.x, p.y);
	}

	/**
	 * Sets the start point.
	 */
	@Override
	public void startPoint(int x, int y) {
		willChange();
		if (fPoints.size() == 0)
			fPoints.addElement(new Point(x, y));
		else
			fPoints.setElementAt(new Point(x, y), 0);
		changed();
	}

	/**
	 * Updates the connection.
	 */
	@Override
	public void updateConnection() {
		if (fStart != null) {
			Point start = fStart.findStart(this);
			startPoint(start.x, start.y);
		}
		if (fEnd != null) {
			Point end = fEnd.findEnd(this);
			endPoint(end.x, end.y);
		}
	}

	@Override
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeStorable(fStart);
		dw.writeStorable(fEnd);
	}
}
