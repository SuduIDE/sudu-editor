package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppWalker extends CPP14ParserBaseListener {

  private final int[] tokenTypes;
  private final int[] tokenStyles;
  public final List<Interval> intervals;
  int lastDeclarationInd = 0;

  public CppWalker(int[] tokenTypes, int[] tokenStyles) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.intervals = new ArrayList<>();
  }

  @Override
  public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.enterDeclaration(ctx);

    int stop = ctx.stop.getStopIndex() + 1;
    intervals.add(new Interval(lastDeclarationInd, stop, IntervalTypes.Cpp.DECLARATION));
    lastDeclarationInd = stop;
  }

  @Override
  public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
    super.enterFunctionDefinition(ctx);
    var identifier = getIdentifier(ctx.declarator());
    if (identifier != null) {
    var token = identifier.getSymbol();
    tokenTypes[token.getTokenIndex()] = TokenTypes.METHOD;
  }

    if (ctx.declSpecifierSeq() != null) {
      var type = ctx.declSpecifierSeq().getStart();
      tokenTypes[type.getTokenIndex()] = TYPE;
    }
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
  public void enterTemplateName(CPP14Parser.TemplateNameContext ctx) {
    super.enterTemplateName(ctx);
    if (!isInsideParameterDeclaration(ctx)) return;
    var node = ctx.Identifier().getSymbol();
    int ind = node.getTokenIndex();
    tokenTypes[ind] = TYPE;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
    var token = node.getSymbol();
    int type = token.getType();
    int ind = token.getTokenIndex();
    if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
    else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
    else if (isComment(type)) tokenTypes[ind] = COMMENT;
    else if (isDirective(type)) tokenTypes[ind] = ANNOTATION;
    else if (isOperator(type)) tokenTypes[ind] = OPERATOR;
  }

  private TerminalNode getIdentifier(CPP14Parser.DeclaratorContext ctx) {
    var noPointerDecl = ctx.pointerDeclarator() != null
        ? ctx.pointerDeclarator().noPointerDeclarator()
        : ctx.noPointerDeclarator();
    var idExpression = getDeclaratorId(noPointerDecl).idExpression();
    return getIdentifier(idExpression);
  }

  private TerminalNode getIdentifier(CPP14Parser.IdExpressionContext idExpression) {
    return idExpression.unqualifiedId() != null
        ? idExpression.unqualifiedId().Identifier()
        : idExpression.qualifiedId().unqualifiedId().Identifier();
  }

  private boolean isInsideFunctionDeclaration(CPP14Parser.ParameterDeclarationContext ctx) {
    return ctx.parent.parent.parent.parent.parent.parent.parent instanceof CPP14Parser.FunctionDefinitionContext;
  }

  private boolean isInsideParameterDeclaration(CPP14Parser.TemplateNameContext ctx) {
    return ctx.parent.parent.parent.parent.parent.parent.parent.parent.parent instanceof CPP14Parser.ParameterDeclarationContext;
  }

  private CPP14Parser.DeclaratoridContext getDeclaratorId(CPP14Parser.NoPointerDeclaratorContext ctx) {
    if (ctx.declaratorid() != null) return ctx.declaratorid();
    if (ctx.noPointerDeclarator() != null) return getDeclaratorId(ctx.noPointerDeclarator());
    if (ctx.pointerDeclarator() != null) return getDeclaratorId(ctx.pointerDeclarator().noPointerDeclarator());
    return null;
  }

}
