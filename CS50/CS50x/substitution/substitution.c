#include <cs50.h>
#include <stdio.h>
#include <ctype.h>
#include <stdbool.h>
#include <string.h>

bool isUpper(char c);
bool isLower(char c);
void encrypt(char cipher[], string text);
bool inputValidate(int argc, string argv[]);

int ALPHABET_LENGTH = 26;

int main(int argc, string argv[])
{
    if (!inputValidate(argc, argv))
    {
        return 1;
    }

    string text = get_string("plaintext:  ");
    encrypt(argv[1], text);

    printf("ciphertext: %s\n", text);
}

bool isUpper(char c)
{
    return c >= 'A' && c <= 'Z';
}

bool isLower(char c)
{
    return c >= 'a' && c <= 'z';
}

void encrypt(string cipher, string text)
{
    for (int i = 0, n = strlen(text); i < n; i++)
    {
        if (isUpper(text[i]))
        {
            text[i] = toupper(cipher[text[i] - 'A']);
        }
        else if (isLower(text[i]))
        {
            text[i] = tolower(cipher[text[i] - 'a']);
        }
    }
}

bool inputValidate(int argc, string argv[])
{
    if (argc != 2)
    {
        printf("Usage : ./substitution key\nFound %i arguments\n", argc);
        return false;
    }
    if (strlen(argv[1]) != ALPHABET_LENGTH)
    {
        printf("Usage : ./substitution key\nKey has length %lu, needs to be length %i\n", strlen(argv[1]), ALPHABET_LENGTH);
        return false;
    }
    bool seen[26] = { false };
    for (int i = 0 ; i < ALPHABET_LENGTH; i++)
    {
        if (!(isUpper(argv[1][i]) || isLower(argv[1][i])))
        {
            printf("Usage : ./substitution key\nFound illegal argument %c\n", argv[1][i]);
            return false;
        }
        if (isUpper(argv[1][i]))
        {
            if (seen[argv[1][i] - 'A'])
            {
                printf("Usage : ./substitution key\nFound duplicate character %c\n", argv[1][i]);
                return false;
            }
            seen[argv[1][i] - 'A'] = true;
        }
        else if (seen[argv[1][i] - 'a'])
        {
            printf("Usage : ./substitution key\nFound duplicate character %c\n", argv[1][i]);
            return false;

        }
        else
        {
            seen[argv[1][i] - 'a'] = true;
        }

    }
    return true;
}