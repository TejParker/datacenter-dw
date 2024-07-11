package com.ziyun.parser2;

public class QueryVisitor extends DSLGrammarBaseVisitor<Query>{
    @Override
    public Query visitQuery(DSLGrammarParser.QueryContext ctx) {
        Query query = new Query();
        query.setSelectClause(formatSelectClause(ctx.select_clause()));
        query.setFromClause(ctx.select_clause().table().getText());
        query.setWhereClause(formatWhereClause(ctx.select_clause().where_expr()));
        query.setGroupByClause(ctx.select_clause().group_by_expr().getText());
        query.setReturnClause(formatReturnClause(ctx.return_clause()));
        return query;
    }

    private String formatSelectClause(DSLGrammarParser.Select_clauseContext ctx) {
        return "Select " + ctx.select_expr().getText() + " from " + ctx.table().getText();
    }

    private String formatWhereClause(DSLGrammarParser.Where_exprContext ctx) {
        return "where " + ctx.getText().toLowerCase().replace("and", " and ");
    }

    private String formatReturnClause(DSLGrammarParser.Return_clauseContext ctx) {
        return "Return " + ctx.return_expr().getText();
    }
}
