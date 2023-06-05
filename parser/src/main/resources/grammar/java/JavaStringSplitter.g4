lexer grammar JavaStringSplitter;

TRI_QUOTE: '"""';
QUOTE: '"' | '\'';
CHARS: ~["\\\r\n]+;
ESCAPE
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

NEW_LINE:           '\r'? '\n';

fragment HexDigits
    : HexDigit ((HexDigit | '_')* HexDigit)?
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;