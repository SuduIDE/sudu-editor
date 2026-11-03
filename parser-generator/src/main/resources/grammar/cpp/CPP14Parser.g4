/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Camilo Sanchez (Camiloasc1) 2020 Martin Mirchev (Marti2203)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ****************************************************************************
 */

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

parser grammar CPP14Parser;

options {
    superClass = CPP14ParserBase;
    tokenVocab = CPP14Lexer;
}

// Insert here @header for C++ parser.

/*Basic concepts*/

translationUnit
    : declarationseq? EOF
    ;

/*Expressions*/

primaryExpression
    : literal+
    | THIS
    | LEFT_PAREN expression RIGHT_PAREN
    | idExpression
    | lambdaExpression
    ;

idExpression
    : unqualifiedId
    | qualifiedId
    ;

unqualifiedId
    : IDENTIFIER
    | operatorFunctionId
    | conversionFunctionId
    | literalOperatorId
    | TILDE (className | decltypeSpecifier)
    | templateId
    ;

qualifiedId
    : nestedNameSpecifier TEMPLATE? unqualifiedId
    ;

nestedNameSpecifier
    : (theTypeName | namespaceName | decltypeSpecifier)? DOUBLECOLON
    | nestedNameSpecifier ( IDENTIFIER | TEMPLATE? simpleTemplateId) DOUBLECOLON
    ;

lambdaExpression
    : lambdaIntroducer lambdaDeclarator? compoundStatement
    ;

lambdaIntroducer
    : LEFT_BRACKET lambdaCapture? RIGHT_BRACKET
    ;

lambdaCapture
    : captureList
    | captureDefault (COMMA captureList)?
    ;

captureDefault
    : AND
    | ASSIGN
    ;

captureList
    : capture (COMMA capture)* ELLIPSIS?
    ;

capture
    : simpleCapture
    | initcapture
    ;

simpleCapture
    : AND? IDENTIFIER
    | THIS
    ;

initcapture
    : AND? IDENTIFIER initializer
    ;

lambdaDeclarator
    : LEFT_PAREN parameterDeclarationClause? RIGHT_PAREN MUTABLE? exceptionSpecification? attributeSpecifierSeq? trailingReturnType?
    ;

postfixExpression
    : primaryExpression
    | postfixExpression LEFT_BRACKET (expression | bracedInitList) RIGHT_BRACKET
    | postfixExpression LEFT_PAREN expressionList? RIGHT_PAREN
    | postfixExpression (DOT | ARROW) (
            | idExpression LEFT_PAREN expressionList? RIGHT_PAREN
            | TEMPLATE? idExpression
            | pseudoDestructorName
        )
    | (simpleTypeSpecifier | typeNameSpecifier) (
        LEFT_PAREN expressionList? RIGHT_PAREN
        | bracedInitList
    )
    | postfixExpression (PLUS_PLUS | MINUS_MINUS)
    | (DYNAMIC_CAST | STATIC_CAST | REINTERPRET_CAST | CONST_CAST) LESS theTypeId GREATER LEFT_PAREN expression RIGHT_PAREN
    | typeIdOfTheTypeId LEFT_PAREN (expression | theTypeId) RIGHT_PAREN
    ;

/*
 add a middle layer to eliminate duplicated function declarations
 */

typeIdOfTheTypeId
    : TYPEID_
    ;

expressionList
    : initializerList
    ;

pseudoDestructorName
    : nestedNameSpecifier? (theTypeName DOUBLECOLON)? TILDE theTypeName
    | nestedNameSpecifier TEMPLATE simpleTemplateId DOUBLECOLON TILDE theTypeName
    | TILDE decltypeSpecifier
    ;

unaryExpression
    : postfixExpression
    | (PLUS_PLUS | MINUS_MINUS | unaryOperator | SIZEOF) unaryExpression
    | SIZEOF (LEFT_PAREN theTypeId RIGHT_PAREN | ELLIPSIS LEFT_PAREN IDENTIFIER RIGHT_PAREN)
    | ALIGNOF LEFT_PAREN theTypeId RIGHT_PAREN
    | noExceptExpression
    | newExpression_
    | deleteExpression
    ;

