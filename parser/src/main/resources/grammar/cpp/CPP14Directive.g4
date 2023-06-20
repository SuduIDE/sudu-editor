grammar CPP14Directive;

directive
    : include
    | error
    | dir
    ;

include: Hash Include String;
error: Hash Error other*;
dir: Hash (Identifier | Keyword) other*;

other
    : String | Left | Right | Keyword | Operators | Identifier
    | IntegerLiteral | DecimalLiteral | OctalLiteral | HexadecimalLiteral | BinaryLiteral
    | DirChar
    ;

String : Quote Schar* Quote | Left Schar* Right;

Hash: '#';
Include: 'include';
Error: 'error';

Whitespace: [ \t]+ -> channel(HIDDEN);
BlockComment: '/*' .*? '*/' -> channel(HIDDEN);
LineComment: '//' ~ [\r\n]* -> channel(HIDDEN);
NewLineSlash: '\\' -> channel(HIDDEN);
NewLine: '\r'? '\n' -> channel(HIDDEN);

fragment Quote: '"';
Left: '<';
Right: '>';

IntegerLiteral:
	DecimalLiteral Integersuffix?
	| OctalLiteral Integersuffix?
	| HexadecimalLiteral Integersuffix?
	| BinaryLiteral Integersuffix?
	;
DecimalLiteral: NONZERODIGIT ('\''? DIGIT)*;
OctalLiteral: '0' ('\''? OCTALDIGIT)*;
HexadecimalLiteral: ('0x' | '0X') HEXADECIMALDIGIT ('\''? HEXADECIMALDIGIT)*;
BinaryLiteral: ('0b' | '0B') BINARYDIGIT ('\''? BINARYDIGIT)*;

Keyword
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
Operators
    : '('
    | ')'
    | '['
    | ']'
    | '{'
    | '}'
    | '+'
    | '-'
    | '*'
    | '/'
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
    | '.'
    | '.*'
    | '...'
    ;

Identifier: Identifiernondigit (Identifiernondigit | DIGIT)*;
DirChar: ~[#\r\n];
Other: . -> channel(HIDDEN);

fragment Schar:
	~ ["\\\r\n]
	| Escapesequence
	| Universalcharactername;
fragment Integersuffix:
	Unsignedsuffix Longsuffix?
	| Unsignedsuffix Longlongsuffix?
	| Longsuffix Unsignedsuffix?
	| Longlongsuffix Unsignedsuffix?;
fragment Identifiernondigit: NONDIGIT | Universalcharactername;
fragment NONDIGIT: [a-zA-Z_];
fragment DIGIT: [0-9];
fragment Universalcharactername
    : '\\u' Hexquad
	| '\\U' Hexquad Hexquad;
fragment Hexquad: HEXADECIMALDIGIT HEXADECIMALDIGIT HEXADECIMALDIGIT HEXADECIMALDIGIT;
fragment HEXADECIMALDIGIT: [0-9a-fA-F];
fragment Escapesequence:
	Simpleescapesequence
	| Octalescapesequence
	| Hexadecimalescapesequence;
fragment Simpleescapesequence:
	'\\\''
	| '\\"'
	| '\\?'
	| '\\\\'
	| '\\a'
	| '\\b'
	| '\\f'
	| '\\n'
	| '\\r'
	| ('\\' ('\r' '\n'? | '\n'))
	| '\\t'
	| '\\v';
fragment Octalescapesequence:
	'\\' OCTALDIGIT
	| '\\' OCTALDIGIT OCTALDIGIT
	| '\\' OCTALDIGIT OCTALDIGIT OCTALDIGIT;
fragment Hexadecimalescapesequence: '\\x' HEXADECIMALDIGIT+;
fragment OCTALDIGIT: [0-7];
fragment NONZERODIGIT: [1-9];
fragment BINARYDIGIT: [01];
fragment Unsignedsuffix: [uU];
fragment Longsuffix: [lL];
fragment Longlongsuffix: 'll' | 'LL';