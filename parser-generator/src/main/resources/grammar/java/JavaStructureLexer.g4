lexer grammar JavaStructureLexer;

LBRACE:             '{';
RBRACE:             '}';
SEMI:               ';';

PACKAGE:            (ANNOTATION Skipable*)* 'package' Skipable+ QualifiedName Skipable* ';';
IMPORT:             'import' Skipable+ ('static' Skipable+)? QualifiedName Skipable* ('.' Skipable* '*')? Skipable* ';';
STATIC:             'static';

CLASS:              'class' Skipable+ IDENTIFIER (Skipable* DIAMONDS)?;
INTERFACE:          'interface' Skipable+ IDENTIFIER (Skipable* DIAMONDS)?;
ENUM:               'enum';
RECORD:             'record' Skipable+ IDENTIFIER (Skipable* DIAMONDS)?;


WS:                 [ \t\u000C]+ -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);
NEW_LINE:           '\r'? '\n' ->  channel(HIDDEN);

STRING_LITERAL:     '"' (~["\\\r\n] | EscapeSequence)* '"' -> channel(HIDDEN);
CHAR_LITERAL:       '\'' (~['\\\r\n] | EscapeSequence) '\'' -> channel(HIDDEN);

AT_INTERFACE:       '@' Skipable* 'interface' Skipable* IDENTIFIER;
ANNOTATION:         ('@' Skipable* QualifiedName | AltQualifiedName) (Skipable* COR_PAREN_SEQ)?;
COR_PAREN_SEQ:      '(' (COR_PAREN_SEQ | Literal | Skipable | ~[)] )*? ')';

DEFAULT:            'default';
SYNCHRONIZED:       'synchronized';
FINAL:              'final';
SEALED:             'sealed';
NON_SEALED:         'non-sealed';

MODIFIER
    : 'private'
    | 'protected'
    | 'public'
    | 'abstract'
    | 'strictfp'
    | 'native'
    | 'transient'
    | 'volatile'
    ;


IDENTIFIER:         Letter LetterOrDigit* -> channel(HIDDEN);

ANY:                . -> channel(HIDDEN);

fragment QualifiedName
    : IDENTIFIER (Skipable* '.' Skipable* IDENTIFIER)*?;
fragment AltQualifiedName
    : (IDENTIFIER Skipable* '.' Skipable*)* '@' Skipable* IDENTIFIER;

fragment DIAMONDS:           '<' (DIAMONDS | Skipable | ~[>])*? '>';

fragment Literal
    : STRING_LITERAL
    | CHAR_LITERAL
    ;

fragment Skipable
    : WS
    | COMMENT
    | LINE_COMMENT
    | NEW_LINE
    ;

fragment ExponentPart
    : [eE] [+-]? Digits
    ;

fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigits
    : HexDigit ((HexDigit | '_')* HexDigit)?
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

fragment Digits
    : [0-9] ([0-9_]* [0-9])?
    ;

fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment Letter
    : [a-zA-Z$_] // these are the "java letters" below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;
