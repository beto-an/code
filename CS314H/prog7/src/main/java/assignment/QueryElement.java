package assignment;


import java.util.ArrayList;

import java.util.HashSet;


public class QueryElement {

    private WebIndex index;

    private HashSet<Page> validPages = new HashSet<>();

    private HashSet<Page> searchSet;

    public void setIndex(WebIndex index) {
        this.index = index;
        setSearchSet(index.getPages());
        computeValidPages();
    }

    public WebIndex getIndex() {
        return index;
    }

    public void setSearchSet(HashSet<Page> searchSet) {
        this.searchSet = searchSet;
    }

    public HashSet<Page> getSearchSet() {
        return searchSet;
    }

    public void computeValidPages() {
    }

    public HashSet<Page> getValidPages() {
        return validPages;
    }

    public void setValidPages(HashSet<Page> validPages) {
        this.validPages = validPages;
    }

    /**
     * Gives the smaller and larger QueryElements (by valid page count)
     * @param other the other QueryElement
     * @return [smaller QueryElement, larger QueryElement]
     */
    public ArrayList<HashSet<Page>> smallerLarger(QueryElement other) {
        HashSet<Page> smaller = validPages;
        HashSet<Page> larger = other.validPages;
        ArrayList<HashSet<Page>> order = new ArrayList<>();
        if (validPages.size() > other.validPages.size()) {
            larger = validPages;
            smaller = other.validPages;
        }
        order.add(smaller);
        order.add(larger);
        return order;
    }

    /**
     * Intersects the valid page sets of two QueryElements (AND)
     * @param other the other page to intersect with
     */
    public void intersect(QueryElement other) {
        ArrayList<HashSet<Page>> ordered = smallerLarger(other);
        ordered.get(0).removeIf(page -> !ordered.get(1).contains(page));
        validPages = ordered.get(0);
    }

    /**
     * Unions the valid page sets of two QueryElements (OR)
     * @param other the other page to union with
     */
    public void union(QueryElement other) {
        ArrayList<HashSet<Page>> ordered = smallerLarger(other);
        ordered.get(1).addAll(ordered.get(0));
        validPages = ordered.get(1);
    }
}