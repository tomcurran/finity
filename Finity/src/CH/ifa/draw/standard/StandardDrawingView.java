/*
 * @(#)StandardDrawingView.java 5.1
 *
 */

package CH.ifa.draw.standard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PrintGraphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JPanel;

import CH.ifa.draw.command.Command;
import CH.ifa.draw.command.DeleteCommand;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingChangeEvent;
import CH.ifa.draw.framework.DrawingChangeListener;
import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Painter;
import CH.ifa.draw.framework.PointConstrainer;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.painter.BufferedUpdateStrategy;
import CH.ifa.draw.util.FigureSelection;
import CH.ifa.draw.util.Geom;

/**
 * The standard implementation of DrawingView.
 * @see DrawingView
 * @see Painter
 * @see Tool
 */

public  class StandardDrawingView
        extends JPanel
        implements DrawingView,
                   MouseListener,
                   MouseMotionListener,
                   KeyListener {

    /**
     * The DrawingEditor of the view.
     * @see #tool
     * @see #setStatus
     */
    transient private DrawingEditor   fEditor;

    /**
     * The shown drawing.
     */
    private Drawing         fDrawing;

    /**
     * the accumulated damaged area
     */
    private transient Rectangle fDamage = null;

    /**
     * The list of currently selected figures.
     */
    transient private Vector<Figure> fSelection;

    /**
     * The shown selection handles.
     */
    transient private Vector<Handle> fSelectionHandles;

    /**
     * The preferred size of the view
     */
    private Dimension fViewSize;

    /**
     * The position of the last mouse click
     * inside the view.
     */
    private Point fLastClick;

    /**
     * A vector of optional backgrounds. The vector maintains
     * a list a view painters that are drawn before the contents,
     * that is in the background.
     */
    private Vector<Painter> fBackgrounds = null;

    /**
     * A vector of optional foregrounds. The vector maintains
     * a list a view painters that are drawn after the contents,
     * that is in the foreground.
     */
    private Vector<Painter> fForegrounds = null;

    /**
     * The update strategy used to repair the view.
     */
    private Painter fUpdateStrategy;

    /**
     * The grid used to constrain points for snap to
     * grid functionality.
     */
    private PointConstrainer fConstrainer;

    /*
     * Serialization support. In JavaDraw only the Drawing is serialized.
     * However, for beans support StandardDrawingView supports
     * serialization
     */
    private static final long serialVersionUID = -3878153366174603336L;
    @SuppressWarnings("unused")
	private int drawingViewSerializedDataVersion = 1;

    /**
     * Constructs the view.
     */
    public StandardDrawingView(DrawingEditor editor, int width, int height) {
        fEditor = editor;
        fViewSize = new Dimension(width,height);
        fLastClick = new Point(0, 0);
        fConstrainer = null;
        fSelection = new Vector<Figure>();
        setDisplayUpdate(new BufferedUpdateStrategy());
        setBackground(Color.lightGray);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    /**
     * Adds a figure to the drawing.
     * @return the added figure.
     */
    @Override
	public Figure add(Figure figure) {
        return drawing().add(figure);
    }

    /**
     * Adds a vector of figures to the drawing.
     */
    // MIW: Removed FigureEnumeration and added Figure to Vector
    @Override
	public void addAll(Vector<Figure> figures) {
        Enumeration<Figure> k = figures.elements();
        while (k.hasMoreElements())
            add(k.nextElement());
    }

    /**
     * Adds a background.
     */
    public void addBackground(Painter painter)  {
        if (fBackgrounds == null)
            fBackgrounds = new Vector<Painter>(3);
        fBackgrounds.addElement(painter);
        repaint();
    }

    /**
     * Adds a foreground.
     */
    public void addForeground(Painter painter)  {
        if (fForegrounds == null)
            fForegrounds = new Vector<Painter>(3);
        fForegrounds.addElement(painter);
        repaint();
    }

    /**
     * Adds a figure to the current selection.
     */
    @Override
	public void addToSelection(Figure figure) {
        if (!fSelection.contains(figure)) {
            fSelection.addElement(figure);
            //MIW: Added so that current selection brought to front of drawing
            // In clearSelection it is sent to back - so that text tends to stay at front.
            drawing().bringToFront(figure);
            fSelectionHandles = null;
            figure.invalidate();
            selectionChanged();
        }
    }

    /**
     * Adds a vector of figures to the current selection.
     */
    @Override
	public void addToSelectionAll(Vector<Figure> figures) {
        Enumeration<Figure> k = (figures.elements());
        while (k.hasMoreElements())
            addToSelection(k.nextElement());
    }

    /**
     * Refreshes the drawing if there is some accumulated damage
     */
    @Override
	public synchronized void checkDamage() {
        Enumeration<DrawingChangeListener> each = drawing().drawingChangeListeners();
        while (each.hasMoreElements()) {
            DrawingChangeListener l = each.nextElement();
            if (l instanceof DrawingView) {
                ((DrawingView)l).repairDamage();
            }
        }
    }

    private void checkMinimumSize() {
        Enumeration<Figure> k = drawing().figures();
        Dimension d = new Dimension(0, 0);
        while (k.hasMoreElements()) {
            Rectangle r = k.nextElement().displayBox();
            d.width = Math.max(d.width, r.x+r.width);
            d.height = Math.max(d.height, r.y+r.height);
        }
        if (fViewSize.height < d.height || fViewSize.width < d.width) {
            fViewSize.height = d.height+10;
            fViewSize.width = d.width+10;
            setSize(fViewSize);
        }
    }

    /**
     * Clears the current selection.
     */
    @Override
	public void clearSelection() {
		Enumeration<Figure> k = selectionElements();
		while (k.hasMoreElements()) {
			Figure f = k.nextElement();
			// MIW: Paired with change in addToSelection
			// Once no longer selected send to back of diagram
			drawing().sendToBack(f);
			f.invalidate();
			fSelection = new Vector<Figure>();
			fSelectionHandles = null;
			selectionChanged();
		}
	}

    /**
     * Constrains a point to the current grid.
     */
    protected Point constrainPoint(Point p) {
        // Constrain to view size
        Dimension size = getSize();
        //p.x = Math.min(size.width, Math.max(1, p.x));
        //p.y = Math.min(size.height, Math.max(1, p.y));
        p.x = Geom.range(1, size.width, p.x);
        p.y = Geom.range(1, size.height, p.y);

        if (fConstrainer != null )
            return fConstrainer.constrainPoint(p);
        return p;
	}

    /**
     * Draws the contents of the drawing view.
     * The view has three layers: background, drawing, handles.
     * The layers are drawn in back to front order.
     */
    @Override
	public void drawAll(Graphics g) {
        boolean isPrinting = g instanceof PrintGraphics;
        drawBackground(g);
        if (fBackgrounds != null && !isPrinting)
            drawPainters(g, fBackgrounds);
        drawDrawing(g);
        if (fForegrounds != null && !isPrinting)
            drawPainters(g, fForegrounds);
        if (!isPrinting)
            drawHandles(g);
    }

    /**
     * Draws the background. If a background pattern is set it
     * is used to fill the background. Otherwise the background
     * is filled in the background color.
     */
    @Override
	public void drawBackground(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getBounds().width, getBounds().height);
    }

    /**
     * Draws the drawing.
     */
    @Override
	public void drawDrawing(Graphics g) {
        fDrawing.draw(g);
    }

    /**
     * Draws the currently active handles.
     */
    @Override
	public void drawHandles(Graphics g) {
        Enumeration<Handle> k = selectionHandles();
        while (k.hasMoreElements())
            (k.nextElement()).draw(g);
    }

    /**
     * Gets the drawing.
     */
    @Override
	public Drawing drawing() {
        return fDrawing;
    }

    @Override
	public void drawingInvalidated(DrawingChangeEvent e) {
        Rectangle r = e.getInvalidatedRectangle();
        if (fDamage == null)
            fDamage = r;
        else
            fDamage.add(r);
    }

    @Override
	public void drawingRequestUpdate(DrawingChangeEvent e) {
        repairDamage();
    }

    private void drawPainters(Graphics g, Vector<Painter> v) {
        for (int i = 0; i < v.size(); i++)
            v.elementAt(i).draw(g, this);
    }

    /**
     * Gets the editor.
     */
    @Override
	public DrawingEditor editor() {
        return fEditor;
    }

    /**
     * Finds a handle at the given coordinates.
     * @return the hit handle, null if no handle is found.
     */
    @Override
	public Handle findHandle(int x, int y) {
        Handle handle;

        Enumeration<Handle> k = selectionHandles();
        while (k.hasMoreElements()) {
            handle = k.nextElement();
            if (handle.containsPoint(x, y))
                return handle;
        }
        return null;
    }

    /**
     * Freezes the view by acquiring the drawing lock.
     * @see Drawing#lock
     */
    @Override
	public void freezeView() {
        drawing().lock();
    }

    /**
     * Gets the current constrainer.
     */
    @Override
	public PointConstrainer getConstrainer() {
        return fConstrainer;
    }

    /**
     * Gets the current selection as a FigureSelection. A FigureSelection
     * can be cut, copied, pasted.
     */
    @Override
	public FigureSelection getFigureSelection() {
        return new FigureSelection(selectionZOrdered());
    }

    /**
     * Gets the minimum dimension of the drawing.
     */
    @Override
	public Dimension getMinimumSize() {
        return fViewSize;
    }

    /**
     * Gets the preferred dimension of the drawing..
     */
    @Override
	public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Handles cursor keys by moving all the selected figures
     * one grid point in the cursor direction.
     */
    protected void handleCursorKey(int key) {
        int dx = 0, dy = 0;
        int stepX = 1, stepY = 1;
        // should consider Null Object.
        if (fConstrainer != null) {
            stepX = fConstrainer.getStepX();
            stepY = fConstrainer.getStepY();
        }

        switch (key) {
        case KeyEvent.VK_DOWN:
            dy = stepY;
            break;
        case KeyEvent.VK_UP:
            dy = -stepY;
            break;
        case KeyEvent.VK_RIGHT:
            dx = stepX;
            break;
        case KeyEvent.VK_LEFT:
            dx = -stepX;
            break;
        }
        moveSelection(dx, dy);
    }

    @Override
	public boolean isFocusTraversable() {
        return true;
    }

    /**
     * Handles key down events. Cursor keys are handled
     * by the view the other key events are delegated to the
     * currently active tool.
     * @return whether the event was handled.
     */
    @Override
	public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if ((code == KeyEvent.VK_BACK_SPACE) || (code == KeyEvent.VK_DELETE)) {
            Command cmd = new DeleteCommand("Delete", this);
            cmd.execute();
        } else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_UP ||
            code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_LEFT) {
            handleCursorKey(code);
        } else {
            tool().keyDown(e, code);
        }
        checkDamage();
    }

    @Override
	public void keyReleased(KeyEvent e) {}

    @Override
	public void keyTyped(KeyEvent e) {}

    /**
     * Gets the position of the last click inside the view.
     */
    @Override
	public Point lastClick() {
        return fLastClick;
    }

    @Override
	public void mouseClicked(MouseEvent e) {}

    /**
     * Handles mouse drag events. The event is delegated to the
     * currently active tool.
     * @return whether the event was handled.
     */
    @Override
	public void mouseDragged(MouseEvent e) {
        Point p = constrainPoint(new Point(e.getX(), e.getY()));
        tool().mouseDrag(e, p.x, p.y);
        checkDamage();
    }

    // listener methods we are not interested in
    @Override
	public void mouseEntered(MouseEvent e) {}


    @Override
	public void mouseExited(MouseEvent e) {}

    /**
     * Handles mouse move events. The event is delegated to the
     * currently active tool.
     * @return whether the event was handled.
     */
    @Override
	public void mouseMoved(MouseEvent e) {
        tool().mouseMove(e, e.getX(), e.getY());
    }

    /**
     * Handles mouse down events. The event is delegated to the
     * currently active tool.
     * @return whether the event was handled.
     */
    @Override
	public void mousePressed(MouseEvent e) {
    	requestFocusInWindow(); // MIW: Replaced requestFocus(). See JDK 1.4
        Point p = constrainPoint(new Point(e.getX(), e.getY()));
        fLastClick = new Point(e.getX(), e.getY());
        tool().mouseDown(e, p.x, p.y);
        checkDamage();
    }

    /**
     * Handles mouse up events. The event is delegated to the
     * currently active tool.
     * @return whether the event was handled.
     */
    @Override
	public void mouseReleased(MouseEvent e) {
        Point p = constrainPoint(new Point(e.getX(), e.getY()));
        tool().mouseUp(e, p.x, p.y);
        checkDamage();
    }

    private void moveSelection(int dx, int dy) {
        Enumeration<Figure> figures = selectionElements();
        while (figures.hasMoreElements())
            figures.nextElement().moveBy(dx, dy);
        checkDamage();
    }

    /**
     * Paints the drawing view. The actual drawing is delegated to
     * the current update strategy.
     * @see Painter
     */
    @Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g); 						//MIW SWING
        fUpdateStrategy.draw(g, this);
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException {

        s.defaultReadObject();

        fSelection = new Vector<Figure>(); // could use lazy initialization instead
        if (fDrawing != null)
            fDrawing.addDrawingChangeListener(this);
    }

    /**
     * Removes a figure from the drawing.
     * @return the removed figure
     */
    @Override
	public Figure remove(Figure figure) {
        return drawing().remove(figure);
    }

    /**
     * Removes a background.
     */
    public void removeBackground(Painter painter)  {
        if (fBackgrounds != null)
            fBackgrounds.removeElement(painter);
        repaint();
    }

    /**
     * Removes a foreground.
     */
    public void removeForeground(Painter painter)  {
        if (fForegrounds != null)
            fForegrounds.removeElement(painter);
        repaint();
    }

    /**
     * Removes a figure from the selection.
     */
    @Override
	public void removeFromSelection(Figure figure) {
        if (fSelection.contains(figure)) {
            fSelection.removeElement(figure);
            fSelectionHandles = null;
            figure.invalidate();
            selectionChanged();
        }
    }

    @Override
	public void repairDamage() {
        if (fDamage != null) {
            repaint(fDamage.x, fDamage.y, fDamage.width, fDamage.height);
            fDamage = null;
        }
    }

    /**
     * Gets the currently selected figures.
     * @return a vector with the selected figures. The vector
     * is a copy of the current selection.
     */
    @SuppressWarnings("unchecked")
	// MIW: Changed to Generic Vector of Figure
    // MIW: Warning is cause run-time system can't check that clone is a Vector<Figure>
    @Override
	public Vector<Figure> selection() {
        // protect the vector with the current selection
    	return (Vector<Figure>) fSelection.clone();
    }

    /**
     * Informs that the current selection changed.
     * By default this event is forwarded to the
     * drawing editor.
     */
    protected void selectionChanged() {
        fEditor.selectionChanged(this);
    }

    /**
     * Gets the number of selected figures.
     */
    @Override
	public int selectionCount() {
        return fSelection.size();
    }

    /**
     * Gets an enumeration over the currently selected figures.
     */
    @Override
	public Enumeration<Figure> selectionElements() {
        return fSelection.elements();
    }

    /**
     * Gets an enumeration of the currently active handles.
     */
    private Enumeration<Handle> selectionHandles() {
        if (fSelectionHandles == null) {
            fSelectionHandles = new Vector<Handle>();
            Enumeration<Figure> k = selectionElements();
            while (k.hasMoreElements()) {
                Figure figure = k.nextElement();
                Enumeration<Handle> kk = figure.handles().elements();
                while (kk.hasMoreElements())
                    fSelectionHandles.addElement(kk.nextElement());
            }
        }
        return fSelectionHandles.elements();
    }

    /**
     * Gets the currently selected figures in Z order.
     * @see #selection
     * @return a vector with the selected figures. The vector
     * is a copy of the current selection.
     */
    // MIW: Updated Figure and Enumeration
    @Override
	public Vector<Figure> selectionZOrdered() {
        Vector<Figure> result = new Vector<Figure>(fSelection.size());
        Enumeration<Figure> figures = drawing().figures();

        while (figures.hasMoreElements()) {
            Figure f= figures.nextElement();
            if (fSelection.contains(f)) {
                result.addElement(f);
            }
        }
        return result;
    }

    /**
     * Sets the grid spacing that is used to constrain points.
     */
    @Override
	public void setConstrainer(PointConstrainer c) {
        fConstrainer = c;
    }

    /**
     * Sets the current display update strategy.
     * @see UpdateStrategy
     */
    @Override
	public void setDisplayUpdate(Painter updateStrategy) {
        fUpdateStrategy = updateStrategy;
    }

    /**
     * Sets and installs another drawing in the view.
     */
    @Override
	public void setDrawing(Drawing d) {
        clearSelection();

        if (fDrawing != null)
            fDrawing.removeDrawingChangeListener(this);

        fDrawing = d;
        if (fDrawing != null)
            fDrawing.addDrawingChangeListener(this);
        checkMinimumSize();
        repaint();
    }

    /**
     * Sets the view's editor.
     */
    @Override
	public void setEditor(DrawingEditor editor) {
        fEditor = editor;
    }
    /**
     * If a figure isn't selected it is added to the selection.
     * Otherwise it is removed from the selection.
     */
    @Override
	public void toggleSelection(Figure figure) {
        if (fSelection.contains(figure))
            removeFromSelection(figure);
        else
            addToSelection(figure);
        selectionChanged();
    }
    /**
     * Gets the current tool.
     */
    @Override
	public Tool tool() {
        return fEditor.tool();
    }
    /**
     * Unfreezes the view by releasing the drawing lock.
     * @see Drawing#unlock
     */
    @Override
	public void unfreezeView() {
        drawing().unlock();
    }
    /**
     * Updates the drawing view.
     */
    @Override
	public void update(Graphics g) {
    	// MIW: paint -> paintComponent
        paintComponent(g);
    }
}
