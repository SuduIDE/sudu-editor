parser grammar ActivityParser;
options { tokenVocab=ActivityLexer; }

program
    : ACTIVITY blocksemi EOF
    ;


blocksemi: LCURLY stat (SEMI stat)* SEMI* RCURLY ;
block: LCURLY stat (',' stat)* RCURLY ;

stat: ID
    | REPEAT LPAREN INT RPAREN block
    | SELECT block
    | SCHEDULE block
    | IF LPAREN expr RPAREN block (SEMI ELSE block)?
    ;

expr: ID
    | INT
    | LCURLY exprlist RCURLY
    | LPAREN expr RPAREN
    | expr 'and' expr
    | expr 'or' expr
    ;

exprlist: exprcons (',' exprcons)* ;
exprcons: ID (CONS ID)* ;
