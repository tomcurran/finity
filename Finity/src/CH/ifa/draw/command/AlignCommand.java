/*
 * @(#)AlignCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.awt.Rectangle;
import java.util.Enumeration;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * Align a selection of figures relative to each other.
 */
public class AlignCommand extends Command {
	
//  /**
//  * align left sides
//  
//  public final static int LEFTS = 0;
// /**
//  * align centers (horizontally)
//	 */
//  public final static int CENTERS = 1;
// /**
//  * align right sides
//  */
//  public final static int RIGHTS = 2;
// /**
//  * align tops
//  */
//  public final static int TOPS = 3;
// /**
//  * align middles (vertically)
//  */
//  public final static int MIDDLES = 4;
// /**
//  * align bottoms
//  */
//  public final static int BOTTOMS = 5;
//  
//  MIW Replaced above by Enumeration
	public enum Alignment {LEFTS, CENTERS, RIGHTS, TOPS, MIDDLES, BOTTOMS}; 
	
    private DrawingView fView;
    private Alignment fOp;

   /**
    * Constructs an alignment command.
    * @param name the command name
    * @param view the target view
    * @param op the alignment operation (LEFTS, CENTERS, RIGHTS, etc.)
    */
    public AlignCommand(String name, DrawingView view, Alignment op) {
        super(name);
        fView = view;
        fOp = op;
    }

    @Override
	public void execute() {
        Enumeration<Figure> selection = fView.selectionElements();
        Figure anchorFigure = selection.nextElement();
        Rectangle r = anchorFigure.displayBox();  
        
        while (selection.hasMoreElements()) {
            Figure f = selection.nextElement();
            Rectangle rr = f.displayBox();
            switch (fOp) {
            case LEFTS:
                f.moveBy(r.x-rr.x, 0);
                break;
            case CENTERS:
                f.moveBy((r.x+r.width/2) - (rr.x+rr.width/2), 0);
                break;
            case RIGHTS:
                f.moveBy((r.x+r.width) - (rr.x+rr.width), 0);
                break;
            case TOPS:
                f.moveBy(0, r.y-rr.y);
                break;
            case MIDDLES:
                f.moveBy(0, (r.y+r.height/2) - (rr.y+rr.height/2));
                break;
            case BOTTOMS:
                f.moveBy(0, (r.y+r.height) - (rr.y+rr.height));
                break;
            }
        }
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 1;
    }
}


