/*
 * @(#)TextFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.FontSizeHandle;
import CH.ifa.draw.handle.NullHandle;
import CH.ifa.draw.locator.OffsetLocator;
import CH.ifa.draw.locator.RelativeLocator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.tool.TextTool;
import CH.ifa.draw.util.ColorMap;
import CH.ifa.draw.util.TextHolder;

/**
 * A text figure.
 *
 * @see TextTool
 */
public  class TextFigure
        extends AttributeFigure
        implements FigureChangeListener, TextHolder {

    /**
     * Creates the current font to be used for new text figures.
     */
    static public Font createCurrentFont() {
        return new Font(fgCurrentFontName, fgCurrentFontStyle, fgCurrentFontSize);
    }
    /**
     * Sets the current font name
     */
    static public void setCurrentFontName(String name) {
        fgCurrentFontName = name;
    }

    /**
     * Sets the current font size.
     */
    static public void setCurrentFontSize(int size) {
        fgCurrentFontSize = size;
    }
    /**
     * Sets the current font style.
     */
    static public void setCurrentFontStyle(int style) {
        fgCurrentFontStyle = style;
    }
    
    private int               fOriginX;
    private int               fOriginY;
    
    // cache of the TextFigure's size
    transient private boolean fSizeIsDirty = true;
    transient private int     fWidth;
    transient private int     fHeight;
    
    private String  fText;

    private Font    fFont;
    private boolean fIsReadOnly;
    private Figure  fObservedFigure = null;

    private OffsetLocator fLocator = null;
    
    private static String fgCurrentFontName  = "Arial";
    private static int    fgCurrentFontSize  = 12;
    private static int    fgCurrentFontStyle = Font.PLAIN;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 4599820785949456124L;

    @SuppressWarnings("unused")
	private int textFigureSerializedDataVersion = 1;

    public TextFigure() {
        fOriginX = 0;
        fOriginY = 0;
        fFont = createCurrentFont();
        setAttribute("FillColor", ColorMap.color("None"));
        fText = new String("");
        fSizeIsDirty = true;
    }

    /**
     * Tests whether the figure accepts typing.
     */
    @Override
	public boolean acceptsTyping() {
        return !fIsReadOnly;
    }

    @Override
	public void basicDisplayBox(Point newOrigin, Point newCorner) {
        fOriginX = newOrigin.x;
        fOriginY = newOrigin.y;
    }

    @Override
	protected void basicMoveBy(int x, int y) {
        fOriginX += x;
        fOriginY += y;
    }

    /**
     * Updates the location whenever the figure changes itself.
     */
    @Override
	public void changed() {
        super.changed();
        updateLocation();
    }

    @Override
	public void connect(Figure figure) {
        if (fObservedFigure != null)
            fObservedFigure.removeFigureChangeListener(this);

        fObservedFigure = figure;
        fLocator = new OffsetLocator(figure.connectedTextLocator(this));
        fObservedFigure.addFigureChangeListener(this);
        updateLocation();
    }

    /**
     * Disconnects the text figure.
     */
    public void disconnect() {
        fObservedFigure.removeFigureChangeListener(this);
        fObservedFigure = null;
        fLocator = null;
    }

    @Override
	public Rectangle displayBox() {
        Dimension extent = textExtent();
        return new Rectangle(fOriginX, fOriginY, extent.width, extent.height);
    }

    @Override
	public void drawBackground(Graphics g) {
        Rectangle r = displayBox();
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    @Override
	public void drawFrame(Graphics g) {
        g.setFont(fFont);
        g.setColor((Color) getAttribute("TextColor"));
        FontMetrics metrics = g.getFontMetrics(fFont);
        g.drawString(fText, fOriginX, fOriginY + metrics.getAscent());
    }

    @Override
	public void figureChanged(FigureChangeEvent e) {
        updateLocation();
    }

    @Override
	public void figureInvalidated(FigureChangeEvent e) {}

    @Override
	public void figureRemoved(FigureChangeEvent e) {
        if (listener() != null)
            listener().figureRequestRemove(new FigureChangeEvent(this));
    }

    @Override
	public void figureRequestRemove(FigureChangeEvent e) {}

    @Override
	public void figureRequestUpdate(FigureChangeEvent e) {}

    /**
     * A text figure understands the "FontSize", "FontStyle", and "FontName"
     * attributes.
     */
    @Override
	public Object getAttribute(String name) {
        Font font = getFont();
        if (name.equals("FontSize"))
            return new Integer(font.getSize());
        if (name.equals("FontStyle"))
            return new Integer(font.getStyle());
        if (name.equals("FontName"))
            return font.getName();
        return super.getAttribute(name);
    }

    /**
     * Gets the font.
     */
    @Override
	public Font getFont() {
        return fFont;
    }

    /**
     * Gets the text shown by the text figure.
     */
    @Override
	public String getText() {
        return fText;
    }

    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = new Vector<Handle>();
        handles.addElement(new NullHandle(this, RelativeLocator.northWest()));
        handles.addElement(new NullHandle(this, RelativeLocator.northEast()));
        handles.addElement(new NullHandle(this, RelativeLocator.southEast()));
        handles.addElement(new FontSizeHandle(this, RelativeLocator.southWest()));
        return handles;
    }

    private void markDirty() {
        fSizeIsDirty = true;
    }

    @Override
	public void moveBy(int x, int y) {
        willChange();
        basicMoveBy(x, y);
        if (fLocator != null)
            fLocator.moveBy(x, y);
        changed();
    }

    /**
     * Gets the number of columns to be overlaid when the figure is edited.
     */
    @Override
	public int overlayColumns() {
        int length = getText().length();
        int columns = 20;
        if (length != 0)
            columns = getText().length()+ 3;
        return columns;
    }

    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        markDirty();
        fOriginX = dr.readInt();
        fOriginY = dr.readInt();
        fText = dr.readString();
        fFont = new Font(dr.readString(), dr.readInt(), dr.readInt());
        fIsReadOnly = dr.readBoolean();

        fObservedFigure = (Figure)dr.readStorable();
        if (fObservedFigure != null)
            fObservedFigure.addFigureChangeListener(this);
        fLocator = (OffsetLocator)dr.readStorable();
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException {

        s.defaultReadObject();

        if (fObservedFigure != null)
            fObservedFigure.addFigureChangeListener(this);
        markDirty();
    }

    /**
     * Tests whether this figure is read only.
     */
    public boolean readOnly() {
        return fIsReadOnly;
    }
    @Override
	public void release() {
        super.release();
        if (fObservedFigure != null)
            fObservedFigure.removeFigureChangeListener(this);
    }
    /**
     * A text figure understands the "FontSize", "FontStyle", and "FontName"
     * attributes.
     */
    @Override
	public void setAttribute(String name, Object value) {
        Font font = getFont();
        if (name.equals("FontSize")) {
            Integer s = (Integer)value;
            setFont(new Font(font.getName(), font.getStyle(), s.intValue()) );
        }
        else if (name.equals("FontStyle")) {
            Integer s = (Integer)value;
            int style = font.getStyle();
            if (s.intValue() == Font.PLAIN)
                style = Font.PLAIN;
            else
                style = style ^ s.intValue();
            setFont(new Font(font.getName(), style, font.getSize()) );
        }
        else if (name.equals("FontName")) {
            String n = (String)value;
            setFont(new Font(n, font.getStyle(), font.getSize()) );
        }
        else
            super.setAttribute(name, value);
    }

    /**
     * Sets the font.
     */
    public void setFont(Font newFont) {
        willChange();
        fFont = newFont;
        markDirty();
        changed();
    }

    /**
     * Sets the read only status of the text figure.
     */
    public void setReadOnly(boolean isReadOnly) {
        fIsReadOnly = isReadOnly;
    }

    /**
     * Sets the text shown by the text figure.
     */
    @Override
	public void setText(String newText) {
        if (!newText.equals(fText)) {
            willChange();
            fText = new String(newText);
            markDirty();
            changed();
        }
    }


    @Override
	public Rectangle textDisplayBox() {
        return displayBox();
    }

    private Dimension textExtent() {
        if (!fSizeIsDirty)
            return new Dimension(fWidth, fHeight);
        
        // MIW: The following is the best I could find to replace @deprecated getFontMetrics
        // Needs access to Graphics, hence creation of BufferedImage
        // From here can get FontMetrics which can be used to calculate
        // width and height of *text* in a *font* on a certain *graphics context*
        //FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(fFont);
        //fWidth = metrics.stringWidth(fText);
        //fHeight = metrics.getHeight();
        
        // BufferedImage just used as a way of gaining access to Graphics object
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        FontMetrics fm = bi.getGraphics().getFontMetrics(fFont);
        fWidth = fm.stringWidth(fText);
        fHeight = fm.getHeight();    

        fSizeIsDirty = false;
        return new Dimension(fWidth, fHeight);
    }

    /**
     * Updates the location relative to the connected figure.
     * The TextFigure is centered around the located point.
     */
    protected void updateLocation() {
        if (fLocator != null) {
            Point p = fLocator.locate(fObservedFigure);
            p.x -= size().width/2 + fOriginX;
            p.y -= size().height/2 + fOriginY;

            if (p.x != 0 || p.y != 0) {
                willChange();
                basicMoveBy(p.x, p.y);
                changed();
            }
        }
    }

    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fOriginX);
        dw.writeInt(fOriginY);
        dw.writeString(fText);
        dw.writeString(fFont.getName());
        dw.writeInt(fFont.getStyle());
        dw.writeInt(fFont.getSize());
        dw.writeBoolean(fIsReadOnly);
        dw.writeStorable(fObservedFigure);
        dw.writeStorable(fLocator);
    }
}
