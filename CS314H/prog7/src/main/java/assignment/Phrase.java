package assignment;


import java.util.ArrayList;
import java.util.HashSet;


public class Phrase extends QueryElement {

    ArrayList<String> phrase;

    public Phrase(ArrayList<String> phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return "\"" + phrase + "\"";
    }

    @Override
    public void computeValidPages() {
        HashSet<Page> validPages = new HashSet<>();
        for (Page page : getSearchSet()) {
            if (getIndex().phraseInPage(phrase, page)) {
                validPages.add(page);
            }
        }
        setValidPages(validPages);
    }
}