unaryOperator
    : OR
    | STAR
    | AND
    | PLUS
    | TILDE
    | MINUS
    | NOT
    ;

newExpression_
    : DOUBLECOLON? NEW newPlacement? (newTypeId | LEFT_PAREN theTypeId RIGHT_PAREN) newInitializer_?
    ;

newPlacement
    : LEFT_PAREN expressionList RIGHT_PAREN
    ;

newTypeId
    : typeSpecifierSeq newDeclarator_?
    ;

newDeclarator_
    : pointerOperator newDeclarator_?
    | noPointerNewDeclarator
    ;

noPointerNewDeclarator
    : LEFT_BRACKET expression RIGHT_BRACKET attributeSpecifierSeq?
    | noPointerNewDeclarator LEFT_BRACKET constantExpression RIGHT_BRACKET attributeSpecifierSeq?
    ;

newInitializer_
    : LEFT_PAREN expressionList? RIGHT_PAREN
    | bracedInitList
    ;

deleteExpression
    : DOUBLECOLON? DELETE (LEFT_BRACKET RIGHT_BRACKET)? castExpression
    ;

noExceptExpression
    : NOEXCEPT LEFT_PAREN expression RIGHT_PAREN
    ;

castExpression
    : unaryExpression
    | LEFT_PAREN theTypeId RIGHT_PAREN castExpression
    ;

pointerMemberExpression
    : castExpression ((DOT_STAR | ARROW_STAR) castExpression)*
    ;

multiplicativeExpression
    : pointerMemberExpression ((STAR | DIV | MOD) pointerMemberExpression)*
    ;

additiveExpression
    : multiplicativeExpression ((PLUS | MINUS) multiplicativeExpression)*
    ;

shiftExpression
    : additiveExpression (shiftOperator additiveExpression)*
    ;

shiftOperator
    : GREATER GREATER
    | LESS LESS
    ;

relationalExpression
    : shiftExpression ((LESS | GREATER | LESS_EQUAL | GREATER_EQUAL) shiftExpression)*
    ;

equalityExpression
    : relationalExpression ((EQUAL | NOT_EQUAL) relationalExpression)*
    ;

andExpression
    : equalityExpression (AND equalityExpression)*
    ;

exclusiveOrExpression
    : andExpression (CARET andExpression)*
    ;

inclusiveOrExpression
    : exclusiveOrExpression (OR exclusiveOrExpression)*
    ;

logicalAndExpression
    : inclusiveOrExpression (AND_AND inclusiveOrExpression)*
    ;

logicalOrExpression
    : logicalAndExpression (OR_OR logicalAndExpression)*
    ;

conditionalExpression
    : logicalOrExpression (QUESTION expression COLON assignmentExpression)?
    ;

assignmentExpression
    : conditionalExpression
    | logicalOrExpression assignmentOperator initializerClause
    | throwExpression
    ;

assignmentOperator
    : ASSIGN
    | STAR_ASSIGN
    | DIV_ASSIGN
    | MOD_ASSIGN
    | PLUS_ASSIGN
    | MINUS_ASSIGN
    | RIGHT_SHIFT_ASSIGN
    | LEFT_SHIFT_ASSIGN
    | AND_ASSIGN
    | XOR_ASSIGN
    | OR_ASSIGN
    ;

expression
    : assignmentExpression (COMMA assignmentExpression)*
    ;

constantExpression
    : conditionalExpression
    ;

/*Statements*/

statement
    : labeledStatement
    | declarationStatement
    | attributeSpecifierSeq? (
        expressionStatement
        | compoundStatement
        | selectionStatement
        | iterationStatement
        | jumpStatement
        | tryBlock
    )
    ;

