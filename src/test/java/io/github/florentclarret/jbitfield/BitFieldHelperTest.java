package io.github.florentclarret.jbitfield;

import io.github.florentclarret.jbitfield.enums.WeekDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class BitFieldHelperTest {

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

    public enum InvalidEnum implements BitFieldElement {
        FIRST_VALUE(0),
        SECOND_VALUE(0),
        THIRD_VALUE(1);

        private final int position;

        InvalidEnum(final int position) {
            this.position = position;
        }

        @Override
        public int getBitFieldPosition() {
            return position;
        }
    }

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
        Assert.assertFalse(BitFieldHelper.isValidEnum(InvalidEnum.class));
    }
}
