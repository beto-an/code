package assignment;

/**
 * Applies Identity (Null) Shift on RGB values.
 */
public class NullShiftOperator implements ShiftOperator{
    @Override
    public int redShift(int redValue) {
        return redValue;
    }

    @Override
    public int greenShift(int greenValue) {
        return greenValue;
    }

    @Override
    public int blueShift(int blueValue) {
        return blueValue;
    }
}