labeledStatement
    : attributeSpecifierSeq? (IDENTIFIER | CASE constantExpression | DEFAULT) COLON statement
    ;

expressionStatement
    : expression? SEMI
    ;

compoundStatement
    : LEFT_BRACE statementSeq? RIGHT_BRACE
    ;

statementSeq
    : statement+
    ;

selectionStatement
    : IF LEFT_PAREN condition RIGHT_PAREN statement (ELSE statement)?
    | SWITCH LEFT_PAREN condition RIGHT_PAREN statement
    ;

condition
    : expression
    | attributeSpecifierSeq? declSpecifierSeq declarator (
        ASSIGN initializerClause
        | bracedInitList
    )
    ;

iterationStatement
    : WHILE LEFT_PAREN condition RIGHT_PAREN statement
    | DO statement WHILE LEFT_PAREN expression RIGHT_PAREN SEMI
    | FOR LEFT_PAREN (
        forInitStatement condition? SEMI expression?
        | forRangeDeclaration COLON forRangeInitializer
    ) RIGHT_PAREN statement
    ;

forInitStatement
    : expressionStatement
    | simpleDeclaration
    ;

forRangeDeclaration
    : attributeSpecifierSeq? declSpecifierSeq declarator
    ;

forRangeInitializer
    : expression
    | bracedInitList
    ;

jumpStatement
    : (BREAK | CONTINUE | RETURN (expression | bracedInitList)? | GOTO IDENTIFIER) SEMI
    ;

declarationStatement
    : blockDeclaration
    ;

/*Declarations*/

declarationseq
    : declaration+
    ;

declaration
    : blockDeclaration
    | functionDefinition
    | templateDeclaration
    | explicitInstantiation
    | explicitSpecialization
    | linkageSpecification
    | namespaceDefinition
    | emptyDeclaration_
    | attributeDeclaration
    ;

blockDeclaration
    : simpleDeclaration
    | asmDefinition
    | namespaceAliasDefinition
    | usingDeclaration
    | usingDirective
    | staticAssertDeclaration
    | aliasDeclaration
    | opaqueEnumDeclaration
    ;

aliasDeclaration
    : USING IDENTIFIER attributeSpecifierSeq? ASSIGN theTypeId SEMI
    ;

simpleDeclaration
    : declSpecifierSeq? initDeclaratorList? SEMI
    | attributeSpecifierSeq declSpecifierSeq? initDeclaratorList SEMI
    ;

staticAssertDeclaration
    : STATIC_ASSERT LEFT_PAREN constantExpression COMMA STRING_LITERAL RIGHT_PAREN SEMI
    ;

emptyDeclaration_
    : SEMI
    ;

attributeDeclaration
    : attributeSpecifierSeq SEMI
    ;

declSpecifier
    : storageClassSpecifier
    | typeSpecifier
    | functionSpecifier
    | FRIEND
    | TYPEDEF
    | CONSTEXPR
    ;

declSpecifierSeq
    : declSpecifier+? attributeSpecifierSeq?
    ;

storageClassSpecifier
    : REGISTER
    | STATIC
    | THREAD_LOCAL
    | EXTERN
    | MUTABLE
    ;

functionSpecifier
    : INLINE
    | VIRTUAL
    | EXPLICIT
    ;

typedefName
    : IDENTIFIER
    ;

typeSpecifier
    : trailingTypeSpecifier
    | classSpecifier
    | enumSpecifier
    ;

trailingTypeSpecifier
    : simpleTypeSpecifier
    | elaboratedTypeSpecifier
    | typeNameSpecifier
    | cvQualifier
    ;

typeSpecifierSeq
    : typeSpecifier+ attributeSpecifierSeq?
    ;

trailingTypeSpecifierSeq
    : trailingTypeSpecifier+ attributeSpecifierSeq?
    ;

simpleTypeLengthModifier
    : SHORT
    | LONG
    ;

simpleTypeSignednessModifier
    : UNSIGNED
    | SIGNED
    ;

