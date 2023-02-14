package assignment;

import java.util.HashSet;

public class Word extends QueryElement {

    private String word;

    private boolean positive;

    public Word(String word, boolean positive) {
        this.word = word;
        this.positive = positive;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    @Override
    public String toString() {
        if (positive) {
            return word;
        } else {
            return '!' + word;
        }
    }


    @Override
    public void computeValidPages() {
        HashSet<Page> validPages = new HashSet<>();
        if (positive) {
            for (Page page : getSearchSet()) {
                if (getIndex().wordInPage(word, page)) {
                    validPages.add(page);
                }
            }
        } else {
            for (Page page : getSearchSet()) {
                if (!getIndex().wordInPage(word, page)) {
                    validPages.add(page);
                }
            }
        }
        setValidPages(validPages);
    }
}
