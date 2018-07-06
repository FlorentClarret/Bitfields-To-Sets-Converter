package io.github.florentclarret.jbitfield;

import io.github.florentclarret.jbitfield.enums.WeekDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class BitFieldHelperTest {

    @Test
    public void testIsValid() {
        Assert.assertTrue(BitFieldHelper.isValidEnum(WeekDay.class));
    }

    @Test
    public void testIsValid_WithMissingValues() {
        Assert.assertTrue(BitFieldHelper.isValidEnum(ValidEnum.class));
    }

    @Test
    public void testIsValid_WithDuplicatedValues() {
        Assert.assertFalse(BitFieldHelper.isValidEnum(DuplicatedEnum.class));
    }

    @Test
    public void testIsValid_WithNegativeValues() {
        Assert.assertFalse(BitFieldHelper.isValidEnum(NegativeEnum.class));
    }

    public enum ValidEnum implements BitFieldElement {
        FIRST_VALUE(0),
        SECOND_VALUE(2),
        THIRD_VALUE(3);

        private final int position;

        ValidEnum(final int position) {
            this.position = position;
        }

        @Override
        public int getBitFieldPosition() {
            return position;
        }
    }

    public enum DuplicatedEnum implements BitFieldElement {
        FIRST_VALUE(0),
        SECOND_VALUE(0),
        THIRD_VALUE(1);

        private final int position;

        DuplicatedEnum(final int position) {
            this.position = position;
        }

        @Override
        public int getBitFieldPosition() {
            return position;
        }
    }

    public enum NegativeEnum implements BitFieldElement {
        FIRST_VALUE(0),
        SECOND_VALUE(1),
        THIRD_VALUE(-2);

        private final int position;

        NegativeEnum(final int position) {
            this.position = position;
        }

        @Override
        public int getBitFieldPosition() {
            return position;
        }
    }
}
