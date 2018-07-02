package fr.florentclarret.bitfield;

import fr.florentclarret.bitfield.enums.WeekDay;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BitFieldTest {

    @Test
    public void testConstructorFromArray_WithNoValues() {
        final BitField<WeekDay> weekDaysBitField = new BitField<>();

        assertEquals(0, weekDaysBitField.getBitFieldValue());
        assertTrue(weekDaysBitField.getSet().isEmpty());
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

        return weekDays.entrySet().stream().map(day -> DynamicTest.dynamicTest("day[" + day.getKey() + "], " +
                "expectedBitFieldValue[" + day.getValue() + "]", () -> assertEquals(day.getValue().longValue(), new
                BitField<>(day.getKey()).getBitFieldValue())));
    }

    @Test
    public void testConstructorFromArray_WithSeveralValues() {
        Assert.assertEquals(36, new BitField<>(WeekDay.SATURDAY, WeekDay.WEDNESDAY).getBitFieldValue());
        Assert.assertEquals(34, new BitField<>(WeekDay.SATURDAY, WeekDay.TUESDAY).getBitFieldValue());
        Assert.assertEquals(127, new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY,
                WeekDay.FRIDAY, WeekDay.SATURDAY, WeekDay.SUNDAY).getBitFieldValue());
    }

    @TestFactory
    public Stream<DynamicTest> testConstructorFromSet() {
        final Map<Set<WeekDay>, Long> map = new HashMap<>();
        map.put(EnumSet.noneOf(WeekDay.class), 0L);
        map.put(EnumSet.of(WeekDay.MONDAY), 1L);
        map.put(EnumSet.of(WeekDay.TUESDAY), 2L);
        map.put(EnumSet.of(WeekDay.WEDNESDAY), 4L);
        map.put(EnumSet.of(WeekDay.THURSDAY), 8L);
        map.put(EnumSet.of(WeekDay.FRIDAY), 16L);
        map.put(EnumSet.of(WeekDay.SATURDAY), 32L);
        map.put(EnumSet.of(WeekDay.SUNDAY), 64L);
        map.put(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY), 17L);
        map.put(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY), 40L);
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY), 68L);
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY), 76L);
        map.put(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY), 42L);
        map.put(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY), 127L);

        return map.entrySet().stream().map(element -> DynamicTest.dynamicTest("days[" + element.getKey() + "], " +
                "expectedBitFieldValue[" + element.getValue() + "]", () -> assertEquals(element.getValue().longValue
                (), new BitField<>(element.getKey()).getBitFieldValue())));
    }

    @Test
    public void testConstructorFromSet_WithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Set<WeekDay> set = null;
            new BitField<>(set);
        });
    }

    @TestFactory
    public Stream<DynamicTest> testConstructorFromBitField() {
        final Map<Set<WeekDay>, Long> map = new HashMap<>();
        map.put(EnumSet.noneOf(WeekDay.class), 0L);
        map.put(EnumSet.of(WeekDay.MONDAY), 1L);
        map.put(EnumSet.of(WeekDay.TUESDAY), 2L);
        map.put(EnumSet.of(WeekDay.WEDNESDAY), 4L);
        map.put(EnumSet.of(WeekDay.THURSDAY), 8L);
        map.put(EnumSet.of(WeekDay.FRIDAY), 16L);
        map.put(EnumSet.of(WeekDay.SATURDAY), 32L);
        map.put(EnumSet.of(WeekDay.SUNDAY), 64L);
        map.put(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY), 17L);
        map.put(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY), 40L);
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY), 68L);
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY), 76L);
        map.put(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY), 42L);
        map.put(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY), 127L);

        return map.entrySet().stream().map(element -> DynamicTest.dynamicTest("bitFieldValue[" + element.getValue() +
                "], expectedSet[" + element.getKey() + "]", () -> {
            final BitField<WeekDay> result = new BitField<>(WeekDay.class, element.getValue());
            assertEquals(element.getKey(), result.getSet());
            assertEquals(element.getValue().longValue(), result.getBitFieldValue());
        }));
    }

    @Test
    public void testConstructorFromBitField_WithNullClass() {
        assertThrows(IllegalArgumentException.class, () -> new BitField<WeekDay>(null, 0));
    }

    @TestFactory
    public Stream<DynamicTest> testGetSet() {
        final List<Set<WeekDay>> list = new ArrayList<>();
        list.add(EnumSet.noneOf(WeekDay.class));
        list.add(EnumSet.of(WeekDay.MONDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY));
        list.add(EnumSet.of(WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY));

        return list.stream().map(element -> DynamicTest.dynamicTest("days[" + element + "]", () -> assertEquals
                (element, new BitField<>(element).getSet())));
    }

    @TestFactory
    public Stream<DynamicTest> testGetSet_ReturnCopy() {
        final List<Set<WeekDay>> list = new ArrayList<>();
        list.add(EnumSet.noneOf(WeekDay.class));
        list.add(EnumSet.of(WeekDay.MONDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY));
        list.add(EnumSet.of(WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY));

        return list.stream().map(element -> DynamicTest.dynamicTest("days[" + element + "]", () -> assertNotSame
                (element, new BitField<>(element).getSet())));
    }

    @Test
    public void testAddValue() {
        assertEquals(new BitField<>(WeekDay.MONDAY), new BitField<WeekDay>().addValue(WeekDay.MONDAY));
        assertEquals(new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY), new BitField<>(WeekDay.MONDAY).addValue(WeekDay
                .TUESDAY));
        assertEquals(new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.FRIDAY), new BitField<>(WeekDay.MONDAY,
                WeekDay.TUESDAY).addValue(WeekDay.FRIDAY));
    }

    @Test
    public void testAddValue_ExistingValue() {
        assertEquals(new BitField<>(WeekDay.MONDAY), new BitField<>(WeekDay.MONDAY).addValue(WeekDay.MONDAY));
        assertEquals(new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY), new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY)
                .addValue(WeekDay.TUESDAY));
    }

    @Test
    public void testAddValue_ReturnCopy() {
        final BitField<WeekDay> bitField = new BitField<>(WeekDay.THURSDAY);

        Assert.assertNotSame(bitField, bitField.addValue(WeekDay.MONDAY));
        Assert.assertNotSame(bitField, bitField.addValue(WeekDay.MONDAY, WeekDay.THURSDAY));
    }

    @Test
    public void testAddValue_WithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new BitField<WeekDay>().addValue(null));
    }

    @Test
    public void testAddValue_WithNullValueInExtraParameter() {
        assertEquals(new BitField<>(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.THURSDAY), new BitField<>(WeekDay
                .MONDAY).addValue(WeekDay.MONDAY, WeekDay.THURSDAY, null, WeekDay.TUESDAY));
    }

    @TestFactory
    public Stream<DynamicTest> testSetValue() {
        final List<Set<WeekDay>> list = new ArrayList<>();
        list.add(EnumSet.noneOf(WeekDay.class));
        list.add(EnumSet.of(WeekDay.MONDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY));
        list.add(EnumSet.of(WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY));

        return list.stream().map(element -> DynamicTest.dynamicTest("days[" + element + "]", () -> assertEquals
                (element, new BitField<>(WeekDay.SUNDAY).setValue(element).getSet())));
    }

    @TestFactory
    public Stream<DynamicTest> testSetValue_ReturnCopy() {
        final List<Set<WeekDay>> list = new ArrayList<>();
        list.add(EnumSet.noneOf(WeekDay.class));
        list.add(EnumSet.of(WeekDay.MONDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY));
        list.add(EnumSet.of(WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.FRIDAY));
        list.add(EnumSet.of(WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY));
        list.add(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY));
        list.add(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY));

        return list.stream().map(element -> DynamicTest.dynamicTest("days[" + element + "]", () -> assertNotSame
                (element, new BitField<>(WeekDay.SUNDAY).setValue(element))));
    }

    @Test
    public void testSetValue_WithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new BitField<WeekDay>().setValue(null));
    }

}