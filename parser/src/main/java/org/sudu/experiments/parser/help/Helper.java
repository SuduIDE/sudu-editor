package org.sudu.experiments.parser.help;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.help.gen.StringSplitter;

import java.util.ArrayList;
import java.util.List;

public class Helper {

  public static List<Token> splitStringOrCharLiteral(Token token) {
    var text = CharStreams.fromString(token.getText());
    var splitter = new StringSplitter(text);
    var splitTokenStream = new CommonTokenStream(splitter);
    splitTokenStream.fill();

    List<Token> result = new ArrayList<>();
    int line = token.getLine() - 1, start = token.getStartIndex();

    for (var splitToken: splitTokenStream.getTokens()) {
      int splitTokenType = splitToken.getType();
      if (splitTokenType == StringSplitter.NEW_LINE
          || splitTokenType == StringSplitter.EOF
      ) continue;

      int type = splitTokenType == StringSplitter.ESCAPE
          ? ParserConstants.TokenTypes.ESCAPE_CHAR
          : ParserConstants.TokenTypes.STRING;

      result.add(new SplitToken(splitToken, line, start, type));
    }
    return result;
  }
}
