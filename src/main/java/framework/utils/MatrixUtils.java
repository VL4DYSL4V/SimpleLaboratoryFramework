package framework.utils;

import framework.exception.LaboratoryFrameworkException;
import org.apache.commons.math3.linear.*;

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

    public static void swapRows(RealMatrix matrix, int index1, int index2) {
        ValidationUtils.requireNonNull(matrix);
        String messageTemplate = "Matrix row dimension must be > index%d";
        ValidationUtils.requireGreaterThan(matrix.getRowDimension(), index1, String.format(messageTemplate, 1));
        ValidationUtils.requireGreaterThan(matrix.getRowDimension(), index2, String.format(messageTemplate, 2));

        RealVector row1 = new ArrayRealVector(matrix.getRowVector(index1));
        RealVector row2 = new ArrayRealVector(matrix.getRowVector(index2));
        matrix.setRow(index1, row2.toArray());
        matrix.setRow(index2, row1.toArray());
    }

    public static void swapColumns(RealMatrix matrix, int index1, int index2) {
        ValidationUtils.requireNonNull(matrix);
        String messageTemplate = "Matrix column dimension must be > index%d";
        ValidationUtils.requireGreaterThan(matrix.getColumnDimension(), index1, String.format(messageTemplate, 1));
        ValidationUtils.requireGreaterThan(matrix.getColumnDimension(), index2, String.format(messageTemplate, 2));

        RealVector column1 = new ArrayRealVector(matrix.getColumn(index1));
        RealVector column2 = new ArrayRealVector(matrix.getColumn(index2));
        matrix.setColumn(index1, column2.toArray());
        matrix.setColumn(index2, column1.toArray());
    }
}
