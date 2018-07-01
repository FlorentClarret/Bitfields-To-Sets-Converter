package fr.florentclarret.bitfield.enums;

import fr.florentclarret.bitfield.BitFieldElement;

public enum WeekDay implements BitFieldElement {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final int bitFieldPosition;

    WeekDay(final int position) {
        if(position < 0) {
            throw new IllegalArgumentException("The bitFieldPosition must be positive or null, current value = [" + position + "]");
        }
        this.bitFieldPosition = position;
    }

    @Override
    public int getBitFieldPosition() {
        return bitFieldPosition;
    }
}