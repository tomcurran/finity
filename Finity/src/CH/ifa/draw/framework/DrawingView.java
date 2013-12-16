/*
 * @(#)DrawingView.java 5.1
 *
 */

package CH.ifa.draw.framework;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.util.FigureSelection;

/**
 * DrawingView renders a Drawing and listens to its changes.
 * It receives user input and delegates it to the current tool.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld026.htm>Observer</a></b><br>
 * DrawingView observes drawing for changes via the DrawingListener interface.<br>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld032.htm>State</a></b><br>
 * DrawingView plays the role of the StateContext in
 * the State pattern. Tool is the State.<br>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld034.htm>Strategy</a></b><br>
 * DrawingView is the StrategyContext in the Strategy pattern
 * with regard to the UpdateStrategy. <br>
 * DrawingView is the StrategyContext for the PointConstrainer.
 *
 * @see Drawing
 * @see Painter
 * @see Tool
 */


public interface DrawingView extends ImageObserver, DrawingChangeListener {

    /**
     * Adds a figure to the drawing.
     * @return the added figure.
     */
    public Figure add(Figure figure);

    /**
     * Adds a vector of figures to the drawing.
     */
    
    // MIW: Added Figure parameter to Vector
    public void addAll(Vector<Figure> figures);

    /**
     * Adds a figure to the current selection.
     */
    public void addToSelection(Figure figure);

    /**
     * Adds a vector of figures to the current selection.
     */
    // MIW: Added Figure to Vector
    public void addToSelectionAll(Vector<Figure> figures);

    /**
     * Checks whether the drawing has some accumulated damage
     */
    public void checkDamage();

    /**
     * Clears the current selection.
     */
    public void clearSelection();

    /**
     * Creates an image with the given dimensions
     */
    public Image createImage(int width, int height);

    /**
     * Draws the contents of the drawing view.
     * The view has three layers: background, drawing, handles.
     * The layers are drawn in back to front order.
     */
    public void drawAll(Graphics g);

    /**
     * Draws the background. If a background pattern is set it
     * is used to fill the background. Otherwise the background
     * is filled in the background color.
     */
    public void drawBackground(Graphics g);

    /**
     * Draws the drawing.
     */
    public void drawDrawing(Graphics g);

    /**
     * Draws the currently active handles.
     */
    public void drawHandles(Graphics g);

    /**
     * Gets the drawing.
     */
    public Drawing drawing();

    /**
     * Gets the editor.
     */
    public DrawingEditor editor();

    /**
     * Finds a handle at the given coordinates.
     * @return the hit handle, null if no handle is found.
     */
    public Handle findHandle(int x, int y);

    /**
     * Freezes the view by acquiring the drawing lock.
     * @see Drawing#lock
     */
    public void freezeView();

    /**
     * Gets the background color of the DrawingView
     */
    public Color getBackground();

    /**
     * Gets the current grid setting.
     */
    public PointConstrainer getConstrainer();

    /**
     * Gets the current selection as a FigureSelection. A FigureSelection
     * can be cut, copied, pasted.
     */
    public FigureSelection getFigureSelection();

    /**
     * Gets a graphic to draw into
     */
    public Graphics getGraphics();

    /**
     * Gets the minimum dimension of the drawing.
     */
    public Dimension getMinimumSize();

    /**
     * Gets the preferred dimension of the drawing..
     */
    public Dimension getPreferredSize();

    /**
     * Gets the size of the drawing.
     */
    public Dimension getSize();

    /**
     * Gets the position of the last click inside the view.
     */
    public Point lastClick();

    /**
     * Paints the drawing view. The actual drawing is delegated to
     * the current update strategy.
     * @see Painter
     */
    // public void paint(Graphics g); MIW repalced by paintComponent() - SWING
    
    public void paintComponent(Graphics g);

    /**
     * Removes a figure from the drawing.
     * @return the removed figure
     */
    public Figure remove(Figure figure);

    /**
     * Removes a figure from the selection.
     */
    public void removeFromSelection(Figure figure);

    /**
     * Repair the damaged area
     */
    public void repairDamage();

    /**
     * Gets the currently selected figures.
     * @return a vector with the selected figures. The vector
     * is a copy of the current selection.
     */
    
    // MIW: Added Figure to Vector
    public Vector<Figure> selection();

    /**
     * Gets the number of selected figures.
     */
    public int selectionCount();

    /**
     * Gets an enumeration over the currently selected figures.
     */
    public Enumeration<Figure> selectionElements();

    /**
     * Gets the currently selected figures in Z order.
     * @see #selection
     * @return a vector with the selected figures. The vector
     * is a copy of the current selection.
     */
    // MIW: Added Figure To Vector
    public Vector<Figure> selectionZOrdered();

    /**
     * Gets the background color of the DrawingView
     */
    public void setBackground(Color c);

    /**
     * Sets the current point constrainer.
     */
    public void setConstrainer(PointConstrainer p);

    /**
     * Sets the cursor of the DrawingView
     */
    public void setCursor(Cursor c);

    /**
     * Sets the current display update strategy.
     * @see UpdateStrategy
     */
    public void setDisplayUpdate(Painter updateStrategy);

    /**
     * Sets and installs another drawing in the view.
     */
    public void setDrawing(Drawing d);

    /**
     * Sets the view's editor.
     */
    public void setEditor(DrawingEditor editor);

    /**
     * If a figure isn't selected it is added to the selection.
     * Otherwise it is removed from the selection.
     */
    public void toggleSelection(Figure figure);

    /**
     * Gets the current tool.
     */
    public Tool tool();

    /**
     * Unfreezes the view by releasing the drawing lock.
     * @see Drawing#unlock
     */
    public void unfreezeView();
}
