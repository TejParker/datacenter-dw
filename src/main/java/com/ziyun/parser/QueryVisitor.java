package com.ziyun.parser;


public class QueryVisitor extends DSLGrammarBaseVisitor<Query> {
    @Override
    public Query visitQuery(DSLGrammarParser.QueryContext ctx) {
        Query query = new Query();
        query.setSelectClause(ctx.select_clause().getText());
        query.setFromClause(ctx.select_clause().table().getText());
        query.setWhereClause(ctx.select_clause().where_expr().getText());
        query.setGroupByClause(ctx.select_clause().group_by_expr().getText());
        query.setReturnClause(ctx.return_clause().getText());
        return query;
    }
}
