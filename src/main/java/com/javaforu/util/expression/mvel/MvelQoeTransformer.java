package com.javaforu.util.expression.mvel;

import com.javaforu.util.annotation.Nullable;
import com.javaforu.util.expression.BinaryOperator;
import com.javaforu.util.expression.QuasiOptimizedExpression;
import com.javaforu.util.expression.QuasiOptimizedExpression.OptimizedComponent;
import com.javaforu.util.expression.QuasiOptimizedExpression.UnOptimizedComponent;
import com.javaforu.util.expression.RelationalOperator;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.ast.*;
import org.mvel2.compiler.ExecutableAccessor;
import org.mvel2.compiler.ExecutableAccessorSafe;
import org.mvel2.compiler.ExecutableStatement;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class MvelQoeTransformer {
    public MvelQoeTransformer() {
    }

    public QuasiOptimizedExpression transform(ASTNode astNode) {
        QuasiOptimizedExpression oqe = new QuasiOptimizedExpression();

        visit(oqe, astNode);

        oqe.done();

        if (oqe.getOperator() == null) {
            oqe.setOperator(BinaryOperator.and);
        }

        return oqe;
    }

    protected void visit(QuasiOptimizedExpression optimizedExpression, ASTNode node) {
        node = extractOrSimplifyToNode(node);

        //todo Check if constant like (a < 10 && false)

        if (node instanceof BinaryOperation) {
            if (visitBinaryOperation(optimizedExpression, (BinaryOperation) node)) {
                return;
            }
        }
        else if (node instanceof BooleanNode) {
            BooleanNode booleanNode = (BooleanNode) node;

            if (goInside(booleanNode)) {
                ASTNode left = extractOrSimplifyToNode(booleanNode.getLeft());
                ASTNode right = extractOrSimplifyToNode(booleanNode.getRight());

                BinaryOperator operator = map(booleanNode);
                BinaryOperator onStackOperator = optimizedExpression.getOperator();

                //Hmmm.. previous item on stack is similar, so let's see if we are also like that.
                if (onStackOperator == null || operator.equals(onStackOperator)) {
                    if (onStackOperator == null) {
                        optimizedExpression.setOperator(operator);
                    }

                    /*
                    Break up and add components at same level to the list:

                    From this: ( (a > 10 && b == 10) && (a > x || a % x == 0) ) && (b < 878)
                    To this: (a > 10) && (b == 10) && (a > x || a % x == 0) && (b < 878)
                    */

                    visit(optimizedExpression, left);

                    visit(optimizedExpression, right);

                    return;
                }
            }
        }

        //Default.
        String s = decompile(node, null);

        optimizedExpression.addExpressionComponent(new UnOptimizedComponent(s));
    }

    protected boolean goInside(BooleanNode booleanNode) {
        return map(booleanNode) != null;
    }

    protected boolean visitBinaryOperation(QuasiOptimizedExpression optimizedExpression,
                                           BinaryOperation binaryOperation) {
        /*
        todo Change !( a == b) to (a != b).
        Currently Negation cannot handle this - [mvel-user] Access to org.mvel2.ast.Negation's internal statement | http://markmail.org/message/i6sevfzjw7uma6gc
        */

        RelationalOperator relationalOperator = map(binaryOperation);
        if (relationalOperator == null) {
            return false;
        }

        String theExpression = null;

        //a.b > 10
        Object theLiteral = extractLiteral(binaryOperation.getRight());
        if (theLiteral == AstNodeType.other) {
            //10 <= a.b
            theLiteral = extractLiteral(binaryOperation.getLeft());

            //10 == 10 will never reach here because MVEL optimizes it immediately to a literal.
            if (theLiteral == AstNodeType.other) {
                return false;
            }
            else {
                theExpression = decompile(binaryOperation.getRight());

                relationalOperator = relationalOperator.inverse();
            }
        }
        else {
            theExpression = decompile(binaryOperation.getLeft());
        }

        OptimizedComponent oc = new OptimizedComponent(theExpression, relationalOperator, theLiteral);
        optimizedExpression.addExpressionComponent(oc);

        return true;
    }

    /**
     * @param lhsOrRhs
     * @return {@link AstNodeType#other} or a literal value (null allowed).
     */
    protected Object extractLiteral(ASTNode lhsOrRhs) {
        if (lhsOrRhs.isLiteral()) {
            return lhsOrRhs.getLiteralValue();
        }
        else if (lhsOrRhs.getEgressType().isEnum()) {
            Object pc = lhsOrRhs.getLiteralValue();
            if (!(pc instanceof ParserContext)) {
                return AstNodeType.other;
            }

            String expressionString = lhsOrRhs.getName();
            Object rightValueExtractor = MVEL.compileExpression(expressionString, (ParserContext) pc);
            Object rightLiteralValue = MVEL.executeExpression(rightValueExtractor);

            if (rightLiteralValue instanceof Enum) {
                return rightLiteralValue;
            }
        }

        return AstNodeType.other;
    }

    protected String decompile(ASTNode lhsOrRhs) {
        ASTNode stopBefore = lhsOrRhs.nextASTNode;
        if (lhsOrRhs instanceof BooleanNode) {
            stopBefore = ((BooleanNode) lhsOrRhs).getRightMost();
            stopBefore = stopBefore.nextASTNode;
        }

        return decompile(lhsOrRhs, stopBefore);
    }

    private String decompile(ASTNode startAt, @Nullable ASTNode stopBefore) {
        StringBuilder sb = new StringBuilder();

        new ASTNodeDecompiler().walk(startAt, stopBefore, sb);

        return sb.toString().trim();
    }

    /**
     * @param that
     * @return null if it cannot be extracted. Same as what was sent if it already is an {@link ASTNode}.
     */
    @Nullable
    protected ASTNode extractOrSimplifyToNode(Object that) {
        if (that instanceof ExecutableAccessor) {
            ExecutableAccessor ea = (ExecutableAccessor) that;

            return extractOrSimplifyToNode(ea.getNode());
        }
        else if (that instanceof ExecutableAccessorSafe) {
            ExecutableAccessorSafe eas = (ExecutableAccessorSafe) that;

            return extractOrSimplifyToNode(eas.getNode());
        }
        else if (that instanceof Substatement) {
            Substatement substatement = (Substatement) that;
            ExecutableStatement executableStatement = substatement.getStatement();

            return extractOrSimplifyToNode(executableStatement);
        }
        else if (that instanceof ASTNode) {
            return (ASTNode) that;
        }

        return null;
    }

    static BinaryOperator map(BooleanNode booleanNode) {
        if (booleanNode instanceof And) {
            return BinaryOperator.and;
        }
        else if (booleanNode instanceof Or) {
            return BinaryOperator.or;
        }

        return null;
    }

    static RelationalOperator map(BinaryOperation binaryOperation) {
        switch (binaryOperation.getOperation()) {
            //Strange compilation error - "cannot find symbol org.mve2.Operator"
            //Similar? http://drools.46999.n3.nabble.com/Dependency-management-td2893027.html

            case 18: //org.mvel2.Operator.EQUAL:
                return RelationalOperator.eq;

            case 14: //org.mvel2.Operator.LTHAN:
                return RelationalOperator.lt;

            case 15: //org.mvel2.Operator.GTHAN:
                return RelationalOperator.gt;

            case 16: //org.mvel2.Operator.LETHAN:
                return RelationalOperator.le;

            case 17: //org.mvel2.Operator.GETHAN:
                return RelationalOperator.ge;

            default:
                return null;
        }
    }
}
