package io.github.florentclarret.jbitfield.enums;

import io.github.florentclarret.jbitfield.BitFieldElement;

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
