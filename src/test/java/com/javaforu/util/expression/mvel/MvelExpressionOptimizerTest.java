package com.javaforu.util.expression.mvel;

import com.javaforu.util.expression.QuasiOptimizedExpression;
import org.junit.Test;
import org.mvel2.ParserContext;
import org.mvel2.ast.ASTNode;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Ashwin Jayaprakash
 * Email: ashwin.jayaprakash@gmail.com
 * Web: http://www.ashwinjayaprakash.com
 */
public class MvelExpressionOptimizerTest {

    //todo Add proper assertions.

    @Test
    public void test1() {
        //todo Unoptimized part still needs fixing. "a % x == 0" appears twice.

        QuasiOptimizedExpression result = test("( (a > 1 && b == 1) && (a > x || a % x == 0) ) && (b < 878)");
        System.out.println(result);

        result = test("(a > 2) && (b == 2) && (a > x || a % x == 0) && (b < 878)");
        System.out.println(result);

        result = test("(a > 3) && ( (b == 3) && (a > x || a % x == 0) ) && (b < 878)");
        System.out.println(result);

        result = test("(a > 4) && ( (b == 4) && (a > x || a % x == 0) && (b < 878) )");
        System.out.println(result);

        result = test("( (a > 5) && ( (b == 5) && ( (a > x || a % x == 0) && (b < 878) ) ) )");
        System.out.println(result);

        result = test("a > 10");
        System.out.println(result);

        result = test("a & 10");
        System.out.println(result);

        result = test("a && false");
        System.out.println(result);

        result = test("a && false || b < 10");
        System.out.println(result);

        result = test("(b < 80) && ((a & 10) == 10)");
        System.out.println(result);

        result = test("((a & 10) == 10) && (b < 80)");
        System.out.println(result);

        result = test("a > 10 && b == 10");
        System.out.println(result);

        result = test("a > 10 || b == 10");
        System.out.println(result);
    }

    @Test
    public void test2() {
        QuasiOptimizedExpression result = test("(a.b > 1)");
        System.out.println(result);

        result = test("(a.b + c.d > 2)");
        System.out.println(result);

        result = test("(a.b + c.d > 2) && false && ((d < 3) || e.k >= 34)");
        System.out.println(result);

        result = test("System.out.println( \"Result: \" + (a.b + foo(c,45,Integer.toHexString(1234)) > 2) )");
        System.out.println(result);
    }

    @Test
    public void test3() {
        QuasiOptimizedExpression result = test("(a.b > 1)");
        System.out.println(result);

        result = test("(1 > 1)");
        System.out.println(result);

        result = test("123 == (123)");
        System.out.println(result);

        result = test("10 > (c.d)");
        System.out.println(result);

        result = test("!(10 < c.d)");
        System.out.println(result);

        result = test("!(x.y == c.d)");
        System.out.println(result);

        result = test("x.y != 12");
        System.out.println(result);

        result = test("12 == x.y");
        System.out.println(result);

        result = test("3323 <= x.e");
        System.out.println(result);
    }

    @Test
    public void testEnum() {
        System.out.println(SampleEnum.black instanceof Comparable);
        System.out.println(SampleEnum.black.compareTo(SampleEnum.gold));

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("card", SampleEnum.gold);

        ParserContext pc = new ParserContext();
        pc.addImport("cc", SampleEnum.class);
        pc.setStrictTypeEnforcement(true);

        ExpressionCompiler e = new ExpressionCompiler("card == cc.silver", pc) {
            @Override
            protected ASTNode nextToken() {
                ASTNode node = super.nextToken();

                System.out.println("next: " + node);

                return node;
            }
        };
        CompiledExpression ce = e.compile();

        Object result = optimizeAndPrint(ce);
        System.out.println(result);
    }

    public static QuasiOptimizedExpression test(String expression) {
        System.out.println();
        System.out.println("Testing: " + expression);

        //-------------

        ExpressionCompiler e = new ExpressionCompiler(expression);
        CompiledExpression ce = e.compile();

        return optimizeAndPrint(ce);
    }

    protected static QuasiOptimizedExpression optimizeAndPrint(CompiledExpression ce) {
        ASTNode node = ce.getFirstNode();

        StringBuilder sb = new StringBuilder();
        new ASTNodeDecompiler().walk(node, null, sb);

        System.out.println("Decompiled: " + sb);

        return new MvelQoeTransformer().transform(node);
    }
}
