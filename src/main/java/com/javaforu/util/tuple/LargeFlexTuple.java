package com.javaforu.util.tuple;

import java.util.Arrays;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class LargeFlexTuple<X> implements Tuple<X> {
    protected final Object[] array;

    public LargeFlexTuple(int capacity) {
        this.array = new Object[capacity];
    }

    public LargeFlexTuple(X[] array) {
        this.array = array;
    }

    @Override
    public int getCapacity() {
        return array.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public X get(int position) {
        return (X) array[position];
    }

    @SuppressWarnings("unchecked")
    @Override
    public X set(int position, X x) {
        X ret = (X) array[position];

        array[position] = x;

        return ret;
    }

    @Override
    public void clear(X defaultValue) {
        Arrays.fill(array, defaultValue);
    }
}
