package assignment;

/**
 * Shifts RGB values by specified amount.
 */
public class ColorShiftOperator implements ShiftOperator{

    private final int redShift;
    private final int greenShift;
    private final int blueShift;

    public ColorShiftOperator(int[] shift) {
        redShift = shift[0];
        greenShift = shift[1];
        blueShift = shift[2];
    }

    @Override
    public int redShift(int redValue) {
        return redValue + redShift;
    }

    @Override
    public int greenShift(int greenValue) {
        return greenValue + greenShift;
    }

    @Override
    public int blueShift(int blueValue) {
        return blueValue + blueShift;
    }
}
