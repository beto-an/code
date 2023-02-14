#include <cs50.h>
#include <stdio.h>
#include <stdbool.h>

long promptUser(void) {
    return get_long("Number: ");
}

int addToSum(long number, bool evenDigit) {
    int digit = number % 10;
    if (evenDigit) {
        if (digit >= 5) {
            return 1 + (2 * digit) % 10;
        }
        return (2 * digit) % 10;
    } else {
        return digit;
    }
}

int checkCardNumber(long number) {
    int sum = 0;
    int length = 2;
    long reduction = number;
    bool evenDigit = false;
    while (reduction >= 100) {
        sum += addToSum(reduction, evenDigit);
        evenDigit = !evenDigit;
        reduction /= 10;
        length++;
    }
    sum += addToSum(reduction, evenDigit) + addToSum(reduction / 10, !evenDigit);
    if (sum % 10 != 0) {
        return -1;
    }
    if ((reduction == 34 || reduction == 37) && length == 15) {
        return 1;
    }
    if ((reduction == 51 || reduction == 52 || reduction == 53 || reduction == 54 || reduction == 55) && length == 16) {
        return 2;
    }
    if (reduction / 10 == 4 && length >= 13 && length <= 16) {
        return 3;
    }
    return -1;

}

int main(void) {
    long creditCardNumber = promptUser();
    int result = checkCardNumber(creditCardNumber);
    if (result == -1) {
        printf("INVALID\n");
    } else if (result == 1) {
        printf("AMEX\n");
    } else if (result == 2) {
        printf("MASTERCARD\n");
    } else if (result == 3) {
        printf("VISA\n");
    }
}