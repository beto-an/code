package assignment;

import java.util.Iterator;

/**
 * Represents a range of pixels in an image.
 */
public class PixelRange implements Iterable<Integer[]> {

    /**
     * Left Edge of range
     */
    private final int minX;

    /**
     * Right Edge of range
     */
    private final int maxX;

    /**
     * Top Edge of range
     */
    private final int minY;

    /**
     * Bottom Edge of range
     */
    private final int maxY;

    /**
     * Width of range
     */
    private final int rangeWidth;

    /**
     * Height of range
     */
    private final int rangeHeight;

    /**
     * Number of pixels in range
     */
    private final int pixelCount;

    /**
     * Initializes a range with rectangular bounds.
     *
     * @param minY top edge
     * @param maxY bottom edge
     * @param minX left edge
     * @param maxX right edge
     */
    public PixelRange(final int minY, final int maxY, final int minX, final int maxX) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        rangeWidth = maxX - minX;
        rangeHeight = maxY - minY;
        pixelCount = (maxX - minX) * (maxY - minY);
    }

    /**
     * Initializes a range centered at a pixel with
     *
     * @param y        y-coordinate of central pixel
     * @param x        x-coordinate of the central pixel
     * @param gridSize largest difference in coordinate value
     * @param width    width of the entire image
     * @param height   height of the entire image
     */
    public PixelRange(final int y, final int x, final int gridSize, final int width, final int height) {
        this(Math.max(0, y - gridSize),
                Math.min(height, y + gridSize + 1),
                Math.max(0, x - gridSize),
                Math.min(width, x + gridSize + 1));
    }

    /**
     * Initializes a range with the bounds of a 2D pixel array
     *
     * @param pixels 2D pixel array to take range
     */
    public PixelRange(final int[][] pixels) {
        this(0, pixels.length, 0, pixels[0].length);
    }

    /**
     * Initializes a range at a single point.
     *
     * @param y y-coordinate of point
     * @param x x-coordinate of point
     */
    public PixelRange(final int y, final int x) {
        this(y, y + 1, x, x + 1);
    }


    /**
     * Gets the left bound of the range.
     *
     * @return the minimum x value in the range - inclusive
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Gets the right bound of the range.
     *
     * @return the maximum x value in the range - exclusive
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Gets the upper bound of the range.
     *
     * @return the minimum y value in the range - inclusive
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Gets the lower bound of the range.
     *
     * @return the maximum y value in the range - exclusive
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Gets the number of pixels in a range.
     *
     * @return the number of pixels in the range
     */
    public int getPixelCount() {
        return pixelCount;
    }

    /**
     * Gets the width of a range.
     *
     * @return the width of the range
     */
    public int getRangeWidth() {
        return rangeWidth;
    }

    /**
     * Gets the height of a range.
     *
     * @return the height of the range
     */
    public int getRangeHeight() {
        return rangeHeight;
    }

    /**
     * Gets an iterator over all the pixels contained within a range.
     *
     * @return an iterator for the range
     */
    public Iterator<Integer[]> iterator() {
        return new RangeIterator();
    }

    /**
     * RangeIterator creates an iterator to iterate over all the pixels within the range.
     */
    class RangeIterator implements Iterator<Integer[]> {
        int counter = 0;

        public boolean hasNext() {
            return counter < pixelCount;
        }

        public Integer[] next() {
            final Integer[] coordinates = new Integer[]{minY + counter / rangeWidth, minX + counter % rangeWidth};
            counter++;
            return coordinates;
        }
    }
}
