package com.javaforu.util.tuple;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */

/**
 * A utility class for creating various length {@link Tuple}s.
 */
public class Tuples {
    public static <X> Tuple<X> newTuple(X c0, X c1) {
        return new Pair<>(c0, c1);
    }

    public static <X> Tuple<X> newTuple(X c0, X c1, X c2) {
        return new Triple<>(c0, c1, c2);
    }

    public static <X> Tuple<X> newTuple(X c0, X c1, X c2, X c3) {
        return new Quad<>(c0, c1, c2, c3);
    }

    @SafeVarargs
    public static <X> Tuple<X> newTuple(X... xes) {
        return new LargeFlexTuple<>(xes);
    }

    public static <X> Tuple<X> newTuple(int capacity) {
        switch (capacity) {
            case 2:
                return newTuple(null, null);

            case 3:
                return newTuple(null, null, null);

            case 4:
                return newTuple(null, null, null, null);

            default:
                return new LargeFlexTuple<>(capacity);
        }
    }
}
