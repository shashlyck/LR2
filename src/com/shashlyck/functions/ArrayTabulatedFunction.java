package com.shashlyck.functions;
import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction{
    private double[] xValues;
    private double[] yValues;
    private int count;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {

        count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {

        this.count = count;
        if (xFrom > xTo) {
            double xOdds;
            xOdds = xFrom - xTo;
            xTo = xTo + xOdds;
            xFrom = xFrom - xOdds;
        }

        xValues = new double[count];
        yValues = new double[count];
        double step = (xTo - xFrom) / (count - 1);
        double adding = xFrom;
        if (xFrom == xTo) {
            double functionXFrom = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom;
                yValues[i] = functionXFrom;
                adding = adding + step;
            }
        } else {
            for (int i = 0; i < count; i++) {
                xValues[i] = adding;
                yValues[i] = source.apply(adding);
                adding = adding + step;
            }

        }
    }

    public ArrayTabulatedFunction() {
        xValues = new double[]{};
        yValues = new double[]{};
        count = 0;
    }

    public static TabulatedFunction getIdentity() {
        return new ArrayTabulatedFunction();
    }

    public int getCount() {
        return count;
    }

    public double getX(int index){

        return xValues[index];
    }

    public double getY(int index){
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("index went beyond");
        }
        return yValues[index];
    }

    public void setY(int index, double value){

        yValues[index] = value;
    }

    public double leftBound() {
        return xValues[0];
    }

    public double rightBound() {
        return xValues[count - 1];
    }

    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (xValues[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (yValues[i] == y) {
                return i;
            }
        }
        return -1;
    }

    protected int floorIndexOfX(double x){

        for (int i = 0; i < count; i++) {
            if (xValues[i] > x) {
                return i - 1;
            }
        }
        return count;
    }

    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    protected double interpolate(double x, int floorIndex) {

        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }
}
