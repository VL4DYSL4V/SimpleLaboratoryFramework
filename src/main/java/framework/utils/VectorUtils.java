package framework.utils;

import org.apache.commons.math3.linear.RealVector;

public class VectorUtils {

    private VectorUtils(){}

    public static void swapCoordinates(RealVector vector, int index1, int index2) {
        ValidationUtils.requireNonNull(vector);
        String messageTemplate = "Vector length must be > index%d";
        ValidationUtils.requireGreaterThan(vector.getDimension(), index1, String.format(messageTemplate, 1));
        ValidationUtils.requireGreaterThan(vector.getDimension(), index2, String.format(messageTemplate, 2));

        double entry1 = vector.getEntry(index1);
        double entry2 = vector.getEntry(index2);
        vector.setEntry(index1, entry2);
        vector.setEntry(index2, entry1);
    }

}
