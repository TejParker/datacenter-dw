grammar DSLGrammar;

query: select_clause return_clause;

select_clause
    : SELECT select_expr FROM table WHERE where_expr GROUP BY group_by_expr
    ;

return_clause
    : RETURN return_expr ';'
    ;

select_expr
    : field (',' field)*
    ;

field
    : ID
    | aggregation
    ;

aggregation
    : agg_type '(' ID ')'
    ;

table
    : ID '.' ID
    ;

where_expr
    : condition ('AND' condition)*
    ;

condition
    : ID '=' value
    ;

group_by_expr
    : ID
    ;

return_expr
    : ID (',' ID)*
    ;

agg_type
    : 'sum'
    ;

SELECT  : 'Select';
FROM    : 'from';
WHERE   : 'where';
GROUP   : 'group';
BY      : 'by';
RETURN  : 'Return';
AND     : 'and';

ID
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

value
    : placeholder
    | STRING
    | INT
    ;

placeholder
    : '#{' ID '}'
    ;

INT
    : [0-9]+
    ;

STRING
    : '\'' .*? '\''
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
