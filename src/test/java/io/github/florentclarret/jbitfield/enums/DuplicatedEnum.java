package io.github.florentclarret.jbitfield.enums;

import io.github.florentclarret.jbitfield.BitFieldElement;

public enum DuplicatedEnum implements BitFieldElement {
    FIRST_VALUE(0),
    SECOND_VALUE(3),
    THIRD_VALUE(3);

    private final int position;

    DuplicatedEnum(final int position) {
        this.position = position;
    }

    @Override
    public int getBitFieldPosition() {
        return position;
    }
}
