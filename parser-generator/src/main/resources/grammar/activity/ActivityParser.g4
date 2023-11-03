parser grammar ActivityParser;
options { tokenVocab=ActivityLexer; }

activity
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
    | LCURLY exprcomma RCURLY
    | LPAREN expr RPAREN
    | '!' expr
    | expr 'and' expr
    | expr 'xor' expr
    | expr 'or' expr
    ;

exprcomma: exprcons (',' exprcons)* ;
exprcons: ID (CONS ID)* ;
