# TODO

def main():
    ccnumber = promptCCNumber()
    if (not luhnCheck(ccnumber)):
        print("INVALID")
    elif (isVisa(ccnumber)):
        print("VISA")
    elif (isMaster(ccnumber)):
        print("MASTERCARD")
    elif (isAMEX(ccnumber)):
        print("AMEX")
    else:
        print("INVALID")


def promptCCNumber():
    num_input = input("Number: ")
    if (not num_input.isdigit()):
        return promptCCNumber()
    return int(num_input)


def luhnCheck(c):
    sum = 0
    digit = False
    while (c != 0):
        if (digit):
            sum += sumOfDigits(2 * (c % 10))
        else:
            sum += (c % 10)
        c //= 10
        digit = not digit
    return (sum % 10 == 0)


def sumOfDigits(n):
    sum = 0
    while (n != 0):
        sum += (n % 10)
        n //= 10
    return sum


def isVisa(c):
    if (c < 10**12 or c >= 10**16):
        return False
    while (c >= 10):
        c //= 10
    if (c == 4):
        return True
    return False


def isMaster(c):
    firstTwoDigits = c // (10**14)
    if (firstTwoDigits >= 51 and firstTwoDigits <= 55):
        return True
    return False


def isAMEX(c):
    firstTwoDigits = c // (10**13)
    if (firstTwoDigits == 34 or firstTwoDigits == 37):
        return True
    return False


if __name__ == "__main__":
    main()