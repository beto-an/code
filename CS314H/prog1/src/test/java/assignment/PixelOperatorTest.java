package assignment;

public class PixelOperatorTest {
    public void testMedianOf() {
        {
            final int[] a = {1, 4, 6, 8, 12, 15, 20};
            assert PixelOperator.medianOf(a) == 8;
        }
        {
            final int[] b = {1, 4, 6, 8, 12, 15, 20, 42};
            assert PixelOperator.medianOf(b) == 10;
        }
    }

    public static void main(String[] args) {
        final PixelOperatorTest tester = new PixelOperatorTest();
        tester.testMedianOf();
    }
}