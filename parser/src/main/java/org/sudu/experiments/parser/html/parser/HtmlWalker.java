package org.sudu.experiments.parser.html.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.Utils;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.html.gen.HTMLParser;
import org.sudu.experiments.parser.html.gen.HTMLParserBaseListener;

import java.util.*;

public class HtmlWalker extends HTMLParserBaseListener {

  final int[] tokenTypes, tokenStyles;
  final Map<Pos, Pos> usageToDefinition;
  LinkedList<Token> tagStack = new LinkedList<>();

  public HtmlWalker(int[] tokenTypes, int[] tokenStyles, Map<Pos, Pos> usageToDefinition) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDefinition = usageToDefinition;
  }

  @Override
  public void exitHtmlElement(HTMLParser.HtmlElementContext ctx) {
    super.exitHtmlElement(ctx);
    if (ctx.TAG_OPEN() != null) {
      var tag = ctx.TAG_NAME().getSymbol();
      if (ctx.TAG_SLASH() != null) {
        Token tagFound = searchForTag(tag.getText());
        if (tagFound != null) {
          mark(tag, ParserConstants.TokenTypes.ANNOTATION, ParserConstants.TokenStyles.NORMAL);
          usageToDefinition.put(Pos.fromToken(tagFound), Pos.fromToken(tag));
        }
        else {
          Utils.printError(ctx.TAG_NAME(), "Can't find open tag " + tag.getText());
          mark(ctx.TAG_NAME(), ParserConstants.TokenTypes.ERROR, ParserConstants.TokenStyles.NORMAL);
        }
      } else if (ctx.TAG_SLASH_CLOSE() == null) {
        tagStack.addLast(tag);
        mark(tag, ParserConstants.TokenTypes.ANNOTATION, ParserConstants.TokenStyles.NORMAL);
      } else {
        mark(tag, ParserConstants.TokenTypes.ANNOTATION, ParserConstants.TokenStyles.NORMAL);
      }
    }
  }

  Token searchForTag(String tag) {
    LinkedList<Token> stackCopy = (LinkedList<Token>) tagStack.clone();
    while (!tagStack.isEmpty()) {
      var popTag = tagStack.removeLast();
      if (popTag.getText().equals(tag)) return popTag;
    }
    tagStack = stackCopy;
    return null;
  }

  void mark(TerminalNode node, int type, int style) {
    if (tokenTypes == null || tokenStyles == null || node == null) return;
    mark(node.getSymbol(), type, style);
  }

  void mark(Token token, int type, int style) {
    int ind = token.getTokenIndex();
    tokenTypes[ind] = type;
    tokenStyles[ind] = style;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    Utils.markError(tokenTypes, tokenStyles, node.getSymbol().getTokenIndex());
  }
}
