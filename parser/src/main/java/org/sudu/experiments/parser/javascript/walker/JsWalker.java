package org.sudu.experiments.parser.javascript.walker;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParser;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;
import static org.sudu.experiments.parser.javascript.parser.highlighting.JavaScriptLexerHighlighting.*;
import static org.sudu.experiments.parser.ParserConstants.*;

public class JsWalker extends JavaScriptParserBaseListener {

  private final int[] tokenTypes;
  private final int[] tokenStyles;
  public List<Interval> intervals;

  private int lastIntervalEnd = 0;

  public JsWalker(int[] tokenTypes, int[] tokenStyles) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    intervals = new ArrayList<>();
  }

  @Override
  public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
    super.enterFunctionDeclaration(ctx);
    var node = getIdentifier(ctx.identifier());
    var token = node.getSymbol();
    tokenTypes[token.getTokenIndex()] = METHOD;
  }

  @Override
  public void enterVariableDeclaration(JavaScriptParser.VariableDeclarationContext ctx) {
    super.enterVariableDeclaration(ctx);
    if (ctx.assignable().identifier() == null) return;
    var node = getIdentifier(ctx.assignable().identifier());
    var token = node.getSymbol();
    tokenTypes[token.getTokenIndex()] = FIELD;
  }

  @Override
  public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
    super.enterMethodDefinition(ctx);
    TerminalNode node;
    if (ctx.propertyName() != null) node = getPropertyName(ctx.propertyName());
    else if (ctx.getter() != null) node = getIdentifier(ctx.getter().identifier());
    else node = getIdentifier(ctx.setter().identifier());

    if (node == null) return;
    var token = node.getSymbol();
    tokenTypes[token.getTokenIndex()] = METHOD;
  }

  @Override
  public void enterSourceElement(JavaScriptParser.SourceElementContext ctx) {
    super.enterSourceElement(ctx);
    int stop = ctx.stop.getStopIndex() + 1;
    intervals.add(new Interval(lastIntervalEnd, stop, IntervalTypes.Js.SRC_ELEM));
  }

  @Override
  public void exitSourceElement(JavaScriptParser.SourceElementContext ctx) {
    super.exitSourceElement(ctx);
    lastIntervalEnd = ctx.stop.getStopIndex() + 1;
  }

  @Override
  public void enterFunctionBody(JavaScriptParser.FunctionBodyContext ctx) {
    super.enterFunctionBody(ctx);
    lastIntervalEnd = ctx.OpenBrace().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    var token = node.getSymbol();
    int ind = token.getTokenIndex();
    int type = token.getType();
    if (node.getParent() instanceof JavaScriptParser.IdentifierContext) return;
    if (isComment(type)) tokenTypes[ind] = COMMENT;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isBoolean(type)) tokenTypes[ind] = BOOLEAN;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
    else if (isString(type)) tokenTypes[ind] = STRING;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
  }

  private TerminalNode getIdentifier(JavaScriptParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.getChild(0);
  }

  private TerminalNode getIdentifier(JavaScriptParser.KeywordContext ctx) {
    if (ctx.let_() != null) return (TerminalNode) ctx.let_().getChild(0);
    else return (TerminalNode) ctx.getChild(0);
  }

  private TerminalNode getIdentifier(JavaScriptParser.IdentifierNameContext ctx) {
    if (ctx.identifier() != null) return getIdentifier(ctx.identifier());
    else {
      if (ctx.reservedWord().keyword() == null) return (TerminalNode) ctx.reservedWord().getChild(0);
      else return getIdentifier(ctx.reservedWord().keyword());
    }
  }

  private TerminalNode getPropertyName(JavaScriptParser.PropertyNameContext ctx) {
    if (ctx.StringLiteral() != null) return ctx.StringLiteral();
    if (ctx.numericLiteral() != null) return (TerminalNode) ctx.numericLiteral().getChild(0);
    if (ctx.identifierName() != null) return getIdentifier(ctx.identifierName());
    else return null;
  }

}
