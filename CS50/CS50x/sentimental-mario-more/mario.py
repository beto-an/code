def main():
    height = promptHeight()

    for i in range(1, height + 1):
        for j in range(height - i):
            print(" ", end="")
        for j in range(i):
            print("#", end="")
        print("  ", end="")
        for j in range(i):
            print("#", end="")
        print()


def promptHeight():
    height_string = input("Height: ")
    if (not height_string.isdigit() or height_string == ""):
        return promptHeight()
    height = int(height_string)
    if (height < 1 or height > 8):
        return promptHeight()
    return height


if __name__ == "__main__":
    main()