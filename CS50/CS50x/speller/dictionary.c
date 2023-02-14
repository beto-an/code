// Implements a dictionary's functionality

#include <ctype.h>
#include <stdbool.h>
#include <string.h>
#include <strings.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "dictionary.h"

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// TODO: Choose number of buckets in hash table
const unsigned int N = 100000;

// Hash table
node *table[N];

unsigned int dict_size = 0;


void recursive_free(node *current_node);
void insert(node *current_node);
node *get_last_node(unsigned int i);
bool check_branch(unsigned int hash_value, const char *word);
bool check_node(node *current_node, const char *word);
int min(int a, int b);

// Returns true if word is in dictionary, else false
bool check(const char *word)
{
    unsigned int hash_value = hash(word);

    return check_branch(hash_value, word);
}

// Hashes word to a number
unsigned int hash(const char *word)
{
    long sum = 0;

    for (int i = 0, n = min(strlen(word), 4); i < n; i++)
    {
        sum += ((long)(tolower(word[i]) * pow(31.0, (double)(n - i - 1)))) % N;
    }

    return (unsigned int)((N + (sum % N)) % N);
}

// Loads dictionary into memory, returning true if successful, else false
bool load(const char *dictionary)
{
    // TODO
    FILE *dict = fopen(dictionary, "r");
    if (dict == NULL)
    {
        printf("Could not open file\n");
        return false;
    }
    char *buffer = malloc(1);
    int i = 0;
    node *current_node = malloc(sizeof(node));
    current_node -> next = NULL;
    while (fread(buffer, 1, 1, dict))
    {
        if (*buffer == '\n')
        {
            current_node->word[i] = '\0';
            insert(current_node);
            current_node = malloc(sizeof(node));
            current_node -> next = NULL;
            i = 0;
            dict_size++;
        }
        else
        {
            current_node -> word[i] = *buffer;
            i++;
        }
    }
    free(current_node);
    free(buffer);
    fclose(dict);
    return true;
}

// Returns number of words in dictionary if loaded, else 0 if not yet loaded
unsigned int size(void)
{
    // TODO
    return dict_size;
}

// Unloads dictionary from memory, returning true if successful, else false
bool unload(void)
{
    for (int i = 0; i < N; i++)
    {
        if (table[i] != NULL)
        {
            recursive_free(table[i]);
        }
    }
    return true;
}

void recursive_free(node *current_node)
{
    if (current_node == NULL)
    {
        return;
    }
    if (current_node -> next == NULL)
    {
        free(current_node);
        return;
    }
    recursive_free(current_node -> next);
    free(current_node);
    return;
}

void insert(node *current_node)
{
    unsigned int hash_value = hash(current_node -> word);
    node *previous_node = get_last_node(hash_value);
    if (previous_node == NULL)
    {
        table[hash_value] = current_node;
        return;
    }
    previous_node -> next = current_node;
}

node *get_last_node(unsigned int i)
{
    node *current_node = table[i];
    if (current_node == NULL)
    {
        return NULL;
    }
    while (current_node -> next != NULL)
    {
        current_node = current_node -> next;
    }
    return current_node;
}

bool check_branch(unsigned int hash_value, const char *word)
{
    if (table[hash_value] == NULL)
    {
        return false;
    }
    return check_node(table[hash_value], word);
}

bool check_node(node *current_node, const char *word)
{
    if (current_node == NULL)
    {
        return false;
    }
    if (strcasecmp(current_node -> word, word) == 0)
    {
        return true;
    }
    return check_node(current_node -> next, word);
}

int min(int a, int b)
{
    if (a < b)
    {
        return a;
    }
    return b;
}