# TODO

def main():
    text = promptText()
    counter = countText(text)
    grade = round(5.88 * counter[2] / counter[0] - 29.6 * counter[1] / counter[0] - 15.8)
    if (grade < 1):
        print("Before Grade 1")
    elif (grade >= 16):
        print("Grade 16+")
    else:
        print(f"Grade {grade}")


def promptText():
    return input("Text: ")


def countText(text):
    counter = [0 for i in range(3)]
    for c in text:
        if (c == ' '):
            counter[0] += 1
        elif (isEnding(c)):
            counter[1] += 1
        elif (isLetter(c)):
            counter[2] += 1
    counter[0] += 1
    return counter


def isLetter(c):
    if (c >= 'A' and c <= 'Z'):
        return True
    if (c >= 'a' and c <= 'z'):
        return True
    return False


def isEnding(c):
    if (c == '.' or c == '!' or c == '?'):
        return True
    return False


if __name__ == "__main__":
    main()