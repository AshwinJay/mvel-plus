package com.javaforu.util.tuple;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class Quad<X> implements Tuple<X> {
    protected X c0;

    protected X c1;

    protected X c2;

    protected X c3;

    public Quad() {
    }

    public Quad(X c0, X c1, X c2) {
        this.c0 = c0;
        this.c1 = c1;
        this.c2 = c2;
    }

    public Quad(X c0, X c1, X c2, X c3) {
        this.c0 = c0;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    @Override
    public X get(int position) {
        switch (position) {
            case 0:
                return c0;

            case 1:
                return c1;

            case 2:
                return c2;

            case 3:
                return c3;

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

            case 2: {
                X ret = c2;
                c2 = x;
                return ret;
            }

            case 3: {
                X ret = c3;
                c3 = x;
                return ret;
            }

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void clear(X defaultValue) {
        c0 = c1 = c2 = c3 = defaultValue;
    }
}
