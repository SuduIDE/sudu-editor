/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Bart Kiers (original author) and Alexandre Vitorelli (contributor -> ported to CSharp)
 * Copyright (c) 2017-2020 by Ivan Kochurkin (Positive Technologies):
    added ECMAScript 6 support, cleared and transformed to the universal grammar.
 * Copyright (c) 2018 by Juan Alvarez (contributor -> ported to Go)
 * Copyright (c) 2019 by Student Main (contributor -> ES2020)
 * Copyright (c) 2019 by Andrii Artiushok (contributor -> added TypeScript support)
 * Copyright (c) 2024 by Andrew Leppard (www.wegrok.review)
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

// $antlr-format alignTrailingComments true, columnLimit 150, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine true, allowShortBlocksOnASingleLine true, minEmptyLines 0, alignSemicolons ownLine
// $antlr-format alignColons trailing, singleLineOverrulesHangingColon true, alignLexerCommands true, alignLabels true, alignTrailers true

lexer grammar LightEcmaScriptLexer;

options {
    superClass = LightEcmaScriptLexerBase;
}

HASH_BANG_LINE: '#!' ~[\r\n\u2028\u2029]* {this.isJavaScript()}?; // only allowed at start
MULTI_LINE_COMMENT:               '/*' .*? '*/'             -> channel(HIDDEN);
SINGLE_LINE_COMMENT:              '//' ~[\r\n\u2028\u2029]* -> channel(HIDDEN);
REGULAR_EXPRESSION_LITERAL:       '/' RegularExpressionFirstChar RegularExpressionChar* '/' IdentifierPart*;

OPEN_BRACKET:                    '[';
CLOSE_BRACKET:                   ']';
OPEN_PAREN:                      '(';
CLOSE_PAREN:                     ')';
OPEN_BRACE:                      '{';
CLOSE_BRACE:                     '}';
SEMI_COLON:                      ';';
COMMA:                          ',';
ASSIGN:                         '=';
QUESTION_MARK:                   '?';
QUESTION_MARK_DOT:                '?.';
COLON:                          ':';
ELLIPSIS:                       '...';
DOT:                            '.';
PLUS_PLUS:                       '++';
MINUS_MINUS:                     '--';
PLUS:                           '+';
MINUS:                          '-';
BIT_NOT:                         '~';
NOT:                            '!';
MULTIPLY:                       '*';
DIVIDE:                         '/';
MODULUS:                        '%';
POWER:                          '**';
NULL_COALESCE:                   '??';
HASHTAG:                        '#';
RIGHT_SHIFT_LOGICAL:              '>>>' {this.isJavaScript()}?;
RIGHT_SHIFT_ARITHMETIC:           '>>' {this.isJavaScript()}?;
LEFT_SHIFT_ARITHMETIC:            '<<';
LESS_THAN:                       '<';
MORE_THAN:                       '>';
LESS_THAN_EQUALS:                 '<=';
GREATER_THAN_EQUALS:              '>=';
EQUALS_:                        '==';
NOT_EQUALS:                      '!=';
IDENTITY_EQUALS:                 '===';
IDENTITY_NOT_EQUALS:              '!==';
BIT_AND:                         '&';
BIT_X_OR:                         '^';
BIT_OR:                          '|';
AND:                            '&&';
OR:                             '||';
MULTIPLY_ASSIGN:                 '*=';
DIVIDE_ASSIGN:                   '/=';
MODULUS_ASSIGN:                  '%=';
PLUS_ASSIGN:                     '+=';
MINUS_ASSIGN:                    '-=';
LEFT_SHIFT_ARITHMETIC_ASSIGN:      '<<=';
RIGHT_SHIFT_ARITHMETIC_ASSIGN:     '>>=';
RIGHT_SHIFT_LOGICAL_ASSIGN:        '>>>=';
BIT_AND_ASSIGN:                   '&=';
BIT_XOR_ASSIGN:                   '^=';
BIT_OR_ASSIGN:                    '|=';
POWER_ASSIGN:                    '**=';
NULLISH_COALESCING_ASSIGN:        '??=' {this.isTypeScript()}?;
ARROW:                          '=>';

