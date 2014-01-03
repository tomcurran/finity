package org.tomcurran.finity.tool;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.util.FloatingTextField;
import CH.ifa.draw.util.TextHolder;

// based on texttool
public abstract class LabelTextTool extends CreationTool {

	private FloatingTextField fTextField;
	private TextHolder fTypingTarget;

	public LabelTextTool(DrawingView view) {
		super(view, new TextFigure());
	}

	/**
	 * Sets the text cursor.
	 */
	@Override
	public void activate() {
		super.activate();
		view().clearSelection();
		view().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
	}

	protected void beginEdit(TextHolder figure) {
		if (fTextField == null)
			fTextField = new FloatingTextField();

		if (figure != fTypingTarget && fTypingTarget != null)
			endEdit();

		fTextField.createOverlay((JPanel) view(), figure.getFont());
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
			processText(fTextField.getText());
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
	 * If the pressed figure is a TextHolder it can be edited otherwise a new
	 * text figure is created.
	 */
	@Override
	public void mouseDown(MouseEvent e, int x, int y) {
		if (isTargetFigure(drawing().findFigure(x, y))) {
			beginEdit(getTextHolder());
		} else if (fTypingTarget != null) {
			editor().toolDone();
			endEdit();
		}
	}

	public abstract boolean isTargetFigure(Figure pressedFigure);
	public abstract TextHolder getTextHolder();
	public abstract void processText(String text);

	@Override
	public void mouseDrag(MouseEvent e, int x, int y) {
	}

	@Override
	public void mouseUp(MouseEvent e, int x, int y) {
	}

}
