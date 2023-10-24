package org.sudu.experiments.parser.activity.walker;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.gen.ActivityParserBaseListener;
import org.sudu.experiments.parser.common.Pos;

import java.util.HashMap;
import java.util.Map;

public class ActivityWalker extends ActivityParserBaseListener {
  private final int[] tokenTypes;
  private final int[] tokenStyles;
  private Map<Pos, Pos> usageToDef;
  private Map<String, Pos> def = new HashMap<>();

  private int exprStackCount = 0;


  public ActivityWalker(int[] tokenTypes, int[] tokenStyles, Map<Pos, Pos> usageToDef) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDef = usageToDef;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    int index = node.getSymbol().getTokenIndex();
    if (index < 0) return;

    tokenTypes[index] = ParserConstants.TokenTypes.ERROR;
  }


  @Override
  public void enterExpr(ActivityParser.ExprContext ctx) {
    exprStackCount ++;
  }

  @Override
  public void exitExpr(ActivityParser.ExprContext ctx) {
    exprStackCount --;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    int type = node.getSymbol().getType();
    int index = node.getSymbol().getTokenIndex();

    if (type >= ActivityLexer.ACTIVITY && type <= ActivityLexer.ELSE) {
      tokenTypes[index] = ParserConstants.TokenTypes.KEYWORD;

    } else if (type == ActivityLexer.INT) {
      tokenTypes[index] = ParserConstants.TokenTypes.NUMERIC;

    } else if (type == ActivityLexer.SEMI) {
      tokenTypes[index] = ParserConstants.TokenTypes.SEMI;

    } else if (type == ActivityLexer.ID) {
      var id = node.getText();
      var pos = Pos.fromNode(node);
      if (def.containsKey(id)) {
        usageToDef.put(pos, def.get(id));
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
      } else if (exprStackCount == 0) {
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
        def.put(id, pos);
      } else {
        tokenTypes[index] = ParserConstants.TokenTypes.ERROR;
      }
    }
  }
}