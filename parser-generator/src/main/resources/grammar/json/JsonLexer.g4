
lexer grammar JsonLexer;

STRING: '"' (Esc | SafeCodePoint)* '"';
NUMBER: '-'? Int ('.' [0-9]+)? Exp?;
BOOLEAN: 'true'| 'false';
NULL: 'null';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
COMMA: ',';
COLON: ':';

COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);
NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);
WS: [ \t]+ -> channel(HIDDEN);

ERROR: . -> channel(HIDDEN);

fragment Esc
    : '\\' (["\\/bfnrt] | Unicode)
    ;

fragment Unicode
    : 'u' Hex Hex Hex Hex
    ;

fragment Hex
    : [0-9a-fA-F]
    ;

fragment SafeCodePoint
    : ~ ["\\\u0000-\u001F]
    ;

fragment Int
    // integer part forbids leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;

// no leading zeros

fragment Exp
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+-]? [0-9]+
    ;
