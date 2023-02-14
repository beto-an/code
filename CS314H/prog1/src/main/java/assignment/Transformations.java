package assignment;

/*
 *
 * CS314H Programming Assignment 1 - Java image processing
 *
 * Included is the Invert effect from the assignment.  Use this as an
 * example when writing the rest of your transformations.  For
 * convenience, you should place all of your transformations in this file.
 *
 * You can compile everything that is needed with
 * javac -d bin src/main/java/assignment/*.java
 *
 * You can run the program with
 * java -cp bin assignment.JIP
 *
 * Please note that the above commands assume that you are in the prog1
 * directory.
 */

import java.util.ArrayList;

import static assignment.Transformations.*;

/**
 * Contains common scaling matrices
 */
public class Transformations {
    /**
     * Matrix to send Red RGB values to zero.
     */
    public static final double[][] NO_RED_SCALE = new double[][]{
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 1}};

    /**
     * Matrix to send Green RGB values to zero.
     */
    public static final double[][] NO_GREEN_SCALE = new double[][]{
            {1, 0, 0},
            {0, 0, 0},
            {0, 0, 1}};

    /**
     * Matrix to send Blue RGB values to zero.
     */
    public static final double[][] NO_BLUE_SCALE = new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 0}};

    /**
     * Matrix to send Green and Blue RGB values to zero.
     */
    public static final double[][] RED_ONLY_SCALE = new double[][]{
            {1, 0, 0},
            {0, 0, 0},
            {0, 0, 0}};

    /**
     * Matrix to send Red and Blue RGB values to zero.
     */
    public static final double[][] GREEN_ONLY_SCALE = new double[][]{
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}};

    /**
     * Matrix to send Red and Green RGB values to zero.
     */
    public static final double[][] BLUE_ONLY_SCALE = new double[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 1}};

    /**
     * Matrix to average RGB values.
     */
    public static final double[][] BLACK_WHITE_SCALE = new double[][]{
            {1.0 / 3, 1.0 / 3, 1.0 / 3},
            {1.0 / 3, 1.0 / 3, 1.0 / 3},
            {1.0 / 3, 1.0 / 3, 1.0 / 3}};
}

/**
 * Contains transformation to invert an image
 */
class Invert extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[y][x] = ~pixels[y][x];
            }
        }
        return pixels;
    }
}

/**
 * Contains transformation to remove red from an image
 */
class NoRed extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, NO_RED_SCALE);
    }
}

/**
 * Contains transformation to remove green from an image
 */
class NoGreen extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, NO_GREEN_SCALE);
    }
}

/**
 * Contains transformation to remove blue from an image
 */
class NoBlue extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, NO_BLUE_SCALE);
    }
}

/**
 * Contains transformation to remove green and blue from an image
 */
class RedOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, RED_ONLY_SCALE);
    }
}

/**
 * Contains transformation to remove red and blue from an image
 */
class GreenOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, GREEN_ONLY_SCALE);
    }
}

/**
 * Contains transformation to remove red and green from an image
 */
class BlueOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, BLUE_ONLY_SCALE);
    }
}

/**
 * Contains transformation to make pixels grayscale
 */
class BlackAndWhite extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        return PixelOperator.transformRGB(pixels, BLACK_WHITE_SCALE);
    }
}

/**
 * Contains transformation to reflect an image over the vertical axis
 */
class VerticalReflect extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int halfWidth = width / 2;

        final PixelRange image = new PixelRange(0, height, 0, halfWidth);

        for (Integer[] coordinates : image) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            final int temp = pixels[y][x];
            pixels[y][x] = pixels[y][width - 1 - x];
            pixels[y][width - 1 - x] = temp;

        }
        return pixels;
    }
}

/**
 * Contains transformation to reflect an image over the horizontal axis
 */
class HorizontalReflect extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int halfHeight = height / 2;

        final PixelRange image = new PixelRange(0, halfHeight, 0, width);

        for (Integer[] coordinates : image) {
            final int y = coordinates[0];
            final int x = coordinates[1];

            final int temp = pixels[y][x];
            pixels[y][x] = pixels[height - 1 - y][x];
            pixels[height - 1 - y][x] = temp;
        }
        return pixels;
    }
}

/**
 * Contains transformation to double each dimension
 */
class Grow extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        
        int[][] grow_pixels = new int[2*height][2*width];
        final PixelRange image = new PixelRange(grow_pixels);

        for (Integer[] pixel : image) {
            grow_pixels[pixel[0]][pixel[1]] = pixels[pixel[0]/2][pixel[1]/2];
        }
        return grow_pixels;
    }
}

/**
 * Contains transformation to half each dimension
 */
class Shrink extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;

        int[][] shrink_pixels = new int[height / 2][width / 2];
        final PixelRange image = new PixelRange(shrink_pixels);

        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(2*pixel[0], 2*pixel[0] + 2, 2*pixel[1], 2*pixel[1] + 2);
            final int[] averagePixel = PixelOperator.averageRGB(pixels, neighborhood);
            shrink_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(averagePixel[0], averagePixel[1], averagePixel[2]);
        }
        return shrink_pixels;
    }
}

/**
 * Contains transformation to send each pixel's RGB values to 0 or 255 relative to the threshold.
 */
class Threshold extends ImageEffect {

