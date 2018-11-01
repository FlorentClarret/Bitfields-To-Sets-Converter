package io.github.florentclarret.jbitfield;

import io.github.florentclarret.jbitfield.enums.DuplicatedEnum;
import io.github.florentclarret.jbitfield.enums.NegativeEnum;
import io.github.florentclarret.jbitfield.enums.ValidEnum;
import io.github.florentclarret.jbitfield.enums.WeekDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BitFieldHelperTest {

    @Test
    public void testIsValid() {
        Assert.assertTrue(BitFieldHelper.isValidEnum(WeekDay.class));
    }

    @Test
    public void testIsValidWithMissingValues() {
        Assert.assertTrue(BitFieldHelper.isValidEnum(ValidEnum.class));
    }

    @Test
    public void testIsValidWithDuplicatedValues() {
        Assert.assertFalse(BitFieldHelper.isValidEnum(DuplicatedEnum.class));
    }

    @Test
    public void testIsValidWithNegativeValues() {
        Assert.assertFalse(BitFieldHelper.isValidEnum(NegativeEnum.class));
    }

    @Test
    public void testIsValidWithNullInput() {
        assertThrows(NullPointerException.class, () -> BitFieldHelper.isValidEnum(null), "enumClass must not be null");
    }
}
