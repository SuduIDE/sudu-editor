grammar CPP14Directive;

directive
    : include EOF
    | error EOF
    | dir EOF
    ;

include: HASH INCLUDE (STRING | (LEFT (IDENTIFIER | '/' | '.')+ RIGHT));
error: HASH ERROR_DIRECTIVE other*;
dir: HASH (IDENTIFIER | KEYWORD) other*;

other
    : (STRING | RAW_STRING) | LEFT | RIGHT | KEYWORD | (OPERATORS | DOT | SLASH) | IDENTIFIER
    | INTEGER_LITERAL | DECIMAL_LITERAL | OCTAL_LITERAL | HEXADECIMAL_LITERAL | BINARY_LITERAL
    | DIR_CHAR
    | INCLUDE | ERROR_DIRECTIVE
    | HASH
    ;

STRING : Quote SChar* Quote;
RAW_STRING
    : 'R"' SChar* '"'
    | 'u8R"' SChar* '"'
    | 'uR"'  SChar* '"'
    | 'UR"'  SChar* '"'
    | 'LR"'  SChar* '"'
    ;

HASH: '#';
INCLUDE: 'include';
ERROR_DIRECTIVE: 'error';

WHITESPACE: [ \t]+ -> channel(HIDDEN);
BLOCK_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~ [\r\n]* -> channel(HIDDEN);
NEW_LINE_SLASH: '\\' -> channel(HIDDEN);
NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);

fragment Quote: '"';
LEFT: '<';
RIGHT: '>';

INTEGER_LITERAL:
	DECIMAL_LITERAL IntegerSuffix?
	| OCTAL_LITERAL IntegerSuffix?
	| HEXADECIMAL_LITERAL IntegerSuffix?
	| BINARY_LITERAL IntegerSuffix?
	;
DECIMAL_LITERAL: NonZeroDigit ('\''? Digit)*;
OCTAL_LITERAL: '0' ('\''? OctalDigit)*;
HEXADECIMAL_LITERAL: ('0x' | '0X') HexadecimalDigit ('\''? HexadecimalDigit)*;
BINARY_LITERAL: ('0b' | '0B') BinaryDigit ('\''? BinaryDigit)*;

KEYWORD
    : 'alignas'
    | 'alignof'
    | 'asm'
    | 'auto'
    | 'bool'
    | 'break'
    | 'case'
    | 'catch'
    | 'char'
    | 'char16_t'
    | 'char32_t'
    | 'class'
    | 'const'
    | 'constexpr'
    | 'const_cast'
    | 'continue'
    | 'decltype'
    | 'default'
    | 'delete'
    | 'do'
    | 'double'
    | 'dynamic_cast'
    | 'else'
    | 'enum'
    | 'explicit'
    | 'export'
    | 'extern'
    | 'false'
    | 'final'
    | 'float'
    | 'for'
    | 'friend'
    | 'goto'
    | 'if'
    | 'inline'
    | 'int'
    | 'long'
    | 'mutable'
    | 'namespace'
    | 'new'
    | 'noexcept'
    | 'nullptr'
    | 'operator'
    | 'override'
    | 'private'
    | 'protected'
    | 'public'
    | 'register'
    | 'reinterpret_cast'
    | 'return'
    | 'short'
    | 'signed'
    | 'sizeof'
    | 'static'
    | 'static_assert'
    | 'static_cast'
    | 'struct'
    | 'switch'
    | 'template'
    | 'this'
    | 'thread_local'
    | 'throw'
    | 'true'
    | 'try'
    | 'typedef'
    | 'typeid'
    | 'typename'
    | 'union'
    | 'unsigned'
    | 'using'
    | 'virtual'
    | 'void'
    | 'volatile'
    | 'wchar_t'
    | 'while'
    ;
OPERATORS
    : '('
    | ')'
    | '['
    | ']'
    | '{'
    | '}'
    | '+'
    | '-'
    | '*'
    | '%'
    | '^'
    | '&'
    | '|'
    | '~'
    | '!'
    | 'not'
    | '='
    | '+='
    | '-='
    | '*='
    | '/='
    | '%='
    | '^='
    | '&='
    | '|='
    | '<<='
    | '>>='
    | '=='
    | '!='
    | '<='
    | '>='
    | '&&'
    | 'and'
    | '||'
    | 'or'
    | '++'
    | '--'
    | ','
    | '->*'
    | '->'
    | '?'
    | ':'
    | '::'
    | ';'
    | '.*'
    | '...'
    ;

SLASH: '/';
DOT  : '.';

IDENTIFIER: IdentifierNonDigit (IdentifierNonDigit | Digit)*;
DIR_CHAR: ~[#\r\n];
ERROR: . -> channel(HIDDEN);

fragment SChar:
	~ ["\\\r\n]
	| EscapeSequence
	| UniversalCharacterName;
fragment IntegerSuffix:
	UnsignedSuffix LongSuffix?
	| UnsignedSuffix LongLongSuffix?
	| LongSuffix UnsignedSuffix?
	| LongLongSuffix UnsignedSuffix?;
fragment IdentifierNonDigit: NonDigit | UniversalCharacterName;
fragment NonDigit: [a-zA-Z_];
fragment Digit: [0-9];
fragment UniversalCharacterName
    : '\\u' HexQuad
	| '\\U' HexQuad HexQuad;
fragment HexQuad: HexadecimalDigit HexadecimalDigit HexadecimalDigit HexadecimalDigit;
fragment HexadecimalDigit: [0-9a-fA-F];
fragment EscapeSequence:
	SimpleEscapeSequence
	| OctalEscapeSequence
	| HexadecimalEscapeSequence;
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
	| '\\t'
	| '\\v';
fragment OctalEscapeSequence:
	'\\' OctalDigit
	| '\\' OctalDigit OctalDigit
	| '\\' OctalDigit OctalDigit OctalDigit;
fragment HexadecimalEscapeSequence: '\\x' HexadecimalDigit+;
fragment OctalDigit: [0-7];
fragment NonZeroDigit: [1-9];
fragment BinaryDigit: [01];
fragment UnsignedSuffix: [uU];
fragment LongSuffix: [lL];
fragment LongLongSuffix: 'll' | 'LL';