// $antlr-format alignTrailingComments true, columnLimit 150, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine true, allowShortBlocksOnASingleLine true, minEmptyLines 0, alignSemicolons ownLine
// $antlr-format alignColons trailing, singleLineOverrulesHangingColon true, alignLexerCommands true, alignLabels true, alignTrailers true

lexer grammar CPP14Lexer;

INTEGER_LITERAL:
    DECIMAL_LITERAL INTEGERSUFFIX?
    | OCTAL_LITERAL INTEGERSUFFIX?
    | HEXADECIMAL_LITERAL INTEGERSUFFIX?
    | BINARY_LITERAL INTEGERSUFFIX?
;

CHARACTER_LITERAL: ('u' | 'U' | 'L')? '\'' CChar+ '\'';

FLOATING_LITERAL:
    FractionalConstant ExponentPart? FloatingSuffix?
    | DigitSequence ExponentPart FloatingSuffix?
;

STRING_LITERAL: EncodingPrefix? (RawString | '"' SChar* '"');

BOOLEAN_LITERAL: FALSE_ | TRUE_;

POINTER_LITERAL: NULLPTR;

USER_DEFINED_LITERAL:
    USER_DEFINED_INTEGER_LITERAL
    | USER_DEFINED_FLOATING_LITERAL
    | USER_DEFINED_STRING_LITERAL
    | USER_DEFINED_CHARACTER_LITERAL
;

MULTI_LINE_MACRO: '#' (~[\n]*? '\\' '\r'? '\n')+ ~ [\n]+ -> channel (HIDDEN);

DIRECTIVE: '#' ~ [\n]* -> channel (HIDDEN);
/*Keywords*/

ALIGNAS: 'alignas';

ALIGNOF: 'alignof';

ASM: 'asm';

AUTO: 'auto';

BOOL: 'bool';

BREAK: 'break';

CASE: 'case';

CATCH: 'catch';

CHAR: 'char';

CHAR16: 'char16_t';

CHAR32: 'char32_t';

CLASS: 'class';

CONST: 'const';

CONSTEXPR: 'constexpr';

CONST_CAST: 'const_cast';

CONTINUE: 'continue';

DECLTYPE: 'decltype';

DEFAULT: 'default';

DELETE: 'delete';

DO: 'do';

DOUBLE: 'double';

DYNAMIC_CAST: 'dynamic_cast';

ELSE: 'else';

ENUM: 'enum';

EXPLICIT: 'explicit';

EXPORT: 'export';

EXTERN: 'extern';

//DO NOT RENAME - PYTHON NEEDS True and False
FALSE_: 'false';

FINAL: 'final';

FLOAT: 'float';

FOR: 'for';

FRIEND: 'friend';

GOTO: 'goto';

IF: 'if';

INLINE: 'inline';

INT: 'int';

LONG: 'long';

MUTABLE: 'mutable';

NAMESPACE: 'namespace';

NEW: 'new';

NOEXCEPT: 'noexcept';

NULLPTR: 'nullptr';

OPERATOR: 'operator';

OVERRIDE: 'override';

PRIVATE: 'private';

PROTECTED: 'protected';

PUBLIC: 'public';

REGISTER: 'register';

REINTERPRET_CAST: 'reinterpret_cast';

RETURN: 'return';

SHORT: 'short';

SIGNED: 'signed';

SIZEOF: 'sizeof';

STATIC: 'static';

STATIC_ASSERT: 'static_assert';

STATIC_CAST: 'static_cast';

STRUCT: 'struct';

SWITCH: 'switch';

TEMPLATE: 'template';

THIS: 'this';

THREAD_LOCAL: 'thread_local';

THROW: 'throw';

//DO NOT RENAME - PYTHON NEEDS True and False
TRUE_: 'true';

TRY: 'try';

TYPEDEF: 'typedef';

TYPEID_: 'typeid';

TYPENAME_: 'typename';

UNION: 'union';

UNSIGNED: 'unsigned';

USING: 'using';

VIRTUAL: 'virtual';

VOID: 'void';

VOLATILE: 'volatile';

WCHAR: 'wchar_t';

WHILE: 'while';
/*Operators*/

LEFT_PAREN: '(';

RIGHT_PAREN: ')';

LEFT_BRACKET: '[';

RIGHT_BRACKET: ']';

LEFT_BRACE: '{';

RIGHT_BRACE: '}';

PLUS: '+';

MINUS: '-';

STAR: '*';

DIV: '/';

MOD: '%';

CARET: '^';

AND: '&';

OR: '|';

TILDE: '~';

NOT: '!' | 'not';

ASSIGN: '=';

LESS: '<';

GREATER: '>';

PLUS_ASSIGN: '+=';

MINUS_ASSIGN: '-=';

STAR_ASSIGN: '*=';

DIV_ASSIGN: '/=';

MOD_ASSIGN: '%=';