simpleTypeSpecifier
    : nestedNameSpecifier? theTypeName
    | nestedNameSpecifier TEMPLATE simpleTemplateId
    | CHAR
    | CHAR16
    | CHAR32
    | WCHAR
    | BOOL
    | SHORT
    | INT
    | LONG
    | FLOAT
    | SIGNED
    | UNSIGNED
    | FLOAT
    | DOUBLE
    | VOID
    | AUTO
    | decltypeSpecifier
    ;

theTypeName
    : className
    | enumName
    | typedefName
    | simpleTemplateId
    ;

decltypeSpecifier
    : DECLTYPE LEFT_PAREN (expression | AUTO) RIGHT_PAREN
    ;

elaboratedTypeSpecifier
    : classKey (
        attributeSpecifierSeq? nestedNameSpecifier? IDENTIFIER
        | simpleTemplateId
        | nestedNameSpecifier TEMPLATE? simpleTemplateId
    )
    | ENUM nestedNameSpecifier? IDENTIFIER
    ;

enumName
    : IDENTIFIER
    ;

enumSpecifier
    : enumHead LEFT_BRACE (enumeratorList COMMA?)? RIGHT_BRACE
    ;

enumHead
    : enumkey attributeSpecifierSeq? (nestedNameSpecifier? IDENTIFIER)? enumbase?
    ;

opaqueEnumDeclaration
    : enumkey attributeSpecifierSeq? IDENTIFIER enumbase? SEMI
    ;

enumkey
    : ENUM (CLASS | STRUCT)?
    ;

enumbase
    : COLON typeSpecifierSeq
    ;

enumeratorList
    : enumeratorDefinition (COMMA enumeratorDefinition)*
    ;

enumeratorDefinition
    : enumerator (ASSIGN constantExpression)?
    ;

enumerator
    : IDENTIFIER
    ;

namespaceName
    : originalNamespaceName
    | namespaceAlias
    ;

originalNamespaceName
    : IDENTIFIER
    ;

namespaceDefinition
    : INLINE? NAMESPACE (IDENTIFIER | originalNamespaceName)? LEFT_BRACE namespaceBody = declarationseq? RIGHT_BRACE
    ;

namespaceAlias
    : IDENTIFIER
    ;

namespaceAliasDefinition
    : NAMESPACE IDENTIFIER ASSIGN qualifiednamespacespecifier SEMI
    ;

qualifiednamespacespecifier
    : nestedNameSpecifier? namespaceName
    ;

usingDeclaration
    : USING (TYPENAME_? nestedNameSpecifier | DOUBLECOLON) unqualifiedId SEMI
    ;

usingDirective
    : attributeSpecifierSeq? USING NAMESPACE nestedNameSpecifier? namespaceName SEMI
    ;

asmDefinition
    : ASM LEFT_PAREN STRING_LITERAL RIGHT_PAREN SEMI
    ;

linkageSpecification
    : EXTERN STRING_LITERAL (LEFT_BRACE declarationseq? RIGHT_BRACE | declaration)
    ;

attributeSpecifierSeq
    : attributeSpecifier+
    ;

attributeSpecifier
    : LEFT_BRACKET LEFT_BRACKET attributeList? RIGHT_BRACKET RIGHT_BRACKET
    | alignmentspecifier
    ;

alignmentspecifier
    : ALIGNAS LEFT_PAREN (theTypeId | constantExpression) ELLIPSIS? RIGHT_PAREN
    ;

attributeList
    : attribute (COMMA attribute)* ELLIPSIS?
    ;

attribute
    : (attributeNamespace DOUBLECOLON)? IDENTIFIER attributeArgumentClause?
    ;

attributeNamespace
    : IDENTIFIER
    ;

attributeArgumentClause
    : LEFT_PAREN balancedTokenSeq? RIGHT_PAREN
    ;

balancedTokenSeq
    : balancedtoken+
    ;

