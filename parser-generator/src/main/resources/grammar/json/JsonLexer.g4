
lexer grammar JsonLexer;

STRING: '"' (ESC | SAFECODEPOINT)* '"';
NUMBER: '-'? INT ('.' [0-9]+)? EXP?;
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
NEWLINE: '\r'? '\n' -> channel(HIDDEN);
WS: [ \t]+ -> channel(HIDDEN);

ERROR: . -> channel(HIDDEN);

fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;

fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;

fragment HEX
    : [0-9a-fA-F]
    ;

fragment SAFECODEPOINT
    : ~ ["\\\u0000-\u001F]
    ;

fragment INT
    // integer part forbids leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;

// no leading zeros

fragment EXP
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+-]? [0-9]+
    ;
