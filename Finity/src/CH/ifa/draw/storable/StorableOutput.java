/*
 * @(#)StorableOutput.java 5.1
 *
 */

package CH.ifa.draw.storable;

import java.awt.Color;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * An output stream that can be used to flatten Storable objects.
 * StorableOutput preserves the object identity of the stored objects.
 *
 * @see Storable
 * @see StorableInput
 */


public  class StorableOutput extends Object {

    private PrintWriter     fStream;
    // MIW: Added generic parameter
    private Vector<Storable>          fMap;
    private int             fIndent;

    /**
     * Initializes the StorableOutput with the given output stream.
     */
    public StorableOutput(OutputStream stream) {
        fStream = new PrintWriter(stream);
        fMap = new Vector<Storable>();
        fIndent = 0;
    }

    /**
     * Closes a storable output stream.
     */
    public void close() {
        fStream.close();
    }

    private void decrementIndent() {
        fIndent -= 4;
        if (fIndent < 0) fIndent = 0;
    }

    private void incrementIndent() {
        fIndent += 4;
    }

    private void map(Storable storable) {
        if (!fMap.contains(storable))
            fMap.addElement(storable);
    }

    private boolean mapped(Storable storable) {
        return fMap.contains(storable);
    }

    private void space() {
        fStream.print(' ');
    }

    private void startNewLine() {
        fStream.println();
        for (int i=0; i<fIndent; i++)
            space();
    }

    /**
     * Writes an int to the output stream.
     */
    public void writeBoolean(boolean b) {
        if (b)
            fStream.print(1);
        else
            fStream.print(0);
        space();
    }

    public void writeColor(Color c) {
        writeInt(c.getRed());
        writeInt(c.getGreen());
        writeInt(c.getBlue());
    }

    /**
     * Writes an int to the output stream.
     */
    public void writeDouble(double d) {
        fStream.print(d);
        space();
    }

    /**
     * Writes an int to the output stream.
     */
    public void writeInt(int i) {
        fStream.print(i);
        space();
    }

    private void writeRef(Storable storable) {
        int ref = fMap.indexOf(storable);

        fStream.print("REF");
        space();
        fStream.print(ref);
        space();
    }

    /**
     * Writes a storable object to the output stream.
     */
    public void writeStorable(Storable storable) {
        if (storable == null) {
            fStream.print("NULL");
            space();
            return;
        }

        if (mapped(storable)) {
            writeRef(storable);
            return;
        }

        incrementIndent();
        startNewLine();
        map(storable);
        fStream.print(storable.getClass().getName());
        space();
        storable.write(this);
        space();
        decrementIndent();
    }

    /**
     * Writes a string to the output stream. Special characters
     * are quoted.
     */
    public void writeString(String s) {
        fStream.print('"');
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\n': fStream.print('\\'); fStream.print('n'); break;
                case '"' : fStream.print('\\'); fStream.print('"'); break;
                case '\\': fStream.print('\\'); fStream.print('\\'); break;
                case '\t': fStream.print('\\'); fStream.print('\t'); break;
                default: fStream.print(c);
            }

        }
        fStream.print('"');
        space();
    }

}
