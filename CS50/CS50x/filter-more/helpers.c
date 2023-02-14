#include "helpers.h"
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <stdio.h>

void swap(RGBTRIPLE *x, RGBTRIPLE *y);
double *convolute(int height, int width, RGBTRIPLE image[height][width], int i, int j, int a1, int a2, int a3, int b1, int b2,
                  int b3, int c1, int c2, int c3, bool edges);
int min(int a, int b);

// Convert image to grayscale
void grayscale(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            BYTE averageRGB = round((image[i][j].rgbtBlue + image[i][j].rgbtGreen + image[i][j].rgbtRed) / 3.0);
            image[i][j].rgbtBlue = averageRGB;
            image[i][j].rgbtGreen = averageRGB;
            image[i][j].rgbtRed = averageRGB;
        }
    }

    return;
}

// Reflect image horizontally
void reflect(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width / 2; j++)
        {
            swap(&image[i][j], &image[i][width - 1 - j]);
        }
    }
    return;
}

// Blur image
void blur(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE newimage[height][width];
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            double *rgb = convolute(height, width, image, i, j, 1, 1, 1, 1, 1, 1, 1, 1, 1, true);
            newimage[i][j].rgbtRed = (BYTE) round(*rgb);
            newimage[i][j].rgbtGreen = (BYTE) round(*(rgb + 1));
            newimage[i][j].rgbtBlue = (BYTE) round(*(rgb + 2));
            free(rgb);
        }
    }
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            image[i][j] = newimage[i][j];
        }
    }
    return;
}

// Detect edges
void edges(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE newimage[height][width];
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            double *rgb1 = convolute(height, width, image, i, j, -9, 0, 9, -18, 0, 18, -9, 0, 9, false);
            double *rgb2 = convolute(height, width, image, i, j, -9, -18, -9, 0, 0, 0, 9, 18, 9, false);
            newimage[i][j].rgbtRed = (BYTE) min(round(sqrt(*rgb1 * *rgb1 + *rgb2 * *rgb2)), 255);
            newimage[i][j].rgbtGreen = (BYTE) min(round(sqrt(*(rgb1 + 1) * *(rgb1 + 1) + * (rgb2 + 1) * *(rgb2 + 1))), 255);
            newimage[i][j].rgbtBlue = (BYTE) min(round(sqrt(*(rgb1 + 2) * *(rgb1 + 2) + * (rgb2 + 2) * *(rgb2 + 2))), 255);
            free(rgb1);
            free(rgb2);
        }
    }
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            image[i][j] = newimage[i][j];
        }
    }
    return;
}

void swap(RGBTRIPLE *x, RGBTRIPLE *y)
{
    RGBTRIPLE temp = *x;
    *x = *y;
    *y = temp;
}

double *convolute(int height, int width, RGBTRIPLE image[height][width], int i, int j, int a1, int a2, int a3, int b1, int b2,
                  int b3, int c1, int c2, int c3, bool edges)
{
    int red = b2 * (int) image[i][j].rgbtRed;
    int green = b2 * (int) image[i][j].rgbtGreen;
    int blue = b2 * (int) image[i][j].rgbtBlue;
    int count = 9;

    if (i > 0)
    {
        red += a2 * (int) image[i - 1][j].rgbtRed;
        green += a2 * (int) image[i - 1][j].rgbtGreen;
        blue += a2 * (int) image[i - 1][j].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (i < height - 1)
    {
        red += c2 * (int) image[i + 1][j].rgbtRed;
        green += c2 * (int) image[i + 1][j].rgbtGreen;
        blue += c2 * (int) image[i + 1][j].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (j > 0)
    {
        red += b1 * (int) image[i][j - 1].rgbtRed;
        green += b1 * (int) image[i][j - 1].rgbtGreen;
        blue += b1 * (int) image[i][j - 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (j < width - 1)
    {
        red += b3 * (int) image[i][j + 1].rgbtRed;
        green += b3 * (int) image[i][j + 1].rgbtGreen;
        blue += b3 * (int) image[i][j + 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (i > 0 && j > 0)
    {
        red += a1 * (int) image[i - 1][j - 1].rgbtRed;
        green += a1 * (int) image[i - 1][j - 1].rgbtGreen;
        blue += a1 * (int) image[i - 1][j - 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (i > 0 && j < width - 1)
    {
        red += a3 * (int) image[i - 1][j + 1].rgbtRed;
        green += a3 * (int) image[i - 1][j + 1].rgbtGreen;
        blue += a3 * (int) image[i - 1][j + 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (i < height - 1 && j > 0)
    {
        red += c1 * (int) image[i + 1][j - 1].rgbtRed;
        green += c1 * (int) image[i + 1][j - 1].rgbtGreen;
        blue += c1 * (int) image[i + 1][j - 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    if (i < height - 1 && j < width - 1)
    {
        red += c3 * (int) image[i + 1][j + 1].rgbtRed;
        green += c3 * (int) image[i + 1][j + 1].rgbtGreen;
        blue += c3 * (int) image[i + 1][j + 1].rgbtBlue;
    }
    else if (edges)
    {
        count--;
    }

    double *rgb = malloc(3 * sizeof(double));
    *rgb = red * 1.0 / count;
    *(rgb + 1) = green * 1.0 / count;
    *(rgb + 2) = blue * 1.0 / count;
    return rgb;
}

int min(int a, int b)
{
    if (a < b)
    {
        return a;
    }
    return b;
}

void printImage(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            printf("Location: %i, %i, Red: %i, Green: %i, Blue: %i\n", i, j, image[i][j].rgbtRed, image[i][j].rgbtGreen, image[i][j].rgbtBlue);
        }
    }
}