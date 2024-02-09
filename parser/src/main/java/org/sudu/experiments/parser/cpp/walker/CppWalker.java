package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Utils;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.TypedDecl;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;
import org.sudu.experiments.parser.cpp.model.CppBlock;
import org.sudu.experiments.parser.cpp.model.CppClass;
import org.sudu.experiments.parser.cpp.model.CppMethod;

import java.util.*;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppWalker extends CPP14ParserBaseListener {

  private final int[] tokenTypes;
  private final int[] tokenStyles;
  public final Map<Pos, Pos> usageToDefinition;

  private CppBlock currentBlock;
  public CppClass cppClass;

  private boolean isInsideParameters = false;

  public CppWalker(int[] tokenTypes, int[] tokenStyles, CppClass currentClass, Map<Pos, Pos> usageToDefinition) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDefinition = usageToDefinition;
    this.cppClass = currentClass;

    currentBlock = new CppBlock(null);
  }

  @Override
  public void enterMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.enterMemberSpecification(ctx);
    int ind = cppClass.nestPos;
    cppClass = cppClass.nestedClasses.get(ind);
  }

  @Override
  public void exitMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.exitMemberSpecification(ctx);
    cppClass = cppClass.innerClass;
    cppClass.nestPos++;
  }

  @Override
  public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
    super.enterFunctionDefinition(ctx);
    boolean isFuncDef = ctx.declSpecifierSeq() != null;
    boolean isConstructorDef = ctx.parent instanceof CPP14Parser.MemberdeclarationContext;
    if (!isFuncDef && !isConstructorDef) return;

    var node = getIdentifier(ctx.declarator());
    if (node == null) return;

    var token = node.getSymbol();
    tokenTypes[token.getTokenIndex()] = TokenTypes.METHOD;

    String name = node.getText();
    Pos pos = Pos.fromNode(node);
    String type = isFuncDef ? getType(ctx.declSpecifierSeq()) : name;
    var args = getMethodArguments(ctx.declarator());
    var argsTypes = args.stream().map(it -> it.type).toList();

    if (isFuncDef) currentBlock.methods.add(new CppMethod(name, pos, type, argsTypes));

    enterBlock();   // Args block
    currentBlock.localVars.addAll(args);
    enterBlock();   // Function body block
  }

  @Override
  public void enterMemberDeclarator(CPP14Parser.MemberDeclaratorContext ctx) {
    super.enterMemberDeclarator(ctx);
    var node = getIdentifier(ctx.declarator());
    if (node == null) return;
    tokenTypes[node.getSymbol().getTokenIndex()] = FIELD;
  }

  @Override
  public void enterParameterDeclaration(CPP14Parser.ParameterDeclarationContext ctx) {
    super.enterParameterDeclaration(ctx);
    if (!isInsideParameters) return;
    if (ctx.declSpecifierSeq() != null) {
      var type = ctx.declSpecifierSeq().getStop();
      if (type.getType() != CPP14Lexer.Identifier) return;
      tokenTypes[type.getTokenIndex()] = TYPE;
    }
  }

  @Override
  public void exitSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
    super.exitSimpleDeclaration(ctx);
    if (isNotDeclaration(ctx)) return;

    String type = getType(ctx.declSpecifierSeq());
    for (var initDecl: ctx.initDeclaratorList().initDeclarator()) {
      var node = getIdentifier(initDecl.declarator());
      if (node == null) continue;
      String name = node.getText();
      Pos pos = Pos.fromNode(node);
      currentBlock.localVars.add(new TypedDecl(name, pos, type));
    }
  }

  @Override
  public void exitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
    super.exitFunctionDefinition(ctx);
    if (ctx.declSpecifierSeq() == null) return;
    exitBlock();
    exitBlock();
  }

  @Override
  public void enterTemplateName(CPP14Parser.TemplateNameContext ctx) {
    super.enterTemplateName(ctx);
    if (!isInsideParameters) return;
    var node = ctx.Identifier().getSymbol();
    int ind = node.getTokenIndex();
    tokenTypes[ind] = TYPE;
  }

  @Override
  public void enterIterationStatement(CPP14Parser.IterationStatementContext ctx) {
    super.enterIterationStatement(ctx);
    if (ctx.For() != null) enterBlock();
  }

  @Override
  public void exitIterationStatement(CPP14Parser.IterationStatementContext ctx) {
    super.exitIterationStatement(ctx);
    if (ctx.For() != null) exitBlock();
  }

  @Override
  public void enterCompoundStatement(CPP14Parser.CompoundStatementContext ctx) {
    super.enterCompoundStatement(ctx);
    enterBlock();
  }

  @Override
  public void exitCompoundStatement(CPP14Parser.CompoundStatementContext ctx) {
    super.exitCompoundStatement(ctx);
    exitBlock();
  }

  @Override
  public void enterForRangeDeclaration(CPP14Parser.ForRangeDeclarationContext ctx) {
    super.enterForRangeDeclaration(ctx);
    var node = getIdentifier(ctx.declarator());
    if (node == null) return;

    String name = node.getText();
    Pos pos = Pos.fromNode(node);
    String type = getType(ctx.declSpecifierSeq());
    currentBlock.localVars.add(new TypedDecl(name, pos, type));
  }

  @Override
  public void enterParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx) {
    super.enterParametersAndQualifiers(ctx);
    if (isInsideMethodDeclarationArgs(ctx)) isInsideParameters = true;
  }

  @Override
  public void exitParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx) {
    super.exitParametersAndQualifiers(ctx);
    if (isInsideMethodDeclarationArgs(ctx)) isInsideParameters = false;
  }

  @Override
  public void enterSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
    super.enterSimpleDeclaration(ctx);
    if (ctx.initDeclaratorList() == null ||
    ctx.declSpecifierSeq() != null
    ) return;

    for (var initDecl: ctx.initDeclaratorList().initDeclarator()) {
      var node = getIdentifier(initDecl.declarator());
      markNode(node);
    }
  }

  @Override
  public void enterPrimaryExpression(CPP14Parser.PrimaryExpressionContext ctx) {
    super.enterPrimaryExpression(ctx);
    if (ctx.idExpression() == null) return;
    var node = getIdentifier(ctx.idExpression());
    markNode(node);
  }

  private void markNode(TerminalNode node) {
    if (node == null) return;

    var token = node.getSymbol();
    int tokenInd = token.getTokenIndex();
    String name = token.getText();
    Pos usagePos = Pos.fromNode(node);
    if (hasThis(node)) {
      boolean marked = markField(tokenInd, name, usagePos) || markMethod(tokenInd, name, usagePos);
      if (!marked) Utils.markError(tokenTypes, tokenStyles, tokenInd);
      return;
    }
    if (markLocalVar(name, usagePos)) return;
    if (markField(tokenInd, name, usagePos)) return;
    markMethod(tokenInd, name, usagePos);
  }

  private boolean markLocalVar(String name, Pos usagePos) {
    Decl decl = currentBlock.getLocalDecl(name);
    if (decl == null) decl = currentBlock.getMethod(name);
    if (decl == null || decl.position.equals(usagePos)) return false;
    usageToDefinition.put(usagePos, decl.position);
    return true;
  }

  private boolean markField(int tokenInd, String name, Pos usagePos) {
    Decl decl = cppClass.getField(name);
    if (decl == null || decl.position.equals(usagePos)) return false;
    usageToDefinition.put(usagePos, decl.position);
    tokenTypes[tokenInd] = FIELD;
    return true;
  }

  private boolean markMethod(int tokenInd, String name, Pos usagePos) {
    Decl decl = cppClass.getMethod(name);
    if (decl == null || decl.position.equals(usagePos)) return false;
    usageToDefinition.put(usagePos, decl.position);
    tokenStyles[tokenInd] = TokenStyles.BOLD;
    return true;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
    var token = node.getSymbol();
    int type = token.getType();
    int ind = token.getTokenIndex();
    /*if (type == CPP14Lexer.Identifier) visitIdentifier(node);
    else*/ if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
    else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
    else if (isComment(type)) tokenTypes[ind] = COMMENT;
    else if (isDirective(type)) tokenTypes[ind] = ANNOTATION;
    else if (isOperator(type)) tokenTypes[ind] = OPERATOR;
  }

  private void enterBlock() {
    CppBlock block = new CppBlock(currentBlock);
    currentBlock.subBlock = block;
    currentBlock = block;
  }

  private void exitBlock() {
    currentBlock = currentBlock.innerBlock;
    currentBlock.subBlock = null;
  }

  static List<TypedDecl> getArgList(CPP14Parser.ParameterDeclarationListContext ctx) {
    List<TypedDecl> result = new ArrayList<>();
    for (var paramDecl: ctx.parameterDeclaration()) {
      if (paramDecl.declarator() != null) {
        var node = getIdentifier(paramDecl.declarator());
        if (node == null) continue;
        String name = node.getText();
        Pos pos = Pos.fromNode(node);
        String type = getType(paramDecl.declSpecifierSeq());
        result.add(new TypedDecl(name, pos, type));
      }
    }
    return result;
  }

  static TerminalNode getIdentifier(CPP14Parser.DeclaratorContext ctx) {
    var noPointerDecl = ctx.pointerDeclarator() != null
        ? ctx.pointerDeclarator().noPointerDeclarator()
        : ctx.noPointerDeclarator();
    var idExpression = getDeclaratorId(noPointerDecl).idExpression();
    return getIdentifier(idExpression);
  }

  static TerminalNode getIdentifier(CPP14Parser.IdExpressionContext idExpression) {
    return idExpression.unqualifiedId() != null
        ? idExpression.unqualifiedId().Identifier()
        : idExpression.qualifiedId().unqualifiedId().Identifier();
  }

  static TerminalNode getIdentifier(CPP14Parser.ClassNameContext ctx) {
    if (ctx.Identifier() != null) return ctx.Identifier();
    else return ctx.simpleTemplateId().templateName().Identifier();
  }

  static List<TypedDecl> getMethodArguments(CPP14Parser.DeclaratorContext declaratorContext) {
    CPP14Parser.ParametersAndQualifiersContext params;
    if (declaratorContext.parametersAndQualifiers() != null) params = declaratorContext.parametersAndQualifiers();
    else if (declaratorContext.pointerDeclarator() != null &&
        declaratorContext.pointerDeclarator().noPointerDeclarator() != null &&
        declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers() != null) {
      params = declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers();
    } else return Collections.emptyList();
    if (params.parameterDeclarationClause() == null) return Collections.emptyList();

    return getArgList(params.parameterDeclarationClause().parameterDeclarationList());
  }

  static List<String> getMethodArgumentsTypes(CPP14Parser.DeclaratorContext declaratorContext) {
    return getMethodArguments(declaratorContext).stream().map(it -> it.type).toList();
  }

  static String getType(CPP14Parser.DeclSpecifierSeqContext ctx) {
    for (var decl: ctx.declSpecifier()) {
      if (decl.typeSpecifier() != null)
        return getType(decl.typeSpecifier());
    }
    return null;
  }

  static String getType(CPP14Parser.TypeSpecifierContext ctx) {
    return ctx.getText(); // todo
  }

  static boolean isNotDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
    return ctx.declSpecifierSeq() == null || ctx.initDeclaratorList() == null;
  }

  // foo() ->
  static boolean isMethodCall(CPP14Parser.SimpleDeclarationContext ctx) {
    if (ctx.declSpecifierSeq() != null ||
        ctx.attributeSpecifierSeq() != null ||
        ctx.initDeclaratorList() == null
    ) return false;
    return false;
  }

  static boolean isInsideMethodDeclarationArgs(CPP14Parser.ParametersAndQualifiersContext ctx) {
    CPP14Parser.DeclaratorContext declarator = null;
    if (ctx.parent.parent.parent instanceof CPP14Parser.DeclaratorContext parent) declarator = parent;
    if (declarator == null && ctx.parent instanceof CPP14Parser.DeclaratorContext parent) declarator = parent;

    return declarator != null && declarator.parent instanceof CPP14Parser.FunctionDefinitionContext;
  }

  static boolean hasThis(TerminalNode node) {
    if (!(node.getParent().getParent().getParent() instanceof CPP14Parser.PostfixExpressionContext postfixExpr))
      return false;
    return postfixExpr.getChildCount() >= 3
        && postfixExpr.Arrow() != null
        && postfixExpr.postfixExpression() != null
        && postfixExpr.postfixExpression().primaryExpression() != null
        && postfixExpr.postfixExpression().primaryExpression().This() != null;
  }

  static CPP14Parser.DeclaratoridContext getDeclaratorId(CPP14Parser.NoPointerDeclaratorContext ctx) {
    if (ctx.declaratorid() != null) return ctx.declaratorid();
    if (ctx.noPointerDeclarator() != null) return getDeclaratorId(ctx.noPointerDeclarator());
    else return getDeclaratorId(ctx.pointerDeclarator().noPointerDeclarator());
  }

}
