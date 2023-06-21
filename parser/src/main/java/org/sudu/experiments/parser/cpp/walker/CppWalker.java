package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;
import org.sudu.experiments.parser.cpp.model.CppBlock;
import org.sudu.experiments.parser.cpp.model.CppClass;
import org.sudu.experiments.parser.cpp.model.CppMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppWalker extends CPP14ParserBaseListener {

  private final int[] tokenTypes;
  private final int[] tokenStyles;
  public final Map<Pos, Pos> usageToDefinition;

  private List<Decl> curMethodArgs;
  private CppBlock currentBlock;
  public CppClass cppClass;

  private boolean isInsideParameters = false;

  public CppWalker(int[] tokenTypes, int[] tokenStyles, CppClass currentClass, Map<Pos, Pos> usageToDefinition) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDefinition = usageToDefinition;
    this.cppClass = currentClass;

    currentBlock = new CppBlock(null);
    curMethodArgs = new ArrayList<>();
  }

  @Override
  public void enterMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.enterMemberSpecification(ctx);
    var classHead = (CPP14Parser.ClassSpecifierContext) ctx.parent;
    var className = classHead.classHead().classHeadName().className();
    var node = getIdentifier(className);
    if (node != null) currentBlock.localVars.add(Decl.fromNode(node));

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
    var node = getIdentifier(ctx.declarator());
    if (node != null) {
      var token = node.getSymbol();
      tokenTypes[token.getTokenIndex()] = TokenTypes.METHOD;
    }

    curMethodArgs = getMethodArguments(ctx.declarator());

    CppBlock block = new CppBlock(currentBlock);
    currentBlock.subBlock = block;
    currentBlock = block;
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
    if (!isInsideFunctionDeclaration(ctx)) return;
    if (ctx.declSpecifierSeq() != null) {
      var type = ctx.declSpecifierSeq().getStop();
      if (type.getType() != CPP14Lexer.Identifier) return;
      tokenTypes[type.getTokenIndex()] = TYPE;
    }
  }

  @Override
  public void exitInitDeclarator(CPP14Parser.InitDeclaratorContext ctx) {
    super.exitInitDeclarator(ctx);
    if (ctx.parent.parent.parent instanceof CPP14Parser.ForInitStatementContext) return;
    var node = getIdentifier(ctx.declarator());
    if (node != null) currentBlock.localVars.add(Decl.fromNode(node));
  }

  @Override
  public void exitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
    super.exitFunctionDefinition(ctx);
    currentBlock = currentBlock.innerBlock;
    currentBlock.subBlock = null;

    if (!(ctx.parent instanceof CPP14Parser.MemberdeclarationContext)) {
      var node = getIdentifier(ctx.declarator());
      if (node != null) {
        List<Decl> args = new ArrayList<>(curMethodArgs);
        CppMethod method = new CppMethod(node.getText(), Pos.fromNode(node), args);
        currentBlock.methods.add(method);
      }
    }

    curMethodArgs.clear();
  }

  @Override
  public void enterTemplateName(CPP14Parser.TemplateNameContext ctx) {
    super.enterTemplateName(ctx);
    if (!isInsideParameterDeclaration(ctx)) return;
    var node = ctx.Identifier().getSymbol();
    int ind = node.getTokenIndex();
    tokenTypes[ind] = TYPE;
  }

  @Override
  public void enterParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx) {
    super.enterParametersAndQualifiers(ctx);
    isInsideParameters = true;
  }

  @Override
  public void exitParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx) {
    super.exitParametersAndQualifiers(ctx);
    isInsideParameters = false;
  }

  public void visitIdentifier(TerminalNode node) {
    if (isInsideParameters) return;

    var token = node.getSymbol();
    int tokenInd = token.getTokenIndex();
    String name = token.getText();
    Pos usagePos = Pos.fromNode(node);
    if (hasThis(node)) {
      boolean marked = markField(tokenInd, name, usagePos) || markMethod(tokenInd, name, usagePos);
      if (!marked) tokenTypes[tokenInd] = ERROR;
      return;
    }
    if (markLocalVar(name, usagePos)) return;
    if (markField(tokenInd, name, usagePos)) return;
    markMethod(tokenInd, name, usagePos);
  }

  private boolean markLocalVar(String name, Pos usagePos) {
    Decl decl = currentBlock.getLocalDecl(name);
    if (decl == null) decl = getMethodArgument(name);
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
    if (type == CPP14Lexer.Identifier) visitIdentifier(node);
    else if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
    else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
    else if (isComment(type)) tokenTypes[ind] = COMMENT;
    else if (isDirective(type)) tokenTypes[ind] = ANNOTATION;
    else if (isOperator(type)) tokenTypes[ind] = OPERATOR;
  }

  private Decl getMethodArgument(String declName) {
    for (var arg: curMethodArgs) {
      if (arg.name.equals(declName)) return arg;
    }
    return null;
  }

  static List<Decl> getArgList(CPP14Parser.ParameterDeclarationListContext ctx) {
    List<Decl> result = new ArrayList<>();
    for (var paramDecl : ctx.parameterDeclaration()) {
      if (paramDecl.declarator() != null) {
        var name = getIdentifier(paramDecl.declarator());
        if (name == null) continue;
        result.add(new Decl(name.getText(), Pos.fromNode(name)));
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

  static List<Decl> getMethodArguments(CPP14Parser.DeclaratorContext declaratorContext) {
    if (declaratorContext.pointerDeclarator() == null
        || declaratorContext.pointerDeclarator().noPointerDeclarator() == null
        || declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers() == null)
      return Collections.emptyList();
    var params = declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers().parameterDeclarationClause();
    if (params == null) return Collections.emptyList();
    return getArgList(params.parameterDeclarationList());
  }

  static boolean isInsideFunctionDeclaration(CPP14Parser.ParameterDeclarationContext ctx) {
    return ctx.parent.parent.parent.parent.parent.parent.parent instanceof CPP14Parser.FunctionDefinitionContext;
  }

  static boolean isInsideParameterDeclaration(CPP14Parser.TemplateNameContext ctx) {
    return ctx.parent.parent.parent.parent.parent.parent.parent.parent.parent instanceof CPP14Parser.ParameterDeclarationContext;
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
