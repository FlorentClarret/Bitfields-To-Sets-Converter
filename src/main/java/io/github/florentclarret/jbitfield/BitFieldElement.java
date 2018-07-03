package io.github.florentclarret.jbitfield;

/**
 * Represents the super class for any enum that would be represented as a bit field. It's user's responsibility to
 * provide unique index for each element. Warning : It's not recommended to rely on ordinal() to specify the positions
 * (not even recommended at all). If the order changes in your enum, the results will be different. Instead, set a
 * attribute with the value.
 *
 * @author Florent Clarret
 */
public interface BitFieldElement {
    /**
     * Return the index of the current element in the bit field.
     *
     * @return A integer positive or null.
     */
    int getBitFieldPosition();
}