balancedtoken
    : LEFT_PAREN balancedTokenSeq RIGHT_PAREN
    | LEFT_BRACKET balancedTokenSeq RIGHT_BRACKET
    | LEFT_BRACE balancedTokenSeq RIGHT_BRACE
    | ~(LEFT_PAREN | RIGHT_PAREN | LEFT_BRACE | RIGHT_BRACE | LEFT_BRACKET | RIGHT_BRACKET)+
    ;

/*Declarators*/

initDeclaratorList
    : initDeclarator (COMMA initDeclarator)*
    ;

initDeclarator
    : declarator initializer?
    ;

declarator
    : pointerDeclarator
    | noPointerDeclarator parametersAndQualifiers trailingReturnType
    ;

pointerDeclarator
    : (pointerOperator CONST?)* noPointerDeclarator
    ;

noPointerDeclarator
    : declaratorid attributeSpecifierSeq?
    | noPointerDeclarator (
        initializer
        | parametersAndQualifiers
        | LEFT_BRACKET constantExpression? RIGHT_BRACKET attributeSpecifierSeq?
    )
    | LEFT_PAREN pointerDeclarator RIGHT_PAREN
    ;

parametersAndQualifiers
    : LEFT_PAREN parameterDeclarationClause? RIGHT_PAREN cvqualifierseq? refqualifier? exceptionSpecification? attributeSpecifierSeq?
    ;

trailingReturnType
    : ARROW trailingTypeSpecifierSeq abstractDeclarator?
    ;

pointerOperator
    : (AND | AND_AND) attributeSpecifierSeq?
    | nestedNameSpecifier? STAR attributeSpecifierSeq? cvqualifierseq?
    ;

cvqualifierseq
    : cvQualifier+
    ;

cvQualifier
    : CONST
    | VOLATILE
    ;

refqualifier
    : AND
    | AND_AND
    ;

declaratorid
    : ELLIPSIS? idExpression
    ;

theTypeId
    : typeSpecifierSeq abstractDeclarator?
    ;

abstractDeclarator
    : pointerAbstractDeclarator
    | noPointerAbstractDeclarator? parametersAndQualifiers trailingReturnType
    | abstractPackDeclarator
    ;

pointerAbstractDeclarator
    : noPointerAbstractDeclarator
    | pointerOperator+ noPointerAbstractDeclarator?
    ;

noPointerAbstractDeclarator
    : noPointerAbstractDeclarator (
        parametersAndQualifiers
        | noPointerAbstractDeclarator LEFT_BRACKET constantExpression? RIGHT_BRACKET attributeSpecifierSeq?
    )
    | parametersAndQualifiers
    | LEFT_BRACKET constantExpression? RIGHT_BRACKET attributeSpecifierSeq?
    | LEFT_PAREN pointerAbstractDeclarator RIGHT_PAREN
    ;

abstractPackDeclarator
    : pointerOperator* noPointerAbstractPackDeclarator
    ;

noPointerAbstractPackDeclarator
    : noPointerAbstractPackDeclarator (
        parametersAndQualifiers
        | LEFT_BRACKET constantExpression? RIGHT_BRACKET attributeSpecifierSeq?
    )
    | ELLIPSIS
    ;

parameterDeclarationClause
    : parameterDeclarationList (COMMA? ELLIPSIS)?
    ;

parameterDeclarationList
    : parameterDeclaration (COMMA parameterDeclaration)*
    ;

parameterDeclaration
    : attributeSpecifierSeq? declSpecifierSeq (declarator | abstractDeclarator?) (
        ASSIGN initializerClause
    )?
    ;

functionDefinition
    : attributeSpecifierSeq? declSpecifierSeq? declarator virtualSpecifierSeq? functionBody
    ;

functionBody
    : constructorInitializer? compoundStatement
    | functionTryBlock
    | ASSIGN (DEFAULT | DELETE) SEMI
    ;

initializer
    : braceOrEqualInitializer
    | LEFT_PAREN expressionList RIGHT_PAREN
    ;

