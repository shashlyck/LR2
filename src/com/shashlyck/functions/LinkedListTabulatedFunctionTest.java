package com.shashlyck.functions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {

    private double[] xValues = new double[]{1., 4., 7., 10., 13.};
    private double[] yValues = new double[]{4., 6., 8., 10., 12.};
    private double PRECISION = 0.1;

    private MathFunction Function = new IdentityFunction();

    private LinkedListTabulatedFunction initializeUsingArrays() {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    private LinkedListTabulatedFunction initializeUsingMathFunction() {
        return new LinkedListTabulatedFunction(Function, 1, 5, 11);
    }
}