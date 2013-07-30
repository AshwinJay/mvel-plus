package com.javaforu.util.tuple;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class Pair<X> implements Tuple<X> {
    protected X c0;

    protected X c1;

    public Pair() {
    }

    public Pair(X c0, X c1) {
        this.c0 = c0;
        this.c1 = c1;
    }

    @Override
    public int getCapacity() {
        return 2;
    }

    @Override
    public X get(int position) {
        switch (position) {
            case 0:
                return c0;

            case 1:
                return c1;

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public X set(int position, X x) {
        switch (position) {
            case 0: {
                X ret = c0;
                c0 = x;
                return ret;
            }

            case 1: {
                X ret = c1;
                c1 = x;
                return ret;
            }

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void clear(X defaultValue) {
        c0 = c1 = defaultValue;
    }
}
