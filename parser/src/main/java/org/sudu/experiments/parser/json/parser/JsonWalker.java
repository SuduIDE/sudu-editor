package org.sudu.experiments.parser.json.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.json.gen.JsonParser;
import org.sudu.experiments.parser.json.gen.JsonParserBaseListener;

import java.util.HashMap;
import java.util.Map;

public class JsonWalker extends JsonParserBaseListener {

  final int[] tokenTypes, tokenStyles;
  final Map<Pos, Pos> usageToDefinition;
  final Map<String, Pos> fieldToDefinition;

  public JsonWalker(int[] tokenTypes, int[] tokenStyles, Map<Pos, Pos> usageToDefinition) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDefinition = usageToDefinition;
    this.fieldToDefinition = new HashMap<>();
  }

  @Override
  public void exitPair(JsonParser.PairContext ctx) {
    super.exitPair(ctx);
    String field = ctx.STRING().getText();
    if (!fieldToDefinition.containsKey(field)) {
      fieldToDefinition.put(field, Pos.fromNode(ctx.STRING()));
    } else {
      var defPos = fieldToDefinition.get(field);
      var usePos = Pos.fromNode(ctx.STRING());
      usageToDefinition.put(usePos, defPos);
    }
    mark(ctx.STRING(), ParserConstants.TokenTypes.FIELD);
  }

  void mark(TerminalNode node, int type) {
    mark(node.getSymbol(), type);
  }

  void mark(Token token, int type) {
    int ind = token.getTokenIndex();
    tokenTypes[ind] = type;
  }
}
