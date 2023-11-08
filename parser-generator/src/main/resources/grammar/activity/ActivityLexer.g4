// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar ActivityLexer;

AND : 'and' ;
OR : 'or' ;
XOR : 'xor' ;
NOT : '!' ;
EQ : '=' ;
COMMA : ',' ;
SEMI : ';' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
LESSER : '<' ;
GREATER: '>' ;
CONS : '->' ;

ACTIVITY : 'activity' ;
SELECT : 'select';
REPEAT : 'repeat';
SCHEDULE : 'schedule';
SEQUENCE : 'sequence';
RANDOM : 'random';
IF : 'if' ;
ELSE : 'else';

INT : [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;

WS:                 [ \t\u000C]+ -> channel(HIDDEN);
JAVADOC:            '/**' .*? '*/'    -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);
NEW_LINE:           '\r'? '\n' ->  channel(HIDDEN);

ERROR:              . -> channel(HIDDEN);