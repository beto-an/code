package assignment;

public class Operation extends QueryElement {

    char indicator;

    public Operation(char indicator) {
        this.indicator = indicator;
    }

    public boolean equals(char indicator) {
        return this.indicator == indicator;
    }

    @Override
    public String toString() {
        return String.valueOf(indicator);
    }
}
