/*
 * @(#)Storable.java 5.1
 *
 */

package CH.ifa.draw.storable;

import java.io.IOException;

/**
 * Interface that is used by StorableInput and StorableOutput
 * to flatten and resurrect objects. Objects that implement
 * this interface and that are resurrected by StorableInput
 * have to provide a default constructor with no arguments.
 *
 * @see StorableInput
 * @see StorableOutput
 */
public interface Storable {
    /**
     * Reads the object from the StorableInput.
     */
    public void read(StorableInput dr) throws IOException;

    /**
     * Writes the object to the StorableOutput.
     */
    public void write(StorableOutput dw);
}
