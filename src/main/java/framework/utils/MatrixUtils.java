package framework.utils;

import framework.exception.LaboratoryFrameworkException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
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

    public static RealMatrix getFrobeniusMatrix(double[] coefficients) {
        if (coefficients == null || coefficients.length <= 1) {
            throw new LaboratoryFrameworkException("Invalid coefficient array");
        }
        Array2DRowRealMatrix out = new Array2DRowRealMatrix(coefficients.length, coefficients.length);
        for (int i = 0; i < coefficients.length - 1; i++) {
            double[] row = new double[coefficients.length];
            Arrays.fill(row, 0);
            row[i + 1] = 1;
            out.setRow(i, row);
        }
        double[] lastRow = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            lastRow[i] = -1 * coefficients[i];
        }
        out.setRow(coefficients.length - 1, lastRow);
        return out;
    }
}
