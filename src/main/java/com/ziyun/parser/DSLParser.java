package com.ziyun.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class DSLParser {
    public static Query parse(String dslScript) {
        // Create a lexer that processes the CharStream
        DSLGrammarLexer lexer = new DSLGrammarLexer(new ANTLRInputStream(dslScript));

        // Create a token stream from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create a parser that processes the token stream
        DSLGrammarParser parser = new DSLGrammarParser(tokens);

        // Parse the input: the method name corresponds to the entry point rule in your grammar
        ParseTree tree = parser.query();

        // Create a visitor that will create a Query object based on the parse tree
        QueryVisitor visitor = new QueryVisitor();

        // Use the visitor to traverse the parse tree and return the Query object
        return visitor.visit(tree);
    }
}
