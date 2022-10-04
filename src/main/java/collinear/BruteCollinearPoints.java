package collinear;

import java.util.Arrays;

public class BruteCollinearPoints {
//    private double[] slopes;
    private LineSegment[] lineSegments;
    private int numOfSegments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("Points cannot be empty");
        }
        int pLength = points.length;

        numOfSegments = 0;
        lineSegments = new LineSegment[1];

        Point[] copy = copyPoints(points);
        // sort the points by y and x
        Arrays.sort(copy);

        for (int i = 0; i < pLength; i++) {
            double[] slopes = new double[pLength - i - 1];
            Point origin = copy[i];
//            if (origin == null) {
//                throw new IllegalArgumentException("Any point cannot be null");
//            }
            // determine the slopes from the original to other points
            for (int j = i + 1; j < pLength; j++) {
                double slope = origin.slopeTo(copy[j]);
                if (slope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("Repeated points");
                }
                slopes[j-i-1] = slope;
            }
            // iterate the slopes to check the line segments
            for (int k = 0; k < slopes.length; k++) {
                double slope = slopes[k];
                int n = 1;
                for (int m = k + 1; m < slopes.length; m++) {
                    if (slope == slopes[m]) {
                        n++;
                    }
                    if (n >= 3) {
                        if (numOfSegments == lineSegments.length) enlargeLineSegments(2 * lineSegments.length);
                        lineSegments[numOfSegments++] = new LineSegment(origin, copy[m + i + 1]);
                        break;
                    }
                }
            }
        }
    }

    private Point[] copyPoints(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = points[i];
        }
        return copy;
    }
    private void enlargeLineSegments(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < numOfSegments; i++) {
            copy[i] = lineSegments[i];
        }
        lineSegments = copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[numOfSegments];
        for (int i = 0; i < numOfSegments; i++) {
            result[i] = lineSegments[i];
        }
        return result;
    }

    public static void main(String[] args) {
        Point[] points = { new Point(10000, 0), new Point(0, 10000), new Point(3000, 7000),
                            new Point(7000, 3000), new Point(20000, 21000), new Point(3000, 4000),
                            new Point(14000, 15000), new Point(6000, 7000)};
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        System.out.println(bruteCollinearPoints.numOfSegments);
        for (LineSegment ls: bruteCollinearPoints.segments()) {
            System.out.println(ls);
        }
    }

}
