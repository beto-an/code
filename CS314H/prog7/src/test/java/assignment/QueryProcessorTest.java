package assignment;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QueryProcessorTest implements Serializable {

    @Test
    void createQueryStack() {
        String[][] expected = {
                {"apple", "banana", "pear", "limo", "|", "\"[plaster, goat]\"", "&", "&", "&"},
                {"!apple", "banana", "pear", "limo", "|", "\"[plaster, goat]\"", "!giraffe", "polar", "bear", "\"[polar, bear]\"", "|", "&", "&", "&", "&", "&", "&"},
                {"apple", "war", "|"},
        };
        String[] queries = {
                "apple banana ( pear | limo ) \" plaster goat \"",
                "!apple banana ( pear | limo ) \" plaster goat \" & !giraffe ( polar & bear | \" polar bear \" )",
                "apple | war",
        };
        for (int i = 0; i < queries.length; i++) {
            checkQueryStackEquality(expected[i], QueryProcessor.createQueryStack(queries[i]));
        }
    }


    void checkQueryStackEquality(String[] expected, ArrayList<QueryElement> actual) {
        assertEquals(expected.length, actual.size(), "Expected Size: " + expected.length + ", Actual Size: " + actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual.get(i).toString(), "Stack element " + i + ": Expected " + expected[i] + ", Actual: " + actual.get(i).toString());
        }
    }
}
