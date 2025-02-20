/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from https://json.org

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

parser grammar JsonParser;

options {
    tokenVocab=JsonLexer;
}

json
    : value EOF
    ;

obj
    : '{' pair (',' pair)* '}'
    | '{' '}'
    ;

pair
    : STRING ':' value
    ;

arr
    : '[' value (',' value)* ']'
    | '[' ']'
    ;

value
    : STRING
    | NUMBER
    | obj
    | arr
    | BOOLEAN
    | NULL
    ;
