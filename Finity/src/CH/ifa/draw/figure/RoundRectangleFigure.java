/*
 * @(#)RoundRectangleFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Vector;

import CH.ifa.draw.connector.ShortestDistanceConnector;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.BoxHandleKit;
import CH.ifa.draw.handle.RadiusHandle;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;


/**
 * A round rectangle figure.
 * @see RadiusHandle
 * 
 * MIW: Bug in this class - If RouundRectangle is resized so that (yellow) RadiusHandle is outside RoundRectangle it ain't re-located / redrawn.
 * Appears that standard LocatorHandles need to force relocation of RadiusHandle and redrawing of damaged area including RadiusHandle.
 */
public  class RoundRectangleFigure extends AttributeFigure {

    private Rectangle   fDisplayBox;
    private int         fArcWidth;
    private int         fArcHeight;
    private static final int DEFAULT_ARC = 8;
    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 7907900248924036885L;
    @SuppressWarnings("unused")
	private int roundRectangleSerializedDataVersion = 1;

    public RoundRectangleFigure() {
        this(new Point(0,0), new Point(0,0));
        fArcWidth = fArcHeight = DEFAULT_ARC;
    }

    public RoundRectangleFigure(Point origin, Point corner) {
        basicDisplayBox(origin,corner);
        fArcWidth = fArcHeight = DEFAULT_ARC;
    }

    @Override
	public void basicDisplayBox(Point origin, Point corner) {
        fDisplayBox = new Rectangle(origin);
        fDisplayBox.add(corner);
        // MIW: Bug fixed here - makes sure Arc stays within bounds of DisplayBox
        fArcWidth = Math.min(fArcWidth, fDisplayBox.width);
        fArcHeight = Math.min(fArcHeight, fDisplayBox.height);
    }

    @Override
	protected void basicMoveBy(int x, int y) {
        fDisplayBox.translate(x,y);
    }

    @Override
	public Insets connectionInsets() {
        return new Insets(fArcHeight/2, fArcWidth/2, fArcHeight/2, fArcWidth/2);
    }

    @Override
	public Connector connectorAt(int x, int y) {
        return new ShortestDistanceConnector(this); // just for demo purposes
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
        g.fillRoundRect(r.x, r.y, r.width, r.height, fArcWidth, fArcHeight);
    }

    @Override
	public void drawFrame(Graphics g) {
        Rectangle r = displayBox();
        g.drawRoundRect(r.x, r.y, r.width-1, r.height-1, fArcWidth, fArcHeight);
    }

    /**
     * Gets the arc's width and height.
     */
    public Point getArc() {
        return new Point(fArcWidth, fArcHeight);
    }

    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = new Vector<Handle>();
        BoxHandleKit.addHandles(this, handles);
        handles.addElement(new RadiusHandle(this));
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
        fArcWidth = dr.readInt();
        fArcHeight = dr.readInt();
    }

    /**
     * Sets the arc's width and height.
     */
    public void setArc(int width, int height) {
        willChange();
        fArcWidth = width;
        fArcHeight = height;
        changed();
    }

    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fDisplayBox.x);
        dw.writeInt(fDisplayBox.y);
        dw.writeInt(fDisplayBox.width);
        dw.writeInt(fDisplayBox.height);
        dw.writeInt(fArcWidth);
        dw.writeInt(fArcHeight);
    }

}
