package io.github.florentclarret.jbitfield;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a immutable bit field of BitFieldElement enum elements. This class aims to ease conversion between EnumSet and bit field
 * for database storage for instance. However, if not needed, never prefer using a bit field over an EnumSet. See Item
 * 36, Effective Java (Third Edition) by Joshua Bloch for further information. Warning : the representation of the bit
 * field is stored in a long primitive type. It means that you can not have more than 64 different values in this
 * implementation
 *
 * @param <T> The enum which represents the value in the bit field.
 * @author Florent Clarret
 */
public final class BitField<T extends Enum<T> & BitFieldElement> implements Iterable<T> {

    /**
     * Binary representation of the bit field.
     */
    private final long bitField;

    /**
     * Set representation of the bit field.
     */
    private final Set<T> set;

    /**
     * The class represented in the bit field
     */
    private final Class<T> enumClass;

    /**
     * The cached hash code
     */
    private int hashCode;

    /**
     * Creates a bit field initialized to zero for the given class.
     * @param enumClass The type of the elements stored in the bit field
     * @throws NullPointerException if the enumClass is null
     * @throws IllegalArgumentException if the enumClass is not a valid BtFieldElement
     */
    public BitField(final Class<T> enumClass) {
        this(enumClass, EnumSet.noneOf(Objects.requireNonNull(enumClass, "enumClass can not be null")));
    }

    /**
     * Generates a new instance of the bit field using the given set of BitFieldElement. Duplicated values are used only
     * once and automatically removed.
     *
     * @param set The set containing the element to place in the bit field.
     * @param enumClass The type of the elements stored in the bit field
     * @throws NullPointerException if the set is null
     * @throws NullPointerException if the enumClass is null
     * @throws IllegalArgumentException if the enumClass is not a valid BtFieldElement
     */
    public BitField(final Class<T> enumClass, final Set<T> set) {
        Objects.requireNonNull(set, "set can not be null");
        Objects.requireNonNull(enumClass, "enumClass can not be null");

        if (!BitFieldHelper.isValidEnum(enumClass)) {
            throw new IllegalArgumentException(String.format("the class [%s] is not a valid " +
                    "BitFieldElement", enumClass.getName()));
        }

        // Not using guava immutable set (instead of unmodifiable set) to avoid relying on external libraries
        this.set = (set.isEmpty()) ? Collections.unmodifiableSet(EnumSet.noneOf(enumClass)) : Collections.unmodifiableSet(EnumSet.copyOf(set));
        this.bitField = this.set.stream().mapToLong(element -> (1 << element.getBitFieldPosition())).sum();
        this.enumClass = enumClass;
    }

    /**
     * Generates an instance of BitField from the binary bit field value.
     *
     * @param enumClass the Class represented in the bit field
     * @param bitField  The binary representation of the bit field.
     * @throws IllegalArgumentException if any value in the field are not present in the BitFieldElement enum position's or if the
     *                                  enumClass is null.
     * @throws NullPointerException if the enumClass is null
     * @throws IllegalArgumentException if the enumClass is not a valid BtFieldElement
     */
    public BitField(final Class<T> enumClass, final long bitField) {
        Objects.requireNonNull(enumClass, "enumClass can not be null");

        if (!BitFieldHelper.isValidEnum(enumClass)) {
            throw new IllegalArgumentException(String.format("the class [%s] is not a valid " +
                    "BitFieldElement", enumClass.getName()));
        }

        long localBitField = bitField;
        this.set = EnumSet.noneOf(enumClass);
        this.bitField = localBitField;
        this.enumClass = enumClass;

        if (localBitField != 0) {
            for (final T element : EnumSet.allOf(enumClass)) {
                if (((1 << element.getBitFieldPosition()) & localBitField) != 0) {
                    this.set.add(element);
                    // Remove the current bit to ease final comparison
                    localBitField = localBitField ^ (1 << element.getBitFieldPosition());
                }
            }

            // If the local bit field is not 0, it means that we have missing position values in the <T> enum
            if (localBitField != 0) {
                throw new IllegalArgumentException(String.format("Invalid value found in bit field [%s] for enum [%s]", bitField, enumClass.getName()));
            }
        }
    }

    /**
     * Return a new instance of a bit field using the given value. Duplicated values are used only once and
     * automatically removed.
     *
     * @param set The set to define in the bit field.
     * @return The newly generated bit field.
     * @throws NullPointerException if the set is null
     */
    public BitField<T> set(final Set<T> set) {
        return new BitField<>(enumClass, set);
    }

    /**
     * Create a new instance of the current bit field with the given extra values. If the element is already in the bit
     * field, nothing will be done. Null values from the elements parameter are ignored.
     *
     * @param elements The elements to add to the current bit field
     * @return The newly generated bit field with the extra values.
     * @throws NullPointerException if elements is null
     */
    public BitField<T> addAll(final Set<T> elements) {
        Objects.requireNonNull(elements, "elements can not be null");

        if(elements.isEmpty()) {
            return this;
        }

        final Set<T> copy = set.isEmpty() ? EnumSet.noneOf(enumClass) : EnumSet.copyOf(set);

        if (!elements.isEmpty()) {
            copy.addAll(elements);
        }

        return new BitField<>(enumClass, copy);
    }

    /**
     * Create a new instance of the current bit field with the given extra value. If the element is already in the bit
     * field, nothing will be done.
     *
     * @param element The element to add to the current bit field
     * @return The newly generated bit field with the extra value.
     * @throws NullPointerException if element is null
     */
    public BitField<T> add(final T element) {
        return this.set.contains(Objects.requireNonNull(element, "element can not be null")) ? this : this.addAll(EnumSet.of(element));
    }

    /**
     * Return the internal value of the bit field.
     *
     * @return A long representing the current bit field.
     */
    public long getBitFieldValue() {
        return bitField;
    }

    /**
     * Return the Set containing all the value from the current bit field.
     *
     * @return A unmodifiable set of the value stored in the current bit field.
     */
    public Set<T> getSet() {
        return set;
    }

    @Override
    public Iterator<T> iterator() {
        return this.set.iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BitField<?> bitField1 = (BitField<?>) o;

        return bitField == bitField1.bitField;
    }

    @Override
    public int hashCode() {
        return (hashCode == 0) ? hashCode = ((int) (bitField ^ (bitField >>> 32))) : hashCode;
    }

    @Override
    public String toString() {
        return "BitField{" + "bitField=" + bitField + ", set=" + set + '}';
    }
}