XOR_ASSIGN: '^=';

AND_ASSIGN: '&=';

OR_ASSIGN: '|=';

LEFT_SHIFT_ASSIGN: '<<=';

RIGHT_SHIFT_ASSIGN: '>>=';

EQUAL: '==';

NOT_EQUAL: '!=';

LESS_EQUAL: '<=';

GREATER_EQUAL: '>=';

AND_AND: '&&' | 'and';

OR_OR: '||' | 'or';

PLUS_PLUS: '++';

MINUS_MINUS: '--';

COMMA: ',';

ARROW_STAR: '->*';

ARROW: '->';

QUESTION: '?';

COLON: ':';

DOUBLECOLON: '::';

SEMI: ';';

DOT: '.';

DOT_STAR: '.*';

ELLIPSIS: '...';

fragment HexQuad: HexadecimalDigit HexadecimalDigit HexadecimalDigit HexadecimalDigit;

fragment UniversalCharacterName: '\\u' HexQuad | '\\U' HexQuad HexQuad;

IDENTIFIER:
    /*
	 IdentifierNonDigit | IDENTIFIER IdentifierNonDigit | IDENTIFIER Digit
	 */ IdentifierNonDigit (IdentifierNonDigit | Digit)*
;

fragment IdentifierNonDigit: NonDigit | UniversalCharacterName;

fragment NonDigit: [a-zA-Z_];

fragment Digit: [0-9];

DECIMAL_LITERAL: NonZeroDigit ('\''? Digit)*;

OCTAL_LITERAL: '0' ('\''? OctalDigit)*;

HEXADECIMAL_LITERAL: ('0x' | '0X') HexadecimalDigit ( '\''? HexadecimalDigit)*;

BINARY_LITERAL: ('0b' | '0B') BinaryDigit ('\''? BinaryDigit)*;

fragment NonZeroDigit: [1-9];

fragment OctalDigit: [0-7];

fragment HexadecimalDigit: [0-9a-fA-F];

fragment BinaryDigit: [01];

INTEGERSUFFIX:
    UnsignedSuffix LongSuffix?
    | UnsignedSuffix LongLongSuffix?
    | LongSuffix UnsignedSuffix?
    | LongLongSuffix UnsignedSuffix?
;

fragment UnsignedSuffix: [uU];

fragment LongSuffix: [lL];

fragment LongLongSuffix: 'll' | 'LL';

fragment CChar: ~ ['\\\r\n] | EscapeSequence | UniversalCharacterName;

fragment EscapeSequence: SimpleEscapeSequence | OctalEscapeSequence | HexadecimalEscapeSequence;

fragment SimpleEscapeSequence:
    '\\\''
    | '\\"'
    | '\\?'
    | '\\\\'
    | '\\a'
    | '\\b'
    | '\\f'
    | '\\n'
    | '\\r'
    | '\\' ('\r' '\n'? | '\n')
    | '\\t'
    | '\\v'
;

fragment OctalEscapeSequence:
    '\\' OctalDigit
    | '\\' OctalDigit OctalDigit
    | '\\' OctalDigit OctalDigit OctalDigit
;

fragment HexadecimalEscapeSequence: '\\x' HexadecimalDigit+;

fragment FractionalConstant: DigitSequence? '.' DigitSequence | DigitSequence '.';

fragment ExponentPart: 'e' Sign? DigitSequence | 'E' Sign? DigitSequence;

fragment Sign: [+-];

fragment DigitSequence: Digit ('\''? Digit)*;

fragment FloatingSuffix: [flFL];

fragment EncodingPrefix: 'u8' | 'u' | 'U' | 'L';

fragment SChar: ~ ["\\\r\n] | EscapeSequence | UniversalCharacterName;

fragment RawString: 'R"' ( '\\' ["()] | ~[\r\n (])*? '(' ~[)]*? ')' ( '\\' ["()] | ~[\r\n "])*? '"';

USER_DEFINED_INTEGER_LITERAL:
    DECIMAL_LITERAL UdSuffix
    | OCTAL_LITERAL UdSuffix
    | HEXADECIMAL_LITERAL UdSuffix
    | BINARY_LITERAL UdSuffix
;

USER_DEFINED_FLOATING_LITERAL:
    FractionalConstant ExponentPart? UdSuffix
    | DigitSequence ExponentPart UdSuffix
;

USER_DEFINED_STRING_LITERAL: STRING_LITERAL UdSuffix;

USER_DEFINED_CHARACTER_LITERAL: CHARACTER_LITERAL UdSuffix;

fragment UdSuffix: IDENTIFIER;

WHITESPACE: [ \t]+ -> channel(HIDDEN);

NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);

DOCUMENTATION: '/**' .*? '*/' -> channel(HIDDEN);

BLOCK_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

LINE_COMMENT: '//' ~ [\r\n]* -> channel(HIDDEN);

ERROR:              . -> channel(HIDDEN);
