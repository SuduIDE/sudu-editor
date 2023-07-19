package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.help.JavaStringSplitter;

import java.util.ArrayList;
import java.util.List;

public class JavaSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringOrCharLiteral, this::splitStringOrCharLiteral),
        makeRule(this::isMultiline, super::splitTokenByLine)
    );
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == JavaLexer.COMMENT
        || type == JavaLexer.TEXT_BLOCK;
  }

  private boolean isStringOrCharLiteral(Token token) {
    int type = token.getType();
    return type == JavaLexer.TEXT_BLOCK
        || type == JavaLexer.STRING_LITERAL
        || type == JavaLexer.CHAR_LITERAL;
  }

  private List<Token> splitStringOrCharLiteral(Token token) {
    JavaStringSplitter splitter = new JavaStringSplitter(CharStreams.fromString(token.getText()));
    var splitTokenStream = new CommonTokenStream(splitter);
    splitTokenStream.fill();

    ArrayList<Token> result = new ArrayList<>();
    int line = token.getLine() - 1, start = token.getStartIndex();

    for (var splitToken: splitTokenStream.getTokens()) {
      int splitTokenType = splitToken.getType();
      if (splitTokenType == JavaStringSplitter.NEW_LINE
          || splitTokenType == JavaStringSplitter.EOF
      ) continue;

      int type = splitTokenType == JavaStringSplitter.ESCAPE
          ? ParserConstants.TokenTypes.KEYWORD
          : ParserConstants.TokenTypes.STRING;

      result.add(new SplitToken(splitToken, line, start, type));
    }
    return result;
  }

}
