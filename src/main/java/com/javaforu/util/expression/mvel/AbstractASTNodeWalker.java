package com.javaforu.util.expression.mvel;

import com.javaforu.util.annotation.Nullable;
import org.mvel2.ast.*;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.util.ASTIterator;
import org.mvel2.util.ASTLinkedList;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public abstract class AbstractASTNodeWalker<C> {
    public AbstractASTNodeWalker() {
    }

    public void walk(ASTNode startAt, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
        ASTIterator iterator = new ASTLinkedList(startAt);

        while (iterator.hasMoreNodes()) {
            startAt = iterator.nextNode();

            if (startAt == stopBefore) {
                break;
            }

            if (startAt instanceof NestedStatement) {
                NestedStatement nestedStatement = (NestedStatement) startAt;
                CompiledExpression ce =
                        tryGetCompiledExpression(startAt, nestedStatement.getNestedStatement(), stopBefore);
                if (ce == null) {
                    visitUnCompiledNestedStatement(startAt, nestedStatement, stopBefore, context);
                }
                else {
                    visitCompiledNestedStatement(startAt, ce, stopBefore, context);
                }
            }
            if (startAt instanceof Substatement) {
                Substatement substatement = (Substatement) startAt;
                CompiledExpression ce = tryGetCompiledExpression(startAt, substatement.getStatement(), stopBefore);
                if (ce == null) {
                    visitUnCompiledSubtatement(startAt, substatement, stopBefore, context);
                }
                else {
                    visitCompiledSubstatement(startAt, ce, stopBefore, context);
                }
            }
            else if (startAt instanceof Function) {
                visitFunction((Function) startAt, stopBefore, context);
            }
            else if (startAt.isDebuggingSymbol()) {
                visitDebuggingSymbol(startAt, stopBefore, context);
            }
            else if (startAt.isLiteral()) {
                visitLiteralNode((LiteralNode) startAt, stopBefore, context);
            }
            else if (startAt.isOperator()) {
                visitOperator(startAt, stopBefore, context);
            }
            else if (startAt.isIdentifier()) {
                visitIdentifier(startAt, stopBefore, context);
            }
            else if (startAt instanceof BinaryOperation) {
                visitBinaryOperation((BinaryOperation) startAt, stopBefore, context);
            }
            else if (startAt instanceof BooleanNode) {
                visitBooleanNode((BooleanNode) startAt, stopBefore, context);
            }
            else if (startAt instanceof Negation) {
                visitNegation((Negation) startAt, stopBefore, context);
            }
            else {
                visitUnknown(startAt, stopBefore, context);
            }
        }
    }

    /**
     * @param ownerNode
     * @param es
     * @param stopBefore
     * @return The {@link CompiledExpression} if the given {@link ExecutableStatement} can be cast. Null otherwise.
     */
    @Nullable
    protected org.mvel2.compiler.CompiledExpression tryGetCompiledExpression(ASTNode ownerNode, ExecutableStatement es,
                                                                             @Nullable
                                                                             org.mvel2.ast.ASTNode stopBefore) {
        if (es instanceof CompiledExpression) {
            return (CompiledExpression) es;
        }

        return null;
    }

    protected void visitUnknown(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    protected void visitFunction(Function node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    protected void visitNegation(Negation node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    /**
     * If overriding, call super.
     *
     * @param node
     * @param stopBefore
     * @param context
     */
    protected void visitBooleanNode(BooleanNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
        //No need to visit right as the entire leaf level gets visited.
        walk(node.getLeft(), stopBefore, context);
    }

    protected void visitBinaryOperation(BinaryOperation node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
        visitBooleanNode(node, stopBefore, context);
    }

    protected void visitIdentifier(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    protected void visitOperator(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    protected void visitLiteralNode(LiteralNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    protected void visitUnCompiledSubtatement(ASTNode ownerNode, Substatement substatement,
                                              @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    /**
     * If overriding, call super.
     *
     * @param ownerNode
     * @param ce
     * @param stopBefore
     * @param context
     */
    protected void visitCompiledSubstatement(ASTNode ownerNode, CompiledExpression ce, @Nullable
    org.mvel2.ast.ASTNode stopBefore,
                                             C context) {
        walk(ce.getFirstNode(), stopBefore, context);
    }

    protected void visitUnCompiledNestedStatement(ASTNode ownerNode, NestedStatement nestedStatement,
                                                  @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }

    /**
     * If overriding, call super.
     *
     * @param ownerNode
     * @param ce
     * @param stopBefore
     * @param context
     */
    protected void visitCompiledNestedStatement(ASTNode ownerNode, CompiledExpression ce, @Nullable
    org.mvel2.ast.ASTNode stopBefore,
                                                C context) {
        walk(ce.getFirstNode(), stopBefore, context);
    }

    protected void visitDebuggingSymbol(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, C context) {
    }
}
