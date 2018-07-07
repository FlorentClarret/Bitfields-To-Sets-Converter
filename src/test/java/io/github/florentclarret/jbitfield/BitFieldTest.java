package io.github.florentclarret.jbitfield;

import io.github.florentclarret.jbitfield.enums.WeekDay;
import io.github.florentclarret.jbitfield.BitField;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
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
    public void testDefaultConstructor() {
        final BitField<WeekDay> weekDaysBitField = new BitField<>();

        assertEquals(0, weekDaysBitField.getBitFieldValue());
        assertTrue(weekDaysBitField.getSet().isEmpty());
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
        assertEquals(new BitField<>(Collections.singleton(WeekDay.MONDAY)), new BitField<WeekDay>().addValue
                (Collections.singleton(WeekDay.MONDAY)));
        assertEquals(new BitField<>(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new BitField<>
                (Collections.singleton(WeekDay.MONDAY)).addValue(Collections.singleton(WeekDay.TUESDAY)));
        assertEquals(new BitField<>(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.FRIDAY))), new
                BitField<>(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))).addValue(Collections
                .singleton(WeekDay.FRIDAY)));
    }

    @Test
    public void testAddValue_ExistingValue() {
        assertEquals(new BitField<>(Collections.singleton(WeekDay.MONDAY)), new BitField<>(Collections.singleton
                (WeekDay.MONDAY)).addValue(Collections.singleton(WeekDay.MONDAY)));
        assertEquals(new BitField<>(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new BitField<>(new
                HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))).addValue(Collections.singleton(WeekDay
                .TUESDAY)));
    }

    @Test
    public void testAddValue_ReturnCopy() {
        final BitField<WeekDay> bitField = new BitField<>(Collections.singleton(WeekDay.THURSDAY));

        Assert.assertNotSame(bitField, bitField.addValue(Collections.singleton(WeekDay.MONDAY)));
        Assert.assertNotSame(bitField, bitField.addValue(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.THURSDAY))));
    }

    @Test
    public void testAddValue_WithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new BitField<WeekDay>().addValue(null));
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
                (element, new BitField<>(Collections.singleton(WeekDay.SUNDAY)).setValue(element).getSet())));
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
                (element, new BitField<>(Collections.singleton(WeekDay.SUNDAY)).setValue(element))));
    }

    @Test
    public void testSetValue_WithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new BitField<WeekDay>().setValue(null));
    }

}