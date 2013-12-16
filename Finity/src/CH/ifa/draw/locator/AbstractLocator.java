/*
 * @(#)AbstractLocator.java 5.1
 *
 */

package CH.ifa.draw.locator;

import java.io.IOException;

import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.storable.Storable;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * AbstractLocator provides default implementations for
 * the Locator interface.
 *
 * @see Locator
 * @see Handle
 */

public abstract class AbstractLocator
                implements Locator, Storable, Cloneable {

    /**
	 * MIW: Added to address Serialisable warning.
	 */
	private static final long serialVersionUID = -3020697056280734541L;

	protected AbstractLocator() {
    }

    @Override
	public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
	        throw new InternalError();
        }
    }

    /**
     * Reads the arrow tip from a StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
    }

    /**
     * Stores the arrow tip to a StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
    }
}


