package assignment;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardInputs {
    public static String flattenInputs(ArrayList<int[]> inputs, Double value) {
        StringBuilder output = new StringBuilder();
        String delimiter = ",";
        for (int[] a : inputs) {
            // System.out.println(Arrays.toString(a));
            for (int b : a) {
                output.append(b);
                output.append(delimiter);
            }
        }
        output.append(value);
        return output.toString();
    }

}