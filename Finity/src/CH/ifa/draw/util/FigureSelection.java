/*
 * @(#)FigureSelection.java 5.1
 *
 */

package CH.ifa.draw.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * FigureSelection enables transfer of selected figures
 * to a clipboard.<p>
 * Will soon be converted to the JDK 1.1 Transferable interface.
 *
 * @see Clipboard
 */

public class FigureSelection extends Object {

    private byte[] fData; // Flattened figures, ready to be resurrected
    /**
     * The type identifier of the selection.
     */
    public final static String TYPE = "CH.ifa.draw.Figures";

    /**
     * Constructs the Figure selection for the vector of figures.
     */
    public FigureSelection(Vector<Figure> figures) {
        // a FigureSelection is represented as a flattened ByteStream
        // of figures.
        ByteArrayOutputStream output = new ByteArrayOutputStream(200);
        StorableOutput writer = new StorableOutput(output);
        writer.writeInt(figures.size());
        
        // MIW: added Figure to enumeration and removed cast
        Enumeration<Figure> selected = figures.elements();
        while (selected.hasMoreElements()) {
            Figure figure = selected.nextElement();
            writer.writeStorable(figure);
        }
        writer.close();
        fData = output.toByteArray();
    }

    /**
     * Gets the data of the selection. The result is returned
     * as a Vector of Figures.
     *
     * @return a copy of the figure selection.
     */
    public Object getData(String type) {
        if (type.equals(TYPE)) {
            InputStream input = new ByteArrayInputStream(fData);
            // MIW: Added Figure to Vector
            Vector<Figure> result = new Vector<Figure>(10);
            StorableInput reader = new StorableInput(input);
            int numRead = 0;
            try {
                int count = reader.readInt();
                while (numRead < count) {
                    Figure newFigure = (Figure) reader.readStorable();
                    result.addElement(newFigure);
                    numRead++;
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            return result;
        }
        return null;
    }

    /**
     * Gets the type of the selection.
     */
    public String getType() {
        return TYPE;
    }
}

