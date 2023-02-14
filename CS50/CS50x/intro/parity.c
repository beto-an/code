# include <stdio.h>
# include <cs50.h>

int main(void) {
    int n = get_int("Enter a number: ");
    if (n % 2 == 0) {
        printf("%i is an even number.\n", n);
    } else {
        printf("%i is an odd number.\n", n);
    }
    char c = get_char("Do you want to continue? Enter y for yes, anything else for no. ");
    if (c == 'y' || c == 'Y') {
        main();
    }
}