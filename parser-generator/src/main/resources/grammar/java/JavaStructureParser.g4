parser grammar JavaStructureParser;

options { tokenVocab=JavaStructureLexer; }

compilationUnit
    : packageDeclaration? importDeclaration* typeDeclaration* EOF
    ;

packageDeclaration
    : PACKAGE
    ;

importDeclaration
    : IMPORT
    ;

typeDeclaration
    : modifier*
      (classDeclaration | interfaceDeclaration | enumDeclaration | recordDeclaration | annotationTypeDeclaration)
    | ';'
    ;

modifier
    : ANNOTATION
    | MODIFIER
    | STATIC
    | FINAL
    | SYNCHRONIZED
    | DEFAULT
    | SEALED
    | NON_SEALED
    ;

classDeclaration
    : CLASS classBody
    ;


interfaceDeclaration
    : INTERFACE classBody
    ;

enumDeclaration
    : ENUM anyBlock
    ;

recordDeclaration
    : RECORD COR_PAREN_SEQ recordBody
    ;

annotationTypeDeclaration
    : AT_INTERFACE anyBlock
    ;

classBody
    : '{' classBodyDeclaration*? '}'
    ;

classBodyDeclaration
    : ';'
    | STATIC? block
    | modifier* memberDeclaration
    ;

memberDeclaration
    : methodDeclaration
    | classDeclaration
    | interfaceDeclaration
    | annotationTypeDeclaration
    | recordDeclaration
    | enumDeclaration
    | fieldDeclaration
    ;

methodDeclaration
    : COR_PAREN_SEQ methodBody
    ;


recordBody
    : '{' (classBodyDeclaration | compactConstructorDeclaration)*  '}'
    ;

compactConstructorDeclaration
    : modifier* block
    ;

fieldDeclaration
    : (block | anyToken)*? ';'
    ;

methodBody
    : block
    | ';'
    ;

anyBlock
    : '{' (anyBlock | anyToken | MODIFIER)*? '}'
    ;

block
    : '{' (block | anyToken)*? '}'
    ;

anyToken
    :  SEMI
    |  PACKAGE
    |  IMPORT
    |  STATIC
    |  CLASS
    |  INTERFACE
    |  ENUM
    |  RECORD
    |  COR_PAREN_SEQ
    |  ANNOTATION
    |  FINAL
    |  SYNCHRONIZED
    |  DEFAULT
    |  SEALED
    |  NON_SEALED
    |  recordDeclaration
    ;
