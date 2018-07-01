package fr.florentclarret.bitfield;

/**
 * Represents the super class for any class that can be represented as a bit field.
 * It's user's responsibility to provide unique index for each element.
 * @author Florent Clarret
 */
public interface BitFieldElement {
    /**
     * Return the index of the current element in the bit field.
     * @return A integer positive or null.
     */
    int getBitFieldPosition();
}
