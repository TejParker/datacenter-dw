grammar DSLGrammar;

query: select_clause return_clause;

select_clause: 'Select' select_expr 'from' table 'where' where_expr 'group by' group_by_expr;
return_clause: 'Return' return_expr ';';

select_expr: field (',' field)*;
field: ID | aggregation;
aggregation: agg_type '(' ID ')';
agg_type: 'sum';

table: ID '.' ID;

where_expr: condition ('and' condition)*;
condition: ID '=' value;

group_by_expr: ID;

return_expr: ID (',' ID)*;

ID: [a-zA-Z_][a-zA-Z_0-9]*;
value: ID | STRING | INT;

INT: [0-9]+;
STRING: '\'' .*? '\'';

WS: [ \t\r\n]+ -> skip;