braceOrEqualInitializer
    : ASSIGN initializerClause
    | bracedInitList
    ;

initializerClause
    : assignmentExpression
    | bracedInitList
    ;

initializerList
    : initializerClause ELLIPSIS? (COMMA initializerClause ELLIPSIS?)*
    ;

bracedInitList
    : LEFT_BRACE (initializerList COMMA?)? RIGHT_BRACE
    ;

/*Classes*/

className
    : IDENTIFIER
    | simpleTemplateId
    ;

classSpecifier
    : classHead LEFT_BRACE memberSpecificationList? RIGHT_BRACE
    ;

classHead
    : classKey attributeSpecifierSeq? (classHeadName classVirtSpecifier?)? baseClause?
    | UNION attributeSpecifierSeq? ( classHeadName classVirtSpecifier?)?
    ;

classHeadName
    : nestedNameSpecifier? className
    ;

classVirtSpecifier
    : FINAL
    ;

classKey
    : CLASS
    | STRUCT
    ;

memberSpecificationList: memberSpecification+;

memberSpecification
    : memberdeclaration | accessSpecifier COLON
    ;

memberdeclaration
    : attributeSpecifierSeq? declSpecifierSeq? memberDeclaratorList? SEMI
    | functionDefinition
    | usingDeclaration
    | staticAssertDeclaration
    | templateDeclaration
    | aliasDeclaration
    | emptyDeclaration_
    ;

memberDeclaratorList
    : memberDeclarator (COMMA memberDeclarator)*
    ;

memberDeclarator
    : declarator (
        virtualSpecifierSeq
        | { this.IsPureSpecifierAllowed() }? pureSpecifier
        | { this.IsPureSpecifierAllowed() }? virtualSpecifierSeq pureSpecifier
        | braceOrEqualInitializer
    )
    | declarator
    | IDENTIFIER? attributeSpecifierSeq? COLON constantExpression
    ;

virtualSpecifierSeq
    : virtualSpecifier+
    ;

virtualSpecifier
    : OVERRIDE
    | FINAL
    ;

/*
 purespecifier: ASSIGN '0'//Conflicts with the lexer ;
 */

pureSpecifier
    : ASSIGN INTEGER_LITERAL
    ;

/*Derived classes*/

baseClause
    : COLON baseSpecifierList
    ;

baseSpecifierList
    : baseSpecifier ELLIPSIS? (COMMA baseSpecifier ELLIPSIS?)*
    ;

baseSpecifier
    : attributeSpecifierSeq? (
        baseTypeSpecifier
        | VIRTUAL accessSpecifier? baseTypeSpecifier
        | accessSpecifier VIRTUAL? baseTypeSpecifier
    )
    ;

classOrDeclType
    : nestedNameSpecifier? className
    | decltypeSpecifier
    ;

baseTypeSpecifier
    : classOrDeclType
    ;

accessSpecifier
    : PRIVATE
    | PROTECTED
    | PUBLIC
    ;

/*Special member functions*/

conversionFunctionId
    : OPERATOR conversionTypeId
    ;

conversionTypeId
    : typeSpecifierSeq conversionDeclarator?
    ;

conversionDeclarator
    : pointerOperator conversionDeclarator?
    ;

constructorInitializer
    : COLON memInitializerList
    ;

memInitializerList
    : memInitializer ELLIPSIS? (COMMA memInitializer ELLIPSIS?)*
    ;

memInitializer
    : meminitializerid (LEFT_PAREN expressionList? RIGHT_PAREN | bracedInitList)
    ;

meminitializerid
    : classOrDeclType
    | IDENTIFIER
    ;

/*Overloading*/

operatorFunctionId
    : OPERATOR theOperator
    ;

literalOperatorId
    : OPERATOR (STRING_LITERAL IDENTIFIER | USER_DEFINED_STRING_LITERAL)
    ;

/*Templates*/

templateDeclaration
    : TEMPLATE LESS templateparameterList GREATER declaration
    ;

