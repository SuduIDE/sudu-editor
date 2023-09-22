package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.cpp.gen.help.CPP14DirectiveBaseListener;
import org.sudu.experiments.parser.cpp.gen.help.CPP14DirectiveParser;
import org.sudu.experiments.parser.cpp.gen.help.CPP14DirectiveLexer;

import static org.sudu.experiments.parser.cpp.gen.help.CPP14DirectiveParser.*;


import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.*;

public class CppDirectiveSplitter {

  public static List<Token> divideDirective(Token token) {
    CPP14DirectiveLexer directiveLexer = new CPP14DirectiveLexer(CharStreams.fromString(token.getText()));
    var directiveStream = new CommonTokenStream(directiveLexer);
    directiveStream.fill();

    var allTokens = directiveStream.getTokens();
    var result = new ArrayList<Token>();
    int[] splitTokenTypes = new int[allTokens.size()];

    CPP14DirectiveParser directiveParser = new CPP14DirectiveParser(directiveStream);
    var directive = directiveParser.directive();
    var walker = new ParseTreeWalker();
    walker.walk(new DirectiveWalker(splitTokenTypes), directive);

    int line = token.getLine() - 1, start = token.getStartIndex();
    for (var splitToken : allTokens) {
      int ind = splitToken.getTokenIndex();
      if (splitToken.getType() == EOF) continue;
      if (splitToken.getType() == CPP14DirectiveLexer.NewLine) continue;
      result.add(new SplitToken(splitToken, line, start, splitTokenTypes[ind]));
    }

    return result;
  }

  static class DirectiveWalker extends CPP14DirectiveBaseListener {

    public int[] splitTokenTypes;

    public DirectiveWalker(int[] splitTokenTypes) {
      this.splitTokenTypes = splitTokenTypes;
    }

    @Override
    public void enterInclude(IncludeContext ctx) {
      super.enterInclude(ctx);
      markToken(ctx.Hash(), TokenTypes.ANNOTATION);
      markToken(ctx.Include(), TokenTypes.ANNOTATION);
      markToken(ctx.String(), TokenTypes.STRING);
    }

    @Override
    public void enterError(ErrorContext ctx) {
      super.enterError(ctx);
      markToken(ctx.Hash(), TokenTypes.ANNOTATION);
      markToken(ctx.Error(), TokenTypes.ANNOTATION);
    }

    @Override
    public void enterDir(DirContext ctx) {
      super.enterDir(ctx);
      markToken(ctx.Hash(), TokenTypes.ANNOTATION);
      markToken(ctx.Identifier(), TokenTypes.ANNOTATION);
      markToken(ctx.Keyword(), TokenTypes.ANNOTATION);
    }

    @Override
    public void enterOther(OtherContext ctx) {
      super.enterOther(ctx);
      if (ctx.parent instanceof ErrorContext) return;
      markToken(ctx.Keyword(), TokenTypes.KEYWORD);
      markToken(ctx.Operators(), TokenTypes.OPERATOR);
      markToken(ctx.Left(), TokenTypes.OPERATOR);
      markToken(ctx.Right(), TokenTypes.OPERATOR);
      markToken(ctx.String(), TokenTypes.STRING);
      markToken(ctx.IntegerLiteral(), TokenTypes.NUMERIC);
      markToken(ctx.DecimalLiteral(), TokenTypes.NUMERIC);
      markToken(ctx.OctalLiteral(), TokenTypes.NUMERIC);
      markToken(ctx.HexadecimalLiteral(), TokenTypes.NUMERIC);
      markToken(ctx.BinaryLiteral(), TokenTypes.NUMERIC);
    }

    private void markToken(TerminalNode node, int type) {
      if (node == null) return;
      int ind = node.getSymbol().getTokenIndex();
      if (ind < 0) return;
      splitTokenTypes[ind] = type;
    }
  }

}
