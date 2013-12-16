/*
 * @(#)TextTool.java 5.1
 *
 */

package CH.ifa.draw.tool;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.FloatingTextField;
import CH.ifa.draw.util.TextHolder;

/**
 * Tool to create new or edit existing text figures.
 * The editing behavior is implemented by overlaying the
 * Figure providing the text with a FloatingTextField.<p>
 * A tool interaction is done once a Figure that is not
 * a TextHolder is clicked.
 *
 * @see TextHolder
 * @see FloatingTextField
 */
public class TextTool extends CreationTool {

    private FloatingTextField   fTextField;
    private TextHolder  fTypingTarget;

    public TextTool(DrawingView view, Figure prototype) {
        super(view, prototype);
    }

    /**
     * Sets the text cursor.
     */
    @Override
	public void activate() {
        super.activate();
        view().clearSelection();
        // JDK1.1 TEXT_CURSOR has an incorrect hot spot
        // MIW: uncommented this:
        view().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    protected void beginEdit(TextHolder figure) {
        if (fTextField == null)
            fTextField = new FloatingTextField();

	    if (figure != fTypingTarget && fTypingTarget != null)
	        endEdit();

        fTextField.createOverlay((JPanel)view(), figure.getFont());
	    fTextField.setBounds(fieldBounds(figure), figure.getText());
	    fTypingTarget = figure;
    }

    /**
     * Terminates the editing of a text figure.
     */
    @Override
	public void deactivate() {
        super.deactivate();
        endEdit();
    }

    protected void endEdit() {
	    if (fTypingTarget != null) {
	        if (fTextField.getText().length() > 0)
	            fTypingTarget.setText(fTextField.getText());
	        else
	            drawing().remove((Figure)fTypingTarget);
	        fTypingTarget = null;
	        fTextField.endOverlay();
	        view().checkDamage();
	    }
    }

    private Rectangle fieldBounds(TextHolder figure) {
    	Rectangle box = figure.textDisplayBox();
    	int nChars = figure.overlayColumns();
        Dimension d = fTextField.getPreferredSize(nChars);
        return new Rectangle(box.x, box.y, d.width, d.height);
    }

    /**
     * If the pressed figure is a TextHolder it can be edited otherwise
     * a new text figure is created.
     */
    @Override
	public void mouseDown(MouseEvent e, int x, int y)
    {
	    Figure pressedFigure;
	    TextHolder textHolder = null;

	    pressedFigure = drawing().findFigureInside(x, y);
	    if (pressedFigure instanceof TextHolder) {
	        textHolder = (TextHolder) pressedFigure;
	        if (!textHolder.acceptsTyping())
	            textHolder = null;
        }
	    if (textHolder != null) {
	        beginEdit(textHolder);
	        return;
	    }
	    if (fTypingTarget != null) {
	        editor().toolDone();
	        endEdit();
	    } else {
    	    super.mouseDown(e, x, y);
    	    textHolder = (TextHolder)createdFigure();
    	    beginEdit(textHolder);
        }
    }

    @Override
	public void mouseDrag(MouseEvent e, int x, int y) {
    }

    @Override
	public void mouseUp(MouseEvent e, int x, int y) {
    }
}

