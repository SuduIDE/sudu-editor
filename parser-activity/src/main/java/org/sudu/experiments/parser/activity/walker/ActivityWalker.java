package org.sudu.experiments.parser.activity.walker;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.Utils;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.gen.ActivityParserBaseListener;
import org.sudu.experiments.parser.common.Pos;

import java.util.HashMap;
import java.util.Map;

public class ActivityWalker extends ActivityParserBaseListener {
  final int[] tokenTypes;
  final int[] tokenStyles;
  final Map<Pos, Pos> usageToDef;
  final Map<String, Pos> def = new HashMap<>();

  public ActivityWalker(int[] tokenTypes, int[] tokenStyles, Map<Pos, Pos> usageToDef) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDef = usageToDef;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    int index = node.getSymbol().getTokenIndex();
    Utils.markError(tokenTypes, tokenStyles, index);
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    int type = node.getSymbol().getType();
    int index = node.getSymbol().getTokenIndex();

    if (type >= ActivityLexer.ACTIVITY && type <= ActivityLexer.ELSE) {
      tokenTypes[index] = ParserConstants.TokenTypes.KEYWORD;

      if (type == ActivityLexer.DEFAULT) {
        var condBlock = (ActivityParser.CondblockContext) node.getParent();

        boolean flag = true;
        for (int i = 0; i < condBlock.exprstat().size() - 1; i++) {
          var cond = condBlock.exprstat(i);
          flag &= cond.expr() != null;
        }

        if (!flag) {
          Utils.markError(tokenTypes, tokenStyles, index);
          Utils.printError(node, "default is possible only when previous branches are with conditions");
        }
      }

    } else if (type == ActivityLexer.INT) {
      tokenTypes[index] = ParserConstants.TokenTypes.NUMERIC;

    } else if (type == ActivityLexer.SEMI) {
      tokenTypes[index] = ParserConstants.TokenTypes.SEMI;

    } else if (type == ActivityLexer.ID) {
      var id = node.getText();
      var pos = Pos.fromNode(node);
      boolean isUnderExpr = node.getParent() instanceof ActivityParser.ExprContext
          || node.getParent() instanceof ActivityParser.ExprconsContext;

      if (def.containsKey(id)) {
        usageToDef.put(pos, def.get(id));
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
      } else if (!isUnderExpr) {
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
        def.put(id, pos);
      } else {
        Utils.markError(tokenTypes, tokenStyles, index);
        Utils.printError(node, node.getText() + " is not declared");
      }
    }
  }
}
