package com.javaforu.util.tuple;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
/**
 * A generic class to represent a row with the specified number of type safe columns.
 *
 * @param <X>
 */
public interface Tuple<X> {
    /**
     * @return The maximum number of items this tuple is capable of holding.
     */
    int getCapacity();

    /**
     * @param position
     * @return Item at the requested position.
     */
    X get(int position);

    /**
     * Sets the given item at the specified position.
     *
     * @param position
     * @param x
     * @return The previous value. Null if it was never set.
     */
    X set(int position, X x);

    /**
     * Resets all the positions to the given default value.
     *
     * @param defaultValue Ex: Pass {@code null} to set all positions to null.
     */
    void clear(X defaultValue);
}
