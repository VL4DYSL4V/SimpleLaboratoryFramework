package framework.utils;

import framework.exception.LaboratoryFrameworkException;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MatrixUtils {

    private MatrixUtils() {
    }

    public static Map<Integer, RealMatrix> getPowerToMatrixInThatPower(RealMatrix matrix, int q)
            throws LaboratoryFrameworkException {
        ValidationUtils.requireEquals(matrix.getRowDimension(), matrix.getColumnDimension(),
                "Dimensions of matrix must be equal");
        Map<Integer, RealMatrix> iterationStepToMatrix = new HashMap<>();
        DiagonalMatrix elementaryMatrix = getElementaryMatrix(matrix.getRowDimension());
        iterationStepToMatrix.put(0, elementaryMatrix);
        iterationStepToMatrix.put(1, matrix);
        for (int i = 2; i <= q; i++) {
            RealMatrix computedOnPreviousStep = iterationStepToMatrix.get(i - 1);
            RealMatrix computedOnThisStep = computedOnPreviousStep.multiply(matrix);
            iterationStepToMatrix.put(i, computedOnThisStep);
        }
        return iterationStepToMatrix;
    }

    public static DiagonalMatrix getElementaryMatrix(int dimension) {
        double[] arrayOfOnes = new double[dimension];
        Arrays.fill(arrayOfOnes, 1);
        return new DiagonalMatrix(arrayOfOnes);
    }

}
