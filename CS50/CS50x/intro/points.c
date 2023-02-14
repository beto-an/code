# include <stdio.h>
# include <cs50.h>

int main(void) {
    int my_points = get_int("How many points did Beto lose? ");
    while (my_points < 0) {
        printf("Must be positive\n");
        my_points = get_int("How many points did Beto lose? ");
    }
    int your_points = get_int("How many points did you lose? ");
    while (your_points < 0) {
        printf("Must be positive\n");
        your_points = get_int("How many points did you lose? ");
    }
    if (my_points < your_points) {
        printf("Hah! Take an L.\n");
    } else if (my_points > your_points) {
        printf("Fuck you.\n");
    } else {
        printf("Whatever.\n");
    }
}