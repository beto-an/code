package assignment;

/**
 * Implements objects which can shift RGB values.
 */
public interface ShiftOperator {
    public int redShift(int redValue);
    public int greenShift(int greenValue);
    public int blueShift(int blueValue);
}
