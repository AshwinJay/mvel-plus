mvel-plus
=========

[MVEL](https://github.com/mvel/mvel) 2.x AST walker code to rewrite/extract expressions.

This is some old experimental code that I wrote to walk the AST nodes created by the MVEL expression parser. 
Another little [experiment with mini-DSLs](https://github.com/AshwinJay/diesel).

The code is mostly complete with a test case (ahem, no assertions .. yet).

Best place to dive in is the test code [MvelExpressionOptimizerTest.java](https://github.com/AshwinJay/mvel-plus/blob/master/src/test/java/com/javaforu/util/expression/mvel/MvelExpressionOptimizerTest.java)

The [ASTNodeDecompiler](https://github.com/AshwinJay/mvel-plus/blob/master/src/main/java/com/javaforu/util/expression/mvel/ASTNodeDecompiler.java) will probably be more useful to you.

The [MvelQoeTransformer](https://github.com/AshwinJay/mvel-plus/blob/master/src/main/java/com/javaforu/util/expression/mvel/MvelQoeTransformer.java) was an attempt to extract and
flatten nested conjunctions/disjunctions (and/or). The intent was to pull some parts out for indexing data sets etc like a database index.

