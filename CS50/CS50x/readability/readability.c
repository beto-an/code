#include <cs50.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

string promptText(void);
void countText(string text, int properties[]);
bool isLetter(char c);
bool isWhitespace(char c);
bool isSentenceEnd(char c);
double computeIndex(int properties[]);
void findGradeLevel(double index);

int main(void)
{
    string text = promptText();
    int properties[] = {0, 0, 0};
    countText(text, properties);
    findGradeLevel(computeIndex(properties));
}

string promptText(void)
{
    return get_string("Text: ");

}

void countText(string text, int properties[])
{
    for (int i = 0, n = strlen(text); i < n; i++)
    {
        if (isLetter(text[i]))
        {
            properties[0]++;
        }
        else if (isWhitespace(text[i]))
        {
            properties[1]++;
        }
        else if (isSentenceEnd(text[i]))
        {
            properties[2]++;
        }
    }
    properties[1]++;
}

bool isLetter(char c)
{
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
}

bool isWhitespace(char c)
{
    return c == ' ';
}

bool isSentenceEnd(char c)
{
    return c == '.' || c == '?' || c == '!';
}

double computeIndex(int properties[])
{
    return 5.88 * properties[0] / properties[1] - 29.6 * properties[2] / properties[1] - 15.8;
}

void findGradeLevel(double index)
{
    int grade = (int) round(index);
    if (grade < 1)
    {
        printf("Before Grade 1\n");
    }
    else if (grade >= 16)
    {
        printf("Grade 16+\n");
    }
    else
    {
        printf("Grade %i\n", grade);
    }
}