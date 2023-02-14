#include <cs50.h>
#include <stdio.h>

int promptUser(int lowerBound, int upperBound) {
    int height = get_int("Height: ");
    while (height < lowerBound || height > upperBound) {
        printf("Please enter a height between %i and %i\n", lowerBound, upperBound);
        height = get_int("Height: ");
    }
    return height;
}

void printSpaces(int num) {
    for (int i = 0; i < num; i++) {
        printf(" ");
    }
}

void printHashTags(int num) {
    for (int i = 0; i < num; i++) {
        printf("#");
    }
}

void constructPyramidLevel(int height, int level) {
    printSpaces(height - level);
    printHashTags(level);
    printSpaces(2);
    printHashTags(level);
    printf("\n");
}

void constructPyramid(int height) {
    for (int i = 1; i <= height; i++) {
        constructPyramidLevel(height, i);
    }
}

int main(void) {
    int height = promptUser(1, 8);
    constructPyramid(height);
}