    public Threshold() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("Threshold",
                "RGB value threshold",
                127, 0, 254));
    }
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int threshold = params.get(0).getValue();


        final int[] thresholdShift = {254 - threshold, 254 - threshold, 254 - threshold};

        final double[][] downScale = {
                {1.0/255, 0, 0},
                {0, 1.0/255, 0},
                {0, 0, 1.0/255}
        };

        final double[][] upScale = {
                {255, 0, 0},
                {0, 255, 0},
                {0, 0, 255}
        };

        pixels = PixelOperator.transformRGB(pixels, downScale, thresholdShift, PixelOperator.IDENTITY_SHIFT);
        pixels = PixelOperator.transformRGB(pixels, upScale);
        return pixels;
    }
}

/**
 * Contains transformation to replace pixels with their region's average pixel.
 */
class Smooth extends ImageEffect {
    public Smooth() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelAverage = PixelOperator.averageRGB(pixels, neighborhood);

            new_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(pixelAverage[0], pixelAverage[1], pixelAverage[2]);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to replace pixels with a pixel that diverges from the average pixel in the nearby region.
 */
class Sharpen extends ImageEffect {
    public Sharpen() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
        params.add(new ImageEffectParam("sharpeningRate",
                "Magnitude of Sharpening",
                1, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();
        final int sharpeningRate = params.get(1).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelAverage = PixelOperator.averageRGB(pixels, neighborhood);

            final int[] sharpenedPixelAverage = new int[]{
                    -sharpeningRate * pixelAverage[0],
                    -sharpeningRate * pixelAverage[1],
                    -sharpeningRate * pixelAverage[2]
            };
            final double[][] sharpenedScale = new double[][]{
                    {1 + sharpeningRate, 0, 0},
                    {0, 1 + sharpeningRate, 0},
                    {0, 0, 1 + sharpeningRate}
            };

            new_pixels[pixel[0]][pixel[1]] = PixelOperator.transformRGB(pixels, sharpenedScale, sharpenedPixelAverage, new PixelRange(pixel[0], pixel[1]))[pixel[0]][pixel[1]];
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to highlight contrast.
 */
class HighlightEdges extends ImageEffect {
    public HighlightEdges() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
        params.add(new ImageEffectParam("degree",
                "Degree of Contrast",
                2, 0, Integer.MAX_VALUE));
        params.add(new ImageEffectParam("contrastScale",
                "Scale of Contrast",
                2, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();
        final int degree = params.get(1).getValue();
        final int contrastScale = params.get(2).getValue();

        int[][] new_pixels = new int[height][width];

        final PixelRange image = new PixelRange(pixels);
        for (Integer[] coordinates : image) {
            final int y = coordinates[0];
            final int x = coordinates[1];
            final PixelRange neighborhood = new PixelRange(y, x, gridSize, width, height);

            final int contrast = (int) PixelOperator.pixelContrast(pixels, neighborhood, degree);

            new_pixels[y][x] = ImageEffect.makePixel(contrastScale * contrast, contrastScale * contrast, contrastScale * contrast);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to replace pixels with their region's median pixel.
 */
class DeNoise extends ImageEffect {
    public DeNoise() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelMedian = PixelOperator.medianRGB(pixels, neighborhood);

            new_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(pixelMedian[0], pixelMedian[1], pixelMedian[2]);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to replace pixels with their region's median pixel, in terms of brightness.
 */
class DeNoiseBrightness extends ImageEffect {
    public DeNoiseBrightness() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelMedian = PixelOperator.medianBrightness(pixels, neighborhood);

            new_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(pixelMedian[0], pixelMedian[1], pixelMedian[2]);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to replace pixels with their region's minimum pixel.
 */
class Erode extends ImageEffect {
    public Erode() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelMinimum = PixelOperator.minimumRGB(pixels, neighborhood);

            new_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(pixelMinimum[0], pixelMinimum[1], pixelMinimum[2]);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to replace pixels with their region's maximum pixel.
 */
class Dilate extends ImageEffect {
    public Dilate() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("gridsize",
                "Size of grid",
                1, 0, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final int gridSize = params.get(0).getValue();

        final PixelRange image = new PixelRange(pixels);

        int[][] new_pixels = new int[height][width];
        for (Integer[] pixel : image) {
            final PixelRange neighborhood = new PixelRange(pixel[0], pixel[1], gridSize, width, height);

            final int[] pixelMaximum = PixelOperator.maximumRGB(pixels, neighborhood);

            new_pixels[pixel[0]][pixel[1]] = ImageEffect.makePixel(pixelMaximum[0], pixelMaximum[1], pixelMaximum[2]);
        }
        return new_pixels;
    }
}

/**
 * Contains transformation to shift the RGB values of each pixel by the same given amount.
 */
class ColorShift extends ImageEffect {
    public ColorShift() {
        params = new ArrayList<>();
        params.add(new ImageEffectParam("redshift",
                "Red Shift",
                8, Integer.MIN_VALUE, Integer.MAX_VALUE));
        params.add(new ImageEffectParam("greenshift",
                "Green Shift",
                8, Integer.MIN_VALUE, Integer.MAX_VALUE));
        params.add(new ImageEffectParam("blueshift",
                "Blue Shift",
                8, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        final int[] shift = new int[]{params.get(0).getValue(), params.get(1).getValue(), params.get(2).getValue()};
        return PixelOperator.transformRGB(pixels, shift, new PixelRange(pixels));
    }
}


