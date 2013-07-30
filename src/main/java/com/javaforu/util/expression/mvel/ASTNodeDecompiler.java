package com.javaforu.util.expression.mvel;

import com.javaforu.util.annotation.Nullable;
import org.mvel2.ast.*;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.debug.DebugTools;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class ASTNodeDecompiler extends AbstractASTNodeWalker<StringBuilder> {
    public ASTNodeDecompiler() {
    }

    @Override
    public void walk(ASTNode startAt, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        super.walk(startAt, stopBefore, context);

        context.append(' ');
    }

    @Override
    protected void visitUnknown(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ').append(node.getName());
    }

    @Override
    protected void visitNegation(Negation node, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append("!")
                .append(node.getName());
    }

    @Override
    protected void visitFunction(Function node, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append(node.getName())
                .append('(');

        int x = 0;
        for (String s : node.getParameters()) {
            if (x > 0) {
                context.append(',')
                        .append(' ');
            }
            context.append(s);

            x++;
        }

        context.append(')');
    }

    @Override
    protected void visitBooleanNode(BooleanNode node, @Nullable org.mvel2.ast.ASTNode stopBefore,
                                    StringBuilder context) {
        context.append(' ');

        super.visitBooleanNode(node, stopBefore, context);
    }

    @Override
    protected void visitIdentifier(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append(node.getName());
    }

    @Override
    protected void visitOperator(ASTNode node, @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append(DebugTools.getOperatorSymbol(node.getOperator()));
    }

    @Override
    protected void visitLiteralNode(LiteralNode node, @Nullable org.mvel2.ast.ASTNode stopBefore,
                                    StringBuilder context) {
        context.append(' ')
                .append(node.getLiteralValue());
    }

    @Override
    protected void visitUnCompiledSubtatement(ASTNode ownerNode, Substatement substatement,
                                              @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append('(')
                .append(substatement.getName())
                .append(')');
    }

    @Override
    protected void visitCompiledSubstatement(ASTNode ownerNode, CompiledExpression ce, @Nullable
    org.mvel2.ast.ASTNode stopBefore,
                                             StringBuilder context) {
        context.append(' ')
                .append('(');

        super.visitCompiledSubstatement(ownerNode, ce, stopBefore, context);

        context.append(')');
    }

    @Override
    protected void visitUnCompiledNestedStatement(ASTNode ownerNode, NestedStatement nestedStatement,
                                                  @Nullable org.mvel2.ast.ASTNode stopBefore, StringBuilder context) {
        context.append(' ')
                .append('(')
                .append(ownerNode.getName())
                .append(')');
    }

    @Override
    protected void visitCompiledNestedStatement(ASTNode ownerNode, CompiledExpression ce, @Nullable
    org.mvel2.ast.ASTNode stopBefore,
                                                StringBuilder context) {
        context.append(' ')
                .append('(');

        super.visitCompiledNestedStatement(ownerNode, ce, stopBefore, context);

        context.append(')');
    }
}
