#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>

typedef uint8_t BYTE;

bool checkJPEG(BYTE *ptr);

const int BLOCK_SIZE = 512;

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        printf("Usage: ./recover IMAGE\n");
        return 1;
    }
    if (argv[1] == NULL)
    {
        printf("Invalid File\n");
        return 1;
    }

    FILE *file = fopen(argv[1], "r");
    if (file == NULL)
    {
        printf("Could not open File\n");
        return 1;
    }

    int image_number = 0;
    BYTE *buffer = malloc(BLOCK_SIZE * sizeof(BYTE));
    char *label = malloc(8 * sizeof(char));
    FILE *new_image = NULL;
    while (fread(buffer, 1, BLOCK_SIZE, file) == BLOCK_SIZE)
    {
        if (checkJPEG(buffer))
        {
            sprintf(label, "%03d.jpg", image_number);
            image_number++;
            //printf("%i\n", image_number);

            if (new_image != NULL)
            {
                fclose(new_image);
            }
            new_image = fopen(label, "w");
            if (new_image == NULL)
            {
                printf("Could not create a new image %s\n", label);
            }
        }
        if (new_image != NULL)
        {
            fwrite(buffer, 1, BLOCK_SIZE, new_image);
        }

    }
    free(buffer);
    free(label);
    if (new_image != NULL)
    {
        fclose(new_image);
    }
    if (file != NULL)
    {
        fclose(file);
    }
    return 0;

}


bool checkJPEG(BYTE *ptr)
{
    if (ptr == NULL)
    {
        printf("Unexpected Null Pointer\n");
        return false;
    }
    if (*ptr != 0xFF || *(ptr + 1) != 0xD8 || *(ptr + 2) != 0xFF) // First three bytes
    {
        return false;
    }
    if (*(ptr + 3) / 16 != 14) // Last byte
    {
        return false;
    }
    return true;
}