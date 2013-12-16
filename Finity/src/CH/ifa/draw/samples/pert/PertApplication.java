/*
 * @(#)PertApplication.java 5.1
 *
 */

package CH.ifa.draw.samples.pert;

import javax.swing.JPanel;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figure.LineFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;

public  class PertApplication extends DrawApplication {

	private static final long serialVersionUID = -7715041272799303145L;
	static private final String PERTIMAGES = "/CH/ifa/draw/samples/pert/images/";

    public static void main(String[] args) {
		PertApplication pert = new PertApplication();
		pert.open();
    }

    PertApplication() {
        super("PERT Editor");
    }

    //-- main -----------------------------------------------------------

	@Override
	protected void createTools(JPanel palette) {
        super.createTools(palette);

        Tool tool;
        tool = new TextTool(view(), new TextFigure());
        palette.add(createToolButton(IMAGES+"TEXT", "Text Tool", tool));

        // the generic but slower version. 
        // MIW - difficult to see any noticeable difference?
        // Uses Prototype DP / cloning
        tool = new CreationTool(view(), new PertFigure());
        palette.add(createToolButton(PERTIMAGES+"PERT", "Task Tool", tool));
        
        // MIW: Use Factory Method!
        tool = new PertFigureCreationTool(view());
        palette.add(createToolButton(PERTIMAGES+"PERT", "Task Tool", tool));

        tool = new ConnectionTool(view(), new PertDependency());
        palette.add(createToolButton(IMAGES+"CONN", "Dependency Tool", tool));

        tool = new CreationTool(view(), new LineFigure());
        palette.add(createToolButton(IMAGES+"Line", "Line Tool", tool));
    }
}

