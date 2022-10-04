package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.HashMap;
//import java.util.HashSet;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;
    private int numOfSegments;
//    HashMap<Point, HashSet<Double>> endPoints; // store the end points and slopes of the line segments
    private List<Point> starts = new ArrayList<>();
    private List<Point> ends = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("Points cannot be empty");
        }
        int pLength = points.length;

        numOfSegments = 0;
        lineSegments = new LineSegment[1];
//        endPoints =new HashMap<>();

        Point[] copyPoints = copyPoints(points);
        // sort the points by y and x
        Arrays.sort(copyPoints);

        for (int i = 0; i < pLength - 3; i++) {
            Point origin = copyPoints[i];
            Point[] others = new Point[pLength - i - 1];

            for (int j = 0; j < others.length; j++) {
                others[j] = copyPoints[j + i + 1];
            }
            // sort other points by slope with origin
            Arrays.sort(others, origin.slopeOrder());
            findLineSegments(origin, others);
        }
    }

    private Point[] copyPoints(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = points[i];
        }
        return copy;
    }

    private void findLineSegments(Point origin, Point[] otherPoints) {
        int n = 1;
        double slope = origin.slopeTo(otherPoints[0]);
        if (slope == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("Repeated points");
        }
        for (int k = 1; k < otherPoints.length; k++) {
            double kSlope = origin.slopeTo(otherPoints[k]);
            if (kSlope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Repeated points");
            }
            // if the slope is the same with the previous one, increment n by 1
            if (kSlope == slope) {
                n++;
                // check whether k is the last index and n is larger than 2
                if (k == otherPoints.length - 1 && n > 2) {
                    addLineSegments(origin, otherPoints[k]);
                }
            } else {
                // if the slope is different, set the slope the current one
                // add the previous point to the line segment if n is larger than 2
                if (n > 2) {
                    addLineSegments(origin, otherPoints[k - 1]);
                }
                slope = origin.slopeTo(otherPoints[k]);
                n = 1;
            }
        }
    }

//    private void addLineSegments(Point start, Point end) {
//        if (numOfSegments == lineSegments.length) enlargeLineSegments(2 * lineSegments.length);
//        double slope = start.slopeTo(end);
//        if (!endPoints.containsKey(end) || !endPoints.get(end).contains(slope)) {
//            lineSegments[numOfSegments++] = new LineSegment(start, end);
//            if (endPoints.containsKey(end)) {
//                endPoints.get(end).add(slope);
//            } else {
//                HashSet<Double> slopeSet = new HashSet<>();
//                slopeSet.add(slope);
//                endPoints.put(end, slopeSet);
//            }
//        }
//    }

    private void addLineSegments(Point start, Point end) {
        if (numOfSegments == lineSegments.length) enlargeLineSegments(2 * lineSegments.length);
        if (!lineExist(start, end)) {
            lineSegments[numOfSegments++] = new LineSegment(start, end);
            starts.add(start);
            ends.add(end);
        }
    }

    private boolean lineExist(Point start, Point end) {
        for (int i = 0; i < ends.size(); i++) {
            if ((ends.get(i) == end) && (start.slopeTo(end) == starts.get(i).slopeTo(end))){
                return true;
            }
        }
        return false;
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
//        Point[] points = {new Point(9000, 9000), new Point(8000, 8000), new Point(7000, 7000),
//                          new Point(6000, 6000), new Point(5000, 5000), new Point(4000, 4000),
//                          new Point(3000, 3000), new Point(2000, 2000), new Point(1000, 1000)};
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        System.out.println(fcp.numberOfSegments());
        for (LineSegment ls: fcp.segments()) {
            System.out.println(ls);
        }
    }
}
