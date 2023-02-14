#include <cs50.h>
#include <stdio.h>
#include <string.h>

// Max number of candidates
#define MAX 9

// preferences[i][j] is number of voters who prefer i over j
int preferences[MAX][MAX];

// locked[i][j] means i is locked in over j
bool locked[MAX][MAX];

// Each pair has a winner, loser
typedef struct
{
    int winner;
    int loser;
}
pair;

// Array of candidates
string candidates[MAX];
pair pairs[MAX * (MAX - 1) / 2];

int pair_count;
int candidate_count;


// Function prototypes
bool vote(int rank, string name, int ranks[]);
void record_preferences(int ranks[]);
void add_pairs(void);
void sort_pairs(void);
void lock_pairs(void);
void print_winner(void);
void quickSort(int start, int end);
void swap(int i, int j);
bool isCycle(int start, int finish);
int traverseToTop(int start);
void printGraph(void);
void testSort(void);

int main(int argc, string argv[])
{
    // Check for invalid usage
    if (argc < 2)
    {
        printf("Usage: tideman [candidate ...]\n");
        return 1;
    }

    // Populate array of candidates
    candidate_count = argc - 1;
    if (candidate_count > MAX)
    {
        printf("Maximum number of candidates is %i\n", MAX);
        return 2;
    }
    for (int i = 0; i < candidate_count; i++)
    {
        candidates[i] = argv[i + 1];
    }

    // Clear graph of locked in pairs
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            locked[i][j] = false;
        }
    }

    pair_count = 0;
    int voter_count = get_int("Number of voters: ");

    // Query for votes
    for (int i = 0; i < voter_count; i++)
    {
        // ranks[i] is voter's ith preference
        int ranks[candidate_count];

        // Query for each rank
        for (int j = 0; j < candidate_count; j++)
        {
            string name = get_string("Rank %i: ", j + 1);

            if (!vote(j, name, ranks))
            {
                printf("Invalid vote.\n");
                return 3;
            }
        }

        record_preferences(ranks);

        printf("\n");
    }
    add_pairs();
    sort_pairs();
    lock_pairs();
    print_winner();
    return 0;

}

// Update ranks given a new vote
bool vote(int rank, string name, int ranks[])
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (strcmp(name, candidates[i]) == 0)
        {
            ranks[rank] = i;
            return true;
        }
    }
    return false;
}

// Update preferences given one voter's ranks
void record_preferences(int ranks[])
{
    for (int i = 0; i < candidate_count - 1; i++)
    {
        for (int j = i + 1; j < candidate_count; j++)
        {
            preferences[ranks[i]][ranks[j]]++;
        }
    }
    return;
}

// Record pairs of candidates where one is preferred over the other
void add_pairs(void)
{
    int k = 0;
    for (int i = 0; i < candidate_count - 1; i++)
    {
        for (int j = i + 1; j < candidate_count; j++)
        {
            if (preferences[i][j] > preferences[j][i])
            {
                pair curr = { .winner = i, .loser = j};
                pairs[k] = curr;
                k++;
            }
            else if (preferences[i][j] < preferences[j][i])
            {
                pair curr = { .winner = j, .loser = i};
                pairs[k] = curr;
                k++;
            }
        }
    }
    pair_count = k;
}

// Sort pairs in decreasing order by strength of victory
void sort_pairs(void)
{
    quickSort(0, pair_count - 1);

    /*
    printf("pair_count: %i\n", pair_count);
    for (int i = 0; i < pair_count; i++)
    {
        printf("Pair Winner: %i, Pair Loser: %i, Strength: %i\n", pairs[i].winner, pairs[i].loser, pairs[i].strength);
    }
    */

    return;
}

// Lock pairs into the candidate graph in order, without creating cycles
void lock_pairs(void)
{
    for (int i = 0; i < pair_count; i++)
    {
        if (!isCycle(pairs[i].loser, pairs[i].winner))
        {
            locked[pairs[i].winner][pairs[i].loser] = true;
        }
    }
    return;
}

// Print the winner of the election
void print_winner(void)
{
    // printGraph();
    int result = traverseToTop(0);
    printf("%s\n", candidates[result]);
    return;
}

void quickSort(int start, int end)
{
    if (start >= end)
    {
        return;
    }

    pair mid = pairs[start];
    int mid_strength = preferences[mid.winner][mid.loser];
    int i = start + 1;
    int j = start + 1;
    int k = end;

    while (j <= k)
    {
        int curr_strength  = preferences[pairs[j].winner][pairs[j].loser];
        if (curr_strength > mid_strength)
        {
            swap(i, j);
            i++;
            j++;
        }
        else if (curr_strength < mid_strength)
        {
            swap(j, k);
            k--;
        }
        else
        {
            j++;
        }
    }
    swap(start, i - 1);
    quickSort(start, i - 2);
    quickSort(j, end);
}

void swap(int i, int j)
{
    pair temp = pairs[i];;
    pairs[i] = pairs[j];
    pairs[j] = temp;
}

bool isCycle(int start, int finish)
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (locked[start][i])
        {
            if (i == finish || isCycle(i, finish))
            {
                return true;
            }
        }
    }
    return false;
}

int traverseToTop(int start)
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (locked[i][start])
        {
            return traverseToTop(i);
        }
    }
    return start;
}

void printGraph(void)
{
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            printf("%s ", locked[i][j] ? "true " : "false");
        }
        printf("\n");
    }
}

/*
void testSort(void) {
    for (int i = 0; i < candidate_count * (candidate_count - 1) / 2; i++) {
        pair curr = { .winner = i, .loser = i, .strength = (i - candidate_count) * (i - candidate_count)};
        pairs[i] = curr;
    }
    pair_count = candidate_count * (candidate_count - 1) / 2;
    sort_pairs();
}
*/