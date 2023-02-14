package assignment;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Contains operations to apply to 2D pixel arrays.
 */
public class PixelOperator {
    /**
     * Matrix to describe an identity transformation on RGB values.
     */
    public static final double[][] IDENTITY_SCALE = new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };

    public static final int[] IDENTITY_SHIFT = new int[]{0, 0, 0};

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels    2D array describing each pixel's RGB values through an int
     * @param scale     3X3 matrix to project onto RGB values
     * @param preShift  3X1 vector to offset RGB values to each pixel BEFORE scaling
     * @param postShift 3X1 vector to offset RGB values to each pixel AFTER scaling
     * @param range     Range of Pixels Affected
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final double[][] scale, final int[] preShift, final int[] postShift, final PixelRange range) {
        int[][] newPixels = copyOf(pixels);
        for (Integer[] coordinates : range) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            //Applying preShift
            final int preShiftRed = ImageEffect.getRed(pixels[y][x]) + preShift[0];
            final int preShiftGreen = ImageEffect.getGreen(pixels[y][x]) + preShift[1];
            final int preShiftBlue = ImageEffect.getBlue(pixels[y][x]) + preShift[2];

            //Applying Scale
            final int redValue = (int) (scale[0][0] * preShiftRed
                    + scale[0][1] * preShiftGreen
                    + scale[0][2] * preShiftBlue);
            final int greenValue = (int) (scale[1][0] * preShiftRed
                    + scale[1][1] * preShiftGreen
                    + scale[1][2] * preShiftBlue);
            final int blueValue = (int) (scale[2][0] * preShiftRed
                    + scale[2][1] * preShiftGreen
                    + scale[2][2] * preShiftBlue);

            //Applying postShift
            newPixels[y][x] = ImageEffect.makePixel(Math.max(0, Math.min(255, redValue + postShift[0])),
                    Math.max(0, Math.min(255, greenValue + postShift[1])),
                    Math.max(0, Math.min(255, blueValue + postShift[2])));
        }
        return newPixels;
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param scale  3X3 matrix to project onto RGB values
     * @param postShift  3X1 vector to offset RGB values to each pixel AFTER scaling
     * @param range  Range of Pixels Affected
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final double[][] scale, final int[] postShift, final PixelRange range) {
        return transformRGB(pixels, scale, IDENTITY_SHIFT, postShift, range);
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param scale  3X3 matrix to project onto RGB values
     * @param range  Range of Pixels Affected
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final double[][] scale, final PixelRange range) {
        return transformRGB(pixels, scale, IDENTITY_SHIFT, IDENTITY_SHIFT, range);
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param postShift  3X1 vector to offset RGB values to each pixel AFTER scaling
     * @param range  Range of Pixels Affected
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final int[] postShift, final PixelRange range) {
        return transformRGB(pixels, IDENTITY_SCALE, IDENTITY_SHIFT, postShift, range);
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels    2D array describing each pixel's RGB values through an int
     * @param scale     3X3 matrix to project onto RGB values
     * @param preShift  3X1 vector to offset RGB values to each pixel BEFORE scaling
     * @param postShift 3X1 vector to offset RGB values to each pixel AFTER scaling
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final double[][] scale, final int[] preShift, final int[] postShift) {
        return transformRGB(pixels, scale, preShift, postShift, new PixelRange(pixels));
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param scale  3X3 matrix to project onto RGB values
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final double[][] scale) {
        return transformRGB(pixels, scale, IDENTITY_SHIFT, IDENTITY_SHIFT);
    }

    /**
     * Applies a linear or affine transformation to the RGB Values to all pixels in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param postShift  3X1 vector to offset RGB values to each pixel AFTER scaling
     * @return 2D array of pixels scaled and shifted as described
     */
    public static int[][] transformRGB(final int[][] pixels, final int[] postShift) {
        return transformRGB(pixels, IDENTITY_SCALE, IDENTITY_SHIFT, postShift);
    }

    /**
     * Finds the average of each RGB values in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @return An int array with the average RGB values
     */
    public static int[] averageRGB(final int[][] pixels, PixelRange range) {
        int[] rgbSum = new int[3];

        for (Integer[] coordinates : range) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            rgbSum[0] += ImageEffect.getRed(pixels[y][x]);
            rgbSum[1] += ImageEffect.getGreen(pixels[y][x]);
            rgbSum[2] += ImageEffect.getBlue(pixels[y][x]);
        }
        rgbSum[0] /= range.getPixelCount();
        rgbSum[1] /= range.getPixelCount();
        rgbSum[2] /= range.getPixelCount();

        return rgbSum;
    }

    /**
     * Finds the median of each RGB values in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @return An int array with the median RGB values
     */
    public static int[] medianRGB(final int[][] pixels, PixelRange range) {
        int[][] rgbValues = new int[3][range.getPixelCount()];
        int i = 0;
        for (Integer[] coordinates : range) {
            final int x = coordinates[1];
            final int y = coordinates[0];

            rgbValues[0][i] = ImageEffect.getRed(pixels[y][x]);
            rgbValues[1][i] = ImageEffect.getGreen(pixels[y][x]);
            rgbValues[2][i] = ImageEffect.getBlue(pixels[y][x]);
            i++;
        }
        Arrays.sort(rgbValues[0]);
        Arrays.sort(rgbValues[1]);
        Arrays.sort(rgbValues[2]);

        return new int[]{medianOf(rgbValues[0]), medianOf(rgbValues[1]), medianOf(rgbValues[2])};
    }

    /**
     * Finds the pixel with median brightness in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @return An int array with the RGB values of the median pixel with respect to brightness
     */
    public static int[] medianBrightness(final int[][] pixels, PixelRange range) {
        int[] brightnessValues = new int[range.getPixelCount()];
        int i = 0;
        HashMap<Integer, Integer[]> brightness = new HashMap<>();

        for (Integer[] coordinates : range) {
            final int x = coordinates[1];
            final int y = coordinates[0];
            brightnessValues[i] = ImageEffect.getRed(pixels[y][x])
                    + ImageEffect.getGreen(pixels[y][x])
                    + ImageEffect.getBlue(pixels[y][x]);
            brightness.putIfAbsent(brightnessValues[i], new Integer[]{y, x});
            i++;
        }
        Arrays.sort(brightnessValues);

        final Integer[] medianPixelCoordinates = brightness.get(brightnessValues[range.getPixelCount() / 2]);
        final int medianPixel = pixels[medianPixelCoordinates[0]][medianPixelCoordinates[1]];
        return new int[]{ImageEffect.getRed(medianPixel), ImageEffect.getGreen(medianPixel), ImageEffect.getBlue(medianPixel)};
    }

    /**
     * Finds the maximum of each RGB values in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @return An int array with the maximum RGB values
     */
    public static int[] maximumRGB(final int[][] pixels, PixelRange range) {
        int[] maximumRGB = new int[3];

        for (Integer[] coordinates : range) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            maximumRGB[0] = Math.max(ImageEffect.getRed(pixels[y][x]), maximumRGB[0]);
            maximumRGB[1] = Math.max(ImageEffect.getGreen(pixels[y][x]), maximumRGB[1]);
            maximumRGB[2] = Math.max(ImageEffect.getBlue(pixels[y][x]), maximumRGB[2]);
        }
        return maximumRGB;
    }

    /**
     * Finds the minimum of each RGB values in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @return An int array with the minimum RGB values
     */
    public static int[] minimumRGB(final int[][] pixels, PixelRange range) {
        int[] minimumRGB = new int[]{255, 255, 255};

        for (Integer[] coordinates : range) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            minimumRGB[0] = Math.min(ImageEffect.getRed(pixels[y][x]), minimumRGB[0]);
            minimumRGB[1] = Math.min(ImageEffect.getGreen(pixels[y][x]), minimumRGB[1]);
            minimumRGB[2] = Math.min(ImageEffect.getBlue(pixels[y][x]), minimumRGB[2]);
        }
        return minimumRGB;
    }

    /**
     * Finds the norm contrast in a specified range.
     *
     * @param pixels 2D array describing each pixel's RGB values through an int
     * @param range  Range of Pixels Affected
     * @param DEGREE Degree of Deviation
     * @return The length of the contrast vector, calculated by taking DEGREE-norm differences
     */
    public static double pixelContrast(final int[][] pixels, final PixelRange range, final double DEGREE) {
        final int[] pixelAverage = PixelOperator.averageRGB(pixels, range);
        final double[] pixelNorm = new double[3];
        for (Integer[] coordinates : range) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            pixelNorm[0] += Math.pow(Math.abs(ImageEffect.getRed(pixels[y][x]) - pixelAverage[0]), DEGREE);
            pixelNorm[1] += Math.pow(Math.abs(ImageEffect.getGreen(pixels[y][x]) - pixelAverage[1]), DEGREE);
            pixelNorm[2] += Math.pow(Math.abs(ImageEffect.getBlue(pixels[y][x]) - pixelAverage[2]), DEGREE);
        }

        pixelNorm[0] = Math.pow(pixelNorm[0] / range.getPixelCount(), 1.0 / DEGREE);
        pixelNorm[1] = Math.pow(pixelNorm[1] / range.getPixelCount(), 1.0 / DEGREE);
        pixelNorm[2] = Math.pow(pixelNorm[2] / range.getPixelCount(), 1.0 / DEGREE);

        return Math.sqrt(Math.pow(pixelNorm[0], 2) + Math.pow(pixelNorm[1], 2) + Math.pow(pixelNorm[2], 2));
    }

    /**
     * Deep-copies a 2D integer array.
     *
     * @param a the target array
     * @return copy of target array
     */
    private static int[][] copyOf(final int[][] a) {
        int[][] arrayCopy = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            arrayCopy[i] = Arrays.copyOf(a[i], a[0].length);
        }
        return arrayCopy;
    }

    /**
     * Finds the median of an int array.
     *
     * @param a the target array
     * @return the median of the target array
     */
    protected static int medianOf(final int[] a) {
        if (a.length % 2 == 1) return a[a.length / 2];
        return (a[a.length / 2 - 1] + a[a.length / 2]) / 2;
    }
}