/// Null Literals

NULL_LITERAL:                    'null';

/// BOOLEAN Literals

BOOLEAN_LITERAL:                 'true'
              |                 'false';

/// Numeric Literals

DECIMAL_LITERAL:                 DecimalIntegerLiteral '.' [0-9] [0-9_]* ExponentPart?
              |                 '.' [0-9] [0-9_]* ExponentPart?
              |                 DecimalIntegerLiteral ExponentPart?
              ;

HEX_INTEGER_LITERAL:              '0' [xX] [0-9a-fA-F] HexDigit*;
OCTAL_INTEGER_LITERAL:            '0' [0-7]+;
OCTAL_INTEGER_LITERAL2:           '0' [oO] [0-7] [_0-7]*;
BINARY_INTEGER_LITERAL:           '0' [bB] [01] [_01]*;

BIG_HEX_INTEGER_LITERAL:           '0' [xX] [0-9a-fA-F] HexDigit* 'n';
BIG_OCTAL_INTEGER_LITERAL:         '0' [oO] [0-7] [_0-7]* 'n';
BIG_BINARY_INTEGER_LITERAL:        '0' [bB] [01] [_01]* 'n';
BIG_DECIMAL_INTEGER_LITERAL:       DecimalIntegerLiteral 'n';

COLOR
    : '#' HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit {this.isJavaScript()}?
    | '#' HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit {this.isJavaScript()}?
    ;

/// Keywords

BREAK:                          'break';
DO:                             'do';
INSTANCEOF:                     'instanceof';
TYPEOF:                         'typeof';
CASE:                           'case';
ELSE:                           'else';
NEW:                            'new';
VAR:                            'var';
CATCH:                          'catch';
FINALLY:                        'finally';
RETURN:                         'return';
VOID:                           'void';
CONTINUE:                       'continue';
FOR:                            'for';
SWITCH:                         'switch';
WHILE:                          'while';
DEBUGGER:                       'debugger';
FUNCTION_:                      'function';
THIS:                           'this';
WITH:                           'with';
DEFAULT:                        'default';
IF:                             'if';
THROW:                          'throw';
DELETE:                         'delete';
IN:                             'in';
TRY:                            'try';
AS:                             'as';
FROM:                           'from';
READ_ONLY:                       'readonly' {this.isTypeScript()}?;
ASYNC:                          'async';
AWAIT:                          'await';
YIELD:                          'yield';
YIELD_STAR:                      'yield*' {this.isTypeScript()}?;

/// Future Reserved Words

CLASS:                          'class';
ENUM:                           'enum';
EXTENDS:                        'extends';
SUPER:                          'super';
CONST:                          'const';
EXPORT:                         'export';
IMPORT:                         'import';

/// The following tokens are also considered to be FutureReservedWords
/// when parsing strict mode

IMPLEMENTS:                     'implements';
LET:                            'let';
PRIVATE:                        'private';
PUBLIC:                         'public';
INTERFACE:                      'interface';
PACKAGE:                        'package';
PROTECTED:                      'protected';
STATIC:                         'static';

// TypeScript-only keywords
ANY:                            'any' {this.isTypeScript()}?;
NUMBER:                         'number' {this.isTypeScript()}?;
NEVER:                          'never' {this.isTypeScript()}?;
BOOLEAN:                        'boolean' {this.isTypeScript()}?;
STRING:                         'string' {this.isTypeScript()}?;
UNIQUE:                         'unique' {this.isTypeScript()}?;
SYMBOL:                         'symbol' {this.isTypeScript()}?;
UNDEFINED:                      'undefined' {this.isTypeScript()}?;
OBJECT:                         'object' {this.isTypeScript()}?;
OF:                             'of' {this.isTypeScript()}?;
KEY_OF:                          'keyof' {this.isTypeScript()}?;
TYPE_ALIAS:                      'type' {this.isTypeScript()}?;
CONSTRUCTOR:                    'constructor' {this.isTypeScript()}?;
NAMESPACE:                      'namespace' {this.isTypeScript()}?;
REQUIRE:                        'require' {this.isTypeScript()}?;
MODULE:                         'module' {this.isTypeScript()}?;
DECLARE:                        'declare' {this.isTypeScript()}?;
ABSTRACT:                       'abstract' {this.isTypeScript()}?;
IS:                             'is' {this.isTypeScript()}?;

