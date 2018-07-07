package io.github.florentclarret.jbitfield.enums;

import io.github.florentclarret.jbitfield.BitFieldElement;

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
