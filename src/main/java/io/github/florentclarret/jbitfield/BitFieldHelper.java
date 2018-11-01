package io.github.florentclarret.jbitfield;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Helper class to provide utility method for the BitField class
 */
public final class BitFieldHelper {

    private BitFieldHelper() {
        throw new IllegalArgumentException("no instance for you");
    }

    /**
     * Check that the given BitFieldElement class is valid. By valid, we means that all positions are unique.
     * If some positions are missing (for example the the positions are 0, 2 and 3), the enum is considered valid.
     * Negative values are also not considered valid.
     * @param enumClass The enumClass which extends BitFieldElement to check.
     * @param <T> The BitFieldElement class.
     * @return true if the enum is valid from the BitFieldp oint of view, false otherwise.
     * @throws NullPointerException if the enumClass is null
     */
    public static <T extends Enum<T> & BitFieldElement> boolean isValidEnum(final Class<T> enumClass) {
        final Set<T> enumSet = EnumSet.allOf(Objects.requireNonNull(enumClass, "enumClass must not be null)"));
        final Set<Integer> set = new HashSet<>(enumSet.size());

        for(final T element : enumSet) {
            final int position = element.getBitFieldPosition();
            if(!set.add(position) || position < 0) {
                return false;
            }
        }

        return true;
    }
}