// Decorators
AT:                             '@' {this.isTypeScript()}?;

/// IDENTIFIER Names and Identifiers

IDENTIFIER:                     IdentifierStart IdentifierPart*;

/// STRING Literals
STRING_LITERAL:                  ('"' DoubleStringCharacter* '"'
             |                  '\'' SingleStringCharacter* '\'')
             ;

BACK_TICK:                       '`';

WHITE_SPACES:                    [\t\u000B\u000C\u0020\u00A0]+ -> channel(HIDDEN);

NEW_LINE:                       ('\r\n' | '\r' | '\n' | '\u2028' | '\u2029') -> channel(HIDDEN);

/// Comments

HTML_COMMENT:                    '<!--' .*? '-->' -> channel(HIDDEN);
C_DATA_COMMENT:                   '<![CDATA[' .*? ']]>' -> channel(HIDDEN);
ERROR:                          . -> channel(HIDDEN);

// Fragment rules

fragment DoubleStringCharacter
    : ~["\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;

fragment SingleStringCharacter
    : ~['\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;

fragment EscapeSequence
    : CharacterEscapeSequence
    | '0' // no digit ahead! TODO
    | HexEscapeSequence
    | UnicodeEscapeSequence
    | ExtendedUnicodeEscapeSequence
    ;

fragment CharacterEscapeSequence
    : SingleEscapeCharacter
    | NonEscapeCharacter
    ;

fragment HexEscapeSequence
    : 'x' HexDigit HexDigit
    ;

fragment UnicodeEscapeSequence
    : 'u' HexDigit HexDigit HexDigit HexDigit
    | 'u' '{' HexDigit HexDigit+ '}'
    ;

fragment ExtendedUnicodeEscapeSequence
    : 'u' '{' HexDigit+ '}'
    ;

fragment SingleEscapeCharacter
    : ['"\\bfnrtv]
    ;

fragment NonEscapeCharacter
    : ~['"\\bfnrtv0-9xu\r\n]
    ;

fragment EscapeCharacter
    : SingleEscapeCharacter
    | [0-9]
    | [xu]
    ;

fragment LineContinuation
    : '\\' [\r\n\u2028\u2029]+
    ;

fragment HexDigit
    : [_0-9a-fA-F]
    ;

fragment DecimalIntegerLiteral
    : '0'
    | [1-9] [0-9_]*
    ;

fragment ExponentPart
    : [eE] [+-]? [0-9_]+
    ;

fragment IdentifierPart
    : IdentifierStart
    | [\p{Mn}]
    | [\p{Nd}]
    | [\p{Pc}]
    | '\u200C'
    | '\u200D'
    ;

fragment IdentifierStart
    : [\p{L}]
    | [$_]
    | '\\' UnicodeEscapeSequence
    ;

fragment RegularExpressionFirstChar
    : ~[*\r\n\u2028\u2029\\/[]
    | RegularExpressionBackslashSequence
    | '[' RegularExpressionClassChar* ']'
    ;

fragment RegularExpressionChar
    : ~[\r\n\u2028\u2029\\/[]
    | RegularExpressionBackslashSequence
    | '[' RegularExpressionClassChar* ']'
    ;

fragment RegularExpressionClassChar
    : ~[\r\n\u2028\u2029\]\\]
    | RegularExpressionBackslashSequence
    ;

fragment RegularExpressionBackslashSequence
    : '\\' ~[\r\n\u2028\u2029]
    ;