templateparameterList
    : templateParameter (COMMA templateParameter)*
    ;

templateParameter
    : typeParameter
    | parameterDeclaration
    ;

typeParameter
    : ((TEMPLATE LESS templateparameterList GREATER)? CLASS | TYPENAME_) (
        ELLIPSIS? IDENTIFIER?
        | IDENTIFIER? ASSIGN theTypeId
    )
    ;

simpleTemplateId
    : templateName LESS templateArgumentList? GREATER
    ;

templateId
    : simpleTemplateId
    | (operatorFunctionId | literalOperatorId) LESS templateArgumentList? GREATER
    ;

templateName
    : IDENTIFIER
    ;

templateArgumentList
    : templateArgument ELLIPSIS? (COMMA templateArgument ELLIPSIS?)*
    ;

templateArgument
    : theTypeId
    | constantExpression
    | idExpression
    ;

typeNameSpecifier
    : TYPENAME_ nestedNameSpecifier (IDENTIFIER | TEMPLATE? simpleTemplateId)
    ;

explicitInstantiation
    : EXTERN? TEMPLATE declaration
    ;

explicitSpecialization
    : TEMPLATE LESS GREATER declaration
    ;

/*Exception handling*/

tryBlock
    : TRY compoundStatement handlerSeq
    ;

functionTryBlock
    : TRY constructorInitializer? compoundStatement handlerSeq
    ;

handlerSeq
    : handler+
    ;

handler
    : CATCH LEFT_PAREN exceptionDeclaration RIGHT_PAREN compoundStatement
    ;

exceptionDeclaration
    : attributeSpecifierSeq? typeSpecifierSeq (declarator | abstractDeclarator)?
    | ELLIPSIS
    ;

throwExpression
    : THROW assignmentExpression?
    ;

exceptionSpecification
    : dynamicExceptionSpecification
    | noeExceptSpecification
    ;

dynamicExceptionSpecification
    : THROW LEFT_PAREN typeIdList? RIGHT_PAREN
    ;

typeIdList
    : theTypeId ELLIPSIS? (COMMA theTypeId ELLIPSIS?)*
    ;

noeExceptSpecification
    : NOEXCEPT LEFT_PAREN constantExpression RIGHT_PAREN
    | NOEXCEPT
    ;

/*Preprocessing directives*/

/*Lexer*/

theOperator
    : NEW (LEFT_BRACKET RIGHT_BRACKET)?
    | DELETE (LEFT_BRACKET RIGHT_BRACKET)?
    | PLUS
    | MINUS
    | STAR
    | DIV
    | MOD
    | CARET
    | AND
    | OR
    | TILDE
    | NOT
    | ASSIGN
    | GREATER
    | LESS
    | GREATER_EQUAL
    | PLUS_ASSIGN
    | MINUS_ASSIGN
    | STAR_ASSIGN
    | MOD_ASSIGN
    | XOR_ASSIGN
    | AND_ASSIGN
    | OR_ASSIGN
    | LESS LESS
    | GREATER GREATER
    | RIGHT_SHIFT_ASSIGN
    | LEFT_SHIFT_ASSIGN
    | EQUAL
    | NOT_EQUAL
    | LESS_EQUAL
    | AND_AND
    | OR_OR
    | PLUS_PLUS
    | MINUS_MINUS
    | COMMA
    | ARROW_STAR
    | ARROW
    | LEFT_PAREN RIGHT_PAREN
    | LEFT_BRACKET RIGHT_BRACKET
    ;

literal
    : INTEGER_LITERAL
    | CHARACTER_LITERAL
    | FLOATING_LITERAL
    | STRING_LITERAL
    | BOOLEAN_LITERAL
    | POINTER_LITERAL
    | USER_DEFINED_LITERAL
    ;
// Only for iterative parsing usage

unknownInterval
    : translationUnit
    | declaration+?
    | EOF
    ;

translationUnitOrAny
    : translationUnit
    ;

anySeq: .+?;