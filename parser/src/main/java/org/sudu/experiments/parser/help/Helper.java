package org.sudu.experiments.parser.help;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.LangToken;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.help.gen.StringSplitter;
import org.sudu.experiments.parser.html.gen.HTMLLexer;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.json.gen.JsonLexer;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;

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
      if (splitTokenType == Token.EOF) continue;

      int type = splitTokenType == StringSplitter.ESCAPE
          ? ParserConstants.TokenTypes.ESCAPE_CHAR
          : ParserConstants.TokenTypes.STRING;

      int delta = splitToken.getText().length() - (splitToken.getStopIndex() - splitToken.getStartIndex() + 1);
      int startIndex = baseStartIndex + splitToken.getStartIndex() + totalDelta;
      int stopIndex = baseStartIndex + splitToken.getStopIndex() + totalDelta + delta;
      int line = baseLine + splitToken.getLine();
      totalDelta += delta;
      result.add(new SplitToken(splitToken, splitToken.getText(), line, startIndex, stopIndex, type));
    }
    return result;
  }

  public static List<Token> splitMultilineToken(Token token) {
    List<Token> result = new ArrayList<>();
    String text = token.getText();
    if (text.indexOf('\n') == -1 && text.indexOf('\r') == -1) return Collections.singletonList(token);

    StringTokenizer lineTokenizer = new StringTokenizer(text, "\n\r", true);
    int lineNum = token.getLine();
    int start = token.getStartIndex();
    String prev = null;
    while (lineTokenizer.hasMoreTokens()) {
      var line = lineTokenizer.nextToken();

      if (line.equals("\n")) {
        if (!Objects.equals(prev, "\r")) {
          result.add(new SplitToken(token, "\n", lineNum, start, start + 1, token.getType()));
          lineNum++;
        } else {
          SplitToken last = (SplitToken) result.get(result.size() - 1);
          last.text = "\r\n";
          last.stopIndex++;
        }
      } else if (line.equals("\r")) {
        result.add(new SplitToken(token, "\r", lineNum, start, start + 1, token.getType()));
        lineNum++;
      } else {
        var splitToken = new SplitToken(token, line, lineNum, start, start + line.length() - 1, token.getType());
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
    return new SplitToken(token, token.getText(), line, startIndex, stopIndex, type);
  }

  public static int tokenDelta(Token token) {
    return token.getText().length() - (token.getStopIndex() + 1 - token.getStartIndex());
  }

  public static boolean tokenFilter(Token token) {
    if (!(token instanceof LangToken langToken)) {
      System.err.println(String.format("Token %s must be instance of LangToken", token));
      return true;
    }
    int type = token.getType();
    if (type == Token.EOF) return false;
    return switch (langToken.getLanguage()) {
      case JAVA -> javaFilter(type);
      case CPP -> cppFilter(type);
      case JS_LIGHT -> jsLightFilter(type);
      case TS_LIGHT -> tsLightFilter(type);
      case ACTIVITY -> activityFilter(type);
      case HTML -> htmlFilter(type);
      case JSON -> jsonFilter(type);
      default -> {
        System.err.println(String.format("Unexpected language for token %s", langToken));
        yield true;
      }
    };
  }

  private static boolean javaFilter(int type) {
    return type != JavaLexer.NEW_LINE;
  }

  private static boolean cppFilter(int type) {
    return type != CPP14Lexer.Newline;
  }

  private static boolean jsLightFilter(int type) {
    return type != LightJavaScriptLexer.LineTerminator;
  }

  private static boolean tsLightFilter(int type) {
    return type != LightTypeScriptLexer.LineTerminator;
  }

  private static boolean activityFilter(int type) {
    return type != 30; // ActivityLexer.NEW_LINE
  }

  private static boolean htmlFilter(int type) {
    return type != HTMLLexer.SEA_NEW_LINE
        && type != HTMLLexer.TAG_NEW_LINE;
  }

  private static boolean jsonFilter(int type) {
    return type != JsonLexer.NEWLINE;
  }
}
