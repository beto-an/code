#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <stdbool.h>

bool isLowerCase(char c) {
    return c >= 'a' && c < 'z';
}

void toUpperCase(string s) {
    for (int i = 0, n = strlen(s); i < n; i++) {
        if (isLowerCase(s[i])) {
            s[i] -= 32;
        }
    }
}

int main(void) {
    string text = get_string("Input:  ");
    toUpperCase(text);rm    
    printf("Output: %s\n", text);
}