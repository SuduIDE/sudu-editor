parser grammar ActivityParser;
options { tokenVocab=ActivityLexer; }

activity
    : ACTIVITY blocksemi EOF
    ;


blocksemi: LCURLY stat (SEMI stat)* SEMI* RCURLY ;
block: LCURLY stat ((','|';') stat)* RCURLY ;
condblock: LCURLY exprstat ((','|';') exprstat)* RCURLY ;

exprstat: ('(' expr ')')? stat;

stat: ID
    | REPEAT LPAREN INT RPAREN block
    | SELECT condblock
    | SCHEDULE block
    | SEQUENCE block
    | RANDOM (LPAREN INT RPAREN)? block
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
