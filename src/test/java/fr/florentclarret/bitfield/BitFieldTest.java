package fr.florentclarret.bitfield;

import fr.florentclarret.bitfield.enums.WeekDay;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BitFieldTest {

    @Test
    public void testConstructorFromArray_WithNoValues() {
        final BitField<WeekDay> weekDaysBitField = new BitField<>();

        assertEquals(0, weekDaysBitField.getBitFieldValue());
        assertTrue(weekDaysBitField.getSet().isEmpty());
    }

    @Test
    public void testConstructorFromSet_WithNullInput() {
        assertThrows(IllegalArgumentException.class,
            () -> {
                final Set<WeekDay> set = null;
                new BitField<>(set);
            });
    }

    @TestFactory
    public Stream<DynamicTest> testConstructorFromArray_WithOneValue() {
        final Map<WeekDay, Long> weekDays = new HashMap<>();
        weekDays.put(WeekDay.MONDAY, 1L);
        weekDays.put(WeekDay.TUESDAY, 2L);
        weekDays.put(WeekDay.WEDNESDAY, 4L);
        weekDays.put(WeekDay.THURSDAY, 8L);
        weekDays.put(WeekDay.FRIDAY, 16L);
        weekDays.put(WeekDay.SATURDAY, 32L);
        weekDays.put(WeekDay.SUNDAY, 64L);

        return weekDays.entrySet().stream().map(day -> DynamicTest.dynamicTest(
                "day[" + day.getKey() + "], expectedBitFieldValue[" + day.getValue() + "]",
                () -> assertEquals(day.getValue().longValue(),
                        new BitField<>(day.getKey()).getBitFieldValue())));
    }
}