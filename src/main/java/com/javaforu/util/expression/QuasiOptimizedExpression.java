package com.javaforu.util.expression;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class QuasiOptimizedExpression {
    protected BinaryOperator operator;

    protected LinkedList<ExpressionComponent> expressionComponents;

    public QuasiOptimizedExpression() {
        this.expressionComponents = new LinkedList<ExpressionComponent>();
    }

    public void setOperator(BinaryOperator operator) {
        this.operator = operator;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public List<ExpressionComponent> getExpressionComponents() {
        return expressionComponents;
    }

    public void addExpressionComponent(ExpressionComponent expressionComponent) {
        expressionComponents.add(expressionComponent);
    }

    public void done() {
        Collections.sort(expressionComponents, new ExpressionComponentComparator());
    }

    @Override
    public String toString() {
        return "{" + "operator=" + operator + ", expressionComponents=" + expressionComponents + '}';
    }

    //---------------

    public interface ExpressionComponent {
    }

    public static class UnOptimizedComponent implements ExpressionComponent {
        protected Object expression;

        public UnOptimizedComponent(Object expression) {
            this.expression = expression;
        }

        public Object getExpression() {
            return expression;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" + expression + '}';
        }
    }

    public static class OptimizedComponent implements ExpressionComponent {
        protected Object left;

        protected RelationalOperator relationalOperator;

        protected Object literalRight;

        public OptimizedComponent(Object left, RelationalOperator relationalOperator, Object literalRight) {
            this.left = left;
            this.relationalOperator = relationalOperator;
            this.literalRight = literalRight;
        }

        public Object getLeft() {
            return left;
        }

        public RelationalOperator getRelationalOperator() {
            return relationalOperator;
        }

        public Object getLiteralRight() {
            return literalRight;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{'" + left + "' " + relationalOperator + ' ' + literalRight + '}';
        }
    }

    public static class ExpressionComponentComparator implements Comparator<ExpressionComponent> {
        @Override
        public int compare(ExpressionComponent o1, ExpressionComponent o2) {
            if (o1 instanceof OptimizedComponent && o2 instanceof OptimizedComponent) {
                return ((OptimizedComponent) o1).relationalOperator
                        .compareTo(((OptimizedComponent) o2).relationalOperator);
            }

            return o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
        }
    }
}
