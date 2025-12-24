package org.sudu.experiments.parser.help;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.help.gen.StringSplitter;

import java.util.*;

public class Helper {

  public static List<Token> splitStringOrCharLiteral(Token token) {
    var text = CharStreams.fromString(token.getText());
    var splitter = new StringSplitter(text);
    var splitTokenStream = new CommonTokenStream(splitter);
    splitTokenStream.fill();

    List<Token> result = new ArrayList<>();
    int baseLine = token.getLine() - 1;
    int baseStartIndex = token.getStartIndex();
    int totalDelta = 0;
    for (var splitToken: splitTokenStream.getTokens()) {
      int splitTokenType = splitToken.getType();
      if (splitTokenType == StringSplitter.NEW_LINE
          || splitTokenType == StringSplitter.EOF
      ) continue;

      int type = splitTokenType == StringSplitter.ESCAPE
          ? ParserConstants.TokenTypes.ESCAPE_CHAR
          : ParserConstants.TokenTypes.STRING;

      int delta = splitToken.getText().length() - (splitToken.getStopIndex() - splitToken.getStartIndex() + 1);
      int startIndex = baseStartIndex + splitToken.getStartIndex() + totalDelta;
      int stopIndex = baseStartIndex + splitToken.getStopIndex() + totalDelta + delta;
      int line = baseLine + splitToken.getLine();
      totalDelta += delta;
      result.add(new SplitToken(splitToken, line, startIndex, stopIndex, type));
    }
    return result;
  }

  public static List<Token> splitMultilineToken(Token token) {
    List<Token> result = new ArrayList<>();
    String text = token.getText();
//    if (text.indexOf('\n') == -1 && text.indexOf('\r') == -1) return Collections.singletonList(token);

    StringTokenizer lineTokenizer = new StringTokenizer(text, "\n\r", true);
    int lineNum = token.getLine();
    int start = token.getStartIndex();
    String prev = null;
    while (lineTokenizer.hasMoreTokens()) {
      var line = lineTokenizer.nextToken();

      if (line.equals("\n")) {
        if (!Objects.equals(prev, "\r")) lineNum++;
      } else if (line.equals("\r")) {
        lineNum++;
      } else {
        var splitToken = new SplitToken(token, lineNum, start, start + line.length() - 1, token.getType());
        result.add(splitToken);
      }

      start += line.length();
      prev = line;
    }
    return result;
  }

  public static SplitToken mkSplitToken(
      Token token,
      int baseStartIndex, int baseLine,
      int totalDelta, int delta,
      int type
  ) {
    int startIndex = baseStartIndex + token.getStartIndex() + totalDelta;
    int stopIndex = baseStartIndex + token.getStopIndex() + totalDelta + delta;
    int line = baseLine + token.getLine();
    return new SplitToken(token, line, startIndex, stopIndex, type);
  }

  public static int tokenDelta(Token token) {
    return token.getText().length() - (token.getStopIndex() + 1 - token.getStartIndex());
  }
}
