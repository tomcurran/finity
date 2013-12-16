/*
 * @(#)JavaDrawViewer.java 5.1
 *
 */

package CH.ifa.draw.samples.javadraw;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.StandardDrawing;
import CH.ifa.draw.standard.StandardDrawingView;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.util.Iconkit;


public  class JavaDrawViewer extends Applet implements DrawingEditor {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8306672579023284007L;
	private Drawing         fDrawing;
    private Tool            fTool;
    private StandardDrawingView fView;
    @SuppressWarnings("unused")
	private Iconkit         fIconkit;

    /**
     * Gets the editor's drawing.
     */
    @Override
	public Drawing drawing() {
        return fDrawing;
    }

    @Override
	public void init() {
		setLayout(new BorderLayout());
	    fView = new StandardDrawingView(this, 400, 370);
        add("Center", fView);
        fTool = new FollowURLTool(view(), this);

        fIconkit = new Iconkit(this);

        String filename = getParameter("Drawing");
        if (filename != null) {
		    loadDrawing(filename);
            fView.setDrawing(fDrawing);
		} else
		    showStatus("Unable to load drawing");
    }

    private void loadDrawing(String filename) {
        try {
            URL url = new URL(getCodeBase(), filename);
            InputStream stream = url.openStream();
            StorableInput reader = new StorableInput(stream);
            fDrawing = (Drawing)reader.readStorable();
        } catch (IOException e) {
            fDrawing = new StandardDrawing();
            System.out.println("Error when Loading: " + e);
            showStatus("Error when Loading: " + e);
        }
    }

    /**
     * Ignore selection changes, we don't show any selection
     */
    @Override
	public void selectionChanged(DrawingView view) {}

    /**
     * Gets the current the tool (there is only one):
     */
    @Override
	public Tool tool() {
        return fTool;
    }

    /**
     * Sets the editor's default tool. Do nothing since we only have one tool.
     */
    @Override
	public void toolDone() {}

    /**
     * Gets the editor's drawing view.
     */
    @Override
	public DrawingView view() {
        return fView;
    }
}

