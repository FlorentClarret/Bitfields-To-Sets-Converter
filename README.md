*Disclaimer : I wrote this very small "library" for fun. It is not finished nor completely tested.*

[![CircleCI](https://circleci.com/gh/FlorentClarret/JBitField.svg?style=svg)](https://circleci.com/gh/FlorentClarret/JBitField)

# BitField and (Enum)Set conversions

## Purpose

This java library is designed to convert sets of enumeration to bit fields. It's also possible to convert bit fields to set of enumeration.

My first idea was to create the most generic library possible to make it usable almost everywhere without (not so much) effort.

This library is still in construction. At the moment there are not a lot of features and a lot of tests are missing.

## How to use it

### General description

*TL;DR* : [Code example](#code-example).

This library only allow you to convert sets of enumerations to bit fields.

1. Make your enumeration extends the BitFieldElement and give a position for all your values. Be careful, each value of your enum must have a different position in the bit field.
2. Create instances of the BitField class using your previously created enumeration.

### Code example

Your enumeration :

``` java
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
```

Simple BitField class usage :

``` java
public static void main(final String[] args) {
        long mondayBitField = new BitField<>(WeekDay.MONDAY).getBitFieldValue();
        long mondaySundayBitField = new BitField<>(WeekDay.MONDAY,WeekDay.SUNDAY).getBitFieldValue();

        Set<WeekDay> mondaySet = new BitField<>(WeekDay.class, mondayBitField).getSet();
        Set<WeekDay> mondaySundaySet = new BitField<>(WeekDay.class, mondaySundayBitField).getSet();
    }
```

## Contribute

If you have any idea or if you're willing to contribute, don't hesitate to do it!

## License

This project is under the MIT license.
