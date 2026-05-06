package org.sudu.experiments.parser.help;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.help.gen.StringSplitter;

import static org.sudu.experiments.parser.ParserConstants.*;

import java.util.*;

public class Helper {

  // Languages
  public static final String JAVA     = "java";
  public static final String CPP      = "cpp";
  public static final String JS_LIGHT = "js.light";
  public static final String TS_LIGHT = "ts.light";
  public static final String ACTIVITY = "activity";
  public static final String HTML     = "html";
  public static final String JSON     = "json";
  public static final String PYTHON   = "python";

  public static List<Token> splitStringOrCharLiteral(Token token) {
    var text = CharStreams.fromString(token.getText());
    var splitter = new StringSplitter(text);
    var splitTokenStream = new CommonTokenStream(splitter);
    splitTokenStream.fill();

    List<Token> result = new ArrayList<>();
    for (var splitToken: splitTokenStream.getTokens()) {
      int splitTokenType = splitToken.getType();
      if (splitTokenType == Token.EOF) continue;

      int type = splitTokenType == StringSplitter.ESCAPE ? TokenTypes.ESCAPE_CHAR : TokenTypes.STRING;

      result.add(new SplitToken(splitToken, splitToken.getText(), type, TokenStyles.NORMAL));
    }
    return result;
  }

  public static List<Token> splitMultilineToken(Token token) {
    List<Token> result = new ArrayList<>();
    String text = token.getText();
    if (text.indexOf('\n') == -1 && text.indexOf('\r') == -1) return Collections.singletonList(token);

    StringTokenizer lineTokenizer = new StringTokenizer(text, "\n\r", true);
    String prev = null;
    while (lineTokenizer.hasMoreTokens()) {
      var line = lineTokenizer.nextToken();

      if (line.equals("\n")) {
        if (!Objects.equals(prev, "\r")) {
          result.add(new SplitToken(token, "\n", token.getType(), TokenStyles.NORMAL));
        } else {
          SplitToken last = (SplitToken) result.get(result.size() - 1);
          last.text = "\r\n";
        }
      } else if (line.equals("\r")) {
        result.add(new SplitToken(token, "\r", token.getType(), TokenStyles.NORMAL));
      } else {
        result.add(new SplitToken(token, line, token.getType(), TokenStyles.NORMAL));
      }
      prev = line;
    }
    return result;
  }
}
