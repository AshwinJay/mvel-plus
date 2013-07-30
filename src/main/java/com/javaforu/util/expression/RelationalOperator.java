package com.javaforu.util.expression;

import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.javaforu.util.tuple.Pair;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public enum RelationalOperator implements Predicate<Pair<? extends Comparable>> {
    eq {
        @Override
        public RelationalOperator inverse() {
            return eq;
        }

        @Override
        public boolean apply(Pair<? extends Comparable> pair) {
            int i = simpleCompare(pair);

            return i == 0;
        }
    },
    gt {
        @Override
        public RelationalOperator inverse() {
            return lt;
        }

        @Override
        public boolean apply(Pair<? extends Comparable> pair) {
            int i = simpleCompare(pair);

            return i > 0;
        }
    },
    ge {
        @Override
        public RelationalOperator inverse() {
            return le;
        }

        @Override
        public boolean apply(Pair<? extends Comparable> pair) {
            int i = simpleCompare(pair);

            return i >= 0;
        }
    },
    lt {
        @Override
        public RelationalOperator inverse() {
            return gt;
        }

        @Override
        public boolean apply(Pair<? extends Comparable> pair) {
            int i = simpleCompare(pair);

            return i < 0;
        }
    },
    le {
        @Override
        public RelationalOperator inverse() {
            return ge;
        }

        @Override
        public boolean apply(Pair<? extends Comparable> pair) {
            int i = simpleCompare(pair);

            return i <= 0;
        }
    };

    static int simpleCompare(Pair<? extends Comparable> pair) {
        return ComparisonChain
                .start()
                .compare(pair.get(0), pair.get(1), Ordering.natural().nullsLast())
                .result();
    }

    public abstract RelationalOperator inverse();
}
