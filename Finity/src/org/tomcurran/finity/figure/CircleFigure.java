package org.tomcurran.finity.figure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Vector;

import org.tomcurran.finity.handle.CircleResizeHandle;

import CH.ifa.draw.connector.ChopEllipseConnector;
import CH.ifa.draw.figure.AttributeFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.locator.RelativeLocator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.util.Geom;

public class CircleFigure extends AttributeFigure {

	private static final long serialVersionUID = 5446287493886009904L;
	private static final int MAX_SIZE = 120;

	private Rectangle fDisplayBox;

	public CircleFigure() {
		this(new Point(0, 0), new Point(0, 0));
	}

	public CircleFigure(Point origin, Point corner) {
		setAttribute("FillColor", Color.WHITE);
		setAttribute("MaxSize", MAX_SIZE);
		basicDisplayBox(origin, corner);
	}

	@Override
	public void basicDisplayBox(Point origin, Point corner) {
		int radius = Math.min(((int)getAttribute("MaxSize")) / 2, (int) Geom.length(origin.x, origin.y, corner.x, corner.y));
		corner = new Point(origin.x + radius, origin.y + radius);
		origin = new Point(origin.x - radius, origin.y - radius);
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);
	}

	@Override
	protected void basicMoveBy(int x, int y) {
		fDisplayBox.translate(x, y);
	}

	@Override
	public Insets connectionInsets() {
		Rectangle r = fDisplayBox;
		int cx = r.width / 2;
		int cy = r.height / 2;
		return new Insets(cy, cx, cy, cx);
	}

	@Override
	public Connector connectorAt(int x, int y) {
		return new ChopEllipseConnector(this);
	}

	@Override
	public Rectangle displayBox() {
		return new Rectangle(fDisplayBox.x, fDisplayBox.y, fDisplayBox.width, fDisplayBox.height);
	}

	@Override
	public void drawBackground(Graphics g) {
		Rectangle r = displayBox();
		g.fillOval(r.x, r.y, r.width, r.height);
	}

	@Override
	public void drawFrame(Graphics g) {
		Rectangle r = displayBox();
		g.drawOval(r.x, r.y, r.width - 1, r.height - 1);
	}

	@Override
	public Vector<Handle> handles() {
		Vector<Handle> handles = new Vector<Handle>();
		handles.add(new CircleResizeHandle(this, RelativeLocator.north()));
		handles.add(new CircleResizeHandle(this, RelativeLocator.south()));
		handles.add(new CircleResizeHandle(this, RelativeLocator.east()));
		handles.add(new CircleResizeHandle(this, RelativeLocator.west()));
		return handles;
	}

	@Override
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		fDisplayBox = new Rectangle(dr.readInt(), dr.readInt(), dr.readInt(), dr.readInt());
	}

	@Override
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fDisplayBox.x);
		dw.writeInt(fDisplayBox.y);
		dw.writeInt(fDisplayBox.width);
		dw.writeInt(fDisplayBox.height);
	}

}
