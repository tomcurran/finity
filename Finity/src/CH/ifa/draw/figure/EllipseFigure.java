/*
 * @(#)EllipseFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Vector;

import CH.ifa.draw.connector.ChopEllipseConnector;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.BoxHandleKit;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * An ellipse figure.
 */
public class EllipseFigure extends AttributeFigure {

    private Rectangle   fDisplayBox;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -6856203289355118951L;
    @SuppressWarnings("unused")
	private int ellipseFigureSerializedDataVersion = 1;

    public EllipseFigure() {
        this(new Point(0,0), new Point(0,0));
    }

    public EllipseFigure(Point origin, Point corner) {
        basicDisplayBox(origin,corner);
    }

    @Override
	public void basicDisplayBox(Point origin, Point corner) {
        fDisplayBox = new Rectangle(origin);
        fDisplayBox.add(corner);
    }

    @Override
	protected void basicMoveBy(int x, int y) {
        fDisplayBox.translate(x,y);
    }

    @Override
	public Insets connectionInsets() {
        Rectangle r = fDisplayBox;
        int cx = r.width/2;
        int cy = r.height/2;
        return new Insets(cy, cx, cy, cx);
    }

    @Override
	public Connector connectorAt(int x, int y) {
        return new ChopEllipseConnector(this);
    }

    @Override
	public Rectangle displayBox() {
        return new Rectangle(
            fDisplayBox.x,
            fDisplayBox.y,
            fDisplayBox.width,
            fDisplayBox.height);
    }

    @Override
	public void drawBackground(Graphics g) {
        Rectangle r = displayBox();
        g.fillOval(r.x, r.y, r.width, r.height);
    }

    @Override
	public void drawFrame(Graphics g) {
        Rectangle r = displayBox();
        g.drawOval(r.x, r.y, r.width-1, r.height-1);
    }

    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = new Vector<Handle>();
        BoxHandleKit.addHandles(this, handles);
        return handles;
    }

    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        fDisplayBox = new Rectangle(
            dr.readInt(),
            dr.readInt(),
            dr.readInt(),
            dr.readInt());
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
