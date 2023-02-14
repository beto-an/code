package assignment;


import java.util.*;

public class QueryProcessor {

    public static final Operation OR = new Operation('|');

    public static final Operation AND = new Operation('&');

    public static final Operation PAREN = new Operation('(');

    /**
     * Turns an infix ordered query into a postfix ordered query (Shunting Yard's)
     * @param query Query to modify
     * @return a stack of QueryElements, in postfix ordering
     */
    public static ArrayList<QueryElement> createQueryStack(String query) {
        ArrayList<QueryElement> operatorStack = new ArrayList<>();
        ArrayList<QueryElement> outputStack = new ArrayList<>();


        int level = 0;
        boolean isQuoteOpen = false;
        boolean lastCharOperator = true;
        boolean nextWordPositive = true;

        ArrayList<String> lastPhrase = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(addSpaces(query)); // Add extraneous spaces to all the tokenizer to detect all operator tokens

        while (st.hasMoreTokens()) {
            String token = st.nextToken().toLowerCase();
            if (!lastCharOperator && level == 0 && !token.equals("|") && !token.equals("&")) {  // implicit AND
                operatorStack.add(AND);
            }
            if (token.equals("|")) {  // OR
                operatorStack.add(OR);
                lastCharOperator = true;

            } else if (token.equals("&")) {  // AND
                operatorStack.add(AND);
                lastCharOperator = true;

            } else if (token.equals("(")) {  // OPEN PARENTHESIS
                operatorStack.add(PAREN);
                lastCharOperator = true;
                level++;

            } else if (token.equals(")")) {  // CLOSE PARENTHESIS: take everything off the opartor stack until an opening parenthesis is found
                while (operatorStack.get(operatorStack.size() - 1) != PAREN) {
                    outputStack.add(operatorStack.remove(operatorStack.size() - 1));
                }
                operatorStack.remove(operatorStack.size() - 1);
                lastCharOperator = false;
                level--;

            } else if (token.equals("\"")) {  // Quotation: If open, mark it as opening, if close, create a Phrase
                if (isQuoteOpen) {
                    outputStack.add(new Phrase(lastPhrase));
                    lastPhrase = new ArrayList<>();
                    level--;
                    lastCharOperator = false;
                } else {
                    level++;
                    lastCharOperator = true;
                }
                isQuoteOpen = !isQuoteOpen;

            } else if (isQuoteOpen) {
                lastPhrase.add(token);
                lastCharOperator = false;

            } else if (token.equals("!")){  // NEGATION OPERATOR: modify field value
                nextWordPositive = false;
                lastCharOperator = true;

            } else {  // Regular word creation
                outputStack.add(new Word(token, nextWordPositive));
                nextWordPositive = true;
                lastCharOperator = false;
            }
        }
        while (operatorStack.size() > 0) {  // Empty the rest of the operator stack onto the output stack
            outputStack.add(operatorStack.remove(operatorStack.size() - 1));
        }
        return outputStack;
    }

    public static List<Page> queryPages(ArrayList<QueryElement> outputStack, WebIndex index) {
        int i = 0;
        ArrayList<QueryElement> wordStack = new ArrayList<>();
        do {
            // Use postfix ordering to quickly compute the query
            // If operator, compute operation and add back to compute stack
            // If word or phrase, add to compute stack
            QueryElement currentToken = outputStack.get(i);
            if (currentToken.equals(AND)) {
                QueryElement q1 = wordStack.remove(wordStack.size() - 1);
                wordStack.get(wordStack.size() - 1).intersect(q1);
            } else if (currentToken.equals(OR)) {
                QueryElement q1 = wordStack.remove(wordStack.size() - 1);
                wordStack.get(wordStack.size() - 1).union(q1);
            } else {
                currentToken.setIndex(index);
                wordStack.add(currentToken);
            }
            i++;
        } while (i < outputStack.size());
        if (wordStack.size() != 1) { // Size should be 1 at the end, with all the valid pages satisfying the overall query
            System.err.println("Invalid Query");
            return new ArrayList<>();
        }
        return new ArrayList<>(wordStack.get(0).getValidPages());
    }

    public static void printErrorLocation(String query, int location) {
        System.err.println(query);
        for (int j = 0; j <location; j++) {
            System.err.print(" ");
        }
        System.err.print("^");
    }

    /**
     * Create extraneous spaces to allow token separation
     * @param query input query
     * @return query with more spaces
     */
    private static String addSpaces(String query) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '&' || query.charAt(i) == '|' || query.charAt(i) == '(' || query.charAt(i) == ')' || query.charAt(i) == '!' || query.charAt(i) == '\"') {  // Add space before and after if operator symbol
                result.append(' ').append(query.charAt(i)).append(' ');
            } else {
                result.append(query.charAt(i));
            }
        }
        return result.toString();
    }

}


