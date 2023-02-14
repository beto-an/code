#include <stdio.h>
#include <cs50.h>

int main(void) {
    long first_number = get_int("Enter a value for x: ");
    long second_number = get_int("Enter a value for y: ");
    printf("x + y = %li\n", first_number + second_number);
}