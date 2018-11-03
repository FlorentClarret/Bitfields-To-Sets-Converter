package io.github.florentclarret.jbitfield;

import io.github.florentclarret.jbitfield.enums.DuplicatedEnum;
import io.github.florentclarret.jbitfield.enums.NegativeEnum;
import io.github.florentclarret.jbitfield.enums.WeekDay;
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
import java.util.Iterator;
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
        final BitField<WeekDay> weekDaysBitField = new BitField<>(WeekDay.class);

        assertEquals(0, weekDaysBitField.getBitFieldValue());
        assertTrue(weekDaysBitField.getSet().isEmpty());
    }

    @Test
    public void testDefaultConstructorWithNullInput() {
        Assert.assertEquals("enumClass can not be null", assertThrows(NullPointerException.class, () -> new BitField<WeekDay>(null)).getMessage());
    }

    @Test
    public void testDefaultConstructorWithInvalidEnum() {
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.NegativeEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(NegativeEnum.class)).getMessage());
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.DuplicatedEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(DuplicatedEnum.class)).getMessage());
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
                (), new BitField<>(WeekDay.class, element.getKey()).getBitFieldValue())));
    }

    @Test
    public void testConstructorFromSetWithNullInput() {
        Assert.assertEquals("set can not be null", assertThrows(NullPointerException.class, () -> new BitField<>(null, null)).getMessage());
        Assert.assertEquals("set can not be null",assertThrows(NullPointerException.class, () -> new BitField<>(WeekDay.class, null)).getMessage());
        Assert.assertEquals("enumClass can not be null", assertThrows(NullPointerException.class, () -> {
            final Class<WeekDay> clazz = null;
            new BitField<>(clazz, Collections.emptySet());
        }).getMessage());
    }

    @Test
    public void testConstructorFromSetWithInvalidEnum() {
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.NegativeEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(NegativeEnum.class, Collections.emptySet())).getMessage());
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.DuplicatedEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(DuplicatedEnum.class, Collections.emptySet())).getMessage());
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
    public void testConstructorFromBitFieldWithNullClass() {
        assertEquals("enumClass can not be null", assertThrows(NullPointerException.class, () -> new BitField<WeekDay>(null, 0)).getMessage());
    }

    @Test
    public void testConstructorFromBitFieldWithInvalidEnum() {
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.DuplicatedEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(DuplicatedEnum.class, 0)).getMessage());
        assertEquals("the class [io.github.florentclarret.jbitfield.enums.NegativeEnum] is not a valid BitFieldElement", assertThrows(IllegalArgumentException.class, () -> new BitField<>(NegativeEnum.class, 0)).getMessage());
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
                (element, new BitField<>(WeekDay.class, element).getSet())));
    }

    @TestFactory
    public Stream<DynamicTest> testGetSetReturnCopy() {
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
                (element, new BitField<>(WeekDay.class, element).getSet())));
    }

    @Test
    public void testAddAllValue() {
        assertEquals(new BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)), new BitField<>(WeekDay
                .class).addAll(Collections.singleton(WeekDay.MONDAY)));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new
                BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)).addAll(Collections.singleton
                (WeekDay.TUESDAY)));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay
                .FRIDAY))), new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))
        ).addAll(Collections.singleton(WeekDay.FRIDAY)));
    }

    @Test
    public void testAddAllValueExistingValue() {
        assertEquals(new BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)), new BitField<>(WeekDay
                .class, Collections.singleton(WeekDay.MONDAY)).addAll(Collections.singleton(WeekDay.MONDAY)));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new
                BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))).addAll
                (Collections.singleton(WeekDay.TUESDAY)));
    }

    @Test
    public void testAddAllValueReturnCopy() {
        final BitField<WeekDay> bitField = new BitField<>(WeekDay.class, Collections.singleton(WeekDay.THURSDAY));

        Assert.assertNotSame(bitField, bitField.addAll(Collections.singleton(WeekDay.MONDAY)));
        Assert.assertNotSame(bitField, bitField.addAll(new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.THURSDAY))));
    }

    @Test
    public void testAddValue() {
        assertEquals(new BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)), new BitField<>(WeekDay
                .class).add(WeekDay.MONDAY));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new
                BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)).add(WeekDay.TUESDAY));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay
                .FRIDAY))), new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))
        ).add(WeekDay.FRIDAY));
    }

    @Test
    public void testAddValueExistingValue() {
        assertEquals(new BitField<>(WeekDay.class, Collections.singleton(WeekDay.MONDAY)), new BitField<>(WeekDay
                .class, Collections.singleton(WeekDay.MONDAY)).add(WeekDay.MONDAY));
        assertEquals(new BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))), new
                BitField<>(WeekDay.class, new HashSet(Arrays.asList(WeekDay.MONDAY, WeekDay.TUESDAY))).add(WeekDay.TUESDAY));
    }

    @Test
    public void testAddValueReturnCopy() {
        final BitField<WeekDay> bitField = new BitField<>(WeekDay.class, Collections.singleton(WeekDay.THURSDAY));

        Assert.assertNotSame(bitField, bitField.add(WeekDay.MONDAY));
    }

    @Test
    public void testAddValueWithNullInput() {
        final Set<WeekDay> set = null;
        assertEquals("elements can not be null", assertThrows(NullPointerException.class, () -> new BitField<>(WeekDay.class).addAll(set)).getMessage());
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
                (element, new BitField<>(WeekDay.class, Collections.singleton(WeekDay.SUNDAY)).set(element).getSet())));
    }

    @TestFactory
    public Stream<DynamicTest> testSetValueReturnCopy() {
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
                (element, new BitField<>(WeekDay.class, Collections.singleton(WeekDay.SUNDAY)).set(element))));
    }

    @Test
    public void testSetValueWithNullInput() {
        assertEquals("set can not be null", assertThrows(NullPointerException.class, () -> new BitField<>(WeekDay.class).set(null)).getMessage());
    }

    @TestFactory
    public Stream<DynamicTest> testIterator() {
        final Map<Set<WeekDay>, BitField<WeekDay>> map = new HashMap<>();
        map.put(EnumSet.noneOf(WeekDay.class), new BitField<>(WeekDay.class, EnumSet.noneOf(WeekDay.class)));
        map.put(EnumSet.of(WeekDay.MONDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.MONDAY)));
        map.put(EnumSet.of(WeekDay.TUESDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.TUESDAY)));
        map.put(EnumSet.of(WeekDay.WEDNESDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.WEDNESDAY)));
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY)));
        map.put(EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.SUNDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY)));
        map.put(EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.TUESDAY, WeekDay.SATURDAY, WeekDay.THURSDAY)));
        map.put(EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY), new BitField<>(WeekDay.class, EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
                WeekDay.SATURDAY, WeekDay.SUNDAY)));

        return map.entrySet().stream().map(element -> DynamicTest.dynamicTest("set[" + element.getKey() +
                "]", () -> assertTrue(this.isSameIterator(element.getKey().iterator(), element.getValue().iterator()))));
    }

    private <T> boolean isSameIterator(final Iterator<T> iterator1, final Iterator<T> iterator2) {
        while(iterator1.hasNext()){
            if(!iterator2.hasNext() || !iterator1.next().equals(iterator2.next())){
                return false;
            }
        }
        return true;
    }

}