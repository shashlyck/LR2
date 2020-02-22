package com.shashlyck.functions;

import java.io.Serializable;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction {

    private Node head;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {

        for (int i = 0; i < xValues.length; i++) {
            this.addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count){
        if (xFrom > xTo) {
            double xOdds;
            xOdds = xFrom - xTo;
            xTo = xTo + xOdds;
            xFrom = xFrom - xOdds;
        }

        double step = (xTo - xFrom) / (count - 1);
        double x = xFrom;
        if (xFrom != xTo) {
            for (int i = 0; i < count; i++) {
                this.addNode(x, source.apply(x));
                x = x + step;
            }
        } else {
            for (int i = 0; i < count; i++) {
                this.addNode(xFrom, source.apply(xFrom));
            }
        }
    }

    public static class Node implements Serializable {

        Node next;
        Node prev;
        double x;
        double y;
    }

    void addNode(double x, double y) {
        var node = new Node();
        node.x = x;
        node.y = y;
        if (head == null) {
            head = node;
            node.next = node;
            node.prev = node;
        } else {
            node.prev = head.prev;
            node.next = head;
            head.prev.next = node;
        }
        head.prev = node;
        count++;
    }

    public int getCount() {
        return count;
    }

    public double leftBound() {
        return head.x;
    }

    public double rightBound() {
        return head.prev.x;
    }


    Node getNode(int index) {
        Node node;

        if (index > (count / 2)) {
            node = head.prev;
            for (int i = count - 1; i > 0; i--) {
                if (i == index) {
                    return node;
                } else {
                    node = node.prev;
                }
            }
        } else {
            node = head;
            for (int i = 0; i < count; i++) {
                if (index == i) {
                    return node;
                } else {
                    node = node.next;
                }
            }
        }
        return node;
    }

    public double getX(int index)  {

        return getNode(index).x;
    }

    public double getY(int index){

        return getNode(index).y;
    }

    public void setY(int index, double value) {

        getNode(index).y = value;
    }

    public int indexOfX(double x) {
        Node node;
        node = head;
        for (int i = 0; i < count; i++) {
            if (node.x == x) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    public int indexOfY(double y) {
        Node node;
        node = head;
        for (int i = 0; i < count; i++) {
            if (node.y == y) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    protected int floorIndexOfX(double x){
        Node node;
        node = head;
        for (int i = 0; i < count; i++) {
            if (node.x < x) {
                node = node.next;
            } else {
                return i - 1;
            }
        }
        return getCount();
    }


    private Node floorNodeOfX(double x) throws IllegalArgumentException {
        Node adding;

        adding = head;
        for (int i = 0; i < count; i++) {
            if (adding.x < x) {
                adding = adding.next;
            } else {
                return adding.prev;
            }
        }
        return head.prev;
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, head.prev.prev.x, head.prev.x, head.prev.prev.y, head.prev.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        Node left = getNode(floorIndex);
        Node right = left.next;
        return interpolate(x, left.x, right.x, left.y, right.y);
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else try {
            return nodeOfX(x).y;
        } catch (UnsupportedOperationException e) {
            Node left = floorNodeOfX(x);
            Node right = left.next;
            return super.interpolate(x, left.x, right.x, left.y, right.y);
        }
    }

    private Node nodeOfX(double x) {
        Node node;
        node = head;
        for (int i = 0; i < count; i++) {
            if (node.x == x) {
                return node;
            } else {
                node = node.next;
            }
        }
        throw new UnsupportedOperationException();
    }
}
